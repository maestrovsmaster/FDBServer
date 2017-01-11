package Connect;

import java.util.ArrayList;
import java.util.Vector;


/**
 * �����, ������������ ����������� �������������
 * @author v.gorodetskiy
 *
 */
public class UserPrivilegies {
	

	private boolean IS_BACK_ALLOW =false; //���� �� ������ � ����
	private boolean IS_FRONT_ADMIN=false; //���� �� ������ � ���� ������
	private boolean CAN_OPEN_SHIFTS=false; //��������� �����
	private boolean CAN_CLOSE_SHIFTS=false;//��������� �����
	
	private boolean CAN_CHANGE_TABLE=false;//����������� �������� ����
	private boolean CAN_CHANGE_COUNT=false;//��������� ���-��
	
	private boolean CAN_DEL_RECORD=false;//�������� ������
	private boolean CAN_DEL_CHEQUE=false;//�������� ����
	
	private boolean CAN_PRINT_ORDER=false;//������ ������
	private boolean CAN_PRINT_REFUSE=false;//������ ������
	private boolean CAN_PRINT_CHEQUE=false;//������ ����
	private boolean CAN_PRINT_PRECHEQUE=false;//������ ������
	
	private boolean CAN_DIV_CHEQUE=false;//��������� ���
	private boolean CAN_TRANSFER_CHEQUE=false;//�������� ���
	
	private boolean CAN_CHANGE_CLIENT=false;//��������� ������� � ����
	private boolean CAN_CHANGE_CLIENT_BY_LIST=false;//��������� ������� �� ������ 
	
	private boolean CAN_PRINT_WITHOUT_ORDER=false;//������ ���� ��� ������
	private boolean CAN_PRINT_WITH_ZERO_PRICE=false; //������ ���� � ������� ������� �����
	private boolean CAN_PREPRINT_WITHOUT_ORDER=false;//������ ������� ��� ������
	private boolean CAN_REFUSE_AFTER_PRINT=false;//����� ����� ����
	private boolean CAN_REFUSE_AFTER_PREPRINT=false;//����� ����� �������
	private boolean IS_FISCAL_OPERATOR=false;//������ � ���������� ���������
	private boolean CAN_MODIFY_AFTER_PREPRINT=false;//����� ������ ������� ����� ��������� ������� � ����
	private boolean CAN_CHANGE_PRICE_F=false;//����������� �������� ���� �� ������
	private boolean CAN_REPRINT=false;//��������� ������ ����
	private boolean CAN_PRINT_WITHOUT_PRECHECK=false;//��������� �������� ���� ��� ������ �������
	private boolean CAN_REPRINT_PREPRINT=false;//��������� ������ �������
		
	String userId;//�� ������������(������)
	Firebird  conn;
	ArrayList<String> group;
	
	/**
	 * Id ������������ (������) 
	 * @param userId
	 */
	public UserPrivilegies(String userId, Firebird conn)
	{
		this.userId=userId;
		this.conn=conn;
		
		group = new ArrayList<>();
		
		//���� ������������ ������������ � ������, ������� ������ �����
		if(isMemberGroup())
		{
			//������� ��� ������, � ������� ������� ������������
			ArrayList<ArrayList<Object>> obj = conn.getNomen("select sm.group_id from sec_membership sm where sm.user_id="+userId);
			for(int i=0;i<obj.size();i++)
			{
				int a =(int) obj.get(i).get(0);
				group.add(Integer.toString(a));
			}
		}//if isMember group
		//System.out.println("group = "+group);
		
		
	}//end constructor
	
	/**
	 * �������� �� ������������ ������ ������
	 * @return
	 */
	public boolean isMemberGroup()
	{
		ArrayList<ArrayList<Object>> member = conn.getNomen("select sm.group_id from sec_membership sm where sm.user_id="+userId);
		if(member.size()>0) return true;
		else return false;
	}

