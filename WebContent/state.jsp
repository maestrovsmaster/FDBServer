<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
	
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>	

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; UTF-8">
<title>Insert title here</title>
</head>
<body>


	<form action="SettingsDBServlet" method="get"
		enctype="multipart/form-data">


		<font size="2">Параметры подключения к базе данных:</font> <br /> <br />

		<table style="width: 100%;">



			<tr>
				<td>
					<%
						String dbpath = response.getHeader("path");
						String ver = response.getHeader("ver");
					%> <%
 	if (dbpath == null || dbpath.length() == 0 || ver == null || ver.length() == 0) {
 %><font color="maroon">Подключение не установлено!</font><br />  <%=dbpath%><%
 	} else {
 %><font color="green">Подключено: <%=dbpath%></font><br />  ver.<%=ver%><%
 	}
 %>
				</td>
			</tr>
		</table>
		<br /> <a href="index.html">Войти в настройки</a>
	</form>




</body>
</html>