package com.coding.test;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class SilverBarsLiveOrderBoard implements LiveOrderBoard {
    private final Set<Order> orders = new HashSet<>();

    @Override
    public void register(Order order) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(order.userId()), "User id cannot be null or empty!");
        Preconditions.checkArgument(order.quantity() > 0, "Quantity has to be greater than zero!");
        Preconditions.checkArgument(order.price().compareTo(BigDecimal.ZERO) > 0, "Price has to be greater than zero!");
        Preconditions.checkArgument(order.type() != null, "Type cannot be null!");
        orders.add(order);
    }

    @Override
    public void cancel(Order order) {
        Preconditions.checkArgument(orders.contains(order), "Specified order does not exist!");
        orders.remove(order);
    }

    @Override
    public SummaryInfo summary() {
        final List<String> finalOrdersSummary = new ArrayList<>();

        orders.stream()
                .collect(groupingBy(Order::type, TreeMap::new, Collectors.toList()))
                .forEach((key, value) -> {
                    if (value != null) {
                        final Map<BigDecimal, Order> ordersByPrice = groupOrdersByPrice(value);
                        finalOrdersSummary.addAll(ordersByPrice.values().stream()
                                .sorted(comparatorForOrderType(key))
                                .map(Order::summary)
                                .collect(toList()));
                    }
                });

        return SummaryInfo.fromOrderSummaries(finalOrdersSummary);
    }


    private Map<BigDecimal, Order> groupOrdersByPrice(List<Order> ordersByType) {
        return ordersByType.stream()
                .collect(groupingBy(Order::price, Collectors.reducing(
                        new Order(),
                        identity(),
                        (left, right) -> new Order(right.userId(), left.quantity() + right.quantity(), right.price(), right.type()))));
    }

    private Comparator<Order> comparatorForOrderType(OrderType type) {
        final Comparator<Order> comparator = Comparator.comparing(Order::price);
        return type.equals(OrderType.BUY) ? comparator.reversed() : comparator;
    }
}
