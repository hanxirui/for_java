package com.ez.framework.core.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.springframework.web.servlet.DispatcherServlet;

import com.ez.framework.core.scheduler.SchedulerManager;

/**
 * @author liuyang
 *
 */
public class BaseDispatcherServlet extends DispatcherServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 983562049066611848L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// 启动定时任务
        SchedulerManager.getInstance().start();
	}
}
