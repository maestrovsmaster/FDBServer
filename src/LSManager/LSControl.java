package LSManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServlet;

public class LSControl {

	private String wsId;

	IGenId igen;
	LocalEncrypter localss;
	DBReadWriterLS dbread;

	private String originWsId; // оригинальный ид компа
	private String hashId = "";

	private boolean isLicensed = false;

	private String inputKey = "";// вводимый ключ активации

	HttpServlet servlet;

	private int devCount = 0;

	public LSControl(HttpServlet s) {
		servlet = s;

		localss = new LocalEncrypter();

		// Получаем оригинальный ид устройства
		igen = new IGenId();

		originWsId = igen.getIh();
		System.out.println("o id " + originWsId);
		try {
			hashId = localss.genHashId(originWsId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		controlKey();

		/*
		 * //теперь сверяем с тем ИД, что в базе dbread = new DBReadWriterLS();
		 * 
		 * String dbHashId = dbread.getHardId(servlet); hashId=dbHashId;
		 * 
		 * System.out.println("dbHashId ***"+dbHashId);
		 * 
		 * String dbId=""; try { dbId = localss.unparseByte(dbHashId);
		 * }catch(Exception e) { }
		 * 
		 * System.out.println("originWsId "+originWsId+"    basa = "+dbId);
		 * 
		 * if(!originWsId.equals(dbId))//если ключи не совпадают { //хештруем
		 * оригинальный ключ String originIdHash=""; try { originIdHash =
		 * localss.genHashId(originWsId);
		 * 
		 * } catch (Exception e) { e.printStackTrace(); } System.out.println(
		 * "orig id = "+originIdHash);
		 * 
		 * //Записываем его в базу dbread.setHardId(servlet, originIdHash);
		 * //dbread.setKey(servlet, "");
		 * ///!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! hashId =
		 * dbread.getHardId(servlet); isLicensed = false; } else//если ключи
		 * совпадают, проверяем хард { controlKey();
		 * 
		 * }//если ключи совпадают, проверяем хард
		 * 
		 */
	}

	private boolean checkDate = true;

	/**
	 * Совпали ли даты
	 * 
	 * @return
	 */
	public boolean isCheckDate() {
		return checkDate;
	}

	/**
	 * Проверяем совпадение ключей
	 */
	private void controlKey() {

		String keydbHash = dbread.getKey(servlet);
		System.out.println("keyHash = " + keydbHash);
		if (keydbHash == null) {
			// dbread.setKey(servlet,"");
			System.out.println("LSControl key null");
			isLicensed = false;
		} else if (!keydbHash.equals("")) {
			System.out.println(" control ");
			String keydb = "";
			try {
				keydb = localss.unparseByte(keydbHash);
			} catch (Exception e) {
			}

			System.out.println("key = " + keydb);
			String idString = keydb.substring(0, 5);
			System.out.println("data= " + idString);

			if (originWsId.equals(idString)) // если зашитый и оригинальный
												// ключи совпадают, проверяем
												// даты
			{
				// System.out.println("control data.... ");
				if (isInDiapazonDat(keydb)) {
					System.out.println("diapazon ok");

					// если диапазон дат совпадает, проверяем кол-во устройств в
					// лицензии
					System.out.println("keydb === " + keydb);
					String devCnt = keydb.substring(15);
					System.out.println("devCnt =" + devCnt);

					try {
						devCount = Integer.parseInt(devCnt);
						System.out.println("int dev cnt = " + devCnt);
					} catch (Exception e) {
						// TODO: handle exception
					}

					isLicensed = true;
				} else {
					System.out.println("diapazon NE ok(");
					isLicensed = false;
					checkDate = false;
				}

			} else {
				System.out.println("no sovp");
				dbread.setKey(servlet, "");
				isLicensed = false;
			}
		} else {
			dbread.setKey(servlet, "");
			isLicensed = false;
		}
	}

	String dateFrom = "";
	String dateTo = "";

	/**
	 * Попадаем ли мы в диапазон, указанный в ключе
	 * 
	 * @param dataOfKey
	 *            чистый, не хешированный ключ из базы
	 * @return
	 */
	private boolean isInDiapazonDat(String dbKey) {

		String dataString = dbKey.substring(5, 15);

		System.out.println("DATE STRING=" + dataString);
		// from
		int keyDataFrom = Integer.parseInt(dataString.substring(0, 2));
		int keyMonthFrom = Integer.parseInt(dataString.substring(2, 4));
		int keyYearFrom = Integer.parseInt(dataString.substring(4, 5));
		// to
		int keyDataTo = Integer.parseInt(dataString.substring(5, 7));
		int keyMonthTo = Integer.parseInt(dataString.substring(7, 9));
		int keyYearTo = Integer.parseInt(dataString.substring(9, 10));

		dateFrom = keyDataFrom + "." + keyMonthFrom + ".201" + keyYearFrom;
		dateTo = keyDataTo + "." + keyMonthTo + ".201" + keyYearTo;

		// now
		System.out.println("now");
		Calendar cal = new GregorianCalendar();

		int nowYear = cal.getTime().getYear() - 110;
		int nowMonth = 1 + cal.getTime().getMonth();
		int nowDay = cal.getTime().getDate();

		System.out.println("from year= " + keyYearFrom + " from month= " + keyMonthFrom + " from day=  " + keyDataFrom);
		System.out.println("now  year= " + nowYear + " now  month= " + nowMonth + " now  day=  " + nowDay);
		System.out.println("to   year= " + keyYearTo + " to   month= " + keyMonthTo + " to   day=  " + keyDataTo);

		if (nowYear < keyYearFrom || nowYear > keyYearTo) {
			System.out.println(">>1");
			return false;
		} else {

			if (nowYear > keyYearFrom && nowYear < keyYearTo) {
				System.out.println(">>2");
				return true;
			} else {
				
				if(nowYear==keyYearFrom&&nowYear==keyYearTo)
				{
					
					//check month
					if(nowMonth<keyMonthFrom||nowMonth>keyMonthTo){
						System.out.println(">>3");
						return false;
					}
					else return true;
					
					
				}else{
				if(nowYear== keyYearFrom)
				{
					//check month
					if(nowMonth>=keyMonthFrom) return true;
					else return false;
				}
				else
				if(nowYear== keyYearTo)
				{
					//check month
					if(nowMonth<=keyMonthTo) return true;
					else return false;
				}
				else{
					System.out.println("unknown condition");
					return true;
				}
				
				}

			}

		}

	}

	/**
	 * Успешно ли выполнено лицензирование
	 * 
	 * @return
	 */
	public boolean isLicensed() {
		return isLicensed;
	}

	/**
	 * Берем хэш ид компа
	 * 
	 * @return
	 */
	public String getHashId() {
		return hashId;
	}

	public void setInputKey(String inputKey) {
		this.inputKey = inputKey;

		dbread.setKey(servlet, this.inputKey);

		controlKey();
	}

	public int getDevCount() {
		return devCount;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public String getOriginWsId() {
		return originWsId;
	}

}
