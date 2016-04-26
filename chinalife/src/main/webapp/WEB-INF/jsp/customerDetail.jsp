<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp" %>
<jsp:include page="../menu.jsp" >
    <jsp:param name="activeMenu" value="cse"/>
</jsp:include>  

      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
        <!-- Content Header (Page header) -->
      <!--   <section class="content-header">
          <h1>订单管理
          	<small>管理您的订单</small> 
          </h1>
        </section>-->
		<ul class="nav nav-pills">
		  <li role="presentation"  class="active"><a href="${ctx}openCustomerForC.do">详情</a></li>
		  <li role="presentation"><a href="${ctx}openInsuranceForC.do">保单记录</a></li>
		  <li role="presentation"><a href="${ctx}openServiceRecordForC.do">服务记录</a></li>
		  <li role="presentation"><a href="${ctx}openSitRecordForC.do">拜访记录</a></li>
		</ul>
        <!-- Main content -->
        <section class="content">

          <!-- Your Page Content Here -->
          <div class="box box-default">
               <div class="box-body">
                 <!-- form start -->
                 <form id="schFrm" class="form-inline" onsubmit="return false;">
                     客户姓名: 张三
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
														<th w_index="id"></th>
																			<th w_index="idcardnum">身份证号</th>
																			<th w_index="phone">电话</th>
																			<th w_index="mobile">手机</th>
																			<th w_index="carBand">车品牌</th>
																			<th w_index="carNum">车牌号</th>
																			<th w_index="addr">住址</th>
																			<th w_index="account">维护人</th>
																			<th w_index="insertDate">维护日期</th>
													<th w_render="operate" width="10%;">操作</th>
						</tr>
					</table>
				</div><!-- /.box-body -->
			</div>
			
			 <div class="box">
				<div class="box-header">
				     拜访记录
					 <div class="btn-group">
			         	<a id="addBtn" class="btn btn-primary">
			            	<i class="fa fa-plus"></i> 录入 
			         	</a>
			          </div>
				</div><!-- /.box-header -->
			
				<div class="box-body">	 
					<table id="searchTable2">
						<tr>           
							<th w_index="account">客户经理</th>
							<th w_index="visittime">拜访时间</th>
							<th w_index="idcardnum">客户</th>
							<th w_index="content">拜访内容</th>
							<th w_render="operate" width="10%;">操作</th>
						</tr>
					</table>
				</div><!-- /.box-body -->
			</div>
			
			 <div class="box">
				<div class="box-header">
				     服务记录
					 <div class="btn-group">
			         	<a id="addBtn" class="btn btn-primary">
			            	<i class="fa fa-plus"></i> 录入 
			         	</a>
			          </div>
				</div><!-- /.box-header -->
			
				<div class="box-body">	 
					<table id="searchTable3">
						<tr>           
							<th w_index="idcardnum">客户</th>
							<th w_index="servicetime">服务时间</th>
							<th w_index="content">服务内容</th>
							<th w_index="account">客户经理</th>
							<th w_render="operate" width="10%;">操作</th>
						</tr>
					</table>
				</div><!-- /.box-body -->
			</div>
		    
		    <div id="crudWin">
			    	<form id="crudFrm" class="form-horizontal">
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">身份证号</label>
	                      <div class="col-sm-7">
	                        <input name="idcardnum" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
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
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">维护人</label>
	                      <div class="col-sm-7">
	                        <input name="account" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">维护日期</label>
	                      <div class="col-sm-7">
	                        <input name="insertDate" type="text" class="form-control" required="true" onfocus="WdatePicker({skin:'default'})">
	                      </div>
	                    </div>
					   										</form>
			    </div>
		    
<script type="text/javascript">     

var pk = 'id'; // java类中的主键字段

var customerlistUrl = ctx + 'listCustomerExtras.do'; // 查询
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
	url: customerlistUrl
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
<!-- 2 -->
<script type="text/javascript">     

var pk = 'id'; // java类中的主键字段
var visitlistUrl = ctx + 'listSitRecord.do'; // 查询
var visitaddUrl = ctx + 'addSitRecord.do'; // 添加
var visitupdateUrl = ctx + 'updateSitRecord.do'; // 修改
var visitdelUrl = ctx + 'delSitRecord.do'; // 删除
var visitsubmitUrl = ''; // 提交URL

var visitgridObj; // 表格
var visitcrudWin; // 窗口
var $visitschFrm = $('#visitschFrm'); // 查询表单
var $visitcrudFrm = $('#visitcrudFrm'); // 编辑表单

var $visitschBtn = $('#visitschBtn'); // 查询按钮
var $visitaddBtn = $('#visitaddBtn'); // 添加按钮

var visitvalidator; // 验证器

function visitreset() {
	$visitcrudFrm.get(0).reset();
	visitvalidator.resetForm();
}


