<%@page import="org.durcframework.core.UserContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>用户登录</title>
<script type="text/javascript" src="${res}js/MD5.js"></script>
</head>
<body>
<c:choose>
<c:when test="<%=UserContext.getInstance().getUser() != null %>">
	<script type="text/javascript">window.location = 'main.jsp';</script>
</c:when>
<c:otherwise>
	
	<div id="win" class="hide">
		<div class="row">
			<div class="span8">
				<form id="loginForm" >
					<div class="row">
						<div class="control-group span8">
							<label class="control-label">用户名：</label>
							<div class="controls">
								<input type="text" 
								name="username" 
								data-rules="{required:true,minlength:1,maxlength:200}"
								class="control-text">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="control-group span8">
							<label class="control-label">密码：</label>
							<div class="controls">
								<input type="password"
								name="password" 
								data-rules="{required:true,minlength:1,maxlength:200}"
								class="control-text">
							</div>
						</div>
					</div>
					<div class="row">
						<div class="control-group offset3">
							<button id="btnLogin" type="button" class="button button-primary">登录</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	BUI.use(['bui/editor','bui/tooltip'], function(Editor,Tooltip) {
		var editing = new Editor.DialogEditor({
			contentId : 'win',
			form : {
				srcNode : $('#loginForm')
			},
			title : '登录系统',
			width : 500,
			zIndex:100,
			mask : false,
			closeable : false,
			// enter按钮
			success:function(){
				login();
			},
			buttons : []
		});
		editing.show();
		editing.focus();
		
	
		//使用模板上边显示
		var tip = new Tooltip.Tip({
			//trigger : '#t3', //不设置触发点
			align : {
				node : '#loginForm'
			},
			alignType : 'top',
			offset : 20,
			zIndex:101,
			triggerEvent : 'click',
			autoHideType : 'click',
			title : '',
			elCls : 'tips tips-warning',
			titleTpl : '<span class="x-icon x-icon-small x-icon-error"><i class="icon icon-white icon-bell"></i></span><div class="tips-content">{title}</div>'
		});
		tip.render();

		$('#btnLogin').unbind().click(function() {
			login();
		});

		function login() {
			editing.valid();
			if (editing.isValid()) {
				editing.disable(); //禁用
				var form = editing.get('form');
				var data = form.toObject(); // 表单数据
				var password = data.password;
				data.password = faultylabs.MD5(password);

				Action.post('login.do', data, function(result) {
					if (result && result.success) {
						top.location = 'main.jsp';
					} else {
						var errorMsg = result.message;
						editing.enable(); //解除禁用
						tip.set('title',errorMsg);
						tip.show();
					}
				});
			}
		}
		
		$('.bui-stdmod-header').css({cursor:'default'}).unbind();
	});
	</script>
	
			
	</c:otherwise>
</c:choose>
</body>
</html>
