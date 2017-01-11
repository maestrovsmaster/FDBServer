package Connect;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Модель группы товаров
 * 
 * @author Vasily
 *
 */
public class PriceGRPModel {

	Firebird baza;
	
	private static PriceGRPModel priceGRPModel=null;
	
	public static PriceGRPModel getPriceGRPModelInstance(Firebird conn)
	{
		if(priceGRPModel==null){
			priceGRPModel = new PriceGRPModel(conn);
		}
		return priceGRPModel;
	}
	
	

	private PriceGRPModel(Firebird conn) {
		baza = conn;

	}

	/**
	 * Берем папки, у которых грп name равна заданной
	 * 
	 * @param id
	 *            корневого прайса
	 * @return ид + наименование
	 */
	public ArrayList<ArrayList<String>> getPricelistGRPforId(String id) {

		String query = "select  dgp.id, dgp.name from dic_price_list_grp dgp where dgp.parent_id = " + id
				+ "  order by dgp.name";
		ArrayList<ArrayList<Object>> obj = baza.getNomen(query);

		System.out.println(query);

		return objectToString(obj);
	}

	/**
	 * Берем папки на уровень выше от заданной. Формирует запрос группы прайс
	 * листа назад.
	 * 
	 * @param p
	 * @param s
	 *            = 1
	 * @return
	 */
	public ArrayList<ArrayList<String>> zaprosNazad(String p) {
		String query = "";

		query = "select nf.id,   nf.name from  dic_price_list_grp  nf where nf.parent_id = "
				+ " (select    ig.parent_id  from  dic_price_list_grp ig where ig.id ="
				+ "(select  dg.parent_id from dic_price_list_grp dg where dg.id =" + p
				+ " ) )AND nf.ID <> 0 order by nf.name";

		ArrayList<ArrayList<Object>> obj = baza.getNomen(query);

		// ArrayList<ArrayList<Object>> arr = vectorToArray(obj);

		return objectToString(obj);
	}

	private ArrayList<ArrayList<Object>> vectorToArray(Vector<Vector<Object>> v) {
		ArrayList<ArrayList<Object>> arr = new ArrayList<ArrayList<Object>>();
		for (int i = 0; i < v.size(); i++) {

			ArrayList<Object> a = new ArrayList<Object>();

			a.add(v.get(i).get(0));
			a.add(v.get(i).get(1));

			arr.add(a);
		}

		return arr;
	}

	private ArrayList<ArrayList<String>> objectToString(ArrayList<ArrayList<Object>> v) {
		ArrayList<ArrayList<String>> arr = new ArrayList<ArrayList<String>>();

		for (int i = 0; i < v.size(); i++) {
			ArrayList<String> a = new ArrayList<String>();
			a.add(v.get(i).get(0).toString());
			a.add(v.get(i).get(1).toString());

			arr.add(a);
		}

		return arr;
	}

}// end main class
