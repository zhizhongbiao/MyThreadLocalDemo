package com.waterflower.mythreadlocaldemo.my_thread_local;

import java.lang.ref.WeakReference;

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
            MyThreadLocalMap.MyWeakReferenceEntry e = map.getEntry(this);
            if (e != null) {
                return (T) e.value;
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
        ((MyThread) currentThread).threadLocalMap = new MyThreadLocalMap(this, v);
    }

    private MyThreadLocalMap getMap(Thread currentThread) {
        return ((MyThread) currentThread).threadLocalMap;
    }


    static class MyThreadLocalMap {


        /**
         * Table's initial capacity.Capacity Must be power of 2.
         * Table.length Must be a power of 2.
         */
        private int INITIAL_CAPACITY = 16;

        /**
         * Table 's size;
         */
        private int size = 0;


        /**
         * A threshold for that table.
         */
        private int threshold = 0;

        /**
         * An array for MyWeakReferenceEntry.
         */
        MyWeakReferenceEntry[] table;


        public MyThreadLocalMap(MyThreadLocalImp firstKey, Object firstValue) {
            table = new MyWeakReferenceEntry[INITIAL_CAPACITY];
            int i = firstKey.hashCode() & (INITIAL_CAPACITY - 1);
            table[i] = new MyWeakReferenceEntry(firstKey, firstValue);
            size = 1;
            setThreshold(table.length);
        }

        private void setThreshold(int length) {
            threshold = length * 2 / 3;
            ;
        }


        public void set(IMyThreadLocal key, Object v) {
            MyWeakReferenceEntry[] entries = table;
            int len = entries.length;
            int i = key.hashCode() & (len - 1);


            for (MyWeakReferenceEntry e = entries[i];
                 e != null;
                 e = entries[getNextIndex(i, len)]) {

                IMyThreadLocal iMyThreadLocal = e.get();
                if (key == iMyThreadLocal) {
                    e.value = v;
                    return;
                }

                if (iMyThreadLocal == null) {
                    //Do somethings necessary.
                    return;
                }
            }

            table[i] = new MyWeakReferenceEntry(key, v);
            size++;
            //Do things left.
        }


        public MyWeakReferenceEntry getEntry(MyThreadLocalImp key) {

            int i = key.hashCode() & (table.length - 1);
            MyWeakReferenceEntry e = table[i];

            if (e != null && e.get() == key) {
                return e;
            }
            return getEntryAfterMiss(key, i, e);


        }

        private MyWeakReferenceEntry getEntryAfterMiss(MyThreadLocalImp key, int index, MyWeakReferenceEntry e) {

            MyWeakReferenceEntry[] tab = table;
            int len = tab.length;

            while (e != null) {
                IMyThreadLocal k = e.get();
                if (k == key) {
                    return e;
                } else if (k == null) {
                    //Do other necessary things.
                } else {
                    index = getNextIndex(index, len);
                }

                e = table[index];
            }


            return null;

        }

        private int getNextIndex(int i, int len) {
            ++i;
            return i < len ? i : 0;
        }

        public void remove(IMyThreadLocal key) {
            MyWeakReferenceEntry[] tab = table;
            int length = tab.length;
            for (int i = 0; i < length; i++) {
                MyWeakReferenceEntry e = tab[i];
                if (e!=null&&e.get() == key) {
                    e.clear();

                    //Do somethings else.
                    return;
                }
            }
        }


        static class MyWeakReferenceEntry extends WeakReference<IMyThreadLocal> {

            private Object value;

            public MyWeakReferenceEntry(IMyThreadLocal key, Object v) {
                super(key);
                this.value = v;
            }
        }
    }


}
