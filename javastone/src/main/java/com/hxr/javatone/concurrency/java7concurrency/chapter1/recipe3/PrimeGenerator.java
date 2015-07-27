package com.hxr.javatone.concurrency.java7concurrency.chapter1.recipe3;

/**
 *  This class generates prime numbers until is interrumped
 */
public class PrimeGenerator extends Thread{

	/**
	 *  Central method of the class
	 */
	@Override
	public void run() {
		long number=1L;
		
		// This bucle never ends... until is interrupted
		while (true) {
			if (isPrime(number)) {
				System.out.printf("Number %d is Prime\n",number);
			}
			
			// When is interrupted, write a message and ends
			if (isInterrupted()) {
				System.out.printf("The Prime Generator has been Interrupted\n");
				return;
			}
//			加上这段话会有什么不同吗？？？ 可以试试
//			Interrupts this thread.
//
//			Unless the current thread is interrupting itself, which is always permitted, the checkAccess method of this thread is invoked, which may cause a SecurityException to be thrown.
//
//			If this thread is blocked in an invocation of the wait(), wait(long), or wait(long, int) methods of the Object class, or of the join(), join(long), join(long, int), sleep(long), or sleep(long, int), methods of this class, then its interrupt status will be cleared and it will receive an InterruptedException.
//
//			If this thread is blocked in an I/O operation upon an interruptible channel then the channel will be closed, the thread's interrupt status will be set, and the thread will receive a java.nio.channels.ClosedByInterruptException.
//
//			If this thread is blocked in a java.nio.channels.Selector then the thread's interrupt status will be set and it will return immediately from the selection operation, possibly with a non-zero value, just as if the selector's wakeup method were invoked.
//
//			If none of the previous conditions hold then this thread's interrupt status will be set.
//
//			Interrupting a thread that is not alive need not have any effect.


//			try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
			number++;
		}
	}

	/**
	 *  Method that calculate if a number is prime or not
	 * @param number : The number
	 * @return A boolean value. True if the number is prime, false if not.
	 */
	private boolean isPrime(final long number) {
		if (number <=2) {
			return true;
		}
		for (long i=2; i<number; i++){
			if ((number % i)==0) {
				return false;
			}
		}
		return true;
	}

}
