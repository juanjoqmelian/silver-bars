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
}
