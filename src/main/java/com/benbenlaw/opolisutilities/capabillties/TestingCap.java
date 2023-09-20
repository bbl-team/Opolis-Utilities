package com.benbenlaw.opolisutilities.capabillties;

public class TestingCap implements ITesting {
    private int value;
    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void setValue(int test) {
        this.value = test;
    }
}
