<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=utf-8"%>
<html>
<head>
	
	<%@ include file="/common/jsp/common.jsp"%>
	<title>Asyn Tree Demo</title>

</head>
<body style="height:768px">
<div style="height:768px">
	<div class="subnav-subset" id="demoasynTreeMain" style="height:300px">

	    ${asyntreedemo}

	</div>
	
	<div class="zy-shrink-right" id="layout_right" style="width:100%">

	<div class="portlet" dolayout="whole" style="height:100%" style="width:100%">
	
		<div class="portlet-top-l">
			<div class="portlet-top-r">
				<div class="portlet-top-m">
					<span>layout_right</span> 	
				</div>
			</div>
		</div>
		
		<div class="portlet-middle-l">
			<div class="portlet-middle-r" style="width:100%">
				<div class="portlet-middle-m p-content" style="width:99%;overflow-y:hidden;">
				     <span>layout_right</span> 	
				 </div>
			</div>
		</div>
		
		<div class="portlet-bottom-l">
			<div class="portlet-bottom-r">
				<div class="portlet-bottom-m">
					<span>layout_right</span> 	
				</div>
			</div>
		</div>
		
	</div>
</div>
</div>
</body>
</html>
<%@ include file="/common/jsp/tail.jsp"%>
<script type="text/javascript" src='/webstone/treedemo/static/asyntreedemo.js' namespace='AsynTreeDemo'></script>