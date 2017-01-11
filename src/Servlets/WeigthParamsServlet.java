package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

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
import Connect.Firebird;
import Connect.Query;

/**
 * Servlet implementation class WeigthParamsServlet
 */
@WebServlet("/WeigthParamsServlet")
public class WeigthParamsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WeigthParamsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("doPost get Weigth params");
		try {
			
		
			JSONObject scanJson= getWeigthScanParameters();
			System.out.println("scanJson="+scanJson.toString());
				PrintWriter out = response.getWriter();
				 out.print(scanJson.toString());
			
		} catch (Exception e) {
			System.out.println("err scan settings  "+e.toString());
			JSONObject returnJsonObject = new JSONObject();
			try {
				returnJsonObject.put("status", "err");
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			try {
				returnJsonObject.put("details", e.toString());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			PrintWriter out = response.getWriter();
			out.print(returnJsonObject.toString());
		}
				
	}
	
	
	private JSONObject getWeigthScanParameters()
	{
		//ConnectDBFactory connects = new ConnectDBFactory();
		Firebird conn = ConnectDBFactory.getInstanceFirebird(ConnectConstants.DB_IP, ConnectConstants.DB_PATH);
		Query query =  Query.getQuery(conn);
		
		System.out.println("112");
		JSONObject scanJson= new JSONObject();
		scanJson = query.getWeigthScanParameters();
		return scanJson;
	}

}



