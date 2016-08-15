<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp"%>
<jsp:include page="../menu.jsp">
	<jsp:param name="activeMenu" value="customer" />
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
					姓名:<input name="name" type="text" class="form-control  input-sm">
					身份证号:<input name="idcardnum" type="text" class="form-control  input-sm">
					客户经理:<input name="empname" type="text" class="form-control  input-sm">
					机构:<select name="emporgcode" id="emporgcode" type="text"
						class="form-control  input-sm">
					</select> <input id="vcount" name="vcount" type="hidden"
						class="form-control  input-sm">
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

		<div class="box" style="overflow-x: auto;">
			<div class="box-header">
				<div class="btn-group">
					<a id="addBtn" class="btn btn-primary"> <i class="fa"></i> 录入
					</a>
				</div>

				<%
					if (userDetails.getLevel() == 5||userDetails.getLevel() == 7) {
				%>
				<div class="btn-group">
					<!-- 按钮需要权限控制，3级及以上 -->
					<a id="unfenpeiBtn" class="btn btn-primary"> <i class="fa"></i>
						未分配客户
					</a>
				</div>
				<div class="btn-group">
					<!-- 按钮需要权限控制，3级及以上 -->
					<a id="fenpeiBtn" class="btn btn-primary"> <i class="fa"></i>
						分配
					</a>
				</div>
				<%
					}
				%>
				<div class="btn-group">
					<!-- 按钮需要权限控制，普通业务员查询自己的-->
					<!-- <a id="apiBtn" class="btn btn-primary"> <i class="fa"></i>
						KPI统计查询
					</a> -->
				</div>
				<div class="btn-group">
					<a id="vtBtn" class="btn btn-primary"> </a>
				</div>
				<div class="btn-group">
					<a id="vsBtn" class="btn btn-primary"> </a>
				</div>
				<!--  <div class="btn-group">
			         	<a id="importBtn" class="btn btn-primary">
                           <i class="fa"></i>  导入用户
                        </a>
			          </div> -->
			</div>
			<!-- /.box-header -->

			<div class="box-body" style="width: 1000px; overflow-x: auto;">
				<table id="searchTable">
					<tr>
						<th w_check="true" w_index="idcardnum" width="3%;"></th>
						<th w_index="name" w_sort="name">客户姓名</th>
						<th w_index="sex" w_render="sexRender">性别</th>
						<th w_index="idcardnum">身份证号</th>
						<th w_index="phone" w_sort="addr">电话号码</th>
						<th w_index="addr" w_sort="addr">地址</th>
						<th w_index="type" w_render="fromRender">初始来源</th>
						<th w_index="leibie" w_render="leibieRender">类别</th>
						<th w_index="empname" w_sort="empname">客户经理</th>
						<th w_index="emporgname" w_sort="emporgname">机构</th>
						<!-- <th w_index="note">爱好及特点</th> -->
						<th w_render="operate">操作</th>
					</tr>
				</table>
			</div>
			<!-- /.box-body -->
		</div>



		<div id="crudWin">
			<form id="crudFrm" class="form-horizontal">
				<div class="form-group">
					<label class="col-sm-2 control-label">姓名</label>
					<div class="col-sm-3">
						<input name="name" type="text" class="form-control  input-sm"
							required="true">
					</div>

					<label class="col-sm-2 control-label">身份证号</label>
					<div class="col-sm-3">
						<input name="idcardnum" type="text" class="form-control  input-sm"
							required="true">
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label">地址</label>
					<div class="col-sm-3">
						<input name="addr" type="text" class="form-control  input-sm"
							required="true">
					</div>

					<label class="col-sm-2 control-label">初始来源</label>
					<div class="col-sm-3">
						<select id="type" name="type" class="form-control  input-sm" required="true">
							<option value="1">发放</option>
							<option value="2">新拓</option>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">新拓方式</label>
					<div class="col-sm-3">
						<select id="laiyuan" name="laiyuan" class="form-control  input-sm"
							required="true" disabled>
							<option value="2">自营新拓</option>
							<option value="3">渠道新拓</option>
						</select>
					</div>

					<label class="col-sm-2 control-label">生日</label>
					<div class="col-sm-3">
						<input name="birthday" type="text" class="form-control  input-sm"
							required="true" onfocus="WdatePicker({skin:'default'})">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">结婚纪念日</label>
					<div class="col-sm-3">
						<input name="weddingDay" type="text" class="form-control  input-sm"  onfocus="WdatePicker({skin:'default'})">
					</div>

					<label class="col-sm-2 control-label">客户经理</label>
					<div class="col-sm-3">
						<input id="empcode" name="empcode" type="hidden"
							class="form-control  input-sm" required="true"> <input id="empname"
							name="empname" type="text" class="form-control  input-sm" required="true">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">性别</label>
					<div class="col-sm-3">
						<label class="radio-inline"> <input type="radio"
							name="sex" id="sex0" value="0">女
						</label> <label class="radio-inline"> <input type="radio"
							name="sex" id="sex1" value="1">男
						</label>
					</div>

					<label class="col-sm-2 control-label">爱好及特点</label>
					<div class="col-sm-3">
						<input name="note" type="text" class="form-control  input-sm" >
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">类别</label>
					<div class="col-sm-3">
						<select id="leibie" name="leibie" class="form-control  input-sm"
							required="true">
							<option value="1">新客户</option>
							<option value="2">维护期</option>
							<option value="3">投诉客户</option>
							<option value="4">待出单客户</option>
							<option value="5">已新开发客户</option>
						</select>
					</div>
				</div>
			</form>
		</div>

		<div id="importWin">
			<form id="importFrm" method="post" enctype="multipart/form-data"
				class="form-horizontal" action="${ctx}importCustomer.do">
				<div class="form-group">
					<label class="col-sm-2 control-label">选择文件</label>
					<div class="col-sm-3">
						<input class="btn btn-default" id="filename" type="file"
							name="filename" accept="xls" />
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
			<form id="kpiFrm" class="form-horizontal">
				<div class="form-group">
					<label class="col-sm-3 control-label">开始时间</label>
					<div class="col-sm-3">
						<input id="startTime" name="startTime" type="text" class="form-control  input-sm"
							required="true" onfocus="WdatePicker({skin:'default'})">
					</div>

					<label class="col-sm-3 control-label">结束时间</label>
					<div class="col-sm-3">
						<input id="endTime" name="endTime" type="text" class="form-control  input-sm"
							required="true" onfocus="WdatePicker({skin:'default'})">
					</div>
				</div>
				<br>
				<div class="form-group">
					<label class="col-sm-3 control-label">拜访量</label>
					<div class="col-sm-3">
						<input name="baifangliang" type="text" class="form-control  input-sm"
							readonly>
					</div>

					<label class="col-sm-3 control-label">日均拜访量</label>
					<div class="col-sm-3">
						<input name="rijunbaifangliang" type="text" class="form-control  input-sm"
							readonly>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">新开自营件数</label>
					<div class="col-sm-3">
						<input name="ziyingcount" type="text" class="form-control  input-sm"
							readonly>
					</div>

					<label class="col-sm-3 control-label">保费</label>
					<div class="col-sm-3">
						<input name="ziyingbaofei" type="text" class="form-control  input-sm"
							readonly>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">新开渠道件数</label>
					<div class="col-sm-3">
						<input name="qudiaocount" type="text" class="form-control  input-sm"
							readonly>
					</div>

					<label class="col-sm-3 control-label">保费</label>
					<div class="col-sm-3">
						<input name="qudaobaofei" type="text" class="form-control  input-sm"
							readonly>
					</div>
				</div>

			</form>
		</div>

		<input type="hidden" id="winFrom">

		<script type="text/javascript">
			var that = this;

			var pk = 'id'; // java类中的主键字段
			var listUrl = ctx + 'listCustomerBasic.do?from=${from}'; // 查询
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
			var $vtBtn = $('#vtBtn'); // 二访查询按钮
			var $vsBtn = $('#vsBtn'); // 三访查询按钮
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
			$vtBtn.click(function() {
				/* 二访客户 */
				search('2');
			});
			$vsBtn.click(function() {
				/* 三访客户 */
				search('3');
			});

			$("#unfenpeiBtn").click(function() {
				/* 未分配客户 */
				search('NAN');
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
				displayBlankRows : false,//pageSize: 10
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
				},
				onshow : function() {
					if ($("#empcode").val()) {
						$.getJSON("${ctx}listEmployeeForCus.do", {
							"empcode" : $("#empcode").val()
						}, function(result) {
							var emp = result.data[0];
							$("#empname").val((emp.name));
						});
					}
				}
			});

			function search(vcount) {
				$("#vcount").val(vcount);
				var schData = getFormData($schFrm);
				gridObj.search(schData);
			}

			function operate(row, rowIndex, colIndex, options) {
				return '<a href="#" onclick="'
						+ FunUtil.createFun(that, 'edit', row)
						+ ' return false;">修改</a>' + '&nbsp;&nbsp;'
						/* + '<a href="#" onclick="'
						+ FunUtil.createFun(that, 'del', row)
						+ ' return false;">删除</a>'
						+ '&nbsp;&nbsp;' */
						+ '<a href="' + detailUrl + '?' + pk + '=' + row[pk]
						+ '">详情</a>';
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

			var sexRender = function(record, rowIndex, colIndex, options) {
				if (record.sex == 0) {
					return "女";
				} else {
					return "男";
				}

			}

			var fromRender = function(record, rowIndex, colIndex, options) {
				if (record.laiyuan == 2) {
					return "自营新拓";
				} else if (record.laiyuan == 3) {
					return "渠道新拓";
				} else {
					return "发放";
				}
			}

			var leibieRender = function(record, rowIndex, colIndex, options) {
				if (record.leibie == 1) {
					return "新客户";
				} else if (record.leibie == 2) {
					return "维护期";
				} else if (record.leibie == 3) {
					return "投诉客户";
				} else if (record.leibie == 4)  {
					return "待出单客户";
				} else {
					return "已新开发客户";
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
					$.ajaxFileUpload({
						url : ctx + "importCustomer.do",
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

			$("#fenpeiBtn").click(function() {
				var cardnums = gridObj.getCheckedValues('idcardnum');
				if (cardnums.length < 1) {
					alert("请至少选择一个客户.");
					return false;
				}
				$("#winFrom").val("fenpei");
				cusWin.showModal();
			})

			var roleList;
			$.getJSON("${ctx}listAllRole.do", null, function(result) {
				roleList = result.data;
			});

			$("#empname").click(function() {
				$("#winFrom").val("empname");
				cusWin.showModal();
			});

			var cusGridObj = $.fn.bsgrid.init('cusTable', {
				url : ctx + 'listEmployeeForCus.do',
				pageSizeSelect : true,
				rowHoverColor : true // 移动行变色
				,
				rowSelectedColor : false // 选择行不高亮
				,
				isProcessLockScreen : false // 加载数据不显示遮罩层
				,
				displayBlankRows : false,
				pagingLittleToolbar : true
			//
			});

			var cusWin = dialog({
				title : '选择客户经理',
				width : 800,
				content : document.getElementById('cusWin'),
				okValue : '保存',
				ok : function() {

					var name = cusGridObj.getCheckedValues('name');
					if (name.length != 1) {
						alert("请选择一个客户经理.");
						return false;
					}

					if ($("#winFrom").val() == "empname") {
						$('#empcode').val(cusGridObj.getCheckedValues('code'));
						$('#empname').val(cusGridObj.getCheckedValues('name'));
					} else {

						var cardnums = gridObj.getCheckedValues('idcardnum');
						if (cardnums.length < 1) {
							alert("请至少选择一个客户.");
							return false;
						}

						var _cardnums = new Array()

						$.each(cardnums, function(i, n) {
							_cardnums[i] = "'" + n + "'";
						});

						Action.post(ctx + 'fenpeiCustomer.do?cusIds='
								+ _cardnums + "&empcode="
								+ cusGridObj.getCheckedValues('code'), null,
								function(result) {
									Action.execResult(result, function(result) {
										gridObj.refreshPage();

									});
								});
					}
					this.close();
					return false;
				},
				cancelValue : '取消',
				cancel : function() {
					this.close();
					return false;
				},
				onshow : function() {
					$("#cusTable_pt_outTab").width($("#cusTable").width());
				}
			});
			var roleRender = function(record, rowIndex, colIndex, options) {
				var roleName;
				$.each(roleList, function(i, item) {
					if (item.id == record.role) {
						roleName = item.name;
					}
				});
				return roleName;
			}
			$("#type").change(function() {
				if ($("#type").val() == 1) {
					$("#laiyuan").attr("disabled", true);
				} else {
					$("#laiyuan").attr("disabled", false);
				}
			});

			$("#apiBtn").click(function() {
				getKpiInfo();
				kpiWin.showModal();
			});

			var kpiWin = dialog({
				title : '编辑',
				width : 600,
				content : document.getElementById('kpiWin'),
				okValue : '查询',
				ok : function() {
					getKpiInfo();
					return false;
				},
				cancelValue : '关闭',
				cancel : function() {
					this.close();
					return false;
				}
			});

			function getKpiInfo() {
				var startTime = $("#startTime").val();
				var endTime = $("#endTime").val();

				$.getJSON("${ctx}getKpiInfo.do", {
					'startTime' : startTime,
					'endTime' : endTime
				}, function(result) {
					console.log(result);
					loadFormData($("#kpiFrm"), result);
				});
			}

			var getOrgList = function() {

				$("<option></option>").val(0).text("请选择").appendTo(
						$("#emporgcode"));
				var empList;
				$.getJSON("${ctx}listOrg.do", null, function(result) {
					empList = result.data;
					if (empList) {
						$.each(empList, function(i, item) {
							$("<option></option>").val(item.code).text(
									item.name).appendTo($("#emporgcode"));
						});
					}
				});

			}
			getOrgList();

			$.getJSON("${ctx}getCustomerCountByVisit.do", {
				'count' : 2
			}, function(result) {
				$("#vtBtn").append("<i class='fa'></i>二访客户数量：" + result + "位");
			});

			$.getJSON("${ctx}getCustomerCountByVisit.do", {
				'count' : 3
			}, function(result) {
				$("#vsBtn").append(
						"<i class='fa'></i>三访及以上客户数量：" + result + "位");
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

