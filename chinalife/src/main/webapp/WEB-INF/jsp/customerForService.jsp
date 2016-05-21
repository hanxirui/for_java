<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp" %>
<jsp:include page="../menu.jsp" >
    <jsp:param name="activeMenu" value="customer"/>
</jsp:include>  

      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
        <!-- Content Header (Page header) -->
      <!--   <section class="content-header">
          <h1>订单管理
          	<small>管理您的订单</small> 
          </h1>
        </section>-->

        <!-- Main content -->
        <section class="content">

          <!-- Your Page Content Here -->
          <div class="box box-default">
               <div class="box-body">
                 <!-- form start -->
                 <form id="schFrm" class="form-inline" onsubmit="return false;">
				      姓名:<input name="name" type="text" class="form-control">      
                   	<button id="schBtn" type="submit" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
					<button type="reset" class="btn btn-default"><i class="fa fa-remove"></i> 清空</button>
				</form>
               </div><!-- /.box-body -->
           </div>
           
          <div class="box">
				<div class="box-header">
					 <div class="btn-group">
			         	<a id="addBtn" class="btn btn-primary">
			            	<i class="fa"></i> 批量维护服务记录 
			         	</a>
			          </div>
			          
				</div><!-- /.box-header -->
			
				<div class="box-body">	 
					<table id="searchTable">
						<tr>           
						    <th w_check="true" w_index="idcardnum" width="3%;"></th> 
							<th w_index="name">客户姓名</th>
							<th w_index="sex"  w_render="sexRender">性别</th>
							<th w_index="idcardnum">身份证号</th>
							<th w_index="addr">地址</th>
							<!-- <th w_index="type">类型  1-原始；2-自营新拓；3-渠道新拓</th> -->
							<!-- <th w_index="birthday">生日</th> -->
							<!-- <th w_index="weddingDay">结婚纪念日</th> -->
							
							<th w_index="type"  w_render="fromRender">性质</th>
							<!-- <th w_index="note">爱好及特点</th> -->
							<th w_render="operate" width="10%;">操作</th>
						</tr>
					</table>
				</div><!-- /.box-body -->
			</div>
		    
		    <div id="crudWin">
			    	<form id="crudFrm" class="form-horizontal">
											   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">姓名</label>
	                      <div class="col-sm-7">
	                        <input name="name" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   	
					   	<div class="form-group">
	                      <label class="col-sm-3 control-label">身份证号</label>
	                      <div class="col-sm-7">
	                        <input name="idcardnum" type="text" class="form-control" required="true">
	                      </div>
	                       </div>
	                       
	                      <div class="form-group">
	                      <label class="col-sm-3 control-label">地址</label>
	                      <div class="col-sm-7">
	                        <input name="addr" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
	                   
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">性质</label>
	                      <div class="col-sm-7">
	                        <select id="type" name="type"  class="form-control" required="true">
	                          <option value="1">发放</option>
	                          <option value="2">新拓</option>
	                        </select>
	                      </div>
	                    </div>
	                     <div class="form-group">
	                      <label class="col-sm-3 control-label">新拓方式</label>
	                      <div class="col-sm-7">
	                        <select id="laiyuan" name="type"  class="form-control" required="true">
	                          <option value="2">自营新拓</option>
	                          <option value="3">渠道新拓</option>
	                        </select>
	                      </div>
	                       </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">生日</label>
	                      <div class="col-sm-7">
	                        <input name="birthday" type="text" class="form-control" required="true"  onfocus="WdatePicker({skin:'default'})">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">结婚纪念日</label>
	                      <div class="col-sm-7">
	                        <input name="weddingDay" type="text" class="form-control" required="true" onfocus="WdatePicker({skin:'default'})">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">客户经理</label>
	                      <div class="col-sm-7">
	                        <input name="empcode" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">性别</label>
	                      <div class="col-sm-7">
	                             <label class="radio-inline">
								    <input type="radio" name="sex" id="sex0" value="0">女
								  </label>
								  <label class="radio-inline">
								    <input type="radio" name="sex" id="sex1" value="1">男
								  </label>
	                      </div>
	                    </div>
					   										   						
	                   
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">爱好及特点</label>
	                      <div class="col-sm-7">
	                        <input name="note" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										</form>
			    </div>
		    
<script type="text/javascript">     
var that = this;

var pk = 'id'; // java类中的主键字段
var listUrl = ctx + 'listCustomerForService.do'; // 查询
var addUrl = ctx + 'addCustomerForService.do'; // 添加
var updateUrl = ctx + 'updateCustomerForService.do'; // 修改
var delUrl = ctx + 'delCustomerForService.do'; // 删除
var detailUrl = ctx + 'openCustomerForC.do'; // 修改
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
		/* + '<a href="#" onclick="'
		+ FunUtil.createFun(that, 'del', row)
		+ ' return false;">删除</a>'
		+ '&nbsp;&nbsp;' */
		+ '<a href="'+detailUrl + '?' + pk + '=' + row[pk]+'">详情</a>';
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
 
/* this.detail = function(row) {
	if (row) {
		submitUrl = detailUrl + '?' + pk + '=' + row[pk];
		reset();
		crudWin.title('详情');
		loadFormData($crudFrm,row);		
		crudWin.showModal();
	}
} */

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

var sexRender = function(record, rowIndex, colIndex, options){
	if(record.sex==0){
		return "女";
	}else{
		return "男";
	}
	
}

var fromRender = function(record, rowIndex, colIndex, options){
	if(record.laiyuan==1){
		return "原始";
	}else if(record.laiyuan==2){
		return "自营新拓";
	}else{
		return "渠道新拓";
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