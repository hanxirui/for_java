

<input id="myDate" name="myDate" class="easyui-datebox" data-options="formatter:myFormatter,parser:myParser"></input>



<script type="text/javascript">
	function myFormatter(date) {

		var y = date.getFullYear();

		var m = date.getMonth() + 1;

		return y + '-' + (m < 10 ? ('0' + m) : m);

	}

	function myParser(s) {

		if (!s) {

			return new Date();

		}

		var ss = s.split('-');

		var y = parseInt(ss[0], 10);

		var m = parseInt(ss[1], 10);

		if (!isNaN(y) && !isNaN(m)) {

			return new Date(y, m - 1);

		}

	}

	$(document).ready(function() {

		if (!$("#myDate").datebox('getValue')) {

			$("#myDate").datebox('setValue', myFormatter(new Date()));

		}

	});
</script>
