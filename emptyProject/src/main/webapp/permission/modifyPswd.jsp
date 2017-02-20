<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改密码</title>
<script type="text/javascript" src="${res}js/MD5.js"></script>
</head>
<body>
<div class="row">
	<div class="span16">
		<form id="baseForm" class="form-horizontal">
			<div class="row">
				<div class="control-group span16">
					<label class="control-label">原密码：</label>
					<div class="controls">
						<input type="password" 
						name="oldPswd" 
						data-rules="{required:true,minlength:5,maxlength:20}"
						class="control-text">
					</div>
				</div>
			</div>
			<div class="row">
				<div class="control-group span16">
					<label class="control-label">新密码：</label>
					<div class="controls">
						<input type="password"
						name="newPswd" 
						data-rules="{required:true,minlength:5,maxlength:20}"
						class="control-text">
					</div>
				</div>
			</div>
			<div class="row">
				<div class="control-group span16">
					<label class="control-label">重复新密码：</label>
					<div class="controls">
						<input type="password"
						name="newPswd2" 
						data-rules="{required:true,minlength:5,maxlength:20}"
						class="control-text">
					</div>
				</div>
			</div>
			<div class="row">
				<div class="control-group offset3">
					<button id="btnUpdate" type="button" class="button button-primary">修改</button>
					<button id="btnCancel" type="button" class="button">取消</button>
				</div>
			</div>
		</form>
	</div>
</div>
<script type="text/javascript">
(function(){
	
var form = new BUI.Form.Form({srcNode:'#baseForm',autoRender:true});

$('#btnUpdate').click(function(){
	modifyPswd();	
});
$('#btnCancel').click(function(){
	hide();
});
function hide() {
	top.hideModifyPwsd();
}
function modifyPswd() {
	form.valid();
	if(form.isValid()) {
		var data = form.toObject();
		
		data.oldPswd = faultylabs.MD5(data.oldPswd);
		data.newPswd = faultylabs.MD5(data.newPswd);
		data.newPswd2 = faultylabs.MD5(data.newPswd2);
		
		Action.post(ctx + 'updateUserPassword_backuser.do',data,function(e){
			Action.execResult(e,function(){
				hide();
				alert('密码修改成功,请重新登录');
				top.logout();
			});
		});
	}
}

})();
</script>
</body>
</html>