package com.nbu.util.mode.observer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 被观察者
 * 要和观察者一起联合使用
 */
public abstract class Subject {

    private List<Observer> observers = new LinkedList<>();

    private Object status;

    public void SetStatus(Object status) {
        this.status = status;
        NotifyAllObserver();
    }

    public Object GetStatus() {
        return this.status == null?new Object():this.status;
    }

    public void Attach(Observer observer) {
        observers.add(observer);
    }
    public void NotifyAllObserver() {
        for (Observer observer : this.observers) {
                observer.update();
        }
    }
}
