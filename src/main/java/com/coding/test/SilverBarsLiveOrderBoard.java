package com.coding.test;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;
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
        final List<String> finalOrdersSummary = orders.stream()
                .collect(groupingBy(Order::type, TreeMap::new, toList()))
                .entrySet()
                .stream()
                .map((entry) -> {
                    final Map<BigDecimal, Order> ordersByPrice = groupOrdersByPrice(entry.getValue());
                    return ordersByPrice.values().stream()
                            .sorted(comparatorForOrderType(entry.getKey()))
                            .map(Order::summary)
                            .collect(toList());
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return SummaryInfo.fromOrderSummaries(finalOrdersSummary);
    }


    private Map<BigDecimal, Order> groupOrdersByPrice(List<Order> ordersByType) {
        return ordersByType.stream()
                .collect(groupingBy(Order::price, reducing(
                        new Order(),
                        identity(),
                        (left, right) -> new Order(right.userId(), left.quantity() + right.quantity(), right.price(), right.type()))));
    }

    private Comparator<Order> comparatorForOrderType(OrderType type) {
        final Comparator<Order> comparator = comparing(Order::price);
        return type.equals(OrderType.BUY) ? comparator.reversed() : comparator;
    }
}
