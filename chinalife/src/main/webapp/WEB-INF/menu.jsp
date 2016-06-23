<%@ page contentType="text/html; charset=utf-8"%> 
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page import="com.chinal.emp.security.AuthUser"  %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%
	String am = request.getParameter("activeMenu");
    AuthUser userDetails = (AuthUser) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
%>

<c:set var="ctx" value='<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/" %>'/>

<body class="hold-transition skin-blue-light sidebar-mini">
    <div class="wrapper">

      <!-- Main Header -->
      <header class="main-header">

        <!-- Logo -->
        <a href="#" class="logo">
          <!-- mini logo for sidebar mini 50x50 pixels -->
          <span class="logo-mini"></span>
          <!-- logo for regular state and mobile devices -->
          <!-- <span class="logo-lg">LBS管理</span> -->
          <span class="logo-lg">银保业务经营分析系统</span>
        </a>

        <!-- Header Navbar -->
        <nav class="navbar navbar-static-top" role="navigation">
          <!-- Sidebar toggle button-->
          <a href="#" class="sidebar-toggle" data-toggle="offcanvas">
            <span class="sr-only">Toggle navigation</span>
          </a>
          <!-- Navbar Right Menu -->
          <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">
             <li>
                <a href="#" id="changepassword"><i class="fa fa-key"></i>修改密码</a>
              </li>
              <li>
                <a href="${ctx}logout.do"><i class="fa fa-share"></i>退出</a>
              </li>
            </ul>
          </div>
        </nav>
      </header>
      <!-- Left side column. contains the logo and sidebar -->
      <aside class="main-sidebar">

        <!-- sidebar: style can be found in sidebar.less -->
        <section class="sidebar">
          <!-- Sidebar user panel -->
          <div class="user-panel">
              <div>你好,<span> <%=userDetails.getcName() %></div>
          </div>
          <!-- Sidebar Menu -->
          <ul class="sidebar-menu">
            <!-- Optionally, you can add icons to the links -->
            <li <%="main".equals(am)?"class='active'":""%> ><a href="${ctx}openMainStatistics.do"><i class="fa fa-dashboard"></i> <span>统计分析</span></a></li>
            <li <%="customer".equals(am)?"class='active'":""%> ><a href="${ctx}openCustomerBasic.do"><i class="fa fa-diamond"></i> <span>客户管理</span></a></li>
            <li <%="insurance".equals(am)?"class='active'":""%>><a href="${ctx}openInsuranceRecord.do"><i class="fa fa-diamond"></i> <span>保单管理</span> </a></li>
            <li <%="bizplat".equals(am)?"class='active'":""%>><a href="${ctx}openBizplatform.do"><i class="fa fa-diamond"></i> <span>业务平台</span> </a></li>
            <li <%="claim".equals(am)?"class='active'":""%>><a href="${ctx}openClaimRecord.do"><i class="fa fa-diamond"></i> <span>投诉及突发事件管理</span> </a></li>
            <li <%="visit".equals(am)?"class='active'":""%>><a href="${ctx}openCustomerForVisit.do"><i class="fa fa-diamond"></i> <span>拜访管理</span> </a></li>
            <li <%="service".equals(am)?"class='active'":""%>><a href="${ctx}openCustomerForService.do"><i class="fa fa-diamond"></i> <span>服务管理</span> </a></li>
           
            <li <%="employee".equals(am)?"class='active'":""%> <%=userDetails.getLevel()<3?"style='display:none'":"" %>><a href="${ctx}openEmployee.do"><i class="fa fa-diamond"></i> <span>人员管理</span> </a></li>
            <li <%="org".equals(am)?"class='active'":""%> <%=userDetails.getLevel()==5?"":"style='display:none'" %>><a href="${ctx}openOrg.do"><i class="fa fa-bank"></i> 机构管理</a></li>
            <li <%="role".equals(am)?"class='active'":""%> <%=userDetails.getLevel()==5?"":"style='display:none'" %>><a href="${ctx}openRole.do"><i class="fa fa-child"></i>职务管理</a></li>
            <li <%="jixiao".equals(am)?"class='active'":""%>> <a href="${ctx}openGongzidan.do"><i class="fa fa-trophy"></i>绩效管理</a></li>
             <%-- <li <%="report".equals(am)?"class='active'":""%>> <a href="${ctx}openReport.do"><i class="fa fa-trophy"></i>统计报表</a></li> --%>
            <li <%="bank".equals(am)?"class='active'":""%> <%=userDetails.getLevel()==5?"":"style='display:none'" %>><a href="${ctx}openBankRecord.do"><i class="fa fa-trophy"></i>银行机构管理</a></li>
            <li <%="login".equals(am)?"class='active'":""%> <%=userDetails.getcName().equals("admin")?"":"style='display:none'" %>><a href="${ctx}openLoginrecord.do"><i class="fa fa-trophy"></i>登录日志</a></li>
          </ul><!-- /.sidebar-menu -->
        </section>
        <!-- /.sidebar -->
      </aside>
      
  
		    <div id="pwdWin">
			    	<form id="pwdFrm" class="form-horizontal">
						<div class="form-group">
	                      <label class="col-sm-3 control-label">原始密码</label>
	                      <div class="col-sm-7">
	                        <input name="oldpassword" id="oldpassword" type="password" class="form-control  input-sm">
	                      </div>
	                    </div>
					   	<div class="form-group">
	                      <label class="col-sm-3 control-label">新密码</label>
	                      <div class="col-sm-7">
	                        <input name="password" id="password" type="password" class="form-control  input-sm">
	                      </div>
	                    </div>
					   	<div class="form-group">
	                      <label class="col-sm-3 control-label">请确认</label>
	                      <div class="col-sm-7">
	                        <input name="repassword" id="repassword" type="password" class="form-control  input-sm">
	                      </div>
	                    </div>
	                    <input name="id" id="id" type="hidden"  value="<%=userDetails.getId() %>">
		            <input name="code" id="code" type="hidden" value="<%=userDetails.getCode() %>">
					   	
					   										</form>
			    </div>
		    
