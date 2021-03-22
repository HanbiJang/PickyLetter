package com.makeus.pineapple.server_controllers;

import android.util.Log;

public interface PostBookmarkAddInterface extends PostRequest {

    @Override
    default String makeRequestUrl(Object data) {
        String url;
        url = "http://3.13.65.158/v1/users/current/bookmarks";
        return url;
    }

}
