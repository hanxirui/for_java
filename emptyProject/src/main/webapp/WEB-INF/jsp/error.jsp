<%@page import="org.durcframework.core.DefaultMessageResult"%>
<%@page import="com.alibaba.fastjson.JSON"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%=JSON.toJSONString( DefaultMessageResult.error( (String)request.getAttribute("error") ) ) %>