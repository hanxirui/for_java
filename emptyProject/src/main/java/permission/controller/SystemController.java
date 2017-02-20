package permission.controller;

import org.durcframework.core.MessageResult;
import org.durcframework.core.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import permission.common.RMSContext;

@Controller
public class SystemController extends BaseController {
	// 刷新缓存
	@RequestMapping("refreshCache.do")
	public @ResponseBody MessageResult refreshCache() {
		RMSContext.getInstance().refreshAllUserRightData();
		return this.success();
	}
}
