package com.bydesign.hmicontroller.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bydesign.hmicontroller.Service.DatabaseHandler;
import com.bydesign.hmicontroller.Service.OOM;
import com.bydesign.hmicontroller.model.CurrentDataModel;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import info.bydesign.hmicontroller.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class HistoricalActivityFragment extends Fragment {

    // ProgressDialog pdialog=null;
    static int N ;
    TableLayout tl,th;
    TableRow tr;
    TextView Date;
    TextView Time;
    TextView QCODE,result;
    TextView DmMessage,Parameter,A_PPM;
    ImageButton to,from;
    ArrayList<String> paramname =new ArrayList<String>();
    //UI References
    private EditText fromDateEtxt;
    private EditText toDateEtxt;
    int flag=0,flagForcheck=0;

    RadioGroup radioGroup;
    String tod,fromd;
    Spinner spinner;
    List<String> categories; //= new ArrayList<String>();

    static String statusE="enter"; static String statusX="exit";
    public HistoricalActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_historical, container, false);
        tl = (TableLayout) view. findViewById(R.id.maintable);
        th = (TableLayout) view. findViewById(R.id.maintableH);


        from=(ImageButton)view.findViewById(R.id.imageButton1);
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
                RadioButton actual = (RadioButton) view.findViewById(R.id.r1);
                actual.setChecked(false);
                RadioButton actual1 = (RadioButton)view.findViewById(R.id.r2);
                actual1.setChecked(false);
                result=(TextView)view.findViewById(R.id.reslt);
                result.setText("");
                statusX="exit";
                statusE="enter";
                //onStart();

            }
        });
        return view;
    }

    ProgressDialog pdialog;

    @Override
    public void onStart() {
        super.onStart();
        Runtime.getRuntime().gc();
        System.gc();
        //OOM PROTECTION
        Thread.currentThread().setDefaultUncaughtExceptionHandler(new OOM.MyUncaughtExceptionHandler());
        final RadioButton actual = (RadioButton) getView().findViewById(R.id.r1);
        actual.setChecked(false);
        final RadioButton actual1 = (RadioButton) getView().findViewById(R.id.r2);
        actual1.setChecked(false);
        try{
            fromDateEtxt=(EditText)getView().findViewById(R.id.editText1);
            if(!fromDateEtxt.getText().toString().equalsIgnoreCase("")) {
                fromDateEtxt.setText("");
                fromDateEtxt.setHint("From Date");
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        result=(TextView)getView().findViewById(R.id.reslt);
        result.setText("");
        DatabaseHandler db=new DatabaseHandler(getActivity());
        paramname = db.getParam();
        N = paramname.size();
        System.out.print("..............size history.................  " + N);
        //spinner = (Spinner) getView().findViewById(R.id.spinner);


    radioGroup=(RadioGroup)getView().findViewById(R.id.rg);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                try {
                    switch (checkedId) {
                        case R.id.r1:

                            statusX="exit";
                            try {
                                if (statusE.equalsIgnoreCase("enter")) {

                                String toDate;
                                java.util.Date dateFrom, dateTo;
                                String fromDate;
                                SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-mm-dd");
                                dateTo = new java.util.Date();
                                System.out.println("to date" + dateTo);
                                fromDate = fromDateEtxt.getText().toString();
                                // toDate=tod;
                                Date dNow = new Date();
                                dateFrom = formatter.parse(fromDate);
                                // dateTo = formatter.parse(dNow.toString());
                                    actual.setChecked(true);



                                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

                                System.out.println("Current Date: " + ft.format(dNow));
                                dateTo = formatter.parse(ft.format(dNow));

                                if (dateFrom.after(dateTo)) {

                                    tl.removeAllViews();
                                    th.removeAllViews();
                                    Toast.makeText(getActivity(), "Please enter valid date (" + dateFrom + "before " + dateTo + ")", Toast.LENGTH_LONG).show();
                                    fromDateEtxt.setText("");
                                    fromDateEtxt.setHint("from Date");


                                } else {
                                    result.setText("Actual values are..");
                                    Toast.makeText(getActivity(), " date is " + fromDate, Toast.LENGTH_LONG).show();
                                    System.out.println("ninininin :p.");
                                    // tod = toDate;
                                    fromd = fromDate;
                                    System.out.println(fromd+"..............size history hu.................  " + N);
                                    System.out.print("$" + fromDate + "$5");
                                    tl.removeAllViews();
                                    th.removeAllViews();
                                    pdialog = ProgressDialog.show(getContext(), "", "Please wait...", true);
                                    new MachingOfdata().execute();


                                }
                                    statusE="exit";
                            }



                            } catch (NullPointerException e) {
                                Toast.makeText(getActivity(), "Please Enter durations", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            } catch (ParseException e) {
                                Toast.makeText(getActivity(), "Please Enter valid durations", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }

                            break;
                        case R.id.r2:

                            try {
                                statusE="enter";
                                String toDate;

                                if(statusX.equalsIgnoreCase("exit")) {
                                    java.util.Date dateFrom, dateTo;
                                    String fromDate;
                                    SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-mm-dd");
                                    dateTo = new java.util.Date();
                                    System.out.println("to date" + dateTo);
                                    fromDate = fromDateEtxt.getText().toString();
                                    //  toDate = tod;
                                    Date dNow = new Date();
                                    dateFrom = formatter.parse(fromDate);
                                    // dateTo = formatter.parse(dNow.toString());

                                    result.setText("Corrected  values are..");
                                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
                                    actual1.setChecked(true);

                                    System.out.println("Current Date: " + ft.format(dNow));
                                    dateTo = formatter.parse(ft.format(dNow));
                                    if (dateFrom.after(dateTo)) {

                                        fromDateEtxt.setText("");
                                        fromDateEtxt.setHint("from Date");
                                        tl.removeAllViews();
                                        th.removeAllViews();
                                        Toast.makeText(getActivity(), "Please enter valid date (" + dateFrom + "before " + dateTo + ")", Toast.LENGTH_LONG).show();

                                    } else {
                                        //  tod = toDate;

                                        fromd = fromDate;
                                        System.out.print("..............size history hu.................  " + N);
                                        tl.removeAllViews();
                                        th.removeAllViews();
                                        pdialog = ProgressDialog.show(getContext(), "", "Please wait...", true);
                                        new Calculations().execute();

                                        Toast.makeText(getActivity(), "date is (" + fromDate + " )", Toast.LENGTH_LONG).show();
                                    }
                                    statusX="otof";
                                }
                            } catch (NullPointerException e) {
                                Toast.makeText(getActivity(), "Please Enter durations", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            } catch (ParseException e) {
                                Toast.makeText(getActivity(), "Please Enter valid durations", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                            break;
                    }


                } catch (Exception e) {

                    e.printStackTrace();

                }
            }
        });

    }





    public void populateSetDate(int year, int month, int day) {

        String d,m;

        if(day<9){
            d="0"+day;
        }
        else d=""+day;

        if(month<9)
            m="0"+month;
        else
            m=""+month;

        fromDateEtxt.setText(year + "-"+ m + "-" + d);
        System.out.print(" dated are " + year + "-" + m + "-" + d);

        calebde(year, m, d);


        flagForcheck=1;
    }


    void calebde(int year,String m,String d){
        int d1,d2;
        d1=(Integer.parseInt(d)+1);
        if(d1<=9)
            d="0"+d1;
        else d=""+d1;
        categories= new ArrayList<String>();
        categories.add(year + "-" + m + "-" + d);
        d2=(Integer.parseInt(d)+1);
        if(d2<=9)
            d="0"+d2;
        else d=""+d2;
        categories.add(year + "-" + m + "-" + d);


    }

    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
             populateSetDate(yy, mm+1, dd);
           // else TOSetDate(yy, mm+1, dd);

        }
    }


    //********************************************** ASYN task ***********************************
    public Double[] calculationForData(){
        DatabaseHandler db=new DatabaseHandler(getActivity());
        System.out.println("in asyn from" + fromd + "  " + tod);
        ArrayList<Long> timestamp_array=db.HistoryDatedata(fromd, tod);
        ArrayList<CurrentDataModel> val=db.Histroyparamvalue(timestamp_array);

        //converting actual ppm value to correct ppm value
        //formula of ppm= ppm = (((20.95 -11)  / (20.95 - Data.get(i).getO2()))* Data.get(i).getValue());
        System.out.println("vaalue.............. size" + val.size());
        Double[]Values = new Double[val.size()];
        for (int i = 0; i < val.size(); i++) {

            Double temp = (((20.95 - 11) / (20.95 - val.get(i).getO2())) * val.get(i).getValue());
            System.out.println("$$$$$$$$$$ "+temp+"param "+val.get(i).getParamName()+" ###");
            Values[i] = temp;
            System.out.println(i);
        }
        System.out.println(Values.length);
        return Values;
    }



    private  class Calculations extends AsyncTask<String, Void, Double[]> {
        @Override
        public Double[] doInBackground(String... urls) {

            System.out.print("inside of doinbackgroung");
            return calculationForData();
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Double[]  result) {

            tl.removeAllViews();
            th.removeAllViews();
            // tg.removeAllViews();
            addHeaders();
            addCorrectDataForFilter(result);
            pdialog.dismiss();

        }
    }

    public ArrayList<Double> machintForData(){
        DatabaseHandler db=new DatabaseHandler(getActivity());
        ArrayList<Long> timestamp_array=db.HistoryDatedata(fromd, tod);
        ArrayList<Double> val=db.Histroyparamvalues(timestamp_array);
        return val;
    }



    private class MachingOfdata extends AsyncTask<String, Void, ArrayList<Double>> {
        @Override
        public ArrayList<Double> doInBackground(String... urls) {
            System.out.println("in side background");
            return machintForData();
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(ArrayList<Double>  result) {

            tl.removeAllViews();
            th.removeAllViews();

            addHeaders();

            addDatedData(result);
            pdialog.dismiss();

        }
    }

    //**************************************************** Header of historical data **************************************


    public void addHeaders(){
        DatabaseHandler db=new DatabaseHandler(getActivity());
         ArrayList<String> unit= db. getUnitsofParameter();
        /** Create a TableRow dynamically **/
        tr = new TableRow(getActivity());
       // tr.setBackgroundColor(Color.argb(196,18,86,136));
        tr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));



        /** Creating a TextView to add to the row **/
        TextView Date = new TextView(getActivity());
        Date.setText("Date");
        Date.setTextColor(Color.WHITE);
        Date.setTextSize(20);
        Date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        Date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        Date.setPadding(5, 5, 5, 0);
        Date.setBackgroundResource(R.drawable.header);//cell.png);
        tr.addView(Date);  // Adding textView to tablerow.

        TextView Time = new TextView(getActivity());
        Time.setText("Time");
        Time.setBackgroundResource(R.drawable.header);
        Time.setTextSize(20);
        Time.setTextColor(Color.WHITE);
        Time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        Time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        Time.setPadding(5, 5, 5, 0);
        tr.addView(Time);  // Adding textView to tablerow.

        Parameter = new TextView(getActivity());
        Parameter.setText(" O2 ");
        Parameter.setBackgroundResource(R.drawable.header);
        Parameter.setTextColor(Color.WHITE);
        Parameter.setTextSize(20);
        Parameter.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        Parameter.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        Parameter.setPadding(5, 5, 5, 0);
        tr.addView(Parameter);  // Adding textView to tablerow.

        A_PPM = new TextView(getActivity());
        A_PPM.setText("  CO2 ");
        A_PPM.setTextColor(Color.WHITE);
        A_PPM.setTextSize(20);
        A_PPM.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        A_PPM.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        A_PPM.setPadding(5, 5, 5, 0);
        A_PPM.setBackgroundResource(R.drawable.header);
        tr.addView(A_PPM);  // Adding textView to tablerow.

        /** Creating a TextView to add to the row **/
        Parameter = new TextView(getActivity());
        Parameter.setText(" Stack temp ");
        Parameter.setTextColor(Color.WHITE);
        Parameter.setTextSize(20);
        Parameter.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        Parameter.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        Parameter.setPadding(5, 5, 5, 0);
        Parameter.setBackgroundResource(R.drawable.header);
        tr.addView(Parameter);  // Adding textView to tablerow.

        A_PPM = new TextView(getActivity());
        A_PPM.setText(" Ambient temp ");
        A_PPM.setTextColor(Color.WHITE);
        A_PPM.setTextSize(20);
        A_PPM.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        A_PPM.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        A_PPM.setPadding(5, 5, 5, 0);
        A_PPM.setBackgroundResource(R.drawable.header);
        tr.addView(A_PPM);  // Adding textView to tablerow.


        for(int row=0;row<N;row++) {
            TextView  Co = new TextView(getActivity());
            System.out.print(" param :"+paramname.get(row)+"\n"+"("+unit.get(row)+")");
            Co.setText(paramname.get(row) + "\n" + "(" + unit.get(row) + ")");
            Co.setTextColor(Color.WHITE);
            Co.setTextSize(20);
            Co.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            Co.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            Co.setPadding(5, 5, 5, 0);
            Co.setBackgroundResource(R.drawable.header);
            tr.addView(Co);
            // Co. removeView();// Adding textView to tablerow.
        }


        TextView QCODE = new TextView(getActivity());
        QCODE.setText("QCODE ");
        QCODE.setTextColor(Color.WHITE);
        QCODE.setTextSize(20);
        QCODE.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        QCODE.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        QCODE.setPadding(5, 5, 5, 0);
        QCODE.setBackgroundResource(R.drawable.header);
        tr.addView(QCODE);  // Adding textView to tablerow.

        TextView DmMessage = new TextView(getActivity());
        DmMessage.setText("Dm Message");
        DmMessage.setTextSize(20);
        DmMessage.setTextColor(Color.WHITE);
        DmMessage.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        DmMessage.setBackgroundResource(R.drawable.header);
        DmMessage.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        DmMessage.setPadding(5, 5, 5, 0);

        tr.addView(DmMessage);  // Adding textView to tablerow.


        // Add the TableRow to the TableLayout
        th.addView(tr, new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));

        // we are adding two textviews for the divider because we have two columns



    }