// 初始化事件
$visitaddBtn.click(function() {
	visitsubmitUrl = visitaddUrl;
	reset();
	visitcrudWin.title('添加');
	visitcrudWin.showModal();	
});

$visitschBtn.click(function() {
	visitsearch();
});



var visitgridObj = $.fn.bsgrid.init('searchTable2', {
	url: visitlistUrl
    ,pageSizeSelect: true
    ,rowHoverColor: true // 移动行变色
    ,rowSelectedColor: false // 选择行不高亮
    ,isProcessLockScreen:false // 加载数据不显示遮罩层
	,displayBlankRows: false
    ,pageSize: 10
});


visitcrudWin = dialog({
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

function visitsearch(){
    var schData = getFormData($schFrm);
    visitgridObj.search(schData);
}

function visitoperate(row, rowIndex, colIndex, options) {
	return '<a href="#" onclick="'
		+ FunUtil.createFun(that, 'visitedit', row)
		+ ' return false;">修改</a>'
		+ '&nbsp;&nbsp;'
		+ '<a href="#" onclick="'
		+ FunUtil.createFun(that, 'visitdel', row)
		+ ' return false;">删除</a>';
}

// 保存
visitsave = function() {
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
visitedit = function(row) {
	if (row) {
		submitUrl = updateUrl + '?' + pk + '=' + row[pk];
		reset();
		crudWin.title('修改');
		loadFormData($crudFrm,row);		
		crudWin.showModal();
	}
}

// 删除
visitdel = function(row) {
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

visitvalidator = $visitcrudFrm.validate();
</script>

<!-- //3 -->
<script type="text/javascript">     

var pk = 'id'; // java类中的主键字段
var servicelistUrl = ctx + 'listServiceRecord.do'; // 查询
var serviceaddUrl = ctx + 'addServiceRecord.do'; // 添加
var serviceupdateUrl = ctx + 'updateServiceRecord.do'; // 修改
var servicedelUrl = ctx + 'delServiceRecord.do'; // 删除
var servicesubmitUrl = ''; // 提交URL

var servicegridObj; // 表格
var servicecrudWin; // 窗口
var $serviceschFrm = $('#serviceschFrm'); // 查询表单
var $servicecrudFrm = $('#servicecrudFrm'); // 编辑表单

var $serviceschBtn = $('#serviceschBtn'); // 查询按钮
var $serviceaddBtn = $('#serviceaddBtn'); // 添加按钮

var servicevalidator; // 验证器

function servicereset() {
	$servicecrudFrm.get(0).reset();
	servicevalidator.resetForm();
}


// 初始化事件
$serviceaddBtn.click(function() {
	submitUrl = serviceaddUrl;
	reset();
	crudWin.title('添加');
	crudWin.showModal();	
});

$serviceschBtn.click(function() {
	search();
});


var servicegridObj = $.fn.bsgrid.init('searchTable3', {
	url: servicelistUrl
    ,pageSizeSelect: true
    ,rowHoverColor: true // 移动行变色
    ,rowSelectedColor: false // 选择行不高亮
    ,isProcessLockScreen:false // 加载数据不显示遮罩层
	,displayBlankRows: false
    ,pageSize: 10
});

servicecrudWin = dialog({
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

function servicesearch(){
    var schData = getFormData($schFrm);
    servicegridObj.search(schData);
}

function serviceoperate(row, rowIndex, colIndex, options) {
	return '<a href="#" onclick="'
		+ FunUtil.createFun(that, 'serviceedit', row)
		+ ' return false;">修改</a>'
		+ '&nbsp;&nbsp;'
		+ '<a href="#" onclick="'
		+ FunUtil.createFun(that, 'servicedel', row)
		+ ' return false;">删除</a>';
}

// 保存
servicesave = function() {
	var self = this;
	var data = getFormData($crudFrm);
	var validateVal = servicevalidator.form();
	if(validateVal) {
		Action.post(servicesubmitUrl, data, function(result) {
			Action.execResult(result, function(result) {
				servicegridObj.refreshPage();
				servicecrudWin.close();
			});
		});
	}
}
 // 编辑
serviceedit = function(row) {
	if (row) {
		servicesubmiservicetUrl = serviceupdateUrl + '?' + pk + '=' + row[pk];
		reset();
		servicecrudWin.title('修改');
		loadFormData($crudFrm,row);		
		servicecrudWin.showModal();
	}
}

// 删除
servicedel = function(row) {
	if (row) {
		var d = dialog({
			title: '提示',
			width: 200,
			content: '确定要删除该记录吗?',
			okValue: '确定',
			ok: function () {
				Action.post(servicedelUrl, row, function(result) {
					Action.execResult(result, function(result) {
						servicegridObj.refreshPage();
					});
				});
			},
			cancelValue: '取消',
			cancel: function () {}
		});
		d.showModal();
	}
}

servicevalidator = $servicecrudFrm.validate();
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
