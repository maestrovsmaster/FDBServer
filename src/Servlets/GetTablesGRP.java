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
import Connect.WS_Options;
import Utils.TXTEncoding;

/**
 * Servlet implementation class StateServlet
 */
@WebServlet(name = "/GetTablesGRP", urlPatterns = { "/GetTablesGRP" })
public class GetTablesGRP extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetTablesGRP() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println("doGet WSOptionsServlet");
	/*	ArrayList<String> settings = ConnectConstants.readSettings(this);
		System.out.println("settings list = " + settings.toString());
		if (settings.size() >= 4) {
			ConnectConstants.DB_IP = settings.get(0);
			ConnectConstants.DB_PATH = settings.get(1);
			ConnectConstants.DB_LOGIN = settings.get(2);
			ConnectConstants.DB_PASS = settings.get(3);
		}
		if (settings.size() >= 5) {
			ConnectConstants.WSNAME = settings.get(4);
		}else ConnectConstants.WSNAME = "";
		System.out.println("ip=" + ConnectConstants.DB_IP + " path=" + ConnectConstants.DB_PATH);*/
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("doPost GetTablesGRP servlet post");
		
		Firebird conn = ConnectDBFactory.getInstanceFirebird(ConnectConstants.DB_IP, ConnectConstants.DB_PATH);
		if(conn==null) System.out.println("connect null");
		else System.out.println("connect ok");
		
		
		
		JSONObject jsonObject = conn.getTablesHallsJSON();
		
	//	System.out.println(" my tables grp json = "+jsonObject.toString());
		if(jsonObject!=null)System.out.println("tablegrp json ok");
		else System.out.println("table grp json err");
		
		
		try {
			response.setContentType("text/html; charset=UTF-8");
			 response.setCharacterEncoding("UTF-8");
			 PrintWriter out = response.getWriter();
			
			 out.print(jsonObject.toString());
		} catch (Exception e) {
			System.out.println("err send ver = "+e.toString());
		}
		
		
	}

	

}
