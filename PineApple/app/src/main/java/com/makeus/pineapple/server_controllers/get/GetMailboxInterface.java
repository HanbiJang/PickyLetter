package com.makeus.pineapple.server_controllers.get;

import android.util.Log;

import com.makeus.pineapple.server_controllers.server_data.MailboxRequestData;

public interface GetMailboxInterface extends GetRequestInterface {

    @Override
    default String makeRequestUrl(Object data) {
        MailboxRequestData data_ = (MailboxRequestData) data;
        String url;
        url = "http://3.13.65.158/v1/users/" + data_.getUserId() + "/mailbox"
                + "?endDate=" + data_.getEndDate() + "&page=" + data_.getPage() + "&startDate=" + data_.getStartDate();
        Log.e("makeRequestUrl",url+"");
        return url;
    }

}
