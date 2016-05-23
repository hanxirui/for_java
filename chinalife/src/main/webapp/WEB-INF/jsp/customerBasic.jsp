<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp" %>
<jsp:include page="../menu.jsp" >
    <jsp:param name="activeMenu" value="customer"/>
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

        <!-- Main content -->
        <section class="content">

          <!-- Your Page Content Here -->
          <div class="box box-default">
               <div class="box-body">
                 <!-- form start -->
                 <form id="schFrm" class="form-inline" onsubmit="return false;">
				      姓名:<input name="name" type="text" class="form-control">      
                      <!-- 身份证号:<input name="idcardnum" type="text" class="form-control">      
                      类型  1-原始；2-自营新拓；3-渠道新拓:<input name="type" type="text" class="form-control">    -->   
                      <!-- 生日:<input name="birthday" type="text" class="form-control">  -->     
                     <!--  结婚纪念日:<input name="weddingDay" type="text" class="form-control">   -->    
                      客户经理:<input name="kehujingli" type="text" class="form-control">      
                     <!--  性别:<input name="sex" type="text" class="form-control">      
                      初始来源:<input name="laiyuan" type="text" class="form-control">  -->     
                     <!--  爱好:<input name="note" type="text" class="form-control">     
                      投保日期:<input name="note" type="text" class="form-control"> ---    <input name="note" type="text" class="form-control">
                      投保银行:<input name="note" type="text" class="form-control">     
                      保单金额:<input name="note" type="text" class="form-control">     
                      爱好:<input name="note" type="text" class="form-control">   -->    
                   	<button id="schBtn" type="submit" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
					<button type="reset" class="btn btn-default"><i class="fa fa-remove"></i> 清空</button>
				</form>
               </div><!-- /.box-body -->
           </div>
           
          <div class="box">
				<div class="box-header">
					 <div class="btn-group">
			         	<a id="addBtn" class="btn btn-primary">
			            	<i class="fa"></i> 录入 
			         	</a>
			          </div>
			          
			          <%if(userDetails.getLevel()>=3){ %>
			          <div class="btn-group"><!-- 按钮需要权限控制，3级及以上 -->
			         	<a id="fenpeiBtn" class="btn btn-primary">
			            	<i class="fa"></i> 分配 
			         	</a>
			          </div>
			          <%} %>
			          <div class="btn-group"><!-- 按钮需要权限控制，普通业务员查询自己的-->
			         	<a id="apiBtn" class="btn btn-primary">
			            	<i class="fa"></i> KPI统计查询 
			         	</a>
			          </div>
			          
			          <!--  <div class="btn-group">
			         	<a id="importBtn" class="btn btn-primary">
                           <i class="fa"></i>  导入用户
                        </a>
			          </div> -->
				</div><!-- /.box-header -->
			
				<div class="box-body">	 
					<table id="searchTable">
						<tr>     
						    <th w_check="true" w_index="idcardnum" width="3%;"   ></th>      
							<th w_index="name">客户姓名</th>
							<th w_index="sex"  w_render="sexRender">性别</th>
							<th w_index="idcardnum" >身份证号</th>
							<th w_index="addr">地址</th>
							<!-- <th w_index="type">类型  1-原始；2-自营新拓；3-渠道新拓</th> -->
							<!-- <th w_index="birthday">生日</th> -->
							<!-- <th w_index="weddingDay">结婚纪念日</th> -->
							<!-- <th w_index="kehujingli">客户经理</th> -->
							
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
	                      <label class="col-sm-2 control-label">姓名</label>
	                      <div class="col-sm-3">
	                        <input name="name" type="text" class="form-control" required="true">
	                      </div>
	                   
	                      <label class="col-sm-2 control-label">身份证号</label>
	                      <div class="col-sm-3">
	                        <input name="idcardnum" type="text" class="form-control" required="true">
	                      </div>
	                       </div>
	                       
	                      <div class="form-group">
	                      <label class="col-sm-2 control-label">地址</label>
	                      <div class="col-sm-3">
	                        <input name="addr" type="text" class="form-control" required="true">
	                      </div>
	                   
	                      <label class="col-sm-2 control-label">性质</label>
	                      <div class="col-sm-3">
	                        <select id="type" name="type"  class="form-control" required="true">
	                          <option value="1">发放</option>
	                          <option value="2">新拓</option>
	                        </select>
	                      </div>
	                    </div>
	                     <div class="form-group">
	                      <label class="col-sm-2 control-label">新拓方式</label>
	                      <div class="col-sm-3">
	                        <select id="laiyuan" name="laiyuan"  class="form-control" required="true" disabled>
	                          <option value="2">自营新拓</option>
	                          <option value="3">渠道新拓</option>
	                        </select>
	                      </div>
	                       
	                      <label class="col-sm-2 control-label">生日</label>
	                      <div class="col-sm-3">
	                        <input name="birthday" type="text" class="form-control" required="true"  onfocus="WdatePicker({skin:'default'})">
	                      </div>
	                    </div>
					   <div class="form-group">
	                      <label class="col-sm-2 control-label">结婚纪念日</label>
	                      <div class="col-sm-3">
	                        <input name="weddingDay" type="text" class="form-control" required="true" onfocus="WdatePicker({skin:'default'})">
	                      </div>
	                    
	                      <label class="col-sm-2 control-label">客户经理</label>
	                      <div class="col-sm-3">
	                        <input id="kehujingli" name="kehujingli" type="hidden" class="form-control" required="true">
	                        <input id="jinglimingcheng" name="jinglimingcheng" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   <div class="form-group">
	                      <label class="col-sm-2 control-label">性别</label>
	                      <div class="col-sm-3">
	                             <label class="radio-inline">
								    <input type="radio" name="sex" id="sex0" value="0">女
								  </label>
								  <label class="radio-inline">
								    <input type="radio" name="sex" id="sex1" value="1">男
								  </label>
	                      </div>
	                   
	                      <label class="col-sm-2 control-label">爱好及特点</label>
	                      <div class="col-sm-3">
	                        <input name="note" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										</form>
			    </div>
			    
		     <div id="importWin">
                    <form id="importFrm"  method="post"   enctype="multipart/form-data"  class="form-horizontal" action="${ctx}importCustomer.do">                   
                       <div class="form-group">
                          <label class="col-sm-2 control-label">选择文件</label>
                          <div class="col-sm-3">
                           <input class="btn btn-default" id="filename" type="file" name="filename"  accept="xls"/>
                          </div>
                        </div>
                    </form>
             </div>
               
             <div class="box-body" id="cusWin">	 
					<table id="cusTable">
						<tr>          
						    <th w_check="true" w_index="code" width="3%;"></th> 
						    <th w_index="name">姓名</th>
							<th w_index="role" w_render="roleRender">职务</th>
							<th w_index="code">工号</th>
							<th w_index="orgname">所属公司</th>
							<th w_index="orgcode">公司代码</th>
						</tr>
					</table>
			</div>
				
			<div id="kpiWin">
			
			         <div class="container-fluid">
					  <div class="row">
					     <div class="col-md-10">
	                        时段：<input name="startTime" type="text"  onfocus="WdatePicker({skin:'default'})">-
	                        <input name="endTime" type="text"  onfocus="WdatePicker({skin:'default'})">
	                      </div>
	                    
					  </div> 
					  <div class="row">
					     <div class="col-md-3">
	                       拜访量： 15人次
	                      </div>
					  </div>
					   <div class="row">
					     <div class="col-md-5">
	                       日均拜访量： 5人次
	                      </div>
	                   
	                      <div class="col-md-5">
	                        拜访深度： 3
	                      </div>
					  </div>
					   <div class="row">
					     <div class="col-md-5">
	                        新开自营件数：512件
	                      </div>
	                       
	                      <div class="col-md-5">
	                        保费： 1024
	                      </div>
					  </div>
					   <div class="row">
					     <div class="col-md-5">
	                      新开渠道件数：256
	                      </div>
	                    
	                      <div class="col-md-5">
	                        保费：1024
	                      </div>
					  </div>
					</div>
			    </div>	
				
			<input type="hidden" id="winFrom">
               
