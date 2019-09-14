package com.coding.test;

import com.google.common.base.Strings;

import java.math.BigDecimal;
import java.util.Objects;

public class Order {
    private final String userId;
    private final Double quantity;
    private final BigDecimal price;
    private final OrderType type;

    public Order() {
        this("", 0.0, new BigDecimal(0.0), null);
    }

    public Order(String userId, Double quantity, BigDecimal price, OrderType type) {
        this.userId = userId;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }

    String userId() {
        return userId;
    }

    Double quantity() {
        return quantity;
    }

    BigDecimal price() {
        return price;
    }

    OrderType type() {
        return type;
    }

    String summary() {
        return String.format("%s: %s kg for £%s", type, quantity, price.toPlainString());
    }

    @Override
    public String toString() {
        return "Order{" +
                "userId='" + userId + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(userId, order.userId) &&
                Objects.equals(quantity, order.quantity) &&
                Objects.equals(price, order.price) &&
                type == order.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, quantity, price, type);
    }

    boolean isValid() {
        return !Strings.isNullOrEmpty(userId)
                && quantity != null && quantity > 0
                && price != null && price.compareTo(BigDecimal.ZERO) > 0
                && type != null;
    }
}
