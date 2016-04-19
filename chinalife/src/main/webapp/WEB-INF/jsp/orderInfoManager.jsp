<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../taglib.jsp" %>
<c:set var="leoui" value='<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/res/leoui/" %>'/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>        
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />    
    <!--[if gt IE 8]>
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />        
    <![endif]-->                
    <title>Title</title>
    <link rel="icon" type="image/ico" href="favicon.ico"/>
    
    <link href="${leoui}css/stylesheets.css" rel="stylesheet" type="text/css" />
    
    <!--[if lte IE 7]>
        <script type="text/javascript" src="${leoui}js/other/lte-ie7.js"></script>
    <![endif]-->    
    
    <script type="text/javascript" src="${leoui}js/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="${leoui}js/jquery/jquery-ui-1.10.3.custom.min.js"></script>
    <script type="text/javascript" src="${leoui}js/jquery/jquery-migrate.min.js"></script>
    <script type="text/javascript" src="${leoui}js/jquery/globalize.js"></script>
    
    <script type="text/javascript" src="${leoui}js/bootstrap/bootstrap.min.js"></script>    
    <script type="text/javascript" src="${leoui}js/cookies/jquery.cookies.2.2.0.min.js"></script>
    
    <script type="text/javascript" src="${leoui}js/scrollup/jquery.scrollUp.min.js"></script>
    
    <script type="text/javascript" src="${leoui}js/plugins.js"></script>    
    <script type="text/javascript" src="${leoui}js/actions.js"></script>
</head>
<body>    
    
    <div id="wrapper" class="screen_wide">
        
        <div id="header">
            
            <div class="wrap">
                
                <a href="index.html" class="logo"></a>
                
                <div class="buttons">
                	<div class="item">                        
                        <div class="btn-group">                        
                            <a href="#" class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown">
                                <span class="i-forward"></span>
                            </a>
                            <ul class="dropdown-menu">
                                <li><a href="#"><span class="i-forward"></span> 退出</a></li>
                            </ul> 
                        </div>
                    </div>
                	
                </div>
                
            </div>
            
        </div>
        
        <div id="layout">
        
            <div id="sidebar">

                <div class="user">
                    <div class="info">
                        <div class="name">
                            你好,<a href="#">admin</a>
                        </div>
                        <div class="buttons">
                            您的角色是[管理员]
                        </div>
                    </div>
                </div>

                <ul class="navigation">
                    <li>
                        <a href="index.html">首页</a>
                    </li>
                    <li class="openable">
                        <a href="#">基础管理</a>
                        <ul>
                            <li>
                                <a href="orderInfoManager.do">订单管理</a>
                            </li>
                        </ul>
                    </li>  
                </ul>

            </div>

            <div id="content">                        
                <div class="wrap">
                    
                    <div class="head">
                        <div class="info">
                            <h1>订单管理</h1>
                            <ul class="breadcrumb">
                                <li> 管理您的订单</li>                                
                            </ul>
                        </div>                                                
                    </div>                                                                    
                    
                    <div class="container">

                        <div class="row">
                            
                            <div class="col-md-12">
                            
                             <div class="block">
							   <div class="block_wrapper" >
	                                <form id="frm" action="${ctx}jsp/orderInfoManager.do" method="post">
									订单号:<input name="orderIdSch" type="text" value="${searchData.orderIdSch}">
									地址:<input name="addressSch" type="text" value="${searchData.addressSch}">
									<input id="pageIndex" name="pageIndex" type="hidden" value="${resultHolder.currentPageIndex}">
									<button class="btn btn-sm btn-primary" onclick="searchStu()">查询</button>
									</form>
								</div>
							</div>

<div class="row">
 <div class="block">
   <div class="content np">
<table width="100%">
	<thead>
		<tr>
			<th></th>
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

                                
                            </div>
                                
                                </div>
                        </div>                        
                        
                    </div>
                        
                </div>
            </div>
            
        </div>

    </div>
    </div>
    </div>
    
    						
<script type="text/javascript">
	function goPage(index){
		document.getElementById('pageIndex').value = index;
		document.getElementById('frm').submit();
	}
	
	function searchStu(){
		goPage(1);
	}
	
	function del(id){
		var isTrue = confirm('确定要删除('+id+')吗?');
		if(isTrue){
			location.href = ctx + 'jsp/delOrderInfo.do?id='+id;
		}
	}
</script>
</body>
</html>
