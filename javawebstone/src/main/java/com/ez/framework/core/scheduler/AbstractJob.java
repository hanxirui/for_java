package com.ez.framework.core.scheduler;

import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 * @author liuyang
 *
 */
public abstract class AbstractJob implements org.quartz.Job{
	/**
	 * <code>logger</code> - {description}.
	 */
	private static final Logger S_LOG = Logger.getLogger(AbstractJob.class);
	
    /* (non-Javadoc)
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    public void execute(final JobExecutionContext context) throws JobExecutionException {
        try {
        	S_LOG.error(this.getClass().getName() + " is start...");
			execute(context.getJobDetail().getKey().getName(), context.getJobDetail().getJobDataMap().getWrappedMap());
			S_LOG.error(this.getClass().getName() + " is finish!");
		} catch (Exception e) {
			S_LOG.error(e.getMessage(), e);
			throw new JobExecutionException(e);
		}
    }
    
    /**
     * Execute job.
     * @param params
     */
    public abstract void execute(String jobName, Map<String, Object> params) throws Exception;

}
