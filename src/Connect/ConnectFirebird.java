package Connect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ConnectFirebird implements Firebird {

	private static Connection con = null;
	private static ConnectFirebird connectDB = null;

	private static String errorMsg = "";

	public static ConnectFirebird getConnectFirebird(String driver, String url, String login, String pass,
			String charset) {
		if (connectDB == null) {
			synchronized (ConnectFirebird.class) {
				if (connectDB == null) {
					System.out.println("++++++++++++++ connect is null");
					connectDB = new ConnectFirebird(driver, url, login, pass, charset);
				}
			}
		}
		return connectDB;

	}

	@SuppressWarnings("unused")
	private ConnectFirebird() {
	}

	private ConnectFirebird(String driver, String url, String login, String pass, String charset) {
		System.out.println("create singleton of connectDB...");
		errorMsg = "";
		try {
			Class.forName(driver);

			Properties connParam = new Properties();
			connParam.put("user", login);
			connParam.put("password", pass);
			connParam.put("lc_ctype", charset);

			con = DriverManager.getConnection(url, connParam);
			if (con == null)
				System.out.println("con = null");

		} catch (ClassNotFoundException ex) {
			System.err.println("KFDB.Cannot find this db driver classes.");
			ex.printStackTrace();
			errorMsg = ex.toString();
		} catch (SQLException e) {
			System.err.println("KFDB.Cannot connect to this db.");
			e.printStackTrace();
			errorMsg = e.toString();
		}
	}

	/**
	 * Получить данные по запросу из базы
	 */
	public ArrayList<ArrayList<Object>> getNomen(String query) {
		ArrayList<ArrayList<Object>> retList = new ArrayList<ArrayList<Object>>();
		try {
			if (con == null) {
				System.out.println("con = null");
				return null;
			}
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();

			System.out.println("cols cnt = " + cols);

			for (int cl = 1; cl <= cols; cl++) {
				try {
					String col_name = rsmd.getColumnName(cl);
					System.out.println("----col " + col_name);
				} catch (Exception e) {
					System.out.println("eee " + e.toString());
				}

				/*
				 * String col_name = rsmd.getColumnName(cl); if(col_name==null){
				 * System.out.println("----col null!!!"); } else
				 * System.out.println(">>>>>"+col_name);
				 */
			}

			Properties connInfo = new Properties();
			connInfo.put("charSet", "UNICODE_FSS");

			while (rs.next()) {
				ArrayList<Object> newRow = new ArrayList<Object>();
				for (int i = 1; i <= cols; i++) {
					newRow.add(rs.getObject(i));

				}
				retList.add(newRow);
				for (int j = 0; j < listeners.size(); j++) {
					ConnectListener listener = (ConnectListener) listeners.get(j);
					listener.setSelectedRow(newRow);
				}
			}
			rs.close();
			st.close();

		} catch (SQLException e) {
			System.err.println("KFDB.There are problems with the query ******" + query);
			e.printStackTrace();
		}

		for (int j = 0; j < listeners.size(); j++) {
			ConnectListener listener = (ConnectListener) listeners.get(j);
			listener.setSelectedRow(null);
		}

		return retList;
	}

	/**
	 * Получить данные по запросу из базы в формате JSON
	 */
	public JSONObject getJSONNomen(String query) {
		JSONObject jsonObject = new JSONObject();
		ArrayList<ArrayList<Object>> retList = new ArrayList<ArrayList<Object>>();

		try {
			if (con == null) {
				System.out.println("con js = null");
				try {
					jsonObject.put("status", "error");
					jsonObject.put("name", "error");
					jsonObject.put("details", "connect = null");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return jsonObject;
			}
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();

			System.out.println("cols cnt = " + cols);

			/*
			 * for(int cl = 1;cl<=cols;cl++){ try { String col_name =
			 * rsmd.getColumnName(cl);
			 * 
			 * System.out.println("----col "+col_name+" lab ="
			 * +rsmd.getColumnLabel(cl)+"  type="+rsmd.getColumnTypeName(cl)+
			 * " class="+rsmd.getColumnClassName(cl)); } catch (Exception e) {
			 * System.out.println("eee "+e.toString()); }
			 * 
			 * 
			 * }
			 */

			Properties connInfo = new Properties();
			connInfo.put("charSet", "UNICODE_FSS");

			String tableName = "table";
			if (cols >= 1)
				tableName = rsmd.getTableName(1);
			System.out.println("table name = " + tableName);

			JSONArray arrayList = new JSONArray();

			while (rs.next()) {
				// ArrayList<Object> newRow = new ArrayList<Object>();
				JSONObject jsobj = new JSONObject();

				// for (int i = 1; i <= cols; i++)
				for (int i = cols; i >= 1; i--) {
					// newRow.add(rs.getObject(i));
					Object obj = rs.getObject(i);
					String col_name = rsmd.getColumnLabel(i);// rsmd.getColumnName(i);
					try {
						jsobj.put(col_name, obj);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				// retList.add(newRow);
				arrayList.put(jsobj);
				/*
				 * for(int j=0;j<listeners.size();j++) { ConnectListener
				 * listener = (ConnectListener)listeners.get(j);
				 * listener.setSelectedRow(newRow); }
				 */
			}

			try {
				jsonObject.put("status", "success");
				jsonObject.put("name", tableName);
				jsonObject.put("data", arrayList);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			rs.close();
			st.close();

		} catch (SQLException e) {
			System.err.println("KFDB.There are problems with the query ******" + query);
			e.printStackTrace();
		}

		for (int j = 0; j < listeners.size(); j++) {
			ConnectListener listener = (ConnectListener) listeners.get(j);
			listener.setSelectedRow(null);
		}

		// System.out.println("---***-- "+jsonObject.toString());

		return jsonObject;
	}

	/**
	 * Получить данные о картах зала с изображениями
	 */
	public JSONObject getTablesHallsJSON() {
		JSONObject jsonObject = new JSONObject();
		ArrayList<ArrayList<Object>> retList = new ArrayList<ArrayList<Object>>();

		try {
			if (con == null) {
				System.out.println("con js = null");
				try {
					jsonObject.put("status", "error");
					jsonObject.put("name", "error");
					jsonObject.put("details", "connect = null");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return jsonObject;
			}
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select  dtg.id, dtg.name, dtg.map from dic_tables_grp dtg");
			ResultSetMetaData rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();

			System.out.println("cols cnt = " + cols);

			Properties connInfo = new Properties();
			connInfo.put("charSet", "UNICODE_FSS");

			String tableName = "table";
			if (cols >= 1)
				tableName = rsmd.getTableName(1);
			System.out.println("table name = " + tableName);

			JSONArray arrayList = new JSONArray();

			while (rs.next()) {

				JSONObject jsobj = new JSONObject();

				for (int i = cols; i >= 1; i--) {

					if (i == 0 || i == 1) {
						String col_name = rsmd.getColumnLabel(i);
						Object obj = rs.getObject(i);
						try {
							jsobj.put(col_name, obj);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (i == 2) {
						if (rs.getObject(2) != null) {
							String col_name = rsmd.getColumnLabel(i);
							System.out.println("is image! " + col_name);
							
							String imageString="";
							 Blob imageByte = rs.getBlob(3);
							 if(imageByte!=null)
							 {
								// String str = new String(imageByte.getBytes(1l, (int) imageByte.length()));
								 
								// String pdfBase64String = StringUtils.newStringUtf8(Base64.encodeBase64(ba.toByteArray()));
								 

								 StringBuffer strOut = new StringBuffer();
								 String aux;
								 // We access to stream, as this way we don't have to use the CLOB.length() which is slower...
								 // assuming blob = resultSet.getBlob("somefield");
								 BufferedReader br = new BufferedReader(new InputStreamReader(imageByte.getBinaryStream()));
								 try {
									while ((aux=br.readLine())!=null) {
										 strOut.append(aux);
									 }
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								 imageString=strOut.toString();
								 
								 
								 
								 //imageString=str;
								 String fragment = imageString.substring(0, 100);
								 System.out.println("blob str = "+fragment);
							 }
							 try {
									jsobj.put(col_name, imageString);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							
							/*String url = "D:\\hall_" + i + ".jpg";

							File image = new File(url);
							FileOutputStream fos = null;
							try {
								fos = new FileOutputStream(image);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							byte[] buffer = new byte[1];
							InputStream is = rs.getBinaryStream(3);
							if (is != null) {

								String imageString ="";
								System.out.println("img not nul =)");
								try {
									while (is.read(buffer) > 0) {
										try {
											fos.write(buffer);
											
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									try {
										fos.close();
										
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									
									
									 
									 try {
											jsobj.put(col_name, imageString);
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									 
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								
							} else {
								System.out.println("empt img");
								try {
									jsobj.put(col_name, "");
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}*/
						}
					} else {
						System.out.println("empty image");
					}
				}

				arrayList.put(jsobj);

			}

			try {
				jsonObject.put("status", "success");
				jsonObject.put("name", tableName);
				jsonObject.put("data", arrayList);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			rs.close();
			st.close();

		} catch (SQLException e) {
			System.err.println("KFDB.There are problems with the query for selecting  TABLES GRP");
			e.printStackTrace();
		}

		for (int j = 0; j < listeners.size(); j++) {
			ConnectListener listener = (ConnectListener) listeners.get(j);
			listener.setSelectedRow(null);
		}

		// System.out.println("---***-- "+jsonObject.toString());

		return jsonObject;
	}
	
	
	
	/*
	 * private byte[] data = null; String encodedImage=null;
	 * 
	 * public void addImage(Bitmap imageBitmap) { if(imageBitmap!=null){
	 * ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 * imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos); data =
	 * bos.toByteArray(); encodedImage = bitmapToString(imageBitmap);
	 * 
	 * } }
	 */

	/*public String byteToString(byte[] b) {
		
		
		String temp = null;
		try {
			System.gc();
			temp = Base64.e
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			
			temp = Base64.encodeToString(b, Base64.getDecoder().D);
			System.out.println("PictureDemo  Out of memory error catched");
		}
		return temp;
	}*/

	/**
	 * Выполнить запрос в потоке с построчным возвращением результата
	 * 
	 * @param query
	 */
	public void getFireNomen(String query) {
		FireNomen fn = new FireNomen(query);
		fn.start();
	}

	class FireNomen extends Thread {
		String query = "";

		public FireNomen(String query) {
			this.query = query;
		}

		public void run() {
			try {
				if (con == null)
					System.out.println("connect = null");
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(query);
				ResultSetMetaData rsmd = rs.getMetaData();
				int cols = rsmd.getColumnCount();
				Properties connInfo = new Properties();
				connInfo.put("charSet", "UNICODE_FSS");
				while (rs.next()) {
					ArrayList<Object> newRow = new ArrayList<Object>();
					for (int i = 1; i <= cols; i++) {
						newRow.add(rs.getObject(i));
					}
					for (int j = 0; j < listeners.size(); j++) {
						ConnectListener listener = (ConnectListener) listeners.get(j);
						listener.setSelectedRow(newRow);
					}

				}
				rs.close();
				st.close();
			} catch (SQLException e) {
				System.err.println("KFDB.There are problems with the query ******" + query);
				e.printStackTrace();
			}
		}
	}

	/**
	 * Выполнить запрос
	 */
	public String setNomen(String query) {
		try {

			Statement st = con.createStatement();
			int q = st.executeUpdate(query);
			String msg = "";
			msg = Integer.toString(q);
			return msg;

		} catch (SQLException e) {
			System.err.println("KFDB.There are problems with the query ****** " + query);
			e.printStackTrace();
			String err = e.getMessage();
			return err;
		}
	}

	/**
	 * Выполнить запрос
	 */
	public JSONObject setJSONNomen(String query) {

		JSONObject jsonObject = new JSONObject();
		try {

			Statement st = con.createStatement();
			if (con == null) {
				System.out.println("con js = null");
				try {
					jsonObject.put("status", "error");
					jsonObject.put("details", "connect = null");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return jsonObject;
			}
			int q = st.executeUpdate(query);
			String msg = "";
			msg = Integer.toString(q);
			try {
				jsonObject.put("result", "success");
				jsonObject.put("details", msg);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsonObject;

		} catch (SQLException e) {
			System.err.println("KFDB.There are problems with the query ****** " + query);
			e.printStackTrace();
			String err = e.getMessage();
			try {
				jsonObject.put("result", "error");
				jsonObject.put("details", err);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			return jsonObject;
		}
	}

	private ArrayList listeners = new ArrayList();

	public void addListener(ConnectListener listener) {
		this.listeners.add(listener);
	}

	public static String getErrorMsg() {
		return errorMsg;
	}

}// end of class
