package com.makeus.pineapple.sign.users;


public class UserResult {

    /*    {
        "token":"string",
            "user":{
        "bookmarkCount":0,
                "email":"string",
                "nickname":"string",
                "subscribingPlatformCount":0,
                "userId":0
    }
    }*/

    String token;
    User user;

    //


    public UserResult(String token, User user) {
        this.token = token;
        this.user = user;
    }

    //

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    //


    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(User user) {
        this.user = user;
    }
}