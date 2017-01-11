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
import Connect.WS_Options;

/**
 * Servlet implementation class UniversalQueryServlet
 */
@WebServlet(name = "/CheckCreate", urlPatterns = { "/CheckCreate" })
public class CheckCreate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckCreate() {
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
		
		System.out.println("--***--");
		
		
		
		
		
		 JSONObject obj =null;

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
         System.out.println("input JSON = "+inputJson);
         
         JSONObject returnJsonObject=new JSONObject();
         
         int tableId=0;
         
         try {
        	 JSONObject jsonObject = new JSONObject(inputJson);
				 tableId= jsonObject.getInt("TABLE_ID");
				//String user = jsonObject.getString("USER_ID");
				//System.out.println("doPost: get query!");
			//	userInfoJSON = getQuery(pin);
				
				//JSONArray data = userInfoJSON.getJSONArray("data");
            
				
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}
         
         System.out.println("create new check with table id = "+tableId);
         
         
         Firebird conn = ConnectDBFactory.getInstanceFirebird(ConnectConstants.DB_IP, ConnectConstants.DB_PATH);
 		Query query =  Query.getQuery(conn);
 		
 		query.execute("execute procedure sys_connect(1,1);");
 		
 		WS_Options ws_Options = WS_Options.getWS_OptionsInstance(ConnectConstants.WSNAME, conn);
 		CheckQueryModel checkQueryModel = CheckQueryModel.getCheckQueryModelInstance("0", ws_Options, conn);
 		System.out.println(" connect const user id = "+ConnectConstants.USER_ID);
 		checkQueryModel.setUserId(ConnectConstants.USER_ID);
 		
 		String tableidS="null";
 		if(tableId>0){
 			tableidS=Integer.toString(tableId);
 		}
 		
 		String newCheckId = checkQueryModel.createNewChek(tableidS);
 		
 		String num = checkQueryModel.getCheckNum(newCheckId );
 		
 		
 		
 		obj = new JSONObject();
			obj.put("ID", newCheckId);
			obj.put("NUM", num);
			
			System.out.println("create new check result = "+newCheckId+" num = "+num);
			
		
			response.setContentType("text/html; charset=UTF-8");
			 response.setCharacterEncoding("UTF-8");
		
				PrintWriter out = response.getWriter();
				
				 out.print(obj.toString());
			
		} catch (Exception e) {
			System.out.println("err  "+e.toString());
			//returnJsonObject.put("status", "err");
			//returnJsonObject.put("details", e.toString());
			
		}
       
       
	
		
		return obj;
	}
	
	
	
	
	
	
	
	

}



