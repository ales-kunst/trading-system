package com.apssouza.mytrade.trading.domain.forex.orderbook;

import com.apssouza.mytrade.trading.domain.forex.common.Event;
import com.apssouza.mytrade.trading.domain.forex.common.observerinfra.Observer;
import com.apssouza.mytrade.trading.domain.forex.order.OrderCreatedEvent;
import com.apssouza.mytrade.trading.domain.forex.order.OrderFilledEvent;
import com.apssouza.mytrade.trading.domain.forex.portfolio.PositionClosedEvent;

class OrderCreatedListener implements Observer {
    private final BookHistoryService historyHandler;

    public OrderCreatedListener(BookHistoryService historyHandler) {
        this.historyHandler = historyHandler;
    }

    @Override
    public void update(final Event e) {
        if (!(e instanceof OrderCreatedEvent event)) {
            return;
        }
        var order = event.getOrder();
        this.historyHandler.addOrder(order);

    }


}
