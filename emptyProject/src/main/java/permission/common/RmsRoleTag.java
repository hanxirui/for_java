package permission.common;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.jstl.core.ConditionalTagSupport;

import permission.util.RightUtil;

/**
 * 权限检查标签
 * @author hc.tang
 *
 */
public class RmsRoleTag extends ConditionalTagSupport {
	private static final long serialVersionUID = 1L;
	
	private static final String SYS_RES_ID = "srId";
	// 操作代码
	private String operateCode;

	@Override
	protected boolean condition() throws JspTagException {
		return RightUtil.checkOperateCode(
				this.pageContext.getRequest().getParameter(SYS_RES_ID),
				operateCode);
	}

	public String getOperateCode() {
		return operateCode;
	}

	public void setOperateCode(String operateCode) {
		this.operateCode = operateCode;
	}

}
