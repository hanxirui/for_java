package com.ez.framework.core.scheduler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.ez.framework.core.dam.tx.TransactionManager;
import com.ez.framework.core.service.ServiceContainer;
import com.ez.framework.core.service.ServiceContainerException;
import com.ez.framework.core.util.DefaultConfiguration;

/**
 * 定时任务管理器 <br>
 * <p>
 * {@link SchedulerManager}启动时会加载classpath下的scheduler-config.properties配置文件. <br>
 * 配置文件内容如下： <br>
 * <br>
 * <code>
 * scheduler.jobs=job1, job2
 * <br>
 * <br>
 * scheduler.job1.class=test.scheduler.TestJob1
 * <br>
 * scheduler.job1.cron=0/2 * * * * ?
 * <br>
 * <br>
 * scheduler.job2.class=test.scheduler.TestJob2
 * <br>
 * scheduler.job2.cron=0/5 * * * * ?
 * </code> <br>
 * </p>
 *
 */
public final class SchedulerManager {
	
	/**
	 * <code>PARAM_SID</code> - schedule参数的ID列名.
	 */
	private static final String PARAM_SID = "SID";

	/**
	 * <code>PARAM_PVAL</code> - schedule参数的值的列名.
	 */
	private static final String PARAM_PVAL = "PVAL";

	/**
	 * <code>PARAM_PKEY</code> - schedule参数的键的列名.
	 */
	private static final String PARAM_PKEY = "PKEY";

	/**
	 * <code>QUARTZ_PROPERTIES</code> - quartz properties file.
	 */
	private static final String QUARTZ_PROPERTIES = "quartz.properties";

	/**
	 * <code>logger</code> - Logger.
	 */
	private static final Logger s_logger = Logger.getLogger(SchedulerManager.class);

	/**
	 * <code>instance</code> - {@link SchedulerManager} instance.
	 */
	private static SchedulerManager instance;

	/**
	 * <code>classLoader</code> - classloader.
	 */
	private static ClassLoader classLoader;

	/**
	 * <code>scheduler</code> - quartz scheduler.
	 */
	private Scheduler schedulerContainer;

	/**
	 * <code>persister</code> - persister.
	 */
	private SchedulerPersister persister;

	/**
	 * <code>schedulerMap</code> - schedule cache.
	 */
	private final Map<String, com.ez.framework.core.scheduler.Scheduler> schedulerMap = new ConcurrentHashMap<String, com.ez.framework.core.scheduler.Scheduler>();


	/**
	 * Set class loader.
	 * 
	 * @param classLoader
	 */
	public static void setClassLoader(final ClassLoader classLoader) {
		SchedulerManager.classLoader = classLoader;
	}

	/**
	 * Constructors.
	 */
	private SchedulerManager() {
		try {
			initialize();
		} catch (Exception e) {
			s_logger.error(e.getMessage(), e);
		}
	}
	/**
	 * Initialize.
	 * @throws IOException
	 * @throws SchedulerException
	 * @throws ContainerException
	 */
	protected void initialize() throws IOException, SchedulerException, ServiceContainerException {
		persister = ServiceContainer.getAppContext().getBean(SchedulerPersister.class);
		Properties props = new Properties();
		props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(QUARTZ_PROPERTIES));
		schedulerContainer = new StdSchedulerFactory(props).getScheduler();

