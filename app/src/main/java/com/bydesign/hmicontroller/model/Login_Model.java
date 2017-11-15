package com.bydesign.hmicontroller.model;

/**
 * Created by PARIKSH on 1/14/2016.
 */

public class Login_Model {

    private String email;
    private String username;
    private String password;
    private long mobile;
    private int userlevel;
    private String fullname;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    //Constructor
    public Login_Model()
    {
        //Default Constructor
    }
    public Login_Model(String username, String password, String email, long mobile, int userlevel) {

        this.username = username;
        this.password = password;
        this.email = email;
        this.mobile = mobile;
        this.userlevel = userlevel;

    }

    //getters and setters

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    public int getUserlevel() {
        return userlevel;
    }

    public void setUserlevel(int userlevel) {
        this.userlevel = userlevel;
    }


}





