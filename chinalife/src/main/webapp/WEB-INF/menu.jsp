<%@ page contentType="text/html; charset=utf-8"%> 
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="ctx" value='<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/" %>'/>

<%
	String am = request.getParameter("activeMenu");
%>
<body class="hold-transition skin-blue-light sidebar-mini">
    <div class="wrapper">

      <!-- Main Header -->
      <header class="main-header">

        <!-- Logo -->
        <a href="#" class="logo">
          <!-- mini logo for sidebar mini 50x50 pixels -->
          <span class="logo-mini"></span>
          <!-- logo for regular state and mobile devices -->
          <!-- <span class="logo-lg">银保业务经营分析系统</span> -->
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
                <a href="#"><i class="fa fa-key"></i>修改密码</a>
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
              <div>你好,<span><sec:authentication property="name"/></span></div>
          </div>
          <!-- Sidebar Menu -->
          <ul class="sidebar-menu">
            <!-- Optionally, you can add icons to the links -->
            <li <%="customer".equals(am)?"class='active'":""%> ><a href="${ctx}openCustomer.do"><i class="fa fa-dashboard"></i> <span>客户管理</span></a></li>
            <li <%="employee".equals(am)?"class='active'":""%>><a href="${ctx}openEmployee.do"><i class="fa fa-diamond"></i> <span>保单管理</span> </a></li>
            <li <%="employee".equals(am)?"class='active'":""%>><a href="${ctx}openEmployee.do"><i class="fa fa-diamond"></i> <span>业务平台</span> </a></li>
            <li <%="employee".equals(am)?"class='active'":""%>><a href="${ctx}openEmployee.do"><i class="fa fa-diamond"></i> <span>投诉及突发事件管理</span> </a></li>
            <li <%="employee".equals(am)?"class='active'":""%>><a href="${ctx}openEmployee.do"><i class="fa fa-diamond"></i> <span>拜访管理</span> </a></li>
            <li <%="employee".equals(am)?"class='active'":""%>><a href="${ctx}openEmployee.do"><i class="fa fa-diamond"></i> <span>服务管理</span> </a></li>
             
            <li <%="employee".equals(am)?"class='active'":""%>><a href="${ctx}openEmployee.do"><i class="fa fa-diamond"></i> <span>人员管理</span> </a></li>
            <li <%="org".equals(am)?"class='active'":""%>><a href="${ctx}openOrg.do"><i class="fa fa-bank"></i> 机构管理</a></li>
            <li <%="role".equals(am)?"class='active'":""%>><a href="${ctx}openRole.do"><i class="fa fa-child"></i>职务管理</a></li>
          </ul><!-- /.sidebar-menu -->
        </section>
        <!-- /.sidebar -->
      </aside>
      
      