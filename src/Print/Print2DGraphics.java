package Print;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.GlyphVector;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;

public class Print2DGraphics implements Printable {

	Doc doc = null;

	DocPrintJob pj;

	PrintRequestAttributeSet aset;

	String printewName;
	int paperWight;

	boolean isPrinterExist = false;

	public Print2DGraphics(String printewName, int paperWight)
			throws PrintException {

		this.printewName = printewName;
		this.paperWight = paperWight;

		/*
		 * Construct the print request specification. The print data is a
		 * Printable object. the request additonally specifies a job name, 2
		 * copies, and landscape orientation of the media.
		 */
		DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
		aset = new HashPrintRequestAttributeSet();

		aset.add(new JobName("My job", null));

		/* locate a print service that can handle the request */
		int k = -1;
		PrintService[] services = PrintServiceLookup.lookupPrintServices(
				flavor, aset);

		if (services.length > 0) {
			for (int i = 0; i < services.length; i++) {
				System.out.println(i + "=    " + services[i].getName() + i);

				if (services[i].getName().equals(printewName)) {
					k = i;
					isPrinterExist = true;
				}
			}
			// if(printewName.equals(""))

			if (isPrinterExist) {
				/* create a print job for the chosen service */
				PrintService printer = services[k];
				
				pj = printer.createPrintJob();
				
			
				
				

				DocAttributeSet docAttributes = new HashDocAttributeSet();

				MediaPrintableArea mediaPrintArea = new MediaPrintableArea(0,
						0, 72, 1500, MediaPrintableArea.MM);
				docAttributes.add(mediaPrintArea);

				/*
				 * MediaSize mediaSize = new MediaSize(1, 1, MediaSize.MM);
				 * docAttributes.add(mediaSize );
				 */

				docAttributes.add(OrientationRequested.PORTRAIT);
				PrintRequestAttributeSet printAttributes = new HashPrintRequestAttributeSet();
				// printAttributes.add(new Copies(2));
				doc = new SimpleDoc(this, flavor, docAttributes);

			}// is printer exist

		}
	}

	private int TYPE = -1; // ��� ���-�� -1 -�� ��������; 1-������ 2-�����;
							// 3-������; 4-���;

	public int print(Graphics g, PageFormat pf, int pageIndex) {

		Paper paper = new Paper();
		paper.setSize(2200, 130); // Large Address Dimension
		paper.setImageableArea(0, 0, 222, 172);
		pf.setPaper(paper);
		pf.setOrientation(PageFormat.PORTRAIT);

		if (pageIndex == 0) {
			Graphics2D g2d = (Graphics2D) g;

			g2d.translate(0, 0);

			g2d.setColor(Color.black);

			if (TYPE == 1)
				printZakazOtkaz(g2d, "�����:");
			if (TYPE == 2)
				printZakazOtkaz(g2d, "!!!�����:");
			if (TYPE == 3)
				printCheckPrechek(g2d, "��������� �������", 0);
			if (TYPE == 4)
				printCheckPrechek(g2d, "���", 1);

			return Printable.PAGE_EXISTS;
		} else {
			return Printable.NO_SUCH_PAGE;
		}
	}

	private String checknum = "";
	private String employeName = "";
	private String tablenum = "";
	String subdiv = "";
	private ArrayList<ArrayList<String>> list = null;

	/**
	 * ������ �� ����� ������� � �������
	 * 
	 * @return
	 */
	public boolean isPrinterExist() {
		return isPrinterExist;
	}

