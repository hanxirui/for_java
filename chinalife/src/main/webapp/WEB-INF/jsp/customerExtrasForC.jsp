<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp" %>
<jsp:include page="../menu.jsp" >
    <jsp:param name="activeMenu" value="cse"/>
</jsp:include>  
<%@page import="com.chinal.emp.security.AuthUser"  %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%
    AuthUser userDetails = (AuthUser) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
%>
      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
        <!-- Content Header (Page header) -->
      <!--   <section class="content-header">
          <h1>订单管理
          	<small>管理您的订单</small> 
          </h1>
        </section>-->
       <ul class="nav nav-pills">
		  <li role="presentation"  class="active"><a href="${ctx}openCustomerForC.do?id=${customer.id}">详情</a></li>
		  <li role="presentation"><a href="${ctx}openInsuranceForC.do?id=${customer.id}">保单记录</a></li>
		  <li role="presentation"><a href="${ctx}openServiceRecordForC.do?id=${customer.id}">服务记录</a></li>
		  <li role="presentation"><a href="${ctx}openSitRecordForC.do?id=${customer.id}">拜访记录</a></li>
		</ul>
        <!-- Main content -->
        <section class="content">

          <!-- Your Page Content Here -->
          <div class="box box-default">
               <div class="box-body">
                 <!-- form start -->
                 <form id="schFrm" class="form-inline" onsubmit="return false;">
                     客户姓名: ${customer.name}
				</form>
               </div><!-- /.box-body -->
           </div>
           
          <div class="box">
				<div class="box-header">
				     详细信息
					 <div class="btn-group">
			         	<a id="addBtn" class="btn btn-primary">
			            	<i class="fa fa-plus"></i> 录入 
			         	</a>
			          </div>
				</div><!-- /.box-header -->
			
				<div class="box-body">	 
					<table id="searchTable">
						<tr>           
													<!-- <th w_index="idcardnum">身份证号</th> -->
													<th w_index="phone">电话</th>
													<th w_index="mobile">手机</th>
													<th w_index="carBand">车品牌</th>
													<th w_index="carNum">车牌号</th>
													<th w_index="addr">住址</th>
													<th w_index="empname">维护人</th>
													<th w_index="insertDate">维护日期</th>
													<!-- <th w_render="operate" width="10%;">操作</th> -->
						</tr>
					</table>
				</div><!-- /.box-body -->
			</div>
		    
		    <div id="crudWin">
			    	<form id="crudFrm" class="form-horizontal">
					   										   						<%-- <div class="form-group">
	                      <label class="col-sm-3 control-label">身份证号</label>
	                      <div class="col-sm-7">
	                        <input name="idcardnum" type="hidden" class="form-control" value="${customer.idcardnum}">
	                      </div>
	                    </div> --%>
	                    <input name="idcardnum" type="hidden" value="${customer.idcardnum}">
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">电话</label>
	                      <div class="col-sm-7">
	                        <input name="phone" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">手机</label>
	                      <div class="col-sm-7">
	                        <input name="mobile" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">车品牌</label>
	                      <div class="col-sm-7">
	                        <input name="carBand" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">车牌号</label>
	                      <div class="col-sm-7">
	                        <input name="carNum" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">住址</label>
	                      <div class="col-sm-7">
	                        <input name="addr" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<!-- <div class="form-group">
	                      <label class="col-sm-3 control-label">维护人</label>
	                      <div class="col-sm-7">
	                        <input name="empcode" type="text" class="form-control" >
	                      </div>
	                    </div> -->
	                    <input name="empcode" type="hidden" class="form-control" value="<%=userDetails.getCode()%>">
	                    <input name="empname" type="hidden" class="form-control" value="<%=userDetails.getcName()%>">
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">维护日期</label>
	                      <div class="col-sm-7">
	                        <input name="insertDate" type="text" class="form-control" required="true" onfocus="WdatePicker({skin:'default'})">
	                      </div>
	                    </div>
					   										</form>
			    </div>
		    
<script type="text/javascript">     
var that = this;

