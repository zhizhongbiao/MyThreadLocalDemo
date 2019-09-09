package com.waterflower.mythreadlocaldemo.my_thread_local;

import android.os.HandlerThread;

import java.util.HashMap;
import java.util.Map;

/**
 * FileName :  MyThreadLocalImp
 * Author   :  zhizhongbiao
 * Date     :  2019/9/9
 * Describe :
 */

public class MyThreadLocalImp<T> implements IMyThreadLocal<T> {

    @Override
    public T initValue() {
        return null;
    }

    @Override
    public void set(T v) {
        Thread currentThread = Thread.currentThread();
        MyThreadLocalMap map = getMap(currentThread);
        if (map != null) {
            map.set(this, v);
        } else {
            createMap(currentThread, v);
        }
    }

    @Override
    public T get() {
        Thread currentThread = Thread.currentThread();
        MyThreadLocalMap map = getMap(currentThread);
        if (map != null) {
            MyThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                return (T) e.getValue();
            }
        }
        return setInitValue();
    }

    private T setInitValue() {
        T initValue = initValue();
        set(initValue);
        return initValue;
    }

    @Override
    public void remove() {
        MyThreadLocalMap map = getMap(Thread.currentThread());
        if (map != null) {
            map.remove(this);
        }
    }

    private void createMap(Thread currentThread, T v) {
        ((MyThread) currentThread).threadLocalMap=new MyThreadLocalMap(currentThread,v);
    }

    private MyThreadLocalMap getMap(Thread currentThread) {
        return ((MyThread) currentThread).threadLocalMap;
    }


    public class MyThreadLocalMap {
        private Thread t;

        public MyThreadLocalMap(Thread t, Object firstValue) {
            this.t = t;
        }


        public void set(IMyThreadLocal key, T v) {

        }


        public T get() {
            return null;
        }


        public void remove(IMyThreadLocal key) {

        }

        public MyThreadLocalMap.Entry getEntry(MyThreadLocalImp<T> tMyThreadLocalImp) {
            return null;
        }


        private class Entry implements Map.Entry<IMyThreadLocal, Object> {

            @Override
            public IMyThreadLocal getKey() {
                return null;
            }

            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public Object setValue(Object value) {
                return null;
            }
        }
    }


}
