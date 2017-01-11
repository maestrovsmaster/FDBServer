package Servlets;

import java.io.IOException;
import java.io.PrintWriter;

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
 * Servlet implementation class InventorysListServlet
 */
@WebServlet("/InventorysListServlet")
public class InventorysListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InventorysListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// response.getOutputStream().println("<h1> Goods list: </h1>");
		 JSONArray arr = getInventorysListList();
		
		
		
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
		
		/*try {
			
		
		 int length = request.getContentLength();
         byte[] input = new byte[length];
         ServletInputStream sin = request.getInputStream();
         int c, count = 0 ;
         while ((c = sin.read(input, count, input.length-count)) != -1) {
             count +=c;
         }
         sin.close();
         
         String grp_id = new String(input);
         System.out.println("GRP_ID = "+grp_id);
		
		
		int igrp_id=-1;
		try {
			igrp_id=Integer.parseInt(grp_id);
		} catch (Exception e) {
			// TODO: handle exception
		}
		*/
		
		System.out.println("doPost: get goods!");
				 JSONArray arr = getInventorysListList();
				
				
				
			//	response.getWriter().print("asdasdasd");
				response.setContentType("text/html; charset=UTF-8");
				 response.setCharacterEncoding("UTF-8");
				//	response.getOutputStream().println("ляляля");
					PrintWriter out = response.getWriter();
					
					 out.print(arr.toString());
		
					
	}
	
	private JSONArray getInventorysListList()
	{
		//ConnectDBFactory connects = new ConnectDBFactory();
		Firebird conn = ConnectDBFactory.getInstanceFirebird(ConnectConstants.DB_IP, ConnectConstants.DB_PATH);
		Query query =  Query.getQuery(conn);
		
		JSONArray invArray = new JSONArray();
		invArray = query.getInventorynList();
		
		
		
		
		return invArray;
	}

}



