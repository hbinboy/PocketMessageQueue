package com.hb.pocket;

import java.lang.reflect.Method;

/**
 * Subscriber mothod class.
 * Created by hb on 03/07/2018.
 */
public class SubscriberMethod {

    private static String TAG = SubscriberMethod.class.getSimpleName();

    private Method method;
    private int type;

    /**
     * Subscriber construction function.
     * @param m     subscriber's method
     * @param param subscriber's method params
     * @param type  subscriber's method run in main thread or sub thread.
     */
    public SubscriberMethod(Method m, Class<?> param, int type) {
        this.method = m;
        this.type = type;
    }

    public Method getMethod() {
        return method;
    }

    public int getType() {
        return type;
    }

}
