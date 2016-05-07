<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="ctx" value='<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/" %>'/>
<c:set var="res" value="${ctx}res/"/>
<c:set var="AdminLTE" value="${res}AdminLTE/"/>
<c:set var="bsgrid" value="${res}bsgrid/"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    
    <title>LBS管理</title>
    <script type="text/javascript">var ctx = '${ctx}';</script>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
    <!-- Bootstrap 3.3.5 -->
    <link rel="stylesheet" href="${AdminLTE}bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="${AdminLTE}plugins/font-awesome/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="${res}css/ionicons.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="${AdminLTE}dist/css/AdminLTE.min.css">
    <!-- AdminLTE Skins. We have chosen the skin-blue for this starter
          page. However, you can choose any other skin. Make sure you
          apply the skin class to the body tag so the changes take effect.
    -->
    <link rel="stylesheet" href="${AdminLTE}dist/css/skins/skin-blue-light.min.css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="${AdminLTE}plugins/html5shiv.js"></script>
        <script src="${AdminLTE}plugins/respond.min.js"></script>
    <![endif]-->
    <script src="${AdminLTE}plugins/jQuery/jquery.min.js"></script>
    
    <script src="${res}js/common.js" type="text/javascript"></script>
    <!-- datepicker -->
    <script src="${res}My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <!-- validate -->
	<script src="${res}jqueryValidate/jquery.validate.min.js" type="text/javascript"></script>
	<script src="${res}jqueryValidate/localization/messages_zh.min.js" type="text/javascript"></script>
    <!-- dialog -->
	<link rel="stylesheet" href="${res}artDialog/css/ui-dialog.css">
	<script src="${res}artDialog/dist/dialog-plus-min.js"></script>
	<!-- bsgrid -->
	<link rel="stylesheet" href="${bsgrid}merged/bsgrid.all.min.css"/>
	<link rel="stylesheet" href="${bsgrid}css/skins/grid_bootstrap.min.css"/>
    <script type="text/javascript" src="${bsgrid}js/lang/grid.zh-CN.min.js"></script>
    <script type="text/javascript" src="${bsgrid}merged/bsgrid.all.min.js"></script>
    <script src="${res}easyui/plugins/jquery.messager.js"></script>
    <script src="${res}js/ajaxfileupload.js" type="text/javascript"></script>
    <script src="${res}js/ajaxupload.js" type="text/javascript"></script>
  
  </head>
