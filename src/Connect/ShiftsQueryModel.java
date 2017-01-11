package Connect;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShiftsQueryModel {
	
Firebird baza;
	
	

	private String default_ws_id = ""; //текущей рабочей станции
	
	private String default_employee_name; //мнемокод сотрудника по умолчанию
	private String default_employee_id = "1"; //id сотрудника по умолчанию
	
	private String default_check_grp=""; //группа счетов по умолчанию
	
	
	private String default_cash_subvision_id=""; //подразделение кассы
	private String default_shift_cash_id=""; //подразделение открытой смены кассы
	
	 WS_Options ws_Options; //ќбъект опций рабочей станций
	 
	 
	 private static ShiftsQueryModel shiftsQueryModel =null;

	 public static ShiftsQueryModel getShiftsQueryModelInstance( WS_Options ws_Options,Firebird conn)
	 {
		 if(shiftsQueryModel ==null){
			 shiftsQueryModel = new ShiftsQueryModel( ws_Options, conn);
		 }
		 return shiftsQueryModel;
	 }
		
	
	 private ShiftsQueryModel(WS_Options ws_Options,Firebird conn)
	 {
		 
			
			this.ws_Options = ws_Options;
			
			baza = conn;
		 
	 }
	
	/**
	 * »д текущей смены кассы 
	 * @return
	 */
	public JSONObject  getOpenCashShiftID()
	{
	//id подразделени€ кассы данной р.с.
	default_cash_subvision_id = ws_Options.getDef_cash_subvision_id();
	
	default_ws_id = ws_Options.getWs_id();
	
	//id открытой смены кассы 
	JSONObject obj = baza.getJSONNomen("select  js.id  from jor_shifts js where  js.dt_stop is null and js.subdivision_id = "+default_cash_subvision_id);
	
	JSONArray data;
	try {
		data = obj.getJSONArray("data");
		if(data.length()>0){
			JSONObject js = data.getJSONObject(0);
			int id;
			id = js.getInt("ID");
			System.out.println("open shift id = "+id);
		}
		
		
	} catch (JSONException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	
	
	
	return obj;
	}
	
	/**
	 * Ѕерем все открытые чеки текущей смены по указанному ид сотрудника
	 * @param employee_id
	 * @return
	 */
	public ArrayList<ArrayList<String>> getOpenChecks(String employee_id)
	{
		ArrayList<ArrayList<String>> arrlist = new ArrayList<ArrayList<String>>();
		
		ArrayList<ArrayList<Object>> obj = baza.getNomen("select  jc.id, jc.num, (select dt.name from dic_tables dt where dt.id = jc.table_id) as tabl  from   jor_checks jc  where jc.doc_state = 0   and jc.shifts_id = "
										+getOpenCashShiftID()+ "  and jc.employee_id= "+employee_id+" order by jc.id");
		for(int i=0;i<obj.size();i++)
		{
			ArrayList<String> arrl = new ArrayList<String>();
			Object st =  obj.get(i).get(0);
			Integer iii= (Integer) st;
			Integer.toString(iii);
			String chek_id = Integer.toString(iii);
			arrl.add(chek_id);
			
			st =  obj.get(i).get(1);
			String num = (String) st;
			
			arrl.add(num);
			
			if(obj.get(i).get(2)!=null){
			st =  obj.get(i).get(1);
			String table = (String) st;
			arrl.add(table);
			}
			else arrl.add("");
			
			
			arrl.add(getTotalSumm(chek_id));
			
			
			arrlist.add(arrl);
		}
		
		return arrlist;
	}
	
	/**
	 * Ѕерем все открытые чеки текущей смены по указанному ид столикап
	 * @param employee_id
	 * @return
	 */
	public ArrayList<ArrayList<String>> getOpenChecksTable(String table_id)
	{
		ArrayList<ArrayList<String>> arrlist = new ArrayList<ArrayList<String>>();
		
		ArrayList<ArrayList<Object>> obj = baza.getNomen("select  jc.id, jc.num, (select dt.name from dic_tables dt where dt.id = jc.table_id) as tabl  from   jor_checks jc  where jc.doc_state = 0   and jc.shifts_id = "
										+getOpenCashShiftID()+ "  and jc.table_id= "+table_id+" order by jc.id");
		for(int i=0;i<obj.size();i++)
		{
			ArrayList<String> arrl = new ArrayList<String>();
			Object st =  obj.get(i).get(0);
			Integer iii= (Integer) st;
			Integer.toString(iii);
			String chek_id = Integer.toString(iii);
			arrl.add(chek_id);
			
			st =  obj.get(i).get(1);
			String num = (String) st;
			
			arrl.add(num);
			
			if(obj.get(i).get(2)!=null){
			st =  obj.get(i).get(1);
			String table = (String) st;
			arrl.add(table);
			}
			else arrl.add("");
			
			
			arrl.add(getTotalSumm(chek_id));
			
			
			arrlist.add(arrl);
		}
		
		return arrlist;
	}
	
	
	
	/**
	 * 
	 * @return получаем общую сумму чека
	 */
	public String getTotalSumm(String id)
	{
		String sum="";
		double summa=0;
		
		
		 ArrayList<ArrayList<Object>> obj =new ArrayList<>();
			Object st =  null;
			int iii;
			
			//“€нем номер чека
			obj = baza.getNomen("select jc.sum_  from jor_checks_dt jc where jc.hd_id = "+ id +"  and jc.refuse_time is null ");
			for(int i = 0; i<obj.size(); i ++)
			{
		    st =  obj.get(i).get(0);
		   BigDecimal sum_  =(BigDecimal) st;
		   
		    summa +=Double.parseDouble((sum_.toString()));
		   
		    //System.out.println("KLASS SUMMI = "+st.getClass());
			}
		
		

		
		//for(PoziciaCheka c : spisok)
		//	summa+=c.getPrice()*c.getKolvo();
		sum = Double.toString(summa);
		return sum;
		//return "4";
	}
	
	public void setUserId(int id)
	{
		// default_employee_name = Integer.toString(id);
		 default_employee_id =  Integer.toString(id);
	}
	
	

}
