<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="permission.common.RMSContext"%>
<%@page import="com.alibaba.fastjson.JSON"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<c:choose>
	<c:when test="<%=RMSContext.getInstance().isCurrentUserHaveSuperPermission() %>">
		<script type="text/javascript">
		(function(){
			RightUtil.check = function(a,b){
				return true;
			}			
		})();
		</script>
	</c:when>
	<c:otherwise>
		<script type="text/javascript">
		(function(){
			var dataStr = '<%=JSON.toJSON(RMSContext.getInstance().getCurrentUserPermission()) %>';
			var data = jQuery.parseJSON(dataStr);
			
			var rightData = {};
			for(var key in data) {
				var operateCodes = [];
				var userOperations = data[key];
				
				for(var i=0,len=userOperations.length;i<len;i++) {
					operateCodes.push(userOperations[i].operateCode);
				}
				
				rightData[key] = operateCodes;
			}
			/*结构:
			key/value -> srId/operateCodes
			var permissionData = {
				{"1":["view","update"],"2":["del"]}
				,{"2":["view","del"]}
			}
			*/
			RightUtil.setData(rightData);
		})();
		</script>
	</c:otherwise>
</c:choose>
