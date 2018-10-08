package com.hb.pocket.test;

import com.hb.pocket.Center;
import com.hb.pocket.test.login.LoginInfo;
import com.hb.utils.log.MyLog;

/**
 * Created by hb on 04/07/2018.
 */
public class SubscriptionTwo {

    private static String TAG = SubscriptionTwo.class.getSimpleName();

    public SubscriptionTwo() {
        // Register as a subscriber.
        Center.getInstance().register(this);
    }

    public void onEvent(LoginInfo loginInfo){
        if (loginInfo != null && loginInfo.getSuccess()) {
            MyLog.d(TAG, "Login success!");
        } else {
            MyLog.d(TAG, "Login failed!");
        }

    }

    /**
     * Unregister listen.
     */
    public void unregister() {
        // Unregister.
        Center.getInstance().register(this);
    }

}
