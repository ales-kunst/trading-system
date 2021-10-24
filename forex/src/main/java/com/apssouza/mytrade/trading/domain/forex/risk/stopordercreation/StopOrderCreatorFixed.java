package com.apssouza.mytrade.trading.domain.forex.risk.stopordercreation;

import com.apssouza.mytrade.feed.api.PriceDto;
import com.apssouza.mytrade.trading.domain.forex.portfolio.Position;
import com.apssouza.mytrade.trading.domain.forex.common.Event;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

class StopOrderCreatorFixed implements StopOrderCreator {
    private CreatorContext creatorContext;
    private final StopOrderConfigDto priceDistance;

    public StopOrderCreatorFixed(StopOrderConfigDto priceDistance) {
        this.priceDistance = priceDistance;
    }

    @Override
    public void createContext(Position.PositionType type) {
        if (type == Position.PositionType.LONG) {
            this.creatorContext = new CreatorContext(new LongPositionStrategy(priceDistance));
            return;
        }
        this.creatorContext = new CreatorContext(new ShortPositionStrategy(priceDistance));

    }

    @Override
    public StopOrderDto getHardStopLoss(Position position) {
        return creatorContext.getHardStopLoss(position);
    }

    @Override
    public StopOrderDto getProfitStopOrder(Position position) {
        return creatorContext.getProfitStopOrder(position);
    }

    @Override
    public Optional<StopOrderDto> getEntryStopOrder(Position position, Event event) {
        Map<String, PriceDto> price = event.getPrice();
        BigDecimal priceClose = price.get(position.getSymbol()).close();
        return creatorContext.getEntryStopOrder(position, priceClose);
    }

    @Override
    public Optional<StopOrderDto> getTrailingStopOrder(Position position, Event event) {
        Map<String, PriceDto> price = event.getPrice();
        BigDecimal priceClose = price.get(position.getSymbol()).close();
        return creatorContext.getTrailingStopOrder(position, priceClose);
    }

}