<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>监控页面</title>
</head>
<body>
<iframe src="${ctx}druid/index.html" id="iframe" scrolling="yes" frameborder="0" width="100%" height="100%"></iframe>
<script type="text/javascript">
$("#iframe").load(function(){
	$(this).height($('body').height());
});
</script>
</body>
</html>