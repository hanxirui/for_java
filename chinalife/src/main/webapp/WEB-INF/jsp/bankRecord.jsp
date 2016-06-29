<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp"%>
<jsp:include page="../menu.jsp">
	<jsp:param name="activeMenu" value="org" />
</jsp:include>
<%@page import="com.chinal.emp.security.AuthUser"%>
<%@ page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%
	AuthUser userDetails = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
					网点:<input name="wangdianname" type="text" class="form-control input-sm">
					网点代码:<input name="wangdiancode" type="text" class="form-control input-sm">
					专管员:<input name="mzhuanguanyuan" type="text" class="form-control input-sm">
					专管员代码:<input name="mzhuanguanyuancode" type="text" class="form-control input-sm"> 
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
			    <%
					if (userDetails.getLevel() >= 3) {
				%>
				<div class="btn-group">
					<a id="addBtn" class="btn btn-primary"> <i class="fa"></i>
						录入
					</a>
				</div>
				<div class="btn-group">
					<a id="importBtn" class="btn btn-primary"> <i class="fa"></i>
						导入
					</a>
				</div>
				<%
					}
				%>
			</div>
			<!-- /.box-header -->

			<div class="box-body">
				<table id="searchTable">
					<tr>
						<th w_index="bankname">银行名称</th>
						<th w_index="bankcode">银行代码</th>
						<th w_index="zhihangname">支行</th>
						<th w_index="zhihangcode">支行代码</th>
						<th w_index="wangdianname">网点</th>
						<th w_index="wangdiancode">网点代码</th>
						<th w_index="mzhuanguanyuan">专管员1</th>
						<th w_index="mzhuanguanyuancode">专管员1代码</th>
						<th w_index="szhuanguanyuan">专管员2</th>
						<th w_index="szhuanguanyuancode">专管员2代码</th>
						<th w_render="operate" width="10%;">操作</th>
					</tr>
				</table>
			</div>
			<!-- /.box-body -->
		</div>

		<div id="crudWin">
			<form id="crudFrm" class="form-horizontal">
				<div class="form-group">
					<label class="col-sm-2 control-label">银行名称</label>
					<div class="col-sm-3">
						<input name="bankname" type="text" class="form-control  input-sm"
							required="true">
					</div>
				
					<label class="col-sm-2 control-label">银行代码</label>
					<div class="col-sm-3">
						<input name="bankcode" type="text" class="form-control  input-sm"
							required="true">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">支行</label>
					<div class="col-sm-3">
						<input name="zhihangname" type="text" class="form-control  input-sm"
							required="true">
					</div>
				
					<label class="col-sm-2 control-label">支行代码</label>
					<div class="col-sm-3">
						<input name="zhihangcode" type="text" class="form-control  input-sm"
							required="true">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">网点</label>
					<div class="col-sm-3">
						<input name="wangdianname" type="text" class="form-control  input-sm"
							required="true">
					</div>
				
					<label class="col-sm-2 control-label">网点代码</label>
					<div class="col-sm-3">
						<input name="wangdiancode" type="text" class="form-control  input-sm"
							required="true">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">第一专管员</label>
					<div class="col-sm-3">
						<input name="mzhuanguanyuan" type="text" class="form-control  input-sm"
							required="true">
					</div>
				
					<label class="col-sm-2 control-label">第一专管员代码</label>
					<div class="col-sm-3">
						<input name="mzhuanguanyuancode" type="text" class="form-control  input-sm"
							required="true">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">第二专管员</label>
					<div class="col-sm-3">
						<input name="szhuanguanyuan" type="text" class="form-control  input-sm"
							required="true">
					</div>
				
					<label class="col-sm-2 control-label">第二专管员代码</label>
					<div class="col-sm-3">
						<input name="szhuanguanyuancode" type="text" class="form-control  input-sm"
							required="true">
					</div>
				</div>
			</form>
		</div>

		<div id="importWin">
			<form id="importFrm" method="post" enctype="multipart/form-data"
				class="form-horizontal" action="${ctx}importBank.do">
				<div class="form-group">
					<label class="col-sm-3 control-label">选择文件</label>
					<div class="col-sm-7">
						<input class="btn btn-default" id="filename" type="file"
							name="filename" accept="xls" />
					</div>
				</div>
				<div id="forLoad"></div>
				<div class="form-group">
					<label class="col-sm-3 control-label"> <a
						href="${ctx}template/BankTemplate.xlsx">下载模板</a></label>
				</div>
			</form>
		</div>

		<script type="text/javascript">
			var that = this;

			var pk = 'id'; // java类中的主键字段
			var listUrl = ctx + 'listBankRecord.do'; // 查询
			var addUrl = ctx + 'addBankRecord.do'; // 添加
			var updateUrl = ctx + 'updateBankRecord.do'; // 修改
			var delUrl = ctx + 'delBankRecord.do'; // 删除
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
				url : listUrl,
				pageSizeSelect : true,
				rowHoverColor : true // 移动行变色
				,
				rowSelectedColor : false // 选择行不高亮
				,
				isProcessLockScreen : false // 加载数据不显示遮罩层
				,
				displayBlankRows : false,
				pageSize : 10
			});

			crudWin = dialog({
				title : '编辑',
				width : 600,
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

			var importWin = dialog({
				title : '导入',
				width : 400,
				content : document.getElementById('importWin'),
				okValue : '导入',
				ok : function() {
					$("#importWin").LoadingOverlay("show", {
						image : "",
						fontawesome : "fa fa-spinner fa-spin"
					});
					$.ajaxFileUpload({
						url : ctx + "importBank.do",
						fileElementId : "filename",
						dataType : 'json',
						success : function(data, status) {
							$("#importWin").LoadingOverlay("hide");
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