<script type="text/javascript">     
var that = this;

var pk = 'id'; // java类中的主键字段
var updateAdminUser = ctx + 'updatePassword.do'; // 修改
var submitAdminUser = ''; // 提交URL

var pwdWin; // 窗口
var $pwdFrm = $('#pwdFrm'); // 编辑表单


var $pwdBtn = $('#changepassword'); 

var pwdValidator; // 验证器

function reset() {
	$pwdFrm.get(0).reset();
	pwdValidator.resetForm();
}

pwdWin = dialog({
	title: '编辑',
	width:400,
	content: document.getElementById('pwdWin'),
	okValue: '保存',
	ok: function () {
		savePwd();
		return false;
	},
	cancelValue: '取消',
	cancel: function () {
		this.close();
		return false;
	}
});

//初始化事件
$pwdBtn.click(function() {
	submitAdminUser = updateAdminUser;
	reset();
	pwdWin.title('修改密码');
	pwdWin.showModal();	
});


// 保存
savePwd = function() {
	var self = this;
	var data = getFormData($pwdFrm);

	
    var formState=pwdvalidator.form();    
    
	if(formState) {
		Action.post(submitAdminUser, data, function(result) {
			Action.execResult(result, function(result) {
				gridObj.refreshPage();
				pwdWin.close();
			});
		});
	}
}

pwdvalidator = $pwdFrm.validate({
	  rules:{
		    oldpassword: {required:true,
		        remote:{
		               type:"POST",
		               url:"${ctx}checkPassword.do",             
		               data:{
		            	   oldpassword:function(){return $("#oldpassword").val();},
		            	   id:function(){return $("#id").val();}
		               } 
		        } 
		     },
		     password: {required:true,minlength:6},
		     repassword: {required:true,equalTo:"#password"}
		 
	   },
      messages:{
	     password: {required:"密码不能为空！",minlength:"密码位数必须大于等于6个字符！"},
	     repassword: {required:"确认密码不能为空！",equalTo:"确认密码和密码不一致！"},
	     oldpassword:  {required:"请输入原密码",remote:"原密码错误"}
     }
});
</script>      