package com.bydesign.hmicontroller.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bydesign.hmicontroller.Service.DatabaseHandler;
import com.bydesign.hmicontroller.Service.OOM;
import com.bydesign.hmicontroller.activity.GaugeView;
import com.bydesign.hmicontroller.model.Actual_corrected_model;
import com.bydesign.hmicontroller.model.CurrentDataModel;
import com.bydesign.hmicontroller.model.GainOffset;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import info.bydesign.hmicontroller.R;

public class CurrentActivityFragment extends Fragment  {


    static int N;
    ProgressDialog pdialog;
    TableLayout tl, tg,th;
    TableRow tr;
    TextView Parameter;
    TextView C_PPM, C_MG;
    TextView A_PPM, A_MG;
    TextView DmMessage, date;
    // GaugeView gaugeview;
    ArrayList<String> paramname = new ArrayList<String>();
    //Button gauge;
    LinearLayout c1, c2;
    Button graphical;
    public CurrentActivityFragment() {
        // Required empty public constructor
    }

    CheckBox actual;//= (CheckBox) getView().findViewById(R.id.actual);
    CheckBox correct;//= (CheckBox) getView().findViewById(R.id.currect);
    CheckBox Mg;//= (CheckBox) getView().findViewById(R.id.mg);
    CheckBox Ppm;//= (CheckBox) getView().findViewById(R.id.ppm);





    public static Fragment newInstance() {
        CurrentActivityFragment mFrgment = new CurrentActivityFragment();
        //mFrgment.r
        return mFrgment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //  private GaugeView mGaugeView1;
    private LinearLayout layout, gauge;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_currentdata, container, false);
        date = (TextView) view.findViewById(R.id.date);

