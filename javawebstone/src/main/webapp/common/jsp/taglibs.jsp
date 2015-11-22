<%@page import="java.util.Date"%>
<%@page import="com.hxr.webstone.util.WebUtil"%>
<%@page import="com.hxr.webstone.util.I18nUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% response.setHeader("P3P","CP=CAO PSA OUR");%>
<%
pageContext.setAttribute("ctx",WebUtil.getCtx(request),pageContext.PAGE_SCOPE);
pageContext.setAttribute("ctxCss",WebUtil.getCtx(request) + "/common/static/css",pageContext.PAGE_SCOPE);
pageContext.setAttribute("ctxImages",WebUtil.getCtx(request) +"/common/static/images",pageContext.PAGE_SCOPE);
pageContext.setAttribute("ctxJs",WebUtil.getCtx(request) + "/common/static/js" ,pageContext.PAGE_SCOPE);
pageContext.setAttribute("httpurl",WebUtil.getServerPath(request),pageContext.PAGE_SCOPE);
%>
<fmt:setLocale value="<%=I18nUtils.getCurrentLocale()%>"  scope="session"/>