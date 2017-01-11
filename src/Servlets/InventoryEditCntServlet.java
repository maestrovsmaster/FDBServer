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
 * Servlet implementation class InventoryEditCntServlet
 */
@WebServlet("/InventoryEditCntServlet")
public class InventoryEditCntServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InventoryEditCntServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// response.getOutputStream().println("<h1> Goods list: </h1>");
		// JSONArray arr = getInventorysListList();

		// response.getWriter().print("asdasdasd");
		// response.setContentType("text/html; charset=UTF-8");
		// response.setCharacterEncoding("UTF-8");
		// response.getOutputStream().println("ляляля");
		// PrintWriter out = response.getWriter();

		// out.print(arr.toString());

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// response.getOutputStream().println("<h1> Goods list: </h1>");

		try {

			int length = request.getContentLength();
			byte[] input = new byte[length];
			ServletInputStream sin = request.getInputStream();
			int c, count = 0;
			while ((c = sin.read(input, count, input.length - count)) != -1) {
				count += c;
			}
			sin.close();

			String inputJson = new String(input);
			System.out.println("input JSON = " + inputJson);

			JSONObject returnJsonObject = new JSONObject();

			try {
				JSONObject jsonObject = new JSONObject(inputJson);
				int invId = jsonObject.getInt("invId");
				int goodId = jsonObject.getInt("goodId");
				Object cnt = jsonObject.get("cnt");
				System.out.println("class: " + cnt.getClass().toString());
				double fcnt = -1;
				if (cnt instanceof Double)
					fcnt = (double) cnt;
				if (cnt instanceof Integer) {
					int cn = (int) cnt;
					fcnt = cn;
				}

				String device_id = "";
				String device_name = "";

				try {
					device_id = jsonObject.getString("device_id");
					device_name = jsonObject.getString("device_name");
					
					System.out.println("Dev name = "+device_name +" dev id = "+device_id);
					
					JSONObject devJSON = new JSONObject();
					devJSON.put("device_id", device_id);
					devJSON.put("device_name", device_name);
					
					boolean exist = ConnectConstants.writeOrExistsDevice(this, devJSON);
					System.out.println("InvDtEdit: checkDate = "+ConnectConstants.isCheckDate());
					
					System.out.println("IS EXIST "+exist);
					if(!ConnectConstants.isCheckDate()){
						System.out.println("WRONG DATE!!!");
						returnJsonObject.put("status", "wrong_date");
						//returnJsonObject.put("dev_list",jsonArray);
						
						
					}
					else
					if(exist)//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					{
						returnJsonObject = sendEditInvDt(invId, goodId, fcnt);
					}
					else{
						
						
						
						ArrayList<JSONObject> arrl = ConnectConstants.readDevList(this);
						JSONArray jsonArray = new JSONArray();
						for(JSONObject json:arrl)
						{
							jsonArray.put(json);
						}
						
						returnJsonObject.put("status", "full");
						returnJsonObject.put("dev_list",jsonArray);
						
					}
					
					
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				

			} catch (Exception e) {
				System.out.println("err " + e.toString());
				returnJsonObject.put("status", "err");
				returnJsonObject.put("details", e.toString());

			}

			System.out.println("doPost: get goods!");

			// response.getWriter().print("asdasdasd");
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			// response.getOutputStream().println("ляляля");
			PrintWriter out = response.getWriter();

			out.print(returnJsonObject.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	String ans = "";

	private JSONObject sendEditInvDt(int invId, int goodId, double cnt) {
		//ConnectDBFactory connects = new ConnectDBFactory();
		Firebird conn = ConnectDBFactory.getInstanceFirebird(ConnectConstants.DB_IP, ConnectConstants.DB_PATH);
		Query query = Query.getQuery(conn);

		ConnectConstants.writeMyLog(this, "invId="+invId+"  goodId="+goodId+" cnt="+cnt+" \n\r");
		String ans = query.sendEditInvDt(invId, goodId, cnt, this);
		//ConnectConstants.writeMyLog(this, ans+"\n");
		//ConnectConstants.writeMyLog(this, "----------------------\n\r");

		JSONObject jsonObject = new JSONObject();

		if (ans.length() < 5) {
			double fcnt = query.getDtCNT(invId, goodId);
			System.out.println("URAAAA! fcnt = " + fcnt);
			try {
				jsonObject.put("status", "ok");
				jsonObject.put("cnt", fcnt);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			if (ans.contains("validation error for column OUT_PRICE_CURRENT")) {

				try {
					jsonObject.put("status", "err");
					jsonObject.put("details", "validation error for column OUT_PRICE_CURRENT");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				try {
					jsonObject.put("status", "err");
					jsonObject.put("details", ans.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		return jsonObject;
	}

}
