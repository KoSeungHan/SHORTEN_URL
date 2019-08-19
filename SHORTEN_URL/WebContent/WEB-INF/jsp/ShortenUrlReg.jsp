<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html> <html> <head>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>

<script type="text/javascript">

	function urlShorten() {
		//alert(document.getElementById("urlText").value);
		alert($("#urlText").val());
		//ajaxCall("${ctx}/urlShorten", "", false);
		var urlText = $("#urlText").val();
		
		//var result = ajaxCall("/urlShorten","urlText="+urlText,false);
		//alert(":::::::" + result);
		
		$.ajax({
				type: "post",
				data: {urlText: urlText},
				url: "<%=request.getContextPath()%>/urlShorten",
				dataType: 'text',
				success: function(rv) {
					if(rv == "-1") {
						alert("유효하지 않은 URL 입니다.");
						return;
					}
					$("#urlResult").html("<%=request.getContextPath()%>" + rv);
				},
				error:function(rv){
					alert("fail : " + rv);
				}
			});
		
	}
</script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
      <div>
        <ul>
          <!-- 1. URL 입력
               2. 변환버튼 클릭시 단축 URL 생성
               3. 단축 URL 클릭시 해당 페이지로 Send 
            -->
          <li>
            <input id="urlText" name="urlText" type="text" style="ime-mode: inactive" placeholder="입력URL" value="http://"/>
            <a href="javascript:urlShorten()"> URL Shorten → </a>
            <span id="urlResult" name="urlResult"></span>
          </li>
        </ul>
      </div>
<P>  The time on the server is ${serverTime}. </P>
</body>
</html>