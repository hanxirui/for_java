<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../taglib.jsp" %>
<c:set var="bluenile" value="${res}bluenile/"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>后台管理</title>
    <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" type="text/css" href="${bluenile}lib/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="${bluenile}stylesheets/theme.css">
    <link rel="stylesheet" href="${bluenile}lib/font-awesome/css/font-awesome.css">
    <script src="${bluenile}lib/jquery-1.7.2.min.js" type="text/javascript"></script>
    
	<link rel="stylesheet" href="${res}artDialog/css/ui-dialog.css">
	<script src="${res}artDialog/dist/dialog-plus-min.js"></script>

	<!--[if lt IE 9]>
		<script type="text/javascript" src="${bluenile}lib/bootstrap/js/respond.min.js"></script>
	<![endif]-->
    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="${bluenile}lib/html5.js"></script>
    <![endif]-->
  </head>

  <!--[if lt IE 7 ]> <body class="ie ie6"> <![endif]-->
  <!--[if IE 7 ]> <body class="ie ie7 "> <![endif]-->
  <!--[if IE 8 ]> <body class="ie ie8 "> <![endif]-->
  <!--[if IE 9 ]> <body class="ie ie9 "> <![endif]-->
  <!--[if (gt IE 9)|!(IE)]><!--> 
  <body class=""> 
  <!--<![endif]-->
    <div class="navbar">
        <div class="navbar-inner">
                <ul class="nav pull-right">
                    <li><a href="#" class="hidden-phone visible-tablet visible-desktop" role="button">设置</a></li>
                    <li id="fat-menu" class="dropdown">
                        <a href="#" role="button" class="dropdown-toggle" data-toggle="dropdown">
                            <i class="icon-user"></i> admin
                            <i class="icon-caret-down"></i>
                        </a>
                        <ul class="dropdown-menu">
                            <li><a tabindex="-1" href="#">我的账号</a></li>
                            <li class="divider"></li>
                            <li><a tabindex="-1" href="#">退出</a></li>
                        </ul>
                    </li>
                </ul>
                <a class="brand" href="#"><span class="first">后台管理</span></a>
        </div>
    </div>
    
    <div class="sidebar-nav">
        <a href="#dashboard-menu" class="nav-header" data-toggle="collapse"><i class="icon-dashboard"></i>基础管理</a>
        <ul id="dashboard-menu" class="nav nav-list collapse in">
            <li class="active"><a href="orderInfoManager.do">订单管理</a></li>
            
        </ul>
    </div>
    
    <div class="content">
        <div class="header">
            <span class="page-title">订单管理</span>
        </div>

        <div class="container-fluid">
            <div class="row-fluid">
				<!-- content here -->

				 <footer>
				     <hr>
				     <p>&copy; 2016</p>
				 </footer>
            </div>
        </div>
    </div>

    <script src="${bluenile}lib/bootstrap/js/bootstrap.js"></script>
  </body>
</html>
