package com.bydesign.hmicontroller.Service;

import android.annotation.TargetApi;
import android.app.Activity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.StringTokenizer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bydesign.hmicontroller.activity.SimpleTabsActivity;

import info.bydesign.hmicontroller.R;


/**
 * Created by PARIKSHIT on 1/14/2016.
 */
public class PostData extends Activity{

    Button btnPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_activity);
// check if you are connected or not
        if (isConnected()) {
            Toast.makeText(this,"you are connected",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,"you are connected",Toast.LENGTH_LONG).show();
        }
        // call AsyncTask to perform network operation on separate thread
        new HttpAsyncTask().execute("http://180.179.236.222:8080/smartcity/gassensor/register");

        // add click listener to Button "POST"
        // btnPost.setOnClickListener(this);

    }

    public String Post(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            //created HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            //made POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            //built jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("devid", "Parikshit_dev");
            jsonObject.accumulate("values","");

            //converted JSONObject to JSON to String
            json = jsonObject.toString();

            //json to StringEntity
            StringEntity se = new StringEntity(json);

            //set httpPost Entity
            httpPost.setEntity(se);

            //Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            //Executed POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            //received response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            //converted inputstream to string
            if (inputStream != null)
            {
                int i=0;
                String tt=null;
                result = convertInputStreamToString(inputStream);
                StringTokenizer st = new StringTokenizer(result,",");
                while(st.hasMoreTokens()){
                    String token=st.nextToken();
                    if(i==0) {
                        StringTokenizer st1 = new StringTokenizer(token, ":, ");
                        while (st1.hasMoreTokens()) {
                            tt = st1.nextToken();
                            i++;
                            Log.d("TOKEN==",tt);
                        }
                    }

                }

                Log.d("GET TOKEN ::", result);
                Log.d("TOKENWHICH@@@@",tt);
                Update("http://180.179.236.222:8080/smartcity/gassensor/update", tt);

            }
            else
                result = "Did not work!";

        } catch (Exception e)
        {
            Log.d("InputStream2345", e.getLocalizedMessage());
        }

        return result;
    }

    //Update Metthod to post data on the server....

    @TargetApi(Build.VERSION_CODES.KITKAT)

    public void Update(String url, String result){
        Log.d("UPDATE METHOD CALLED","HI I M IN UPDATE");
        InputStream inputStream = null;
        String res = "";
        try {

            //created HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            //made POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";
            JSONObject jsonObject = new JSONObject();
            try {
                JSONObject obj=new JSONObject();
                obj.accumulate("co","12");
                obj.accumulate("So2", "3.1");
                obj.put("No", "8.2");
                obj.put("Co2","6.3");
                obj.put("No2","7.3");
                System.out.print("array objec: " + obj);
                //JsonArray
                JSONArray jsonArray =new JSONArray();
                jsonArray.put(obj);
                System.out.println("...json array is : " + jsonArray);

                jsonObject.accumulate("values", jsonArray);
                //  jsonObject.accumulate("status", status);
                //  jsonObject.accumulate("TimeStamp", new Timestamp(date.getTime()));
                jsonObject.accumulate("Qcode", "good");
                jsonObject.accumulate("DmMsg", "ok");
                System.out.print("json object : "+jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //JsonArray
            JSONArray jsonArray =new JSONArray();
            jsonArray.put(jsonObject);
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.accumulate("token", result);
            jsonObject2.accumulate("values", jsonArray);

            //converted JSONObject to JSON to String
            json = jsonObject2.toString();

            //json to StringEntity
            StringEntity se = new StringEntity(json);

            //set httpPost Entity
            httpPost.setEntity(se);

            //Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            //Executed POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            //received response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            //converted inputstream to string
            if (inputStream != null)
            {
                res = convertInputStreamToString(inputStream);
                Log.d("GET TOKEN For UPDATE ::", res);

                String S_Status="";
                StringTokenizer st = new StringTokenizer(res, " ");
                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    System.out.println("\n" + token);
                    S_Status = token;
                    break;
                }
                   jsonObject.put("status", S_Status);
                System.out.print("json object in post data " + jsonObject);
                DatabaseHandler databaseHandler=new DatabaseHandler(this);

                /*
                * Tokenize res variable and get status code
                * create reference variable of databasehandler class
                * call store method and store jsonobject + res(after tokenizing)
                * */

            }
            else
                res = "Did not work!";

        } catch (Exception e)
        {
            Log.d("InputStream2345", e.getLocalizedMessage());
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


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return Post(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_SHORT).show();
            Toast.makeText(getBaseContext(),"DATA STORED AND UPDATED",Toast.LENGTH_LONG).show();
            //intent post to sample tab activity

        }
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}