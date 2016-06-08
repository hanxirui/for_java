<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp"%>
<jsp:include page="../menu.jsp">
	<jsp:param name="activeMenu" value="bizplat" />
</jsp:include>
<%@page import="com.chinal.emp.security.AuthUser"%>
<%@ page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%
	AuthUser userDetails = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
%>
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">

	<!-- Main content -->
	<section class="content">

		<!-- Your Page Content Here -->
		<div class="box box-default">
			<div class="box-body">
				<!-- form start -->
				<form id="schFrm" class="form-inline" onsubmit="return false;">
					开始时间:<input name="start" type="text" class="form-control">
					结束时间:<input name="end" type="text" class="form-control">
					<button id="schBtn" type="submit" class="btn btn-primary">
						<i class="fa fa-search"></i> 查询
					</button>
					<button type="reset" class="btn btn-default">
						<i class="fa fa-remove"></i> 清空
					</button>
				</form>
			</div>
			<!-- /.box-body -->
		</div>

		<div class="box">
			<div class="box-header">
				<div class="btn-group">
					<a id="addBtn" class="btn btn-primary"> <i class="fa"></i>
						录入
					</a>
				</div>
				<div class="btn-group">
					 <a href="${ctx}openBizplatform.do"><i class="fa fa-reply"></i>返回</a>
				</div>
			</div>
			<!-- /.box-header -->

			<div class="box-body">
				<table id="searchTable">
					<tr>
						<th w_index="title">平台名称</th>
						<th w_index="zhishibaifang" w_render="zhishiRender">制式拜访</th>
						<th w_index="start">开始时间</th>
						<th w_index="end">结束时间</th>
						<th w_index="caiye" w_render="uploadRender">彩页</th>
						<th w_index="huashu" w_render="uploadRender">话术</th>
						<th w_index="jishuziliao" w_render="uploadRender">技术资料</th>
						<th w_index="others" w_render="uploadRender">其他</th>
						<th w_render="operate" width="10%;">操作</th>
					</tr>
				</table>
			</div>
			<!-- /.box-body -->
		</div>

		<div id="crudWin">
			<form id="crudFrm" class="form-horizontal">

				<div class="form-group">
					<label class="col-sm-3 control-label">平台名称</label>
					<div class="col-sm-7">
						<input name="title" type="text" class="form-control"
							required="true">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">制式拜访</label>
					<div class="col-sm-7">
						<label class="radio-inline"> <input type="radio"
							name="zhishibaifang" id="zhishibaifang0" value="0">是
						</label> <label class="radio-inline"> <input type="radio"
							name="zhishibaifang" id="zhishibaifang1" value="1">否
						</label>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">开始时间</label>
					<div class="col-sm-7">
						<input name="start" type="text" class="form-control"
							required="true" onfocus="WdatePicker({skin:'default'})">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">结束时间</label>
					<div class="col-sm-7">
						<input name="end" type="text" class="form-control" required="true" onfocus="WdatePicker({skin:'default'})">
					</div>
				</div>

				<input name="empId" type="hidden" class="form-control"
					value="<%=userDetails.getCode()%>">
			</form>
		</div>
		
<div id="importWin">
	<form id="importFrm" method="post" enctype="multipart/form-data"
		class="form-horizontal" action="${ctx}importPlatform.do">
		<div class="form-group">
			<label class="col-sm-3 control-label">选择文件</label>
			<div class="col-sm-7">
				<input class="btn btn-default" id="filename" type="file"
					name="filename" accept="xls" /> <input id="filetype" type="hidden"
					name="filetype" /> <input id="platid" type="hidden" name="platid" />
			</div>
		</div>
	</form>
