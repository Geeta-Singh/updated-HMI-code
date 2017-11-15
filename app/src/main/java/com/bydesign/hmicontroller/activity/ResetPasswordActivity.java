package com.bydesign.hmicontroller.activity;
//Packages
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.bydesign.hmicontroller.Service.DatabaseHandler;
import com.bydesign.hmicontroller.model.Login_Model;
import com.sun.mail.smtp.SMTPAddressFailedException;

import java.net.ConnectException;
import java.net.UnknownHostException;
import javax.mail.MessagingException;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import info.bydesign.hmicontroller.R;
public class ResetPasswordActivity extends Activity  implements OnClickListener{
    DatabaseHandler databaseHandler;
    Login_Model login_model;
    Session session=null;
    ProgressDialog pdialog=null;
    Context context=null;
    String recpient = null;
    String textmessage;
    String token;
    EditText otp;
    EditText newpassword;
    EditText u_name;
    EditText a_email;
    Button submit;
    Button reset;
    Button back_reset;
    Button back_submit;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpassword_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        otp = (EditText) findViewById(R.id.otp);
        newpassword =  (EditText) findViewById(R.id.newpassword);
        reset = (Button) findViewById(R.id.reset);
        back_reset= (Button) findViewById(R.id.back_reset);
        back_submit= (Button) findViewById(R.id.back_submit);
        back_reset.setVisibility(View.INVISIBLE);
        otp.setVisibility(View.INVISIBLE);
        newpassword.setVisibility(View.INVISIBLE);
        reset.setVisibility(View.INVISIBLE);
        context=this;
        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_rp);
        toolbar.setTitle("Reset Password");
        toolbar.setTitleTextColor(-1);
        Random x = new Random();
        StringTokenizer stringTokenizer = new StringTokenizer(x.toString(),"@");
        while (stringTokenizer.hasMoreTokens()){
            token=stringTokenizer.nextToken();
        }
        //Toast.makeText(this,token,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        if(isConnected()){
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("noreplybydesign@gmail.com", "bydesign@123");
                }
            });

            pdialog = ProgressDialog.show(context, "", "Sending Mail...", true);
            SendMail sendMail = new SendMail();
            sendMail.execute();
        }else {
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }}

    class SendMail extends AsyncTask<String,Void ,String> {
        protected String doInBackground(String... urls) {
            try {
                String password;
                //Retrieving Password from Database
                u_name  = (EditText) findViewById(R.id.u_name);
                a_email = (EditText) findViewById(R.id.mail);
                final String username = u_name.getText().toString();
                final String alternate_email= a_email.getText().toString();
                databaseHandler = new DatabaseHandler(getApplicationContext());
                login_model= databaseHandler.Retrieve(username);
                //Setting Password Into String Variable password
                password = login_model.getPassword();
                if(alternate_email.isEmpty())
                {
                    recpient = login_model.getEmail();
                    Log.d("EMpty","#@##@#@#");
                }
                else
                    recpient = alternate_email;
                textmessage="Hi "+username+" this is one time password:  ";
                Log.d("RECPIENT",recpient);
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("noreplybydesign@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recpient));
                message.setSubject("Password Recovery");
                message.setContent(textmessage+token , "text/html; charset=utf-8");
                Transport.send(message);
            } catch (SendFailedException re){
                re.printStackTrace();
                return "mailfailed";
            } catch (MessagingException e){
                return "return";
            }
            catch (NullPointerException e){
                e.printStackTrace();
                return "nullpointer";
            }  catch (Exception e) {
                e.printStackTrace();
                return "remote";
            }

            return "Success";
        }
        protected void onPostExecute(String feed) {
            if(feed.equalsIgnoreCase("mailfailed")) {
                pdialog.dismiss();
                Toast.makeText(getApplicationContext(), "email id is not valid", Toast.LENGTH_LONG).show();

            }
            else if(feed.equalsIgnoreCase("nullpointer")){
                pdialog.dismiss();
                Toast.makeText(getApplicationContext(), "user not found", Toast.LENGTH_LONG).show();

            }
            else if(feed.equalsIgnoreCase("null pointer")){
                pdialog.dismiss();
                Toast.makeText(getApplicationContext(), "Recpient cannot be empty", Toast.LENGTH_LONG).show();

            }

            else if(feed.equalsIgnoreCase("return")){
                pdialog.dismiss();
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();

            }
            else {
                Toast.makeText(getApplicationContext(),"Recovery Mail Sent",Toast.LENGTH_LONG).show();
                pdialog.dismiss();
                otp = (EditText) findViewById(R.id.otp);
                newpassword =  (EditText) findViewById(R.id.newpassword);
                u_name  = (EditText) findViewById(R.id.u_name);
                a_email = (EditText) findViewById(R.id.mail);
                submit= (Button) findViewById(R.id.submit);
                u_name.setVisibility(View.INVISIBLE);
                a_email.setVisibility(View.INVISIBLE);
                submit.setVisibility(View.INVISIBLE);
                reset.setVisibility(View.VISIBLE);
                back_reset.setVisibility(View.VISIBLE);
                back_submit.setVisibility(View.INVISIBLE);
                otp.setVisibility(View.VISIBLE);
                newpassword.setVisibility(View.VISIBLE);
                reset = (Button) findViewById(R.id.reset);
                reset.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (token.equals(otp.getText().toString())&&!newpassword.getText().toString().isEmpty()) {
                            databaseHandler.Update_login(u_name.getText().toString(), newpassword.getText().toString());
                            Toast.makeText(getApplicationContext(), "password updated successfully ", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.animator.push_up_in, R.animator.push_up_out);
                        } else {
                            if(otp.getText().toString().isEmpty()||!token.equals(otp.getText().toString()))
                                Toast.makeText(getApplicationContext(), "please enter correct OTP", Toast.LENGTH_LONG).show();
                            else  if(newpassword.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(), "please enter new password", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });
            }
        }
    }
    public void back(View view){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.push_up_in, R.animator.push_up_out);
    }

    //Connection check.......................................................................

    public void ConnectionCheck(){
        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        if (isConnected()) {
            Toast.makeText(getApplicationContext(),"you are connected",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),"you are not connected",Toast.LENGTH_LONG).show();
        }

    }
    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
}