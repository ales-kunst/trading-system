package com.apssouza.mytrade.trading.forex.execution;

public interface ExecutionHandler {
    void closeAllPositions();

    void cancelOpenLimitOrders();
}
