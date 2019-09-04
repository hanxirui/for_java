package com.hxr.javatone.disruptor.demosfrominternet;

import com.lmax.disruptor.EventHandler;

/**
 * @author hanxirui
 */
public class TradeTransactionVasConsumer implements EventHandler<TradeTransaction> {
    @Override
    public void onEvent(TradeTransaction event, long sequence,
                        boolean endOfBatch) throws Exception {
        System.out.println("Vas :: "+event.getId());
    }

}
