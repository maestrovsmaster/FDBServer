package Servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.ConnectConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Connect.ConnectDBFactory;
import Connect.ConnectFirebird;
import Connect.Firebird;
import Connect.Query;
import LSManager.DBReadWriterLS;
import LSManager.LSControl;
import Utils.TXTEncoding;

/**
 * Servlet implementation class SettingsDBServlet
 */
@WebServlet(name = "/SettingsDBServlet", urlPatterns = { "/SettingsDBServlet" })
public class SettingsDBServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SettingsDBServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("doGet SettingsDBServlet");

		response.setContentType("text/html;charset=UTF-8");
		// request.setCharacterEncoding("windows-1251");

		String ipDB = (String) request.getParameter("ipDB");
		String loginDB = (String) request.getParameter("loginDB");
		String passDB = (String) request.getParameter("passDB");
		String fileDB = (String) request.getParameter("fileDB");
		String key=(String) request.getParameter("key");
		
		String action = request.getParameter("action");
		
		String wsname = (String) request.getParameter("wsname");
		System.out.println("WSNAME === "+wsname);

		ipDB = TXTEncoding.encodeUTF(ipDB);
		loginDB = TXTEncoding.encodeUTF(loginDB);
		passDB = TXTEncoding.encodeUTF(passDB);
		wsname = TXTEncoding.encodeUTF(wsname);
		//fileDB = TXTEncoding.encodeUTF(fileDB);
		action = TXTEncoding.encodeUTF(action);
		
		System.out.println("input =" + ipDB + "-" + fileDB + "-" + loginDB + "-" + passDB);

		ConnectConstants.DB_IP = ipDB;
		ConnectConstants.DB_PATH = fileDB;
		ConnectConstants.DB_LOGIN = loginDB;
		ConnectConstants.DB_PASS = passDB;
		ConnectConstants.WSNAME = wsname;

		
		System.out.println("action = "+action);

		if ("Save".equals(action)) {
			
			System.out.println("Сохранить настройки");
			
			ArrayList<String> settings = new ArrayList<>();
			settings.add(ipDB);
			settings.add(fileDB);
			settings.add(loginDB);
			settings.add(passDB);
			settings.add(wsname);
			
			ConnectConstants.writeSettings(this, settings);
		
		} 
		
			System.out.println("Установить подключение");
			
			String ver ="";
			try {
			 ver = ConnectConstants.getConnect(ConnectConstants.DB_IP, ConnectConstants.DB_PATH, "", "");
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(ver==null) ver="";
			
			
			
			if ("Activate".equals(action)) {
				
				System.out.println("Активировать лицензию!");
				System.out.println("key = "+key);
				//LSControl ls = new LSControl(this);
				
				if(key.length()>=15){
				DBReadWriterLS dr = new DBReadWriterLS();
				dr.setKey(this, key);
				}
			
			
			} 
			
			if ("Delete".equals(action)) {
				
				System.out.println("Активировать лицензию!");
				System.out.println("key = "+key);
				//LSControl ls = new LSControl(this);
				if(key.contains("activated")){
					DBReadWriterLS dr = new DBReadWriterLS();
					dr.setKey(this, "");	
				}
			} 
		
			
			

			response.addHeader("ip", ConnectConstants.DB_IP);
			response.addHeader("path", ConnectConstants.DB_PATH);
			response.addHeader("log", loginDB);
			response.addHeader("pass", passDB);
			response.addHeader("dbversion", ver);
			response.addHeader("wsname", wsname);
			
			 
			 System.out.println("ls control start!");
			    LSControl ls = new LSControl(this);
			  
				if(ls.isLicensed())
				{
					System.out.println("licensed");
					response.addHeader("license", "true");
					response.addHeader("dFrom", ls.getDateFrom());
					response.addHeader("dTo",ls.getDateTo());
					response.addHeader("devCnt",Integer.toString(ls.getDevCount()));
					response.addHeader("key", "activated");
					
				}
				else{
					System.out.println("no licensed");
					response.addHeader("license", "false");
					response.addHeader("dFrom", "");
					response.addHeader("dTo","");
					response.addHeader("devCnt","");
					response.addHeader("key", "");
				}
			 
				
				response.addHeader("id", ls.getHashId());
				  System.out.println("ls control end!");
				  
				  String path = getServletContext().getRealPath("/WEB-INF/IMG_20150608_184528.jpg");
				  
				  System.out.println("img 01 = "+path);
				  response.addHeader("image",path);
			
			
			request.getRequestDispatcher("WellcomeServer.jsp").forward(request, response);
			
	


	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("doPost SettingsDBServlet");

	}

}
