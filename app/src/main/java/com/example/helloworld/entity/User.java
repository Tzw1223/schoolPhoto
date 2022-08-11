package com.example.helloworld.entity;

public class User {
    private String account, password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static User user=new User();

    public static void setUser(String account, String password){
        user.setAccount(account);
        user.setPassword(password);
    }
}
