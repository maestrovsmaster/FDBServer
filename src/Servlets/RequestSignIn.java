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









import Connect.ConnectDBFactory;
import Connect.Firebird;
import Connect.Query;

/**
 * Servlet implementation class UniversalQueryServlet
 */
@WebServlet("/RequestSignIn")
public class RequestSignIn extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RequestSignIn() {
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
		
		
		
		
		System.out.println("hello RequestSignIn");
		
		
		
		
		
		 JSONObject userInfoJSON =null;

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
         
         try {
			JSONObject jsonObject = new JSONObject(inputJson);
			
			try {
				String pin = jsonObject.getString("PIN");
				System.out.println("doPost: get query!");
				userInfoJSON = getQuery(pin);
				
				JSONArray data = userInfoJSON.getJSONArray("data");
                if(data.length()==0)
                {
                    //showNoUserDialog();
                }
                else {
                    JSONObject jsu = data.getJSONObject(0);
                    int id = jsu.getInt("ID");
                    try{
                    String name= jsu.getString("CODE_NAME");
                    }catch(Exception e){}
                    try{
                    String job = jsu.getString("JOB");
                    }catch(Exception e){}

                    System.out.println("LOGIN. user id = "+id);
                    ConnectConstants.USER_ID=id;
                    System.out.println("LOGIN. conn const usr  id = "+ConnectConstants.USER_ID);
                }
				
				
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			  
			System.out.println("****-*** json = "+userInfoJSON.toString());
			
			
		
			response.setContentType("text/html; charset=UTF-8");
			 response.setCharacterEncoding("UTF-8");
		
				PrintWriter out = response.getWriter();
				
				 out.print(userInfoJSON.toString());
			
		} catch (Exception e) {
			System.out.println("err  "+e.toString());
			returnJsonObject.put("status", "err");
			returnJsonObject.put("details", e.toString());
			
		}
       
       
	
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("err2 "+e.toString());
		}
		
		return userInfoJSON;
	}
	
	
	
	
	
	
	private JSONObject getQuery(String pin)
	{
		//ConnectDBFactory connects = new ConnectDBFactory();
		Firebird conn = ConnectDBFactory.getInstanceFirebird(ConnectConstants.DB_IP, ConnectConstants.DB_PATH);
		Query query =  Query.getQuery(conn);
		System.out.println("112");
		JSONObject json = new JSONObject();
		
		  String str = "select first 1 de.id  , de.code_name ,  " +
          "(select su.id from sec_users su where su.id = de.user_id)  as user_id," +
          "(select su.user_login from sec_users su where su.id = de.user_id)  as login," +
          " (select  jt.name from dic_job_title jt where jt.id = de.job_title_id) as job  " +
          "from dic_employee de where de.pin_code='"+pin+"'";
		
		
		json= query.getQuery(str);
		return json;
	}
	
	
	
	

}



