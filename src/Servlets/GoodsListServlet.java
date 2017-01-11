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
 * Servlet implementation class GoodsListServlet
 */
@WebServlet("/GoodsListServlet")
public class GoodsListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoodsListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// response.getOutputStream().println("<h1> Goods list: </h1>");
		 JSONArray arr = getGoodsList(-1,-1,-1,-1);
		
		
		
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
         
         
         
         String inputJson = new String(input);
         System.out.println("input JSON = "+inputJson);
         
         JSONObject returnJsonObject=new JSONObject();
         
         try {
			JSONObject jsonObject = new JSONObject(inputJson);
			int grp_id = jsonObject.getInt("grp_id");
			
			int invId=-1;
			try {
				invId=jsonObject.getInt("invId");
			} catch (Exception e) {
				System.out.println("err good* "+e.toString());
			}
			
			int offset=-1;
			try {
				offset=jsonObject.getInt("offset");
			} catch (Exception e) {
				System.out.println("err good* "+e.toString());
			}
			int limit=-1;
			try {
				limit=jsonObject.getInt("limit");
			} catch (Exception e) {
				System.out.println("err good* "+e.toString());
			}
			
			
			System.out.println("doPost: get goods!");
			 JSONArray arr = getGoodsList(grp_id,invId,offset,limit);
			
			
			
		//	response.getWriter().print("asdasdasd");
			response.setContentType("text/html; charset=UTF-8");
			 response.setCharacterEncoding("UTF-8");
			//	response.getOutputStream().println("ляляля");
				PrintWriter out = response.getWriter();
				
				 out.print(arr.toString());
			
		} catch (Exception e) {
			System.out.println("err good> "+e.toString());
			returnJsonObject.put("status", "err");
			returnJsonObject.put("details", e.toString());
			
		}
       
       
	
		} catch (Exception e) {
			// TODO: handle exception
		}
					
	}
	
	private JSONArray getGoodsList(int grp_id, int invId, int offset, int limit)
	{
		//ConnectDBFactory connects = new ConnectDBFactory();
		Firebird conn = ConnectDBFactory.getInstanceFirebird(ConnectConstants.DB_IP, ConnectConstants.DB_PATH);
		Query query =  Query.getQuery(conn);
		
		JSONArray goodsArray = new JSONArray();
		goodsArray = query.getGoodList(grp_id, invId,"","","",offset,limit);
		
		
		
		
		return goodsArray;
	}

}



