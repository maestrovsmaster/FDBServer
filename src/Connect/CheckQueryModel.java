package Connect;


import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JPanel;






/**
 * Класс, отвечающий за создание и редактирование чеков
 * @author v.gorodetskiy
 *
 */
public class CheckQueryModel {
	
	Firebird baza;
	
	
	private String check_num=""; //номер чека
	private String data = ""; // дата чека
	private String table = ""; //стол
	private String employee = ""; //официант
	
	
	private String default_ws_name = ""; //имя текущей рабочей станции
	private String default_ws_id = ""; //текущей рабочей станции
	
	private String default_employee_name; //мнемокод сотрудника по умолчанию
	private String default_employee_id = "1"; //id сотрудника по умолчанию
	
	private String default_check_grp=""; //группа счетов по умолчанию
	
	private String current_time = ""; //дата чека
	private String default_client=""; //клиент по умолчанию
	private String default_cash_subvision_id=""; //подразделение кассы
	private String default_shift_cash_id=""; //подразделение открытой смены кассы
	
	WS_Options ws_Options; //Объект опций рабочей станций
	
	
	private static CheckQueryModel checkQueryModel=null;
	
	public static CheckQueryModel getCheckQueryModelInstance( String user,WS_Options ws_Options,Firebird baza2)
	{
		if(checkQueryModel==null){
			checkQueryModel=new CheckQueryModel( ws_Options, baza2);
		}
		return checkQueryModel;
	}
	 
		
	/**
	 * 
	 * @param user id сотрудника
	 * @param ws_Options
	 * @param baza2
	 */
	 private CheckQueryModel( WS_Options ws_Options,Firebird baza2)
	 {
		
			
			this.ws_Options = ws_Options;
			
			baza = baza2;
		 
	 }
	 
	
	/**
	  * Создает новый чек, автоматически генерируя все необходимые параметры шапки
	  * @param tableId может быть 'null'
	  * @return check id
	  */
	 public String createNewChek(String tableId)
	 {
		 System.out.println("=== create check table id = "+tableId);
		
		   ArrayList<ArrayList<Object>> obj =new ArrayList<ArrayList<Object>>();
			Object st =  null;
			int iii;
			
			//берем ид сотрудника
			//default_employee_id =ws_Options.getDef_employee_id();
			
			//----Берем параметры данной Р.С.
			//id группы счетов по умолчанию
			default_check_grp = ws_Options.getDEF_PATH_CHECKS();
			
			//id клиента по умолчанию
			default_client=ws_Options.getDef_client();
					
			//id подразделения кассы данной р.с.
			default_cash_subvision_id = ws_Options.getDef_cash_subvision_id();
			
			default_ws_id = ws_Options.getWs_id();
			
			
			//id открытой смены кассы 
			obj = baza.getNomen("select  js.id  from jor_shifts js where  js.dt_stop is null and js.subdivision_id = "+default_cash_subvision_id);
			st =  obj.get(0).get(0);
			 iii= (Integer) st;
			default_shift_cash_id = Integer.toString(iii);
			//System.out.println("ид смены="+default_shift_cash_id);
			
			//Генерируем номер чека
			obj = baza.getNomen("select  gen_id ( num_jor_checks ,1 ) from RDB$DATABASE");		
			st =  obj.get(0).get(0);
			 Long lg= (Long) st;
			String stg=  Long.toString(lg);
			check_num=stg;
			
			System.out.println("id sotrudnika = "+default_employee_id);
			//System.out.println("номер чека="+check_num);
			
			//Создаем активный коннект к базе
					//baza.setNomen("execute procedure sys_connect(1,"+default_ws_id+")");
			baza.setNomen("execute procedure sys_connect(1,1)");
			//Создаем чек
					baza.setNomen("execute procedure z$jor_checks_i(null,"+
							default_check_grp+",'now',"+check_num+", 0, "+default_shift_cash_id+","+default_employee_id+","+default_cash_subvision_id+","
							+default_client+","+tableId+", null,null)");
			
			
					
			//узнаем ид новосозданного чека
			obj = baza.getNomen("select  first 1 jc.id from jor_checks jc  where jc.subdivision_id= "+default_cash_subvision_id+"  order by jc.id desc"); 
			st =  obj.get(0).get(0);
			iii= (Integer) st;
			String check_id=Integer.toString(iii);
			System.out.println("ид чека="+check_id);
			
			//System.out.println("22222222222222222222222222222222222222222222222");
			
			return check_id;
	 }
	 
	 
	 /**
	  * Выгружает чек из базы по указанному ID
	  * @param id
	  *        0 - good name
		     * 1 - good unit
		     * 2 - price
		     * 3 - cnt
		     * 4 - id
		     * 5 - order time
		     * 6 - need print refuse
		     * 7 - refuse time
	  */
	 public  ArrayList<ArrayList<String>> updateCheck(String id)
	 {
		 ArrayList<ArrayList<String>> arrl = new ArrayList<>();
		 
		 
		 ArrayList<ArrayList<Object>> obj =new ArrayList<ArrayList<Object>>();
	
		 
		 String query = "select (select DG.NAME from DIC_GOODS DG  where DG.ID = JD.GOODS_ID) as GOOD,"+
				    "(select DU.NAME   from DIC_GOODS DG, DIC_UNIT DU   where DU.ID = DG.UNIT_ID and DG.ID = JD.GOODS_ID) as UNIT,"+
					   " JD.PRICE, JD.CNT, JD.ID, JD.ORDER_TIME, JD.NEED_PRINT_REFUSE, JD.REFUSE_TIME, JD.ID"+
					   " from JOR_CHECKS_DT JD where JD.HD_ID = "+id+" order by JD.ID";
			
		// System.out.println(query);
		    /**
		     * 0 - good name
		     * 1 - good unit
		     * 2 - price
		     * 3 - cnt
		     * 4 - id
		     * 5 - order time
		     * 6 - need print refuse
		     * 7 - refuse time
		     */
		    obj = baza.getNomen(query);
		    
		    
		    
		    
		    for(int i=0;i<obj.size();i++)
		    {
		    	ArrayList<String> arr = new ArrayList<>();
		    	
		    	
		    	String name = (String) obj.get(i).get(0);
		    	
		    	String unit = (String) obj.get(i).get(1);
		    	
		    	//System.out.println("Имя позиции! "+name);
		    	BigDecimal price =  (BigDecimal) obj.get(i).get(2);
		    	//System.out.println(price);
		    	BigDecimal cnt =    (BigDecimal) obj.get(i).get(3);
		    	
		    	
		    	//System.out.println(cnt);
		    	
		    	//а теперь узнаем состояние чека:
		    	
		    	Timestamp order_time =    (Timestamp) obj.get(i).get(5); //время заказа 
		    	Integer NEED_PRINT_REFUSE = (Integer) obj.get(i).get(6); //печатать ли отказ
		    	//System.out.println("UUUUUUUUUUUUUUUUUUUUUUUUUUUUU="+ obj.get(i).get(5).getClass());
		    	Timestamp REFUSE_TIME =    (Timestamp) obj.get(i).get(7); //время отказа
		    	
		    	Integer dtid = (Integer) obj.get(i).get(4); //id детализации
		    	
		    	String dt1 = Integer.toString(dtid);
		    	//System.out.println("ИД Детализашки "+dtid);
		    	
		    	Double price1=Double.parseDouble(price.toString());
		    	String price11  = Double.toString(price1);
		    	
		    	Double cnt1=Double.parseDouble(cnt.toString());
		    	String cnt11 = Double.toString(cnt1);
		    	
		    	int stan = 0;
		    	if(order_time!=null&&REFUSE_TIME==null&&NEED_PRINT_REFUSE==0) stan = 1;
		    	if(NEED_PRINT_REFUSE!=0&&REFUSE_TIME==null){stan = 2;}
		    	if(REFUSE_TIME!=null) stan = 3;
		    	
		    	String stan1 = Integer.toString(stan);
		    	
		    	double sum1 = cnt1*price1;
		    	String sum = Double.toString(sum1); 
		    	
		    	
		    	arr.add(dt1);
		    	arr.add(name);
		    	arr.add(unit);
		    	arr.add(cnt11);
		    	arr.add(price11);
		    	arr.add(sum);
		    	arr.add(stan1);
		    
		    	arrl.add(arr);
		    }
		    
		  
			return arrl;
			
	 }
	 
