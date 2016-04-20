<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>操作成功</title>
</head>
<body>
<div class="alert alert-success">操作成功！</div>
<c:if test="${!empty backUrl}">
	<input type="button" class="btn btn-primary" onclick="location='${backUrl}'" value="返回">
</c:if>
</body>
</html>