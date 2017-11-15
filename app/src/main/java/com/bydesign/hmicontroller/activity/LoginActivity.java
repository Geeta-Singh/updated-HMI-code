package com.bydesign.hmicontroller.activity;
/*
* Created by PARIKSHIT...................................
* */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bydesign.hmicontroller.Service.DatabaseHandler;
import com.bydesign.hmicontroller.Service.SessionManager;
import com.bydesign.hmicontroller.model.Login_Model;
import com.bydesign.hmicontroller.model.Logs_Model;

import java.lang.reflect.InvocationTargetException;

import info.bydesign.hmicontroller.R;

public class LoginActivity extends AppCompatActivity {

    TextView username;
    TextView password;
    String u_name;
    String pass;
    Login_Model user = null;


    //Reference Variables
    DatabaseHandler databaseHandler = new DatabaseHandler(this);
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        toolbar.setTitle("Login");
        toolbar.setTitleTextColor(-1);
        DatabaseHandler db=new DatabaseHandler(this);


        db. Insert_login();
        db.InsertConversionValue();
        db.InsertParam();
        //db.defalutURL();
        db.defalutDevice();
//        SessionManager sessionManager = new SessionManager(getApplicationContext());
//        sessionManager.logoutUser();



    }

    public void onBackPressed(){

   /*     AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to close this application ?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        System.exit(-1);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
                        toolbar.setTitle("Login");
                        toolbar.setTitleTextColor(-1);
                        dialog.cancel();
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();

        //Setting the title manually
        alert.setTitle("Alert");
        alert.show();
        setContentView(R.layout.login_activity);*/
    }

//Layout's Buttons Implementation

    //Login Button Implementation
    public void Login(View view) throws InvocationTargetException {

        username = (TextView)findViewById(R.id.username);
        password = (TextView)findViewById(R.id.password);
        u_name = username.getText().toString();
        pass = password.getText().toString();

        if(isValid(u_name,pass))
        {
            Intent in = new Intent(LoginActivity.this, SimpleTabsActivity.class);

            startActivity(in);
            //push from bottom to top
            overridePendingTransition(R.animator.push_up_in, R.animator.push_up_out);
            Context c = getApplicationContext();
            Toast.makeText(c, "please wait.....", Toast.LENGTH_SHORT).show();

            //Updating Logs Factory Table
            Logs_Insertion(user);
        }
        else
        {

        }


    }


    //Forgot Password Button Implementation
    public void ForgotPassword(View view) {
        Intent in = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(in);
        //push from bottom to top
        overridePendingTransition(R.animator.push_up_in, R.animator.push_up_out);
    }


    //User Authentication
    private boolean isValid(String u_name, String pass)
    {
        user=databaseHandler.Retrieve(u_name);

        //Verifying User Details
        try
        {
            if (u_name.equals(user.getUsername()) && pass.equals(user.getPassword()))
            {
                System.out.println("password of factory "+user.getPassword()+ u_name+pass);
                Session_Management(user.getUserlevel(), user.getUsername());
                return true;
            }

            else
            {
                Toast.makeText(this, "Please Enter Correct Password", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        catch (NullPointerException exception)
        {
            Toast.makeText(this, "please give valid username and password", Toast.LENGTH_SHORT).show();
            exception.printStackTrace();
            return  false;
        }
    }


    //Session Management
    public void Session_Management(int userlevel,String username)
    {
        //Session Manager
        sessionManager = new SessionManager(this);
        sessionManager.createLoginSession(userlevel,username);
    }

    // Logs Table Insertion
    public void Logs_Insertion(Login_Model user)
    {
        Logs_Model logsModel = new Logs_Model();
        logsModel.setUsername(user.getUsername());
        logsModel.setEvent("event1");
        logsModel.setOperation_string("Login");
        databaseHandler.Insert_logs(logsModel);

    }

}

