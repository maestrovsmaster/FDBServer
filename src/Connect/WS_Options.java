package Connect;
import java.util.ArrayList;




/**
 * �����, ���������� ����� ��������� ������� �������
 * @author v.gorodetskiy
 *
 */
public class WS_Options {
	
	public  int ws_id;//id ������� �.�.
	
	public boolean isCorrectWS=true;
	/**
	 * ��������� �� ��������� ������� �������
	 * @return
	 */
	public boolean isCorrectWS() {
		return isCorrectWS;
	}

	private String DEF_PATH_SHIFTS;//����� ���� (��)
	private String DEF_PATH_CHECKS;//����� �����
	private String DEF_PATH_CASH;//����� ���� 
	private String PRICE_LIST_ID;//�� ����������
	
	
	private String default_ws_name = ""; //��� ������� ������� �������

	private ArrayList<String> errorList; //������ ������ ��� ����������� ����� ��
	
	private String def_client=""; //������ �� ���������
	private String def_cash_subvision_id=""; //������������� �����
	//private String default_shift_cash_id=""; //������������� �������� ����� �����
	
	private int login_type=-1; //��� ����� 0-������ / 1-�����
	private String prefix="";//������� ���������			
	private String postfix="";//�������� ���������
	
	private static Firebird baza;
	
	private static  WS_Options thisWsOptions=null;
	
	public static WS_Options getWS_OptionsInstance(String wsname, Firebird conn)
	{
		if(thisWsOptions==null)
		{
			thisWsOptions = new WS_Options(wsname, conn);
			
		}
		 return thisWsOptions;
	}
	
	/**
	 * 
	 * @param id ������� �������
	 * @param employe ���������
	 * @param conn ���������� � �����
	 */
	private WS_Options(String wsname, Firebird conn) 
	{
		
		
		
		baza = conn;
		System.out.println(" wellcome ws");
		
		ws_id = 0;
		
		ws_id = getWsId(wsname);
		
		errorList = new ArrayList<>();
		
		
		ArrayList<ArrayList<Object>> obj =new ArrayList<ArrayList<Object>>();
		
		
		
		Object st =  null;
		int iii;
		System.out.println(" ws id= "+ws_id);
		
		//��� ������� �������
		obj = baza.getNomen("select ws.name from sec_workstations ws where ws.id = "+ws_id);
		
		if(obj.size()>0){
			default_ws_name = (String)obj.get(0).get(0);
		}
		else {
			     errorList.add("�� ������� ������� ������� � ID="+ws_id);
			isCorrectWS=false;
		}
		
		if(isCorrectWS)//���� ������� ������� ��������, �� �������� ������
		{
		//����� ���� �� ���������
		obj = baza.getNomen("select sw.param_value  , sw.ws_id  from sec_ws_options sw where sw.param_name ='DEF_PATH_SHIFTS' and sw.ws_id = "+ws_id);
		
		System.out.println("aaaa"+obj);
		
		
		if(obj.size()>0){
			DEF_PATH_SHIFTS = (String)obj.get(0).get(0);
		}
		else {
			     errorList.add("�� ������ ���� �� ��������� ��� ����!");
			isCorrectWS=false;
		}
		
		//����� ����� �� ���������
		obj = baza.getNomen("select sw.param_value  , sw.ws_id  from sec_ws_options sw where sw.param_name ='DEF_PATH_CHECKS' and sw.ws_id = "+ws_id);
		
		if(obj.size()>0){
			DEF_PATH_CHECKS = (String)obj.get(0).get(0);
		}
		else {
			     errorList.add("�� ������ ���� �� ��������� ��� �����!");
			isCorrectWS=false;
		}
		
		
		
		//����� ���� �� ���������
		obj = baza.getNomen("select sw.param_value  , sw.ws_id  from sec_ws_options sw where sw.param_name ='DEF_PATH_CASH' and sw.ws_id = "+ws_id);
		
		if(obj.size()>0){
			DEF_PATH_CASH = (String)obj.get(0).get(0);
		}
		else {
			     errorList.add("�� ������ ���� �� ��������� ��� ����!");
			isCorrectWS=false;
		}
		
	
	
		//�� ���������� ��� ������ �.�. �� ���������
		//PRICE_LIST_ID
		obj = baza.getNomen("select sw.param_value  , sw.ws_id  from sec_ws_options sw where sw.param_name ='PRICE_LIST_ID' and sw.ws_id = "+ws_id);
		
		if(obj.size()>0){
			PRICE_LIST_ID = (String)obj.get(0).get(0);
		}
		else {
			     errorList.add("�� ������ ���������!");
			isCorrectWS=false;
		}
		
		
	    
	    //����� ��� �����
	    obj = baza.getNomen("select sw.param_value  , sw.ws_id  from sec_ws_options sw where sw.param_name ='LOGIN_TYPE' and sw.ws_id = "+ws_id);
	   
	    
	    if(obj.size()>0){
	    	System.out.println("login type= "+obj.get(0).get(0).getClass());
		    String typeL = (String) obj.get(0).get(0);
		    login_type = Integer.parseInt(typeL); 
		    
		}
		else {
			     errorList.add("�� ������ ��� �����!");
			isCorrectWS=false;
		}
	    
	    
	  
	    
	    //����� ������� ����������������� ���������
	    obj = baza.getNomen("select jc.scan_prefix from cfg_hardware jc where jc.ws_id ="+ws_id);
	    
	    if(obj.size()>0)
	    {
	    if(obj.get(0).get(0)==null) prefix="";
	    prefix =(String) obj.get(0).get(0);
	    }
	    else
	    {
	    	prefix="";
	    }
	  
	    
	    //����� �������� ����������������� ���������
	    obj = baza.getNomen("select jc.scan_postfix from cfg_hardware jc where jc.ws_id ="+ws_id);
	    if(obj.size()>0)
	    {
	    if(obj.get(0).get(0)==null) postfix="";
	    postfix =(String) obj.get(0).get(0);
	    }
	    else
	    {
	    	postfix="";
	    }
	    
	    
	    
		//id ������� �� ���������
		 obj = baza.getNomen("select  go.def_client from cfg_global_options go");
		 
		if(obj.size()>0){
			st =  obj.get(0).get(0);
			 iii= (Integer) st;
			def_client=Integer.toString(iii);
		}
		else {
			     errorList.add("�� ������ ������ �� ���������!");
			isCorrectWS=false;
		}
		
		
				
		//id ������������� ����� ������ �.�.
		obj = baza.getNomen("select sw.subdivision_id  from sec_workstations sw  where sw.id="+ws_id);
		
		if(obj.get(0).get(0)!=null){
			st =  obj.get(0).get(0);
			 iii= (Integer) st;
			def_cash_subvision_id = Integer.toString(iii);
		}
		else {
			     errorList.add("�� ������� ������������� �����!");
			isCorrectWS=false;
		}
		
		}//if is correct
		
		
		
		
		
	}//end constructor
	
	

