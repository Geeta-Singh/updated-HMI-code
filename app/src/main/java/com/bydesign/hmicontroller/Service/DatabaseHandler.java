package com.bydesign.hmicontroller.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.bydesign.hmicontroller.model.CurrentDataModel;
import com.bydesign.hmicontroller.model.GainOffset;
import com.bydesign.hmicontroller.model.Login_Model;
import com.bydesign.hmicontroller.model.LogsModel;
import com.bydesign.hmicontroller.model.Logs_Model;
import com.bydesign.hmicontroller.model.ParameterForGaugeView;
import com.bydesign.hmicontroller.model.Settings_Model;
import com.bydesign.hmicontroller.model.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DatabaseHandler extends SQLiteOpenHelper {


    protected static int database_Version = 23;
    protected static String database_Name = "Final_HmiControllerdb";
    protected static String create_Table_Login = "CREATE  TABLE login (Uid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, Uname VARCHAR(100) NOT NULL UNIQUE, Password VARCHAR(100) NOT NULL,Email VARCHAR(100) NOT NULL ,Mobile VARCHAR(10) , Userlevel INTEGER(1) NOT NULL, Fname VARCHAR NOT NULL);";
    protected static String create_Table_Logs = "CREATE  TABLE logs (Srno INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , Ts TEXT NOT NULL , User VARCHAR(100) NOT NULL , Opcode VARCHAR NOT NULL , OperationString TEXT NOT NULL, FOREIGN KEY(User) REFERENCES login(Uname)); ";
    protected static String create_Table_ParameterValues = "CREATE  TABLE \"parametervalues\" (\"Srno\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"ParamName\" VARCHAR NOT NULL UNIQUE, \"Range\" VARCHAR NOT NULL , \"Alarm1\" DOUBLE NOT NULL , \"Alarm2\" DOUBLE NOT NULL , \"Gain\" DOUBLE NOT NULL , \"Offset\" DOUBLE NOT NULL , \"Resolution\" VARCHAR NOT NULL , \"Unit\" VARCHAR NOT NULL ,\"Mode\" VARCHAR NOT NULL , FOREIGN KEY(ParamName) REFERENCES conversionValue(ParamName));";
    protected static String create_Table_SensorData = "CREATE  TABLE \"sensordata\" (\"Srno\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"ParamName\" VARCHAR NOT NULL , \"Value\" DOUBLE NOT NULL , \"Ts\" INTEGER NOT NULL ,\"Status\" VARCHAR NOT NULL , \"DiagnosticMsg\" VARCHAR NOT NULL , \"Qcode\" VARCHAR NOT NULL,\"O2\" DOUBLE NOT NULL,\"Co2\" DOUBLE NOT NULL,\"StackTemp\" VARCHAR NOT NULL,\"AmbientTemp\" VARCHAR NOT NULL , FOREIGN KEY(ParamName) REFERENCES login(ParamName) );";
    protected static String create_Table_ConversionValue = "CREATE  TABLE \"conversionValue\" (\"Srno\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"ParamName\" VARCHAR NOT NULL UNIQUE ,\"conversionValue\" DOUBLE NOT NULL);";
    protected static String url = "CREATE  TABLE \"url\" (\"Srno\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"Url\" VARCHAR NOT NULL UNIQUE ,\"flag\"INTEGER(1) NOT NULL);";
    protected static String create_Table_Device = "CREATE  TABLE device (Uid VARCHAR(100) PRIMARY KEY NOT NULL UNIQUE, devid VARCHAR(100) NOT NULL UNIQUE );";

    //constructor
    public DatabaseHandler(Context context) {
        super(context, database_Name, null, database_Version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_Table_Login);
        db.execSQL(create_Table_Logs);
        db.execSQL(create_Table_ParameterValues);
        db.execSQL(create_Table_SensorData);
        db.execSQL(create_Table_ConversionValue);
        db.execSQL(url);
        db.execSQL(create_Table_Device);
        System.out.print("Database created...................");
        Log.d("DATABASE CREATED VE ", String.valueOf(database_Version));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS login;");
        db.execSQL("DROP TABLE IF EXISTS logs;");
        db.execSQL("DROP TABLE IF EXISTS parametervalues;");
        db.execSQL("DROP TABLE IF EXISTS sensordata;");
        db.execSQL("DROP TABLE IF EXISTS conversionValue;");
        db.execSQL("DROP TABLE IF EXISTS url;");
        db.execSQL("DROP TABLE IF EXISTS device;");

        onCreate(db);
    }

//==========================================================================================

    Cursor cursor = null;
    SQLiteDatabase db;

    public void InsertConversionValue() {

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put("ParamName", "co");
        values.put("Conversionvalue", 1.23);
        db.insert("conversionValue", null, values);

        values.put("ParamName", "so2");
        values.put("Conversionvalue", 2.82);
        db.insert("conversionValue", null, values);

        values.put("ParamName", "no");
        values.put("Conversionvalue", 1.32);
        db.insert("conversionValue", null, values);

        values.put("ParamName", "cl2");
        values.put("Conversionvalue", 3);
        db.insert("conversionValue", null, values);

        values.put("ParamName", "no2");
        values.put("Conversionvalue", 2.03);
        db.insert("conversionValue", null, values);

        values.put("ParamName", "hc");
        values.put("Conversionvalue", 0.706);
        db.insert("conversionValue", null, values);

        values.put("ParamName", "o3");
        values.put("Conversionvalue", 2.11);
        db.insert("conversionValue", null, values);

        values.put("ParamName", "n2");
        values.put("Conversionvalue", 1.23);
        db.insert("conversionValue", null, values);

        values.put("ParamName", "nh3");
        values.put("Conversionvalue", 0.75);
        db.insert("conversionValue", null, values);

        values.put("ParamName", "hcl");
        values.put("Conversionvalue", 1.5);
        db.insert("conversionValue", null, values);

        values.put("ParamName", "Ne");
        values.put("Conversionvalue", 0.889);
        db.insert("conversionValue", null, values);

        values.put("ParamName", "Kr");
        values.put("Conversionvalue", 3.69);
        db.insert("conversionValue", null, values);

        values.put("ParamName", "He");
        values.put("Conversionvalue", 0.176);
        db.insert("conversionValue", null, values);

        values.put("ParamName", "Ar");
        values.put("Conversionvalue", 1.76);
        db.insert("conversionValue", null, values);

        values.put("ParamName", "HF");
        values.put("Conversionvalue", 0.881);
        db.insert("conversionValue", null, values);

        values.put("ParamName", "PH3");
        values.put("Conversionvalue", 1.3);
        db.insert("conversionValue", null, values);

        values.put("ParamName", "H2");
        values.put("Conversionvalue", 0.0889);
        db.insert("conversionValue", null, values);

        values.put("ParamName", "nox");
        values.put("Conversionvalue", 1.94);
        db.insert("conversionValue", null, values);

        values.put("ParamName", "ch4");
        values.put("Conversionvalue", 0.706);
        db.insert("conversionValue", null, values);

        values.put("ParamName", "h2s");
        values.put("Conversionvalue", 1.5);
        db.insert("conversionValue", null, values);

        values.put("ParamName", "opacity");
        values.put("Conversionvalue", 1.65);
        db.insert("conversionValue", null, values);

        db.close();

    }

    public void InsertParam() {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put("ParamName", "so2");
        values.put("Range", "45");
        values.put("Alarm1", 400);
        values.put("Alarm2", 1000);
        values.put("Gain", 1);
        values.put("Offset", 1);
        values.put("Resolution", "321");
        values.put("Unit", "mg");
        values.put("Mode", "Active");

        db.insert("parametervalues", null, values);

        values.put("ParamName", "no");
        values.put("Range", "45");
        values.put("Alarm1", 1000);
        values.put("Alarm2", 2000);
        values.put("Gain", 1);
        values.put("Offset", 1);
        values.put("Resolution", "321");

        values.put("Unit", "mg");
        values.put("Mode", "Active");

        db.insert("parametervalues", null, values);

        values.put("ParamName", "cl2");
        values.put("Range", "45");
        values.put("Alarm1", 1000);
        values.put("Alarm2", 2000);
        values.put("Gain", 1);
        values.put("Offset", 1);
        values.put("Resolution", "321");

        values.put("Unit", "ppm");
        values.put("Mode", "Active");

        db.insert("parametervalues", null, values);

        values.put("ParamName", "no2");
        values.put("Range", "45");
        values.put("Alarm1", 400);
        values.put("Alarm2", 1000);
        values.put("Gain", 1);
        values.put("Offset", 1);
        values.put("Resolution", "321");

        values.put("Unit", "pm");
        values.put("Mode", "Active");
        db.insert("parametervalues", null, values);


        values.put("ParamName", "hcl");
        values.put("Range", "45");
        values.put("Alarm1", 1000);
        values.put("Alarm2", 2000);
        values.put("Gain", 1);
        values.put("Offset", 1);
        values.put("Resolution", "321");

        values.put("Unit", "pm");
        values.put("Mode", "Active");

        db.insert("parametervalues", null, values);

        values.put("ParamName", "nh3");
        values.put("Range", "45");
        values.put("Alarm1", 1000);
        values.put("Alarm2", 2000);
        values.put("Gain", 1);
        values.put("Offset", 1);
        values.put("Resolution", "321");

        values.put("Unit", "pm");
        values.put("Mode", "Active");

        db.insert("parametervalues", null, values);

        db.close();
    }

    //calculate value of parameter
    public Double CaltculateValues(String param, String v) {
        System.out.println("inside calculate:" + param);
        Double Val = 0.0;
        try {
            ArrayList<GainOffset> gf = getGainAndOffset(param);
            // System.out.print("for chacking : "+gf.get(0).getOffset());
            //  System.out.print("for chacking : "+gf.get(0).getGain());
            Val = gf.get(0).getGain() * Double.parseDouble(v) + gf.get(0).getOffset();

            System.out.print("vaaal in calculate : " + Val);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            ;
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }
        return Val;
    }


    //insert into sensor table
    public void InsertSensorData(JSONObject jsonObject, String S_Status) throws JSONException {

        System.out.print("inside insert into data" + S_Status);
        //S_Status="200 ok";
        //   String json = "";
        // get number of parameter in parammeter table
        ArrayList<String> ActiveparamItem = getActiveParam();
        ArrayList<String> DeactiveparamItem = getDeActiveParam();
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        int size = ActiveparamItem.size();
        int Dsize = DeactiveparamItem.size();
        //   System.out.print("inside insert of sensor :" + jsonObject);
        // extract data from json object
        try {
            //converted JSONObject to JSON to String
            // json = jsonObject.toString();
            String DmMsg = jsonObject.getString("DmMsg");
            String Qcode = jsonObject.getString("Qcode");
            String Ts = jsonObject.getString("TimeStamp");
            // String S_Status = jsonObject.getString("status");
            String stackTemp = jsonObject.getString("stackTemp");
            String AmbientTemp = jsonObject.getString("AmbientTemp");
            Double O2 = jsonObject.getDouble("O2");
            //   JSONArray jsonArray = jsonObject.optJSONArray("values");
            //  System.out.print("dmmsg:" + DmMsg + "\nqcose:" + Qcode + "\nts:" + Ts + "\n startus: " + S_Status);
            JSONArray msg = (JSONArray) jsonObject.get("values");
            String p = msg.getString(0);
            // System.out.println(" value :" + p);

            System.out.println("json object insert : " + jsonObject);


            // System.out.println("  Json print: " + json + "\n");

            //get status-----------------
            StringTokenizer st = new StringTokenizer(S_Status, " ");
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                // System.out.println("\n" + token);
                S_Status = token;
                break;
            }

            if (S_Status.equalsIgnoreCase("200")) {
                S_Status = "true";
            } else
                S_Status = "false";


            Double Co2 = (1 - O2 / 21);
            System.out.println("\nvalue of co2 : " + Co2);

            // System.out.println("size of param:" + size);
            //param name and value fron json array
            System.out.println("\nstatus is :" + S_Status);


            String check = "none";
            long epoch = 0;
            ArrayList<Long> timestamp_array = Historydata();
            if (timestamp_array.contains(epoch)) {
                System.out.println("inside checking");
                check = "success";
            }
            System.out.println("dfhdjfbjsdfbh" + timestamp_array.size());
           /* for (int t = 0; t < timestamp_array.size(); t++) {
                try {
                    System.out.println("Ts " + Ts);
                    epoch = Long.parseLong(Ts);
                    System.out.println("time stam inert  " + epoch  +"Ts   "+timestamp_array.get(t));//1456786977 1456787930
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                if (timestamp_array.contains(epoch)) {
                    System.out.println("inside checking");
                    check = "success";
                    break;
                }

            }*/

            System.out.println("outside loop " + check);
            if (check.equalsIgnoreCase("success")) {
                System.out.println("@@@@@@@@@@@@@@@@@@@@duplicate values come for time stamp$$$$$$$$$$$$$$$$");
            } else {

                for (int i = 0; i < size; i++) {
                    //System.out.println(paramItem.get(i)+"......i.....:"+i);
                    String co = msg.getJSONObject(0).optString(ActiveparamItem.get(i));
                    System.out.println("param ? :" + co);


                    System.out.println(" \nvalue : " + co + " for param :" + ActiveparamItem.get(i) + "\n");
                    if (co.isEmpty()) {
                        co = "0";
                    }
                    values.put("Ts", Ts);
                    values.put("O2", O2);
                    values.put("Co2", Co2);
                    values.put("StackTemp", stackTemp);
                    values.put("AmbientTemp", AmbientTemp);
                    values.put("Value", co);
                    values.put("ParamName", ActiveparamItem.get(i));
                    values.put("Status", S_Status);
                    values.put("DiagnosticMsg", DmMsg);
                    //   System.out.print("DM" + DmMsg);
                    values.put("Qcode", Qcode);
                    //  System.out.print("qcode" + Qcode);
                    db.insert("sensordata", null, values);
                    System.out.println("success fully update1");
                }
                for (int i = 0; i < Dsize; i++) {

                    //*write code for matching*//
                    values.put("Ts", Ts);
                    values.put("O2", O2);
                    values.put("Co2", Co2);
                    values.put("StackTemp", stackTemp);
                    values.put("AmbientTemp", AmbientTemp);
                    values.put("Value", 0);
                    values.put("ParamName", DeactiveparamItem.get(i));
                    values.put("Status", S_Status);
                    values.put("DiagnosticMsg", DmMsg);
                    //   System.out.print("DM1" + DmMsg);
                    values.put("Qcode", Qcode);
                    // System.out.print("qcode1" + Qcode);
                    db.insert("sensordata", null, values);
                    System.out.println("success fully update2");
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }

        db.close();
    }

    //***************************** asyn task for storing data in db(geeta part)===============================
    public String StoreData() {
        // databaseHandler.addExtraParamtoSensorTable(ParamName);
        return "success";
    }

    private class Calculations extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... urls) {

            System.out.print("inside of jdhsgzfdfjkgfj doinbackgroung");
            //System.out.print("param to be add :"+paramname);

            return StoreData();
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("success")) {
                System.out.print("success to enter new values from setting");

            } else
                System.out.print("fail to enter new values from setting");
        }
    }

    //********************************** end ********************************************************
    public ArrayList<GainOffset> getGainAndOffset(String Param) throws Exception {
        System.out.println("inside gain offset");
        ArrayList<GainOffset> Gf = new ArrayList<GainOffset>();
        String selectQuery = "SELECT  * FROM parametervalues where ParamName='" + Param + "'";
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    GainOffset lm = new GainOffset();
                    lm.setGain(cursor.getDouble(5));
                    lm.setOffset(cursor.getDouble(6));

                    Gf.add(lm);
                    System.out.print(":P " + Gf.get(0).getGain());
                } while (cursor.moveToNext());
            }
            db.close();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }
        return Gf;

    }


    // fetch logs detail from logs table
    public ArrayList<LogsModel> getLogasDetail() throws SQLiteException {

        ArrayList<LogsModel> logs = new ArrayList<LogsModel>();
        String selectQuery = "SELECT  * FROM  logs ";//where Opcode='event2'";
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    LogsModel lm = new LogsModel();
                    lm.setTimestamp(cursor.getString(1));
                    lm.setUser(cursor.getString(2));
                    lm.setOper(cursor.getString(4));
                    lm.setOpcode(cursor.getString(3));

                    logs.add(lm);
                } while (cursor.moveToNext());
            }
            db.close();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }
        return logs;
    }

    public ArrayList<LogsModel> getLogasDetailLogin() throws SQLiteException {

        ArrayList<LogsModel> logs = new ArrayList<LogsModel>();
        String selectQuery = "SELECT  * FROM  logs where Opcode='event1'";
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    LogsModel lm = new LogsModel();
                    lm.setTimestamp(cursor.getString(1));
                    lm.setUser(cursor.getString(2));
                    lm.setOper(cursor.getString(4));
                    lm.setOpcode(cursor.getString(3));

                    logs.add(lm);
                } while (cursor.moveToNext());
            }
            db.close();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }
        return logs;
    }

    //fetch Deactive parameter from parameter table
    public ArrayList<String> getDeActiveParam() throws SQLiteException {
        ArrayList<String> paramname = new ArrayList<String>();

        String selectQuery = "SELECT * FROM parametervalues where Mode='Deactive'";
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            int i = 0;
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    paramname.add(cursor.getString(1));
                    // list.add(cursor.getString(1));
                    System.out.print("\n'''param''" + paramname.get(i));
                    i++;
                } while (cursor.moveToNext());
            }
            db.close();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }
        return paramname;
    }

    //fetch active parameter from parameter table
    public ArrayList<String> getActiveParam() throws SQLiteException {
        ArrayList<String> paramname = new ArrayList<String>();

        String selectQuery = "SELECT * FROM parametervalues where Mode='Active'";
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            int i = 0;
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    paramname.add(cursor.getString(1));

                    System.out.print("\n'''param''" + paramname.get(i));
                    i++;
                } while (cursor.moveToNext());
            }
            db.close();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }
        return paramname;
    }

    //fetch parameter value......
    public ArrayList<String> getParam() throws SQLiteException {
        ArrayList<String> paramname = new ArrayList<String>();

        String selectQuery = "SELECT * FROM parametervalues ";
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            // int i=0;
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    paramname.add(cursor.getString(1));
                    // list.add(cursor.getString(1));
               /* System.out.print("\n'''param''" + paramname.get(i));
                i++;*/
                } while (cursor.moveToNext());
            }
            db.close();

        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }
        return paramname;
    }


    //fetch sensor parameter fron sensor table
    public ArrayList<String> getSensoParam() {
        System.out.print("inside sensor param.......\n");
        ArrayList<String> paramname = new ArrayList<String>();

        String selectQuery = "SELECT DISTINCT ParamName FROM sensordata";
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            int i = 0;
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    paramname.add(cursor.getString(0));

                    //  System.out.print(" .......sensor paraam....... "+paramname.get(i));
                    i++;
                } while (cursor.moveToNext());
            }
            db.close();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }
        return paramname;
    }

    ///fetch current data from sensor table