var pk = 'id'; // java类中的主键字段
var listUrl = ctx + 'listCustomerExtras.do?idcardnum='+${customer.idcardnum}; // 查询
var addUrl = ctx + 'addCustomerExtras.do'; // 添加
var updateUrl = ctx + 'updateCustomerExtras.do'; // 修改
var delUrl = ctx + 'delCustomerExtras.do'; // 删除
var submitUrl = ''; // 提交URL

var gridObj; // 表格
var crudWin; // 窗口
var $schFrm = $('#schFrm'); // 查询表单
var $crudFrm = $('#crudFrm'); // 编辑表单

var $schBtn = $('#schBtn'); // 查询按钮
var $addBtn = $('#addBtn'); // 添加按钮

var validator; // 验证器

function reset() {
	$crudFrm.get(0).reset();
	validator.resetForm();
}


// 初始化事件
$addBtn.click(function() {
	submitUrl = addUrl;
	reset();
	crudWin.title('添加');
	crudWin.showModal();	
});

$schBtn.click(function() {
	search();
});

gridObj = $.fn.bsgrid.init('searchTable', {
	url: listUrl
    ,pageSizeSelect: true
    ,rowHoverColor: true // 移动行变色
    ,rowSelectedColor: false // 选择行不高亮
    ,isProcessLockScreen:false // 加载数据不显示遮罩层
	,displayBlankRows: false
    ,pageSize: 10
});

crudWin = dialog({
	title: '编辑',
	width:400,
	content: document.getElementById('crudWin'),
	okValue: '保存',
	ok: function () {
		that.save();
		return false;
	},
	cancelValue: '取消',
	cancel: function () {
		this.close();
		return false;
	}
});

function search(){
    var schData = getFormData($schFrm);
    gridObj.search(schData);
}

function operate(row, rowIndex, colIndex, options) {
	return '<a href="#" onclick="'
		+ FunUtil.createFun(that, 'edit', row)
		+ ' return false;">修改</a>'
		+ '&nbsp;&nbsp;'
		+ '<a href="#" onclick="'
		+ FunUtil.createFun(that, 'del', row)
		+ ' return false;">删除</a>';
}

// 保存
this.save = function() {
	var self = this;
	var data = getFormData($crudFrm);
	var validateVal = validator.form();
	if(validateVal) {
		Action.post(submitUrl, data, function(result) {
			Action.execResult(result, function(result) {
				gridObj.refreshPage();
				crudWin.close();
			});
		});
	}
}
 // 编辑
this.edit = function(row) {
	if (row) {
		submitUrl = updateUrl + '?' + pk + '=' + row[pk];
		reset();
		crudWin.title('修改');
		loadFormData($crudFrm,row);		
		crudWin.showModal();
	}
}

// 删除
this.del = function(row) {
	if (row) {
		var d = dialog({
			title: '提示',
			width: 200,
			content: '确定要删除该记录吗?',
			okValue: '确定',
			ok: function () {
				Action.post(delUrl, row, function(result) {
					Action.execResult(result, function(result) {
						gridObj.refreshPage();
					});
				});
			},
			cancelValue: '取消',
			cancel: function () {}
		});
		d.showModal();
	}
}

validator = $crudFrm.validate();
</script>

        </section><!-- /.content -->
      </div><!-- /.content-wrapper -->

      <!-- Main Footer -->
      <footer class="main-footer">
        <!-- Default to the left -->
        <strong>Copyright &copy; 2016</strong>
      </footer>

    </div><!-- ./wrapper -->

    <!-- REQUIRED JS SCRIPTS -->
    <!-- Bootstrap 3.3.5 -->
    <script src="${AdminLTE}bootstrap/js/bootstrap.min.js"></script>
    <!-- AdminLTE App -->
    <script src="${AdminLTE}dist/js/app.min.js"></script>

    <!-- Optionally, you can add Slimscroll and FastClick plugins.
         Both of these plugins are recommended to enhance the
         user experience. Slimscroll is required when using the
         fixed layout. -->
  </body>
</html>
