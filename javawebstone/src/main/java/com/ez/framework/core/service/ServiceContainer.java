package com.ez.framework.core.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ServiceContainer implements ApplicationContextAware{
	private static ApplicationContext m_appContext;

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		m_appContext = context;
		
	}
	public static <T extends IService> T getService(final String serviceId) throws ServiceContainerException {
		T t_service = null;
		if (null != m_appContext && StringUtils.isNotBlank(serviceId)) {
			t_service = getBean(serviceId);
		} else {
			throw new ServiceContainerException("Can not find service.Service Id is>>>" + serviceId);
		}
		return t_service;
	}

	private static <T extends IService> T getBean(final String serviceId) throws ServiceContainerException {
		if (m_appContext.containsBean(serviceId)) {
			Object t_service = m_appContext.getBean(serviceId);
			if (t_service instanceof IService) {
				return (T) t_service;
			} else {
				throw new ServiceContainerException("Service is not implement IService interface.Service Id is>>>"
						+ serviceId);
			}
		} else {
			throw new ServiceContainerException("Can not find service.Service Id is>>>" + serviceId);
		}

	}

	public static <T> T getDatabaseComponent(final String id) throws ServiceContainerException {

		if (id == null || id.length() == 0) {
			throw new ServiceContainerException("Id is not empty.>>>");
		}
		if (m_appContext.containsBean(id)) {
			T t_ret = (T)m_appContext.getBean(id);
			return t_ret;
		}else{
			throw new ServiceContainerException("Can not find DatabaseComponent.Id is>>>" + id);
		}

	}
	public static ApplicationContext getAppContext(){
		return m_appContext;
	}
	

}
