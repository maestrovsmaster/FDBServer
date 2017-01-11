package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import main.ConnectConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Connect.CheckQueryModel;
import Connect.ConnectDBFactory;
import Connect.Firebird;
import Connect.Query;
import Connect.ShiftsQueryModel;
import Connect.WS_Options;

/**
 * Servlet implementation class UniversalQueryServlet
 */
@WebServlet(name = "/Shift_GetOpenCashShiftID", urlPatterns = { "/Shift_GetOpenCashShiftID" })
public class Shift_GetOpenCashShiftID extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Shift_GetOpenCashShiftID() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		executeDoPost( request, response);
					
	}
	
	private JSONObject executeDoPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 JSONObject arr =null;

		try {
		
		 int length = request.getContentLength();
         byte[] input = new byte[length];
         ServletInputStream sin = request.getInputStream();
         int c, count = 0 ;
         while ((c = sin.read(input, count, input.length-count)) != -1) {
             count +=c;
         }
         sin.close();
         
         String inputJson = new String(input, StandardCharsets.UTF_8);
         System.out.println("Shift input JSON = "+inputJson);
         
         JSONObject returnJsonObject=new JSONObject();
         
         try {
        	 JSONObject jsonObject = new JSONObject(inputJson);
				String table= jsonObject.getString("TABLE_ID");
				String user = jsonObject.getString("USER_ID");
				System.out.println("doPost: get query!");
			
			} catch (Exception e) {
				// TODO: handle exception
			}
         
         Firebird conn = ConnectDBFactory.getInstanceFirebird(ConnectConstants.DB_IP, ConnectConstants.DB_PATH);
 		//Query query =  Query.getQuery(conn);
 		WS_Options ws_Options = WS_Options.getWS_OptionsInstance(ConnectConstants.WSNAME, conn);
 		ShiftsQueryModel shiftsQueryModel = ShiftsQueryModel.getShiftsQueryModelInstance(ws_Options, conn);
 		CheckQueryModel checkQueryModel = CheckQueryModel.getCheckQueryModelInstance("0", ws_Options, conn);
 		shiftsQueryModel.setUserId(ConnectConstants.USER_ID);
 		checkQueryModel.setUserId(ConnectConstants.USER_ID);
 		
 		
 		arr = shiftsQueryModel.getOpenCashShiftID();
			
			
		
			response.setContentType("text/html; charset=UTF-8");
			 response.setCharacterEncoding("UTF-8");
		
				PrintWriter out = response.getWriter();
				
				 out.print(arr.toString());
			
		} catch (Exception e) {
			System.out.println("err  "+e.toString());
			
			
		}
       
       
	
		
		return arr;
	}
	

}



