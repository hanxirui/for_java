<%@page import="org.durcframework.core.UserContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="taglib.jsp" %>
<c:set var="bui" value="${res}bui/"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
   <title>管理系统</title>
   <link href="${bui}css/main-min.css" rel="stylesheet" type="text/css" />
 </head>
 <body>

  <div class="header">
    
      <div class="dl-title">
          <span class="dl-title-text">后台管理</span>
      </div>

    <div class="dl-log">欢迎您，<span class="dl-log-user"><%=UserContext.getInstance().getUser().getUsername() %></span>
    	<a href="#" id="modifyPswd" title="修改密码" class="dl-log-quit">[修改密码]</a>
    	<a href="#" onclick="logout(); return false;" title="退出系统" class="dl-log-quit">[退出]</a>
    </div>
  </div>
   <div class="content">
    <div class="dl-main-nav">
      <div class="dl-inform"><div class="dl-inform-title">贴心小秘书<s class="dl-inform-icon dl-up"></s></div></div>
      <ul id="J_Nav"  class="nav-list ks-clear">
<!--         <li class="nav-item dl-selected"><div class="nav-item-inner nav-order">权限系统</div></li> -->
<!--         <li class="nav-item"><div class="nav-item-inner nav-order">业务系统</div></li> -->
      </ul>
    </div>
    <ul id="J_NavContent" class="dl-tab-conten">

    </ul>
   </div>

  <script>
  	window.logout = function() {
		$.ajax({
			type: "POST",
		    url: ctx + 'logout.do',
		  	dataType:'json',
		    success: function(result){
				if (result.success){
					location.href = ctx + 'index.jsp';
				} 
			}
		});
	}

	 BUI.use(['bui/overlay','bui/mask'],function(Overlay){
        var dialog = new Overlay.Dialog({
            title:'修改密码',
            width:500,
            loader : {
              url : ctx + 'permission/modifyPswd.jsp',
              autoLoad : false, //不自动加载
              lazyLoad : false //不延迟加载
            }
            ,buttons:[]
		});
		
		$('#modifyPswd').click(function(){
			dialog.show();
			dialog.get('loader').load();
		});
		
		window.hideModifyPwsd = function() {
			dialog.close();
		}
		
	});
  
    BUI.use('common/main',function(){
    	// key对应tabId,value对应configItem
    	var $J_Nav = $('#J_Nav');
    	var tab = {}; 
    	var config = [];
    	
    	Action.post(ctx + 'listUserMenu_backuser.do',{},function(menus){
    		for(var i=0,len=menus.length;i<len;i++) {
    			var menu = menus[i];
    			var tabId = menu.tabId;
    			var tabName = menu.tabName;
    			var tabInfo = tabId + '|' + tabName;
    			var configItem = getConfigItem(tabInfo);
    			// 构建数据
    			configItem.id = tabId;
    			configItem.menu = buildConfigItemMenu(configItem,menu);
    		}
    		
    		// 渲染
    		for(var tabInfo in tab) {
    			var configItem = tab[tabInfo];
    			config.push(configItem);
    			appendTabHtml(tabInfo);
    		}
    		
    		new PageUtil.MainPage({
    	        modulesConfig : config
    	    });
    		
    	});
    	
    	function getConfigItem(tabInfo) {
    		var configItem = tab[tabInfo];
    		if(!configItem) {
    			configItem = {};
    			tab[tabInfo] = configItem;
    		}
    		return configItem;
    	}
    	
    	function buildConfigItemMenu(configItem,menu) {
    		var menus = configItem.menu || [];
    		var configMenu = {};
    		var configMenuItems = [];
    		var children = menu.children;
    		
    		for(var i=0,len=children.length;i<len;i++) {
    			var child = children[i];
    			configMenuItems.push({
    				id:child.id + '.jhtml'
    				,text:child.text
    				,href:ctx + child.url + '?srId=' + child.id
    			});
    		}
    		
    		configMenu.text = menu.text;
    		configMenu.items = configMenuItems;
    		
    		menus.push(configMenu);
    		
    		return menus;
    	}
    	
    	function appendTabHtml(tabInfo) {
    		var tabArr = tabInfo.split('|');
    		var tabId = tabArr[0];
    		var tabName = tabArr[1];
    		var isFirst = $J_Nav.find('li').length == 0;
    		
    		var li = '<li class="nav-item '+(isFirst ? 'dl-selected' : '')+'"><div class="nav-item-inner nav-order">' + tabName + '</div></li>'
    		
    		$J_Nav.append(li);
    	}
    	
// 		var config = [
// 		{
// 		    id:'permission', 
// 		    menu:[{
// 		        text:'权限管理',
// 		        items:[
// 		          {id:'user',text:'用户管理',href:'permission/user.jsp'}
// 		          ,{id:'role',text:'角色管理',href:'permission/role.jsp'}
// 		          ,{id:'resMgr',text:'资源管理',href:'permission/resMgr.jsp'}
// 		          ,{id:'group',text:'用户组管理',href:'permission/group.jsp'}
// 		        ]
// 		      }
// 		    ]
// 		}             
//       ,{
//           id:'menu', 
//           homePage : 'orderInfo',
//           menu:[{
//               text:'首页内容',
//               items:[
//                 {id:'orderInfo',text:'订单管理',href:'orderInfo_bui.jsp',closeable : false}
//                 ,{id:'demo-easyui',text:'easyui-demo',href:'orderInfo_easyui.jsp'}
//               ]
//             }
//           ]
//       }];
      
    });
    
</script>
</body>
</html>