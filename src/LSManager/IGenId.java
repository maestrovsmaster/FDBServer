package LSManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * √енерим ид
 * @author ¬асил≥й
 *
 */
public class IGenId {
	
	private static int lenght = 5;
	
	private String hardid="";
	
	public IGenId() {
	
		String path = "C:\\Users\\All Users\\ils.rn";
		//String path = "C:\\ProgramData\\ils.rn";
		
		
		String str =read(path);
		
		if(str==null||str.equals("")){
			System.out.println("no lcns");
			
			String randstr = genId(lenght);
			
			System.out.println(randstr);
			
			//теперь пишем ее
			write(path, randstr);
			
			//и вновь читаем
			str =read(path);
			hardid=str;
			
		}
		else{
			//System.out.println("*"+str+"*");
		hardid=str;	
		}
		
	}
	
	/**
	 * 
	 * @param lenght кол-во цифр
	 * @return
	 */
	private String genId(int lenght)
	{
		String rndstr="";
		
		for(int i=0;i<lenght;i++)
		{
			int a = generateRandom(10);
			
			//System.out.println(a);
			rndstr+=Integer.toString(a);
			
		}
		
		return rndstr;
		
	}
	
	 private static Random random = new Random();

	    static int generateRandom(int n) {
	        return Math.abs(random.nextInt()) % n;
	    }
	
	
	
    public static String read(String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader( new File(fileName).getAbsoluteFile()));
            try {
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                    //sb.append("\n");
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
           
        }
        return sb.toString();
    }
	
  
    public static void write(String fileName, String text) {
        try {
            PrintWriter out = new PrintWriter(new File(fileName).getAbsoluteFile());
            try {
                out.print(text);
            } finally {
                out.close();
            }
        } catch(IOException e) {
            //throw new RuntimeException(e);
        }
    }

	public String getIh() {
		return hardid;
	}
	
    

}
