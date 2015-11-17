package com.hxr.webstone.job;

import java.util.Map;

import com.ez.framework.core.scheduler.AbstractJob;

public class TestJob extends AbstractJob {

	@Override
	public void execute(String jobName, Map<String, Object> params) throws Exception {
		System.out.println("---------------------------------------");

	}

}
