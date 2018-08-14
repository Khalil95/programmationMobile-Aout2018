package com.henallux.primodeal.Model;

/**
 * Created by bil on 05-02-18.
 */

public class Person {

    private String userName, password, status, addressShop, nameShop, email;

    public Person(String userName, String password,String addressShop, String nameShop, String status){
        this.userName = userName;
        this.email = userName;
        this.password = password;
        this.addressShop = addressShop;
        this.nameShop = nameShop;
        this.status = status;
    }

    public Person(String userName, String password, String status)
    {
        this.userName = userName;
        this.email = userName;
        this.password = password;
        this.status = status;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString(){
        return this.userName+" est sur l'application ";
    }


}
