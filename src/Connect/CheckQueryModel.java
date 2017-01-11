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
 * �����, ���������� �� �������� � �������������� �����
 * @author v.gorodetskiy
 *
 */
public class CheckQueryModel {
	
	Firebird baza;
	
	
	private String check_num=""; //����� ����
	private String data = ""; // ���� ����
	private String table = ""; //����
	private String employee = ""; //��������
	
	
	private String default_ws_name = ""; //��� ������� ������� �������
	private String default_ws_id = ""; //������� ������� �������
	
	private String default_employee_name; //�������� ���������� �� ���������
	private String default_employee_id = "1"; //id ���������� �� ���������
	
	private String default_check_grp=""; //������ ������ �� ���������
	
	private String current_time = ""; //���� ����
	private String default_client=""; //������ �� ���������
	private String default_cash_subvision_id=""; //������������� �����
	private String default_shift_cash_id=""; //������������� �������� ����� �����
	
	WS_Options ws_Options; //������ ����� ������� �������
	
	
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
	 * @param user id ����������
	 * @param ws_Options
	 * @param baza2
	 */
	 private CheckQueryModel( WS_Options ws_Options,Firebird baza2)
	 {
		
			
			this.ws_Options = ws_Options;
			
			baza = baza2;
		 
	 }
	 
	
	/**
	  * ������� ����� ���, ������������� ��������� ��� ����������� ��������� �����
	  * @param tableId ����� ���� 'null'
	  * @return check id
	  */
	 public String createNewChek(String tableId)
	 {
		 System.out.println("=== create check table id = "+tableId);
		
		   ArrayList<ArrayList<Object>> obj =new ArrayList<ArrayList<Object>>();
			Object st =  null;
			int iii;
			
			//����� �� ����������
			//default_employee_id =ws_Options.getDef_employee_id();
			
			//----����� ��������� ������ �.�.
			//id ������ ������ �� ���������
			default_check_grp = ws_Options.getDEF_PATH_CHECKS();
			
			//id ������� �� ���������
			default_client=ws_Options.getDef_client();
					
			//id ������������� ����� ������ �.�.
			default_cash_subvision_id = ws_Options.getDef_cash_subvision_id();
			
			default_ws_id = ws_Options.getWs_id();
			
			
			//id �������� ����� ����� 
			obj = baza.getNomen("select  js.id  from jor_shifts js where  js.dt_stop is null and js.subdivision_id = "+default_cash_subvision_id);
			st =  obj.get(0).get(0);
			 iii= (Integer) st;
			default_shift_cash_id = Integer.toString(iii);
			//System.out.println("�� �����="+default_shift_cash_id);
			
			//���������� ����� ����
			obj = baza.getNomen("select  gen_id ( num_jor_checks ,1 ) from RDB$DATABASE");		
			st =  obj.get(0).get(0);
			 Long lg= (Long) st;
			String stg=  Long.toString(lg);
			check_num=stg;
			
			System.out.println("id sotrudnika = "+default_employee_id);
			//System.out.println("����� ����="+check_num);
			
			//������� �������� ������� � ����
					//baza.setNomen("execute procedure sys_connect(1,"+default_ws_id+")");
			baza.setNomen("execute procedure sys_connect(1,1)");
			//������� ���
					baza.setNomen("execute procedure z$jor_checks_i(null,"+
							default_check_grp+",'now',"+check_num+", 0, "+default_shift_cash_id+","+default_employee_id+","+default_cash_subvision_id+","
							+default_client+","+tableId+", null,null)");
			
			
					
			//������ �� �������������� ����
			obj = baza.getNomen("select  first 1 jc.id from jor_checks jc  where jc.subdivision_id= "+default_cash_subvision_id+"  order by jc.id desc"); 
			st =  obj.get(0).get(0);
			iii= (Integer) st;
			String check_id=Integer.toString(iii);
			System.out.println("�� ����="+check_id);
			
			//System.out.println("22222222222222222222222222222222222222222222222");
			
			return check_id;
	 }
	 
	 
	 /**
	  * ��������� ��� �� ���� �� ���������� ID
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
		    	
		    	//System.out.println("��� �������! "+name);
		    	BigDecimal price =  (BigDecimal) obj.get(i).get(2);
		    	//System.out.println(price);
		    	BigDecimal cnt =    (BigDecimal) obj.get(i).get(3);
		    	
		    	
		    	//System.out.println(cnt);
		    	
		    	//� ������ ������ ��������� ����:
		    	
		    	Timestamp order_time =    (Timestamp) obj.get(i).get(5); //����� ������ 
		    	Integer NEED_PRINT_REFUSE = (Integer) obj.get(i).get(6); //�������� �� �����
		    	//System.out.println("UUUUUUUUUUUUUUUUUUUUUUUUUUUUU="+ obj.get(i).get(5).getClass());
		    	Timestamp REFUSE_TIME =    (Timestamp) obj.get(i).get(7); //����� ������
		    	
		    	Integer dtid = (Integer) obj.get(i).get(4); //id �����������
		    	
		    	String dt1 = Integer.toString(dtid);
		    	//System.out.println("�� ����������� "+dtid);
		    	
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
		    	
		    	//System.out.println("��� �������! "+name);
		    	BigDecimal price =  (BigDecimal) obj.get(i).get(2);
		    	//System.out.println(price);
		    	BigDecimal cnt =    (BigDecimal) obj.get(i).get(3);
		    	
		    	
		    	//System.out.println(cnt);
		    	
		    	//� ������ ������ ��������� ����:
		    	
		    	Timestamp order_time =    (Timestamp) obj.get(i).get(5); //����� ������ 
		    	Integer NEED_PRINT_REFUSE = (Integer) obj.get(i).get(6); //�������� �� �����
		    	//System.out.println("UUUUUUUUUUUUUUUUUUUUUUUUUUUUU="+ obj.get(i).get(5).getClass());
		    	Timestamp REFUSE_TIME =    (Timestamp) obj.get(i).get(7); //����� ������
		    	
		    	Integer dtid = (Integer) obj.get(i).get(4); //id �����������
		    	
		    	String dt1 = Integer.toString(dtid);
		    	//System.out.println("�� ����������� "+dtid);
		    	
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
		    
		    	if(stan!=3) arrl.add(arr);//��� ������ �� ������� ���������� �������� �������
		    }
		    
		  
			return arrl;
			
	 }
	 
	
	 
		/**
		 *  ��������� ����� �������
		 * @param id pozicii
		 * @param chek_id id cheka
		 * @return int
		 * @param grp_id id ����������
		 *   -2  ��� ������������� ��������
		 *   -1  �� ������� �����
		 *    1  ��� ��
		 *   
		 */
		public int addPosition(String id, String chekId, String price_grp_id)
		{
					
					//����� �� ������������� �������� ������
					ArrayList<ArrayList<Object>> obj =baza.getNomen("select dp.subdivision_id from dic_price_list_grp dp where dp.id= "+price_grp_id);
					
					String subdivision_id="null";
					Object st=null;
					int iii=0;
					if(obj.size()>0){
					 st =  obj.get(0).get(0);
					 iii= (Integer) st;
					  subdivision_id=Integer.toString(iii); 
					// System.out.println("id ��������"+subdivision_id );
					}
					//����� �� ������������� ������ ������
					 String print_subdivision_id="";
					 obj = baza.getNomen("select dp.print_subdivision_id from dic_price_list_grp dp where dp.id= "+price_grp_id);
					// System.out.println("id �������� "+obj.get(0).get(0).getClass());
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
					 
					//������� �������� ������� � ����
					//baza.setNomen("execute procedure sys_connect(1,"+default_ws_id+")");
					baza.setNomen("execute procedure sys_connect(1,1)");
				 //������� ����������� ����
					baza.setNomen("execute procedure z$jor_checks_dt_i(null,"+
								chekId+","+id+","+1+","+price1+", "+price1+","+subdivision_id+","+print_subdivision_id+",'"+""+"')");
					
				
			
			return 1;
			
		}
		
		
		/**
		 *  ��������� ����� ������� � �������� �����������
		 * @param cnt ����������
		 * @param id pozicii
		 * @param chek_id id cheka
		 * @param price_grp_id  id ����������
		 * @return int
		 *   -2  ��� ������������� ��������
		 *   -1  �� ������� �����
		 *    1  ��� ��
		 *   
		 */
		public int addPosition(int chekId,int id,  double cnt, int price_grp_id )
		{
			System.out.println("hello add position");
			
			//�������� �� ������� ������������� �������� � ������ �������
		/*	String query = "select dp.subdivision_id from dic_price_list_grp dp "+
					"where	dp.id=( select dpg.grp_id from dic_price_list dpg where "+
					"dpg.goods_id = "+id+")";
			
			
			ArrayList<ArrayList<Object>> obj =baza.getNomen(query);
			
			//�������� �� ������� �������� ����� �� ������������� ��������
			/*ArrayList<ArrayList<Object>> objsh = baza.getNomen("select first 1 js.id from jor_shifts js where js.dt_stop is null  and "+
			"js.subdivision_id = (select dp.subdivision_id from dic_price_list_grp dp "+
			"where dp.id = (select dl.grp_id from dic_price_list dl "+
			"where dl.goods_id = "+id+" ) )");*/
			
			//if(obj.size()!=0)
			{
				//if(objsh.size()!=0)
				{
					
					//����� �� ���������� ������
					/*ArrayList<ArrayList<Object>> obj = baza.getNomen("select dl.grp_id from dic_price_list dl where dl.goods_id = "+id);
					 Object st =  obj.get(0).get(0);
					 int iii= (Integer) st;
					 String price_grp_id = Integer.toString(iii); 
					//System.out.println("id ������"+price_grp_id );*/
					
					//����� �� ������������� �������� ������
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
					 System.out.println("id ��������"+subdivision_id );
					 
					//����� �� ������������� ������ ������
					 obj = baza.getNomen("select dp.print_subdivision_id from dic_price_list_grp dp where dp.id= "+price_grp_id);
					 st =  obj.get(0).get(0);
					 iii= (Integer) st;
					 String print_subdivision_id=Integer.toString(iii); 
					 System.out.println("id ������"+print_subdivision_id );
					 System.out.println("----4*");
					 
					 obj = baza.getNomen("select first 1 PLD.PRICE	 from DIC_PRICE_LIST PL, DIC_PRICE_LIST_DT PLD "+
							 "where PL.GOODS_ID = "+id+" and     PLD.HD_ID = PL.ID and "+
							 "PLD.DATE_FROM < current_timestamp	 order by PLD.DATE_FROM desc");
					 
					 BigDecimal price =  (BigDecimal) obj.get(0).get(0);
					// java.util.Vector prc = obj;
					 
					 Double priced=Double.parseDouble(price.toString());
				    	String price1 = Double.toString(priced);
				    	
					 
					 System.out.println("price="+st.getClass());
					 
					//������� �������� ������� � ����
					//baza.setNomen("execute procedure sys_connect(1,"+default_ws_id+")");
					baza.setNomen("execute procedure sys_connect(1,1)");
				 //������� ����������� ����
					baza.setNomen("execute procedure z$jor_checks_dt_i(null,"+
								chekId+","+id+","+cnt+","+price1+", "+price1+","+subdivision_id+","+print_subdivision_id+",'"+"� �����"+"')");
					
					
					
				} //else return -1;
				
			
			}
			//else return -2;
			
			return 1;
			
		}
	 
