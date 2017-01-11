package Connect;

import java.util.ArrayList;

import org.json.JSONObject;

/**
 * Описываем общедоступные методы  выполнения запросов для всех типов подключения
 * @author v.gorodetskiy
 *
 */
public interface QueryMetodsInterface {
	
	/**
	 * Получить коллекцию данных 
	 * @param query запрос
	 * @return
	 */
	public ArrayList<ArrayList<Object>> getNomen(String query);
	
	
	 JSONObject getJSONNomen(String query);
	 
	 JSONObject setJSONNomen(String query);
	 
	 JSONObject getTablesHallsJSON();
	
	
	/**
	 *Выполнить запрос 
	 * @param query запрос
	 * @return результат
	 */
	public String setNomen(String query);

}
