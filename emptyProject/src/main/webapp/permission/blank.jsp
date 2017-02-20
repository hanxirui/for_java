<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <title>后台管理</title>
 </head>
<body>
  
  <div class="container">
    <div class="row">
      <form id="searchForm" class="form-horizontal">
        <div class="row">         
      	  <div class="control-group span8">
            <label class="control-label">用户名：</label>
            <div class="controls">
              <input type="text" class="control-text" name="usernameSch">
            </div>
          </div>    
          <div class="control-group">
            <div class="controls">
              <button  type="button" id="schBtn" class="button button-primary">搜索</button>
            </div>
          </div>
        </div>
      </form>
    </div>
    <hr>
    <div class="search-grid-container">
      <div id="grid"></div>
    </div>
  </div>
 
<script type="text/javascript">
$(function(){
	
});
</script>

</body>
</html>
