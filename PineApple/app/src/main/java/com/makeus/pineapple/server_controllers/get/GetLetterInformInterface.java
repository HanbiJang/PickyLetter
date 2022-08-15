package com.makeus.pineapple.server_controllers.get;

import android.util.Log;

public interface GetLetterInformInterface extends GetRequestInterface {

    @Override
    default String makeRequestUrl(Object data) {
        Integer letterId = (Integer) data;
        String url;
        Log.e("letterId 주소만드는곳 값",letterId+"");
        url = "http://3.13.65.158/v1/letters/" + letterId;

        return url;
    }

}
