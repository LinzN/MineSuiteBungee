package de.linzn.mineSuite.bungee.utils;

import java.util.concurrent.atomic.AtomicBoolean;

public class FakePair {
    private double value;
    private AtomicBoolean key;

    public FakePair(AtomicBoolean key, double value) {
        this.key = key;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public AtomicBoolean getKey() {
        return key;
    }
}
