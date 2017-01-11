package Utils;

import java.io.UnsupportedEncodingException;

public abstract class TXTEncoding {
	
	public static String encodeUTF(String input)
	{
		String output="";
		
		 try {
			 output = new String(input.getBytes("ISO-8859-1"), "UTF-8");
			 
			} catch (UnsupportedEncodingException e) {e.printStackTrace();
			output=input;
			}
		 return output;

	}

}
