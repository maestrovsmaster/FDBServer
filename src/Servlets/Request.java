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
@WebServlet("/Request")
public class Request extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Request() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("request = "+request);
		System.out.println("response ="+response);
		
		
		
		response.setContentType("text/html;charset=UTF-8");
		
		String query0 = (String) request.getParameter("query");
		String execute = (String) request.getParameter("execute");
		
		
		
		
		System.out.println("quuuu = "+query0);
		/////////////////////
		
		JSONObject arr = new JSONObject();
		
		
		
	//	response.getWriter().print("asdasdasd");
		response.setContentType("text/html; charset=UTF-8");
		 response.setCharacterEncoding("UTF-8");
	
			PrintWriter out = response.getWriter();
			
			
			
			if(query0!=null)
				arr =  getQuery( query0);
			
			else arr = execute(execute);
			
			
			String result = "null";
			if(arr!=null){
				result = arr.toString();
			}
			
			 out.print(result);
			
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		executeDoPost( request, response);
					
	}
	
	private JSONObject executeDoPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
		
		System.out.println("--***--");
		
		
		
		
		
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
         System.out.println("input JSON = "+inputJson);
         
         JSONObject returnJsonObject=new JSONObject();
         
         try {
			JSONObject jsonObject = new JSONObject(inputJson);
			
			try {
				String query = jsonObject.getString("query");
				System.out.println("doPost: get query!");
				arr = getQuery(query);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			try {
				String execute = jsonObject.getString("execute");
				System.out.println("doPost: execute!");
				arr = execute(execute);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
			
			
			
			
			  
			System.out.println("****-*** json = "+arr.toString());
			
			
		
			response.setContentType("text/html; charset=UTF-8");
			 response.setCharacterEncoding("UTF-8");
		
				PrintWriter out = response.getWriter();
				
				 out.print(arr.toString());
			
		} catch (Exception e) {
			System.out.println("err  "+e.toString());
			returnJsonObject.put("status", "err");
			returnJsonObject.put("details", e.toString());
			
		}
       
       
	
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("err2 "+e.toString());
		}
		
		return arr;
	}
	
	
	
	
	
	
	private JSONObject getQuery( String str)
	{
		//ConnectDBFactory connects = new ConnectDBFactory();
		Firebird conn = ConnectDBFactory.getInstanceFirebird(ConnectConstants.DB_IP, ConnectConstants.DB_PATH);
		Query query =  Query.getQuery(conn);
		System.out.println("112");
		JSONObject goodsArray = new JSONObject();
		goodsArray = query.getQuery(str);
		return goodsArray;
	}
	
	
	private JSONObject execute( String str)
	{
		//ConnectDBFactory connects = new ConnectDBFactory();
		Firebird conn = ConnectDBFactory.getInstanceFirebird(ConnectConstants.DB_IP, ConnectConstants.DB_PATH);
		Query query =  Query.getQuery(conn);
		System.out.println("112");
		JSONObject goodsArray = new JSONObject();
		goodsArray = query.execute(str);
		return goodsArray;
	}
	
	

}



