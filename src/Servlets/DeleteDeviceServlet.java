package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
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
import Connect.Firebird;
import Connect.Query;

/**
 * Servlet implementation class DeleteDeviceServlet
 */
@WebServlet("/DeleteDeviceServlet")
public class DeleteDeviceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteDeviceServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("del dev get");
		
		// response.getOutputStream().println("<h1> Goods list: </h1>");
		
		
		
		
	//	response.getWriter().print("asdasdasd");
		response.setContentType("text/html; charset=UTF-8");
		 response.setCharacterEncoding("UTF-8");
		//	response.getOutputStream().println("ляляля");
			PrintWriter out = response.getWriter();
			
			 out.print("devlist ");
			
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// response.getOutputStream().println("<h1> Goods list: </h1>");
		
		System.out.println("del dev post");
		
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
         
         String device_id = "";
			String device_name = "";

			try {
				JSONObject jsonObject = new JSONObject(inputJson);
				device_id = jsonObject.getString("device_id");
				device_name = jsonObject.getString("device_name");
				
				System.out.println("kfkfkfk*** Dev name = "+device_name +" dev id = "+device_id);
				
				JSONObject devJSON = new JSONObject();
				devJSON.put("device_id", device_id);
				devJSON.put("device_name", device_name);
				
				JSONObject returnJsonObject = new JSONObject();
				
				boolean existForDeleted = ConnectConstants.deleteDevice(this, devJSON);
				
				
				//System.out.println("IS EXIST "+exist);
				
				/*if(exist)//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				{
					//returnJsonObject = sendEditInvDt(invId, goodId, fcnt);
				}
				else{
					ArrayList<JSONObject> arrl = ConnectConstants.readDevList(this);
					JSONArray jsonArray = new JSONArray();
					for(JSONObject json:arrl)
					{
						jsonArray.put(json);
					}
					
					//returnJsonObject.put("status", "full");
					//returnJsonObject.put("dev_list",jsonArray);
				}
				
				*/
				
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				// response.getOutputStream().println("ляляля");
				PrintWriter out = response.getWriter();
				out.print(returnJsonObject.toString());
				
			} catch (Exception e) {
				// TODO: handle exception
			}
       
       
	
		} catch (Exception e) {
			// TODO: handle exception
		}
					
	}
	
	

}