//************************************************** satic daa able **********************************************************


    public Long CurrentTime() throws SQLiteException {
        Long timestamp_array = null;

        try {
            String selectQuery = "SELECT Ts FROM sensordata group by Ts ORDER BY Ts DESC ";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {


                timestamp_array = (cursor.getLong(0));
                System.out.println("time stamp for static  data" + cursor.getLong(0));
            }
            db.close();
        } catch (IllegalAccessError e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }

        return timestamp_array;
    }

    public ArrayList<CurrentDataModel> StaticTableAttribute(Long ts) throws SQLiteException {
        ArrayList<CurrentDataModel> data = new ArrayList<CurrentDataModel>();

        try {
            String selectQuery = "SELECT Co2,StackTemp,O2,AmbientTemp FROM  sensordata  where Ts='" + ts + "' group by Ts";
            System.out.println(selectQuery);
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            System.out.println("cursor1 data");
            if (cursor != null) {
                System.out.println("cursor data");
                if (cursor.moveToFirst()) {
                    System.out.println("static data");
                    CurrentDataModel cm = new CurrentDataModel();
                    cm.setCo2(cursor.getDouble(0));
                    cm.setStackTemp(cursor.getString(1));
                    cm.setAmbientTemp(cursor.getString(3));
                    cm.setO2(cursor.getDouble(2));
                    System.out.println("@#$% " + cm.getO2());
                    //cm.setDate(cursor.getString(3));
                    data.add(cm);

                }
            }
            db.close();
        } catch (IllegalAccessError e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }
        System.out.println("size od static data " + data.size());
        return data;

    }


    //************************************************current table data  ****************************************************
    public ArrayList<Long> currentdata() {
        ArrayList<Long> timestamp_array = new ArrayList<Long>();

        try {
            String selectQuery = "SELECT Ts FROM sensordata group by Ts ORDER BY Ts DESC";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {


                timestamp_array.add(cursor.getLong(0));
                System.out.println("time stamp for current data" + cursor.getLong(0));
            }
            db.close();
        } catch (IllegalAccessError e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }

        return timestamp_array;
    }

    public ArrayList<CurrentDataModel> Currentparamvalue(Long ts) throws SQLiteException {
        ArrayList<CurrentDataModel> data = new ArrayList<CurrentDataModel>();

        try {
            System.out.println("inside current data value" + ts);
            String selectQuery = "SELECT s.paramName,s.Value,s.O2 FROM  sensordata as s ,parametervalues as p where s.ParamName=p.ParamName and p.Mode='Active' and s.Ts='" + ts + "'";
            db = this.getReadableDatabase();
            System.out.println(selectQuery);
            cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                System.out.println("inside current data cursor");
                if (cursor.moveToFirst()) {
                    System.out.println("inside cursor ");
                    do {
                        CurrentDataModel cm = new CurrentDataModel();

                        cm.setParamName(cursor.getString(0));
                        cm.setValue(cursor.getDouble(1));
                        cm.setO2(cursor.getDouble(2));
                        System.out.print(" O2 ++++" + cm.getO2());
                        //cm.setDate(cursor.getString(3));
                        data.add(cm);
                    } while (cursor.moveToNext());
                }
            }
            db.close();
        } catch (IllegalAccessError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }
        System.out.println("data current data value " + data.size());
        return data;

    }

    /**/
    public ArrayList<GainOffset> getConversionValues() throws SQLiteException {
        ArrayList<GainOffset> ConversionV = new ArrayList<GainOffset>();

        try {
            db = this.getReadableDatabase();
            String selectQuery = "SELECT conversionValue ,ParamName FROM conversionValue  ";

            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    GainOffset gf = new GainOffset();
                    gf.setGain(cursor.getDouble(0));
                    gf.setParam(cursor.getString(1));
                    // cursor.moveToFirst();
                    ConversionV.add(gf);
                    // return contact
                    //   System.out.println("conver :" + cursor.getDouble(0));
                } while (cursor.moveToNext());
            }
            db.close();
        } catch (IllegalAccessError e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError p) {
            p.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }


        return ConversionV;
    }

    public ArrayList<Double> getConversionValues(ArrayList<String> paramName) throws SQLiteException {
        ArrayList<Double> ConversionV = new ArrayList<Double>();
        for (int i = 0; i < paramName.size(); i++) {
            System.out.print("\nln@#$$# " + paramName.get(i));

            try {
                db = this.getReadableDatabase();
                String selectQuery = "SELECT conversionValue  FROM conversionValue where ParamName='" + paramName.get(i) + "' ";

                cursor = db.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        // cursor.moveToFirst();
                        ConversionV.add(cursor.getDouble(0));
                        // return contact
                        System.out.println("conver :" + cursor.getDouble(0));
                    } while (cursor.moveToNext());
                }
                db.close();
            } catch (IllegalAccessError e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError p) {
                p.printStackTrace();
            } finally {
                // this gets called even if there is an exception somewhere above
                if (cursor != null)
                    cursor.close();
            }

        }

        return ConversionV;
    }


    public ArrayList<Long> Historydata() throws SQLiteException {
        ArrayList<Long> timestamp_array = new ArrayList<Long>();

        try {
            String selectQuery = "SELECT distinct Ts FROM sensordata ";
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    // cursor.moveToFirst();
                    timestamp_array.add(cursor.getLong(0));
                    // return contact
                } while (cursor.moveToNext());
            }
            //  db.close();
        } catch (IllegalAccessError | OutOfMemoryError | Exception e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }

        return timestamp_array;
    }


    //public ArrayList<Double>

    public ArrayList<Double> Histroyparamvalues(ArrayList<Long> ts) throws SQLiteException {
        ArrayList<Double> data = new ArrayList<Double>();
        System.out.println("size inside function :" + ts.size());
        for (int i = 0; i < ts.size(); i++) {
            db = this.getWritableDatabase();
            int k = 0;
            System.out.println("........tsjj: " + ts.get(i));
            Double value;
            try {
                String selectQuery = "SELECT Value FROM sensordata where Ts='" + ts.get(i) + "'";
                cursor = db.rawQuery(selectQuery, null);
                System.out.print("........ts: " + ts.get(i));
                if (cursor != null) {
                    cursor.moveToFirst();
                    //  System.out.println("....cursor value is : "+cursor.toString());
                    do {
                        // System.out.println("....cursor  : "+cursor.toString());
                        value = cursor.getDouble(0);
                        System.out.println("v@@@@@@@@@@@@ " + value);
                        data.add(value);
                        k++;
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // this gets called even if there is an exception somewhere above
                if (cursor != null)
                    cursor.close();
            }
            db.close();
        }
        System.out.print(data.size());
        return data;

    }


    public ArrayList<CurrentDataModel> Histroyparamvalue(ArrayList<Long> ts) throws SQLiteException {
        ArrayList<CurrentDataModel> data = new ArrayList<CurrentDataModel>();
        System.out.println("size inside function :" + ts.size());
        for (int i = 0; i < ts.size(); i++) {
            db = this.getWritableDatabase();
            int k = 0;
            System.out.println("........tsjj: " + ts.get(i));

            try {
                String selectQuery = "SELECT * FROM sensordata where Ts='" + ts.get(i) + "'";
                cursor = db.rawQuery(selectQuery, null);
                System.out.print("........ts: " + ts.get(i));
                if (cursor != null) {
                    cursor.moveToFirst();
                    //  System.out.println("....cursor value is : "+cursor.toString());
                    do {
                        // System.out.println("....cursor  : "+cursor.toString());
                        CurrentDataModel cm = new CurrentDataModel();
                        cm.setParamName(cursor.getString(1));
                        Long time = cursor.getLong(3);
                        String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(time));//String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (epoch*1000));
                        cm.setTime(date);
                        cm.setValue(cursor.getDouble(2));
                        cm.setDmMessage(cursor.getString(5));
                        cm.setQCODE(cursor.getString(6));
                        cm.setStatus(cursor.getString(4));
                        System.out.println("v@@@@@@@@@@@@ " + cm.getValue());
                        cm.setO2(cursor.getDouble(7));
                        cm.setCo2(cursor.getDouble(8));
                        cm.setAmbientTemp(cursor.getString(10));
                        cm.setStackTemp(cursor.getString(9));
                        data.add(cm);

                        k++;
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // this gets called even if there is an exception somewhere above
                if (cursor != null)
                    cursor.close();
            }
            db.close();
        }
        System.out.print(data.size());
        return data;

    }
/*Retrieval for reposting starts ..............................................................................................*/

    public long falsyTimeStamp() throws SQLiteException {
        Long time = null;//=new ArrayList<String>();
        String selectQuery = "SELECT distinct Ts FROM sensordata where Status='false' order by Ts ";
        String date = "";

        // String selectQueryFalsy = "SELECT * FROM sensordata where Status='false'";
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {

                // cursor.moveToFirst();
                time = (cursor.getLong(0));
                System.out.println("date time for sending " + time);
                date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(time));
                System.out.print("date " + date);
                // return contact

            }
            db.close();
        } catch (IllegalAccessError e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError p) {
            p.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }


        try {
            return time;
        } catch (NullPointerException e) {

//            System.out.println("no falsy data ");
//            System.out.print("((((&&&&((((((((((( "+date+time);
            e.printStackTrace();
            return 0;
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }

    }

    public ArrayList<CurrentDataModel> falsyData(Long time) throws SQLiteException {
        ArrayList<CurrentDataModel> data = new ArrayList<CurrentDataModel>();
        ArrayList<String> paramname;
        paramname = getActiveParam();
        if (time == 0 && time == null) {

            System.out.println("$$$$$$$$$ inside fals data time " + time);
        } else {
            System.out.println("size inside function :" + time);
            db = this.getWritableDatabase();
            String timestamp;
            //   System.out.println("........tsjj: " + ts.get(0));
            String selectQuery = "SELECT * FROM sensordata where Ts='" + time + "'";
            cursor = db.rawQuery(selectQuery, null);
            try {
                //  System.out.print("........ts: "+ts.get(i));
                if (cursor.moveToFirst()) {
                    //  System.out.println("....cursor value is : "+cursor.toString());
                    do {
                        // System.out.println("....cursor  : "+cursor.toString());
                        CurrentDataModel cm = new CurrentDataModel();
                        if (paramname.contains(cursor.getString(1))) {
                            cm.setParamName(cursor.getString(1));
                            timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(cursor.getLong(3)));
                            cm.setTime(timestamp);
                            cm.setValue(cursor.getDouble(2));
                            cm.setDmMessage(cursor.getString(5));
                            cm.setQCODE(cursor.getString(6));
                            cm.setStatus(cursor.getString(4));
                            // System.out.println("value in history @@@@@@@@@@@@ " + cm.getValue());
                            cm.setO2(cursor.getDouble(7));
                            cm.setCo2(cursor.getDouble(8));
                            cm.setAmbientTemp(cursor.getString(10));
                            cm.setStackTemp(cursor.getString(9));
                            data.add(cm);
                        }
                        // k++;
                    } while (cursor.moveToNext());
                }
            } catch (IllegalAccessError e) {
                e.printStackTrace();
            } finally {
                // this gets called even if there is an exception somewhere above
                if (cursor != null)
                    cursor.close();
            }
            db.close();
        }

        System.out.println("falsy data array list " + data);
        return data;

    }

    public JSONObject Repost_Object_Creation() throws JSONException {

        Log.d("this is me ", "list.get(i).getStatus()");

        ArrayList<CurrentDataModel> list = falsyData(falsyTimeStamp());


        System.out.println("its list" + list);
        if (list.size() == 0) {
            return null;
        } else {
            JSONObject ObjForRePost = new JSONObject();
            if (list.size() > 0) {
                try {
                    System.out.println("falsy status " + list.get(0).getStatus());
                    JSONObject obj = new JSONObject();
                    for (int i = 0; i < list.size(); i++) {
                        ObjForRePost.accumulate(list.get(i).getParamName(), ""+list.get(i).getValue());
                        System.out.println(list.get(i).getParamName() + "  AND  " + list.get(i).getValue());
                    }

                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(obj);
                    //Log.d("this is me ", list.get(i).getStatus());

                    // object for post
                    // ObjForRePost.accumulate("values", jsonArray);
                    ObjForRePost.put("O2", list.get(0).getO2());
                    ObjForRePost.accumulate("DM", list.get(0).getDmMessage());
                    ObjForRePost.put("stackTemp", list.get(0).getStackTemp());
                    ObjForRePost.accumulate("QCode", list.get(0).getQCODE());
                    ObjForRePost.accumulate("ts", list.get(0).getTime());



                    System.out.println("MyObject " + ObjForRePost);


                } catch (Exception e) {
                    Log.d("reposting .....", "exception");

                } finally {
                    // this gets called even if there is an exception somewhere above
                    if (cursor != null)
                        cursor.close();
                }
                return ObjForRePost;
            } else
                return null;
        }

    }

    public ArrayList<String> getConversionParam() throws SQLiteException {
        ArrayList<String> ConversionParam = new ArrayList<String>();

        String selectQuery = "SELECT *  FROM conversionValue ";
        db = this.getWritableDatabase();
        cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    // cursor.moveToFirst();
                    ConversionParam.add(cursor.getString(1));
                    System.out.println("ffggffggfgf   fgf" + cursor.getString(1));
                    // return contact
                } while (cursor.moveToNext());
            }
        } catch (IllegalAccessError e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError p) {
            p.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }
        db.close();

        return ConversionParam;
    }



    /*Retrieval for reposting ends ..............................................................................................*/
    /*------------------------------------------STATUS Update-------------------------------------------*/

    public void InsertUpdateStatus(String status, JSONArray ObjForRePost) throws JSONException {
        System.out.println("inside $#@ ##### " + "\n" + ObjForRePost + ObjForRePost.length());
        // for(int i=0;i<ObjForRePost.length();i++ ){

        long temp = falsyTimeStamp();

        JSONObject rec = ObjForRePost.getJSONObject(1);
        System.out.println("inside $#@ ##### " + rec + "\n" + ObjForRePost);

        String Ts = rec.getString("ts");
        System.out.print("****** date and time for updating status " + Ts + " timestamp " + temp);
        long epoch = 0;
        try {
            epoch = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(Ts).getTime();
            System.out.println(temp + "timestamp " + epoch);//1496222457972
        } catch (ParseException e) {
            e.printStackTrace();
        }

        StringTokenizer st = new StringTokenizer(status, " ");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            System.out.println("\n btao m kon hu :)" + token);
            status = token;
            break;
        }

        if (status.equalsIgnoreCase("200")) {
            status = "true";
        } else
            status = "false";

        db = getReadableDatabase();
        ContentValues values = new ContentValues();
        // values.put("Ts",current);
        values.put("Status", status);
        db.update("sensordata", values, "Ts='" + temp + "'", null);
        db.close();
        // }


    }


    public ArrayList<CurrentDataModel> getDataForHistory(String from) throws SQLiteException {

        Long fdate = null, tdate = null;
        try {
            fdate = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(from + " 00:00:01").getTime();
            tdate = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(from + " 23:59:00").getTime();
            System.out.print("f= " + fdate + " $$$ t= " + tdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //System.out.println("to for final :"+tof+" formm "+datef);
        ArrayList<CurrentDataModel> data = new ArrayList<CurrentDataModel>();
        // ArrayList<String> paramname;
        //  paramname = getActiveParam();
        //  System.out.print("inside data to...............: "+to);
        String selectQuery = "SELECT * FROM sensordata  where Ts between '" + fdate + "' and '" + tdate + "'  group by Ts";//where Ts between '"+from+"'and '"+to+"'
        String timestamp;
        String[] dt = new String[2];
        db = this.getWritableDatabase();
        cursor = db.rawQuery(selectQuery, null);
        try {
            System.out.println("inside try........");
            if (cursor.moveToFirst()) {

                do {
                    CurrentDataModel cm = new CurrentDataModel();

                    cm.setParamName(cursor.getString(1));
                    //cm.setDate(cursor.getString(3));
                    timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(cursor.getLong(3)));
                    System.out.print("\n..............hi hhihi..: " + cursor.getString(1));
                    System.out.print("\n..............hi hhihi..: " + timestamp);
                    int i = 0;
                    StringTokenizer st = new StringTokenizer(timestamp, " ");
                    while (st.hasMoreTokens()) {
                        dt[i] = (st.nextToken());
                        //    System.out.print("\ndate time... : "+dt[i]);
                        i++;
                    }
                    System.out.print("................inside while of history......................\n");
                    cm.setDate(dt[0]);
                    cm.setTime(dt[1]);

                    cm.setDmMessage(cursor.getString(5));
                    cm.setValue(cursor.getDouble(2));
                    cm.setQCODE(cursor.getString(6));
                    cm.setO2(cursor.getDouble(7));
                    cm.setCo2(cursor.getDouble(8));
                    cm.setStackTemp(cursor.getString(9));
                    cm.setAmbientTemp(cursor.getString(10));
                  /*  System.out.print("date is" + cm.getDate() + "\n");
                    System.out.print("time is" + cm.getTime() + "\n");*/
                    data.add(cm);
                } while (cursor.moveToNext());
            }


        } catch (IndexOutOfBoundsException e) {
            Log.d("Exception ", "Sensor data " + e);
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }
        db.close();
        return data;
    }


    public ArrayList<Long> HistoryDatedata(String from, String to) throws SQLiteException {
        ArrayList<Long> timestamp_array = new ArrayList<Long>();
        /*from=from + " 01:00:00";
        to=to+ " 01:00:00";*/
        Long fdate = null, tdate = null;
        try {

            fdate = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(from + " 00:00:01").getTime();
            tdate = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(from + " 23:59:00").getTime();


            System.out.print("f= " + fdate + " $$$ t= " + tdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int i = 0;
        System.out.println("to for final :" + to + " formm  " + from);
        String selectQuery = "SELECT distinct Ts FROM sensordata where Ts between '" + fdate + "' and '" + tdate + "'  ";//where Ts between '"+from+"'and '"+to+"' group by Ts
        System.out.print(selectQuery);
        db = this.getWritableDatabase();
        cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    /*if(i==size)
                        break;*/
                    // cursor.moveToFirst();
                    timestamp_array.add(cursor.getLong(0));

                    System.out.print("$$$$$$" + cursor.getLong(0));
                    // i++; // return contact
                } while (cursor.moveToNext());
            }
        } catch (IllegalAccessError e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError p) {
            p.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }
        db.close();
        return timestamp_array;
    }


    //fetch unit from parameter table
    public ArrayList<String> getUnitsofParameter() throws SQLiteException {
        ArrayList<String> unit = new ArrayList<String>();

        String selectQuery = "SELECT Unit FROM parametervalues ";
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            // int i=0;
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    unit.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            db.close();

        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }
        return unit;
    }


    public void addExtraParamtoSensorTable(String param) throws SQLiteException {
        ArrayList<Long> timestamp = Historydata();
        ArrayList<CurrentDataModel> val = Histroyparamvalue(timestamp);
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (int i = 0; i < timestamp.size(); i++) {


            values.put("Ts", timestamp.get(i));
            values.put("Value", 0);
            values.put("ParamName", param);
            values.put("Status", val.get(i).getStatus());// remainig
            values.put("DiagnosticMsg", val.get(i).getDmMessage());
            values.put("Qcode", val.get(i).getQCODE());
            System.out.print("QCODE " + val.get(i).getQCODE());
            values.put("O2", val.get(i).getO2());
            values.put("Co2", val.get(i).getCo2());
            values.put("StackTemp", val.get(i).getStackTemp());
            values.put("AmbientTEmp", val.get(i).getAmbientTemp());
            db.insert("sensordata", null, values);

            System.out.println("#### succesfully enter ");
        }
        db.close();
    }


    public ArrayList<ParameterForGaugeView> getParameterForGaugeView() throws SQLiteException {

        ArrayList<ParameterForGaugeView> gaugeViews = new ArrayList<ParameterForGaugeView>();


        db = this.getWritableDatabase();
        int k = 0;
        //   System.out.println("........tsjj: " + ts.get(0));
        String selectQuery = "SELECT ParamName,Alarm1,Alarm2,Range,Unit FROM  parametervalues  where  Mode='Active'  ";
        cursor = db.rawQuery(selectQuery, null);
        try {
            //  System.out.print("........ts: "+ts.get(i));
            if (cursor.moveToFirst()) {
                //  System.out.println("....cursor value is : "+cursor.toString());
                do {
                    // System.out.println("....cursor  : "+cursor.toString());
                    ParameterForGaugeView cm = new ParameterForGaugeView();
                    cm.setParam(cursor.getString(0));
                    cm.setAlarm1(cursor.getDouble(1));
                    cm.setAlarm2(cursor.getDouble(2));
                    cm.setRang(cursor.getString(3));
                    cm.setUnit(cursor.getString(4));
                    //  cm.setValue(cursor.getDouble(4));

                    gaugeViews.add(cm);

                } while (cursor.moveToNext());

            }
        } catch (IllegalAccessError e) {
            e.printStackTrace();


        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }
        db.close();
        return gaugeViews;
    }


    /*===========GEETA'S MODULE END=========*/
    /*========================================@@@@@@@@@@@@@@@@@@@@@@@@================================================*/