<script type="text/javascript">     
var that = this;

var pk = 'id'; // java类中的主键字段
var listUrl = ctx + 'listCustomerBasic.do'; // 查询
var addUrl = ctx + 'addCustomerBasic.do'; // 添加
var updateUrl = ctx + 'updateCustomerBasic.do'; // 修改
var delUrl = ctx + 'delCustomerBasic.do'; // 删除
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
	width:600,
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
	},
	onshow:function(){
		if($("#kehujingli").val()){
			$.getJSON("${ctx}listEmployeeForCus.do", {"kehujingli":$("#kehujingli").val()}, function (result) {
				var emp = result.data[0];
				$("#jinglimingcheng").val((emp.name));
			});
		}
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
	if(record.laiyuan==2){
		return "自营新拓";
	}else if(record.laiyuan==3){
		return "渠道新拓";
	}else{
		return "发放";
	}
}

var $importBtn= $('#importBtn'); // 导入按钮
$importBtn.click(function() {   
	    importWin.showModal();    
});

var importWin = dialog({
	title: '导入',
	width:400,
	content: document.getElementById('importWin'),
	okValue: '导入',
    ok: function () {
           $.ajaxFileUpload({
               url:ctx+"importCustomer.do",
               fileElementId:"filename",
               dataType: 'json',
               success: function (data, status){
                 if("success"==data.status){
                     gridObj.refreshPage();
                     importWin.close();
                 }else if("error"==data.status){
                     alert("上传失败!");
                    return false; 
                 }
               }
               });
        return false;
    },
	cancelValue: '取消',
	cancel: function () {
		this.close();
		return false;
	}
});


