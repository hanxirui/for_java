package com.ez.framework.core.scheduler;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ez.framework.core.dao.BaseDAO;

/**
 * @author liuyang
 *
 */
@Repository
public class SchedulerPersister extends BaseDAO<Scheduler> {

	private static final String INSERT_SCHEDULER_PARAMS = "insertSchedulerParams";
	private static final String DELETE_SCHEDULER_PARAMS = "deleteSchedulerParams";
	private static final String GET_SCHEDULER_PARAMS = "getSchedulerParams";
	public static final String SERVICE_ID = "schedulerPersister";
	private static final String DELETE_SCHEDULER_BY_ID = "deleteSchedulerById";
	private static final String GET_SCHEDULER_LIST = "getSchedulerList";
	private static final String INSERT_SCHEDULER = "insertScheduler";

	/**
	 * {method description}.
	 * @param scheduler
	 * @throws DBException
	 */
	public void insertScheduler(Scheduler scheduler) {
		getDam().insert(INSERT_SCHEDULER, scheduler);
	}

	/**
	 * {method description}.
	 * @return
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public List<Scheduler> getSchedulerList() {
		return getDam().selectList(GET_SCHEDULER_LIST,null);
	}

	/**
	 * {method description}.
	 * @param schedulerId
	 * @throws DBException
	 */
	public void deleteSchedulerById(String schedulerId) {
		getDam().delete(DELETE_SCHEDULER_BY_ID, schedulerId);
	}
	
	public void insertSchedulerParams(Map<String, String> param) {
		getDam().insert(INSERT_SCHEDULER_PARAMS, param);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getSchedulerParams(String id) {
		return getDam().selectList(GET_SCHEDULER_PARAMS, id);
	}
	
	public void deleteSchedulerParams(String id) {
		getDam().delete(DELETE_SCHEDULER_PARAMS, id);
	}
}
