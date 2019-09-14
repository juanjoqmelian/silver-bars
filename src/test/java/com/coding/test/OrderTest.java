package com.coding.test;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

    @Test
    public void shouldShowSummaryInfoForBuyOrder() {
        final Order order = new Order(
                "my-user-id",
                5.5,
                new BigDecimal("300"),
                OrderType.BUY
        );

        assertThat(order.summary()).isEqualTo("BUY: 5.5 kg for £300");
    }

    @Test
    public void shouldShowSummaryInfoForSellOrder() {
        final Order order = new Order(
                "my-user-id",
                12.5,
                new BigDecimal("150.43"),
                OrderType.SELL
        );

        assertThat(order.summary()).isEqualTo("SELL: 12.5 kg for £150.43");
    }

    @Test
    public void shouldBeAValidOrder() {
        final Order order = new Order(
                "my-user-id",
                12.5,
                new BigDecimal("150.43"),
                OrderType.SELL
        );

        assertThat(order.isValid()).isTrue();
    }

    @Test
    public void shouldNotBeValidIfThereIsNoUserId() {
        final Order order = new Order(
                null,
                12.5,
                new BigDecimal("150.43"),
                OrderType.SELL
        );

        assertThat(order.isValid()).isFalse();
    }

    @Test
    public void shouldNotBeValidIfUserIdIsEmpty() {
        final Order order = new Order(
                "",
                12.5,
                new BigDecimal("150.43"),
                OrderType.SELL
        );

        assertThat(order.isValid()).isFalse();
    }

    @Test
    public void shouldNotBeValidIfQuantityIsNotProvided() {
        final Order order = new Order(
                "my-user-id",
                null,
                new BigDecimal("150.43"),
                OrderType.SELL
        );

        assertThat(order.isValid()).isFalse();
    }

    @Test
    public void shouldNotBeValidIfQuantityIsZero() {
        final Order order = new Order(
                "my-user-id",
                0.0,
                new BigDecimal("150.43"),
                OrderType.SELL
        );

        assertThat(order.isValid()).isFalse();
    }

    @Test
    public void shouldNotBeValidIfQuantityIsNegative() {
        final Order order = new Order(
                "my-user-id",
                -2.5,
                new BigDecimal("150.43"),
                OrderType.SELL
        );

        assertThat(order.isValid()).isFalse();
    }

    @Test
    public void shouldNotBeValidIfPriceIsNotProvided() {
        final Order order = new Order(
                "my-user-id",
                2.0,
                null,
                OrderType.SELL
        );

        assertThat(order.isValid()).isFalse();
    }

    @Test
    public void shouldNotBeValidIfPriceIsZero() {
        final Order order = new Order(
                "my-user-id",
                2.0,
                new BigDecimal("0.0"),
                OrderType.SELL
        );

        assertThat(order.isValid()).isFalse();
    }

    @Test
    public void shouldNotBeValidIfPriceIsNegative() {
        final Order order = new Order(
                "my-user-id",
                2.0,
                new BigDecimal("-2.50"),
                OrderType.SELL
        );

        assertThat(order.isValid()).isFalse();
    }

    @Test
    public void shouldNotBeValidIfTypeIsNotProvided() {
        final Order order = new Order(
                "my-user-id",
                2.0,
                new BigDecimal("100.0"),
                null
        );

        assertThat(order.isValid()).isFalse();
    }
}