		String idi;
		//RefuseReasonFrame  rfframe;
	 
	
	
	/**
	 * �������� ���������� �������
	 * @param id �����������
	 * @param kolvo ����������
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
	 * ����� ������ ��� ������
	 * @param id ����
	 * ���������� ���� �� ������� ������������, ���-��, ������������� ������ ������ �������, �����������
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
						if(obj.get(0).get(5)!=null)arr.add(obj.get(0).get(5).toString());//�����������
												
						list.add(arr);
				}
				}
		
		return list;
	}
	
	/**
	 * ������ � ���� �� �����
	 * @param id ����
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
	 * ����� ������ ��� ������
	 * @param id ����
	 * @param subdivId �� �������������
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
					
				    //� ������ ������ ��������� ����:
					//System.out.println(obj);
					Integer NEED_PRINT_REFUSE = (Integer) obj.get(0).get(1);
				    Timestamp REFUSE_TIME =    (Timestamp) obj.get(0).get(2); //����� ������ 
			
				if(NEED_PRINT_REFUSE==1&&REFUSE_TIME==null)
				{
						//baza.setNomen("execute procedure sys_connect(1,1)");
						//baza.setNomen("update jor_checks_dt jd set jd.REFUSE_TIME = current_timestamp where jd.id = "+obj0.get(i).get(0));
						
						ArrayList<String> arr = new ArrayList<String>();
						arr.add(obj.get(0).get(3).toString());
						arr.add(obj.get(0).get(4).toString());
						arr.add(obj.get(0).get(5).toString());
						arr.add(obj.get(0).get(6).toString());//�� �������������
						if(obj.get(0).get(7)==null){
							arr.add("");
						}
						else{
							arr.add(obj.get(0).get(7).toString());//������� ������
						}
						
						list.add(arr);
				}
			}
		System.out.println("otkaz="+list);
		return list;
	}
	
	/**
	 * ������ � ���� �����
	 * @param id �� ����
	 * @param subdivId �� �������������
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
					
				    //� ������ ������ ��������� ����:
					//System.out.println(obj);
					Integer NEED_PRINT_REFUSE = (Integer) obj.get(0).get(1);
				    Timestamp REFUSE_TIME =    (Timestamp) obj.get(0).get(2); //����� ������ 
			
				if(NEED_PRINT_REFUSE==1&&REFUSE_TIME==null)
				{
						baza.setNomen("execute procedure sys_connect(1,1)");
						baza.setNomen("update jor_checks_dt jd set jd.REFUSE_TIME = current_timestamp where jd.subdivision_id = "+subdivId+" and jd.id = "+obj0.get(i).get(0));
						
				}
			}
	}
	
	
	/**
	 * ������ ������� 
	 * @param id ����
	 */
	public void setPrechek(String id)
	{
		String query = "select jd.PRINT_TIME  from  jor_checks jd where jd.id = "+id;
		ArrayList<ArrayList<Object>>   obj = baza.getNomen(query);
	   
	    	//� ������ ������ ��������� ����:
		//System.out.println(obj);
		//Integer NEED_PRINT_REFUSE =    (Integer) obj.get(0).get(1);
	    	Timestamp PRINT_TIME =    (Timestamp) obj.get(0).get(0); //����� ������ ���� 
	
		//System.out.println("����� ������: "+order_time);
		
		if(PRINT_TIME!=null){ 
			System.out.println("������! ���� ��� ������!!!");
		}
		else
		{
		
		System.out.println("������ �� ����� ����� "+ id + " ���������");
		
		//������� �������� ������� � ����
		baza.setNomen("execute procedure sys_connect(1,1)");
		//�������� ���-�� � ����������� ���� 
		baza.setNomen("update jor_checks jd set jd.PREPRINT_TIME = current_timestamp where jd.id = "+id);
		}
	}
	
	
	/**
	 *  �������� ���
	 */
	public void setChek(int id)
	{
		
		String query = "select jd.PRINT_TIME  from  jor_checks jd where jd.id = "+id;
		ArrayList<ArrayList<Object>>   obj = baza.getNomen(query);
	   
	    	//� ������ ������ ��������� ����:
		//System.out.println(obj);
		//Integer NEED_PRINT_REFUSE =    (Integer) obj.get(0).get(1);
	    Timestamp PRINT_TIME =    (Timestamp) obj.get(0).get(0); //����� ������ ���� 
	
				
		System.out.println("������ �� ����� ����� "+ id + " ���������");
		
				
		//������� �������� ��������
		
		//������ �� ����� ����
		String def_cash_grp = ws_Options.getDEF_PATH_CASH();
		
		
		
		
		//������� �������� ������� � ����
		baza.setNomen("execute procedure sys_connect(1,1)");
		
		//������� �������� ��������
		baza.setNomen("" +
			"insert into JOR_CASH(ID, GRP_ID, DATE_TIME, NUM, DOC_TYPE, DOC_STATE, SUBDIVISION_ID, DOC_SUM, CONTRAGENT_TYPE,  EMPLOYEE_ID, CLIENT_ID, IO_ITEM_ID)  " +
				" values(null, "+ws_Options.getDEF_PATH_CASH() +" ,current_timestamp, null, 0, 0 , "+ws_Options.getDef_cash_subvision_id() +" , "+getTotalSumm(id) + " , 2 ,  "+default_employee_id+" , " + ws_Options.getDef_client()+" , null )");
		
		//������ �� ������������ �������� ��������
		obj = baza.getNomen("select  first 1 jc.id from jor_cash jc  where jc.subdivision_id= "+ws_Options.getDef_cash_subvision_id()+"  order by jc.id desc"); 
		Object st =  obj.get(0).get(0);
		int iii= (Integer) st;
		String cash_id=Integer.toString(iii);
		System.out.println("�� �������� �� �����="+cash_id);
		
		//��������� ����������� � �����
		baza.setNomen("" +
				"insert into JOR_CASH_DT(ID, HD_ID, DOC_TYPE, DOC_ID, HT_CHECK_ID, BILL_IN_ID , BILL_OUT_ID , FT_CHECK_ID , PAYROLL_ID , SLA_ID, SUM_ROW )  " +
					" values(null, "+cash_id +" , 0 ," +id + ",null ,null ,null ,null ,null ,null, "+getTotalSumm(id)+ ")");
		
		//������ �� ������������ �������� �����������
		obj = baza.getNomen("select  first 1 jc.id from jor_cash_dt jc  where jc.hd_id= "+cash_id+"  order by jc.id desc"); 
		st =  obj.get(0).get(0);
		iii= (Integer) st;
		String cash_dt_id=Integer.toString(iii);
		System.out.println("�� ���������� �������� �� �����="+cash_dt_id);
				
		//�������� ������� �������� ��������*/
			
		baza.setNomen("update jor_cash jd set  jd.DOC_STATE = 1 where jd.id = "+cash_id);
		
		
		baza.setNomen("update jor_checks jd set jd.PRINT_TIME = current_timestamp , jd.DOC_STATE = 1 where jd.id = "+id);
		
	}
	
