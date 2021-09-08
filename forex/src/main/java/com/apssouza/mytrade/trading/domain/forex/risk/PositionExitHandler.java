package com.apssouza.mytrade.trading.domain.forex.risk;

import com.apssouza.mytrade.common.misc.helper.time.MarketTimeHelper;
import com.apssouza.mytrade.feed.api.SignalDto;
import com.apssouza.mytrade.trading.domain.forex.order.OrderDto;
import com.apssouza.mytrade.trading.domain.forex.portfolio.PortfolioModel;
import com.apssouza.mytrade.trading.domain.forex.portfolio.Position;
import com.apssouza.mytrade.trading.domain.forex.feed.pricefeed.PriceChangedEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

class PositionExitHandler {
    private final PortfolioModel portfolio;
    private static Logger log = Logger.getLogger(PositionExitHandler.class.getSimpleName());

    public PositionExitHandler(PortfolioModel portfolio) {
        this.portfolio = portfolio;
    }

    public List<Position> process(PriceChangedEvent event, List<SignalDto> signals) {
        if (this.portfolio.getPositions().isEmpty()){
            return Collections.emptyList();
        }
        log.info("Processing exits...");
        List<Position> exitedPositions = new ArrayList<>();
        for (Map.Entry<String, Position> entry : this.portfolio.getPositions().entrySet()) {
            Position position = entry.getValue();
            Position.ExitReason exit_reason = null;

            if (this.hasCounterSignal(position, signals)) {
                exit_reason = Position.ExitReason.COUNTER_SIGNAL;
            }
            if (exit_reason != null) {
                log.info("Exiting position for(" + position.getSymbol() + " Reason " + exit_reason);
                position = position.closePosition(exit_reason);
                portfolio.getPositions().put(position.getIdentifier(), position);
                exitedPositions.add(position);
            }
        }
        return exitedPositions;
    }

    private boolean isEndOfDay(LocalDateTime time) {
        return MarketTimeHelper.isEOD(time);
    }

    private SignalDto getSignalBySymbol(String symbol, List<SignalDto> signals) {
        for (SignalDto signal : signals) {
            if (signal.symbol().equals(symbol)) {
                return signal;
            }
        }
        return null;
    }


    private boolean hasCounterSignal(Position position, List<SignalDto> signals) {
        SignalDto signal = getSignalBySymbol(position.getSymbol(), signals);
        if (signal == null) {
            return false;
        }

        OrderDto.OrderAction exit_direction = null;
        if (position.getPositionType() == Position.PositionType.LONG) {
            exit_direction = OrderDto.OrderAction.SELL;
        } else {
            exit_direction = OrderDto.OrderAction.BUY;
        }

        if (OrderDto.OrderAction.valueOf(signal.action().toUpperCase()) == exit_direction) {
            return true;
        }
        return false;
    }

}