	 public  ArrayList<ArrayList<String>> updateCheckForPrint(int id)
	 {
		 ArrayList<ArrayList<String>> arrl = new ArrayList<>();
		 
		 
		 ArrayList<ArrayList<Object>> obj =new ArrayList<ArrayList<Object>>();
	
			
		    /**
		     * 0 - good name
		     * 1 - good unit
		     * 2 - price
		     * 3 - cnt
		     * 4 - id
		     * 5 - order time
		     * 6 - need print refuse
		     * 7 - refuse time
		     */
		    obj = baza.getNomen("select (select DG.NAME from DIC_GOODS DG  where DG.ID = JD.GOODS_ID) as GOOD,"+
		    "(select DU.NAME   from DIC_GOODS DG, DIC_UNIT DU   where DU.ID = DG.UNIT_ID and DG.ID = JD.GOODS_ID) as UNIT,"+
		   " JD.PRICE, JD.CNT, JD.ID, JD.ORDER_TIME, JD.NEED_PRINT_REFUSE, JD.REFUSE_TIME, JD.ID"+
		   " from JOR_CHECKS_DT JD where JD.HD_ID = "+id+" order by JD.ID");
		    
		    
		    
		    
		    for(int i=0;i<obj.size();i++)
		    {
		    	ArrayList<String> arr = new ArrayList<>();
		    	
		    	
		    	String name = (String) obj.get(i).get(0);
		    	
		    	String unit = (String) obj.get(i).get(1);
		    	
		    	//System.out.println("Имя позиции! "+name);
		    	BigDecimal price =  (BigDecimal) obj.get(i).get(2);
		    	//System.out.println(price);
		    	BigDecimal cnt =    (BigDecimal) obj.get(i).get(3);
		    	
		    	
		    	//System.out.println(cnt);
		    	
		    	//а теперь узнаем состояние чека:
		    	
		    	Timestamp order_time =    (Timestamp) obj.get(i).get(5); //время заказа 
		    	Integer NEED_PRINT_REFUSE = (Integer) obj.get(i).get(6); //печатать ли отказ
		    	//System.out.println("UUUUUUUUUUUUUUUUUUUUUUUUUUUUU="+ obj.get(i).get(5).getClass());
		    	Timestamp REFUSE_TIME =    (Timestamp) obj.get(i).get(7); //время отказа
		    	
		    	Integer dtid = (Integer) obj.get(i).get(4); //id детализации
		    	
		    	String dt1 = Integer.toString(dtid);
		    	//System.out.println("ИД Детализашки "+dtid);
		    	
		    	Double price1=Double.parseDouble(price.toString());
		    	String price11  = Double.toString(price1);
		    	
		    	Double cnt1=Double.parseDouble(cnt.toString());
		    	String cnt11 = Double.toString(cnt1);
		    	
		    	int stan = 0;
		    	if(order_time!=null&&REFUSE_TIME==null&&NEED_PRINT_REFUSE==0) stan = 1;
		    	if(NEED_PRINT_REFUSE!=0&&REFUSE_TIME==null){stan = 2;}
		    	if(REFUSE_TIME!=null) stan = 3;
		    	
		    	String stan1 = Integer.toString(stan);
		    	
		    	double sum1 = cnt1*price1;
		    	String sum = Double.toString(sum1); 
		    	
		    	
		    	arr.add(dt1);
		    	arr.add(name);
		    	arr.add(unit);
		    	arr.add(cnt11);
		    	arr.add(price11);
		    	arr.add(sum);
		    	arr.add(stan1);
		    
		    	if(stan!=3) arrl.add(arr);//для печати на принтер игнорируем отказные позиции
		    }
		    
		  
			return arrl;
			
	 }
	 
	
	 
