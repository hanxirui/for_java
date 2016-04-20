<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="taglib.jsp" %>
<c:set var="bui" value="${res}bui/"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <title>管理系统</title>
   <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   <link href="${bui}css/dpl-min.css" rel="stylesheet" type="text/css" />
   <link href="${bui}css/bui-min.css" rel="stylesheet" type="text/css" />
   <link href="${bui}css/main-min.css" rel="stylesheet" type="text/css" />
 </head>
 <body>

  <div class="header">
    
      <div class="dl-title">
          <span class="dl-title-text">后台管理</span>
      </div>

    <div class="dl-log">欢迎您，<span class="dl-log-user">admin</span><a href="###" title="退出系统" class="dl-log-quit">[退出]</a>
    </div>
  </div>
   <div class="content">
    <div class="dl-main-nav">
      <div class="dl-inform"><div class="dl-inform-title">贴心小秘书<s class="dl-inform-icon dl-up"></s></div></div>
      <ul id="J_Nav"  class="nav-list ks-clear">
        <li class="nav-item dl-selected"><div class="nav-item-inner nav-home">首页</div></li>
      </ul>
    </div>
    <ul id="J_NavContent" class="dl-tab-conten">

    </ul>
   </div>
  <script type="text/javascript" src="${bui}js/jquery-1.8.1.min.js"></script>
  <script type="text/javascript" src="${bui}js/bui-min.js"></script>

  <script>
    BUI.use('common/main',function(){
      var config = [{
          id:'menu', 
          homePage : 'orderInfo',
          menu:[{
              text:'首页内容',
              items:[
                {id:'orderInfo',text:'订单管理',href:'orderInfo_bui.jsp',closeable : false}
                ,{id:'demo-easyui',text:'easyui-demo',href:'orderInfo_easyui.jsp'}
              ]
            }
          ]
      }];
      new PageUtil.MainPage({
        modulesConfig : config
      });
    });
  </script>
 </body>
</html>