package com.bydesign.hmicontroller.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bydesign.hmicontroller.Service.DatabaseHandler;
import com.bydesign.hmicontroller.Service.SessionManager;
import com.bydesign.hmicontroller.fragments.CurrentActivityFragment;
import com.bydesign.hmicontroller.fragments.HistoricalActivityFragment;
import com.bydesign.hmicontroller.fragments.LogsFactroyFragment;
import com.bydesign.hmicontroller.fragments.SettingActivityFragment;
import com.bydesign.hmicontroller.fragments.UserSettingsFragment;
import com.bydesign.hmicontroller.model.Logs_Model;
import com.bydesign.hmicontroller.model.jsonKeyValue;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import info.bydesign.hmicontroller.R;

public class SimpleTabsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String tokenforupdate;


    TextView text;
    Thread threadreceive;

    static Handler handler;

    private static final int targetVendorID = 1204;
    private static final int targetProductID = 32849;
    private static UsbDevice deviceFound = null;
    private static UsbInterface usbInterfaceFound = null;
    private static UsbEndpoint endpointIn = null;

    DatabaseHandler databaseHandler;// = new DatabaseHandler(getApplicationContext());
    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";
    PendingIntent mPermissionIntent;

    private static UsbInterface usbInterface;
    private static UsbDeviceConnection usbDeviceConnection;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final DatabaseHandler db = new DatabaseHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_tabs);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseHandler = new DatabaseHandler(getApplicationContext());

        Button logout = (Button) findViewById(R.id.logout);
        sharedPreferences = getSharedPreferences("HmiController", MODE_PRIVATE);
        final String Username = sharedPreferences.getString("username", null);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                sessionManager.logoutUser();
                Logs_Insertion(Username);
                finish();

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        String checkSetULR = sharedPreferences.getString("token", "notSet");
        if (checkSetULR.equalsIgnoreCase("URLUDATED")) {
            //Toast.makeText(getApplicationContext(), "I called simple acitivty ", Toast.LENGTH_LONG).show();
            //  ConnectionCheck();
            SessionManager sessionManager = new SessionManager(getApplicationContext());
            sessionManager.setToken("notset");

        } else {

            //register the broadcast receiver
            mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
            IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
            registerReceiver(mUsbReceiver, filter);

            registerReceiver(mUsbDeviceReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED));
            registerReceiver(mUsbDeviceReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_DETACHED));

            connectUsb();
        }

        ConnectionCheck();


        text = (TextView) findViewById(R.id.textView);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    public void onBackPressed() {

    }

    JSONObject ObjForPost;
    JSONObject jsonObject, jsonObject1;
    JSONObject ObjForRePost = new JSONObject();
    JSONArray arrayForPost;//=new JSONArray();
    int flag = 0;

    //token fo usb data


    public JSONObject makingTokenFromUsb(String usbData) {
        DatabaseHandler db = new DatabaseHandler(this);
        ObjForPost = new JSONObject();
        jsonObject = new JSONObject();
        arrayForPost = new JSONArray();
        JSONObject obj = new JSONObject();
        JSONObject DBobj = new JSONObject();
        //**************************************

        ArrayList<jsonKeyValue> keyvalue = new ArrayList<jsonKeyValue>();
        try {
            jsonObject1 = new JSONObject(usbData);
            Iterator<String> iter = jsonObject1.keys();
            while (iter.hasNext()) {
                jsonKeyValue kv = new jsonKeyValue();
                String key = iter.next();
                if (key.equalsIgnoreCase("Stack Temp") || key.equalsIgnoreCase("Amb Temp") || key.equalsIgnoreCase("QCode") || key.equalsIgnoreCase("DM") || key.equalsIgnoreCase("O2")) {
                    System.out.print("\nkuch mt kro ");
                } else {
                    Object value = jsonObject1.get(key);
                    kv.setKey(key);
                    kv.setValue((String) value);
                    keyvalue.add(kv);
                }
            }

            Double Val;

            for (int g = 0; g < keyvalue.size(); g++) {
                System.out.println("key " + keyvalue.get(g).getKey() + "  value " + keyvalue.get(g).getValue());
                Val = db.CaltculateValues(keyvalue.get(g).getKey(), keyvalue.get(g).getValue());
                obj.put(keyvalue.get(g).getKey(), ""+Val);
                DBobj.put(keyvalue.get(g).getKey(), Val);
                System.out.print("\nval " + Val);

            }


            JSONArray jsonArray = new JSONArray();
            jsonArray.put(DBobj);
            //**********************************

            try {

                //generate current time
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String currentTimeStamp; // Find todays date
                currentTimeStamp = dateFormat.format(new java.util.Date());
                long epoch = System.currentTimeMillis();
                System.out.println("epoch is" + epoch);
                long epoch1 = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(currentTimeStamp).getTime();
                System.out.println("epoch1 is" + epoch1);
                String datefalsy = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(epoch1));
                System.out.println("date is epoch1 " + datefalsy);
                String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(epoch));
                System.out.println("date is" + date);
                System.out.println("time  current  " + date);
                //==========================================================
                // System.out.println("gi heeta " + fixValue[2]);
                jsonObject.accumulate("Qcode", jsonObject1.get("QCode"));
                jsonObject.accumulate("values", jsonArray);
                jsonObject.accumulate("TimeStamp", epoch);
                jsonObject.accumulate("DmMsg", jsonObject1.get("DM"));
                System.out.println("inside of simple" + jsonObject1.get("DM"));
                jsonObject.put("stackTemp", jsonObject1.get("Stack Temp"));
                jsonObject.put("AmbientTemp", jsonObject1.get("Amb Temp"));
                jsonObject.put("O2", jsonObject1.get("O2"));
                System.out.println("json object final : " + jsonObject);


                obj.put("O2", jsonObject1.get("O2"));
                obj.accumulate("DM", jsonObject1.get("DM"));
                obj.put("stackTemp", jsonObject1.get("Stack Temp"));
                obj.accumulate("QCode", jsonObject1.get("QCode"));
                obj.accumulate("ts", date);

                System.out.println("obj for post to server" + obj);
                ObjForPost = obj;
                System.out.println("obj for post to server" + ObjForPost);

                DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
                ObjForRePost = databaseHandler.Repost_Object_Creation();
                if (ObjForRePost == null) {

                    System.out.println("ObjectForPost is : " + arrayForPost);
                    arrayForPost.put(ObjForPost);
                    new HttpAsyncTaskUpdate().execute( databaseHandler.Get_Url() + "/smartcity/gassensor/update");

                } else {
                    System.out.println("ObjectForPost with falsy data is : " + arrayForPost + "\n post" + ObjForPost + "\n repost " + ObjForRePost);
                    arrayForPost.put(ObjForPost);
                    arrayForPost.put(ObjForRePost);
                    new HttpAsyncTaskUpdate().execute( databaseHandler.Get_Url() + "/smartcity/gassensor/update");
                }

/*POST DATA ENDED*/


            } catch (JSONException e) {
                e.printStackTrace();

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (ParseException e) {

                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("end of token.........................");
        return jsonObject;
    }
    //

    //Connection check.......................................................................

    public void ConnectionCheck() {
        // DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        if (isConnected()) {
            Toast.makeText(getApplicationContext(), "you are connected to internet", Toast.LENGTH_LONG).show();
            new HttpAsyncTask().execute(databaseHandler.Get_Url() + "/smartcity/gassensor/register");
        } else {
            Toast.makeText(getApplicationContext(), "Please check internet connection ", Toast.LENGTH_LONG).show();
        }

    }

    //Database Insertion
    public void DbInsertion(String result) {

        try {


            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            System.out.println("new object " + jsonObject + "   \nayya " + arrayForPost);
            db.InsertSensorData(jsonObject, result);
            db.InsertUpdateStatus(result, arrayForPost);
        } catch (JSONException e) {
            System.out.println("Exception in insertion " + e.toString());
        }
    }


    public void Reset_Token() {
        tokenforupdate = "";
    }



