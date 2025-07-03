package com.mycompany.ebookwebsite.bean;

import java.util.concurrent.atomic.AtomicInteger;

public class CounterBean {

    private final AtomicInteger count = new AtomicInteger(0);

    public int increment() {
        return count.incrementAndGet();
    }

    public int decrement() {
        return count.decrementAndGet();
    }

    public int get() {
        return count.get();
    }
}