/*===========PARIKSHIT'S MODULE START=========*/


    //Inserting into logs table
    public void Insert_logs(Logs_Model user) {

        db = getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put("User", user.getUsername());
        values.put("Ts", user.getCurrentTimeStamp());
        values.put("Opcode", user.getEvent());
        values.put("OperationString", user.getOperation_string());
        db.insert("logs", null, values);
        db.close();
    }


    //Inserting User Details into Login Table
    public void Insert_login() {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //Factory Insertion
        values.put("userlevel", 3);
        values.put("Uname", "factory");
        values.put("Password", "factory");
        values.put("Email", "factory@iiitb.org");
        values.put("Mobile", "8951262689");
        values.put("Fname", "factory");
        db.insert("login", null, values);

//        //Admin Insertion
//        values.put("userlevel", 2);
//        values.put("Uname", "admin");
//        values.put("Password", "admin");
//        values.put("Email", "admin@iiitb.org");
//        values.put("Mobile", "8962704780");
//        values.put("Fname", "factory");
//        db.insert("login", null, values);
//
//        //User Insertion
//        values.put("userlevel", 1);
//        values.put("Uname", "user");
//        values.put("Password", "user");
//        values.put("Email", "user@iiitb.org");
//        values.put("Mobile", "8269398524");
//        values.put("Fname", "factory");
//        db.insert("login", null, values);
        db.close();
    }


    //update password by user
    public void Update_login(String username, String newPassword) {

        db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("Password", newPassword);
        db.update("login", values, "Uname='" + username + "'", null);
        db.close();

    }

    public String getDevice() {
        String devid = "";
        String selectQuery = "SELECT  * FROM  device";
        try {
            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            System.out.println(" feching devic inside ");
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                System.out.println(" feching devic " + cursor.getString(1));
                devid = cursor.getString(1);
            }
            db.close();
        } catch (SQLiteException | NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }
        return devid;
    }

    public String addDevice(String devid) {
        try {
            db = getReadableDatabase();
            ContentValues values = new ContentValues();
            cursor = null;

            cursor = db.rawQuery("SELECT * FROM device ;", null);
            System.out.println("checking devid in database " + cursor.toString());
            if (cursor == null) {
                values.put("devid", devid);
                values.put("Uid", "bydesign_dev01");
                db.insert("device", null, values);
                db.close();
                return "success";
            } else {
                values.put("devid", devid);
                db.update("device", values, "Uid='bydesign_dev01'", null);
                db.close();
                return "success";
            }


        } catch (SQLiteException | NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            return "fail";
        }
    }
    //Retrieving data from Login Table

    public String checkUsername(String uname) {
        String res = "";
        String usernaem = "";
        db = getReadableDatabase();
        Login_Model loginModel = null;
        cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM login WHERE Uname = '" + uname + "';", null);
            if (cursor == null) {
                res = "create";
            } else if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.moveToFirst()) {// res = "create";
                    usernaem = cursor.getString(1);
                    if (uname.equalsIgnoreCase(usernaem)) {
                        res = "notcreate";
                    } else
                        res = "create";
                } else
                    res = "create";
            }//, cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5));
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
            res = "create";
            db.close();
            System.out.println(" res in when checking user " + res + usernaem + uname);
            return res;

        } catch (IllegalStateException e) {
            e.printStackTrace();
            db.close();
            System.out.println(" res in when checking user " + res + usernaem + uname);
            return res;
        } catch (NullPointerException | SQLiteException e) {
            e.printStackTrace();
            res = "create";
            db.close();
            System.out.println(" res in when checking user " + res + usernaem + uname);
            return res;

        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
            return res;
        }

    }

    public Login_Model Retrieve(String username) {
        db = getReadableDatabase();
        Login_Model loginModel = null;
        cursor = null;

        cursor = db.rawQuery("SELECT * FROM login WHERE Uname = '" + username + "';", null);
        if (cursor != null)
            cursor.moveToFirst();
        try {
            loginModel = new Login_Model(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5));
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }
        db.close();
        return loginModel;
    }


    //Insert into ParamValues Table

    public String check(Settings_Model settings_model) {
        ArrayList<String> conversionParam = getConversionParam();
        String status = "";
        for (int i = 0; i < conversionParam.size(); i++) {

            if (settings_model.getParamName().equalsIgnoreCase(conversionParam.get(i))) {
                status = "success";
                break;

            } else {
                status = "non";
            }
        }

        return status;
    }

    public String Inser_parametervalues(Settings_Model settings_model) {
        ArrayList<String> list = new ArrayList<>();
        list = getParam();
        int flag = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equalsIgnoreCase(settings_model.getParamName())) {

                flag = 1;
                break;
            }
        }
        if (flag == 0) {
            try {

                String check = check(settings_model);
                if (check.equalsIgnoreCase("success")) {

                    //long epoch1 = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(currentTimeStamp).getTime() / 1000;
                    db = getReadableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("ParamName", settings_model.getParamName());
                    values.put("Range", settings_model.getRange());
                    values.put("Alarm1", settings_model.getAlarm1());
                    values.put("Alarm2", settings_model.getAlarm2());
                    values.put("Gain", settings_model.getGain());
                    values.put("Offset", settings_model.getOffset());
                    values.put("Resolution", settings_model.getResolution());
                    values.put("Unit", settings_model.getUnit());
                    values.put("Mode", settings_model.getMode());
                    db.insert("parametervalues", null, values);
                    //db.close();
                    return "success";
                } else
                    return "none";


            } catch (SQLiteConstraintException e) {
                e.printStackTrace();
                return "save";
            } finally {
                // this gets called even if there is an exception somewhere above
                if (cursor != null)
                    cursor.close();
            }
        } else {

            Log.d("value of flag ", String.valueOf(flag));
            return "saved";
        }
    }

    //Retrieving from ParameterValues
    public List<Settings_Model> Retrieve_parametervalue() throws SQLiteException {
        List<Settings_Model> settings_modelList = new ArrayList<Settings_Model>();
        db = getReadableDatabase();
        Log.d("DATABASE FILE PATH", db.getPath());
        Settings_Model settings_model = null;
        cursor = null;

        cursor = db.rawQuery("SELECT * FROM parametervalues;", null);
        if (cursor.moveToFirst()) {
            do {
                try {
                    settings_model = new Settings_Model(cursor.getString(1), cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getDouble(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));
                    settings_modelList.add(settings_model);
                } catch (CursorIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();

                }
            }
            while (cursor.moveToNext());
        }
        db.close();
        return settings_modelList;
    }

    //Update Parameter Values

    public String Inset_UserDetail(Login_Model login_Model)

    {
        String result;
        try {

            db = getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("userlevel", login_Model.getUserlevel());
            values.put("Uname", login_Model.getUsername());
            values.put("Password", login_Model.getPassword());
            values.put("Email", login_Model.getEmail());
            values.put("Mobile", login_Model.getMobile());
            values.put("Fname", login_Model.getFullname());
            db.insert("login", null, values);

            result = "success";
        } catch (NumberFormatException | SQLiteConstraintException e) {
            e.printStackTrace();
            result = "error";
        } catch (SQLiteException ee) {
            ee.printStackTrace();
            result = "error";
        } catch (Exception m) {
            m.printStackTrace();
            result = "error";
        }
        db.close();
        return result;
    }


    public String changePass(String username, String newpass) {
        try {
            db = getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("Password", newpass);
            db.update("login", values, "Uname='" + username + "'", null);
            db.close();
            return "success";
        } catch (SQLiteException e) {
            e.printStackTrace();
            return "fail";
        }

    }

    public void Update_parametervalues(Settings_Model settings_model)

    {
        try {

            db = getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("ParamName", settings_model.getParamName());
            values.put("Range", settings_model.getRange());
            values.put("Alarm1", settings_model.getAlarm1());
            values.put("Alarm2", settings_model.getAlarm2());
            values.put("Gain", settings_model.getGain());
            values.put("Offset", settings_model.getOffset());
            values.put("Resolution", settings_model.getResolution());
            values.put("Unit", settings_model.getUnit());
            values.put("Mode", "Active");
            db.update("parametervalues", values, "ParamName='" + settings_model.getParamName() + "'", null);
            db.close();
        } catch (NumberFormatException e) {
            //
        }

    }

    //Mode Retrieval...
    public Settings_Model getMode(String ParamName) throws SQLiteException {
        db = getReadableDatabase();
        cursor = null;
        Settings_Model settings_model = null;

        cursor = db.rawQuery("SELECT * FROM parametervalues WHERE ParamName = '" + ParamName + "';", null);
        if (cursor != null)
            cursor.moveToFirst();
        try {
            settings_model = new Settings_Model(cursor.getString(1), cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getDouble(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));


        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }
        db.close();
        return settings_model;
    }

    //Delete Parameter from ParameterValues
    public void Delete_parametervalues(String ParamName, String Mode) {
        db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ParamName", ParamName);
        values.put("Mode", Mode);
        db.update("parametervalues", values, "ParamName='" + ParamName + "'", null);
        db.close();
    }


    //Database Export
    //Exporting database to sd card
    public boolean exportDB() {
        SQLiteDatabase db = getReadableDatabase();
        File externalStorageDirectory = Environment.getExternalStoragePublicDirectory("AceBackupFile");
        File dataDirectory = Environment.getDataDirectory();
        Log.d("DATABASE DIRECTORY", Environment.getDataDirectory().toString());
        Log.d("DATABASE EXTERNAL DIRECTORY", externalStorageDirectory.getAbsolutePath());
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/user/0/info.bydesign.hmicontroller/databases/" + database_Name;
        String backupDBPath = database_Name;
        File currentDB = new File(dataDirectory, currentDBPath);
        File backupDB = new File(externalStorageDirectory, backupDBPath);
        try {
            externalStorageDirectory.mkdir();
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            return true;
            //Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void Save_Url(String url) {


        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

     /*   values.put("Url", "http://180.179.236.222:8080/smartcity/gassensor/register");
        values.put("flag", 1);
        db.insert("url", null, values);
     */
        values.put("Url", url);
        values.put("flag", 0);
        db.insert("url", null, values);
        db.close();
    }


    public void Reset_Url() {
        db = getWritableDatabase();
        db.execSQL("UPDATE url SET flag=0;");
    }


    public void Set_Url(String url) {
        db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("flag", 1);
        db.update("url", values, "Url='" + url + "'", null);
    }


    public String Get_Url() throws SQLiteException {

        db = getReadableDatabase();
        cursor = null;
        String u = "";

        cursor = db.rawQuery("SELECT * FROM url WHERE flag = 1;", null);
        if (cursor != null)
            cursor.moveToFirst();
        try {

            u = cursor.getString(1);

        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();

        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            // this gets called even if there is an exception somewhere above
            if (cursor != null)
                cursor.close();
        }
        db.close();
        return u;
    }

    public ArrayList<String> Get_AllUrl() throws SQLiteException {
        ArrayList<String> list = new ArrayList<>();

        db = getReadableDatabase();
        cursor = null;

        cursor = db.rawQuery("SELECT * FROM url;", null);
        if (cursor.moveToFirst()) {
            do {
                try {
                    list.add(cursor.getString(1));
                } catch (CursorIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();

                }
            }
            while (cursor.moveToNext());
        }
        db.close();
        return list;

    }

    public void defalutURL() {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Url", "220.227.124.134:8070");
        values.put("flag", 1);
        db.insert("url", null, values);
        db.close();
    }

    public void defalutDevice() {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("devid", "bydesign_dev01");
        values.put("Uid", "bydesign_dev01");
        db.insert("device", null, values);
        db.close();
    }

    public void deleteKr() {
        db = getWritableDatabase();
        cursor = null;
        Settings_Model settings_model = null;
        db.delete("sensordata", "ParamName='he'", null);
        db.delete("parametervalues", "ParamName='he'", null);
        db.delete("sensordata", "ParamName='He'", null);
        db.delete("parametervalues", "ParamName='He'", null);
        db.close();
    }
 /*===========PARIKSHIT'S MODULE DB END=========*/
}


//===========
// ======================================================================================
