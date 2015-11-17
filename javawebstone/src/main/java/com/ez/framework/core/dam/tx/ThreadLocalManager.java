package com.ez.framework.core.dam.tx;

public final class ThreadLocalManager {

    /** <code>S_INSTANCE</code>-该类的唯一一个实例. */
    private static final ThreadLocalManager S_INSTANCE = new ThreadLocalManager();

    /** <code>THREAD_LOCAL_DAM</code>-DAM的ThreadLocal. */
	private static final ThreadLocal<TransactionContext> S_TRANSACTION_CONTEXT_HOLDER = new ThreadLocal<TransactionContext>();

    /**
     * 构造函数.
     */
    private ThreadLocalManager() {
    }

    /**
     * 获取该类的唯一一个实例的方法.
     *
     * @return <code>ThreadLocalManager</code>
     */
    public static ThreadLocalManager getInstance() {
        return ThreadLocalManager.S_INSTANCE;
    }

    /**
     * @return
     */
    public TransactionContext getTransactionContext() {
        TransactionContext context = S_TRANSACTION_CONTEXT_HOLDER.get();
        return context;
    }

    public TransactionContext createTransactionContext() {
    	return new TransactionContext(S_TRANSACTION_CONTEXT_HOLDER);
    }
    
    public void clear() {
    	S_TRANSACTION_CONTEXT_HOLDER.set(null);
    }
    
}