		initializeFromProperties();
		initializeFromDatabase();
	}

	/**
	 * init schedule from properties file.
	 */
	protected void initializeFromProperties() {
		try {
			PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver(Thread.currentThread().getContextClassLoader());
			Resource[] resources = patternResolver.getResources("classpath*:scheduler-config*.properties");
			for (Resource resource : resources) {
				DefaultConfiguration config = new DefaultConfiguration(resource.getFilename(), Boolean.TRUE);
				buildJobs(config);
			}
		} catch (FileNotFoundException e) {
			s_logger.error(e.getMessage());
		} catch (Exception e) {
			s_logger.error(e.getMessage(), e);
		}
	}

	/**
	 * init schedule from database.
	 */
	protected void initializeFromDatabase() {
		try {
			List<com.ez.framework.core.scheduler.Scheduler> schedulerList = getSchedulerList();
			for (com.ez.framework.core.scheduler.Scheduler scheduler : schedulerList) {
				initializeFromDatabase(scheduler);
			}
		} catch (Exception e) {
			s_logger.error(e.getMessage(), e);
		}
	}

	/**
	 * get schedule data from database.
	 * @return
	 * @throws ServiceContainerException 
	 * @throws DBException
	 */
	private List<com.ez.framework.core.scheduler.Scheduler> getSchedulerList() throws ServiceContainerException {
		List<com.ez.framework.core.scheduler.Scheduler> schedulerList = persister.getSchedulerList();
		for (com.ez.framework.core.scheduler.Scheduler scheduler : schedulerList) {
			String schdId = scheduler.getId();
			List<Map<String, String>> parmaList = persister.getSchedulerParams(schdId);
			if (parmaList != null) {
				JobDataMap _dataMap = new JobDataMap();
				for (Map<String, String> param : parmaList) {
					_dataMap.put(param.get(PARAM_PKEY), param.get(PARAM_PVAL));
				}
				scheduler.setJobDataMap(_dataMap);
			}
		}
		return schedulerList;
	}

	/**
	 * init schedule from database.
	 * @param scheduler
	 */
	private void initializeFromDatabase(final com.ez.framework.core.scheduler.Scheduler scheduler) {
		try {
			addScheduler(SchedulerBuilder.rebuild(scheduler), Boolean.FALSE);
		} catch (Exception e) {
			s_logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Build jobs.
	 * 
	 * @param config
	 */
	protected void buildJobs(final DefaultConfiguration config) {
		String[] jobNames = config.getArray("scheduler.jobs", ',', null);
		if (jobNames != null) {
			for (String jobName : jobNames) {
				buildJob(jobName, config);
			}
		}
	}

	/**
	 * Build job.
	 * @param jobName
	 * @param config
	 */
	protected void buildJob(final String jobName, final DefaultConfiguration config) {
		if (jobName == null || jobName.trim().length() == 0) {
			return;
		}
		try {
			String _jobClassName = config.get(String.format("scheduler.%s.class", jobName), null);
			if (StringUtils.isNotEmpty(_jobClassName)) {
				String _cronExpression = config.get(String.format("scheduler.%s.cron", jobName), null);
				SchedulerBuilder builder = SchedulerBuilder.newScheduler().setId(jobName).setCycleType(CycleType.Day)
						.setSchedulerType(SchedulerType.properties).setCronExpression(_cronExpression).setJobClass(findClass(_jobClassName))
						.setJobDataMap(createJobDataMap(jobName, config));
				addScheduler(builder.build(), Boolean.FALSE);
			} else {
				s_logger.error("job's class not found " + jobName);
			}
		} catch (Exception e) {
			s_logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Build job parameters.
	 * 
	 * @param jobName
	 * @param config
	 * @return
	 */
	private JobDataMap createJobDataMap(final String jobName, final DefaultConfiguration config) {
		Map<String, String> map = config.getMap(String.format("scheduler.%s.params", jobName), ',', ':', null);
		if (map == null)
			return null;
		JobDataMap datamap = new JobDataMap(map);
		return datamap;
	}

	/**
	 * Start.
	 */
	public void start() {
		try {
			if (schedulerContainer.isInStandbyMode() || schedulerContainer.isShutdown())
				schedulerContainer.start();
		} catch (SchedulerException e) {
			s_logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Shutdown.
	 */
	public void shutdown() {
		try {
			schedulerContainer.shutdown(Boolean.TRUE);
		} catch (SchedulerException e) {
			s_logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Standby.
	 */
	public void standby() {
		try {
			if (!schedulerContainer.isInStandbyMode())
				schedulerContainer.standby();
		} catch (SchedulerException e) {
			s_logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Find class of a job.
	 * 
	 * @param <T>
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	protected static Class<AbstractJob> findClass(final String className) throws ClassNotFoundException {
		if (classLoader != null) {
			return (Class<AbstractJob>) classLoader.loadClass(className);
		} else {
			return (Class<AbstractJob>) SchedulerManager.class.getClassLoader().loadClass(className);
		}
	}

	/**
	 * Get instance.
	 * 
	 * @return
	 */
	public static SchedulerManager getInstance() {
		synchronized (SchedulerManager.class) {
			if (instance == null) {
				instance = new SchedulerManager();
			}
			return instance;
		}
	}

	/**
	 * {method description}.
	 * 
	 * @return
	 */
	public Collection<com.ez.framework.core.scheduler.Scheduler> getAllSchedulerList() {
		return new ArrayList<com.ez.framework.core.scheduler.Scheduler>(schedulerMap.values());
	}

	/**
	 * {method description}.
	 * 
	 * @return
	 */
	public Collection<com.ez.framework.core.scheduler.Scheduler> getCustomSchedulerList() {
		List<com.ez.framework.core.scheduler.Scheduler> list = new ArrayList<com.ez.framework.core.scheduler.Scheduler>();
		for (com.ez.framework.core.scheduler.Scheduler s : schedulerMap.values()) {
			if (SchedulerType.custom == s.getSchedulerType()) {
				list.add(s);
			}
		}
		return list;
	}

	/**
	 * get a schedule by id.
	 * 
	 * @param schedulerId
	 * @return
	 */
	public com.ez.framework.core.scheduler.Scheduler getScheduler(final String schedulerId) {
		com.ez.framework.core.scheduler.Scheduler scheduler = schedulerMap.get(schedulerId);
		return scheduler == null ? null : scheduler.clone();
	}

	/**
	 * delete a schedule.
	 * 
	 * @param schedulerId
	 * @throws com.riil.core.scheduler.SchedulerException
	 */
	public void deleteScheduler(final String schedulerId) throws com.ez.framework.core.scheduler.SchedulerException {
		try {
			schedulerContainer.deleteJob(SchedulerBuilder.generateJobKey(schedulerId));
			TransactionManager.beginTransaction();
			persister.deleteSchedulerById(schedulerId);
			persister.deleteSchedulerParams(schedulerId);
			TransactionManager.commitTransaction();
			schedulerMap.remove(schedulerId);
		} catch (Exception e) {
			throw new com.ez.framework.core.scheduler.SchedulerException(e.getMessage(), e);
		}
	}

	/**
	 * execute a schedule and record it in database.
	 * 
	 * @param scheduler
	 * @throws com.riil.core.scheduler.SchedulerException
	 */
	public void addScheduler(final com.ez.framework.core.scheduler.Scheduler scheduler) throws com.ez.framework.core.scheduler.SchedulerException {
		addScheduler(scheduler, Boolean.TRUE);
	}

	/**
	 * add a schedule into container and execute it.
	 * 
	 * @param scheduler
	 * @throws com.riil.core.scheduler.SchedulerException
	 */
	public void executeScheduler(final com.ez.framework.core.scheduler.Scheduler scheduler) throws com.ez.framework.core.scheduler.SchedulerException {
		try {
			if(scheduler.isExecutable()) {
				schedulerContainer.scheduleJob(scheduler.getJobDetail(), scheduler.getTrigger());
			}
			schedulerMap.put(scheduler.getId(), scheduler);
		} catch (Exception e) {
			throw new com.ez.framework.core.scheduler.SchedulerException(e.getMessage(), e);
		}
	}

	/**
	 * add a schedule into container and execute it.
	 * 
	 * @param scheduler
	 * @param record insert this schedule into database if TRUE;
	 * @throws com.riil.core.scheduler.SchedulerException
	 */
	private void addScheduler(final com.ez.framework.core.scheduler.Scheduler scheduler, final boolean record) throws com.ez.framework.core.scheduler.SchedulerException {
		if (schedulerMap.containsKey(scheduler.getId())) {
			throw new com.ez.framework.core.scheduler.SchedulerException("Can not find scheduler id:"+scheduler.getId());
		}
		
		if (record) {
			recordScheduler(scheduler);
		}
		executeScheduler(scheduler);
	}

	/**
	 * insert schedule into database.
	 * 
	 * @param scheduler
	 * @throws com.riil.core.scheduler.SchedulerException
	 */
	private void recordScheduler(final com.ez.framework.core.scheduler.Scheduler scheduler) throws com.ez.framework.core.scheduler.SchedulerException {
		try {
			insertScheduler(scheduler);
		} catch (Exception e) {
			throw new com.ez.framework.core.scheduler.SchedulerException(e.getMessage(), e);
		}
	}

	/**
	 * insert schedule into database.
	 * @param scheduler
	 * @throws DBException
	 */
	private void insertScheduler(final com.ez.framework.core.scheduler.Scheduler scheduler) {
		TransactionManager.beginTransaction();
		persister.insertScheduler(scheduler);
		JobDataMap dataMap = scheduler.getJobDataMap();
		if (dataMap != null && dataMap.size() > 0) {
			for (String _key : dataMap.keySet()) {
				Map<String, String> param = new HashMap<String, String>();
				param.put(PARAM_SID, scheduler.getId());
				param.put(PARAM_PKEY, _key);
				param.put(PARAM_PVAL, dataMap.getString(_key));
				persister.insertSchedulerParams(param);
			}
		}
		TransactionManager.commitTransaction();
	}

}