        correct = (CheckBox) view.findViewById(R.id.currect);
        correct.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //pdialog = ProgressDialog.show(getActivity(), "", "calculating please wait.......", true);
                onStart();
            }
        });
        actual = (CheckBox) view.findViewById(R.id.actual);
        actual.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onStart();
            }
        });

        Mg = (CheckBox) view.findViewById(R.id.mg);
        Mg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onStart();
            }
        });
        Ppm = (CheckBox) view.findViewById(R.id.ppm);
        Ppm.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onStart();
            }
        });

        graphical=(Button)view.findViewById(R.id.graphic);
        graphical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler db=new DatabaseHandler(getActivity());
                db.deleteKr();
                Intent in = new Intent(getActivity(), GaugeView.class);
                startActivity(in);

            }
        });

        return view;
    }

    int count=0;
    Timer autoUpdate;
    /*PDF ENDED*/

    public void onPause(){
        super.onPause();
        System.out.print("inside loop cancel"+count);
        autoUpdate.cancel();
    }
    public void onResume() {
        super.onResume();

//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() { // This thread runs in the UI
//                    @Override
//                    public void run() {
//                        progress.setProgress("anything"); // Update the UI
//                    }
//                });
//            }
//        };
//        new Thread(runnable).start();
//    }

        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {

                System.out.print("inside loop data condition"+count);
                count++;
                new HttpAsyncTaskUpdate().execute("");
            }
        }, 0, 10000); // updates each 120 secs
    }

    public void onStart() {
        super.onStart();

        //   gauge.setVisibility(View.INVISIBLE);
        tl = (TableLayout) getView().findViewById(R.id.maintable);
        th = (TableLayout) getView().findViewById(R.id.maintableH);
        tg = (TableLayout) getView().findViewById(R.id.staticTable);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String currentTimeStamp; // Find todays date
        currentTimeStamp = dateFormat.format(new java.util.Date());
       // date.setText("Last Updated :    "+currentTimeStamp);
        tl.removeAllViews();
        tg.removeAllViews();
        th.removeAllViews();
        System.out.print("inside on start of current");
        //  mGaugeView1=(GaugeView)getView().findViewById(R.id.gauge_view1);
        DatabaseHandler db = new DatabaseHandler(getActivity());
        paramname = db.getActiveParam();
        N = paramname.size();

        System.out.print("\n..............size. og sensor................  \n" + N);
        addStaticDetails();
        System.out.println("CALCLATION START ---------------------");
        // tl.removeAllViews();
        addHeaders();
        addData();
        //   new Calculations().execute();

    }
    private class HttpAsyncTaskUpdate extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            return "success";
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            System.out.println("update result**********"+result);
            try {
                setView();

            } catch (Exception e){

                e.printStackTrace();
            }


        }
    }
    public void setView(){
        Runtime.getRuntime().gc();
        System.gc();
        //OOM PROTECTION
        Thread.currentThread().setDefaultUncaughtExceptionHandler(new OOM.MyUncaughtExceptionHandler());
        //   gauge.setVisibility(View.INVISIBLE);
        tl = (TableLayout) getView().findViewById(R.id.maintable);
        th = (TableLayout) getView().findViewById(R.id.maintableH);
        tg = (TableLayout) getView().findViewById(R.id.staticTable);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String currentTimeStamp; // Find todays date
        currentTimeStamp = dateFormat.format(new java.util.Date());
       // date.setText("Last Updated :    "+currentTimeStamp);
        tl.removeAllViews();
        tg.removeAllViews();
        th.removeAllViews();
        System.out.print("inside on start of current");
        //  mGaugeView1=(GaugeView)getView().findViewById(R.id.gauge_view1);
        DatabaseHandler db = new DatabaseHandler(getActivity());
        paramname = db.getActiveParam();
        N = paramname.size();

        System.out.print("\n..............size. og sensor................  \n" + N);
        addStaticDetails();
        System.out.println("CALCLATION START ---------------------");
        // tl.removeAllViews();
        addHeaders();
        addData();
        //   new Calculations().execute();
    }

    public void addStaticDetails() {
        DatabaseHandler db = new DatabaseHandler(getActivity());
        Long timestamp_array = db.CurrentTime();
        try {
            ArrayList<CurrentDataModel> Data = db.StaticTableAttribute(timestamp_array);
            tr = new TableRow(getActivity());
            // tr.setBackgroundColor(Color.argb(196, 18, 86, 136));
            // tr.setPadding(0,);
            tr.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));


            /** Creating a TextView to add to the row **/
            Parameter = new TextView(getActivity());
            Parameter.setText("   O2      " + new DecimalFormat("##.##").format(Data.get(0).getO2()) +"  %");
            Parameter.setTextColor(Color.WHITE);
            Parameter.setTextSize(20);
            Parameter.setBackgroundResource(R.drawable.header);
            Parameter.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            Parameter.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            Parameter.setPadding(5, 5, 5, 0);
            tr.addView(Parameter);  // Adding textView to tablerow.

            A_PPM = new TextView(getActivity());
            A_PPM.setText("    CO2       " +new DecimalFormat("##.##").format( Data.get(0).getCo2())+"  %");
            A_PPM.setTextColor(Color.WHITE);
            A_PPM.setTextSize(20);
            A_PPM.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            A_PPM.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            A_PPM.setPadding(5, 5, 5, 0);
            A_PPM.setBackgroundResource(R.drawable.header);
            tr.addView(A_PPM);  // Adding textView to tablerow.
            ////Stack temp
            // Ambient temp

            /** Creating a TextView to add to the row **/
            Parameter = new TextView(getActivity());
            Parameter.setText("     Stack temp      " + Data.get(0).getStackTemp()+"  \u2103");
            Parameter.setTextColor(Color.WHITE);
            Parameter.setTextSize(20);
            Parameter.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            Parameter.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            Parameter.setPadding(5, 5, 5, 0);
            Parameter.setBackgroundResource(R.drawable.header);
            tr.addView(Parameter);  // Adding textView to tablerow.

            A_PPM = new TextView(getActivity());
            A_PPM.setText("     Ambient temp      " + Data.get(0).getAmbientTemp()+"  \u2103");
            A_PPM.setTextColor(Color.WHITE);
            A_PPM.setTextSize(20);
            A_PPM.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            A_PPM.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            A_PPM.setPadding(5, 5, 5, 0);
            A_PPM.setBackgroundResource(R.drawable.header);
            tr.addView(A_PPM);  // Adding textView to tablerow.
            tr.setPadding(0,0,0,20);

            tg.addView(tr, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }



        LinearLayout c3 = (LinearLayout) getView().findViewById(R.id.view);
        c1 = (LinearLayout) getView().findViewById(R.id.check1);
        c2 = (LinearLayout) getView().findViewById(R.id.check2);
        c3.setVisibility(View.VISIBLE);


    }

    public void addHeaders() {

        /** Create a TableRow dynamically **/
        tr = new TableRow(getActivity());
        // tr.setBackgroundColor(Color.argb(196, 18, 86, 136));

        tr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        // addListenerOnChkIos();

        /** Creating a TextView to add to the row **/
        Parameter = new TextView(getActivity());
        Parameter.setText(" Parameter ");
        Parameter.setTextColor(Color.WHITE);
        Parameter.setTextSize(20);
        Parameter.setBackgroundResource(R.drawable.header);
        Parameter.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        Parameter.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        Parameter.setPadding(5, 5, 5, 0);
        tr.addView(Parameter);  // Adding textView to tablerow.

        if (actual.isChecked()) {

            if (Ppm.isChecked()) {

                A_PPM = new TextView(getActivity());
                A_PPM.setText("Actual PPM ");
                A_PPM.setTextColor(Color.WHITE);
                A_PPM.setTextSize(20);
                A_PPM.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                A_PPM.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                A_PPM.setPadding(5, 5, 5, 0);
                A_PPM.setBackgroundResource(R.drawable.header);
                tr.addView(A_PPM);  // Adding textView to tablerow.

            }
            if (Mg.isChecked()) {
                A_MG = new TextView(getActivity());
                A_MG.setText("Actual MG ");
                A_MG.setTextColor(Color.WHITE);
                A_MG.setTextSize(20);
                A_MG.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                A_MG.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                A_MG.setPadding(5, 5, 5, 0);
                A_MG.setBackgroundResource(R.drawable.header);
                tr.addView(A_MG);  // Adding textView to tablerow.
            }
        }
        if (correct.isChecked()) {
            if (Ppm.isChecked()) {

                C_PPM = new TextView(getActivity());
                C_PPM.setText(" Corrected PPM ");
                C_PPM.setTextColor(Color.WHITE);
                C_PPM.setTextSize(20);
                C_PPM.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                C_PPM.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                C_PPM.setPadding(5, 5, 5, 0);
                C_PPM.setBackgroundResource(R.drawable.header);
                tr.addView(C_PPM);  // Adding textView to tablerow.
            }
            if (Mg.isChecked()) {
                C_MG = new TextView(getActivity());
                C_MG.setText(" Corrected MG ");
                C_MG.setTextColor(Color.WHITE);
                C_MG.setTextSize(20);
                C_MG.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                C_MG.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                C_MG.setPadding(5, 5, 5, 0);
                C_MG.setBackgroundResource(R.drawable.header);
                tr.addView(C_MG);  // Adding textView to tablerow.
            }
        }
        // Add the TableRow to the TableLayout
        th.addView(tr, new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));


    }

    //***************************************************

    public ArrayList<Actual_corrected_model>calculationForData(){
        System.out.print("\n inside asyn task of current data");
        ArrayList<Actual_corrected_model> Actual_Correct=new ArrayList<Actual_corrected_model>();
        DatabaseHandler db = new DatabaseHandler(getActivity());
        ArrayList<Double> ConversionValue = new ArrayList<Double>();
        ArrayList<GainOffset> Conversion = new ArrayList<GainOffset>();
        //  ConversionValue = db.getConversionValues(paramname);
        Conversion = db.getConversionValues();

        for(int i=0;i<paramname.size();i++){
            for(int j=0;j<Conversion.size();j++){
                if(Conversion.get(j).getParam().equalsIgnoreCase(paramname.get(i))){
                    ConversionValue.add(Conversion.get(j).getGain());
                    break;
                }
            }

        }

     /*   for(int i=0;i<ConversionValue.size();i++){
            System.out.println("CCCCC : " + ConversionValue.get(i));
        }*/

        Long timestamp_array = db.CurrentTime();
        ArrayList<CurrentDataModel> Data = db.Currentparamvalue(timestamp_array);
        int col = Data.size();
        //-------------------------------------------------
        // calculation of mg
        //formula for Actual mg=mg value for gas = gas (ppm value) * conversion value of the gas
        //getCo2 have conversion value of parameter


        //-------------------------------------------------
        //calculating corrected parameter
        //formula for ppm=[20.95-o2(ref) ] ]  /  [20.95-o2(ref)]* (actual ppm value of gas)
        //formula for mg =[20.95-[o2(ref)  ]  /  [20.95-o2(ref)]* (actual mg value of gas)]
        Double[] ActualMG = new Double[col];
        Double[] CorrectMG = new Double[col];
        Double[] CorrectPPm = new Double[col];
        Double ppm, mg;
        try {
            System.out.println("inside of current asyn "+col);
            for (int i = 0; i < col; i++) {
                if(i==N)
                    break;
                Actual_corrected_model ac = new Actual_corrected_model();
                Double amg = Data.get(i).getValue() * ConversionValue.get(i);
                // ActualMG[i] = amg;
                ac.setA_mg(amg);
                System.out.println("Actual  :" + amg);
                ppm = (((20.95 - 11) / (20.95 - Data.get(i).getO2())) * Data.get(i).getValue());
                // CorrectPPm[i] = ppm;
                ac.setC_ppm(ppm);
                mg = (((20.95 - 11) / (20.95 - Data.get(i).getO2())) * amg);
                //  CorrectMG[i] = mg;
                ac.setC_mg(mg);
                Actual_Correct.add(ac);
                System.out.println("mg :" + mg + "    PPm   " + ppm);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Actual_Correct;
    }

//    @Override
//    public void run() {
//        while(true)
//        {  try {
//            Thread.sleep(5000);
////            AndroidListFragmentActivity.strup++;
//            getActivity().runOnUiThread(new Runnable() { //Use the runOnUIThread method to do your UI hanlding in the UI Thread
//                public void run()   {
//                    setView();
//                    Toast.makeText(getActivity(), "" +
//                            "", Toast.LENGTH_LONG).show();
////                    MyListFragment1 fragmentB = (MyListFragment1)getFragmentManager().findFragmentById(R.id.fragment1);
////                    fragmentB.updatefrag();
//                }
//            });
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        }
//    }


    private class Calculations extends AsyncTask<String, Void, ArrayList<Actual_corrected_model>> {

        @Override
        public ArrayList<Actual_corrected_model> doInBackground(String... urls) {
            System.out.println("inside backgraoung");

            return calculationForData();
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(ArrayList<Actual_corrected_model>  result) {
            System.out.println("inside post");
            //pdialog.dismiss();
            tl.removeAllViews();
            // tg.removeAllViews();

            addHeaders();

            addData(result);



        }
    }
    //******************************************************
    public void addData(ArrayList<Actual_corrected_model> ActualC) {}

    /**
     * This function add the data to the table
     **/
    public void addData() {
        try {
            DatabaseHandler db = new DatabaseHandler(getActivity());

            Long timestamp_array = db.CurrentTime();
            String date1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp_array ));
            date.setText("Last Updated :    "+date1);
            ArrayList<CurrentDataModel> Data = db.Currentparamvalue(timestamp_array);
            int col = Data.size();


            ArrayList<Double> ConversionValue = new ArrayList<Double>();
            //  ConversionValue = db.getConversionValues(paramname);

            ArrayList<GainOffset> Conversion = new ArrayList<GainOffset>();
            //  ConversionValue = db.getConversionValues(paramname);
            Conversion = db.getConversionValues();

            for(int i=0;i<paramname.size();i++){
                for(int j=0;j<Conversion.size();j++){
                    if(Conversion.get(j).getParam().equalsIgnoreCase(paramname.get(i))){
                        ConversionValue.add(Conversion.get(j).getGain());
                        break;
                    }
                }

            }

            for(int i=0;i<ConversionValue.size();i++){
                System.out.println("CCCCC : "+ConversionValue.get(i));
            }

            //-------------------------------------------------
            // calculation of mg
            //formula for Actual mg=mg value for gas = gas (ppm value) * conversion value of the gas
            //getCo2 have conversion value of parameter


            //-------------------------------------------------
            //calculating corrected parameter
            //formula for ppm=[20.95-o2(ref) ] ]  /  [20.95-o2(act)]* (actual ppm value of gas)
            //formula for mg =[20.95-[o2(ref)  ]  /  [20.95-o2(act)]* (actual mg value of gas)]
            Double[] ActualMG = new Double[col];
            Double[] CorrectMG = new Double[col];
            Double[] CorrectPPm = new Double[col];
            Double ppm, mg;
            try {
                System.out.println("inside of current asyn "+col);
                System.out.print("N is "+N);
                for (int i = 0; i < col; i++) {
                    if(i==N)
                        break;
                    // Actual_corrected_model ac = new Actual_corrected_model();
                    Double amg = Data.get(i).getValue() * ConversionValue.get(i);
                    ActualMG[i] = amg;
                    //ac.setA_mg(amg);
                    System.out.println("Actual  :" + amg);
                    ppm = (((20.95 - 11) / (20.95 - Data.get(i).getO2())) * Data.get(i).getValue());
                    CorrectPPm[i] = ppm;
                    // ac.setC_ppm(ppm);
                    mg = (((20.95 - 11) / (20.95 - Data.get(i).getO2())) * amg);
                    CorrectMG[i] = mg;
                    // ac.setC_mg(mg);
                    // Actual_Correct.add(ac);
                    System.out.println("mg :" + mg + "    PPm   " + ppm);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            System.out.print("col count..: " + Data.size());
            System.out.print("\n............. data size........... " + col);

            actual = (CheckBox) getView().findViewById(R.id.actual);
            correct = (CheckBox) getView().findViewById(R.id.currect);
            Mg = (CheckBox) getView().findViewById(R.id.mg);
            Ppm = (CheckBox) getView().findViewById(R.id.ppm);

            for (int i = 0; i < N; i++) {
                /** Create a TableRow dynamically **/
                tr = new TableRow(getActivity());
                tr.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                /** Creating a TextView to add to the row **/
                Parameter = new TextView(getActivity());
                Parameter.setText("" + Data.get(i).getParamName() + " ");
                Parameter.setTextColor(Color.argb(208, 45, 136, 82));
                Parameter.setTextSize(20);
                Parameter.setBackgroundResource(R.drawable.r);
                Parameter.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                Parameter.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                Parameter.setPadding(5, 5, 5, 0);
                tr.addView(Parameter);  // Adding textView to tablerow.

                if (actual.isChecked()) {

                    if (Ppm.isChecked()) {
                        A_PPM = new TextView(getActivity());
                        A_PPM.setText("" + new DecimalFormat("##.##").format(Data.get(i).getValue()) + " ");
                        A_PPM.setTextColor(Color.argb(208, 45, 136, 82));
                        A_PPM.setTextSize(20);
                        A_PPM.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                        A_PPM.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                        A_PPM.setPadding(5, 5, 5, 0);
                        A_PPM.setBackgroundResource(R.drawable.r);
                        tr.addView(A_PPM);  // Adding textView to tablerow.
                    }
                    if (Mg.isChecked()) {

                        A_MG = new TextView(getActivity());
                        A_MG.setText("" + new DecimalFormat("##.##").format(ActualMG[i]) + " ");
                        A_MG.setTextColor(Color.argb(208, 45, 136, 82));
                        A_MG.setTextSize(20);
                        A_MG.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                        A_MG.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                        A_MG.setPadding(5, 5, 5, 0);
                        A_MG.setBackgroundResource(R.drawable.r);
                        tr.addView(A_MG);  // Adding textView to tablerow.
                    }
                }
                if (correct.isChecked()) {
                    if (Ppm.isChecked()) {

                        C_PPM = new TextView(getActivity());
                        C_PPM.setText("" + new DecimalFormat("##.##").format(CorrectPPm[i]) + " ");
                        C_PPM.setTextColor(Color.argb(208, 45, 136, 82));
                        C_PPM.setTextSize(20);
                        C_PPM.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                        C_PPM.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                        C_PPM.setPadding(5, 5, 5, 0);
                        C_PPM.setBackgroundResource(R.drawable.r);
                        tr.addView(C_PPM);  // Adding textView to tablerow.
                    }
                    if (Mg.isChecked()) {
                        C_MG = new TextView(getActivity());
                        C_MG.setText("" + new DecimalFormat("##.##").format(CorrectMG[i]) + " ");
                        C_MG.setTextColor(Color.argb(208, 45, 136, 82));
                        C_MG.setTextSize(20);
                        C_MG.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                        C_MG.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                        C_MG.setPadding(5, 5, 5, 0);
                        C_MG.setBackgroundResource(R.drawable.r);
                        tr.addView(C_MG);  // Adding textView to tablerow.
                    }
                }
                // Add the TableRow to the TableLayout
                tl.addView(tr, new TableLayout.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            Toast.makeText(getActivity(), " ", Toast.LENGTH_LONG).show();
        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(getActivity(), "you can not deactivate all parameters", Toast.LENGTH_LONG).show();
        }catch (NullPointerException e){
            e.printStackTrace();
        }


    }

}
