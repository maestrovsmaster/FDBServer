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
 * Servlet implementation class GoodsByBarcodeServlet
 */
@WebServlet("/GoodsByBarcodeServlet")
public class GoodsByBarcodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoodsByBarcodeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// response.getOutputStream().println("<h1> Goods list: </h1>");
		 JSONArray arr = getGoodsList(-1,"","","");
		
		
		
	//	response.getWriter().print("asdasdasd");
		response.setContentType("text/html; charset=UTF-8");
		 response.setCharacterEncoding("UTF-8");
		//	response.getOutputStream().println("ляляля");
			PrintWriter out = response.getWriter();
			
			 out.print(arr.toString());
			
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// response.getOutputStream().println("<h1> Goods list: </h1>");
		
		try {
			
		
		 int length = request.getContentLength();
         byte[] input = new byte[length];
         ServletInputStream sin = request.getInputStream();
         int c, count = 0 ;
         while ((c = sin.read(input, count, input.length-count)) != -1) {
             count +=c;
         }
         sin.close();
         
      //   String item = request.getParameter("param"); 
      //   byte[] bytes = item.getBytes(StandardCharsets.ISO_8859_1);
      //   item = new String(bytes, StandardCharsets.UTF_8);
         
         //request.setContentType("text/html; charset=UTF-8");
        // request.setCharacterEncoding("UTF-8");
         
         
         String inputJson = new String(input, StandardCharsets.UTF_8);
         System.out.println("input JSON = "+inputJson);
         
         JSONObject returnJsonObject=new JSONObject();
         
         try {
			JSONObject jsonObject = new JSONObject(inputJson);
			String barc = jsonObject.getString("barc");
			String article = jsonObject.getString("article");
			String name = jsonObject.getString("name");
			
			int invId=-1;
			try {
				invId=jsonObject.getInt("invId");
			} catch (Exception e) {
				System.out.println("err good* "+e.toString());
			}
			
			
			
			System.out.println("doPost: get goods!");
			 JSONArray arr = getGoodsList(invId,barc,article,name);
			
			
			
		//	response.getWriter().print("asdasdasd");
			response.setContentType("text/html; charset=UTF-8");
			 response.setCharacterEncoding("UTF-8");
			//	response.getOutputStream().println("ляляля");
				PrintWriter out = response.getWriter();
				
				 out.print(arr.toString());
			
		} catch (Exception e) {
			System.out.println("err good "+e.toString());
			returnJsonObject.put("status", "err");
			returnJsonObject.put("details", e.toString());
			
		}
       
       
	
		} catch (Exception e) {
			// TODO: handle exception
		}
					
	}
	
	private JSONArray getGoodsList( int invId, String barc, String art, String name)
	{
		//ConnectDBFactory connects = new ConnectDBFactory();
		Firebird conn = ConnectDBFactory.getInstanceFirebird(ConnectConstants.DB_IP, ConnectConstants.DB_PATH);
		Query query =  Query.getQuery(conn);
		
		System.out.println("112");
		JSONArray goodsArray = new JSONArray();
		goodsArray = query.getGoodList(-1, invId,barc,art,name,-1,-1);
		
		
		
		
		return goodsArray;
	}

}



