package com.makeus.pineapple.server_controllers.get;

import android.util.Log;

import com.makeus.pineapple.server_controllers.server_data.MailboxRequestData;

public interface GetUserDataInterface extends GetRequestInterface {

    @Override
    default String makeRequestUrl(Object data) {
        String url;
        url = "http://3.13.65.158/v1/users";
        Log.e("makeRequestUrl",url+"");
        return url;
    }

}
