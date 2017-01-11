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
@WebServlet(name = "/WSOptionsServlet", urlPatterns = { "/WSOptionsServlet" })
public class WSOptionsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WSOptionsServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ArrayList<String> settings = ConnectConstants.readSettings(this);
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
		System.out.println("ip=" + ConnectConstants.DB_IP + " path=" + ConnectConstants.DB_PATH);
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("doPost WSOptionsServlet");
		
		Firebird conn = ConnectDBFactory.getInstanceFirebird(ConnectConstants.DB_IP, ConnectConstants.DB_PATH);
		if(conn==null) System.out.println("connect null");
		else System.out.println("connect ok");
		WS_Options ws_Options = WS_Options.getWS_OptionsInstance(ConnectConstants.WSNAME, conn);
		if(ws_Options!=null) System.out.println("ws opt inst = ok");
		else System.out.println("ws opt inst = null");
		
		String wsname = ws_Options.getDefault_ws_name();
		String wsid = ws_Options.getWs_id();
		
		JSONObject jsonObject = new JSONObject();
		
		try {
			jsonObject.put("wsid", wsid);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			jsonObject.put("wsname", wsname);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
