package com.ez.framework.core.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;

import com.ez.framework.core.scheduler.SchedulerManager;

/**
 * DispatcherServlet增加ModelAndView池化,
 * 目的是将ModelAndView对象放入Pool中，避免每次请求都new，提高性能考虑<br>
 * <p>
 * Create on : 2011-7-28<br>
 * <p>
 * </p>
 * <br>
 * 
 * @author liqiang<br>
 * @version riil-web-common v1.0
 *          <p>
 *          <br>
 *          <strong>Modify History:</strong><br>
 *          user modify_date modify_content<br>
 *          -------------------------------------------<br>
 *          <br>
 */
public class BaseDispatcherServlet extends DispatcherServlet {

	/**
	 * <code>modelandview</code> - 数据和视图对象池.
	 */
	private static final List<ModelAndView> S_MODELANDVIEW = new ArrayList<ModelAndView>();

	/**
	 * <code>serialVersionUID</code> - 序列化ID.
	 */
	private static final long serialVersionUID = 7927576220708897702L;

	/**
	 * 从对象池中获取ModelAndView对象.
	 * 
	 * @return ModelAndView对象
	 */
	public static ModelAndView getModeAndView() {
		synchronized (S_MODELANDVIEW) {
			if (S_MODELANDVIEW.size() > 0) {
				return S_MODELANDVIEW.remove(0);
			} else {
				return new ModelAndView();
			}
		}
	}

	@Override
	public void init(final ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);

		// 启动定时任务
		SchedulerManager.getInstance().start();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.DispatcherServlet#render(org.
	 * springframework.web.servlet.ModelAndView,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void render(final ModelAndView mv, final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {
		super.render(mv, request, response);
		releaseModelAndView(mv);
	}

	/**
	 * 清除ModelAndView对象内容，并且把对象返还给对象池.
	 * 
	 * @param mv
	 *            待清除ModelAndView对象
	 */
	private void releaseModelAndView(final ModelAndView mv) {
		mv.getModelMap().clear();
		mv.clear();
		synchronized (S_MODELANDVIEW) {
			S_MODELANDVIEW.add(mv);
		}
	}

}