	/**
	 * 
	 * @return id ����� ���� �� ���������
	 */
	public String getDEF_PATH_SHIFTS() {
		return DEF_PATH_SHIFTS;
	}

	/**
	 * 
	 * @return id ��� �������
	 */
	public  String getWs_id() {
		return Integer.toString(ws_id);
	}

	/**
	 * 
	 * @return �� ����� ����� �� �����
	 */
	public String getDEF_PATH_CHECKS() {
		return DEF_PATH_CHECKS;
	}

	/**
	 * 
	 * @return �� ����� ���� �� �����
	 */
	public String getDEF_PATH_CASH() {
		return DEF_PATH_CASH;
	}

	/**
	 * 
	 * @return ��� ��� �� �� �����
	 */
	public String getDefault_ws_name() {
		return default_ws_name;
	}

	
	
	/**
	 * 
	 * @return ��� ���������� �� �����
	 */
	/*public String getDef_employee_name() {
		return def_employee_name;
	}*/

	/**
	 * 
	 * @return �� ���������� �� �����
	 */
	/*public String getDef_employee_id() {
		return def_employee_id;
	}*/

	/**
	 * 
	 * @return �� ������� �� �����
	 */
	public String getDef_client() {
		return def_client;
	}

	/**
	 * 
	 * @return �� ������������� ����� ��� ��� �� �� �����
	 */
	public String getDef_cash_subvision_id() {
		return def_cash_subvision_id;
	}

	/**
	 * ID ���������� ������ �.�.
	 * @return
	 */
	public String getPRICE_LIST_ID() {
		return PRICE_LIST_ID;
	}
	
	/**
	 * ��� �����
	 * @return 0-������ / 1-�����
	 */
	public int getLogintype()
	{
		return login_type;
	}
	
	/**
	 * ������� ����������������� ���������
	 * @return
	 */
	public String getscanPrefix()
	{
		return prefix;
	}
	
	/**
	 * �������� ����������������� ���������
	 * @return
	 */
	public String getscanPostfix()
	{
		return postfix;
	}
	
	/**
	 * ���������� ��� �������� ��� ������
	 * @param reportType ��� ������
	 *  0 - ���
	 *  1 - ������
	 *  2 - �����
	 *  3 - �����
	 * @param subdivID ������������� ������ (����� ������� ����� ��� ���� � �������)
	 * @return
	 */
	public String getPrinterName(int reportType, String subdivID)
	{
		ArrayList<ArrayList<Object>> obj = new ArrayList<ArrayList<Object>>();
		
		if(reportType==0||reportType==1)
			obj=  baza.getNomen("select  cp.printer_name  from cfg_print_settings cp where cp.ws_id = "+getWs_id()+" and cp.rep_type = "+reportType );
		else
		{
			String query = "select  cp.printer_name  from cfg_print_settings cp where cp.ws_id = "+getWs_id()+" and cp.rep_type = "
					+reportType+" and cp.subdivision_id= "+subdivID;
			obj=  baza.getNomen(query );
			//System.out.println(query);
		}
		//System.out.println(obj);
		
		if(obj.size()>0) return (String) obj.get(0).get(0);
		else return "";
		
	}
	
	public int getWsId(String wsname) {
		// conn.setNomen("execute procedure sys_connect(1,1)");
		// String name = ConnectConstants.WSNAME;
		 
		ArrayList<ArrayList<Object>> obj2 = baza.getNomen("select sw.id from sec_workstations sw where sw.name='"+wsname+"'");
		if(baza==null) System.out.println("baza null");
		else System.out.println("baza ok");
		
		if(obj2==null) System.out.println("get wsid obj = null");
		else System.out.println("get wsid obj ok");

		
		int wsId=0;
		if (obj2 != null) {
			if (obj2.size() > 0)

				wsId = (int) obj2.get(0).get(0);

		}

		return wsId;

	}
	
	

	
}//end main class


