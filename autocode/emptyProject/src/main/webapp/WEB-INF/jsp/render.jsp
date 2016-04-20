<%@page import="org.durcframework.core.controller.BaseController"%>
<%@ page import="com.alibaba.fastjson.serializer.SerializerFeature"%>
<%@ page import="com.alibaba.fastjson.JSON"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%
response.setHeader("Pragma","No-cache"); 
response.setHeader("Cache-Control","no-cache"); 
response.setDateHeader("Expires", 0);
%>
<%=JSON.toJSONString(request.getAttribute(BaseController.DEF_MODEL_NAME), SerializerFeature.WriteDateUseDateFormat) %>