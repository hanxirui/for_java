package com.hxr.javatone.radnom;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created with IntelliJ IDEA.
 * Project: test-jar
 * Author: Kevin
 * Date: 2017/6/27
 * Time: 下午10:16
 */
public class RandomApp {


    private static final long COUNT = 10000000;
    private static final int THREADS = 10;


	public static void main(String[] args) {
		System.out.println("Shared Random");
		testRandom(THREADS, COUNT);
		System.out.println("ThreadLocal<Random>");
		testTL_Random(THREADS, COUNT);
		System.out.println("ThreadLocalRandom");
		testTLRandom(THREADS, COUNT);
		System.out.println("Shared Random[] with no padding");
		testRandomArray(THREADS, COUNT, 1);
		System.out.println("Shared Random[] with padding");
		testRandomArray(THREADS, COUNT, 2);
	}


    //runner for all tests
    private static class RandomTask implements Runnable
    {
        private final Random rnd;
        protected final int id;
        private final long cnt;
        private final CountDownLatch latch;

        private RandomTask(Random rnd, int id, long cnt, CountDownLatch latch) {
            this.rnd = rnd;
            this.id = id;
            this.cnt = cnt;
            this.latch = latch;
        }

        protected Random getRandom()
        {
            return rnd;
        }

        @Override
        public void run() {
            try {
                final Random r = getRandom();
                latch.countDown();
                latch.await();
                final long start = System.currentTimeMillis();
                int sum = 0;
                for ( long j = 0; j < cnt; ++j )
                {
                    sum += r.nextInt();
                }
                final long time = System.currentTimeMillis() - start;
                System.out.println( "Thread #" + id + " Time = " + time / 1000.0 + " sec, sum = " + sum );
            } catch (InterruptedException e) {
            }
        }
    }

    private static void testRandom( final int threads, final long cnt )
    {
        final CountDownLatch latch = new CountDownLatch( threads );
        final Random r = new Random( 100 );
        for ( int i = 0; i < threads; ++i )
        {
            final Thread thread = new Thread( new RandomTask( r, i, cnt, latch ) );
            thread.start();
        }
    }

    private static void testRandomArray( final int threads, final long cnt, final int padding )
    {
        final CountDownLatch latch = new CountDownLatch( threads );
        final Random[] rnd = new Random[threads * padding];
        for ( int i = 0; i < threads * padding; ++i ) //allocate together
            rnd[ i ] = new Random( 100 );
        for ( int i = 0; i < threads; ++i )
        {
            final Thread thread = new Thread( new RandomTask( rnd[ i * padding ], i, cnt, latch ) );
            thread.start();
        }
    }

    private static void testTLRandom( final int threads, final long cnt )
    {
        final CountDownLatch latch = new CountDownLatch( threads );
        for ( int i = 0; i < threads; ++i )
        {
            final Thread thread = new Thread( new RandomTask( null, i, cnt, latch ) {
                @Override
                protected Random getRandom() {
                    return ThreadLocalRandom.current();
                }
            } );
            thread.start();
        }
    }

    private static void testTL_Random( final int threads, final long cnt )
    {
        final CountDownLatch latch = new CountDownLatch( threads );
        final ThreadLocal<Random> rnd = new ThreadLocal<Random>() {
            @Override
            protected Random initialValue() {
                return new Random( 100 );
            }
        };
        for ( int i = 0; i < threads; ++i )
        {
            final Thread thread = new Thread( new RandomTask( null, i, cnt, latch ) {
                @Override
                protected Random getRandom() {
                    return rnd.get();
                }
            } );
            thread.start();
        }
    }

//
//    任何情况下都不要在多个线程间共享一个java.util.Random实例，而该把它放入ThreadLocal之中。
//    Java7在所有情形下都更推荐使用java.util.concurrent.ThreadLocalRandom——它向下兼容已有的代码且运营成本更低
}