		/**
		 *  Добавляем новую позицию
		 * @param id pozicii
		 * @param chek_id id cheka
		 * @return int
		 * @param grp_id id прайслиста
		 *   -2  нет подразделения списания
		 *   -1  не открыта смена
		 *    1  все ок
		 *   
		 */
		public int addPosition(String id, String chekId, String price_grp_id)
		{
					
					//берем ид подразделения списания товара
					ArrayList<ArrayList<Object>> obj =baza.getNomen("select dp.subdivision_id from dic_price_list_grp dp where dp.id= "+price_grp_id);
					
					String subdivision_id="null";
					Object st=null;
					int iii=0;
					if(obj.size()>0){
					 st =  obj.get(0).get(0);
					 iii= (Integer) st;
					  subdivision_id=Integer.toString(iii); 
					// System.out.println("id списания"+subdivision_id );
					}
					//берем ид подразделения печати товара
					 String print_subdivision_id="";
					 obj = baza.getNomen("select dp.print_subdivision_id from dic_price_list_grp dp where dp.id= "+price_grp_id);
					// System.out.println("id списания "+obj.get(0).get(0).getClass());
					// String str = (String)(obj.get(0).get(0));
					 if(obj.size()>0){
					 st =  obj.get(0).get(0);
					if(st!=null) iii= (Integer) st;
					 print_subdivision_id=Integer.toString(iii);
					 }
					 else
					 {
						 print_subdivision_id="null";
					 }
						 
					 
					 
					 obj = baza.getNomen("select first 1 PLD.PRICE	 from DIC_PRICE_LIST PL, DIC_PRICE_LIST_DT PLD "+
							 "where PL.GOODS_ID = "+id+" and     PLD.HD_ID = PL.ID and "+
							 "PLD.DATE_FROM < current_timestamp	 order by PLD.DATE_FROM desc");
					 
					 BigDecimal price =  (BigDecimal) obj.get(0).get(0);
					// java.util.Vector prc = obj;
					 
					 Double priced=Double.parseDouble(price.toString());
				    	String price1 = Double.toString(priced);
				    	
					 
					// System.out.println("price="+st.getClass());
					 
					//Создаем активный коннект к базе
					//baza.setNomen("execute procedure sys_connect(1,"+default_ws_id+")");
					baza.setNomen("execute procedure sys_connect(1,1)");
				 //Создаем детализацию чека
					baza.setNomen("execute procedure z$jor_checks_dt_i(null,"+
								chekId+","+id+","+1+","+price1+", "+price1+","+subdivision_id+","+print_subdivision_id+",'"+""+"')");
					
				
			
			return 1;
			
		}
		
		
		/**
		 *  Добавляем новую позицию с заданным количеством
		 * @param cnt количество
		 * @param id pozicii
		 * @param chek_id id cheka
		 * @param price_grp_id  id прайслиста
		 * @return int
		 *   -2  нет подразделения списания
		 *   -1  не открыта смена
		 *    1  все ок
		 *   
		 */
		public int addPosition(int chekId,int id,  double cnt, int price_grp_id )
		{
			System.out.println("hello add position");
			
			//проверка на наличие подразделения списания у данной позиции
		/*	String query = "select dp.subdivision_id from dic_price_list_grp dp "+
					"where	dp.id=( select dpg.grp_id from dic_price_list dpg where "+
					"dpg.goods_id = "+id+")";
			
			
			ArrayList<ArrayList<Object>> obj =baza.getNomen(query);
			
			//Проверка на наличие открытой смены по подразделению списания
			/*ArrayList<ArrayList<Object>> objsh = baza.getNomen("select first 1 js.id from jor_shifts js where js.dt_stop is null  and "+
			"js.subdivision_id = (select dp.subdivision_id from dic_price_list_grp dp "+
			"where dp.id = (select dl.grp_id from dic_price_list dl "+
			"where dl.goods_id = "+id+" ) )");*/
			
			//if(obj.size()!=0)
			{
				//if(objsh.size()!=0)
				{
					
					//берем ид прайслиста товара
					/*ArrayList<ArrayList<Object>> obj = baza.getNomen("select dl.grp_id from dic_price_list dl where dl.goods_id = "+id);
					 Object st =  obj.get(0).get(0);
					 int iii= (Integer) st;
					 String price_grp_id = Integer.toString(iii); 
					//System.out.println("id прайса"+price_grp_id );*/
					
					//берем ид подразделения списания товара
					String qur1="select dp.subdivision_id from dic_price_list_grp dp where dp.id= "+price_grp_id;
					System.out.println("----1*="+qur1);
					 ArrayList<ArrayList<Object>> obj =baza.getNomen(qur1);
					 System.out.println("----2*");
					 Object st =  obj.get(0).get(0);
					 System.out.println("----3*="+st.getClass().toString());
					int iii= (Integer) st;
					System.out.println("----31*");
					 String subdivision_id=Integer.toString(iii); 
					 System.out.println("----32*");
					 System.out.println("id списания"+subdivision_id );
					 
					//берем ид подразделения печати товара
					 obj = baza.getNomen("select dp.print_subdivision_id from dic_price_list_grp dp where dp.id= "+price_grp_id);
					 st =  obj.get(0).get(0);
					 iii= (Integer) st;
					 String print_subdivision_id=Integer.toString(iii); 
					 System.out.println("id печати"+print_subdivision_id );
					 System.out.println("----4*");
					 
					 obj = baza.getNomen("select first 1 PLD.PRICE	 from DIC_PRICE_LIST PL, DIC_PRICE_LIST_DT PLD "+
							 "where PL.GOODS_ID = "+id+" and     PLD.HD_ID = PL.ID and "+
							 "PLD.DATE_FROM < current_timestamp	 order by PLD.DATE_FROM desc");
					 
					 BigDecimal price =  (BigDecimal) obj.get(0).get(0);
					// java.util.Vector prc = obj;
					 
					 Double priced=Double.parseDouble(price.toString());
				    	String price1 = Double.toString(priced);
				    	
					 
					 System.out.println("price="+st.getClass());
					 
					//Создаем активный коннект к базе
					//baza.setNomen("execute procedure sys_connect(1,"+default_ws_id+")");
					baza.setNomen("execute procedure sys_connect(1,1)");
				 //Создаем детализацию чека
					baza.setNomen("execute procedure z$jor_checks_dt_i(null,"+
								chekId+","+id+","+cnt+","+price1+", "+price1+","+subdivision_id+","+print_subdivision_id+",'"+"с софта"+"')");
					
					
					
				} //else return -1;
				
			
			}
			//else return -2;
			
			return 1;
			
		}
	 
		String idi;
		//RefuseReasonFrame  rfframe;
	 
	
	