	/**
	 * ������� ���
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
	 * @return �������� ����� ����� ����
	 */
	public String getTotalSumm(int id)
	{
		String sum="";
		double summa=0;
		
		
		 ArrayList<ArrayList<Object>> obj =new ArrayList<ArrayList<Object>>();
			Object st =  null;
			int iii;
			
			//����� ����� ����
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
	 * �������� ����� ���� �� ����
	 * 
	 */
	public String getCheckNum(String id)
	{
	//����� ����� ����
		 ArrayList<ArrayList<Object>> obj = baza.getNomen("select jc.num from  jor_checks jc where jc.id = "+id);
		 Object st =  obj.get(0).get(0);
     String num =(String) st;
	return num;
   
	}
	
	/**
	 * �������� ��� ������� � ���� 
	 * @param id ����
	 * @return
	 */
	public String getCheckClient(String id)
	{
		System.out.println("id="+id);
		ArrayList<ArrayList<Object>> client = baza.getNomen("select (select dc.code_name from dic_client dc where dc.id =jc.client_id ) as CLIENT from jor_checks jc where jc.id ="+id);
		
		return (String)client.get(0).get(0);
		
	}
	
	/**
	 * �������� id ������� � ���� 
	 * @param id ����
	 * @return
	 */
	public String getCheckClientId(String id)
	{
		System.out.println("id="+id);
		ArrayList<ArrayList<Object>> client = baza.getNomen("select jc.client_id  from jor_checks jc where jc.id ="+id);
		
		return Integer.toString((Integer)client.get(0).get(0));
		
	}
	
	
	/**
	 * �������� ��� ����� � ���� 
	 * @param id ����
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
	 * �������� ����� ������� ����������� �� �� ����
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
	 * �������� �� ������ ������� � ����������� �� �� ����
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
	 * ���������, ������ �� ������� �����������
	 * @param id �����������
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
	 * ���������� ��������� ����.
	 *  0 - �� �������
	 *  1 - �������
	 *  2 - ������� �� �����
	 *  3 - �������
	 * @param id �� ����
	 * @param dt_id �� �����������
	 * @return
	 */
	public int getStanOfDT(String id, String dt_id)
	{
		
		ArrayList<ArrayList<Object>>  obj = baza.getNomen("select   JD.ORDER_TIME, JD.NEED_PRINT_REFUSE, " +
				"   JD.REFUSE_TIME, JD.ID from JOR_CHECKS_DT JD where  JD.id = "+dt_id+" and JD.HD_ID = "+id);
				    	//� ������ ������ ��������� ����:
				    	Timestamp order_time =    (Timestamp) obj.get(0).get(0); //����� ������ 
				    	Integer NEED_PRINT_REFUSE = (Integer) obj.get(0).get(1); //�������� �� �����
				    	Timestamp REFUSE_TIME =    (Timestamp) obj.get(0).get(2); //����� ������
				    	
				    	int stan = 0;
				    	if(order_time!=null&&REFUSE_TIME==null&&NEED_PRINT_REFUSE==0) stan = 1;
				    	if(NEED_PRINT_REFUSE!=0&&REFUSE_TIME==null){stan = 2;}
				    	if(REFUSE_TIME!=null) stan = 3;
				    	
				    	
				    
		return stan;
	}
	
	
	
	
	/**
	 * �������� ��� ����������� hd_id
	 * @param id �����������
	 * @param hd_id id ����
	 * @return
	 */
	public void changeHdId(String id, String hd_id)
	{
		baza.setNomen("execute procedure sys_connect(1,1)");
		baza.setNomen("update jor_checks_dt jd  set jd.hd_id="+hd_id+"  where jd.id = "+id);
	}
	
	 
	 
	/**
	 * ���������, ���������� �� ��� � ��������� ��
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
	  * ���������, �� �������� �� ��� � ��������� ��
	  * @param id ����
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
	 * ������������� ������ � ����
	 * @param checkId �� ����
	 * @param tableId �� �����
	 */
	public void setTableChek(String checkId, String tableId)
	{
		baza.setNomen("update jor_checks jc set jc.table_id= "+tableId+" where jc.id= "+checkId);
	}
	
	/**
	 * ������������� ������� � ����
	 * @param checkId �� ����
	 * @param tableId �� �����
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
	 * ���������� ������ ������������� ��� ������� ����
	 * @param checkId �� �����������
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
	 * ���������� ������������ ������ �� �� ����������� ����
	 * @param dtId �� ����������� ����
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
	 * @param modifier ������ ������������
	 * @param dtId �� ����������� ����
	 */
	public void setModifier(String modifier, String dtId)
	{
		String query = "update jor_checks_dt jc set jc.descr = '"+modifier+"' where jc.id= "+dtId;
		baza.setNomen(query);
	}
	
	/**
	 * ���������� ������������ �� ����������� ����
	 * @param dtId �� ����������� ����
	 * @return
	 */
	public String getModifiersOfChekDt(String dtId)
	{
		String query = "select jc.descr from jor_checks_dt jc where jc.id = "+dtId;
		ArrayList<ArrayList<Object>> obj = baza.getNomen(query); 
		return (String)obj.get(0).get(0);
	}
	
	/**
	 * ���������, �������� �� ������� ����. ���� ��� ������� �������� - ���������� true
	 * @param id �� ����
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
	 * ���������, ��� �� ��������� ������. ���� ��- ���������� true
	 * @param id �� ����
	 * @return
	 */
	public boolean isPrintedPrechek(String id)
	{
		boolean b = true;
		ArrayList<ArrayList<Object>> obj = baza.getNomen("select jd.preprint_time from jor_checks jd where jd.id= "+id);
		System.out.println("������ = "+obj);
			if(obj.get(0).get(0)==null) return false;
		
		return b;
	}
	
	/**
	 * �������� ������
	 * @param id
	 */
	public void cancelPrechek(String id)
	{
		baza.setNomen("update jor_checks jc set jc.preprint_time = null where jc.id = "+id);
	}
	
	
	/**
	 * ���������� ������ ����� �� �� ��� ��������, � ��������� ������
	 * @param printerName ��� ��������
	 * @return ������ �����
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
	 * ������ ��� ������ ������� ������
	 * @param ws_id �� ������� �������
	 * @return ������.
	 * -���� �����
	 * -�������������
	 * -������� ���������
	 * -������� �� ������
	 * -������� �� �����
	 * -������ ��������
	 * -������ �� ������
	 * -������
	 * -������
	 * -������ �� �����
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
		 
		 //��������� ����
		 
		 ArrayList<String> ar = new ArrayList<>();
		 ar.add("����-�����:");
		 ar.add(timeNow());
		// ar.add("sdfsdf");
		 list.add(ar);
		 
		 ar = new ArrayList<>();
		 ar.add("�������������:");
		 if(obj1.get(0).get(0)!=null) ar.add((String)obj1.get(0).get(0));
		 else ar.add("");
		 list.add(ar);
		 
		 ar = new ArrayList<>();
		 ar.add("������� ���������:");
		 if(obj2.get(0).get(0)!=null){
			 BigDecimal price =  (BigDecimal) obj2.get(0).get(0);
			 Double priced=Double.parseDouble(price.toString());
			 String price1 = Double.toString(priced);
			 ar.add(price1);
		 }
		 else ar.add("");
		 list.add(ar);
		 
		 ar = new ArrayList<>();
		 ar.add("������� �� ������:");
		 if(obj2.get(0).get(1)!=null) {
			 BigDecimal price =  (BigDecimal) obj2.get(0).get(1);
		 Double priced=Double.parseDouble(price.toString());
		 String price1 = Double.toString(priced);
		 ar.add(price1);
			 }
		 else ar.add("");
		 list.add(ar);
		 
		 ar = new ArrayList<>();
		 ar.add("������� �� �����:");
		 if(obj2.get(0).get(2)!=null)  {
			 BigDecimal price =  (BigDecimal) obj2.get(0).get(2);
		 Double priced=Double.parseDouble(price.toString());
		 String price1 = Double.toString(priced);
		 ar.add(price1);
			 }
		 else ar.add("");
		 list.add(ar);
		 
		 ar = new ArrayList<>();
		 ar.add("������� ��������:");
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
		 ar.add("b������ �� ������:");
		 if(obj3.get(0).get(0)!=null){
			 BigDecimal price =  (BigDecimal) obj3.get(0).get(0);
			 Double priced=Double.parseDouble(price.toString());
			 String price1 = Double.toString(priced);
			 ar.add(price1);
		 }
		 else ar.add("");
		 list.add(ar);
		 
		 ar = new ArrayList<>();
		 ar.add("������ � �����:");
		 if(obj3.get(0).get(1)!=null) {
			 BigDecimal price =  (BigDecimal) obj3.get(0).get(1);
		 Double priced=Double.parseDouble(price.toString());
		 String price1 = Double.toString(priced);
		 ar.add(price1);
			 }
		 else ar.add("");
		 list.add(ar);
		 
		 ar = new ArrayList<>();
		 ar.add("������ � �����:");
		 if(obj3.get(0).get(2)!=null)  {
			 BigDecimal price =  (BigDecimal) obj3.get(0).get(2);
		 Double priced=Double.parseDouble(price.toString());
		 String price1 = Double.toString(priced);
		 ar.add(price1);
			 }
		 else ar.add("");
		 list.add(ar);
		 
		 ar = new ArrayList<>();
		 ar.add("b������ �� �����:");
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
	 * ������ ��� ������ ������� ������
	 * @param ws_id �� ������� �������
	 * @return ������.
	 * -���� �����
	 * -���������
	 * -������
	 */
	public ArrayList<ArrayList<String>> getVytorgPodrazdelenij(String ws_id)
	{
		ArrayList<ArrayList<String>> list = new ArrayList<>();
		
		String query = "select VS.SUBDIVNAME, sum(VS.SUMREAL) as SUMREAL, sum(VS.SUMPRICE) as SUMPRICE,  VS.IS_NONAME from PRC_RP_GET_VYTORG_SUB ("+ws_id+") VS group by 1,4 " +
				" order by SUBDIVNAME asc, IS_NONAME desc";
		ArrayList<ArrayList<Object>> obj1 = baza.getNomen(query);
		
		
		 
		 //��������� ����
		 
		 ArrayList<String> ar = new ArrayList<>();
		 ar.add("����-�����:");
		 ar.add(timeNow());
		// ar.add("sdfsdf");
		 list.add(ar);
		 
		 ar = new ArrayList<>();
		 ar.add("������- �������");
		 ar.add("�����");
		 ar.add("����� �������");
		 ar.add("������");
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
