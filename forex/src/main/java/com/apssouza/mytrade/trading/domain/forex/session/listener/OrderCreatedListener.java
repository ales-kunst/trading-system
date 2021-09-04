package com.apssouza.mytrade.trading.domain.forex.session.listener;

import com.apssouza.mytrade.trading.domain.forex.order.OrderHandler;
import com.apssouza.mytrade.trading.domain.forex.event.Event;
import com.apssouza.mytrade.trading.domain.forex.order.OrderCreatedEvent;
import com.apssouza.mytrade.trading.domain.forex.common.observer.PropertyChangeEvent;
import com.apssouza.mytrade.trading.domain.forex.common.observer.PropertyChangeListener;


public class OrderCreatedListener implements PropertyChangeListener {

    private final OrderHandler orderHandler;

    public OrderCreatedListener(OrderHandler orderHandler) {
        this.orderHandler = orderHandler;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Event e = (Event) evt.getNewValue();
        if (!(e instanceof OrderCreatedEvent)) {
            return;
        }

        OrderCreatedEvent event = (OrderCreatedEvent) e;
        this.orderHandler.persist(event.getOrder());
    }
}
