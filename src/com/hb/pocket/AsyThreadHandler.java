package com.hb.pocket;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by hb on 03/07/2018.
 */
public class AsyThreadHandler {

    private static String TAG = AsyThreadHandler.class.getSimpleName();

    /**
     * PocketMessageQueue center sigle inistance.
     */
    private Center center;

    /**
     * AsyThreadHandler construction function.
     * @param Center    Center object.
     */
    AsyThreadHandler(Center Center) {
        this.center = Center;
    }

    /**
     * Post a message to the subscription in a sub thread.
     * @param sub
     * @param event
     */
    public void post(final Subscription sub, final Object event) {
        new Thread() {
            @Override
            public void run() {
                center.invoke(sub, event, sub.getSubscriberMethod().getMethod());
            }
        }.start();
    }


}
