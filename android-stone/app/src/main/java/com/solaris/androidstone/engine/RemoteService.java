package com.solaris.androidstone.engine;

import com.deta.androidlib.activity.BaseActivity;
import com.deta.androidlib.net.DefaultThreadPool;
import com.deta.androidlib.net.HttpRequest;
import com.deta.androidlib.net.RequestCallback;
import com.deta.androidlib.net.RequestParameter;
import com.deta.androidlib.net.URLData;
import com.deta.androidlib.net.UrlConfigManager;

import java.util.List;


public class RemoteService {
	private static RemoteService service = null;

	private RemoteService() {

	}

	public static synchronized RemoteService getInstance() {
		if (RemoteService.service == null) {
			RemoteService.service = new RemoteService();
		}
		return RemoteService.service;
	}

	public void invoke(final BaseActivity activity,
			final String apiKey,
			final List<RequestParameter> params,
			final RequestCallback callBack) {
		final URLData urlData = UrlConfigManager.findURL(activity, apiKey);
		HttpRequest request = activity.getRequestManager().createRequest(
				urlData, params, callBack);
		DefaultThreadPool.getInstance().execute(request);
	}
}