package com.coding.test;

public interface LiveOrderBoard {
    String register(Order order);

    void cancel(String id);

    SummaryInfo summary();
}
