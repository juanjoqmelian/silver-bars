package com.coding.test;

public interface LiveOrderBoard {
    void register(Order order);

    void cancel(Order order);

    SummaryInfo summary();
}
