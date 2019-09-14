package com.coding.test;

public interface LiveOrderBoard {
    Order register(Order order);

    void cancel(Order order);

    SummaryInfo summary();
}
