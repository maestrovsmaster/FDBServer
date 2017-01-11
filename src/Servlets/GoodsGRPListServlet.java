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

import org.json.JSONArray;
import org.json.JSONObject;

import Connect.CheckQueryModel;
import Connect.ConnectDBFactory;
import Connect.Firebird;
import Connect.PriceListModel;
import Connect.Query;
import Connect.ShiftsQueryModel;
import Connect.WS_Options;
import main.ConnectConstants;

/**
 * Servlet implementation class GoodsGRPListServlet
 */
@WebServlet("/GoodsGRPListServlet")
public class GoodsGRPListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GoodsGRPListServlet() {
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
		System.out.println("doPost: goods grp get query!");
		executeDoPost(request, response);

	}

	private JSONObject executeDoPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject arr = null;

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
			System.out.println("Shift input JSON = " + inputJson);

			JSONObject returnJsonObject = new JSONObject();

			int id = -1;
			int grp_id = -1;

			try {
				JSONObject jsonObject = new JSONObject(inputJson);
				id = jsonObject.getInt("ID");
				grp_id = jsonObject.getInt("GRP_ID");

			} catch (Exception e) {
				// TODO: handle exception
			}

			Firebird conn = ConnectDBFactory.getInstanceFirebird(ConnectConstants.DB_IP, ConnectConstants.DB_PATH);
			// Query query = Query.getQuery(conn);
			WS_Options ws_Options = WS_Options.getWS_OptionsInstance(ConnectConstants.WSNAME, conn);
			ShiftsQueryModel shiftsQueryModel = ShiftsQueryModel.getShiftsQueryModelInstance(ws_Options, conn);
			CheckQueryModel checkQueryModel = CheckQueryModel.getCheckQueryModelInstance("0", ws_Options, conn);
			PriceListModel priceListModel = PriceListModel.getPriceListModelInstance(conn);
			Query query = Query.getQuery(conn);

			int new_id = 0;
			String str = "select dpr.id , dpr.parent_id, dpr.name from dic_price_list_grp dpr where dpr.parent_id=0";

			if (id == -1 && grp_id == -1) {
				try {
					new_id = Integer.parseInt(ws_Options.getPRICE_LIST_ID());
					str = "select dpr.id , dpr.parent_id, dpr.name from dic_price_list_grp dpr where dpr.parent_id="+ new_id;
				} catch (Exception e) {
				}

			}
			if(id==-1&&grp_id>=0){
				str = "select DPR.ID, DPR.PARENT_ID, DPR.NAME from DIC_PRICE_LIST_GRP DPR  where DPR.PARENT_ID = "
						+ "(select  DG.PARENT_ID  from dic_price_list_grp  DG  where DG.ID = "+grp_id+")";
				
			}
			if(id>0&&grp_id>0){
				str = "select dpr.id , dpr.parent_id, dpr.name from dic_price_list_grp dpr where dpr.parent_id="+ id;
				new_id=id;
			}

			System.out.println("grp прайса = " + new_id);

			String strList = "select DG.ID, DG.ARTICLE, DG.NAME, DU.NAME UNIT, "
					+ "(select first 1 DT.PRICE        from DIC_PRICE_LIST_DT DT "
					+ "where DT.HD_ID = DP.ID and             DT.DATE_FROM < current_timestamp        order by DT.DATE_FROM desc) as CENA "
					+ "from DIC_PRICE_LIST DP inner join DIC_GOODS DG on DP.GOODS_ID = DG.ID inner join DIC_UNIT DU on DG.UNIT_ID = DU.ID "
					+ "inner join DIC_PRICE_LIST_GRP DGP on DP.GRP_ID = DGP.ID         where DP.IS_ACTIVE = 1 and "
					+ " DGP.ID = " + new_id + "       order by DG.NAME  ";

			arr = query.getQuery(strList);
			JSONArray jsdata = arr.getJSONArray("data");
			if (jsdata.length() == 0) {
				System.out.println("в прайс листе данных нет. провер€ем группы");

				
						
				arr = query.getQuery(str);
				System.out.println("PRICE GRP json=" + arr.toString());
			}

			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");

			PrintWriter out = response.getWriter();

			out.print(arr.toString());

		} catch (Exception e) {
			System.out.println("err  " + e.toString());

		}

		return arr;
	}

}