	/**
	 * �������� �����: ������������� ����� ���� ������������ ������� ����� �����
	 * �����: 0- ������������ 1- �����
	 * 
	 * @param list
	 */
	public boolean printZakaz(String subdiv, String checknum,
			String employeName, String tablenum,
			ArrayList<ArrayList<String>> list) {
		this.checknum = checknum;
		this.employeName = employeName;
		this.tablenum = tablenum;
		this.list = list;
		this.subdiv = subdiv;

		TYPE = 1;

		if (isPrinterExist) {

			try {
				pj.print(doc, aset);
			} catch (PrintException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TYPE = -1;
			return true;
		} else {
			TYPE = -1;
			return false;
		}

	}

	/**
	 * �������� �����: ������������� ����� ���� ������������ ������� ����� �����
	 * �����: 0- ������������ 1- �����
	 * 
	 * @param list
	 */
	public boolean printOtkaz(String subdiv, String checknum,
			String employeName, String tablenum,
			ArrayList<ArrayList<String>> list) {
		this.checknum = checknum;
		this.employeName = employeName;
		this.tablenum = tablenum;
		this.list = list;
		this.subdiv = subdiv;

		TYPE = 2;

		if (isPrinterExist) {
			try {
				pj.print(doc, aset);
			} catch (PrintException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TYPE = -1;
			return true;
		} else {
			TYPE = -1;
			return false;
		}
	}

	private void printZakazOtkaz(Graphics2D g2d, String type) {
		int W = 50;// paperWight;// ������ �����

		double koef = 2.8;// ���� ��������� ����� � ����������

		// g2d.drawRect(0, 0, (int)(W*koef), 1000);

		int cuyrentY = 15; // ������� ������
		int stepY = 10; // ��� �������

		int currentX = 5; // ������� ��������� �� � �����
		int currentX1 = 8; // ������� ��������� �� � ������
		
		int kolvWight = (int) (15*koef);
		int nameWight = (int) (W*koef-kolvWight);
		
		Font font = new Font("Arial", 3, 11);

		Font fontModif = new Font("Arial", 3, 8); // ����� �������������

		g2d.setFont(font);

		g2d.drawString(type, currentX + 10, cuyrentY);

		cuyrentY += stepY + 10;

		font = new Font("Arial", 3, 11);
		g2d.drawString(subdiv, currentX1 + 10, cuyrentY);

		cuyrentY += stepY + 10;

		font = new Font("Arial", 1, 9);
		g2d.setFont(font);

		int otstupX = 90;
		g2d.drawString("����� ����:", currentX1, cuyrentY);
		g2d.drawString(checknum, currentX1 + otstupX, cuyrentY);

		cuyrentY += stepY;
		g2d.drawString("���������:", currentX1, cuyrentY);
		g2d.drawString(employeName, currentX1 + otstupX, cuyrentY);

		cuyrentY += stepY;
		g2d.drawString("����:", currentX1, cuyrentY);
		g2d.drawString(tablenum, currentX1 + otstupX, cuyrentY);
		cuyrentY += stepY + 15;

		// �������� �������� ��������
		
		g2d.drawString("������������", currentX1, cuyrentY);
		g2d.drawString("���-��", currentX1 + nameWight , cuyrentY);
		cuyrentY += stepY + 10;

		
		this.font = new Font("Arial", 0, 9);

		for (int i = 0; i < list.size(); i++) {
			
			TRAVEL="L";
			
			String name = "";
			
			if (!list.get(i).get(4).equals("")&&TYPE == 1) {
				name = list.get(i).get(0)+"  (" + list.get(i).get(4) + ")";
				}
			
			else
			{
				name = list.get(i).get(0);
			}
			int y = paintText(name, nameWight, currentX1, cuyrentY, g2d); //������ ������������
			
			
			//TRAVEL="R";
			
			String a = (list.get(i).get(1)).substring(0,(list.get(i).get(1)).indexOf(".") + 3);// ������ ���-��
			paintText(a, kolvWight, currentX1 + nameWight , cuyrentY-9, g2d);

			cuyrentY = y+stepY/2;
			
			
			g2d.setColor(Color.black);
			
			g2d.drawLine(currentX1, cuyrentY, (int)(W*koef)-5, cuyrentY);
			cuyrentY = y+stepY/2;
			
			g2d.setColor(Color.black);

			TRAVEL="L";
			

		}

		/*
		 * FontMetrics fm = g2d.getFontMetrics ();
		 * 
		 * GlyphVector gv = g2d.getFont ().createGlyphVector (
		 * fm.getFontRenderContext (), "Tesdfsdfsdfsdfsdfxt" ); Rectangle
		 * visualBounds = gv.getVisualBounds ().getBounds ();
		 */

		

	}

	String headName;
	String summaryName;

	String totalSum;
	String Nalom;
	String sdacha;

	/**
	 * �������� ������: ������������� ����� ���� ������������ ������� �����
	 * ����� �����: 0- ������������ 1- �����
	 * 
	 * @param list
	 *            ����� ����� ����� �����
	 * @param type
	 *            0 = ������; 1 = ���
	 */
	public boolean printPrechek(String headName, String checknum,
			String employeName, ArrayList<ArrayList<String>> list,
			String totalSum, String Nalom, String sdacha, String summaryName) {
		this.headName = headName;
		this.summaryName = summaryName;
		this.checknum = checknum;
		this.employeName = employeName;
		this.list = list;

		this.totalSum = totalSum;
		this.Nalom = Nalom;
		this.sdacha = sdacha;

		TYPE = 3;

		if (isPrinterExist) {
			try {
				System.out.println("exist precheck printer");
				pj.print(doc, aset);
			} catch (PrintException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TYPE = -1;
			return true;
		} else {
			TYPE = -1;
			System.out.println("NOT exist precheck printer");
			return false;
		}
	}

	/**
	 * �������� ���: ������������� ����� ���� ������������ ������� ����� �����
	 * �����: 0- ������������ 1- �����
	 * 
	 * @param list
	 *            ����� ����� ����� �����
	 * @param type
	 *            0 = ������; 1 = ���
	 */
	public boolean printChek(String headName, String checknum,
			String employeName, ArrayList<ArrayList<String>> list,
			String totalSum, String Nalom, String sdacha, String summaryName) {
		this.headName = headName;
		this.summaryName = summaryName;
		this.checknum = checknum;
		this.employeName = employeName;
		this.list = list;

		this.totalSum = totalSum;
		this.Nalom = Nalom;
		this.sdacha = sdacha;

		TYPE = 4;
		if (isPrinterExist) {

			try {
				pj.print(doc, aset);
			} catch (PrintException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TYPE = -1;
			return true;
		} else {
			TYPE = -1;
			return false;
		}

	}
	
	private int marginLeft = 0;
	private int marginTop = 0;
	
	

	public int getMarginLeft() {
		return marginLeft;
	}

	public void setMarginLeft(int marginLeft) {
		this.marginLeft = marginLeft;
	}

	public int getMarginTop() {
		return marginTop;
	}

	public void setMarginTop(int marginTop) {
		this.marginTop = marginTop;
	}

	private void printCheckPrechek(Graphics2D g2d, String typechek, int type) {
		int W = paperWight;// ������ �����

		System.out.println("Print prechek...");

		double koef = 2.8;// ���� ��������� ����� � ����������

		int cuyrentY = marginTop; // ������� ������
		int stepY = 10; // ��� �������

		int currentX = marginLeft; // ������� ��������� �� � /////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

		Font font = new Font("Arial", 0, 10);
		g2d.setFont(font);

		font = new Font("Arial", 0, 10);
		int y0 = inscribeText(g2d, headName, font, currentX + 10, cuyrentY,
				(int) (55 * koef));

		cuyrentY += y0;

		cuyrentY += stepY + 10;

		font = new Font("Arial", 1, 10);
		g2d.setFont(font);

		int otstupX = 90;

		System.out.println("y=" + cuyrentY);
		g2d.drawString(typechek + " :", currentX, cuyrentY);

		font = new Font("Arial", 0, 9);
		g2d.setFont(font);
		g2d.drawString(checknum, (int) (currentX + 60 * koef), cuyrentY);

		cuyrentY += stepY;
		g2d.drawString("�����:", currentX, cuyrentY);
		g2d.drawString(employeName, currentX + otstupX, cuyrentY);

		cuyrentY += stepY + 15;

		// �������� �������� ��������

		font = new Font("Cambria", 0, 10);
		g2d.setFont(font);
		int nameWight = (int) (50 * koef);

		for (int i = 0; i < list.size(); i++) {
			String name = list.get(i).get(1);
			String kvo = list.get(i).get(3);
			String cena = list.get(i).get(4);
			String sum = list.get(i).get(5);

			g2d.drawString(kvo + " x " + cena, currentX, cuyrentY);
			cuyrentY += stepY;
			int y = inscribeText(g2d, name, font, currentX, cuyrentY, nameWight);
			inscribeText(g2d, sum, font, currentX + nameWight + 15, cuyrentY,
					(int) (20 * koef));
			cuyrentY += y;
		}

		// g2d.drawLine(0, cuyrentY, (int)(80*koef), cuyrentY);
		g2d.drawString(
				"----------------------------------------------------------",
				currentX, cuyrentY);
		cuyrentY += stepY + 5;
		font = new Font("Arial", 1, 10);
		g2d.setFont(font);
		if (type == 0)// ������
		{
			g2d.drawString("�� ������:", currentX, cuyrentY);
			g2d.drawString(totalSum, currentX + 150, cuyrentY);
		} else {// ���
			g2d.drawString("����:", currentX, cuyrentY);
			g2d.drawString(totalSum, currentX + 150, cuyrentY);
		}
		cuyrentY += stepY;
		// g2d.drawLine(0, cuyrentY, (int)(80*koef), cuyrentY);

		font = new Font("Arial", 0, 9);
		g2d.setFont(font);
		g2d.drawString(
				"---------------------------------------------------------------",
				currentX, cuyrentY);
		cuyrentY += stepY;
		if (type == 0) {
			g2d.drawString(timeNow(), currentX, cuyrentY);

		} else {
			g2d.drawString("��Ҳ����", currentX, cuyrentY);
			g2d.drawString(Nalom, currentX + (int) (50 * koef), cuyrentY);
			cuyrentY += stepY;
			g2d.drawString("�����", currentX, cuyrentY);
			g2d.drawString(sdacha, currentX + (int) (50 * koef), cuyrentY);
			cuyrentY += stepY + 5;
			g2d.drawString(timeNow(), currentX, cuyrentY);
		}

		cuyrentY += stepY;
		// ������ ����
		if (type == 1) {
			inscribeText(g2d, summaryName, font, currentX + 10, cuyrentY,
					(int) (50 * koef));
			cuyrentY += stepY + 5;
			g2d.drawString("��Բ�������� ���", currentX + 50, cuyrentY);
		}

	}

	// get current time
	public String timeNow() {
		Calendar now = Calendar.getInstance();

		int day = now.get(Calendar.DATE);
		int mnt = now.get(Calendar.MONTH) + 1;
		int yar = now.get(Calendar.YEAR);

		int hrs = now.get(Calendar.HOUR_OF_DAY);
		int min = now.get(Calendar.MINUTE);
		int sec = now.get(Calendar.SECOND);

		String time = day + "-" + mnt + "-" + yar + " " + zero(hrs) + ":"
				+ zero(min) + ":" + zero(sec);

		return time;
	}

	public String zero(int num) {
		String number = (num < 10) ? ("0" + num) : ("" + num);
		return number; // Add leading zero if needed

	}

	Dimension d;
	Font font = new Font("Arial", 0, 9);
	String TRAVEL = "L"; //������������. L, R , C 
	FontMetrics fm;
	int fh, ascent;
	int space;

	/**
	 * 
	 * @param str
	 * @param wgh ������ �������
	 * @param xx
	 * @param yy
	 * @param g
	 * @return ������� ��������� � ����������
	 */
	public int paintText(String str, int wgh, int xx, int yy, Graphics g) {
		d = new Dimension(wgh, 100);
		g.setFont(font);
		if (fm == null) {
			fm = g.getFontMetrics();
			ascent = fm.getAscent();
			fh = ascent + fm.getDescent();
			space = fm.stringWidth(" ");
		}
		g.setColor(Color.black);
		StringTokenizer st = new StringTokenizer(str);
		int x = xx;
		int nextx;
		int y = yy;
		String word, sp;
		int wordCount = 0;
		String line = "";
		while (st.hasMoreTokens()) {
			word = st.nextToken();
			if (word.equals("<BR>")) {
				drawString(g, line, wordCount, fm.stringWidth(line), y + ascent,xx);
				line = "";
				wordCount = 0;
				x = 0;
				y = y + (fh * 2);
			} else {
				int w = fm.stringWidth(word);
				if ((nextx = (x + space + w)) > d.width) {
					drawString(g, line, wordCount, fm.stringWidth(line), y	+ ascent,xx);
					line = "";
					wordCount = 0;
					x = xx;
					y = y + fh;
				}
				if (x != 0) {
					sp = " ";
				} else {
					sp = "";
				}
				line = line + sp + word;
				x = x + space + w;
				wordCount++;
			}
		}
		int  Y =drawString(g, line, wordCount, fm.stringWidth(line), y + ascent,xx);
		return Y;
	}

	public int drawString(Graphics g, String line, int wc, int lineW, int y,int x) {
		
	if(TRAVEL.equals("C"))	g.drawString(line, (d.width - lineW) / 2, y);// center
		
	if(TRAVEL.equals("L"))	g.drawString(line, x, y);//left
		
	if(TRAVEL.equals("R"))	g.drawString(line, d.width - lineW, y); //right
		
		System.out.println("str= "+line+" y= "+y);
		
		return y;
	}
	

	
	
	/**
	 * �������, ����������� ������ � �������� ������ �������
	 * 	 * @param g	 *            Graphics
	 * @param text	 *            �����
	 * @param font	 *            �����
	 * @param x	 *            �������
	 * @param y	 *            �������
	 * @param wight	 *            ������ �������
	 * @return y - ������������ ������ �������
	 */
	int inscribeText(Graphics g, String text, Font font, int x, int y, int wight) {
		int WSYMB = 0;
		g.setFont(font);

		// g.drawString(text, x, 250);
		FontMetrics fmetr = g.getFontMetrics();
		int wSymb = fmetr.getWidths()[2]; // ������ ������ ������� � �����������
											// �� ������
		int hSymb = fmetr.getHeight(); // ������ ������ �������

		WSYMB = wSymb;

		if (wight < wSymb)
			wight = wSymb;
		int kvoSymb = wight / wSymb - 2; // ����-�� �������� � ����� ������ - 2
											// (�� ������ ������� �� ������ �
											// ����� ������)

		int x1 = x; // start �� �������� �-���������� ������ ���� �����
		int x2 = x1; // ������� ���������� �������

		int y1 = y;

		int a = 0; // ������� ��������
		// int b=0; //������� �����

		String s = ""; // ������� ������ ������

		for (int i = 0; i < text.length(); i++) {
			s = text.substring(i, i + 1);

			if (x2 == x1) {
				if (s.equals(" ")) {
					s = "";
					System.out.println("*");
					x2 -= wSymb;
				}
			}

			g.drawString(s, x2, y1);
			a++;

			if (s.equals("�") || s.equals("�") || s.equals("�")
					|| s.equals("�") || s.equals("�") || s.equals("�")
					|| s.equals("�") || s.equals("W") || s.equals("M")
					|| s.equals("�") || s.equals("O") || s.equals("C")) {
				wSymb += 5;
				// System.out.println("+++");
			}
			if (s.equals("�") || s.equals("�") || s.equals("�")
					|| s.equals("�") || s.equals("�") || s.equals("w")
					|| s.equals("m")) {
				wSymb += 3;
				// System.out.println("+++");
			}

			else {
				if (s.equals("i") || s.equals("I") || s.equals("l")
						|| s.equals("t") || s.equals("f") || s.equals("�")
						|| s.equals("�") || s.equals("�") || s.equals("�")
						|| s.equals("j") || s.equals("J")) {
					wSymb -= 2;
					// System.out.println("---");
				}

				else
					wSymb = WSYMB;
			}

			x2 += (wSymb - 1);

			if (a > kvoSymb) {
				x2 = x1;
				y1 += hSymb;
				a = 0;
			}

		}
		// System.out.println("y1="+y1+" y="+y+" hS="+hSymb+" total="+(y1-y+hSymb+2));
		return y1 - y + hSymb + 2;
	}

}// end class

