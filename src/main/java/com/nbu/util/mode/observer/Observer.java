package com.nbu.util.mode.observer;

/**
 * 观察者模式
 * 目前的作用是观察串口的消息变化
 */
public abstract class Observer {
    protected Subject subject;
    public abstract void update();
}
