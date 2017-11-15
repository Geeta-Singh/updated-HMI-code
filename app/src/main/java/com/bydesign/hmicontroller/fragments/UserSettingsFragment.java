package com.bydesign.hmicontroller.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bydesign.hmicontroller.Service.DatabaseHandler;
import com.bydesign.hmicontroller.Service.OOM;
import com.bydesign.hmicontroller.Service.SessionManager;
import com.bydesign.hmicontroller.activity.SimpleTabsActivity;
import com.bydesign.hmicontroller.model.Login_Model;
import com.bydesign.hmicontroller.model.Logs_Model;

import java.util.ArrayList;

import info.bydesign.hmicontroller.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserSettingsFragment extends Fragment {
    LinearLayout createUser, changePass, setDeviceInfo, opentab, showbuttons;
    Button cu, cp, sdi, opntsetting, submitUserDetail;
    EditText checkPwd;
    Login_Model user = null;
    AutoCompleteTextView text;

    public UserSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_settings, container, false);
        createUser = (LinearLayout) view.findViewById(R.id.createuser);
        changePass = (LinearLayout) view.findViewById(R.id.changepass);
        setDeviceInfo = (LinearLayout) view.findViewById(R.id.setdeviceinfo);
        opentab = (LinearLayout) view.findViewById(R.id.opentab);

        cu = (Button) view.findViewById(R.id.cu);
        sdi = (Button) view.findViewById(R.id.sdi);
        cp = (Button) view.findViewById(R.id.cp);
        opntsetting = (Button) view.findViewById(R.id.openUserSettings);
        submitUserDetail = (Button) view.findViewById(R.id.submitUserDetail);
        showbuttons = (LinearLayout) view.findViewById(R.id.showbuttons);

        checkPwd = (EditText) view.findViewById(R.id.opentabpassword);
        checkPwd.setText("");

        opntsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPassword(checkPwd.getText().toString());
            }
        });
        cu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createUser.setVisibility(View.VISIBLE);
                changePass.setVisibility(View.GONE);
                setDeviceInfo.setVisibility(View.GONE);

            }
        });

        cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser.setVisibility(View.GONE);
                changePass.setVisibility(View.VISIBLE);
                setDeviceInfo.setVisibility(View.GONE);

            }
        });

        sdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser.setVisibility(View.GONE);
                changePass.setVisibility(View.GONE);
                setDeviceInfo.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    public void onStart() {
        super.onStart();
        Runtime.getRuntime().gc();
        System.gc();
        //OOM PROTECTION
        Thread.currentThread().setDefaultUncaughtExceptionHandler(new OOM.MyUncaughtExceptionHandler());
        Button _Save;
        Button _Set;
        text = (AutoCompleteTextView) getView().findViewById(R.id.urls);
        text.setText("");
        Button applyChangePassword, setDeviceID;
        _Save = (Button) getView().findViewById(R.id.saveurl);
        _Set = (Button) getView().findViewById(R.id.seturl);
        databaseHandler = new DatabaseHandler(getActivity());
        ArrayList<String> url_list = new ArrayList<>();
        url_list = databaseHandler.Get_AllUrl();
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, url_list);
        final EditText devid;
        devid = (EditText) getView().findViewById(R.id.devid);
        text.setAdapter(adapter);
        text.setThreshold(1);
        databaseHandler = new DatabaseHandler(getActivity());
        opentab.setVisibility(View.VISIBLE);
        showbuttons.setVisibility(View.GONE);
        createUser.setVisibility(View.GONE);
        changePass.setVisibility(View.GONE);
        setDeviceInfo.setVisibility(View.GONE);

        applyChangePassword = (Button) getView().findViewById(R.id.ApplyChangePassword);
       // setDeviceID = (Button) getView().findViewById(R.id.setDeviceId);
        submitUserDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
        applyChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        _Save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!text.getText().toString().isEmpty()&& !devid.getText().toString().isEmpty()) {
                    databaseHandler = new DatabaseHandler(getActivity());
                    databaseHandler.Save_Url(text.getText().toString());
                    databaseHandler.addDevice(devid.getText().toString());
                    SessionManager sessionManager = new SessionManager(getActivity());
                   // sessionManager.setToken("URLUDATED");
                    Logs_Insertion("Devid \"" + devid.getText().toString() + " \" added");
                   // setDeviceParameter();
                    Toast.makeText(getActivity(), " URL and Device saved ", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity(), " Please enter valid URL device ID ", Toast.LENGTH_SHORT).show();
            }
        });

        _Set.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
                if (!text.getText().toString().isEmpty()) {
                    databaseHandler.Reset_Url();
                    databaseHandler.Set_Url(text.getText().toString());
                    Logs_Insertion("Server URL SET ");
                    SessionManager sessionManager = new SessionManager(getActivity());
                    sessionManager.setToken("URLUDATED");
               /* SimpleTabsActivity simpleTabsActivity = new SimpleTabsActivity();
                simpleTabsActivity.Reset_Token();
                */
                   /* SimpleTabsActivity simpleTabsActivity = new SimpleTabsActivity();
                    simpleTabsActivity.ConnectionCheck();
                    System.out.println("register again calling");*/
                    Intent in = new Intent(getActivity(), SimpleTabsActivity.class);
                    startActivity(in);
                } else
                    Toast.makeText(getActivity(), "Please enter valid URL ", Toast.LENGTH_SHORT).show();
            }
        });

