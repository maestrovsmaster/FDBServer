<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<meta http-equiv="Content-Type" content="text/html; UTF-8">
<title>Insert title here</title>
<STYLE type="text/css">
 
   #div {
     width = 100%;
     margin = 0 auto;
    }
  
  </STYLE>
</head>
<body>


	<h1>
		<font color="navy" face="Arial">Wellcome Barcode Server!!!</font>
	</h1>

	<h3>Настройки</h3>
	Параметры подключения к базе данных:
	<br />
	<form action="SettingsDBServlet" method="get"
		enctype="multipart/form-data">

		<div>

			<div class="auth_cell_titles">Сервер:</div>
		</div>
		<input id="div" name="ipDB" type="text"
			value="<%String ip = response.getHeader("ip");
			if (ip == null || ip.length() == 0) {%>127.0.0.1<%} else {%><%=ip%><%}%>" />
		</div>
		<div>Путь:(Только латиница)</div>
		<div>
			<input name="fileDB" type="text"
				value="<%String path = response.getHeader("path");
			if (path == null || path.length() == 0) {%><%} else {%><%=path%><%}%>" />
		</div>

		</div>
		<div>
			<div class="auth_cell_titles">Логин</div>
			<div class="auth_cell">
				<input name="loginDB" class="auth" type="text" 
					value="<%String log = response.getHeader("log");
			if (log == null || log.length() == 0) {%>SYSDBA<%} else {%><%=log%><%}%>" />
			</div>
		</div>
		<div>
			<div class="auth_cell_titles">Пароль</div>
			<td class="auth_cell"><input name="passDB" 
				class="auth" type="passwordDB"
				value="<%String pass = response.getHeader("pass");
			if (pass == null || pass.length() == 0) {%>masterkey<%} else {%><%=pass%><%}%>" />
		</div>
		</div>
		
		<div>
			<div class="auth_cell_titles">Название рабочей станции, к которой подключаться(Латиницей). Не обязательно.</div>
			<div class="auth_cell">
				<input name="wsname" class="auth" type="text" 
					value="<%String wsname = response.getHeader("wsname");
			if (wsname == null || wsname.length() == 0) {%>Android<%} else {%><%=wsname%><%}%>" />
			</div>
		</div>

		<div>
			<input type="submit" name="action" value="Установить подключение" />
		</div>
		<div>
			<input type="submit" name="action" value="Save" />
		</div>



		</div>

		<h5>
			<%
				String dbversion = response.getHeader("dbversion");
				if (dbversion == null || dbversion.length() == 0) {
			%>
			Подключение не установлено.
			<%
				} else {
			%><font size="2" color="green" face="Arial"> Подключено!
				Версия базы данных: <%=dbversion%></font>
			<%
				}
			%>
		</h5>


		<br />

		<h3>Лицензия</h3>
		<%
			String key = response.getHeader("key");
			String idTitle = "ID";
			String color = "black";
			String value = "Activate";
			String btColor = "green";
			String style1 = "visibility:visible";
			String style2 = "visibility:hidden";
			String btStr = "";
			if (key.contains("activated")) {
				style1 = "visibility:hidden";
				style2 = "visibility:visible";
				idTitle = "Лицензия активирована!";
				color = "green";
				value = "Delete";
				btColor = "red";
				btStr = "Осторожно! Ключ активации будет удален,\n\r программа сканирования перестанет работать!!!";
			}
		%>

		<div>
			<div>
				<div class="auth_cell_titles">
					<font size="3" color=<%=color%> face="Arial"> <%=idTitle%></font>
				</div>
				<div class="auth_cell_titles" style=<%=style1%>>
					<%
						String id = response.getHeader("id");
					%><%=id%>
				</div>

			</div>
			<div>
				<div class="auth_cell_titles" style=<%=style1%>>Key</div>
				<div class="auth_cell">
					<input name="key" style="width: 100%;<%=style1%>" class="auth"
						type="text" value="<%=key%>" />
				</div>
			</div>


			<div>
				<div class="auth_cell_titles" style=<%=style2%>>Ключ
					действителен до:</div>
				<div class="auth_cell_titles" style=<%=style2%>>
					<%
						String dTo = response.getHeader("dTo");
					%><%=dTo%>
				</div>
			</div>
			<div>
				<div class="auth_cell_titles" style=<%=style2%>>Кол-во
					устройств:</div>
				<div class="auth_cell_titles" style=<%=style2%>>
					<%
						String devCnt = response.getHeader("devCnt");
					%><%=devCnt%>
				</div>
			</div>
		</div>

		<input type="submit" name="action" value=<%=value%>
			style="color: <%=btColor%>" /><font size="2" color="orange"
			face="Arial"> <%=btStr%></font>

	</form>




</body>
</html>