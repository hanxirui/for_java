<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp" %>
<jsp:include page="../menu.jsp" >
    <jsp:param name="activeMenu" value="jixiao"/>
</jsp:include>  
<%@page import="com.chinal.emp.security.AuthUser"  %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%
	String am = request.getParameter("activeMenu");
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
                <!--  <form id="schFrm" class="form-inline" onsubmit="return false;">
										                       序号:<input name="xuhao" type="text" class="form-control  input-sm">      
					 										                       部门:<input name="bumen" type="text" class="form-control  input-sm">      
					 										                       姓名:<input name="xingming" type="text" class="form-control  input-sm">      
					 										                       性别:<input name="xingbie" type="text" class="form-control  input-sm">      
					 										                       银行账号:<input name="yinhangzhanghao" type="text" class="form-control  input-sm">      
					 										                       身份证号:<input name="shenfenzhenghao" type="text" class="form-control  input-sm">      
					 										                       工号:<input name="gonghao" type="text" class="form-control  input-sm">      
					 										                       职级津贴:<input name="zhijijintie" type="text" class="form-control  input-sm">      
					 										                       销售精英津贴:<input name="xiaoshoujingyingjintie" type="text" class="form-control  input-sm">      
					 										                       新人保护/推荐津贴:<input name="xinrenbaohu" type="text" class="form-control  input-sm">      
					 										                       年资津贴:<input name="nianzijintie" type="text" class="form-control  input-sm">      
					 										                       管理津贴:<input name="guanlijintie" type="text" class="form-control  input-sm">      
					 										                       短险佣金:<input name="duanxianyongjin" type="text" class="form-control  input-sm">      
					 										                       渠道佣金:<input name="qudaoyongjin" type="text" class="form-control  input-sm">      
					 										                       自营佣金:<input name="ziyingyongjin" type="text" class="form-control  input-sm">      
					 										                       自营续期:<input name="ziyingxuqi" type="text" class="form-control  input-sm">      
					 										                       网点续期:<input name="wangdianxuqi" type="text" class="form-control  input-sm">      
					 										                       借发/补发:<input name="jiefa" type="text" class="form-control  input-sm">      
					 										                       小计:<input name="xiaoji" type="text" class="form-control  input-sm">      
					 										                       其他扣款:<input name="qitakoukuan" type="text" class="form-control  input-sm">      
					 										                       扣款小计:<input name="koukuanxiaoji" type="text" class="form-control  input-sm">      
					 										                       应发小计:<input name="yingfaxiaoji" type="text" class="form-control  input-sm">      
					 										                       分公司奖励:<input name="fengognsijiangli" type="text" class="form-control  input-sm">      
					 										                       基层奖励:<input name="jicengjiangli" type="text" class="form-control  input-sm">      
					 										                       其他补贴:<input name="qitabutie" type="text" class="form-control  input-sm">      
					 										                       其他补贴（基层承担）:<input name="jicengqitabutie" type="text" class="form-control  input-sm">      
					 										                       展业津贴:<input name="zhanyejintie" type="text" class="form-control  input-sm">      
					 										                       分公司实物奖励:<input name="fengongsishiwujiangli" type="text" class="form-control  input-sm">      
					 										                       基层公司实物奖励:<input name="jicengshiwujiangli" type="text" class="form-control  input-sm">      
					 										                       保险保障:<input name="baoxianbaozhang" type="text" class="form-control  input-sm">      
					 										                       应发合计:<input name="yingfaheji" type="text" class="form-control  input-sm">      
					 										                       委托社保:<input name="weituoshebao" type="text" class="form-control  input-sm">      
					 										                       产说会扣款:<input name="chanshuohuikoukuan" type="text" class="form-control  input-sm">      
					 										                       三代扣款和超期扣款:<input name="sandaikoukuanhechaoqikoukuan" type="text" class="form-control  input-sm">      
					 										                       个人所得税:<input name="gerensuodeshui" type="text" class="form-control  input-sm">      
					 										                       增值税:<input name="zengzhishui" type="text" class="form-control  input-sm">      
					 										                       城建税:<input name="chengjianshui" type="text" class="form-control  input-sm">      
					 										                       教育费附加:<input name="jiaoyufeifujia" type="text" class="form-control  input-sm">      
					 										                       地方教育费附加:<input name="difangjiaoyufeifujia" type="text" class="form-control  input-sm">      
					 										                       防洪费:<input name="fanghongfei" type="text" class="form-control  input-sm">      
					 										                       扣分公司实物奖励:<input name="koufengongsishiwujiangli" type="text" class="form-control  input-sm">      
					 										                       扣基层公司实物奖励:<input name="koujicenggongsishiwujiangli" type="text" class="form-control  input-sm">      
					 										                       扣保险保障:<input name="koubaoxianbaozhang" type="text" class="form-control  input-sm">      
					 										                       寿险实发金额:<input name="shouxianshifajine" type="text" class="form-control  input-sm">      
					 										                       财险绩效:<input name="caixianjixiao" type="text" class="form-control  input-sm">      
					 										                       扣财险绩效:<input name="koucaixianjixiao" type="text" class="form-control  input-sm">      
					 										                       实发合计:<input name="shifaheji" type="text" class="form-control  input-sm">      
					 					                   	<button id="schBtn" type="submit" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
					<button type="reset" class="btn btn-default"><i class="fa fa-remove"></i> 清空</button>
				</form> -->
               </div><!-- /.box-body -->
           </div>
           
          <div class="box" style="overflow-x:auto;">
				<div class="box-header">
				<%
					if (userDetails.getLevel() >= 3) {
				%>
					 <div class="btn-group">
			         	<a id="addBtn" class="btn btn-primary">
			            	<i class="fa"></i> 录入 
			         	</a>
			          </div>
			           <div class="btn-group">
			         	<a id="importBtn" class="btn btn-primary">
                           <i class="fa"></i>  导入
                        </a>
			          </div>
			    <%} %>
				</div><!-- /.box-header -->
			
				<div class="box-body" style="width:3000px;overflow-x:auto;">	 
					<table id="searchTable">
						<tr>           
														<!-- <th w_index="xuhao">序号</th> -->
																			<th w_index="bumen">部门</th>
																			<th w_index="xingming">姓名</th>
																			<th w_index="xingbie">性别</th>
																			<th w_index="yinhangzhanghao">银行账号</th>
																			<th w_index="shenfenzhenghao">身份证号</th>
																			<th w_index="gonghao">工号</th>
																			<th w_index="zhijijintie">职级津贴</th>
																			<th w_index="xiaoshoujingyingjintie">销售精英津贴</th>
																			<th w_index="xinrenbaohu">新人保护/推荐津贴</th>
																			<th w_index="nianzijintie">年资津贴</th>
																			<th w_index="guanlijintie">管理津贴</th>
																			<th w_index="duanxianyongjin">短险佣金</th>
																			<th w_index="qudaoyongjin">渠道佣金</th>
																			<th w_index="ziyingyongjin">自营佣金</th>
																			<th w_index="ziyingxuqi">自营续期</th>
																			<th w_index="wangdianxuqi">网点续期</th>
																			<th w_index="jiefa">借发/补发</th>
																			<th w_index="xiaoji">小计</th>
																			<th w_index="qitakoukuan">其他扣款</th>
																			<th w_index="koukuanxiaoji">扣款小计</th>
																			<th w_index="yingfaxiaoji">应发小计</th>
																			<th w_index="fengognsijiangli">分公司奖励</th>
																			<th w_index="jicengjiangli">基层奖励</th>
																			<th w_index="qitabutie">其他补贴</th>
																			<th w_index="jicengqitabutie">其他补贴（基层承担）</th>
																			<th w_index="zhanyejintie">展业津贴</th>
																			<th w_index="fengongsishiwujiangli">分公司实物奖励</th>
																			<th w_index="jicengshiwujiangli">基层公司实物奖励</th>
																			<th w_index="baoxianbaozhang">保险保障</th>
																			<th w_index="yingfaheji">应发合计</th>
																			<th w_index="weituoshebao">委托社保</th>
																			<th w_index="chanshuohuikoukuan">产说会扣款</th>
																			<th w_index="sandaikoukuanhechaoqikoukuan">三代扣款和超期扣款</th>
																			<th w_index="gerensuodeshui">个人所得税</th>
																			<th w_index="zengzhishui">增值税</th>
																			<th w_index="chengjianshui">城建税</th>
																			<th w_index="jiaoyufeifujia">教育费附加</th>
																			<th w_index="difangjiaoyufeifujia">地方教育费附加</th>
																			<th w_index="fanghongfei">防洪费</th>
																			<th w_index="koufengongsishiwujiangli">扣分公司实物奖励</th>
																			<th w_index="koujicenggongsishiwujiangli">扣基层公司实物奖励</th>
																			<th w_index="koubaoxianbaozhang">扣保险保障</th>
																			<th w_index="shouxianshifajine">寿险实发金额</th>
																			<th w_index="caixianjixiao">财险绩效</th>
																			<th w_index="koucaixianjixiao">扣财险绩效</th>
																			<th w_index="shifaheji">实发合计</th>
													<!-- <th w_render="operate" width="10%;">操作</th> -->
						</tr>
					</table>
				</div><!-- /.box-body -->
			</div>
		    
		    <div id="crudWin">
			    	<form id="crudFrm" class="form-horizontal">
											   						<div class="form-group">
	                      <label class="col-sm-3 control-label">序号</label>
	                      <div class="col-sm-7">
	                        <input name="xuhao" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">部门</label>
	                      <div class="col-sm-7">
	                        <input name="bumen" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">姓名</label>
	                      <div class="col-sm-7">
	                        <input name="xingming" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">性别</label>
	                      <div class="col-sm-7">
	                        <input name="xingbie" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">银行账号</label>
	                      <div class="col-sm-7">
	                        <input name="yinhangzhanghao" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">身份证号</label>
	                      <div class="col-sm-7">
	                        <input name="shenfenzhenghao" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">工号</label>
	                      <div class="col-sm-7">
	                        <input name="gonghao" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">职级津贴</label>
	                      <div class="col-sm-7">
	                        <input name="zhijijintie" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">销售精英津贴</label>
	                      <div class="col-sm-7">
	                        <input name="xiaoshoujingyingjintie" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">新人保护/推荐津贴</label>
	                      <div class="col-sm-7">
	                        <input name="xinrenbaohu" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">年资津贴</label>
	                      <div class="col-sm-7">
	                        <input name="nianzijintie" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">管理津贴</label>
	                      <div class="col-sm-7">
	                        <input name="guanlijintie" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">短险佣金</label>
	                      <div class="col-sm-7">
	                        <input name="duanxianyongjin" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">渠道佣金</label>
	                      <div class="col-sm-7">
	                        <input name="qudaoyongjin" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">自营佣金</label>
	                      <div class="col-sm-7">
	                        <input name="ziyingyongjin" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">自营续期</label>
	                      <div class="col-sm-7">
	                        <input name="ziyingxuqi" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">网点续期</label>
	                      <div class="col-sm-7">
	                        <input name="wangdianxuqi" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">借发/补发</label>
	                      <div class="col-sm-7">
	                        <input name="jiefa" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">小计</label>
	                      <div class="col-sm-7">
	                        <input name="xiaoji" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">其他扣款</label>
	                      <div class="col-sm-7">
	                        <input name="qitakoukuan" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">扣款小计</label>
	                      <div class="col-sm-7">
	                        <input name="koukuanxiaoji" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">应发小计</label>
	                      <div class="col-sm-7">
	                        <input name="yingfaxiaoji" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">分公司奖励</label>
	                      <div class="col-sm-7">
	                        <input name="fengognsijiangli" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">基层奖励</label>
	                      <div class="col-sm-7">
	                        <input name="jicengjiangli" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">其他补贴</label>
	                      <div class="col-sm-7">
	                        <input name="qitabutie" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">其他补贴（基层承担）</label>
	                      <div class="col-sm-7">
	                        <input name="jicengqitabutie" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">展业津贴</label>
	                      <div class="col-sm-7">
	                        <input name="zhanyejintie" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">分公司实物奖励</label>
	                      <div class="col-sm-7">
	                        <input name="fengongsishiwujiangli" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">基层公司实物奖励</label>
	                      <div class="col-sm-7">
	                        <input name="jicengshiwujiangli" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">保险保障</label>
	                      <div class="col-sm-7">
	                        <input name="baoxianbaozhang" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">应发合计</label>
	                      <div class="col-sm-7">
	                        <input name="yingfaheji" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">委托社保</label>
	                      <div class="col-sm-7">
	                        <input name="weituoshebao" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">产说会扣款</label>
	                      <div class="col-sm-7">
	                        <input name="chanshuohuikoukuan" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">三代扣款和超期扣款</label>
	                      <div class="col-sm-7">
	                        <input name="sandaikoukuanhechaoqikoukuan" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">个人所得税</label>
	                      <div class="col-sm-7">
	                        <input name="gerensuodeshui" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">增值税</label>
	                      <div class="col-sm-7">
	                        <input name="zengzhishui" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">城建税</label>
	                      <div class="col-sm-7">
	                        <input name="chengjianshui" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">教育费附加</label>
	                      <div class="col-sm-7">
	                        <input name="jiaoyufeifujia" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">地方教育费附加</label>
	                      <div class="col-sm-7">
	                        <input name="difangjiaoyufeifujia" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">防洪费</label>
	                      <div class="col-sm-7">
	                        <input name="fanghongfei" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">扣分公司实物奖励</label>
	                      <div class="col-sm-7">
	                        <input name="koufengongsishiwujiangli" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">扣基层公司实物奖励</label>
	                      <div class="col-sm-7">
	                        <input name="koujicenggongsishiwujiangli" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">扣保险保障</label>
	                      <div class="col-sm-7">
	                        <input name="koubaoxianbaozhang" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">寿险实发金额</label>
	                      <div class="col-sm-7">
	                        <input name="shouxianshifajine" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">财险绩效</label>
	                      <div class="col-sm-7">
	                        <input name="caixianjixiao" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">扣财险绩效</label>
	                      <div class="col-sm-7">
	                        <input name="koucaixianjixiao" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">实发合计</label>
	                      <div class="col-sm-7">
	                        <input name="shifaheji" type="text" class="form-control  input-sm" required="true">
	                      </div>
	                    </div>
					   										</form>
			    </div>
		    <div id="importWin">
                    <form id="importFrm"  method="post"   enctype="multipart/form-data"  class="form-horizontal" action="${ctx}importCustomer.do">                   
                       <div class="form-group">
                          <label class="col-sm-2 control-label">选择文件</label>
                          <div class="col-sm-8">
                           <input class="btn btn-default" id="filename" type="file" name="filename"  accept="xls"/>
                          </div>
                        </div>
                         <a href="${ctx}template/GongzidanTemplate.xlsx">下载模板</a>
                    </form>
               </div>
<script type="text/javascript">     
var that = this;

var pk = ''; // java类中的主键字段
var listUrl = ctx + 'listGongzidan.do'; // 查询
var addUrl = ctx + 'addGongzidan.do'; // 添加
var updateUrl = ctx + 'updateGongzidan.do'; // 修改
var delUrl = ctx + 'delGongzidan.do'; // 删除
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
    //
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
               url:ctx+"importGongzidan.do",
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
