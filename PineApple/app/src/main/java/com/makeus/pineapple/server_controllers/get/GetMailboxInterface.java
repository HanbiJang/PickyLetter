package com.makeus.pineapple.server_controllers.get;

import android.util.Log;

import com.makeus.pineapple.home.Fragment1_Home;
import com.makeus.pineapple.home.filters.PopupFilter;
import com.makeus.pineapple.server_controllers.server_data.MailboxRequestData;

public interface GetMailboxInterface extends GetRequestInterface {

    @Override
    default String makeRequestUrl(Object data) {
        MailboxRequestData data_ = (MailboxRequestData) data;
        String url;
        if(Fragment1_Home.isFilterStart_date == true){ //1. 기간 검색
            url = "http://3.13.65.158/v1/users/" + data_.getUserId() + "/mailbox"
                    + "?endDate=" + Fragment1_Home.endDate + "&page=" + data_.getPage() + "&startDate=" + Fragment1_Home.startDate;
        }
        else{ // 필터 기능 비활성화
            url = "http://3.13.65.158/v1/users/" + data_.getUserId() + "/mailbox"
                    + "?endDate=" + data_.getEndDate() + "&page=" + data_.getPage() + "&startDate=" + data_.getStartDate();
        }

        Log.e("makeRequestUrl",url+"");
        return url;
    }

}
