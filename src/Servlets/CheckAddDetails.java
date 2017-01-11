package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import Connect.CheckQueryModel;
import Connect.ConnectDBFactory;
import Connect.Firebird;
import Connect.Query;
import Connect.WS_Options;
import main.ConnectConstants;

/**
 * Servlet implementation class UniversalQueryServlet
 */
@WebServlet(name = "/CheckAddDetails", urlPatterns = { "/CheckAddDetails" })
public class CheckAddDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckAddDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		executeDoPost(request, response);

	}

	private JSONObject executeDoPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("--***--");

		JSONObject obj = null;

		try {

			int length = request.getContentLength();
			byte[] input = new byte[length];
			ServletInputStream sin = request.getInputStream();
			int c, count = 0;
			while ((c = sin.read(input, count, input.length - count)) != -1) {
				count += c;
			}
			sin.close();

			String inputJson = new String(input, StandardCharsets.UTF_8);
			System.out.println("input JSON = " + inputJson);

			System.out.println("add details to check");
			JSONObject returnJsonObject = new JSONObject();

			Firebird conn = ConnectDBFactory.getInstanceFirebird(ConnectConstants.DB_IP, ConnectConstants.DB_PATH);
			Query query = Query.getQuery(conn);

			query.execute("execute procedure sys_connect(1,1);");

			WS_Options ws_Options = WS_Options.getWS_OptionsInstance(ConnectConstants.WSNAME, conn);
			CheckQueryModel checkQueryModel = CheckQueryModel.getCheckQueryModelInstance("0", ws_Options, conn);
			System.out.println(" connect const user id = " + ConnectConstants.USER_ID);
			checkQueryModel.setUserId(ConnectConstants.USER_ID);
			System.out.println(">>1.. ");
			String priceListId = ws_Options.getPRICE_LIST_ID();
			System.out.println(">>2.. ");
			ArrayList<Integer> idList = new ArrayList<>();
			int ans = -3;

			try {
				JSONObject jsonObject = new JSONObject(inputJson);
				System.out.println(">>3.. jsO="+jsonObject.toString());
				int HD_ID = jsonObject.getInt("HD_ID");
				
				System.out.println(">>4.. ");
				JSONArray arr = jsonObject.getJSONArray("DATA");
				System.out.println(">>5.. ");
				for (int i = 0; i < arr.length(); i++) {
					System.out.println(">>6.. ");
					JSONObject jsobj = arr.getJSONObject(i);
					System.out.println(">>7.. ");
					int id = jsobj.getInt("ID");
					System.out.println(">>8.. ");
					double quant = jsobj.getDouble("CNT");
					System.out.println(">>9.. ");
					int PRICE_GRP = jsobj.getInt("PRICE_GRP");
					System.out.println("after of add hdId= "+HD_ID+" id="+id+" cnt="+quant+" price grp= "+PRICE_GRP);
					ans = checkQueryModel.addPosition(HD_ID, id, quant, PRICE_GRP);
					System.out.println("result of add = "+ans);

				}

				System.out.println("doPost: get query!");
				// userInfoJSON = getQuery(pin);

				// JSONArray data = userInfoJSON.getJSONArray("data");

			} catch (Exception e) {
				System.out.println("result of add e= "+e.toString());
			}
			String result = "����������� ������";

			switch (ans) {
			case 1:
				result = "success";
				break;
			case -1:
				result = "�� ������� �����";
				break;
			case -2:
				result = "�� ������� ������������� ��������";
				break;
			case -3:
				result = "����������� ������";
				break;

			default:
				break;
			}

			obj = new JSONObject();
			obj.put("RESULT", result);
			

			// System.out.println("create new check result = "+newCheckId+" num
			// = "+num);

			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");

			PrintWriter out = response.getWriter();

			out.print(obj.toString());

		} catch (Exception e) {
			System.out.println("err  " + e.toString());
			// returnJsonObject.put("status", "err");
			// returnJsonObject.put("details", e.toString());

		}

		return obj;
	}

	private class Detail {
		int id;
		double cnt;

		public Detail(int id, double cnt) {
			// TODO Auto-generated constructor stub
			this.id = id;
			this.cnt = cnt;
		}

		public int getId() {
			return id;
		}

		public double getCnt() {
			return cnt;
		}

	}

}
