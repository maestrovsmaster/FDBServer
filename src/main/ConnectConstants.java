package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Connect.ConnectDBFactory;
import Connect.Firebird;
import Connect.Query;
import LSManager.LSControl;

public abstract class ConnectConstants {

	public static String DB_IP = "";
	public static String DB_PATH = "";

	public static String DB_LOGIN = "";
	public static String DB_PASS = "";
	public static String WSNAME = "";
	
	public static int USER_ID = -1;

	public static ArrayList<JSONObject> devicesList = new ArrayList<>();

	public static int MAX_DEVICES_COUNT = 0;

	public static String getConnect(String ip, String pathDB, String login, String pass) {
		if (pathDB.length() == 0)
			return "";

		String ver = "";

		//ConnectDBFactory connects = new ConnectDBFactory();

		ConnectDBFactory.deleteConnectFirebird();
		Firebird conn = ConnectDBFactory.getInstanceFirebird(ConnectConstants.DB_IP, ConnectConstants.DB_PATH);
		if (conn != null) {
			System.out.println("conn ok !!!");
			Query query = Query.getQuery(conn);

			JSONArray goodsArray = new JSONArray();
			// goodsArray =
			ver = query.getDataBaseVersion();
		} else {
			System.out.println("conn null!!!");
			// ver+=ConnectFirebird.getErrorMsg();
			ver = "";
		}

		System.out.println("ver=" + ver);
		return ver;
	}

	public static ArrayList<String> readSettings(HttpServlet s) {
		ArrayList<String> list = new ArrayList<String>();
		// String path
		// =getServletContext().getRealPath("/WebContent/db_connect.txt");
		String path = s.getServletContext().getRealPath("/WEB-INF/conf/db_connect.txt");

		try {

			File file = new File(path);

			System.out.println("serv=" + s.getServletName() + "  my reading file: " + file.getAbsolutePath());

			BufferedReader reader = null;

			try {
				reader = new BufferedReader(new FileReader(file));
				String text = null;

				while ((text = reader.readLine()) != null) {
					list.add(text);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e) {
				}
			}
		} catch (Exception e) {
			System.out.println("err open file! " + e.getMessage().toString());
		}
		// print out the list

		if (list.size() > 0) {
			if (list.size() >= 4) {
				if (list.get(0) != null) {
					ConnectConstants.DB_IP = list.get(0);
				}
				if (list.get(1) != null) {
					ConnectConstants.DB_PATH = list.get(1);
				}
				if (list.get(2) != null) {
					ConnectConstants.DB_LOGIN = list.get(2);
				}
				if (list.get(3) != null) {
					ConnectConstants.DB_PASS = list.get(3);
				}
			}
			
			if (list.size() >= 5) {
				if (list.get(4) != null) {
					ConnectConstants.WSNAME = list.get(4);
				}
			}
		}

		return list;
	}

	public static void writeSettings(HttpServlet s, ArrayList<String> settings) {

		String path = s.getServletContext().getRealPath("/WEB-INF/conf/db_connect.txt");
		System.out.println("WRITE SERTTINGS FILE PATH = "+path);
		try {
			// Whatever the file path is.
			File statText = new File(path);
			System.out.println("serv=" + s.getServletName() + " my writing file: " + statText.getAbsolutePath());

			FileOutputStream fos = new FileOutputStream(statText);

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			for (String str : settings) {
				bw.write(str);
				bw.newLine();
			}

			bw.close();
			fos.close();

		} catch (IOException e) {
			System.err.println("Problem writing to the file statsTest.txt");
		}

	}
	
	
	public static void writeMyLog(HttpServlet s, String text) {

		String path = s.getServletContext().getRealPath("/WEB-INF/conf/my_log.txt");
		try {
			// Whatever the file path is.
			File statText = new File(path);
			statText.createNewFile();
			System.out.println("serv=" + s.getServletName() + " my writing file: " + statText.getAbsolutePath());

			try {
	            FileWriter writer = new FileWriter(path, true);
	            BufferedWriter bufferWriter = new BufferedWriter(writer);
	            bufferWriter.write(text);
	            bufferWriter.close();
	        }
	        catch (IOException e) {
	            System.out.println(e);
	        }

		} catch (IOException e) {
			System.err.println("Problem writing to the file statsTest.txt");
		}

	}
	
	
	