	/**
	 * ���� �� ������ � ����
	 * @return
	 */
	public boolean isIS_BACK_ALLOW() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj = conn.getNomen("select sf.IS_BACK_ALLOW from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
			int a =(int) obj.get(0).get(0);
			if(a==1) IS_BACK_ALLOW = true; 
			}
		}
		return IS_BACK_ALLOW;
	}

	/**
	 * ���� �� ������ � ���� ������
	 * @return
	 */
	public boolean isIS_FRONT_ADMIN() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj = conn.getNomen("select sf.IS_FRONT_ADMIN from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
			int a =(int) obj.get(0).get(0);
			if(a==1) IS_FRONT_ADMIN = true;
			}
		}
		return IS_FRONT_ADMIN;
	}

	/**
	 * ��������� �����
	 * @return
	 */
	public boolean isCAN_OPEN_SHIFTS() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj = conn.getNomen("select sf.CAN_OPEN_SHIFTS from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
			int a =(int) obj.get(0).get(0);
			if(a==1) CAN_OPEN_SHIFTS = true; 
			}
		}
		return CAN_OPEN_SHIFTS;
	}

	/**
	 * ��������� �����
	 * @return
	 */
	public boolean isCAN_CLOSE_SHIFTS() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj = conn.getNomen("select sf.CAN_CLOSE_SHIFTS from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
			int a =(int) obj.get(0).get(0);
			if(a==1) CAN_CLOSE_SHIFTS = true; 
			}
		}
		return CAN_CLOSE_SHIFTS;
	}

	/**
	 * ����������� �������� ����
	 * @return
	 */
	public boolean isCAN_CHANGE_TABLE() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj = conn.getNomen("select sf.CAN_CHANGE_TABLE from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
			int a =(int) obj.get(0).get(0);
			if(a==1) CAN_CHANGE_TABLE = true; 
			}
		}
		return CAN_CHANGE_TABLE;
	}

	/**
	 * ��������� ���-��
	 * @return
	 */
	public boolean isCAN_CHANGE_COUNT() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj = conn.getNomen("select sf.CAN_CHANGE_COUNT from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
			int a =(int) obj.get(0).get(0);
			if(a==1) CAN_CHANGE_COUNT = true; 
			}
		}
		return CAN_CHANGE_COUNT;
	}

	/**
	 * �������� ������
	 * @return
	 */
	public boolean isCAN_DEL_RECORD() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj = conn.getNomen("select sf.CAN_DEL_RECORD from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
			int a =(int) obj.get(0).get(0);
			if(a==1) CAN_DEL_RECORD = true; 
			}
		}
		return CAN_DEL_RECORD;
	}

	/**
	 * �������� ����
	 * @return
	 */
	public boolean isCAN_DEL_CHEQUE() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj = conn.getNomen("select sf.CAN_DEL_CHEQUE from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
			int a =(int) obj.get(0).get(0);
			if(a==1) CAN_DEL_CHEQUE = true; 
			}
			else System.out.print("ooups|");
		}
		return CAN_DEL_CHEQUE;
	}

	/**
	 * ������ ������
	 * @return
	 */
	public boolean isCAN_PRINT_ORDER() {
		for(int i=0;i<group.size();i++)	{
			
			ArrayList<ArrayList<Object>> obj =  conn.getNomen("select sf.CAN_PRINT_ORDER from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
			int a =(int) obj.get(0).get(0);
			if(a==1) CAN_PRINT_ORDER = true; 
			}
		}
		return CAN_PRINT_ORDER;
	}

	/**
	 * ������ ������
	 * @return
	 */
	public boolean isCAN_PRINT_REFUSE() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj =  conn.getNomen("select sf.CAN_PRINT_REFUSE from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
				int a =(int) obj.get(0).get(0);
				if(a==1) CAN_PRINT_REFUSE = true; 
			}
		}
		return CAN_PRINT_REFUSE;
	}

	/**
	 * ������ ����
	 * @return
	 */
	public boolean isCAN_PRINT_CHEQUE() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj =  conn.getNomen("select sf.CAN_PRINT_CHEQUE from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
				int a =(int) obj.get(0).get(0);
			if(a==1) CAN_PRINT_CHEQUE = true; 
			}
		}
		return CAN_PRINT_CHEQUE;
	}

	/**
	 * ������ ������
	 * @return
	 */
	public boolean isCAN_PRINT_PRECHEQUE() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj =  conn.getNomen("select sf.CAN_PRINT_PRECHEQUE from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
				int a =(int) obj.get(0).get(0);
				if(a==1) CAN_PRINT_PRECHEQUE = true;
			}
		}
		return CAN_PRINT_PRECHEQUE;
	}

	/**
	 * ��������� ���
	 * @return
	 */
	public boolean isCAN_DIV_CHEQUE() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj =  conn.getNomen("select sf.CAN_DIV_CHEQUE from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
				int a =(int) obj.get(0).get(0);
			if(a==1) CAN_DIV_CHEQUE = true; 
			}
		}
		return CAN_DIV_CHEQUE;
	}

	/**
	 * �������� ���
	 * @return
	 */
	public boolean isCAN_TRANSFER_CHEQUE() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj =  conn.getNomen("select sf.CAN_TRANSFER_CHEQUE from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
				int a =(int) obj.get(0).get(0);
			if(a==1) CAN_TRANSFER_CHEQUE = true; 
			}
		}
		return CAN_TRANSFER_CHEQUE;
	}

	/**
	 * ��������� ������� � ����
	 * @return
	 */
	public boolean isCAN_CHANGE_CLIENT() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj =  conn.getNomen("select sf.CAN_CHANGE_CLIENT from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
				int a =(int) obj.get(0).get(0);
			if(a==1) CAN_CHANGE_CLIENT = true; 
			}
		}
		return CAN_CHANGE_CLIENT;
	}

	/**
	 * ��������� ������� �� ������ 
	 * @return
	 */
	public boolean isCAN_CHANGE_CLIENT_BY_LIST() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj =  conn.getNomen("select sf.CAN_CHANGE_CLIENT_BY_LIST from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
				int a =(int) obj.get(0).get(0);
			if(a==1) CAN_CHANGE_CLIENT_BY_LIST = true; 
			}
		}
		return CAN_CHANGE_CLIENT_BY_LIST;
	}

	/**
	 * ������ ���� ��� ������
	 * @return
	 */
	public boolean isCAN_PRINT_WITHOUT_ORDER() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj =  conn.getNomen("select sf.CAN_PRINT_WITHOUT_ORDER from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
				int a =(int) obj.get(0).get(0);
			if(a==1) CAN_PRINT_WITHOUT_ORDER = true; 
			}
		}
		return CAN_PRINT_WITHOUT_ORDER;
	}

	/**
	 * ������ ���� � ������� ������� �����
	 * @return
	 */
	public boolean isCAN_PRINT_WITH_ZERO_PRICE() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj =  conn.getNomen("select sf.CAN_PRINT_WITH_ZERO_PRICE from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
				int a =(int) obj.get(0).get(0);
			if(a==1) CAN_PRINT_WITH_ZERO_PRICE = true; 
			}
		}
		return CAN_PRINT_WITH_ZERO_PRICE;
	}

	/**
	 * ������ ������� ��� ������
	 * @return
	 */
	public boolean isCAN_PREPRINT_WITHOUT_ORDER() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj =  conn.getNomen("select sf.CAN_PREPRINT_WITHOUT_ORDER from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
				int a =(int) obj.get(0).get(0);
			if(a==1) CAN_PREPRINT_WITHOUT_ORDER = true; 
			}
		}
		return CAN_PREPRINT_WITHOUT_ORDER;
	}

	/**
	 * ����� ����� ����
	 * @return
	 */
	public boolean isCAN_REFUSE_AFTER_PRINT() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj =  conn.getNomen("select sf.CAN_REFUSE_AFTER_PRINT from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
				int a =(int) obj.get(0).get(0);
			if(a==1) CAN_REFUSE_AFTER_PRINT = true; 
			}
		}
		return CAN_REFUSE_AFTER_PRINT;
	}

	/**
	 * ����� ����� �������
	 * @return
	 */
	public boolean isCAN_REFUSE_AFTER_PREPRINT() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj =  conn.getNomen("select sf.CAN_REFUSE_AFTER_PREPRINT from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
				int a =(int) obj.get(0).get(0);
			if(a==1) CAN_REFUSE_AFTER_PREPRINT = true; 
			}
		}
		return CAN_REFUSE_AFTER_PREPRINT;
	}

	/**
	 * ������ � ���������� ���������
	 * @return
	 */
	public boolean isIS_FISCAL_OPERATOR() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj =  conn.getNomen("select sf.IS_FISCAL_OPERATOR from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
				int a =(int) obj.get(0).get(0);
			if(a==1) IS_FISCAL_OPERATOR = true; 
			}
		}
		return IS_FISCAL_OPERATOR;
	}

	/**
	 * ����� ������ ������� ����� ��������� ������� � ����
	 * @return
	 */
	public boolean isCAN_MODIFY_AFTER_PREPRINT() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj =  conn.getNomen("select sf.CAN_MODIFY_AFTER_PREPRINT from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
				int a =(int) obj.get(0).get(0);
			if(a==1) CAN_MODIFY_AFTER_PREPRINT = true; 
			}
		}
		return CAN_MODIFY_AFTER_PREPRINT;
	}

	/**
	 * ����������� �������� ���� �� ������
	 * @return
	 */
	public boolean isCAN_CHANGE_PRICE_F() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj =  conn.getNomen("select sf.CAN_CHANGE_PRICE_F from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
				int a =(int) obj.get(0).get(0);
			if(a==1) CAN_CHANGE_PRICE_F = true;
			}
		}
		return CAN_CHANGE_PRICE_F;
	}

	/**
	 * ��������� ������ ����
	 * @return
	 */
	public boolean isCAN_REPRINT() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj =  conn.getNomen("select sf.CAN_REPRINT from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
				int a =(int) obj.get(0).get(0);
			if(a==1) CAN_REPRINT = true; 
			}
		}
		return CAN_REPRINT;
	}

	/**
	 * ��������� �������� ���� ��� ������ �������
	 * @return
	 */
	public boolean isCAN_PRINT_WITHOUT_PRECHECK() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj =  conn.getNomen("select sf.CAN_PRINT_WITHOUT_PRECHECK from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
				int a =(int) obj.get(0).get(0);
			if(a==1) IS_BACK_ALLOW = true; 
			}
		}
		return CAN_PRINT_WITHOUT_PRECHECK;
	}

	/**
	 * ��������� ������ �������
	 * @return
	 */
	public boolean isCAN_REPRINT_PREPRINT() {
		for(int i=0;i<group.size();i++)	{
			ArrayList<ArrayList<Object>> obj =  conn.getNomen("select sf.CAN_REPRINT_PREPRINT from sec_front_privileges sf where sf.group_id= "+group.get(i));
			if(obj.size()>0)
			{
				int a =(int) obj.get(0).get(0);
			if(a==1) CAN_REPRINT_PREPRINT = true; 
			}
		}
		return CAN_REPRINT_PREPRINT;
	}

	/**
	 * �������� �� �����
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	
	
	public ArrayList<String> getGroup() {
		return group;
	}

	
	
}