	/**
	 * Изменить количество позиции
	 * @param id детализации
	 * @param kolvo количество
	 */
	public void setKolvo(String id , Double kolvo)
	{
		ArrayList<ArrayList<Object>> obj = baza.getNomen("select JD.ORDER_TIME from JOR_CHECKS_DT JD where JD.ID = "+id);
		
		if(obj!=null)
		if(obj.get(0).get(0)==null)
		{
			System.out.println("set kolvo");
			baza.setNomen("update jor_checks_dt jd set jd.cnt = "+kolvo.toString()+"  where id = "+id);
		}
	}
	
	
	
	
	/**
	 * Берем список для заказа
	 * @param id чека
	 * Возвращает лист со списком наименования, кол-ва, подразделения печати каждой позиции, примечанием
	 */
	public ArrayList<ArrayList<String>> getZakazList(String id)
	{
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		
		ArrayList<ArrayList<Object>> obj0 = baza.getNomen("select JD.ID from JOR_CHECKS_DT JD where JD.HD_ID = "+id);
		
		if(obj0!=null)
			
				for(int i=0;i<obj0.size();i++)
				{
				ArrayList<ArrayList<Object>> obj = baza.getNomen("select JD.ORDER_TIME, " +
						"(select dg.name from dic_goods dg where dg.id = JD.GOODS_ID) as name, " +
						" JD.CNT, (select ds.name from dic_subdivision ds where ds.id = JD.SUBDIVISION_ID) as SUBDIV, JD.SUBDIVISION_ID , JD.DESCR from JOR_CHECKS_DT JD where JD.ID = "+
						obj0.get(i).get(0)+" order by JD.ID");
			
				if(obj.get(0).get(0)==null)
				{
						//baza.setNomen("execute procedure sys_connect(1,1)");
				
						//baza.setNomen("update jor_checks_dt jd set jd.order_time = current_timestamp where jd.id = "+obj0.get(i).get(0));
				
						ArrayList<String> arr = new ArrayList<String>();
						arr.add(obj.get(0).get(1).toString());
						arr.add(obj.get(0).get(2).toString());
						arr.add(obj.get(0).get(3).toString());
						arr.add(obj.get(0).get(4).toString());
						if(obj.get(0).get(5)!=null)arr.add(obj.get(0).get(5).toString());//модификатор
												
						list.add(arr);
				}
				}
		
		return list;
	}
	
	/**
	 * Делаем в базе на заказ
	 * @param id чека
	 */
	public void setZakaz(String id, String subdivId)
	{
		ArrayList<ArrayList<Object>> obj0 = baza.getNomen("select JD.ID from JOR_CHECKS_DT JD where JD.HD_ID = "+id);
		if(obj0!=null)
			
			for(int i=0;i<obj0.size();i++)
			{
				ArrayList<ArrayList<Object>> obj = baza.getNomen("select JD.ORDER_TIME, " +
						"(select dg.name from dic_goods dg where dg.id = JD.GOODS_ID) as name, " +
						" JD.CNT, (select ds.name from dic_subdivision ds where ds.id = JD.SUBDIVISION_ID) as SUBDIV, JD.SUBDIVISION_ID  from JOR_CHECKS_DT JD where JD.ID = "+
						obj0.get(i).get(0)+" order by JD.ID");
			
				if(obj.get(0).get(0)==null)
				{
						baza.setNomen("execute procedure sys_connect(1,1)");
						baza.setNomen("update jor_checks_dt jd set jd.order_time = current_timestamp where jd.subdivision_id = "+subdivId+" and jd.id = "+obj0.get(i).get(0));
				}
				
			}
	}
	
	
	/**
	 * Берем список для отказа
	 * @param id чека
	 * @param subdivId ид подразделения
	 */
	public ArrayList<ArrayList<String>> getOtkazList(String id)
	{
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		
		ArrayList<ArrayList<Object>> obj0 = baza.getNomen("select JD.ID from JOR_CHECKS_DT JD where JD.HD_ID = "+id);
		
		if(obj0!=null)
			
				for(int i=0;i<obj0.size();i++)
				{
					ArrayList<ArrayList<Object>> obj = baza.getNomen("select JD.ORDER_TIME, jd.NEED_PRINT_REFUSE, jd.REFUSE_TIME," +
							"(select dg.name from dic_goods dg where dg.id = JD.GOODS_ID) as name, " +
							" JD.CNT, (select ds.name from dic_subdivision ds where ds.id = JD.SUBDIVISION_ID) as SUBDIV , JD.SUBDIVISION_ID," +
							"(select dr.name from dic_refuse_reason dr  where dr.id = JD.REFUSE_REASON_ID) as REF from JOR_CHECKS_DT JD where JD.ID = "+
							obj0.get(i).get(0)+" order by JD.ID");
					
				    //а теперь узнаем состояние чека:
					//System.out.println(obj);
					Integer NEED_PRINT_REFUSE = (Integer) obj.get(0).get(1);
				    Timestamp REFUSE_TIME =    (Timestamp) obj.get(0).get(2); //время отказа 
			
				if(NEED_PRINT_REFUSE==1&&REFUSE_TIME==null)
				{
						//baza.setNomen("execute procedure sys_connect(1,1)");
						//baza.setNomen("update jor_checks_dt jd set jd.REFUSE_TIME = current_timestamp where jd.id = "+obj0.get(i).get(0));
						
						ArrayList<String> arr = new ArrayList<String>();
						arr.add(obj.get(0).get(3).toString());
						arr.add(obj.get(0).get(4).toString());
						arr.add(obj.get(0).get(5).toString());
						arr.add(obj.get(0).get(6).toString());//ид подразделения
						if(obj.get(0).get(7)==null){
							arr.add("");
						}
						else{
							arr.add(obj.get(0).get(7).toString());//причина отказа
						}
						
						list.add(arr);
				}
			}
		System.out.println("otkaz="+list);
		return list;
	}
	
