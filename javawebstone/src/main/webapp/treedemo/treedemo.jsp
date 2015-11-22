<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=utf-8"%>
<html>
<head>

<%@ include file="/common/jsp/common.jsp"%>
<title>Tree Demo</title>

</head>
<body style="height: 768px">
	<div style="height: 768px">
		<div class="subnav-subset" id="demoTreeMain" style="height: 300px">
			------------------------------------------------------------
			${treedemo}
			------------------------------------------------------------
		</div>

		<div class="zy-shrink-right" id="layout_right" style="width: 100%">

			<div class="portlet" dolayout="whole" style="height: 100%"
				style="width:100%">

				<div class="portlet-top-l">
					<div class="portlet-top-r">
						<div class="portlet-top-m">
							<span>layout_right</span>
						</div>
					</div>
				</div>

				<div class="portlet-middle-l">
					<div class="portlet-middle-r" style="width: 100%">
						<div class="portlet-middle-m p-content"
							style="width: 99%; overflow-y: hidden;">
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
		<div id="domPool_domHolder_1" style="position:absolute; width: 500px; left: 0; top:0; visibility: hidden;"><iframe render_id="1" class="overlay" frameborder="0" style="opacity: 0.7; position: absolute; visibility: hidden; top: 0px; left: 0px; height: 100%; width: 100%; z-index: -1;"></iframe></div>
	</div>
</body>
</html>
<script type="text/javascript"
	src='/webstone/treedemo/static/treedemo.js' namespace='TreeDemo'></script>