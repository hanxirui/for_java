<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp" %>
<jsp:include page="../menu.jsp" >
    <jsp:param name="activeMenu" value="employee"/>
</jsp:include>  

      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <!-- <section class="content-header">
          <h5>订单管理
          </h5>
        </section> -->

        <!-- Main content -->
        <section class="content">

          <!-- Your Page Content Here -->
          <div class="box box-default">
               <div class="box-body">
                 <!-- form start -->
                 <form id="schFrm" class="form-inline" onsubmit="return false;">
			        姓名:<input name="name" type="text" class="form-control">      
                     <!-- 职务:<input name="role" type="text" class="form-control">      
                     密码:<input name="password" type="text" class="form-control">     -->  
                     工号:<input name="code" type="text" class="form-control">      
                    <!--  身份证号:<input name="pid" type="text" class="form-control">   -->    
                     所属公司:<input name="orgname" type="text" class="form-control">      
                    <!--  公司代码:<input name="orgcode" type="text" class="form-control">    -->   
                   <!--   性别:<input name="sex" type="text" class="form-control">     -->  
                     电话:<input name="phone" type="text" class="form-control">      
                     <!-- 入司时间:<input name="jointime" type="text" class="form-control">   -->    
                   	<button id="schBtn" type="submit" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
					<button type="reset" class="btn btn-default"><i class="fa fa-remove"></i> 清空</button>
				</form>
               </div><!-- /.box-body -->
           </div>
           
          <div class="box">
				<div class="box-header">
					 <div class="btn-group">
			         	<a id="addBtn" class="btn btn-primary">
			            	<i class="fa fa-plus"></i> 录入 
			         	</a>
			          </div>
				</div><!-- /.box-header -->
			
				<div class="box-body">	 
					<table id="searchTable">
						<tr>           
							<th w_index="name">姓名</th>
							<th w_index="role" w_render="roleRender">职务</th>
							<!-- <th w_index="password">密码</th> -->
							<th w_index="code">工号</th>
							<th w_index="idcardnum">身份证号</th>
							<th w_index="orgname">所属公司</th>
							<th w_index="orgcode">公司代码</th>
							<th w_index="sex" w_render="sexRender">性别</th>
							<th w_index="phone">电话</th>
							<th w_index="jointime">入司时间</th>
                            <th w_index="managerName">直接上级</th>
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
	                    
	                      <label class="col-sm-2 control-label">职务</label>
	                      <div class="col-sm-3">
	                        <select id="role" name="role"  class="form-control" required="true">
	                        </select>
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
	                       <label class="col-sm-2 control-label">电话</label>
	                      <div class="col-sm-3">
	                        <input name="phone" type="text" class="form-control" required="true">
	                      </div>
	                       
	                     
	                    </div>
                         <div class="form-group">
	                      <label class="col-sm-2 control-label">工号</label>
	                      <div class="col-sm-3">
	                        <input name="code" type="text" class="form-control" required="true">
	                      </div>
	                    
	                      <label class="col-sm-2 control-label">身份证号</label>
	                      <div class="col-sm-3">
	                        <input name="idcardnum" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
                         <div class="form-group">
	                      <label class="col-sm-2 control-label">所属公司</label>
	                      <div class="col-sm-3">
	                        <select id="orgname" name="orgname"  class="form-control" required="true">
	                        </select>
	                      </div>
	                     
	                      <label class="col-sm-2 control-label">公司代码</label>
	                      <div class="col-sm-3">
	                       <select id="orgcode" name="orgcode"  class="form-control" required="true" readonly>
	                        </select>
	                      </div>
	                        
	                    </div>
                      
	                    
                          <div class="form-group">
	                     <label class="col-sm-2 control-label">直接上级</label>
	                      <div class="col-sm-3">
	                        <select id="managercode" name="managercode"  class="form-control">
	                        </select>
	                      </div>
	                   
	                      <label class="col-sm-2 control-label">入司时间</label>
	                      <div class="col-sm-3">
	                        <input name="jointime" type="text" class="form-control" required="true"  onfocus="WdatePicker({skin:'default'})">
	                      </div>
	                    </div>
	                   <!--  <div class="form-group">
			    		 
	                     </div> -->
					  </form>
			    </div>
		    
<script type="text/javascript">     
var that = this;