</div>
		<script type="text/javascript">
			var that = this;

			var pk = 'id'; // java类中的主键字段
			var listUrl = ctx + 'listBizplatform.do'; // 查询
			var addUrl = ctx + 'addBizplatform.do'; // 添加
			var updateUrl = ctx + 'updateBizplatform.do'; // 修改
			var delUrl = ctx + 'delBizplatform.do'; // 删除
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

			/* $schBtn.click(function() {
			 search();
			 });

			 var $schBtn = $('#schBtn'); // 查询按钮
			 var $schFrm = $('#schFrm'); // 查询表单 */
			$schBtn.click(function() {
				//var schData = getFormData($schFrm);
				top.window.location.href = "${ctx}/openBizPlatformDetail.do?"
						+ $schFrm.serialize();
				/*  $.post("${ctx}/openBizPlatformDetail.do",schData,
					  function(data,status){
					   alert("Data: " + data + "\nStatus: " + status);
				 }); */

			});

			gridObj = $.fn.bsgrid.init('searchTable', {
				url : listUrl,
				pageSizeSelect : true,
				rowHoverColor : false // 移动行变色
				,
				rowSelectedColor : false // 选择行不高亮
				,
				isProcessLockScreen : false // 加载数据不显示遮罩层
				,
				displayBlankRows : false//,
				//pageSize : 10
			});

			crudWin = dialog({
				title : '编辑',
				width : 400,
				content : document.getElementById('crudWin'),
				okValue : '保存',
				ok : function() {
					that.save();
					return false;
				},
				cancelValue : '取消',
				cancel : function() {
					this.close();
					return false;
				}
			});

			function search() {
				var schData = getFormData($schFrm);
				gridObj.search(schData);
			}

			function operate(row, rowIndex, colIndex, options) {
				return '<a href="#" onclick="'
						+ FunUtil.createFun(that, 'edit', row)
						+ ' return false;">修改</a>' + '&nbsp;&nbsp;'
						+ '<a href="#" onclick="'
						+ FunUtil.createFun(that, 'del', row)
						+ ' return false;">删除</a>';
			}

			// 保存
			this.save = function() {
				var self = this;
				var data = getFormData($crudFrm);
				var validateVal = validator.form();
				if (validateVal) {
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
					loadFormData($crudFrm, row);
					crudWin.showModal();
				}
			}

			// 删除
			this.del = function(row) {
				if (row) {
					var d = dialog({
						title : '提示',
						width : 200,
						content : '确定要删除该记录吗?',
						okValue : '确定',
						ok : function() {
							Action.post(delUrl, row, function(result) {
								Action.execResult(result, function(result) {
									gridObj.refreshPage();
								});
							});
						},
						cancelValue : '取消',
						cancel : function() {
						}
					});
					d.showModal();
				}
			}

			var $importBtn = $('#importBtn'); // 导入按钮
			$importBtn.click(function() {
				importWin.showModal();
			});

			var showImp = function(id,col) {
				$("#platid").val(id);
				if(col==4){
					$("#filetype").val("caiye");
				}else if(col==5){
					$("#filetype").val("huashu");
				}else if(col==6){
					$("#filetype").val("jishuziliao");
				}else if(col==7){
					$("#filetype").val("others");
				}
				
				importWin.showModal();
			}
			
			var importWin = dialog({
				title : '导入',
				width : 600,
				content : document.getElementById('importWin'),
				okValue : '导入',
				ok : function() {
					$.ajaxFileUpload({
						url : ctx + "uploadPlatform.do?platid="+$("#platid").val()+"&filetype="+$("#filetype").val(),
						fileElementId : "filename",
						dataType : 'json',
						success : function(data, status) {
							if ("success" == data.status) {
								gridObj.refreshPage();
								importWin.close();
							} else if ("error" == data.status) {
								alert("上传失败!");
								return false;
							}
						}
					});
					return false;
				},
				cancelValue : '取消',
				cancel : function() {
					this.close();
					return false;
				}
			});

			var zhishiRender = function(record, rowIndex, colIndex, options) {
				if (record.zhishibaifang == 0) {
					return "是";
				} else {
					return "否";
				}

			}
			
		   var uploadRender = function(record, rowIndex, colIndex, options) {
			   var pathString="";
			   
			    if(colIndex==4){
			    	 if(record.caiye){
						    
						    var paths=record.caiye.split(",");
						    $.each(paths,function (i,path){
						    	var t_path = "platform/"+record.id+"/caiye/"+path;
						    	pathString += " <a href='"+t_path+"'>"+path+"</a><br>";
						    });
						    }
				}else if(colIndex==5){
					 if(record.huashu){
						    
						    var paths=record.huashu.split(",");
						    $.each(paths,function (i,path){
						    	var t_path = "platform/"+record.id+"/huashu/"+path;
						    	pathString += " <a href='"+t_path+"'>"+path+"</a><br>";
						    });
						    }
				}else if(colIndex==6){
					 if(record.jishuziliao){
						    
						    var paths=record.jishuziliao.split(",");
						    $.each(paths,function (i,path){
						    	var t_path = "platform/"+record.id+"/jishuziliao/"+path;
						    	pathString += " <a href='"+t_path+"'>"+path+"</a><br>";
						    });
						    }
				}else if(colIndex==7){
					 if(record.others){
						    
						    var paths=record.others.split(",");
						    $.each(paths,function (i,path){
						    	var t_path = "platform/"+record.id+"/others/"+path;
						    	pathString += " <a href='"+t_path+"'>"+path+"</a><br>";
						    });
						    }
				}
			    
			    /* pathString += "</ul>"; */
				return "<a id='importBtn' class='btn btn-primary btn-xs' onClick='showImp("+record.id+","+colIndex+")' style='color:#eee;'><i class='fa'></i> 上传 </a><br>"+pathString;

			}

			validator = $crudFrm.validate();
		</script>


	</section>
	<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<!-- Main Footer -->
<footer class="main-footer">
	<!-- Default to the left -->
	<strong>Copyright &copy; 2016</strong>
</footer>

</div>
<!-- ./wrapper -->

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
