package com.apssouza.mytrade.trading.forex.signalfeed;

import com.apssouza.mytrade.feed.api.FeedModule;
import com.apssouza.mytrade.feed.api.SignalDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SignalFeedHandler is responsible for handle trading signals
 */
public class SignalFeedHandler {

    private FeedModule feedModule;

    public SignalFeedHandler(FeedModule feedModule) {
        this.feedModule = feedModule;
    }

    public List<SignalDto> getSignal(String systemName, final LocalDateTime currentTime) {
        return feedModule.getSignal(systemName, currentTime);
    }
}