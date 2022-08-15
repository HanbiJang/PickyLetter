package com.makeus.pineapple.server_controllers.patch;

import android.util.Log;

public interface PatchNicknameInterface extends PatchRequest {

    @Override
    default String makeRequestUrl(Object data) {
        String url;
        url = "http://3.13.65.158/v1/users/nickname";
        Log.e("makeRequestUrl",url+"");
        return url;
    }

}
