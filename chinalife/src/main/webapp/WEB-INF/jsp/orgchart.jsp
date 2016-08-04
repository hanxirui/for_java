<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp" %>
<jsp:include page="../menu.jsp" >
    <jsp:param name="activeMenu" value="orgchart"/>
</jsp:include>  
<style>
/* Custom chart styling */
.jOrgChart {
  margin                : 10px;
  padding               : 20px;
}

/* Custom node styling */
.jOrgChart .node {
	font-size 			: 14px;
	background-color 	: #35363B;
	border-radius 		: 8px;
	border 				: 5px solid white;
	color 				: #F38630;
	-moz-border-radius 	: 8px;
}
.node p{
	font-family 	: tahoma;
	font-size 		: 10px;
	line-height 	: 11px;
	padding 		: 2px;
}
</style>

   <link rel="stylesheet" href="res/css/jquery.jOrgChart.css"/>
   <link href="res/css/prettify.css" type="text/css" rel="stylesheet" />

    <script type="text/javascript" src="${res}js/prettify.js"></script>
    <script type="text/javascript" src="${res}js/jquery.jOrgChart.js"></script>

      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">

          <!-- Your Page Content Here -->
          <div class="box box-default">
               <div class="box-body">
                   <div id="chart" class="orgChart" style="overflow-x: auto;"></div>
                   
		       </div>
		  </div>
		  <ul id="org" style="display:none">
          </ul>

        </section><!-- /.content -->
      </div><!-- /.content-wrapper -->

      <!-- Main Footer -->
      <footer class="main-footer">
        <!-- Default to the left -->
        <strong>Copyright &copy; 2016</strong>
      </footer>

    </div><!-- ./wrapper -->
    <script>
        jQuery(document).ready(function() {
        	$.getJSON("${ctx}genOrgChart.do", null, function (result) {
        		$("#org").append(result);
        		 $("#org").jOrgChart({
        	            chartElement : '#chart',
        	            dragAndDrop  : false
        	        });
			});
        	
            /* Custom jQuery for the example */
            $("#show-list").click(function(e){
                e.preventDefault();
                
                $('#list-html').toggle('fast', function(){
                    if($(this).is(':visible')){
                        $('#show-list').text('Hide underlying list.');
                        $(".topbar").fadeTo('fast',0.9);
                    }else{
                        $('#show-list').text('Show underlying list.');
                        $(".topbar").fadeTo('fast',1);                  
                    }
                });
            });
            
            $('#list-html').text($('#org').html());
            
            $("#org").bind("DOMSubtreeModified", function() {
                $('#list-html').text('');
                
                $('#list-html').text($('#org').html());
                
                prettyPrint();                
            });
        });
    </script>
  </body>
</html>
