package com.life.shelter.people.homeless;

/**
 * Created by AHMED MAGDY on 9/10/2018.
 */

public class HomeFirebaseClass {
    // private String cId;
    private String cName;
    private String cAddress;
    private String cCity;
    private String cUri;


    private String userUri;
    private String username;
    private String pdate;



    public  HomeFirebaseClass() {}

    public HomeFirebaseClass(String cName, String cAddress, String cCity, String cUri, String userUri, String username, String pdate) {
        this.cName = cName;
        this.cAddress = cAddress;
        this.cCity = cCity;
        this.cUri = cUri;
        this.userUri = userUri;
        this.username = username;
        this.pdate = pdate;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }


    public String getcAddress() {
        return cAddress;
    }

    public void setcAddress(String cAddress) {
        this.cAddress = cAddress;
    }



    public String getcCity() {
        return cCity;
    }

    public void setcCity(String cCity) {
        this.cCity = cCity;
    }

    public String getcUri() {
        return cUri;
    }

    public void setcUri(String cUri) {
        this.cUri = cUri;
    }

    public String getUserUri() {
        return userUri;
    }

    public void setUserUri(String userUri) {
        this.userUri = userUri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPdate() {
        return pdate;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }
}

