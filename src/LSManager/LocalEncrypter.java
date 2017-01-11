package LSManager;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class LocalEncrypter {

	private static String algorithm = "DESede";
	private static Key key = null;
	private static Cipher cipher = null;
	
	private static void setUp() throws Exception {
		
		key = new Key() {
			
			public String getFormat() {
				return "RAW";
			}
			
			public byte[] getEncoded() {

				byte[] keyBytes = null;
				keyBytes = new byte[24];
				
				keyBytes[0]=79;		keyBytes[1]=104;	keyBytes[2]=-111;		keyBytes[3]=127;		keyBytes[4]=-36;		keyBytes[5]=-22;					
				keyBytes[6]=19;		keyBytes[7]=91;		keyBytes[8]=-123;		keyBytes[9]=-111;		keyBytes[10]=19;		keyBytes[11]=-128;				
				keyBytes[12]=-63;	keyBytes[13]=112;	keyBytes[14]=124;   	keyBytes[15]=94;		keyBytes[16]=61;		keyBytes[17]=-27;			
				keyBytes[18]=127;	keyBytes[19]=-48;	keyBytes[20]=-50;		keyBytes[21]=124;		keyBytes[22]=-70;		keyBytes[23]=-23;
				
				return keyBytes;
			}
			
			
			public String getAlgorithm() {
				return "DESede";
			}
		};
		
		cipher = Cipher.getInstance(algorithm);
	}
	
	/**
	 * Шифруем чистое значениет
	 * @param byteString
	 * @return возвращает хэш
	 */
	public String parseByte(String byteString)
	{
		//System.out.println(byteString);
		
		for(int i=0;i<byteString.length();i++)
		{
			String str = byteString.substring(i, i+1);
			
			if(str.equals(" "))
			{
				int a=65+generateRandom(23);
				char ch= (char)a;
				str = Character.toString(ch);
			//	System.out.println(str);
				
				byteString = byteString.substring(0,i)+str+byteString.substring(i+1,byteString.length());
			}
			
			if(str.equals("-"))
			{
				int a=88+generateRandom(3);
				char ch= (char)a;
				str = Character.toString(ch);
			//	System.out.println(str);
				
				byteString = byteString.substring(0,i)+str+byteString.substring(i+1,byteString.length());
			}
			
			
		}
		
		//System.out.println(byteString);
		
		return byteString;
	}
	
	/**
	 * Дешифруем хэш
	 * @param byteString
	 * @return на выходе чистое  исходное значение
	 */
	public String unparseByte(String byteString)
	{
		//System.out.println(byteString);
		
		for(int i=0;i<byteString.length();i++)
		{
			String str = byteString.substring(i, i+1);
			
			char[] chr = str.toCharArray();
			char ch = chr[0];
			int in = (int)ch;
			
			if(in>=65&&in<=87)
			{
				
				str = " ";
							
				byteString = byteString.substring(0,i)+str+byteString.substring(i+1,byteString.length());
			}
			
			if(str.equals("X")||str.equals("Y")||str.equals("Z"))
			{
				str = "-";
			//	System.out.println(str);
				
				byteString = byteString.substring(0,i)+str+byteString.substring(i+1,byteString.length());
			}
			
			
		}
		try {
			setUp();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//System.out.println(byteString);
		
		byte[] encryptionBytes = null;
		
		encryptionBytes = byteStringtoByte(byteString);
		
		String originalString="";
		
		try {
			 originalString = decrypt(encryptionBytes);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return originalString;
	}
	
	/**
	 * Преобразуем стринг список байт в массив байтов
	 * @return
	 */
	private byte[] byteStringtoByte(String str)
	{
		byte[] encryptionBytes = null;
		
		int calcspace=0;
		int currentA=0;
		
		ArrayList<Integer> list = new ArrayList<>();
		
		for(int i=0;i<str.length();i++)
		{
			if(str.substring(i,i+1).equals(" "))
			{
				calcspace+=1;
				
				String snumber = str.substring(currentA,i);
				int a = Integer.parseInt(snumber);
				list.add(a);
				
				currentA=i+1;
				
			}
			if(i==str.length()-1)
			{
				calcspace+=1;
				
				String snumber = str.substring(currentA,i+1);
				int a = Integer.parseInt(snumber);
				list.add(a);
			}
			
		}
		
		//System.out.println(list+" "+list.size());
		
		encryptionBytes = new byte[list.size()];
		
		int n;
		for(int i=0;i<list.size();i++)
		{
			n=list.get(i);
			encryptionBytes[i]=(byte)n;
		}
		
		return encryptionBytes;
	}
	
	
	
	
	 private static Random random = new Random();

	    static int generateRandom(int n) {
	        return Math.abs(random.nextInt()) % n;
	    }
	

	 /**
	  * На основе ид генерим хэш
	  * @param id 
	  * @return
	  */
	 public String genHashId(String id) throws Exception 
	 {
			setUp();
			
			byte[] encryptionBytes = null;
			
			encryptionBytes = encrypt(id);//зашифровываем
			
			String byteString = byteToString(encryptionBytes);
			
			//System.out.println("//*"+byteString);
			
			byteString = parseByte(byteString);
		
		return byteString;
	 }
	    
	 /**
	  * Преобразуем чистый массив байт в байт стринг
	  * @param bt
	  * @return
	  */
	 String byteToString(byte[] bt)
	 {
		 String str="";
		 
		 for(int i=0;i<bt.length;i++)
		 {
			 int a=bt[i];
			 str+=Integer.toString(a);
			 str+=" ";
		 }
		 
		return str.trim();
	 }
	 
	 
	
	private static byte[] encrypt(String input)	throws InvalidKeyException,	BadPaddingException,	IllegalBlockSizeException {

		cipher.init(Cipher.ENCRYPT_MODE, key);

		byte[] inputBytes = input.getBytes();

		return cipher.doFinal(inputBytes);

	}

	private static String decrypt(byte[] encryptionBytes)	throws InvalidKeyException,	BadPaddingException,	IllegalBlockSizeException 
	{

		cipher.init(Cipher.DECRYPT_MODE, key);
		
		
		byte[] recoveredBytes =	cipher.doFinal(encryptionBytes);

		String recovered =	new String(recoveredBytes);

		return recovered;

	}

}