	/**
	 * Делаем в базе отказ
	 * @param id ид чека
	 * @param subdivId ид подразделения
	 */
	public void setOtkaz(String id, String subdivId)
	{
		ArrayList<ArrayList<Object>> obj0 = baza.getNomen("select JD.ID from JOR_CHECKS_DT JD where JD.HD_ID = "+id);
		
		if(obj0!=null)
			
				for(int i=0;i<obj0.size();i++)
				{
					ArrayList<ArrayList<Object>> obj = baza.getNomen("select JD.ORDER_TIME, jd.NEED_PRINT_REFUSE, jd.REFUSE_TIME," +
							"(select dg.name from dic_goods dg where dg.id = JD.GOODS_ID) as name, " +
							" JD.CNT, (select ds.name from dic_subdivision ds where ds.id = JD.SUBDIVISION_ID) as SUBDIV , JD.SUBDIVISION_ID from JOR_CHECKS_DT JD where JD.ID = "+
							obj0.get(i).get(0)+" order by JD.ID");
					
				    //а теперь узнаем состояние чека:
					//System.out.println(obj);
					Integer NEED_PRINT_REFUSE = (Integer) obj.get(0).get(1);
				    Timestamp REFUSE_TIME =    (Timestamp) obj.get(0).get(2); //время отказа 
			
				if(NEED_PRINT_REFUSE==1&&REFUSE_TIME==null)
				{
						baza.setNomen("execute procedure sys_connect(1,1)");
						baza.setNomen("update jor_checks_dt jd set jd.REFUSE_TIME = current_timestamp where jd.subdivision_id = "+subdivId+" and jd.id = "+obj0.get(i).get(0));
						
				}
			}
	}
	
	
	/**
	 * Печать пречека 
	 * @param id чека
	 */
	public void setPrechek(String id)
	{
		String query = "select jd.PRINT_TIME  from  jor_checks jd where jd.id = "+id;
		ArrayList<ArrayList<Object>>   obj = baza.getNomen(query);
	   
	    	//а теперь узнаем состояние чека:
		//System.out.println(obj);
		//Integer NEED_PRINT_REFUSE =    (Integer) obj.get(0).get(1);
	    	Timestamp PRINT_TIME =    (Timestamp) obj.get(0).get(0); //время печати чека 
	
		//System.out.println("Время заказа: "+order_time);
		
		if(PRINT_TIME!=null){ 
			System.out.println("Ошибка! Счет уже закрыт!!!");
		}
		else
		{
		
		System.out.println("Пречек по счету номер "+ id + " напечатан");
		
		//Создаем активный коннект к базе
		baza.setNomen("execute procedure sys_connect(1,1)");
		//Изменяем кол-во в детализации чека 
		baza.setNomen("update jor_checks jd set jd.PREPRINT_TIME = current_timestamp where jd.id = "+id);
		}
	}
	
	
	/**
	 *  печатаем чек
	 */
	public void setChek(int id)
	{
		
		String query = "select jd.PRINT_TIME  from  jor_checks jd where jd.id = "+id;
		ArrayList<ArrayList<Object>>   obj = baza.getNomen(query);
	   
	    	//а теперь узнаем состояние чека:
		//System.out.println(obj);
		//Integer NEED_PRINT_REFUSE =    (Integer) obj.get(0).get(1);
	    Timestamp PRINT_TIME =    (Timestamp) obj.get(0).get(0); //время печати чека 
	
				
		System.out.println("Пречек по счету номер "+ id + " напечатан");
		
				
		//Создаем кассовую проводку
		
		//Узнаем ид папки касс
		String def_cash_grp = ws_Options.getDEF_PATH_CASH();
		
		
		
		
		//Создаем активный коннект к базе
		baza.setNomen("execute procedure sys_connect(1,1)");
		
		//Создаем кассовую проводку
		baza.setNomen("" +
			"insert into JOR_CASH(ID, GRP_ID, DATE_TIME, NUM, DOC_TYPE, DOC_STATE, SUBDIVISION_ID, DOC_SUM, CONTRAGENT_TYPE,  EMPLOYEE_ID, CLIENT_ID, IO_ITEM_ID)  " +
				" values(null, "+ws_Options.getDEF_PATH_CASH() +" ,current_timestamp, null, 0, 0 , "+ws_Options.getDef_cash_subvision_id() +" , "+getTotalSumm(id) + " , 2 ,  "+default_employee_id+" , " + ws_Options.getDef_client()+" , null )");
		
		//узнаем ид новосозданой кассовой проводки
		obj = baza.getNomen("select  first 1 jc.id from jor_cash jc  where jc.subdivision_id= "+ws_Options.getDef_cash_subvision_id()+"  order by jc.id desc"); 
		Object st =  obj.get(0).get(0);
		int iii= (Integer) st;
		String cash_id=Integer.toString(iii);
		System.out.println("ид проводки по кассе="+cash_id);
		
		//Добавляем детализацию к кассе
		baza.setNomen("" +
				"insert into JOR_CASH_DT(ID, HD_ID, DOC_TYPE, DOC_ID, HT_CHECK_ID, BILL_IN_ID , BILL_OUT_ID , FT_CHECK_ID , PAYROLL_ID , SLA_ID, SUM_ROW )  " +
					" values(null, "+cash_id +" , 0 ," +id + ",null ,null ,null ,null ,null ,null, "+getTotalSumm(id)+ ")");
		
		//узнаем ид новосозданой кассовой детализации
		obj = baza.getNomen("select  first 1 jc.id from jor_cash_dt jc  where jc.hd_id= "+cash_id+"  order by jc.id desc"); 
		st =  obj.get(0).get(0);
		iii= (Integer) st;
		String cash_dt_id=Integer.toString(iii);
		System.out.println("ид детализаци проводки по кассе="+cash_dt_id);
				
		//проводим текущую кассовую операцию*/
			
		baza.setNomen("update jor_cash jd set  jd.DOC_STATE = 1 where jd.id = "+cash_id);
		
		
		baza.setNomen("update jor_checks jd set jd.PRINT_TIME = current_timestamp , jd.DOC_STATE = 1 where jd.id = "+id);
		
	}
	
