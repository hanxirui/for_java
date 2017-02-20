<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp"%>
<%
response.setHeader("Pragma","No-cache"); 
response.setHeader("Cache-Control","no-store"); 
response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title><sitemesh:write property="title" /></title>
	<script type="text/javascript">
		var ctx = '${ctx}';
	</script>
	<link href="${ctx}favicon.ico" rel="SHORTCUT ICON">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="${bui}css/dpl-min.css" rel="stylesheet" type="text/css" />
	<link href="${bui}css/bui-min.css" rel="stylesheet" type="text/css" />
	<link href="${bui}css/page-min.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${bui}js/jquery-1.8.1.min.js"></script>
	<script type="text/javascript" src="${bui}js/bui-min.js"></script>
	<script type="text/javascript" src="${res}js/common.js"></script>
	<jsp:include page="../right.jsp" />
	<sitemesh:write property="head" />
</head>
<body>
	<sitemesh:write property="body" />
</body>
</html>
