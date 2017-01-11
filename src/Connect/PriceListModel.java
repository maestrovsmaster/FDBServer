package Connect;


import java.lang.Character.Subset;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Модель прайсов
 * @author v.gorodetskiy
 *
 */
public class PriceListModel {
	
	private Firebird baza;
	
	private boolean all = false;
	
	
	private static PriceListModel priceListModel = null;
	
	public static PriceListModel getPriceListModelInstance(Firebird conn)
	{
		if(priceListModel==null){
			priceListModel=new PriceListModel(conn);
		}
		return priceListModel;
	}
	
	private PriceListModel( Firebird conn)
	{
		baza = conn;
		
	}
	
	private  PriceListModel()	{}
	
	private PriceListModel( Firebird conn, String str)
	{
		baza = conn;
		
		if(!str.trim().equals("")) all= true;
		
	}
	
	
	/**
	  * Берем товары, у которых грп id равна заданной
	  * @param id корневого прайса
	  * @return @SimpleDataGRP
	  */
	private ArrayList<ArrayList<String>>   getPriceListSimpleGRP(String id)
	{
		
		
		
		 ArrayList<ArrayList<Object>> obj = baza.getNomen("select DG.ID, DG.ARTICLE, DG.NAME, DU.NAME UNIT, "+
       "(select first 1 DT.PRICE        from DIC_PRICE_LIST_DT DT "+
        "where DT.HD_ID = DP.ID and             DT.DATE_FROM < current_timestamp        order by DT.DATE_FROM desc) as CENA "+
        "from DIC_PRICE_LIST DP inner join DIC_GOODS DG on DP.GOODS_ID = DG.ID inner join DIC_UNIT DU on DG.UNIT_ID = DU.ID "+
        "inner join DIC_PRICE_LIST_GRP DGP on DP.GRP_ID = DGP.ID         where DP.IS_ACTIVE = 1 and "+
        " DGP.ID = "+id+ "       order by DG.NAME  ");
		
		
		 //System.out.println("obj===="+obj);
		 for(int i=0;i<obj.size();i++)
		 {
			 if(obj.get(i).get(1)==null) obj.get(i).set(1, "");
		 }
		
		 
		 ArrayList<ArrayList<String>> arrsl = objectToString1(obj);
		
			return  arrsl ;	
	}
	
	
	
	
	/**
	 * Берем все товары, находящиеся в родительской папке
	 * @param id родительской папки 
	 * @return
	 */
	public ArrayList<ArrayList<String>> getAllGoodsInPriceGroupId(String id)
	{
		
		if(all)
		{
		ArrayList<ArrayList<String>> pricelist= getTreeGrp(id);
		
		return pricelist;
		}
		else
		{
			return getPriceListSimpleGRP(id);
		}
	}
	
	
	
	private ArrayList<ArrayList<String>> getTreeGrp(String id)
	{
		ArrayList<ArrayList<Object>> obj = baza.getNomen("select  dgp.id, dgp.name from dic_price_list_grp dgp where dgp.parent_id = " +
				 id+"  order by dgp.name");
		
		ArrayList<ArrayList<String>> pricelist = new ArrayList<>();
		
		if(obj.size()!=0)
		{
			for(int i=0;i<obj.size();i++)
			{
				String idd = obj.get(i).get(0).toString();
				//System.out.println(idd);
				pricelist.addAll(getTreeGrp(idd));
				
				
			}
			
			
		}
		else
		{
			 pricelist = getPriceListSimpleGRP(id);
			 
			//System.out.println(pricelist);
			
		}
		return pricelist;
		
	}
	
	
	
	/**
	 * Для групп
	 * @param v
	 * @return
	 */
	private ArrayList<ArrayList<Object>> vectorToArray(Vector<Vector<Object>> v)
	{
		ArrayList<ArrayList<Object>>  arr = new ArrayList<ArrayList<Object>>();
		
		for(int i=0;i<v.size();i++)
		{
			ArrayList<Object> a = new ArrayList<Object>();
			
			a.add(v.get(i).get(0));
			a.add(v.get(i).get(1));
			
			arr.add(a);
		}
		
		return arr;
	}
	
	
	/**
	 * Для групп
	 * @param v
	 * @return
	 */
	private ArrayList<ArrayList<String>> objectToString(ArrayList<ArrayList<Object>> v)
	{
		ArrayList<ArrayList<String>>  arr = new ArrayList<ArrayList<String>>();
		
		for(int i=0;i<v.size();i++)
		{
			
			ArrayList<String> a = new ArrayList<String>();
			a.add(v.get(i).get(0).toString());
			a.add(v.get(i).get(1).toString());
			
			arr.add(a);
		}
		
		return arr;
	}
	
	/**
	 * Для прайса
	 * @param v
	 * @return
	 */
	private ArrayList<ArrayList<Object>> vectorToArray1(Vector<Vector<Object>> v)
	{
		ArrayList<ArrayList<Object>>  arr = new ArrayList<ArrayList<Object>>();
		
		for(int i=0;i<v.size();i++)
		{
			ArrayList<Object> a = new ArrayList<Object>();
			
			a.add(v.get(i).get(0));
			a.add(v.get(i).get(1));
			a.add(v.get(i).get(2));
			a.add(v.get(i).get(3));
			
			//Подкорректируем нули в цене
			String price = v.get(i).get(4).toString();
			int k = price.indexOf(".");
			String newprice = price.substring(0,k+3);
			a.add(newprice);
			
			arr.add(a);
		}
		
		return arr;
	}
	
	
	/**
	 * Для прайса 
	 * @param v
	 * @return
	 */
	private ArrayList<ArrayList<String>> objectToString1(ArrayList<ArrayList<Object>> v)
	{
		ArrayList<ArrayList<String>>  arr = new ArrayList<ArrayList<String>>();
		
		for(int i=0;i<v.size();i++)
		{
			
			ArrayList<String> a = new ArrayList<String>();
			a.add(v.get(i).get(0).toString());
			a.add(v.get(i).get(1).toString());
			a.add(v.get(i).get(2).toString());
			a.add(v.get(i).get(3).toString());
			a.add(v.get(i).get(4).toString());
			
			arr.add(a);
		}
		
		return arr;
	}
	
}