/*POST DATA STARTED*/

    public String Post(String url) {
        InputStream inputStream = null;
        String result = "";
        String status = "";
        try {

            DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
            String devid = databaseHandler.getDevice();
            System.out.print("Device id set to " + devid);
            //created HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            //made POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            //built jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("devid", devid);

            jsonObject.accumulate("values", "");

            //converted JSONObject to JSON to String
            json = jsonObject.toString();
            System.out.println("device id"+jsonObject);
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
            if (inputStream != null) {
                int i = 0;

                result = convertInputStreamToString(inputStream);
                System.out.println("GET TOKEN For UPDATE ::" + result);

                if (result.equalsIgnoreCase("Error : devid not found.")) {
                    Toast.makeText(getBaseContext(), "Please add device ", Toast.LENGTH_LONG).show();
                } else {
                    StringTokenizer st = new StringTokenizer(result, ",");
                    while (st.hasMoreTokens()) {
                        String token = st.nextToken();
                        if (i == 0) {
                            StringTokenizer st1 = new StringTokenizer(token, ":,\" ");
                            while (st1.hasMoreTokens()) {
                                tokenforupdate = st1.nextToken();
                                Session_Management1(tokenforupdate);
                                i++;
                                Log.d("TOKEN==", tokenforupdate);
                            }
                        }

                    }
                }
                Log.d("GET TOKEN ::", result);
                Log.d("TOKENWHICH@@@@", "" + tokenforupdate);
                //   Session_Management(tokenforupdate);
                //status = Update("http://180.179.236.222:8080/smartcity/gassensor/update", tt);

            } else
                tokenforupdate = "Did not work!";

        } catch (IllegalStateException e) {
            Log.d("EXception in POST", e.getLocalizedMessage());
            // Toast.makeText(getBaseContext(), "Please set Server URL", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            result = e.getLocalizedMessage();//"connection fail";
            if (e.getLocalizedMessage().equalsIgnoreCase("Host name may not be null"))
                result = e.getLocalizedMessage();//"connection failed";
            else
                result = "connection fail";
            Log.d("EXception in POST", e.getLocalizedMessage());
            // Toast.makeText(getApplicationContext(), "Please check internet connection ", Toast.LENGTH_LONG).show();
            // Toast.makeText(getBaseContext(),"Please add device "+tokenforupdate,Toast.LENGTH_LONG).show();
        }
        Log.d("Inside Post Method ", "" + tokenforupdate);
        return result;
    }

    SessionManager sessionManager;

    //Session Management
    public void Session_Management(String token) {
        //Session Manager
        sessionManager = new SessionManager(this);
        sessionManager.setToken(token);
    }

    public void Session_Management1(String token) {
        //Session Manager
        sessionManager = new SessionManager(this);
        sessionManager.setMainToken(token);
    }


    //Update Metthod to post data on the server....

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String Update(String url, String result, JSONArray arrayforPost) {
        Log.d("UPDATE METHOD CALLED", "HI I M IN UPDATE");
        InputStream inputStream = null;
        String res = "";
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        try {
            String token1 = sharedPreferences.getString("maintoken", "token not available");
            //created HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            //made POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String tok = result;
            System.out.print("URL "+url +"token check inside update" + token1);
            String json = "";
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.accumulate("token", token1);
            jsonObject2.accumulate("values", arrayforPost);

            System.out.println("***** post value**** " + jsonObject2);
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
            if (inputStream != null) {
                res = convertInputStreamToString(inputStream);
                System.out.println("GET TOKEN For UPDATE ::" + res);

                if (res.equalsIgnoreCase("Error : devid not found.")) {
                    Toast.makeText(getBaseContext(), "Please add device ", Toast.LENGTH_LONG).show();
                } else {
                    //Error 400: Token or values is missing.
                    //
//                    String S_Status = "";
//                    StringTokenizer st = new StringTokenizer(res, " ");
//                    while (st.hasMoreTokens()) {
//                        String token = st.nextToken();
//                        System.out.println("\n" + token);
//                        S_Status = token;
//                        break;
//                    }
                }


                /*
                * Tokenize res variable and get status code
                * create reference variable of databasehandler class
                * call store method and store jsonobject + res(after tokenizing)
                * */

            } else {
                res = "Did not work!";

                Log.d("IN UPDATE ELSE ", "");
                new HttpAsyncTask().execute( databaseHandler.Get_Url() + "/smartcity/gassensor/update");
            }
        } catch (Exception e) {
            if (e.getLocalizedMessage().equalsIgnoreCase("Host name may not be null"))
                res = e.getLocalizedMessage();//"connection failed";
            else
                res = "connection failed";
            e.printStackTrace();
            Log.d("IN UPDATE EXCEPTION ", "");

        }

        return res;
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
        int i = 0;

        @Override
        protected String doInBackground(String... urls) {

            return Post(urls[0]);

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String status) {
            // Toast.makeText(getBaseContext(),"Device registered with  "+tokenforupdate,Toast.LENGTH_LONG).show();
            System.out.println("@@@ ragister called " + status);
            if (status.equalsIgnoreCase("Error : devid not found."))
                Toast.makeText(getBaseContext(), "Please add device ", Toast.LENGTH_LONG).show();
            else if (status.equalsIgnoreCase("connection fail")) {
                Toast.makeText(getApplicationContext(), "Please check internet connection ", Toast.LENGTH_LONG).show();
                //new HttpAsyncTask().execute("http://" + databaseHandler.Get_Url() + "/smartcity/gassensor/register");
            } else if (status.equalsIgnoreCase("Host name may not be null")) {
                Toast.makeText(getBaseContext(), "Please add Server URL ", Toast.LENGTH_LONG).show();
            }

        }
    }

    //*new async task for update ...........*//
    //UPDATE
    private class HttpAsyncTaskUpdate extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
            System.out.print("im token for update" + tokenforupdate);
            return Update( databaseHandler.Get_Url() + "/smartcity/gassensor/update", tokenforupdate, arrayForPost);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            System.out.println("update result**********" + result);
            try {
                if (result.equalsIgnoreCase("200 OK, Update success")) {
                    Toast.makeText(getBaseContext(), "DATA STORED AND UPDATED  " + result, Toast.LENGTH_SHORT).show();
                    DbInsertion(result);


                } else if (result.equalsIgnoreCase("connection failed")) {
                    Toast.makeText(getApplicationContext(), " Please check internet connection ", Toast.LENGTH_LONG).show();
                    DbInsertion(result);
                    // new HttpAsyncTask().execute("http://" + databaseHandler.Get_Url() + "/smartcity/gassensor/register");
                } else if (result.equalsIgnoreCase("Host name may not be null")) {
                    Toast.makeText(getBaseContext(), "Please add Server URL ", Toast.LENGTH_LONG).show();
                    DbInsertion(result);
                } else if(result.equalsIgnoreCase("400 : Token Expires, register again"))
                {
                     new HttpAsyncTask().execute(databaseHandler.Get_Url() + "/smartcity/gassensor/register");
                }else {
                    Toast.makeText(getBaseContext(), " Error in updating " + result, Toast.LENGTH_SHORT).show();
                    ConnectionCheck();
                    DbInsertion(result);
                }

//                FragmentTransaction tr = getFragmentManager().beginTransaction();
//                tr.replace(R.id.your_fragment_container, yourFragmentInstance);
//                tr.commit();
//                CurrentActivityFragment currentActivityFragment=new CurrentActivityFragment();
//                currentActivityFragment.
//               // viewPager.getAdapter().notifyDataSetChanged();

            } catch (Exception e) {
                DbInsertion(result);
                //  viewPager.getAdapter().notifyDataSetChanged();
                e.printStackTrace();
            }


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


    void backtoActivity() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }


    /*......................................................................END................................................................................*/
    private void setupViewPager(final ViewPager viewPager) {
        SharedPreferences sharedPreferences = getSharedPreferences("HmiController", MODE_PRIVATE);
        int level = sharedPreferences.getInt("userlevel", 0);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CurrentActivityFragment(), "Current Data");
        adapter.addFragment(new HistoricalActivityFragment(), "Historical Data");
        adapter.addFragment(new SettingActivityFragment(), "settings");

        if (level == 3) {
            adapter.addFragment(new LogsFactroyFragment(), "Logs");
            adapter.addFragment(new UserSettingsFragment(), "User Settings");
        }
        viewPager.setAdapter(adapter);


        ViewPager.OnPageChangeListener pagechangelistener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

                viewPager.getAdapter().notifyDataSetChanged();

                viewPager.setCurrentItem(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

                Log.d("", "Called second");

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

                Log.d("Called third", "");

            }
        };
        viewPager.setOnPageChangeListener(pagechangelistener);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            System.out.println("************* position ********" + position);
            return mFragmentList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            // getItem(0)
            return POSITION_NONE;
        }


        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    public int getId(Fragment fragment) {
        return R.id.fragment;
    }

    // Logs Table Insertion
    public void Logs_Insertion(String Username) {

        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        Logs_Model logsModel = new Logs_Model();
        logsModel.setUsername(Username);
        logsModel.setEvent("event1");
        logsModel.setOperation_string("Logout");
        databaseHandler.Insert_logs(logsModel);
        //databaseHandler.Retrieve_logs();
    }

    //---------------------------------------------------usb part----------------------------------------------
    class UsbThread implements Runnable {
        @Override
        public void run() {
            //connectUsb();
            messagereceive();
        }

        public void messagereceive() {
            int startflag;
            int endflag;
            int i;
            int j;

            startflag = 0;
            endflag = 0;
            i = 0;

            if (deviceFound != null) {

                System.out.println("Inside getting data from usb &&&&&&&&&&&&&&&&");  //   String name = edit.getText().toString();
                final int usbResult;

                for (; ; ) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {

                        byte[] buffer = new byte[64];
                        final StringBuilder str1 = new StringBuilder();
                        while (endflag == 0) {
                            i = 0;
                            if ((usbDeviceConnection.bulkTransfer(endpointIn, buffer, 64, 500)) >= 0) {
                                if (buffer[i] == '{') {
                                    startflag = 1;

                                    for (i = 0; i < 64; i++) {
                                        str1.append((char) buffer[i]);
                                        if (buffer[i] == '}') {
                                            endflag = 1;
                                            //start_delim_flag = 0;
                                            break;
                                        }
                                    }
                                } else {
                                    if (startflag == 1) {
                                        for (i = 0; i < 64; i++) {
                                            str1.append((char) buffer[i]);
                                            if (buffer[i] == '}') {
                                                endflag = 1;
                                                //start_delim_flag = 0;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if ((endflag == 1) && (startflag == 1)) {
                            endflag = 0;
                            startflag = 0;
                            i = 0;

                            Message msgObj = handler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putString("message", String.valueOf(str1));
                            msgObj.setData(b);
                            System.out.println("msg get from usb" + msgObj);
                            handler.sendMessage(msgObj);
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }//for loop end
            }
        }
    }


    @Override
    protected void onDestroy() {
        releaseUsb();
        unregisterReceiver(mUsbReceiver);
        unregisterReceiver(mUsbDeviceReceiver);
        super.onDestroy();
    }

    private void connectUsb() {

        Toast.makeText(SimpleTabsActivity.this, "connecting to Usb", Toast.LENGTH_LONG).show();
        //  textStatus.setText("connectUsb()");

        searchEndPoint();

        if (usbInterfaceFound != null) {
            setupUsbComm();
            //usb handling
            threadreceive = new Thread(new UsbThread());
            threadreceive.start();

            handler = new Handler() {
                public void handleMessage(Message msg) {
                    String USB = "";
                    String aResponse = msg.getData().getString("message");
                    Toast.makeText(SimpleTabsActivity.this, aResponse, Toast.LENGTH_LONG).show();
                    System.out.println("usb : " + aResponse + " \n length " + aResponse.length());
                    jsonObject = makingTokenFromUsb(aResponse);
                    System.out.println("json boject from making token" + jsonObject);
                }
            };
        }

    }

    private void releaseUsb() {

        Toast.makeText(SimpleTabsActivity.this,
                "releaseUsb()",
                Toast.LENGTH_LONG).show();
        //  textStatus.setText("releaseUsb()");

        if (usbDeviceConnection != null) {
            if (usbInterface != null) {
                usbDeviceConnection.releaseInterface(usbInterface);
                usbInterface = null;
            }
            usbDeviceConnection.close();
            usbDeviceConnection = null;
        }

        deviceFound = null;
        usbInterfaceFound = null;
        endpointIn = null;

    }

    private void searchEndPoint() {


        usbInterfaceFound = null;

        endpointIn = null;

        //Search device for targetVendorID and targetProductID
        if (deviceFound == null) {
            UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
            HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
            Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

            while (deviceIterator.hasNext()) {
                UsbDevice device = deviceIterator.next();

                if (device.getVendorId() == targetVendorID) {
                    if (device.getProductId() == targetProductID) {
                        deviceFound = device;
                    }
                }
            }
        }

        if (deviceFound == null) {
            Toast.makeText(SimpleTabsActivity.this,
                    "device not found",
                    Toast.LENGTH_LONG).show();
            // textStatus.setText("device not found");
        } else {
            String s = deviceFound.toString() + "\n" + "DeviceName: " + deviceFound.getDeviceName() + "\n";
            // textInfo.setText(s);

            //Toast.makeText(SimpleTabsActivity.this, s, Toast.LENGTH_LONG).show();
            //Search for UsbInterface with Endpoint of USB_ENDPOINT_XFER_BULK,
            //and direction USB_DIR_OUT and USB_DIR_IN

            for (int i = 0; i < deviceFound.getInterfaceCount(); i++) {
                UsbInterface usbif = deviceFound.getInterface(i);

                UsbEndpoint tOut = null;
                UsbEndpoint tIn = null;

                int tEndpointCnt = usbif.getEndpointCount();
                if (tEndpointCnt >= 2) {
                    for (int j = 0; j < tEndpointCnt; j++) {
                        if (usbif.getEndpoint(j).getType() ==
                                UsbConstants.USB_ENDPOINT_XFER_BULK) {
                            if (usbif.getEndpoint(j).getDirection() ==
                                    UsbConstants.USB_DIR_OUT) {
                                tOut = usbif.getEndpoint(j);
                            } else if (usbif.getEndpoint(j).getDirection() ==
                                    UsbConstants.USB_DIR_IN) {
                                tIn = usbif.getEndpoint(j);
                            }
                        }
                    }

                    if (tOut != null && tIn != null) {

                        usbInterfaceFound = usbif;

                        endpointIn = tIn;
                    }
                }

            }

            if (usbInterfaceFound == null) {
                Toast.makeText(SimpleTabsActivity.this, "No suitable interface found!", Toast.LENGTH_LONG).show();

            } else {

            }
        }
    }

    private boolean setupUsbComm() {

        //for more info, search SET_LINE_CODING and
        //SET_CONTROL_LINE_STATE in the document:
        //"Universal Serial Bus Class Definitions for Communication Devices"
        //at http://adf.ly/dppFt
        final int RQSID_SET_LINE_CODING = 0x20;
        final int RQSID_SET_CONTROL_LINE_STATE = 0x22;

        boolean success = false;

        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        Boolean permitToRead = manager.hasPermission(deviceFound);

        if (permitToRead) {
            usbDeviceConnection = manager.openDevice(deviceFound);
            if (usbDeviceConnection != null) {
                usbDeviceConnection.claimInterface(usbInterfaceFound, true);

                //showRawDescriptors();	//skip it if you no need show RawDescriptors

                int usbResult;
                usbResult = usbDeviceConnection.controlTransfer(
                        0x21,                            //requestType
                        RQSID_SET_CONTROL_LINE_STATE,    //SET_CONTROL_LINE_STATE
                        0,                //value
                        0,                //index
                        null,            //buffer
                        0,                //length
                        0);                //timeout

               /* Toast.makeText(SimpleTabsActivity.this,
                        "controlTransfer(SET_CONTROL_LINE_STATE): " + usbResult,
                        Toast.LENGTH_LONG).show();
*/
                //baud rate = 9600
                //8 data bit
                //1 stop bit
                byte[] encodingSetting =
                        new byte[]{(byte) 0x80, 0x25, 0x00, 0x00, 0x00, 0x00, 0x08};
                usbResult = usbDeviceConnection.controlTransfer(
                        0x21,                        //requestType
                        RQSID_SET_LINE_CODING,        //SET_LINE_CODING
                        0,                    //value
                        0,                    //index
                        encodingSetting,    //buffer
                        7,                    //length
                        0);                    //timeout
               /* Toast.makeText(SimpleTabsActivity.this,
                        "controlTransfer(RQSID_SET_LINE_CODING): " + usbResult,
                        Toast.LENGTH_LONG).show();*/

				/*byte[] bytesHello =
                        new byte[] {(byte)'H', 'e', 'l', 'l', 'o', ' ',
						'f', 'r', 'o', 'm', ' ',
						'A', 'n', 'd', 'r', 'o', 'i', 'd'};
				usbResult = usbDeviceConnection.bulkTransfer(
						endpointOut,
						bytesHello,
						bytesHello.length,
						0);
				Toast.makeText(MainActivity.this,
						"bulkTransfer: " + usbResult,
						Toast.LENGTH_LONG).show();*/
            }

        } else {
            manager.requestPermission(deviceFound, mPermissionIntent);
           /* Toast.makeText(SimpleTabsActivity.this,
                    "Permission: " + permitToRead,
                    Toast.LENGTH_LONG).show();*/
            // textStatus.setText("Permission: " + permitToRead);
        }


        return success;
    }


    private final BroadcastReceiver mUsbReceiver =
            new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (ACTION_USB_PERMISSION.equals(action)) {

                        Toast.makeText(SimpleTabsActivity.this,
                                "ACTION_USB_PERMISSION",
                                Toast.LENGTH_LONG).show();
                        //     textStatus.setText("ACTION_USB_PERMISSION");

                        synchronized (this) {
                            UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                            if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                                if (device != null) {
                                    // connectUsb();
                                }
                            } else {
                                Toast.makeText(SimpleTabsActivity.this,
                                        "permission denied for device " + device,
                                        Toast.LENGTH_LONG).show();
                                //textStatus.setText("permission denied for device " + device);
                            }
                        }
                    }
                }
            };

    private final BroadcastReceiver mUsbDeviceReceiver =
            new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {

                        deviceFound = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        Toast.makeText(SimpleTabsActivity.this,
                                "ACTION_USB_DEVICE_ATTACHED: \n" +
                                        deviceFound.toString(),
                                Toast.LENGTH_LONG).show();
                        //  textStatus.setText("ACTION_USB_DEVICE_ATTACHED: \n" +
                        //        deviceFound.toString());

                         connectUsb();

                    } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {

                        UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                        Toast.makeText(SimpleTabsActivity.this,
                                "ACTION_USB_DEVICE_DETACHED: \n" +
                                        device.toString(),
                                Toast.LENGTH_LONG).show();
                        // textStatus.setText("ACTION_USB_DEVICE_DETACHED: \n" +
                        //       device.toString());

                        if (device != null) {
                            if (device == deviceFound) {
                                releaseUsb();
                            }
                        }

                        //  textInfo.setText("");
                    }
                }

            };


    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private FragmentRefreshListener fragmentRefreshListener;

    public interface FragmentRefreshListener {
        void onRefresh();
    }
}