var pk = 'id'; // java类中的主键字段
var listUrl = ctx + 'listEmployee.do'; // 查询
var addUrl = ctx + 'addEmployee.do'; // 添加
var updateUrl = ctx + 'updateEmployee.do'; // 修改
var delUrl = ctx + 'delEmployee.do'; // 删除
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
	//getManagerList();
	getRole();
	getOrgList();
	crudWin.showModal();	
});

$schBtn.click(function() {
	search();
});



gridObj = $.fn.bsgrid.init('searchTable', {
	url: listUrl
    ,pageSizeSelect: false //是否显示分页大小选择下拉框, 配合参数pageSizeForGrid, 默认值false
    ,rowHoverColor: true // 移动行变色
    ,rowSelectedColor: false // 选择行不高亮
    ,isProcessLockScreen:false // 加载数据不显示遮罩层
	,displayBlankRows: false
	,stripeRows:false// 隔行变色, 默认值false, 对应样式是: grid.css -> .bsgrid tr.even_index_row td
	,pagingLittleToolbar:false// 是否显示精简的图标按钮分页工具条, 默认值false
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
		+ ' return false;">删除</a>'
		+ '&nbsp;&nbsp;'
		+ '<a href="#" onclick="'
		+ FunUtil.createFun(that, 'resetPwd', row)
		+ ' return false;">重置密码</a>';
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
		getRole();
		//getManagerList();
		getOrgList();
		loadFormData($crudFrm,row);	
		
		crudWin.showModal();
		/* $("#managercode").val(row.managercode);
		$("#orgcode").val(row.orgcode); */
	}
}
 
// 重置密码
this.resetPwd = function(row) {
	if (row) {

		var d = dialog({
			title: '提示',
			width: 200,
			content: '确定要重置密码吗?',
			okValue: '确定',
			ok: function () {
				Action.post(ctx + "resetPassword.do", row, function(result) {
					Action.execResult(result, function(result) {
						var di = dialog({
							title: '提示',
							width: 200,
							content: '密码重置成功',
							okValue: '确定',
							ok: function () {
								
							}
						});
						di.showModal();
					});
				});
			},
			cancelValue: '取消',
			cancel: function () {}
		});
		d.showModal();
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



var sexRender = function(record, rowIndex, colIndex, options){
	if(record.sex==0){
		return "女";
	}else{
		return "男";
	}
	
}

var roleRender = function(record, rowIndex, colIndex, options){
	var roleName;
	$.each(roleList, function (i, item) {
	       if(item.id==record.role){
	    	   roleName = item.name;
	       }
	});
	return roleName;
}

var roleList;
$.getJSON("${ctx}listAllRole.do", null, function (result) {
	roleList = result.data;
});
//获得职务列表
this.getRole = function(){
	 $("#role").empty();
	 
	 $.each(roleList, function (i, item) {
	       $("<option></option>").val(item.id).text(item.name).appendTo($("#role"));
	    });
}



var orgList;
$.getJSON("${ctx}listOrg.do", null, function (result) {
	orgList = result.data;
});
this.getOrgList = function (){
	  $("#orgname").empty();
	   $.each(orgList, function (i, item) {
			   $("<option></option>").val(item.name).text(item.name).appendTo($("#orgname"));
	    });
	   
	   $("#orgcode").empty();
	   $.each(orgList, function (i, item) {
		   $("<option></option>").val(item.code).text(item.code).appendTo($("#orgcode"));
    });
	   
	   getManagerList();
}

$("#orgname").on("change",setOrgCode);

function setOrgCode(){
	var orgname = $("#orgname").val();
	$.each(orgList, function (i, item) {
		   if(item.name==orgname){
			   $("#orgcode").val(item.code);
		   }
		  
   });
	
	getManagerList();
}

var getManagerList = function (){
	  $("#managercode").empty();
	  
	  var roleId = $("#role").val();
	  var orgcode =  $("#orgcode").val();
	  
	  $("<option></option>").val(0).text("请选择").appendTo($("#managercode"));
	  var empList;
	  $.getJSON("${ctx}getAllManagers.do", {"roleId":roleId,"orgcode":orgcode}, function (result) {
	  	empList = result.data;
	  	if(empList){
			   $.each(empList, function (i, item) {
					   $("<option></option>").val(item.code).text(item.name).appendTo($("#managercode"));
			    });
	       }
	  });
	  
	  
}

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
