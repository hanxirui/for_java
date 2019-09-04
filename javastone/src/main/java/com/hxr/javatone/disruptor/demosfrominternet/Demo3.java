package com.hxr.javatone.disruptor.demosfrominternet;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 生产者生产数据经过C1,C2处理完成后再到C3。
 *
 * 1、交易网关收到交易(P1)把交易数据发到RingBuffer中,
 *
 * 2、负责处理增值业务的消费者C1和负责数据存储的消费者C2负责处理交易
 *
 * 3、负责发送JMS消息的消费者C3在C1和C2处理完成后再进行处理。
 *
 * @author hanxirui
 */
class TradeTransactionPublisher implements Runnable {
	Disruptor disruptor;
	private CountDownLatch latch;
    /**
     * 模拟一千万次交易的发生
      */
	private static int LOOP = 10;

	public TradeTransactionPublisher(CountDownLatch latch, Disruptor disruptor) {
		this.disruptor = disruptor;
		this.latch = latch;
	}

	@Override
	public void run() {
		TradeTransactionEventTranslator tradeTranslator = new TradeTransactionEventTranslator();
		for (int i = 0; i < LOOP; i++) {
			disruptor.publishEvent(tradeTranslator);
		}
		latch.countDown();
	}

}

class TradeTransactionEventTranslator implements EventTranslator<TradeTransaction> {
	private Random random = new Random();

	@Override
	public void translateTo(TradeTransaction event, long sequence) {
		this.generateTradeTransaction(event);
	}

	private TradeTransaction generateTradeTransaction(TradeTransaction trade) {
	    trade.setId(random.nextInt()+"");
		trade.setPrice(random.nextDouble() * 9999);
		return trade;
	}
}

public class Demo3 {
	public static void main(String[] args) throws InterruptedException {
		long beginTime = System.currentTimeMillis();

		int bufferSize = 1024;
		ExecutorService executor = Executors.newFixedThreadPool(4);
//这个构造函数参数,相信你在了解上面2个demo之后就看下就明白了,不解释了~
		Disruptor disruptor = new Disruptor(new EventFactory() {
			@Override
			public TradeTransaction newInstance() {
				return new TradeTransaction();
			}
		}, bufferSize, executor, ProducerType.SINGLE, new BusySpinWaitStrategy());

        //使用disruptor创建消费者组C1,C2
        //声明在C1,C2完事之后执行JMS消息发送操作 也就是流程走到C3

        disruptor.handleEventsWith(new TradeTransactionVasConsumer(),
				new TradeTransactionInDBHandler()).then(new TradeTransactionJMSNotifyHandler());

		disruptor.start();// 启动
		CountDownLatch latch = new CountDownLatch(1);
        //生产者准备
		executor.submit(new TradeTransactionPublisher(latch, disruptor));
		latch.await();// 等待生产者完事.
		disruptor.shutdown();
		executor.shutdown();

		System.out.println("总耗时:" + (System.currentTimeMillis() - beginTime));
	}
}