$("#fenpeiBtn").click(function(){
	var cardnums = gridObj.getCheckedValues('idcardnum');
	if(cardnums.length<1){
		alert("请至少选择一个客户.");
		return false;
	}
	$("#winFrom").val("fenpei");
	cusWin.showModal();
})


var roleList;
$.getJSON("${ctx}listAllRole.do", null, function (result) {
	roleList = result.data;
});

$("#jinglimingcheng").click(function() {
	$("#winFrom").val("jinglimingcheng");
	cusWin.showModal();
});

var cusGridObj = $.fn.bsgrid.init('cusTable', {
	url: ctx + 'listEmployeeForCus.do'
    ,pageSizeSelect: true
    ,rowHoverColor: true // 移动行变色
    ,rowSelectedColor: false // 选择行不高亮
    ,isProcessLockScreen:false // 加载数据不显示遮罩层
	,displayBlankRows: false
	,pagingLittleToolbar: true
    ,pageSize: 10
});

var cusWin = dialog({
	title: '选择客户经理',
	width:800,
	content: document.getElementById('cusWin'),
	okValue: '保存',
	ok: function () {
		
		var name = cusGridObj.getCheckedValues('name');
		if(name.length!=1){
			alert("请选择一个客户经理.");
			return false;
		}
		
		if($("#winFrom").val()=="jinglimingcheng"){
			$('#kehujingli').val(cusGridObj.getCheckedValues('code'));
			$('#jinglimingcheng').val(cusGridObj.getCheckedValues('name'));
		}else{
			
			var cardnums = gridObj.getCheckedValues('idcardnum');
			if(cardnums.length<1){
				alert("请至少选择一个客户.");
				return false;
			}
			
			var _cardnums=new Array()
			
			$.each(cardnums, function(i, n){
				_cardnums[i]="'"+n+"'";
				});
			
			 Action.post(ctx + 'fenpeiCustomer.do?cusIds='+_cardnums+"&empId="+cusGridObj.getCheckedValues('code'), null, function(result) {
				Action.execResult(result, function(result) {
					gridObj.refreshPage();
					
				});
			}); 
	    }
		this.close();
		return false;
	},
	cancelValue: '取消',
	cancel: function () {
		this.close();
		return false;
	},
	onshow: function () {
		$("#cusTable_pt_outTab").width($("#cusTable").width());
    }
});
var roleRender = function(record, rowIndex, colIndex, options){
	var roleName;
	$.each(roleList, function (i, item) {
	       if(item.id==record.role){
	    	   roleName = item.name;
	       }
	});
	return roleName;
}
$("#type").change(function(){
	if($("#type").val()==1){
		$("#laiyuan").attr("disabled",true);
	}else{
		$("#laiyuan").attr("disabled",false);
	}
});

$("#apiBtn").click(function(){
	kpiWin.showModal();
});

var kpiWin = dialog({
	title: '编辑',
	width:600,
	content: document.getElementById('kpiWin'),
	okValue: '保存',
	ok: function () {
		that.save();
		return false;
	},
	cancelValue: '取消',
	cancel: function () {
		this.close();
		return false;
	},
	onshow:function(){
		alert("show");
	}
});
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

