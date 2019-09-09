package com.waterflower.mythreadlocaldemo.my_thread_local;

/**
 * FileName :  IMyThreadLocal
 * Author   :  zhizhongbiao
 * Date     :  2019/9/9
 * Describe :
 */

public interface IMyThreadLocal <T>{


     T initValue();

     void set(T v);

     T get();

     void remove();

}
