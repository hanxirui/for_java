package com.ez.framework.core.dam.tx;

import org.springframework.transaction.TransactionStatus;

public final class TransactionContext {

    private String dataSource;
    
    private TransactionStatus txStatus;
    
    private int m_nestedTransaction = 0;
    
    private boolean m_rollback;
    
    private StackTraceElement[] stackTrace;

    private ThreadLocal<TransactionContext> m_contextHolder;
    
    TransactionContext(ThreadLocal<TransactionContext> contextHolder) {
    	m_contextHolder = contextHolder;
    }
    
    String getDataSource() {
        return dataSource;
    }

    void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

	TransactionStatus getTransactionStatus() {
		return txStatus;
	}

	void setTransactionStatus(TransactionStatus txStatus) {
		this.txStatus = txStatus;
	}

	/**
	 * @return m_nestedTransaction - {return content description}
	 */
	void releaseNestedTransaction() {
		m_nestedTransaction--;
	}

	/**
	 * @param m_nestedTransaction - {parameter description}.
	 */
	void acquireNestedTransaction() {
		m_nestedTransaction++;
	}

	boolean hasNestedTransaction() {
		return m_nestedTransaction > 0;
	}
	
	/**
	 * @return m_rollback - {return content description}
	 */
	public boolean isRollback() {
		return m_rollback;
	}

	/**
	 * @param m_rollback - {parameter description}.
	 */
	public void rollback() {
		m_rollback = true;
		stackTrace = new Throwable().getStackTrace();
	}

	/**
	 * @return stackTrace - {return content description}
	 */
	public StackTraceElement[] getRollbackStackTrace() {
		return stackTrace;
	}

	void bindToThread() {
		m_contextHolder.set(this);
	}
}
