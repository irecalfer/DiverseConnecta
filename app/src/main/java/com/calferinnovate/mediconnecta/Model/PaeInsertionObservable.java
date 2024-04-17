package com.calferinnovate.mediconnecta.Model;

import com.calferinnovate.mediconnecta.Interfaces.PaeInsertionObserver;

import java.util.ArrayList;
import java.util.List;

public class PaeInsertionObservable {
    private List<PaeInsertionObserver> observers = new ArrayList<>();

    public void addObserver(PaeInsertionObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(PaeInsertionObserver observer) {
        observers.remove(observer);
    }

    public void notifyPaeInserted() {
        for (PaeInsertionObserver observer : observers) {
            observer.onPaeInserted();
        }
    }
}
