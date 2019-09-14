package com.coding.test;

import com.google.common.base.Preconditions;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toList;

public class SilverBarsLiveOrderBoard implements LiveOrderBoard {
    private final Set<Order> orders = new HashSet<>();

    @Override
    public Order register(Order order) {
        Preconditions.checkArgument(order.isValid(), "Order is not valid!");
        final Order newOrder = new Order(
                UUID.randomUUID().toString(),
                order.userId(),
                order.quantity(),
                order.price(),
                order.type()
        );
        orders.add(newOrder);
        return newOrder;
    }

    @Override
    public void cancel(Order order) {
        Preconditions.checkArgument(orders.contains(order), "Specified order does not exist!");
        orders.remove(order);
    }

    @Override
    public SummaryInfo summary() {
        return SummaryInfo.fromOrderSummaries(
                groupOrdersByType()
                        .map((entry) -> {
                            final Map<BigDecimal, Order> ordersByPrice = groupOrdersByPrice(entry.getValue());
                            return ordersByPrice.values().stream()
                                    .sorted(comparatorForOrderType(entry.getKey()))
                                    .map(Order::summary)
                                    .collect(toList());
                        })
                        .flatMap(Collection::stream)
                        .collect(toList())
        );
    }


    private Stream<Map.Entry<OrderType, List<Order>>> groupOrdersByType() {
        return orders.stream()
                .collect(groupingBy(Order::type, TreeMap::new, toList()))
                .entrySet()
                .stream();
    }

    private Map<BigDecimal, Order> groupOrdersByPrice(List<Order> ordersByType) {
        return ordersByType.stream()
                .collect(groupingBy(Order::price, reducing(
                        new Order(),
                        identity(),
                        (left, right) -> new Order(right.userId(), left.quantity() + right.quantity(), right.price(), right.type()))));
    }

    private Comparator<Order> comparatorForOrderType(OrderType type) {
        final Comparator<Order> comparatorByPrice = comparing(Order::price);
        return type.equals(OrderType.BUY) ? comparatorByPrice.reversed() : comparatorByPrice;
    }
}
