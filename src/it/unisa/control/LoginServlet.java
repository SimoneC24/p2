package it.unisa.control;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.unisa.model.*;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
			
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	UserDao usDao = new UserDao();
		
		try
		{	    

		     UserBean user = new UserBean();
		     user.setUsername(request.getParameter("un"));
		     
		     String enteredPassword = request.getParameter("pw");
		     String hashedEnteredPassword = hashPassword(enteredPassword);
		     user = usDao.doRetrieve(request.getParameter("un"), hashedEnteredPassword);
			   		    
		    
		     String checkout = request.getParameter("checkout");
		     
		     if (user.isValid())
		     {
			        
		          HttpSession session = request.getSession(true);	    
		          session.setAttribute("currentSessionUser",user); 
		          if(checkout!=null)
		        	  response.sendRedirect(request.getContextPath() + "/account?page=Checkout.jsp");
		          
		          else
		        	  response.sendRedirect(request.getContextPath() + "/Home.jsp");
		     }
			        
		     else 
		          response.sendRedirect(request.getContextPath() +"/Login.jsp?action=error"); //error page 
		} 
				
				
		catch(SQLException e) {
			System.out.println("Error:" + e.getMessage());
		}
		  }

	public static String hashPassword(String password) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        byte[] hashedBytes = md.digest(password.getBytes());
	        return Base64.getEncoder().encodeToString(hashedBytes);
	    } catch (NoSuchAlgorithmException e) {
	        // Handle error
	        return null;
	    }
	}
}