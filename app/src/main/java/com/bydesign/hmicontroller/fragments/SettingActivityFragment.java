package com.bydesign.hmicontroller.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bydesign.hmicontroller.Service.DatabaseHandler;
import com.bydesign.hmicontroller.Service.OOM;
import com.bydesign.hmicontroller.model.Logs_Model;
import com.bydesign.hmicontroller.model.Settings_Model;

import java.util.ArrayList;
import java.util.List;

import info.bydesign.hmicontroller.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class SettingActivityFragment extends Fragment {

    SettingActivityFragment settingActivityFragment;
    Fragment fragment=null;
    List<Settings_Model> list = new ArrayList<Settings_Model>();
    TableLayout tl;
    TableRow tr;
    Button _Addnew;
    Button _Backup;
    Button _Save;
    Button _Set;
    TextView _Gain;
    TextView _Offset;
    TextView _MinGain;
    TextView _MinOffset;
    TextView _MaxGain;
    TextView _MaxOffset;
    TextView _Range;
    TextView _Resolution;
    TextView _Status;
    String paramname;
    Spinner _url;

    AutoCompleteTextView text;
    //Reference variabe to database....
    DatabaseHandler databaseHandler;
    public SettingActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Runtime.getRuntime().gc();
        System.gc();
        //OOM PROTECTION
        Thread.currentThread().setDefaultUncaughtExceptionHandler(new OOM.MyUncaughtExceptionHandler());
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        tl = (TableLayout) getView().findViewById(R.id.maintable);

        /*getting id's for customizing header's visibility*/
        _Gain= (TextView) getView().findViewById(R.id.gain);
        _Offset = (TextView) getView().findViewById(R.id.offset);
     /*   _MaxGain= (TextView) getView().findViewById(R.id.maxgain);
        _MaxOffset= (TextView) getView().findViewById(R.id.maxoffset);
        _MinGain = (TextView) getView().findViewById(R.id.mingain);
        _MinOffset = (TextView) getView().findViewById(R.id.minoffset);*/
        _Range = (TextView) getView().findViewById(R.id.range);
        _Resolution = (TextView) getView().findViewById(R.id.resolution);
        _Status = (TextView) getView().findViewById(R.id.status);
//        text=(AutoCompleteTextView)getView().findViewById(R.id.urls);
//        text.setText("");
//        _Save = (Button) getView().findViewById(R.id.saveurl);
//        _Set = (Button) getView().findViewById(R.id.seturl);
//        databaseHandler = new DatabaseHandler(getActivity());
//        ArrayList<String> url_list =new ArrayList<>();
//        url_list = databaseHandler.Get_AllUrl();
//       /* url_list.add("abc");
//        url_list.add("http://180.179.236.222:8080/smartcity/gassensor/register");*/
//
//        //=================================================================================================================================
//
//
//        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,url_list);
//
//        text.setAdapter(adapter);
//        text.setThreshold(1);
//
//        _Save.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (!text.getText().toString().isEmpty()) {
//                    databaseHandler = new DatabaseHandler(getActivity());
//                    databaseHandler.Save_Url(text.getText().toString());
//                    Toast.makeText(getActivity(), " URL saved ", Toast.LENGTH_SHORT).show();
//                } else
//                    Toast.makeText(getActivity(), " Please enter valid URL ", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        _Set.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
//                if(!text.getText().toString().isEmpty()){
//                    databaseHandler.Reset_Url();
//                    databaseHandler.Set_Url(text.getText().toString());
//               /* SimpleTabsActivity simpleTabsActivity = new SimpleTabsActivity();
//                simpleTabsActivity.Reset_Token();
//                */
//                    Intent in = new Intent(getActivity(), SimpleTabsActivity.class);
//                    startActivity(in);
//                }
//               else
//                Toast.makeText(getActivity(),"Please enter valid URL ",Toast.LENGTH_SHORT).show();
//            }
//        });
        //=================================================================================================================================
/*
        *//*
        *Creating adapter for spinner
        * *//*
        _url = (Spinner) getView().findViewById(R.id.url);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, url_list);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        _url.setAdapter(dataAdapter);*/




        /*spinner ============================================================================================================================*/

//adding new parameter
        _Addnew = (Button)getView().findViewById(R.id.addnew);
        _Addnew.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Addnew();
            }


        });
        //backup button
        _Backup  = (Button)getView().findViewById(R.id.backup);
        _Backup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Backup();
            }


        });
        //Retrieving ParameterValues List From Database....
        databaseHandler = new DatabaseHandler(getActivity());
        list = databaseHandler.Retrieve_parametervalue();

        //Getting Shared Preferences......
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("HmiController", Context.MODE_PRIVATE);
        int level = sharedPreferences.getInt("userlevel", 0);

        addHeaders(level);
        addData(list, level);
    }
    //Database Export
    private void Backup() {
        if (databaseHandler.exportDB()) {
            Toast.makeText(getActivity(), "file saved into /storage/sdcard/AceBackupFile", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "file could not saved", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * This function add the headers to the table
     **/
    public void addHeaders(int level) {

         /*
        * Visibilty Condition
        * */

        if (level < 3) {
           /* _MinGain.setVisibility(View.INVISIBLE);
            _MaxGain.setVisibility(View.INVISIBLE);
            _MinOffset.setVisibility(View.INVISIBLE);
            _MaxOffset.setVisibility(View.INVISIBLE);*/
            _Range.setVisibility(View.INVISIBLE);
            _Resolution.setVisibility(View.INVISIBLE);
            _Addnew.setVisibility(View.INVISIBLE);
            _Status.setVisibility(View.INVISIBLE);
         //   text.setVisibility(View.INVISIBLE);
//            _Save.setVisibility(View.INVISIBLE);
//            _Set.setVisibility(View.INVISIBLE);


            if (level == 1) {
                _Offset.setVisibility(View.INVISIBLE);
                _Gain.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * This function add the data to the table
     *
     * @param list
     **/
    public void addData(final List<Settings_Model> list, final int level) {
        try{
            Settings_Model sm;
            for (int i = 0; i < list.size(); i++) {
                /** Create a TableRow dynamically **/
                sm = list.get(i);
                tr = new TableRow(getActivity());
                tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                //Variable
                final String x = sm.getParamName();

                /** Creating a TextView to add to the row **/
                final TextView ParamName = new TextView(getActivity());
                ParamName.setText(sm.getParamName());
                ParamName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                ParamName.setTextColor(Color.argb(208, 45, 136, 82));
                ParamName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                ParamName.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                ParamName.setPadding(5, 5, 5, 0);
                ParamName. setBackgroundResource(R.drawable.row);
                tr.addView(ParamName);  // Adding textView to tablerow.

                /** Creating another textview **/
                final EditText Unit = new EditText(getActivity());
                Unit.setText(sm.getUnit());
                Unit.setTextColor(Color.argb(208, 45, 136, 82));
                Unit.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                Unit.setPadding(5, 5, 5, 0);
                Unit. setBackgroundResource(R.drawable.row);
                Unit.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(Unit); // Adding textView to tablerow.

                final EditText Alarm1 = new EditText(getActivity());

                Alarm1.setInputType(InputType.TYPE_CLASS_NUMBER);
                Alarm1.setText(String.valueOf(sm.getAlarm1()));
                Alarm1.setTextColor(Color.argb(208, 45, 136, 82));
                Alarm1.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                Alarm1.setPadding(5, 5, 5, 0);
                Alarm1. setBackgroundResource(R.drawable.row);
                Alarm1.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(Alarm1);

                final EditText Alarm2 = new EditText(getActivity());
                Alarm2.setInputType(InputType.TYPE_CLASS_NUMBER);
                Alarm2.setText(String.valueOf(sm.getAlarm2()));
                Alarm2.setTextColor(Color.argb(208, 45, 136, 82));
                Alarm2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                Alarm2.setPadding(5, 5, 5, 0);
                Alarm2. setBackgroundResource(R.drawable.row);
                Alarm2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(Alarm2);

                final EditText Gain = new EditText(getActivity());
                Gain.setInputType(InputType.TYPE_CLASS_NUMBER);
                Gain.setText(String.valueOf(sm.getGain()));
                Gain.setTextColor(Color.argb(208, 45, 136, 82));
                Gain.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                Gain.setPadding(5, 5, 5, 0);
                Gain. setBackgroundResource(R.drawable.row);
                Gain.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(Gain);

                final EditText Offset = new EditText(getActivity());
                Offset.setInputType(InputType.TYPE_CLASS_NUMBER);
                Offset.setText(String.valueOf(sm.getOffset()));
                Offset.setTextColor(Color.argb(208, 45, 136, 82));
                Offset.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                Offset.setPadding(5, 5, 5, 0);
                Offset. setBackgroundResource(R.drawable.row);
                Offset.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(Offset);

            /*    final EditText MinOffset = new EditText(getActivity());
                MinOffset.setText(String.valueOf(sm.getMinOffset()));
                MinOffset.setTextColor(Color.argb(208, 45, 136, 82));
                MinOffset.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                MinOffset.setPadding(5, 5, 5, 0);
                MinOffset.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(MinOffset);

                final EditText MaxOffset = new EditText(getActivity());
                MaxOffset.setText(String.valueOf(sm.getMaxOffset()));
                MaxOffset.setTextColor(Color.argb(208, 45, 136, 82));
                MaxOffset.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                MaxOffset.setPadding(5, 5, 5, 0);
                MaxOffset.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(MaxOffset);

                final EditText MinGain = new EditText(getActivity());
                MinGain.setText(String.valueOf(sm.getMinGain()));
                MinGain.setTextColor(Color.argb(208, 45, 136, 82));
                MinGain.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                MinGain.setPadding(5, 5, 5, 0);
                MinGain.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(MinGain);

                final EditText MaxGain = new EditText(getActivity());
                MaxGain.setText(String.valueOf(sm.getMaxGain()));
                MaxGain.setTextColor(Color.argb(208, 45, 136, 82));
                MaxGain.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                MaxGain.setPadding(5, 5, 5, 0);
                MaxGain.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(MaxGain);
*/
                /** Creating another textview **/
                final EditText Range = new EditText(getActivity());
                Range.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                Range.setText(sm.getRange());
                Range.setTextColor(Color.argb(208, 45, 136, 82));
                Range.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                Range.setPadding(5, 5, 5, 0);
                Range. setBackgroundResource(R.drawable.row);
                Range.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(Range); // Adding textView to tablerow.

                final EditText Resolution = new EditText(getActivity());
                Resolution.setText(sm.getResolution());
                Resolution.setTextColor(Color.argb(208, 45, 136, 82));
                Resolution.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                Resolution.setPadding(5, 5, 5, 0);
                Resolution. setBackgroundResource(R.drawable.row);
                Resolution.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tr.addView(Resolution);




                //Toggle Button Implementation
                final ToggleButton delete = (ToggleButton) new ToggleButton(getActivity());
                final Settings_Model settings_model= (Settings_Model) databaseHandler.getMode(ParamName.getText().toString());
                if(settings_model.getMode().equalsIgnoreCase("active")){
                    delete.setText(settings_model.getMode());
                }
                else{delete.setText(settings_model.getMode());}

                delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            databaseHandler = new DatabaseHandler(getActivity());
                            databaseHandler.Delete_parametervalues(ParamName.getText().toString(), "Deactive");
                            Toast.makeText(getActivity(), "Parameter Deactivated", Toast.LENGTH_SHORT).show();
                            final Settings_Model settings_model2 = (Settings_Model) databaseHandler.getMode(ParamName.getText().toString());
                            delete.setTextOn(settings_model2.getMode());
                            delete.setBackgroundResource(R.drawable.button_blue);
                            delete.setTextColor(Color.WHITE);
                            Logs_Insertion("Parameter '" + ParamName.getText().toString() + "'  Deactivated ");
                        } else {

                            databaseHandler = new DatabaseHandler(getActivity());
                            databaseHandler.Delete_parametervalues(ParamName.getText().toString(), "Active");
                            Toast.makeText(getActivity(), "Parameter Activated", Toast.LENGTH_SHORT).show();
                            final Settings_Model settings_model1 = (Settings_Model) databaseHandler.getMode(ParamName.getText().toString());
                            delete.setTextOff(settings_model1.getMode());
                            delete.setBackgroundResource(R.drawable.button_blue);
                            delete.setTextColor(Color.WHITE);
                            Logs_Insertion("Parameter '" + ParamName.getText().toString() + "'  Activated ");
                            //

                        }

                    }
                });
                delete.setBackgroundResource(R.drawable.button_blue);
                delete.setTextColor(Color.WHITE);


                tr.addView(delete);




                TextView Space1 = new TextView(getActivity());
                Space1.setText("   ");
                tr.addView(Space1);


                //Update Parameter Button Implementation
                Button update = new Button(getActivity());
                update.setText("update");
                update.setTextSize(15);

                update.setBackgroundResource(R.drawable.button_blue);
                update.setId(i);
                update.setTextColor(Color.WHITE);
                update.callOnClick();

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {


                            Settings_Model finalSm = new Settings_Model(ParamName.getText().toString(), Range.getText().toString(), Double.parseDouble(Alarm1.getText().toString()), Double.parseDouble(Alarm2.getText().toString()), Double.parseDouble(Gain.getText().toString()), Double.parseDouble(Offset.getText().toString()), Resolution.getText().toString(), Unit.getText().toString(), "Active");

                            databaseHandler = new DatabaseHandler(getActivity());
                            databaseHandler.Update_parametervalues(finalSm);
                            Toast.makeText(getActivity(), "Parameter Modified", Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(SettingsActivity.this,SettingsActivity.class);
//                        startActivity(intent);
                            Logs_Insertion("Parameter '" + ParamName.getText().toString() + "' Modified");
                        }
                        catch (NumberFormatException e){
                            Toast.makeText(getActivity(), "please enter numeric values", Toast.LENGTH_LONG).show();
                        }}
                });
                tr.addView(update);

                tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                // we are adding two textviews for the divider because we have two columns
                tr = new TableRow(getActivity());
                tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                TextView Space2 = new TextView(getActivity());
                Space2.setText("   ");
                tr.addView(Space2);
                tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                /*
        * Visibilty Condition
        * */

                if (level < 3) {
                  /*  MinGain.setVisibility(View.INVISIBLE);
                    MaxGain.setVisibility(View.INVISIBLE);
                    MinOffset.setVisibility(View.INVISIBLE);
                    MaxOffset.setVisibility(View.INVISIBLE);*/
                    Range.setVisibility(View.INVISIBLE);
                    Resolution.setVisibility(View.INVISIBLE);
                    delete.setVisibility(View.INVISIBLE);
                    // update.setVisibility(View.INVISIBLE);
                    Unit.setEnabled(false);
                   /* Alarm1.setEnabled(false);
                    Alarm2.setEnabled(false);*/
                    Offset.setEnabled(false);
                    Gain.setEnabled(false);


                    if (level == 1) {
                        Offset.setVisibility(View.INVISIBLE);
                        Gain.setVisibility(View.INVISIBLE);
                        Alarm1.setEnabled(false);
                        Alarm2.setEnabled(false);
                        update.setVisibility(View.INVISIBLE);
                    }

                }
            }

            //


        }catch (IndexOutOfBoundsException e){
            Toast.makeText(getActivity(),"something went wrong on settings page",Toast.LENGTH_LONG).show();

        }



    }
    public   void ad(){
        Toast.makeText(getActivity(),"i m toast ",Toast.LENGTH_SHORT).show();
    }

    private void Addnew()  {

        /** Creating a TableRow dynamically **/
        tr = new TableRow(getActivity());
        tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        /** Creating a TextView to add to the row **/
        final EditText ParamName = new EditText(getActivity());
        ParamName.setHint("Parameter");
        ParamName.setHintTextColor(Color.BLACK);
        ParamName.setTextColor(Color.BLUE);
        ParamName.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
        ParamName.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        ParamName.setPadding(5, 5, 5, 0);
        tr.addView(ParamName);  // Adding textView to tablerow.

        /** Creating another textview **/
        final EditText Unit = new EditText(getActivity());
        Unit.setHint("Unit");
        Unit.setHintTextColor(Color.BLACK);
        Unit.setTextColor(Color.BLUE);
        Unit.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Unit.setPadding(5, 5, 5, 0);
        Unit.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
        tr.addView(Unit); // Adding textView to tablerow.


        final EditText Alarm1 = new EditText(getActivity());
        Alarm1.setHint("Alarm1");
        Alarm1.setInputType(InputType.TYPE_CLASS_NUMBER);
        Alarm1.setHintTextColor(Color.BLACK);
        Alarm1.setTextColor(Color.BLUE);
        Alarm1.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Alarm1.setPadding(5, 5, 5, 0);
        Alarm1.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
        tr.addView(Alarm1);

        final EditText Alarm2 = new EditText(getActivity());
        Alarm2.setHint("Alarm2");
        Alarm2.setInputType(InputType.TYPE_CLASS_NUMBER);
        Alarm2.setHintTextColor(Color.BLACK);
        Alarm2.setTextColor(Color.BLUE);
        Alarm2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Alarm2.setPadding(5, 5, 5, 0);
        Alarm2.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
        tr.addView(Alarm2);

        final EditText Gain = new EditText(getActivity());
        Gain.setHint("Gain");
        Gain.setHintTextColor(Color.BLACK);
        Gain.setInputType(InputType.TYPE_CLASS_NUMBER);
        Gain.setTextColor(Color.BLUE);
        Gain.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Gain.setPadding(5, 5, 5, 0);
        Gain.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
        tr.addView(Gain);

        final EditText Offset = new EditText(getActivity());
        Offset.setHint("Offset");
        Offset.setInputType(InputType.TYPE_CLASS_NUMBER);
        Offset.setHintTextColor(Color.BLACK);
        Offset.setTextColor(Color.BLUE);
        Offset.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Offset.setPadding(5, 5, 5, 0);
        Offset.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
        tr.addView(Offset);

     /*   final EditText MinOffset = new EditText(getActivity());
        MinOffset.setHint("MinOffset");
        MinOffset.setHintTextColor(Color.BLACK);
        MinOffset.setTextColor(Color.BLUE);
        MinOffset.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        MinOffset.setPadding(5, 5, 5, 0);
        MinOffset.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
        tr.addView(MinOffset);

        final EditText MaxOffset = new EditText(getActivity());
        MaxOffset.setHint("MaxOffset");
        MaxOffset.setHintTextColor(Color.BLACK);
        MaxOffset.setTextColor(Color.BLUE);
        MaxOffset.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        MaxOffset.setPadding(5, 5, 5, 0);
        MaxOffset.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
        tr.addView(MaxOffset);

        final EditText MinGain = new EditText(getActivity());
        MinGain.setHint("MinGain");
        MinGain.setHintTextColor(Color.BLACK);
        MinGain.setTextColor(Color.BLUE);
        MinGain.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        MinGain.setPadding(5, 5, 5, 0);
        MinGain.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
        tr.addView(MinGain);

        final EditText MaxGain = new EditText(getActivity());
        MaxGain.setHint("MaxGain");
        MaxGain.setHintTextColor(Color.BLACK);
        MaxGain.setTextColor(Color.BLUE);
        MaxGain.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        MaxGain.setPadding(5, 5, 5, 0);
        MaxGain.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
        tr.addView(MaxGain);
*/
        /** Creating another textview **/
        final EditText Range = new EditText(getActivity());
        Range.setHint("Range");
        Range.setHintTextColor(Color.BLACK);
        Range.setTextColor(Color.BLUE);
        Range.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Range.setPadding(5, 5, 5, 0);
        Range.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
        tr.addView(Range); // Adding textView to tablerow.

        final EditText Resolution = new EditText(getActivity());
        Resolution.setHint("Resolution");
        Resolution.setHintTextColor(Color.BLACK);
        Resolution.setTextColor(Color.BLUE);
        Resolution.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Resolution.setPadding(5, 5, 5, 0);
        Resolution.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
        tr.addView(Resolution);

        Button Add = new Button(getActivity());
        Add.setText("Add");

        Add.setBackgroundResource(R.drawable.button_green);
        ;
        //Add.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
        Add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!ParamName.getText().toString().isEmpty()&& !Range.getText().toString().isEmpty()&& !Resolution.getText().toString().isEmpty()) {
                    try {
                        Settings_Model AddNewParam = new Settings_Model(ParamName.getText().toString(), Range.getText().toString(), Double.parseDouble(Alarm1.getText().toString()), Double.parseDouble(Alarm2.getText().toString()), Double.parseDouble(Gain.getText().toString()), Double.parseDouble(Offset.getText().toString()), Resolution.getText().toString(), Unit.getText().toString(), "Active");
                        databaseHandler = new DatabaseHandler(getActivity());
                        String status=  databaseHandler.Inser_parametervalues(AddNewParam);
                        // startActivity(getActivity());
                        if(status.equalsIgnoreCase("success"))  {
                            paramname=ParamName.getText().toString();
                            //  databaseHandler.addExtraParamtoSensorTable(ParamName.getText().toString());
                            new Calculations().execute(ParamName.getText().toString());
                            Toast.makeText(getActivity(), "Parameter "+ ParamName.getText().toString()+ " Added", Toast.LENGTH_LONG).show();
                            Logs_Insertion("New Parameter '" + ParamName.getText().toString() + "' Added");
                            tl.removeAllViews();
                            onStart();
                        }
                        else if(status.equalsIgnoreCase("saved")){
                            Toast.makeText(getActivity(), "Parameter Already Exist ", Toast.LENGTH_LONG).show();
                        }
                        else{Toast.makeText(getActivity(), "Match not found", Toast.LENGTH_LONG).show();
                            Logs_Insertion("New Parameter '" + ParamName.getText().toString() + "'not added");}
                    } catch (NumberFormatException e) {
                        Toast.makeText(getActivity(), "all fields are mandatory", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "all fields are mandatory", Toast.LENGTH_LONG).show();
                }
                //databaseHandler.addExtraParamtoSensorTable(ParamName.getText().toString());
            }
        });
        tr.addView(Add);
        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

    }
    public static Fragment newInstance()
    {
        SettingActivityFragment myFragment = new SettingActivityFragment();
        return myFragment;
    }

    // Logs Table Insertion

    public void Logs_Insertion(String Event) {
        //Getting Shared Preferences......
        //Session Management to get Userlevel
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("HmiController", Context.MODE_PRIVATE);
        int level = sharedPreferences.getInt("userlevel", 0);
        String username = sharedPreferences.getString("username", null);
        Logs_Model logsModel = new Logs_Model();
        logsModel.setUsername(username);
        logsModel.setEvent("event2");
        logsModel.setOperation_string(Event);
        databaseHandler.Insert_logs(logsModel);



    }

    //***************************** asyn task for storing data in db(geeta part)===============================
    public String StoreData(String ParamName){
        databaseHandler.addExtraParamtoSensorTable(ParamName);
        return "success";
    }
    private  class Calculations extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... urls) {

            System.out.print("inside of jdhsgzfdfjkgfj doinbackgroung");
            System.out.print("param to be add :"+paramname);

            return  StoreData(paramname);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String  result) {
            if(result.equalsIgnoreCase("success")){
                System.out.print("success to enter new values from setting");

            }
            else
                System.out.print("fail to enter new values from setting");
        }
    }
    //*********************************************************
}