	public static ArrayList<JSONObject> readDevList(HttpServlet s) {
		ArrayList<String> list = new ArrayList<String>();
		// String path
		// =getServletContext().getRealPath("/WebContent/db_connect.txt");
		String path = s.getServletContext().getRealPath("/WEB-INF/conf/dev_list.txt");

		try {

			File file = new File(path);

			System.out.println("serv=" + s.getServletName() + "  my reading file: " + file.getAbsolutePath());

			BufferedReader reader = null;

			try {
				reader = new BufferedReader(new FileReader(file));
				String text = null;

				while ((text = reader.readLine()) != null) {
					list.add(text);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("read dev list 2 err");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e) {
				}
			}
		} catch (Exception e) {
			System.out.println("err open file! " + e.getMessage().toString());
		}
		// print out the list

		ArrayList<JSONObject> devList = new ArrayList<>();

		for (String inputJson : list) {

			try {
				JSONObject jsonObject = new JSONObject(inputJson);

				devList.add(jsonObject);

			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		return devList;
	}

	public static void writeDevList(HttpServlet s, ArrayList<JSONObject> devList) {
		
		

		String path = s.getServletContext().getRealPath("/WEB-INF/conf/dev_list.txt");
		try {
			// Whatever the file path is.
			File statText = new File(path);
			System.out.println("serv=" + s.getServletName() + " my writing file: " + statText.getAbsolutePath());

			FileOutputStream fos = new FileOutputStream(statText);

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			for (JSONObject json : devList) {
				String str = json.toString();
				bw.write(str);
				bw.newLine();
			}

			bw.close();
			fos.close();

		} catch (IOException e) {
			System.err.println("Problem writing to the file statsTest.txt");
		}

	}
	
private static boolean checkDate = true;
	
	
	/**
	 * Совпали ли даты
	 * @return
	 */
	public static boolean isCheckDate() {
		return checkDate;
	}
	

	public static boolean writeOrExistsDevice(HttpServlet s, JSONObject json) {
		
		LSControl lsControl = new LSControl(s);
		
		if(lsControl.isLicensed()){
			MAX_DEVICES_COUNT = lsControl.getDevCount();
		}
		else{
			if(!lsControl.isCheckDate()){
				System.out.println("connect constant: err date");
				checkDate=false;
			}
			MAX_DEVICES_COUNT=0;
		}
		
		System.out.println("MAX_DEV_COUNT = "+ MAX_DEVICES_COUNT);
		
		if (devicesList.size() == 0) {
			devicesList = readDevList(s);
		}
		if(devicesList.size()==0)
		{
			if (devicesList.size() < MAX_DEVICES_COUNT) {

				devicesList.add(json);
				writeDevList(s, devicesList);
				return true;

			} else
				return false;
		}
		
		System.out.println("deviceslist = "+devicesList.toString());
		
		for(JSONObject jsList:devicesList){
			System.out.println("jslist = "+jsList.toString());
			System.out.println("json = "+json.toString());
		
		try {
			String name1 = jsList.getString("device_name");
			String name2 = json.getString("device_name");
			
			String id1 = jsList.getString("device_id");
			String id2 = json.getString("device_id");
			
			if(name1.equals(name2)&&id1.equals(id2)){
				return true;
			}
			else{
				if (devicesList.size() < MAX_DEVICES_COUNT) {

					devicesList.add(json);
					writeDevList(s, devicesList);
					return true;

				} 
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		}
		
		return false;
		
		
	}
	
	
	public static boolean deleteDevice(HttpServlet s, JSONObject json) {
		System.out.println("del device...");
		
		if (devicesList.size() == 0) {
			devicesList = readDevList(s);
		}
		//for(JSONObject jsList:devicesList)
		for(int i=0;i<devicesList.size();i++)
		{
			JSONObject jsList = devicesList.get(i);	
			
		System.out.println("jslist = "+jsList.toString());
		System.out.println("json = "+json.toString());
		try {
			String name1 = jsList.getString("device_name");
			String name2 = json.getString("device_name");
			
			String id1 = jsList.getString("device_id");
			String id2 = json.getString("device_id");
			
			if(name1.equals(name2)&&id1.equals(id2)){
				
				if(devicesList.size()>i) devicesList.remove(i);
				System.out.println("devlist after rem = "+devicesList);
				writeDevList(s, devicesList);
				System.out.println("exist!");
				return true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
		
				
		return false;
	}
	
	
}
