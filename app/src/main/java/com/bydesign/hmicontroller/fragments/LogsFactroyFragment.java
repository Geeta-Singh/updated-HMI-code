package com.bydesign.hmicontroller.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bydesign.hmicontroller.Service.DatabaseHandler;
import com.bydesign.hmicontroller.Service.OOM;
import com.bydesign.hmicontroller.model.LogsModel;

import java.util.ArrayList;

import info.bydesign.hmicontroller.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class LogsFactroyFragment extends Fragment {

    TableLayout tl, tg, th;
    TableRow tr;
    TextView User;
    TextView Time;
    TextView oper;
    TextView opcode;
    TextView Sr;

    public LogsFactroyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_logs_factroy, container, false);
    }


    @Override
    public void onStart() {

        super.onStart();
        Runtime.getRuntime().gc();
        System.gc();
        //OOM PROTECTION
        Thread.currentThread().setDefaultUncaughtExceptionHandler(new OOM.MyUncaughtExceptionHandler());
        tl = (TableLayout) getView().findViewById(R.id.maintable);
      //  tg = (TableLayout) getView().findViewById(R.id.StringTable);
        th = (TableLayout) getView().findViewById(R.id.maintableH);
        ShowLoginLogoutDetail();

    }

    public void ShowLoginLogoutDetail() {

        //addDataLogin();
        ShowOperationDetail();

    }

    public void ShowOperationDetail() {
         addHeaders();
        tl.removeAllViews();
        addData();
    }

    public void addHeaders() {
        tr = new TableRow(getActivity());
        tr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        Sr = new TextView(getActivity());
        // String srno=Integer.toString(i+1);;
        Sr.setText("Sr No. ");

        Sr.setTextSize(20);
        Sr.setTextColor(Color.WHITE);
        Sr.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        Sr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        Sr.setPadding(5, 5, 5, 5);
        Sr.setBackgroundResource(R.drawable.sr_header);
        tr.addView(Sr);  //
        // Adding textView to tablerow.

        /** Creating another textview **/
        Time = new TextView(getActivity());
        Time.setText("  Timestamp     ");
        Time.setTextColor(Color.WHITE);
        Time.setTextSize(20);
        Time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        Time.setPadding(5, 5, 5, 5);
        Time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        Time.setBackgroundResource(R.drawable.header);
        tr.addView(Time); // Adding textView to tablerow.

        /** Creating a TextView to add to the row **/
        User = new TextView(getActivity());
        User.setText("   User   ");
        User.setTextColor(Color.WHITE);
        User.setTextSize(20);
        User.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        User.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        User.setPadding(5, 5, 5, 5);
        User.setBackgroundResource(R.drawable.header);
        tr.addView(User);  // Adding textView to tablerow.


//        oper = new TextView(getActivity());
//       // oper.setText("    Op Code    ");
//        oper.setTextColor(Color.WHITE);
//        oper.setTextSize(20);
//        oper.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//        oper.setPadding(5, 5, 5, 5);
//        oper.setBackgroundResource(R.drawable.header);
//        oper.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//        tr.addView(oper);

        opcode = new TextView(getActivity());
        opcode.setText("  Operation               ");
        opcode.setTextColor(Color.WHITE);
        opcode.setTextSize(20);
        opcode.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        opcode.setPadding(5, 5, 5, 5);
        opcode.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        opcode.setBackgroundResource(R.drawable.header);
        tr.addView(opcode);

        // Add the TableRow to the TableLayout
//        tl.addView(tr, new TableLayout.LayoutParams(
//                TableRow.LayoutParams.MATCH_PARENT,
//                TableRow.LayoutParams.MATCH_PARENT));
        // Add the TableRow to the TableLayout
        th.addView(tr, new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));

        // we are adding two textviews for the divider because we have two columns


    }

    /**
     * This function add the data to the table
     **/
    public void addData() {
        try {
            ArrayList<LogsModel> logs = new ArrayList<LogsModel>();
            DatabaseHandler db = new DatabaseHandler(getActivity());
            //  int count=db.countLogs();
            logs = db.getLogasDetail();
            int logscount = logs.size();


            for (int i = 0; i < logscount; i++) {
                /** Create a TableRow dynamically **/
                tr = new TableRow(getActivity());
                tr.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

               // if (logs.get(i).getOpcode().equalsIgnoreCase("event1"))

                Sr = new TextView(getActivity());
                String srno = Integer.toString(i + 1);
                Sr.setText("    "+srno + "                  ");
                if (logs.get(i).getOpcode().equalsIgnoreCase("event1"))
                    Sr.setTextColor(Color.argb(255, 18, 86, 136));
                else
                    Sr.setTextColor(Color.argb(208, 45, 136, 82));
                Sr.setTextSize(18);
                Sr.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                Sr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                Sr.setPadding(5, 5, 5, 5);
                tr.addView(Sr);  //


                /** Creating another textview **/
                Time = new TextView(getActivity());
                Time.setText("     "+logs.get(i).getTimestamp());
                if (logs.get(i).getOpcode().equalsIgnoreCase("event1"))
                    Time.setTextColor(Color.argb(255, 18, 86, 136));
                else
                    Time.setTextColor(Color.argb(208, 45, 136, 82));
                Time.setTextSize(18);
                Time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                Time.setPadding(5, 5, 5, 5);
                Time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                Time.setBackgroundResource(R.drawable.r);
                tr.addView(Time); // Adding textView to tablerow.

                /** Creating a TextView to add to the row **/
                User = new TextView(getActivity());
                User.setText("              " + logs.get(i).getUser() + "            ");
                if (logs.get(i).getOpcode().equalsIgnoreCase("event1"))
                    User.setTextColor(Color.argb(255, 18, 86, 136));
                else
                    User.setTextColor(Color.argb(208, 45, 136, 82));
                User.setTextSize(18);
                User.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                User.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                User.setPadding(5, 5, 5, 5);
                User.setBackgroundResource(R.drawable.r);
                tr.addView(User);  // Adding textView to tablerow.


//                oper = new TextView(getActivity());
//                oper.setText("  " + logs.get(i).getOpcode() + "     ");
//                if (logs.get(i).getOpcode().equalsIgnoreCase("event1"))
//                    oper.setTextColor(Color.argb(255, 18, 86, 136));
//                else
//                    oper.setTextColor(Color.argb(208, 45, 136, 82));
//                oper.setTextSize(18);
//                oper.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
//                oper.setPadding(5, 5, 5, 5);
//                oper.setBackgroundResource(R.drawable.r);
//                oper.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//                tr.addView(oper);

                opcode = new TextView(getActivity());
                opcode.setText(" " + logs.get(i).getOper());
                if (logs.get(i).getOpcode().equalsIgnoreCase("event1"))
                    opcode.setTextColor(Color.argb(255, 18, 86, 136));
                else
                    opcode.setTextColor(Color.argb(208, 45, 136, 82));
                opcode.setTextSize(18);
                opcode.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                opcode.setPadding(5, 5, 5, 5);
                opcode.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                opcode.setBackgroundResource(R.drawable.r);
                tr.addView(opcode);

                // Add the TableRow to the TableLayout
                tl.addView(tr, new TableLayout.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }


    public void addDataLogin() {
        try {
            ArrayList<LogsModel> logs = new ArrayList<LogsModel>();
            DatabaseHandler db = new DatabaseHandler(getActivity());
            //  int count=db.countLogs();
            logs = db.getLogasDetailLogin();
            int logscount = logs.size();
            for (int i = 0; i < logscount; i++) {
                /** Create a TableRow dynamically **/
                tr = new TableRow(getActivity());
                tr.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));


                Sr = new TextView(getActivity());
                String srno = Integer.toString(i + 1);
                ;
                Sr.setText(srno);
                Sr.setTextColor(Color.argb(208, 45, 136, 82));
                Sr.setTextSize(18);
                Sr.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                Sr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                Sr.setPadding(5, 5, 5, 5);
                tr.addView(Sr);  //


                /** Creating another textview **/
                Time = new TextView(getActivity());
                Time.setText(logs.get(i).getTimestamp());
                Time.setTextColor(Color.argb(208, 45, 136, 82));
                Time.setTextSize(18);
                Time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                Time.setPadding(5, 5, 5, 5);
                Time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                Time.setBackgroundResource(R.drawable.r);
                tr.addView(Time); // Adding textView to tablerow.

                /** Creating a TextView to add to the row **/
                User = new TextView(getActivity());
                User.setText("  " + logs.get(i).getUser() + " ");
                User.setTextColor(Color.argb(208, 45, 136, 82));
                User.setTextSize(18);
                User.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                User.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                User.setPadding(5, 5, 5, 5);
                User.setBackgroundResource(R.drawable.r);
                tr.addView(User);  // Adding textView to tablerow.


                oper = new TextView(getActivity());
                oper.setText("  " + logs.get(i).getOpcode() + " ");
                oper.setTextColor(Color.argb(208, 45, 136, 82));
                oper.setTextSize(18);
                oper.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                oper.setPadding(5, 5, 5, 5);
                oper.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                oper.setBackgroundResource(R.drawable.r);
                tr.addView(oper);

                opcode = new TextView(getActivity());
                opcode.setText(" " + logs.get(i).getOper());
                opcode.setTextColor(Color.argb(208, 45, 136, 82));
                opcode.setTextSize(18);
                opcode.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                opcode.setPadding(5, 5, 5, 5);
                opcode.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                opcode.setBackgroundResource(R.drawable.r);
                tr.addView(opcode);

                // Add the TableRow to the TableLayout
                tg.addView(tr, new TableLayout.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

}

