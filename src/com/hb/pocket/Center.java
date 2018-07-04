package com.hb.pocket;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The center is a single class.
 * Created by hb on 03/07/2018.
 */
public class Center {

    private static String TAG = Center.class.getSimpleName();

    /**
     * The center instance.
     */
    private volatile static Center instance = null;

    /**
     * Subscription container.
     */
    private Map<Class<?>, ArrayList<Subscription>> subscriptionsByEventType =
            new HashMap<Class<?>, ArrayList<Subscription>>();

    /**
     * Save the message until is deleted.
     */
    private Map<Object, Object> staticPostMsg = new HashMap<>();

    /**
     * Thread manager.
     */
    private AsyThreadHandler asyThreadHandler = new AsyThreadHandler(this);

    /**
     * Do not call by user.
     */
    private Center() {
    }

    /**
     * Get the sigle object.
     * @return
     */
    public synchronized static Center getInstance() {
        if (instance == null) {
            synchronized (Center.class) {
                if (instance == null) {
                    instance = new Center();
                }
            }
        }
        return instance;
    }

    /**
     * Register a subscriber to the center.
     * @param subscriber
     */
    public void register(Object subscriber) {
        // get the subscriber class fullname.
        Class<?> clazz = subscriber.getClass();
        // get the class methods.
        Method[] methods = clazz.getMethods();
        // traversal the array.
        for (Method m : methods) {
            // get the method name.
            String name = m.getName();
            // compare the method name.
            if (name.startsWith("onEvent")) {
                // get the method params type.
                Class<?>[] params = m.getParameterTypes();
                ArrayList<Subscription> arr;

                if (subscriptionsByEventType.containsKey(params[0])) {
                    arr = subscriptionsByEventType.get(params[0]);
                } else {
                    arr = new ArrayList<Subscription>();
                }
                // cut the method name but 'onEvent'
                int len = name.substring("onEvent".length()).length();
                Subscription sub;
                // this subscriber run in a sub thread.
                if (len == 0) {
                    sub = new Subscription(subscriber, new SubscriberMethod(m, params[0], 0), clazz.getName());
                } else { // this subscriber run in the main thread.
                    sub = new Subscription(subscriber, new SubscriberMethod(m, params[0], 1), clazz.getName());
                }
                arr.add(sub);
                subscriptionsByEventType.put(params[0], arr);
            }
        }
    }

    /**
     * Post message to the subscriptions.If the className is null, post the message to all subscriptions.
     * @param event
     * @param clazzName
     */
    public void post(Object event, Class<?> clazzName) {
        Class<?> clazz = event.getClass();
        ArrayList<Subscription> arr = subscriptionsByEventType.get(clazz);
        for (Subscription sub : arr) { // Post message to all subscriptions.
            if (clazzName == null) {
                if (sub.getSubscriberMethod().getType() == 0) {
                    asyThreadHandler.post(sub, event);
                } else {
                    invoke(sub, event, sub.getSubscriberMethod().getMethod());
                }
            } else { // Post message to the specical subscriptions.
                if (sub.getClassName().equals(clazzName.getName())) {
                    if (sub.getSubscriberMethod().getType() == 0) {
                        asyThreadHandler.post(sub, event);
                    } else {
                        invoke(sub, event, sub.getSubscriberMethod().getMethod());
                    }
                }
            }
        }
    }

    /**
     * Post the message to all subscriptions.
     * @param event
     */
    public void postAllSubscription(Object event) {
        Class<?> clazz = event.getClass();
        ArrayList<Subscription> arr = subscriptionsByEventType.get(clazz);
        for (Subscription sub : arr) { // Post message to all subscriptions.
            if (sub.getSubscriberMethod().getType() == 0) {
                asyThreadHandler.post(sub, event);
            } else {
                invoke(sub, event, sub.getSubscriberMethod().getMethod());
            }
        }
    }

    /**
     * Post static message, this message saved unitil is deleted.
     * @param event
     * @param clazzName
     */
    public void postStaticMesage(Object event, Class<?> clazzName) {
        Class<?> clazz = event.getClass();
        ArrayList<Subscription> arr = subscriptionsByEventType.get(clazz);
        for (Subscription sub : arr) { // Post message to all subscriptions.
            if (clazzName == null) {
                if (!staticPostMsg.containsKey(event.getClass().getCanonicalName())) {
                    staticPostMsg.put(event.getClass().getCanonicalName(), event);
                }
                if (sub.getSubscriberMethod().getType() == 0) {
                    asyThreadHandler.post(sub, event);
                } else {
                    invoke(sub, event, sub.getSubscriberMethod().getMethod());
                }
            } else { // Post message to the specical subscriptions.
                if (sub.getClassName().equals(clazzName.getName())) {
                    if (!staticPostMsg.containsKey(event.getClass().getCanonicalName())) {
                        staticPostMsg.put(event.getClass().getCanonicalName(), event);
                    }
                    if (sub.getSubscriberMethod().getType() == 0) {
                        asyThreadHandler.post(sub, event);
                    } else {
                        invoke(sub, event, sub.getSubscriberMethod().getMethod());
                    }
                }
            }
        }
    }

    /**
     * Get specical message.
     * @param event
     * @return
     */
    public Object getStaticMessage(Object event) {
        if (staticPostMsg.containsKey(((Class) event).getCanonicalName())) {
            return staticPostMsg.get(((Class) event).getCanonicalName());
        }
        return null;
    }

    /**
     * Delete the specical message.
     * @param event
     */
    public void removeStaticMsg (Object event) {
        if (staticPostMsg.containsKey(((Class) event).getCanonicalName())) {
            staticPostMsg.remove(((Class) event).getCanonicalName());
        }
    }

    /**
     * Call the subscription method.
     * @param sub
     * @param event
     * @param m
     */
    protected void invoke(final Subscription sub, final Object event, Method m) {
        try {
            m.invoke(sub.getSubscriber(), event);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * unregister subscriber.
     * @param subscriber
     */
    public void unregister(Object subscriber) {
        // get the subscriber class fullname.
        Class<?> clazz = subscriber.getClass();
        // get the class methods.
        Method[] methods = clazz.getMethods();
        // traversal the array.
        for (Method m : methods) {
            // get the method name.
            String name = m.getName();
            // compare the method name. Start with 'onEvent'
            if (name.startsWith("onEvent")) {
                // get the method params type.
                Class<?>[] params = m.getParameterTypes();
                if (subscriptionsByEventType != null && subscriptionsByEventType.size() > 0) {
                    if (subscriptionsByEventType.containsKey(params[0])) {
                        if (subscriptionsByEventType.get(params[0]) != null && subscriptionsByEventType.get(params[0]).size() > 0) {
                            for (int i = 0; i < subscriptionsByEventType.get(params[0]).size(); i++) {
                                if (clazz.getName().equals(subscriptionsByEventType.get(params[0]).get(i).getClassName())) {
                                    subscriptionsByEventType.get(params[0]).remove(i);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
