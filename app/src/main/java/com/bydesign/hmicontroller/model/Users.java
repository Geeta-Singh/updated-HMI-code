package com.bydesign.hmicontroller.model;

/**
 * Created by Geeta on 1/18/2016.
 */
public class Users {
    public String email;
    public String username;
    public String password;
    public long mobile;
    public int userlevel;

    //Constructor
    public Users(){}
    public Users(String username, String password, String email, long mobile, int userlevel) {

        this.username = username;
        this.password = password;
        this.email = email;
        this.mobile = mobile;
        this.userlevel = userlevel;

    }

    //getters and setters

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

