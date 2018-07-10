package com.icesoft.tumblrget.Utils;

public class MTimeUtils
{
    public interface Timeable{
        void beTimed();
    }
    private long b = 0L;
    private long e = 0L;
    private Timeable timeable;

    public MTimeUtils(Timeable timeable)
    {
        this.timeable = timeable;
        b = System.currentTimeMillis();
        timeable.beTimed();
        e = System.currentTimeMillis();
        System.out.println((e-b) + " ms.");
    }
}
