package Connect;

import java.util.ArrayList;

import org.json.JSONObject;

/**
 * ��������� ������������� ������  ���������� �������� ��� ���� ����� �����������
 * @author v.gorodetskiy
 *
 */
public interface QueryMetodsInterface {
	
	/**
	 * �������� ��������� ������ 
	 * @param query ������
	 * @return
	 */
	public ArrayList<ArrayList<Object>> getNomen(String query);
	
	
	 JSONObject getJSONNomen(String query);
	 
	 JSONObject setJSONNomen(String query);
	 
	 JSONObject getTablesHallsJSON();
	
	
	/**
	 *��������� ������ 
	 * @param query ������
	 * @return ���������
	 */
	public String setNomen(String query);

}