//************************** Displaying Corrected data in historical data screen **********************************

    public void addCorrectDataForFilter(Double [] Values)
    {
        System.out.print("inside of corrected data in history");
        try{
            DatabaseHandler db=new DatabaseHandler(getActivity());
            ArrayList<CurrentDataModel> data=new ArrayList<CurrentDataModel>();
            data= db. getDataForHistory(fromd);
            int col=data.size();

            System.out.println(Values.length);


            System.out.println("vaalue.............. size" + col);
            int temp= 0,value=1;//,line=col+N,l=-1;
            if(col==0){
                Toast.makeText(getActivity(),"no data available on this date",Toast.LENGTH_LONG).show();
            }

            for (int i = 0; i < col; i++)
            {
                /** Create a TableRow dynamically **/
                tr = new TableRow(getActivity());
                tr.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                /** Creating a TextView to add to the row **/
                Date = new TextView(getActivity());
                Date.setText(" " + data.get(i).getDate() + " ");
                Date .setTextColor(Color.argb(208, 45, 136, 82));
                Date.setTextSize(18);
                Date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                Date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                Date.setPadding(5, 5, 5, 5);
                Date.setBackgroundResource(R.drawable.r);
                tr.addView(Date);  // Adding textView to tablerow.

                /** Creating another textview **/
                Time = new TextView(getActivity());
                Time .setText(" " + data.get(i).getTime() + " ");
                Time .setTextColor(Color.argb(208, 45, 136, 82));
                Time.setTextSize(18);
                //  Time.setBackgroundResource(R.drawable.cell);
                Time .setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                Time .setPadding(5, 5, 5, 5);
                Time.setBackgroundResource(R.drawable.r);
                Time .setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(Time); // Adding textView to tablerow.

                /** Creating a TextView to add to the row **/
                Parameter = new TextView(getActivity());
                Parameter.setText(" " + data.get(i).getO2() + "   ");
                Parameter.setTextColor(Color.argb(208, 45, 136, 82));
                Parameter.setTextSize(20);
                Parameter.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                Parameter.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                Parameter.setPadding(5, 5, 5, 0);
                Parameter.setBackgroundResource(R.drawable.r);
                tr.addView(Parameter);
                // Adding textView to tablerow.

                A_PPM = new TextView(getActivity());
                A_PPM.setText("   " + new DecimalFormat("##.##").format(data.get(i).getCo2()) + "   ");
                A_PPM.setTextColor(Color.argb(208, 45, 136, 82));
                A_PPM.setTextSize(20);
                A_PPM.setBackgroundResource(R.drawable.r);
                A_PPM.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                A_PPM.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                A_PPM.setPadding(5, 5, 5, 0);
                tr.addView(A_PPM);  // Adding textView to tablerow.

                /** Creating a TextView to add to the row **/
                Parameter = new TextView(getActivity());
                Parameter.setText("  " + data.get(i).getStackTemp() + "   ");
                Parameter.setTextColor(Color.argb(208, 45, 136, 82));
                Parameter.setTextSize(20);
                Parameter.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                Parameter.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                Parameter.setPadding(5, 5, 5, 0);
                Parameter.setBackgroundResource(R.drawable.r);
                tr.addView(Parameter);  // Adding textView to tablerow.

                A_PPM = new TextView(getActivity());
                A_PPM.setText("   " + data.get(i).getAmbientTemp() + "   ");
                A_PPM.setTextColor(Color.argb(208, 45, 136, 82));
                A_PPM.setTextSize(20);
                A_PPM.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                A_PPM.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                A_PPM.setPadding(5, 5, 5, 0);
                A_PPM.setBackgroundResource(R.drawable.r);
                tr.addView(A_PPM);  // Adding textView to tablerow.

                // CO = new TextView[N];
                for(int row=0;row<N;row++)
                {
                    //  System.out.println(" ..N:" + N + " sizs: " + val.size());
                    // System.out.println("temp"+temp);
                    TextView  Co = new TextView(getActivity());
                    if(value>N)
                    {
                        int u=row+paramname.size();
                        Co.setText(" " + new DecimalFormat("##.##").format(Values[temp] )+ " ");
                        // System.out.println("   l:  " + l + row + N+u+" value in val"+val.get(row+N).getValue());
                    }
                    else
                        Co.setText(" " +new DecimalFormat("##.##").format(Values[row]) + " ");

                    // System.out.println(" .. " + val.get(row).getValue());
                    Co.setTextColor(Color.argb(208, 45, 136, 82));
                    Co.setTextSize(18);
                    Co.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    Co.setPadding(5, 5, 5, 5);
                    Co.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    Co.setBackgroundResource(R.drawable.r);
                    tr.addView(Co);

                    value++;
                    temp++;
                }
                QCODE = new TextView(getActivity());
                QCODE .setText(" " + data.get(i).getQCODE() + " ");
                QCODE .setTextColor(Color.argb(208, 45, 136, 82));
                QCODE.setTextSize(18);
                QCODE .setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                QCODE .setPadding(5, 5, 5, 5);
                QCODE.setBackgroundResource(R.drawable.r);
                QCODE .setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(QCODE);

                DmMessage = new TextView(getActivity());
                DmMessage .setText(" " + data.get(i).getDmMessage() + " ");
                DmMessage .setTextColor(Color.argb(208, 45, 136, 82));
                DmMessage.setTextSize(18);
                DmMessage .setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                DmMessage .setPadding(5, 5, 5, 5);
                DmMessage.setBackgroundResource(R.drawable.r);
                DmMessage .setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(   DmMessage );

                // Add the TableRow to the TableLayout
                tl.addView(tr, new TableLayout.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
            }
        }catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }catch  (IndexOutOfBoundsException e){
            e.printStackTrace();
            System.out.print("index out of bound");
        }
    }


   //displaying actual data on historical screen******************************************

    public void  addDatedData(ArrayList<Double>val) {

        try {

            DatabaseHandler db = new DatabaseHandler(getActivity());
            ArrayList<CurrentDataModel> data = new ArrayList<CurrentDataModel>();
            data = db.getDataForHistory(fromd);
            int col = data.size();
            System.out.print("col"+col);
           /* ArrayList<Long> timestamp_array=db.Historydata();
            ArrayList<Double> val1=db.Histroyparamvalues(timestamp_array);*/
            System.out.println("vaalue.............. size" + val.size());
            int temp = 0,value=1;

            if(col==0){
                Toast.makeText(getActivity(),"No data on this date",Toast.LENGTH_LONG).show();

            }
            else {
                for (int i = 0; i < col; i++) {
                    /** Create a TableRow dynamically **/
                    tr = new TableRow(getActivity());
                    tr.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.FILL_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    /** Creating a TextView to add to the row **/
                    Date = new TextView(getActivity());
                    Date.setText(" " + data.get(i).getDate() + " ");
                    Date.setTextColor(Color.argb(208, 45, 136, 82));
                    Date.setTextSize(18);
                    Date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    Date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    Date.setPadding(5, 5, 5, 5);
                    Date.setBackgroundResource(R.drawable.r);
                    tr.addView(Date);  // Adding textView to tablerow.

                    /** Creating another textview **/
                    Time = new TextView(getActivity());
                    Time.setText(" " + data.get(i).getTime() + " ");
                    Time.setTextColor(Color.argb(208, 45, 136, 82));
                    Time.setTextSize(18);
                    Time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    Time.setPadding(5, 5, 5, 5);
                    Time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    Time.setBackgroundResource(R.drawable.r);
                    tr.addView(Time); // Adding textView to tablerow.

                    Parameter = new TextView(getActivity());
                    Parameter.setText(" " + data.get(i).getO2() + "   ");
                    Parameter.setTextColor(Color.argb(208, 45, 136, 82));
                    Parameter.setTextSize(20);
                    Parameter.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    Parameter.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    Parameter.setPadding(5, 5, 5, 0);
                    Parameter.setBackgroundResource(R.drawable.r);
                    tr.addView(Parameter);  // Adding textView to tablerow.

                    A_PPM = new TextView(getActivity());
                    A_PPM.setText("   " + new DecimalFormat("##.##").format(data.get(i).getCo2()) + "   ");
                    A_PPM.setTextColor(Color.argb(208, 45, 136, 82));
                    A_PPM.setTextSize(20);
                    A_PPM.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    A_PPM.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    A_PPM.setPadding(5, 5, 5, 0);
                    A_PPM.setBackgroundResource(R.drawable.r);
                    tr.addView(A_PPM);  // Adding textView to tablerow.

                    /** Creating a TextView to add to the row **/
                    Parameter = new TextView(getActivity());
                    Parameter.setText("  " + data.get(i).getStackTemp() + "   ");
                    Parameter.setTextColor(Color.argb(208, 45, 136, 82));
                    Parameter.setTextSize(20);
                    Parameter.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    Parameter.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    Parameter.setPadding(5, 5, 5, 0);
                    Parameter.setBackgroundResource(R.drawable.r);
                    tr.addView(Parameter);  // Adding textView to tablerow.

                    A_PPM = new TextView(getActivity());
                    A_PPM.setText("   " + data.get(i).getAmbientTemp() + "   ");
                    A_PPM.setTextColor(Color.argb(208, 45, 136, 82));
                    A_PPM.setTextSize(20);
                    A_PPM.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    A_PPM.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    A_PPM.setPadding(5, 5, 5, 0);
                    A_PPM.setBackgroundResource(R.drawable.r);
                    tr.addView(A_PPM);

                    // CO = new TextView[N];
                    for (int row = 0; row < N; row++) {
                        //  System.out.println(" ..N:" + N + " sizs: " + val.size());
                        //  System.out.println("temp" + temp);
                        TextView Co = new TextView(getActivity());
                        if (value > N) {
                            int u = row + paramname.size();

                            Co.setText(" " +new DecimalFormat("##.##").format( val.get(temp)) + " ");
                            //  System.out.println("   l:  " + l + row + N + u + " value in val" + val.get(row + N).getValue());
                        } else
                            Co.setText(" " +new DecimalFormat("##.##").format( val.get(row) )+ " ");

                        //  System.out.println(" .. " + val.get(row).getValue());
                        Co.setTextColor(Color.argb(208, 45, 136, 82));
                        Co.setTextSize(18);
                        Co.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        Co.setPadding(5, 5, 5, 5);
                        Co.setBackgroundResource(R.drawable.r);
                        Co.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                        tr.addView(Co);

                        value++;
                        temp++;

                    }
                    QCODE = new TextView(getActivity());
                    QCODE.setText(" " + data.get(i).getQCODE() + " ");
                    QCODE.setTextColor(Color.argb(208, 45, 136, 82));
                    QCODE.setTextSize(18);
                    QCODE.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    QCODE.setPadding(5, 5, 5, 5);
                    QCODE.setBackgroundResource(R.drawable.r);
                    QCODE.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    tr.addView(QCODE);

                    DmMessage = new TextView(getActivity());
                    DmMessage.setText(" " + data.get(i).getDmMessage() + " ");
                    DmMessage.setTextColor(Color.argb(208, 45, 136, 82));
                    DmMessage.setTextSize(18);
                    DmMessage.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    DmMessage.setPadding(5, 5, 5, 5);
                    DmMessage.setBackgroundResource(R.drawable.r);
                    DmMessage.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    tr.addView(DmMessage);

                    // Add the TableRow to the TableLayout
                    tl.addView(tr, new TableLayout.LayoutParams(
                            TableRow.LayoutParams.FILL_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                }
            }
        }catch (IndexOutOfBoundsException e){
            Toast.makeText(getActivity(),"no data",Toast.LENGTH_LONG).show();
            pdialog.dismiss();
            e.printStackTrace();
        }catch (OutOfMemoryError e ){
           // Toast.makeText(getActivity(),"Error in fetching Large data",Toast.LENGTH_LONG).show();
            pdialog.dismiss();
            e.printStackTrace();
        }catch (Exception e){
            Toast.makeText(getActivity(),"Error in fetching  data",Toast.LENGTH_LONG).show();
            pdialog.dismiss();
            e.printStackTrace();
        }

    }

}