	/**
	 * Удаляем чек
	 * @param id
	 */
	public String deleteChek(String id)
	{
		baza.setNomen("execute procedure sys_connect(1,1)");
		String ans =baza.setNomen("delete from jor_checks jd where jd.id = "+id);
		
		return ans;
	}
	
	
	/**
	 * 
	 * @return получаем общую сумму чека
	 */
	public String getTotalSumm(int id)
	{
		String sum="";
		double summa=0;
		
		
		 ArrayList<ArrayList<Object>> obj =new ArrayList<ArrayList<Object>>();
			Object st =  null;
			int iii;
			
			//Тянем номер чека
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
	
	/**
	 * Получаем номер чека по айди
	 * 
	 */
	public String getCheckNum(String id)
	{
	//Тянем номер чека
		 ArrayList<ArrayList<Object>> obj = baza.getNomen("select jc.num from  jor_checks jc where jc.id = "+id);
		 Object st =  obj.get(0).get(0);
     String num =(String) st;
	return num;
   
	}
	
	/**
	 * Получаем имя клиента в чеке 
	 * @param id чека
	 * @return
	 */
	public String getCheckClient(String id)
	{
		System.out.println("id="+id);
		ArrayList<ArrayList<Object>> client = baza.getNomen("select (select dc.code_name from dic_client dc where dc.id =jc.client_id ) as CLIENT from jor_checks jc where jc.id ="+id);
		
		return (String)client.get(0).get(0);
		
	}
	
	/**
	 * Получаем id клиента в чеке 
	 * @param id чека
	 * @return
	 */
	public String getCheckClientId(String id)
	{
		System.out.println("id="+id);
		ArrayList<ArrayList<Object>> client = baza.getNomen("select jc.client_id  from jor_checks jc where jc.id ="+id);
		
		return Integer.toString((Integer)client.get(0).get(0));
		
	}
	
	
	/**
	 * Получаем имя стола в чеке 
	 * @param id чека
	 * @return
	 */
	public String getCheckTable(String id)
	{	String table="";
		ArrayList<ArrayList<Object>> obj = baza.getNomen("select (select dc.name from dic_tables dc where dc.id =jc.table_id ) as CLIENT from jor_checks jc where jc.id ="+id);
		if(obj.size()>0)
			if(obj.get(0).get(0)!=null)
			table=(String)obj.get(0).get(0);
		return table;
		
	}
	
	/**
	 * Получаем сумму позиции детализации по ее айди
	 * @param id
	 * @return
	 */
	public String getSummCheckDT(String id)
	{
		 ArrayList<ArrayList<Object>> obj = baza.getNomen("select jd.cnt from jor_checks_dt jd  where jd.id = "+id);
		 Object st =  obj.get(0).get(0);
		 BigDecimal sum  =(BigDecimal) st;
		   
		 double sum1 =Double.parseDouble((sum.toString()));
		 
		String sum2 = Double.toString(sum1);
		 
		return sum2;
		
	}
	
	/**
	 * Получаем ид товара позиции в детализации по ее айди
	 * @param id
	 * @return
	 */
	public String getGoodIdCheckDT(String id)
	{
		 ArrayList<ArrayList<Object>> obj = baza.getNomen("select (select dg.id from dic_goods dg where dg.id = jd.goods_id) from jor_checks_dt jd  where jd.id = "+id);
		 Object st =  obj.get(0).get(0);
		
		 int goods_id = (Integer) st;
		String goods_ids = Integer.toString(goods_id);
		 
		return goods_ids;
		
	}
	
	
	/**
	 * Проверяем, делима ли позиция детализации
	 * @param id детализации
	 * @return
	 */
	public boolean isDivisibleCheckDT(String id)
	{
		
		String goods_id = getGoodIdCheckDT(id);
		
		 ArrayList<ArrayList<Object>> obj = baza.getNomen("select dg.is_divisible from dic_goods dg where dg.id = "+goods_id);
		 Object st =  obj.get(0).get(0);
		 
		 //System.out.println("Delimost "+st.getClass());
		 
		 Integer is = (Integer) st;
		 
		 if(is==1) return true;
		 
		 else return false;
		
	}
	
	
	/**
	 * Возвращает состояние чека.
	 *  0 - не заказан
	 *  1 - заказан
	 *  2 - помечен на отказ
	 *  3 - отказан
	 * @param id ид чека
	 * @param dt_id ид детализации
	 * @return
	 */
	public int getStanOfDT(String id, String dt_id)
	{
		
		ArrayList<ArrayList<Object>>  obj = baza.getNomen("select   JD.ORDER_TIME, JD.NEED_PRINT_REFUSE, " +
				"   JD.REFUSE_TIME, JD.ID from JOR_CHECKS_DT JD where  JD.id = "+dt_id+" and JD.HD_ID = "+id);
				    	//а теперь узнаем состояние чека:
				    	Timestamp order_time =    (Timestamp) obj.get(0).get(0); //время заказа 
				    	Integer NEED_PRINT_REFUSE = (Integer) obj.get(0).get(1); //печатать ли отказ
				    	Timestamp REFUSE_TIME =    (Timestamp) obj.get(0).get(2); //время отказа
				    	
				    	int stan = 0;
				    	if(order_time!=null&&REFUSE_TIME==null&&NEED_PRINT_REFUSE==0) stan = 1;
				    	if(NEED_PRINT_REFUSE!=0&&REFUSE_TIME==null){stan = 2;}
				    	if(REFUSE_TIME!=null) stan = 3;
				    	
				    	
				    
		return stan;
	}
	
	
	
	
	/**
	 * Изменяем для детализации hd_id
	 * @param id детализации
	 * @param hd_id id чека
	 * @return
	 */
	public void changeHdId(String id, String hd_id)
	{
		baza.setNomen("execute procedure sys_connect(1,1)");
		baza.setNomen("update jor_checks_dt jd  set jd.hd_id="+hd_id+"  where jd.id = "+id);
	}
	
	 
	 
	/**
	 * Проверяет, существует ли чек с указанным ид
	 * @param id chek
	 * @return
	 */
	 public boolean checkID(String id)
	 {
		
		 ArrayList<ArrayList<Object>> obj = baza.getNomen("select jc.id from jor_checks jc where jc.id= "+id); 
		 
		 if(obj.size()!=0) return true;
		 else return false;
		 
	 }
	 
	 /**
	  * Проверяет, не проведен ли чек с указанным ид
	  * @param id чека
	  * @return
	  */
	public boolean checkDocState(String id)
	{
		ArrayList<ArrayList<Object>> obj = baza.getNomen("select jc.doc_state from jor_checks jc where jc.id= "+id); 
		if(obj.size()!=0)
		{
			if(obj.get(0).get(0).toString().equals("0")) return true;
			else return false;
		}		
		else 	return false;
		
		
	}
	
	/**
	 * Устанавливаем столик к чеку
	 * @param checkId ид чека
	 * @param tableId ид стола
	 */
	public void setTableChek(String checkId, String tableId)
	{
		baza.setNomen("update jor_checks jc set jc.table_id= "+tableId+" where jc.id= "+checkId);
	}
	
	/**
	 * Устанавливаем клиента к чеку
	 * @param checkId ид чека
	 * @param tableId ид стола
	 */
	public void setClientChek(String checkId, String clientId)
	{
		baza.setNomen("update jor_checks jc set jc.client_id= "+clientId+" where jc.id= "+checkId);
	}
	
	
	public String getEmployee(String checkId)
	{
		String query = "select DE.CODE_NAME		from DIC_EMPLOYEE DE		where DE.ID = (select JC.EMPLOYEE_ID	   from JOR_CHECKS JC	   where JC.ID= "+checkId+")";
		ArrayList<ArrayList<Object>> obj = baza.getNomen(query);
		String name ="";
			if(obj!=null) name = (String)	obj.get(0).get(0);
			return name;
		
	}
	
	
	/**
	 * Возвращает список модификаторов для позиции чека
	 * @param checkId ид детализации
	 * @return
	 */
	public ArrayList<ArrayList<String>> getModifiers(String dtId)
	{
		ArrayList<ArrayList<String>> arrl = new ArrayList<>();
		
		String query = "select dm.name from DIC_PRICE_LIST_GRP_MOD DGM, dic_modifier dm "+
               " where DGM.PRICE_GROUP_ID = (select JD.PRICE_GROUP_ID  from JOR_CHECKS_DT JD "+
                                          " where JD.ID = "+dtId+" ) and dm.id = dgm.modifier_id";
		//System.out.println("query = "+query);
		ArrayList<ArrayList<Object>> obj = baza.getNomen(query); 
		
		for(int i=0;i<obj.size();i++)
		{
			ArrayList<String> ar = new ArrayList<>();
			ar.add((String)obj.get(i).get(0));
			ar.add((String)obj.get(i).get(0));
			
			arrl.add(ar);
		}
		
		return arrl;
	}
	
	/**
	 * Возвращает наименование товара по ИД детализации чека
	 * @param dtId ид детализации чека
	 * @return
	 */
	public String getGoodsNameForCheckDtId(String dtId)
	{
		String query = "select DG.NAME from DIC_GOODS DG  where DG.ID = (select JT.GOODS_ID  from JOR_CHECKS_DT JT where JT.ID = "+dtId+" )";
		ArrayList<ArrayList<Object>> obj = baza.getNomen(query); 
		return (String)obj.get(0).get(0);
		
	}
	
	/**
	 * 
	 * @param modifier строка модификатора
	 * @param dtId ид детализации чека
	 */
	public void setModifier(String modifier, String dtId)
	{
		String query = "update jor_checks_dt jc set jc.descr = '"+modifier+"' where jc.id= "+dtId;
		baza.setNomen(query);
	}
	
	/**
	 * Возвращает модификаторы по детализации чека
	 * @param dtId ид детализации чека
	 * @return
	 */
	public String getModifiersOfChekDt(String dtId)
	{
		String query = "select jc.descr from jor_checks_dt jc where jc.id = "+dtId;
		ArrayList<ArrayList<Object>> obj = baza.getNomen(query); 
		return (String)obj.get(0).get(0);
	}
	
	/**
	 * Проверяет, заказаны ли позиции чека. Если все позиции заказаны - возвращает true
	 * @param id ид чека
	 * @return
	 */
	public boolean isPrintedOrders(String id)
	{
		boolean b = true;
		ArrayList<ArrayList<Object>> obj = baza.getNomen("select jd.order_time from jor_checks_dt jd where jd.hd_id= "+id); 
		
		for(int i=0;i<obj.size();i++)
		{
			if(obj.get(i).get(0)==null) return false;
		}
		
		return b;
	}
	
	/**
	 * Проверяет, был ли напечатан пречек. Если да- возвращает true
	 * @param id ид чека
	 * @return
	 */
	public boolean isPrintedPrechek(String id)
	{
		boolean b = true;
		ArrayList<ArrayList<Object>> obj = baza.getNomen("select jd.preprint_time from jor_checks jd where jd.id= "+id);
		System.out.println("пречек = "+obj);
			if(obj.get(0).get(0)==null) return false;
		
		return b;
	}
	
	/**
	 * Отменяем пречек
	 * @param id
	 */
	public void cancelPrechek(String id)
	{
		baza.setNomen("update jor_checks jc set jc.preprint_time = null where jc.id = "+id);
	}
	
	
	/**
	 * Возвращает ширину ленты из БД для принтера, с указанным именем
	 * @param printerName имя принтера
	 * @return ширина ленты
	 */
	public int getPrintPaperWight(String printerName)
	{
		String query = "select first 1 ps.file_name from cfg_print_settings ps where ps.printer_name = '"+printerName+"' and ps.ws_id = "+ws_Options.getWs_id();
		
		//System.out.println(query);
		
		ArrayList<ArrayList<Object>> obj = baza.getNomen(query);
		int w =0;
		if(obj.size()>0 )w= Integer.parseInt((String)obj.get(0).get(0));
		else w=120;
		
		return w;
	}
	
	
	/**
	 * Данные для отчета Текущий выторг
	 * @param ws_id ид рабочей станции
	 * @return список.
	 * -дата время
	 * -подразделение
	 * -выручка наличными
	 * -выручка по картам
	 * -выручка по банку
	 * -оплата бонусами
	 * -сальдо на начало
	 * -приход
	 * -расход
	 * -сальдо на конец
	 * 
	 */
	public ArrayList<ArrayList<String>> getCurrentVytorg(String ws_id)
	{
		ArrayList<ArrayList<String>> list = new ArrayList<>();
		
		String query = "select name  from dic_subdivision where id = (select sw.subdivision_id from sec_workstations sw where sw.id = "+ws_id+") ";
		ArrayList<ArrayList<Object>> obj1 = baza.getNomen(query);
		
		query = "select sum(gr.sumcash) sumcash, sum(gr.sumbank) sumbank, sum(gr.sumcard) sumcard, sum(gr.sumbonus) sumbonus from "+
				"prc_rp_get_realise_sotr_front ((select sw.subdivision_id from sec_workstations sw where sw.id = "+ws_id+" )) gr ";
		
		ArrayList<ArrayList<Object>> obj2 = baza.getNomen(query);
		
		 query = "select * from z$front_cash_saldo (null,"+ws_id+") ";
		 ArrayList<ArrayList<Object>> obj3 = baza.getNomen(query);
		 
		 //Заполняем лист
		 
		 ArrayList<String> ar = new ArrayList<>();
		 ar.add("Дата-время:");
		 ar.add(timeNow());
		// ar.add("sdfsdf");
		 list.add(ar);
		 
		 ar = new ArrayList<>();
		 ar.add("Подразделение:");
		 if(obj1.get(0).get(0)!=null) ar.add((String)obj1.get(0).get(0));
		 else ar.add("");
		 list.add(ar);
		 
		 ar = new ArrayList<>();
		 ar.add("Выручка наличными:");
		 if(obj2.get(0).get(0)!=null){
			 BigDecimal price =  (BigDecimal) obj2.get(0).get(0);
			 Double priced=Double.parseDouble(price.toString());
			 String price1 = Double.toString(priced);
			 ar.add(price1);
		 }
		 else ar.add("");
		 list.add(ar);
		 
		 ar = new ArrayList<>();
		 ar.add("Выручка по картам:");
		 if(obj2.get(0).get(1)!=null) {
			 BigDecimal price =  (BigDecimal) obj2.get(0).get(1);
		 Double priced=Double.parseDouble(price.toString());
		 String price1 = Double.toString(priced);
		 ar.add(price1);
			 }
		 else ar.add("");
		 list.add(ar);
		 
		 ar = new ArrayList<>();
		 ar.add("Выручка по банку:");
		 if(obj2.get(0).get(2)!=null)  {
			 BigDecimal price =  (BigDecimal) obj2.get(0).get(2);
		 Double priced=Double.parseDouble(price.toString());
		 String price1 = Double.toString(priced);
		 ar.add(price1);
			 }
		 else ar.add("");
		 list.add(ar);
		 
		 ar = new ArrayList<>();
		 ar.add("Выручка бонусами:");
		 if(obj2.get(0).get(3)!=null)  {
			 BigDecimal price =  (BigDecimal) obj2.get(0).get(3);
		 Double priced=Double.parseDouble(price.toString());
		 String price1 = Double.toString(priced);
		 ar.add(price1);
			 }
		 else ar.add("");
		 list.add(ar);
		//
		 
		 ar = new ArrayList<>();
		 ar.add("bСальдо на начало:");
		 if(obj3.get(0).get(0)!=null){
			 BigDecimal price =  (BigDecimal) obj3.get(0).get(0);
			 Double priced=Double.parseDouble(price.toString());
			 String price1 = Double.toString(priced);
			 ar.add(price1);
		 }
		 else ar.add("");
		 list.add(ar);
		 
		 ar = new ArrayList<>();
		 ar.add("Приход в кассу:");
		 if(obj3.get(0).get(1)!=null) {
			 BigDecimal price =  (BigDecimal) obj3.get(0).get(1);
		 Double priced=Double.parseDouble(price.toString());
		 String price1 = Double.toString(priced);
		 ar.add(price1);
			 }
		 else ar.add("");
		 list.add(ar);
		 
		 ar = new ArrayList<>();
		 ar.add("Расход с кассы:");
		 if(obj3.get(0).get(2)!=null)  {
			 BigDecimal price =  (BigDecimal) obj3.get(0).get(2);
		 Double priced=Double.parseDouble(price.toString());
		 String price1 = Double.toString(priced);
		 ar.add(price1);
			 }
		 else ar.add("");
		 list.add(ar);
		 
		 ar = new ArrayList<>();
		 ar.add("bСальдо на конец:");
		 if(obj3.get(0).get(3)!=null)  {
			 BigDecimal price =  (BigDecimal) obj3.get(0).get(3);
		 Double priced=Double.parseDouble(price.toString());
		 String price1 = Double.toString(priced);
		 ar.add(price1);
			 }
		 else ar.add("");
		 list.add(ar);
		 
		return list;
		
	}
	
	/**
	 * Данные для отчета Текущий выторг
	 * @param ws_id ид рабочей станции
	 * @return список.
	 * -дата время
	 * -заголовки
	 * -данные
	 */
	public ArrayList<ArrayList<String>> getVytorgPodrazdelenij(String ws_id)
	{
		ArrayList<ArrayList<String>> list = new ArrayList<>();
		
		String query = "select VS.SUBDIVNAME, sum(VS.SUMREAL) as SUMREAL, sum(VS.SUMPRICE) as SUMPRICE,  VS.IS_NONAME from PRC_RP_GET_VYTORG_SUB ("+ws_id+") VS group by 1,4 " +
				" order by SUBDIVNAME asc, IS_NONAME desc";
		ArrayList<ArrayList<Object>> obj1 = baza.getNomen(query);
		
		
		 
		 //Заполняем лист
		 
		 ArrayList<String> ar = new ArrayList<>();
		 ar.add("Дата-время:");
		 ar.add(timeNow());
		// ar.add("sdfsdf");
		 list.add(ar);
		 
		 ar = new ArrayList<>();
		 ar.add("Подраз- деление");
		 ar.add("Сумма");
		 ar.add("Сумма базовая");
		 ar.add("Скидка");
		 list.add(ar);
		 
		 
		for(int i=0;i<obj1.size();i++)
		{
			ar = new ArrayList<>();
			
			if(obj1.get(i).get(0)!=null)  {
				
			 ar.add((String)obj1.get(i).get(0));
				 }
			if(obj1.get(i).get(1)!=null)  {
				 BigDecimal price =  (BigDecimal) obj1.get(i).get(1);
			 Double priced=Double.parseDouble(price.toString());
			 String price1 = Double.toString(priced);
			 ar.add(price1);
				 }
			if(obj1.get(i).get(2)!=null)  {
				 BigDecimal price =  (BigDecimal) obj1.get(i).get(2);
			 Double priced=Double.parseDouble(price.toString());
			 String price1 = Double.toString(priced);
			 ar.add(price1);
				 }
			if(obj1.get(i).get(3)!=null)  {
				 Integer price =  (Integer) obj1.get(i).get(3);
			 Double priced=Double.parseDouble(price.toString());
			 String price1 = Double.toString(priced);
			 ar.add(price1);
				 }
			list.add(ar);
		}
		 
		return list;
		 
	}	 
	
	
	// get current time
		public String timeNow() {
			Calendar now = Calendar.getInstance();

			int day = now.get(Calendar.DATE);
			int mnt = now.get(Calendar.MONTH) + 1;
			int yar = now.get(Calendar.YEAR);

			int hrs = now.get(Calendar.HOUR_OF_DAY);
			int min = now.get(Calendar.MINUTE);
			int sec = now.get(Calendar.SECOND);

			String time = day + "-" + mnt + "-" + yar + " " + zero(hrs) + ":"
					+ zero(min) + ":" + zero(sec);

			return time;
		}

		public String zero(int num) {
			String number = (num < 10) ? ("0" + num) : ("" + num);
			return number; // Add leading zero if needed

		}
		
		public void setUserId(int id)
		{
			 default_employee_name = Integer.toString(id);
			 default_employee_id =  Integer.toString(id);
		}

}
