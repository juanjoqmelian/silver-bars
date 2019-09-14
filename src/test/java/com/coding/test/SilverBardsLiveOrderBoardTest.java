package com.coding.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SilverBardsLiveOrderBoardTest {
    private LiveOrderBoard liveOrderBoard;

    @BeforeEach
    void setUp() {
        liveOrderBoard = new SilverBarsLiveOrderBoard();
    }

    @Test
    void shouldRegisterOneOrder() {

        final Order newOrder = new Order(
                "my-userId",
                3.5,
                new BigDecimal("10.5"),
                OrderType.SELL
        );

        liveOrderBoard.register(newOrder);

        assertThat(liveOrderBoard.summary()).isEqualTo(new SummaryInfo("SELL: 3.5 kg for £10.5"));
    }

    @Test
    void shouldNotAllowAnInvalidOrder() {

        final Order newOrder = new Order(
                null,
                2.5,
                new BigDecimal("10.5"),
                OrderType.SELL
        );

        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            liveOrderBoard.register(newOrder);
        });

        assertThat(illegalArgumentException.getMessage()).isEqualTo("Order is not valid!");
    }

    @Test
    void shouldBeEmptyAfterCancellingOrder() {

        final Order newOrder = new Order(
                "my-userId",
                3.5,
                new BigDecimal("10.5"),
                OrderType.SELL
        );

        liveOrderBoard.register(newOrder);
        liveOrderBoard.cancel(newOrder);

        assertThat(liveOrderBoard.summary()).isEqualTo(new SummaryInfo());
    }

    @Test
    void shouldRaiseExceptionWhenCancellingNotExistingOrder() {

        final Order newOrder = new Order(
                "my-userId",
                3.5,
                new BigDecimal("10.5"),
                OrderType.SELL
        );

        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> {
            liveOrderBoard.cancel(newOrder);
        });

        assertThat(illegalArgumentException.getMessage()).isEqualTo("Specified order does not exist!");
    }

    @Test
    void shouldBeAbleToRegisterMultipleOrders() {
        final Order order1 = new Order(
                "my-userId",
                2.5,
                new BigDecimal("120.5"),
                OrderType.SELL
        );
        final Order order2 = new Order(
                "my-userId",
                3.5,
                new BigDecimal("89.0"),
                OrderType.SELL
        );

        liveOrderBoard.register(order1);
        liveOrderBoard.register(order2);

        assertThat(liveOrderBoard.summary()).isEqualTo(new SummaryInfo(
                "SELL: 3.5 kg for £89.0",
                "SELL: 2.5 kg for £120.5"
        ));
    }

    @Test
    void shouldShowSellOrdersSortedByPriceAscending() {
        final Order order1 = new Order(
                "my-userId",
                1.5,
                new BigDecimal("110.5"),
                OrderType.SELL
        );
        final Order order2 = new Order(
                "my-userId",
                4.6,
                new BigDecimal("77.2"),
                OrderType.SELL
        );
        final Order order3 = new Order(
                "my-userId",
                9.2,
                new BigDecimal("102.2"),
                OrderType.SELL
        );

        liveOrderBoard.register(order1);
        liveOrderBoard.register(order2);
        liveOrderBoard.register(order3);

        assertThat(liveOrderBoard.summary()).isEqualTo(new SummaryInfo(
                "SELL: 4.6 kg for £77.2",
                "SELL: 9.2 kg for £102.2",
                "SELL: 1.5 kg for £110.5"
        ));
    }

    @Test
    void shouldShowBuyOrdersSortedByPriceDescending() {
        final Order order1 = new Order(
                "my-userId",
                1.5,
                new BigDecimal("110.5"),
                OrderType.BUY
        );
        final Order order2 = new Order(
                "my-userId",
                4.6,
                new BigDecimal("77.2"),
                OrderType.BUY
        );
        final Order order3 = new Order(
                "my-userId",
                9.2,
                new BigDecimal("102.2"),
                OrderType.BUY
        );

        liveOrderBoard.register(order1);
        liveOrderBoard.register(order2);
        liveOrderBoard.register(order3);

        assertThat(liveOrderBoard.summary()).isEqualTo(new SummaryInfo(
                "BUY: 1.5 kg for £110.5",
                "BUY: 9.2 kg for £102.2",
                "BUY: 4.6 kg for £77.2"
        ));
    }

    @Test
    void shouldShowOrdersOfDifferentTypesInTheRightOrder() {
        final Order order1 = new Order(
                "my-userId",
                2.5,
                new BigDecimal("120.5"),
                OrderType.SELL
        );
        final Order order2 = new Order(
                "my-userId",
                4.6,
                new BigDecimal("77.2"),
                OrderType.BUY
        );
        final Order order3 = new Order(
                "my-userId",
                7.2,
                new BigDecimal("120.50"),
                OrderType.BUY
        );
        final Order order4 = new Order(
                "my-userId",
                1.8,
                new BigDecimal("101.3"),
                OrderType.SELL
        );

        liveOrderBoard.register(order1);
        liveOrderBoard.register(order2);
        liveOrderBoard.register(order3);
        liveOrderBoard.register(order4);

        assertThat(liveOrderBoard.summary()).isEqualTo(new SummaryInfo(
                "BUY: 7.2 kg for £120.50",
                "BUY: 4.6 kg for £77.2",
                "SELL: 1.8 kg for £101.3",
                "SELL: 2.5 kg for £120.5"
        ));
    }

    @Test
    void shouldGroupOrdersOfTheSamePriceAndTypeAndSortThemByPriceAscending() {
        final Order order1 = new Order(
                "my-userId",
                2.5,
                new BigDecimal("120.5"),
                OrderType.SELL
        );
        final Order order2 = new Order(
                "my-userId",
                4.6,
                new BigDecimal("77.2"),
                OrderType.SELL
        );
        final Order order3 = new Order(
                "my-userId",
                7.2,
                new BigDecimal("120.5"),
                OrderType.SELL
        );
        final Order order4 = new Order(
                "my-userId",
                1.8,
                new BigDecimal("101.3"),
                OrderType.SELL
        );
        final Order order5 = new Order(
                "my-userId",
                2.5,
                new BigDecimal("120.5"),
                OrderType.BUY
        );
        final Order order6 = new Order(
                "my-userId",
                4.6,
                new BigDecimal("77.2"),
                OrderType.BUY
        );
        final Order order7 = new Order(
                "my-userId",
                7.2,
                new BigDecimal("120.5"),
                OrderType.BUY
        );
        final Order order8 = new Order(
                "my-userId",
                1.8,
                new BigDecimal("101.3"),
                OrderType.BUY
        );

        liveOrderBoard.register(order1);
        liveOrderBoard.register(order2);
        liveOrderBoard.register(order3);
        liveOrderBoard.register(order4);
        liveOrderBoard.register(order5);
        liveOrderBoard.register(order6);
        liveOrderBoard.register(order7);
        liveOrderBoard.register(order8);

        assertThat(liveOrderBoard.summary()).isEqualTo(new SummaryInfo(
                "BUY: 9.7 kg for £120.5",
                "BUY: 1.8 kg for £101.3",
                "BUY: 4.6 kg for £77.2",
                "SELL: 4.6 kg for £77.2",
                "SELL: 1.8 kg for £101.3",
                "SELL: 9.7 kg for £120.5"
        ));
    }
}
