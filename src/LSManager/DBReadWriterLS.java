package LSManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

public class DBReadWriterLS {
	
	
	
	private static String hardId=null;
	private static String key=null;
	
	public DBReadWriterLS() {
		
		
	}
	
	
	
	/*
	public static String  getHardId(HttpServlet s) {
		ArrayList<String> list = new ArrayList<String>();
		// String path
		// =getServletContext().getRealPath("/WebContent/db_connect.txt");
		String path = s.getServletContext().getRealPath("/WEB-INF/conf/reg_id.txt");

		try {

			File file = new File(path);

			System.out.println("serv=" + s.getServletName() + "  my reading file: " + file.getAbsolutePath());

			BufferedReader reader = null;

			try {
				FileReader fReader = new FileReader(file);
				try {
					reader = new BufferedReader(fReader);
					String text = null;

					while ((text = reader.readLine()) != null) {
						list.add(text);
					}
				} catch (Exception e) {
					System.out.println("err1 "+e.toString());
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("err2 "+e.toString());
				
				IGenId igen = new IGenId();
				
				String originWsId = igen.getIh();
				
				System.out.println("Файл не найден, зашиваем оригинальный ключ "+originWsId);
				//хешируем оригинальный ключ
				String originIdHash="";
				try {
					LocalEncrypter localss = new LocalEncrypter();
					originIdHash = localss.genHashId(originWsId);
					
				} catch (Exception e3) {
					e.printStackTrace();
				}
				System.out.println("orig id = "+originIdHash);
				
				//Записываем его в базу
				setHardId(s, originIdHash);
				
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("err3 "+e.toString());
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

		if (list.size() > 0) {
			hardId = list.get(0);
		}

		return hardId;
	}

	
	
	public static void setHardId(HttpServlet s, String hardId) {

		System.out.println("write hard id...");
		String path = s.getServletContext().getRealPath("/WEB-INF/conf/reg_id.txt");
		try {
			// Whatever the file path is.
			File statText = new File(path);
			System.out.println("serv=" + s.getServletName() + " my writing file: " + statText.getAbsolutePath());

			FileOutputStream fos = new FileOutputStream(statText);

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			
				bw.write(hardId);
				bw.newLine();
			

			bw.close();
			fos.close();

		} catch (IOException e) {
			System.err.println("Problem writing to the file statsTest.txt");
		}

	}*/
	
	public static String  getKey(HttpServlet s) {
		ArrayList<String> list = new ArrayList<String>();
		// String path
		// =getServletContext().getRealPath("/WebContent/db_connect.txt");
		String path = s.getServletContext().getRealPath("/WEB-INF/conf/reg_key.txt");

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

		if (list.size() > 0) {
			key = list.get(0);
		}

		return key;
	}

	
	
	public static void setKey(HttpServlet s, String key) {

		String path = s.getServletContext().getRealPath("/WEB-INF/conf/reg_key.txt");
		try {
			// Whatever the file path is.
			File statText = new File(path);
			System.out.println("serv=" + s.getServletName() + " my writing file: " + statText.getAbsolutePath());

			FileOutputStream fos = new FileOutputStream(statText);

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			
				bw.write(key);
				bw.newLine();
			

			bw.close();
			fos.close();

		} catch (IOException e) {
			System.err.println("Problem writing to the file statsTest.txt");
		}

	}

	
	
	
	
	
	
	
	
	
	
	
	
	

	

	
	
	

}