//        setDeviceID.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setDeviceParameter();
//            }
//        });


    }

    public void checkPassword(String pwd) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("HmiController", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "invalid");
        if (isValid(username, pwd)) {
            checkPwd.setText("");
            showbuttons.setVisibility(View.VISIBLE);
            createUser.setVisibility(View.VISIBLE);
            changePass.setVisibility(View.GONE);
            opentab.setVisibility(View.GONE);
            setDeviceInfo.setVisibility(View.GONE);
            Logs_Insertion("User setting accessed ");

        } else {
            // Toast.makeText(getActivity(), "Failed ", Toast.LENGTH_SHORT).show();
        }


    }

    DatabaseHandler databaseHandler;// = new DatabaseHandler(this);

    private boolean isValid(String u_name, String pass) {
        user = databaseHandler.Retrieve(u_name);

        //Verifying User Details
        try {
            if (u_name.equals(user.getUsername()) && pass.equals(user.getPassword())) {
                System.out.println("inside is valide function u_name " + u_name + " pass " + pass);
                return true;
            } else {
                Toast.makeText(getActivity(), "Please Enter Correct Password", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NullPointerException exception) {
            Toast.makeText(getActivity(), "please Enter valid password", Toast.LENGTH_SHORT).show();
            exception.printStackTrace();
            return false;
        }
    }

    public boolean isMatchPass(String newPass, String conPass) {
        System.out.println("inside match pass");
        try {
            if (newPass.equals(conPass)) {
                return true;
            } else
                return false;
        } catch (NullPointerException exception) {
            Toast.makeText(getActivity(), "Please Enter password", Toast.LENGTH_SHORT).show();
            exception.printStackTrace();
            return false;
        }

    }

    public String isValideString(String level, String unname, String pass, String fullname, String mobile) {
        try {
            System.out.println(" usern ame jo m enter kr rhi hu " + unname);
            if (!level.equalsIgnoreCase("") && !unname.equalsIgnoreCase("") && !pass.equalsIgnoreCase("") && !fullname.equalsIgnoreCase("") ) {
                return "true";
            } else return "false";
//            } else {
//                String res = databaseHandler.checkUsername(username.getText().toString());
//                System.out.println(" res insid user setting " + res);
//                if (res.equalsIgnoreCase("create"))
//                    return "create";
//                else
//                    return "dontcreate";
//            }
        } catch (NullPointerException e) {
            Toast.makeText(getActivity(), "Please Fill All Details", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return "false";
        }
    }

    /**************************** Create USer module start ****************************************************/

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    EditText username;
    EditText password, userlevel, email, mobile, fullname;

    public void createUser() {

        username = (EditText) getActivity().findViewById(R.id.username);
        fullname = (EditText) getActivity().findViewById(R.id.fullname);
        password = (EditText) getActivity().findViewById(R.id.pwd);
        userlevel = (EditText) getActivity().findViewById(R.id.userlevel);
        email = (EditText) getActivity().findViewById(R.id.email);
        mobile = (EditText) getActivity().findViewById(R.id.mobile);
        String mb = "";

        Long mob = null;
        try {
            // mobile.setText("");
            mb = (mobile.getText().toString());
            if (mb.equals("")&& mb.equals(null)) {
                mob = Long.valueOf("1234567890");
                user.setMobile(mob);
            }
        } catch (NumberFormatException nm) {
            nm.printStackTrace();
            if (mb.equals("")&& mb.equals(null)) {
                mob = Long.valueOf("1234567890");
                user.setMobile(mob);
            }
        }
        try {
            System.out.println(" usern ame jo m enter kr rhi hu " + username.getText().toString());


            String check = isValideString(userlevel.getText().toString(), password.getText().toString(), username.getText().toString(), fullname.getText().toString(), mobile.getText().toString());

            String res = databaseHandler.checkUsername(username.getText().toString());
            System.out.println(" res insid user setting " + res);


            if (check.equalsIgnoreCase("true")) {
                System.out.println("inside check ");
                if (res.equalsIgnoreCase("create")) {
                    System.out.println("inside res ");
                    if (isValidEmail(email.getText().toString())) {
                        System.out.println("inside mail check ");
                        user.setUserlevel(Integer.parseInt(userlevel.getText().toString()));
                        user.setPassword(password.getText().toString());
                        user.setUsername(username.getText().toString());
                        user.setEmail(email.getText().toString());
                        user.setFullname(fullname.getText().toString());

                        String result = databaseHandler.Inset_UserDetail(user);
                        if (result.equalsIgnoreCase("success")) {
                            Logs_Insertion("User " + username.getText().toString() + " Created ");
                            Toast.makeText(getActivity(), "Successfully created", Toast.LENGTH_SHORT).show();
                            userlevel.setText("");
                            username.setText("");
                            email.setText("");
                            mobile.setText("");
                            password.setText("");
                            fullname.setText("");
                        } else {
                            Toast.makeText(getActivity(), "New user creation fail", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Enter valid Email", Toast.LENGTH_SHORT).show();
                    }
                } else if (res.equalsIgnoreCase("notcreate")) {
                    username.setText("");
                    Toast.makeText(getActivity(), "User Already exits", Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(getActivity(), "Please Fill All Details", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (NullPointerException nw) {
            Toast.makeText(getActivity(), "Please Enter all Details", Toast.LENGTH_SHORT).show();
            nw.printStackTrace();
        } catch (Exception e) {
            username.setText("");
            Toast.makeText(getActivity(), "User Already exits", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
    /**************************** Create USer module End****************************************************/

    /**************************** Change password module start****************************************************/
    public void changePassword() {
        EditText currentPass, newPass, confirmPass, cp_username;
        currentPass = (EditText) getActivity().findViewById(R.id.currentpass);
        newPass = (EditText) getActivity().findViewById(R.id.newpassword);
        confirmPass = (EditText) getActivity().findViewById(R.id.confirmpass);
        cp_username = (EditText) getActivity().findViewById(R.id.cp_username);
        try {
//            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("HmiController", MODE_PRIVATE);
//            String username = sharedPreferences.getString("username", "invalid");
            System.out.println("change password  username " + cp_username.getText().toString() + "password " + currentPass.getText().toString());
            if (isValid(cp_username.getText().toString(), currentPass.getText().toString())) {
                if (isMatchPass(newPass.getText().toString(), confirmPass.getText().toString())) {
                    String changepass = databaseHandler.changePass(cp_username.getText().toString(), newPass.getText().toString());
                    if (changepass.equalsIgnoreCase("success")) {
                        Logs_Insertion(cp_username.getText().toString() + " Password changed ");
                        currentPass.setText("");
                        newPass.setText("");
                        confirmPass.setText("");
                        cp_username.setText("");
                        Toast.makeText(getActivity(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Password not updated", Toast.LENGTH_SHORT).show();
                    }
                    // Toast.makeText(getActivity(), "Successfully Updated", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), "New and confirm password not matching", Toast.LENGTH_SHORT).show();
                }
            } else {

            }
        } catch (NullPointerException e) {
            Toast.makeText(getActivity(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    /**************************** Change password module Close****************************************************/
    /**************************** SET DEVICE AND SERVER URL  module START****************************************************/

    public void setDeviceParameter() {
        EditText devid;
        devid = (EditText) getView().findViewById(R.id.devid);
        SessionManager sessionManager = new SessionManager(getActivity());
        try {
            if (devid.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(getActivity(), "Please Enter Device ID", Toast.LENGTH_SHORT).show();
            } else {
                String result = databaseHandler.addDevice(devid.getText().toString());
                System.out.println("inside user setting result " + result + " divid " + devid.getText().toString());
                if (result.equalsIgnoreCase("success")) {
                    sessionManager.setToken("URLUDATED");
                    Logs_Insertion("Devid \"" + devid.getText().toString() + " \" added");
                   // Toast.makeText(getActivity(), "Successfully added device", Toast.LENGTH_SHORT).show();
//                    Intent in = new Intent(getActivity(), SimpleTabsActivity.class);
//                    startActivity(in);
                } else {

                    Toast.makeText(getActivity(), "Device not set", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException e) {
            Toast.makeText(getActivity(), "Please Enter Device ID", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**************************** SET DEVICE AND SERVER URL  module Close****************************************************/


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
}
