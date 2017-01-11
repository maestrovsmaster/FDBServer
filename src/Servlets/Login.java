package Servlets;

import java.awt.List;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import main.ConnectConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;









import Connect.ConnectDBFactory;
import Connect.Firebird;
import Connect.Query;
import LSManager.LSControl;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 System.out.println("doGet");
		 
		 request.setCharacterEncoding("UTF-8");
		    String username = (String) request.getParameter("login");
		    username = username.toUpperCase();
		  //  response.setContentType("text/html;charset=UTF-8");
		  //  response.getWriter().println("<!DOCTYPE HTML>");
		  //  response.getWriter().println("<html><body><p>" + username + "</p></body></html>");
			
		    System.out.println("hello = "+username);
		    ArrayList<String> paramList = ConnectConstants.readSettings(this);
		    System.out.println("input params: "+paramList);
		    
		   
		    
		    
		    //getServletContext().getRequestDispatcher("/hello.jsp");
		    response.addHeader("name", "lalala");
		    String ver = ConnectConstants.getConnect(ConnectConstants.DB_IP,ConnectConstants.DB_PATH,"","");
			 
			response.addHeader("ip", ConnectConstants.DB_IP);
			response.addHeader("path", ConnectConstants.DB_PATH);
			//response.addHeader("log", loginDB);
			//response.addHeader("pass", passDB);
			 response.addHeader("dbversion", ver);
			 response.addHeader("wsname", ConnectConstants.WSNAME);
			 
			 if(ver!=null) System.out.println("ver = "+ver);
			 else System.out.println("ver="+null);
			 if(ver.trim().length()==0) System.out.println("no connect");
			// else
			 
			 
			 System.out.println("ls control start!");
			    LSControl ls = new LSControl(this);
			  
				if(ls.isLicensed())
				{
					System.out.println("licensed");
					response.addHeader("license", "true");
					response.addHeader("dFrom", ls.getDateFrom());
					response.addHeader("dTo",ls.getDateTo());
					response.addHeader("devCnt",Integer.toString(ls.getDevCount()));
					response.addHeader("key", "activated");
					
				}
				else{
					System.out.println("no licensed");
					response.addHeader("license", "false");
					response.addHeader("dFrom", "");
					response.addHeader("dTo","");
					response.addHeader("devCnt","");
					response.addHeader("key", "");
				}
			 
				
				response.addHeader("id", ls.getHashId());
				  System.out.println("ls control end!");
				  
				  
				  String path = getServletContext().getRealPath("/WEB-INF/IMG_20150608_184528.jpg");
				  
				  System.out.println("img 01 = "+path);
				  response.addHeader("image",path);
			 
			 
			 request.getRequestDispatcher("WellcomeServer.jsp").forward(request, response);
			 
			 
		    
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 System.out.println("doPost");
		
	}
	
	

}



