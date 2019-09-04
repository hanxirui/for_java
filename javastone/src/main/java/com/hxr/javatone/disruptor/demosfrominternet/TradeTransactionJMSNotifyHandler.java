package com.hxr.javatone.disruptor.demosfrominternet;

import com.lmax.disruptor.EventHandler;

/**
 * @author hanxirui
 */
public class TradeTransactionJMSNotifyHandler implements EventHandler<TradeTransaction> {
	@Override
	public void onEvent(TradeTransaction event, long sequence, boolean endOfBatch) throws Exception {
        //do send jms message
		System.out.println("JMS :: " + event.getId());
	}
}