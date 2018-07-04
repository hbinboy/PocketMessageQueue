package com.hb.pocket;

/**
 * Created by hb on 03/07/2018.
 */
public class Subscription {
    /**
     * subscriber object.
     */
    private Object subscriber;
    /**
     * subscriber method object.
     */
    private SubscriberMethod subscriberMethod;
    /**
     * subscriber class name.
     */
    private String className;

    /**
     * Subscriber object warp class.
     * @param subscriber
     * @param SubscriberMethod
     * @param className
     */
    public Subscription(Object subscriber, SubscriberMethod SubscriberMethod, String className) {
        this.subscriber = subscriber;
        this.subscriberMethod = SubscriberMethod;
        this.className = className;
    }

    public SubscriberMethod getSubscriberMethod() {
        return subscriberMethod;
    }

    public Object getSubscriber() {
        return subscriber;
    }

    public String getClassName() {
        return className;
    }
}
