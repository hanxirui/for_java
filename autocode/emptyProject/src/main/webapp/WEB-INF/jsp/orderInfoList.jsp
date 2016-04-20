<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../taglib.jsp" %>
<c:set var="bluenile" value="${res}bluenile/"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Bootstrap Admin</title>
    <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

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
            
    <br>
     <form id="frm" class="form-inline" action="${ctx}jsp/orderInfoManager.do" method="post">
		订单号:<input name="orderIdSch" type="text" value="${searchData.orderIdSch}">
		手机号:<input name="mobileSch" type="text" value="${searchData.mobileSch}">
		地址:<input name="addressSch" type="text" value="${searchData.addressSch}">
		<input id="pageIndex" name="pageIndex" type="hidden" value="${resultHolder.currentPageIndex}">
		<button class="btn" onclick="search()">
		<i class="icon-search"></i>
		查询</button>
		<input type="reset" class="btn" value="清空">
	</form>
    <hr>
	<div class="btn-toolbar">
	    <button class="btn btn-primary" onclick="add()"><i class="icon-plus"></i> 新增订单</button>
	  <div class="btn-group">
	  </div>
	</div>
	
    <table class="table table-bordered table-striped">
	<thead>
		<tr>
			<th>#</th>
			<th>订单号</th>
			<th>城市</th>
			<th>手机号码</th>
			<th>地址</th>
			<th>下单时间</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${resultHolder.rows}" var="orderInfo" varStatus="stat">
			<tr
			<c:if test="${stat.index%2==0}">
				class="odd"
			</c:if>
			>
				<td>${stat.index+1}</td>
				<td>${orderInfo.orderId}</td>
				<td>${orderInfo.cityName}</td>
				<td>${orderInfo.mobile}</td>
				<td>${orderInfo.address}</td>
				<td><fmt:formatDate value="${orderInfo.createDate}" pattern="yyyy-MM-dd"/></td>
				<td>
					<a href="${ctx}jsp/editOrderInfo.do?id=${orderInfo.orderId}">编辑</a>
					|
					<a href="#" onclick="del(${orderInfo.orderId});return false;">删除</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="10">
				<a href="javascript:void(0)" onclick="goPage(${resultHolder.firstPageIndex})">首页</a>
				<a href="javascript:void(0)" onclick="goPage(${resultHolder.prePageIndex})">上一页</a>
				<a href="javascript:void(0)" onclick="goPage(${resultHolder.nextPageIndex})">下一页</a>
				<a href="javascript:void(0)" onclick="goPage(${resultHolder.lastPageIndex})">尾页</a>
				|
				第 <c:out value="${resultHolder.currentPageIndex}"/>/<c:out value="${resultHolder.pageCount}"/> 页 |
				共<c:out value="${resultHolder.total}"/>条记录 
			</td>
		</tr>
	</tfoot>
</table>

 <footer>
     <hr>
     <p>&copy; 2016</p>
 </footer>
                    
            </div>
        </div>
    </div>
    


    <script src="${bluenile}lib/bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript">
    function goPage(index){
		document.getElementById('pageIndex').value = index;
		document.getElementById('frm').submit();
	}
    
    function search(){
		goPage(1);
	}
    
    function del(id){
    	var d = dialog({
    	    title: '提示',
    	    width: 200,
    	    content: '确定要删除订单('+id+')吗?',
    	    okValue: '确定',
    	    ok: function () {
    	    	location.href = 'delOrderInfo.do?id='+id;
    	    },
    	    cancelValue: '取消',
    	    cancel: function () {}
    	});
    	d.showModal();
	}
    
    function add() {
    	location.href = 'orderInfoAdd.do';
    }
    </script>
  </body>
</html>

