/*
 *Datum: 5-6-2008
 *Authors:	-Ellen Kilsdonk
 *			-Oussama Bouhcine
 *			-Nikita van Wijk
 *
 *In de GebruikerVerwijderen class wordt ervoor gezorgd dat er gebruikers verwijderd kunnen worden 
 *ui de database. 
 */
 
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class GebruikerVerwijderen extends HttpServlet
{
	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
	{	res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		
		// aanmaken van de html pagina
        out.println("<html><head><title>Gebruiker verwijderen</title><meta http-equiv=\"refresh\" content=\"3; URL=../files/beheerder/gebruiker-invoeren.html\"></head><body>");
		
		// de naam van de gebruiker wordt opgehaald zodat de juiste gebruiker verwijderd kan worden
		String naam = req.getParameter("name");
		
		try
		{	
			// connectie maken met de database
			DatabaseConnectie database = new DatabaseConnectie();
			
	        // sql query aanmaken en uitvoeren waarmee de gebruiker met de opgegeven naam verwijderd
	        // wordt uit de database
	        String query = "SELECT U_Naam FROM User WHERE U_Naam='"+naam+"'";
	     
	        ResultSet rs = database.executeQuery(query);
	        if(rs.next())	
	        {	String statement1 = "Delete from User_Roles where U_Naam = '" +naam+ "'";
	          	String statement2 = "Delete from User where U_Naam = '" +naam+ "'";
	            database.executeStatement(statement1);
	           	database.executeStatement(statement2);
	            		           		
	            // wanneer de gebruiker verwijderd is wordt dit op de html pagina aan de gebruiker verteld
	           	out.println("<h3 align='center'>Gebruiker verwijderd</h3>");
	        }
	        else
	        {	out.println("<h3> Deze gebruiker bestaat niet!</h3>");
				out.println("<input type='button' value='opnieuw' onClick='history.back()'");
	       	}
	       	
        }
        
        catch (Exception e)
        {   
            out.println("<p>Error:"+e+"</p>");
        }
        
        // html pagina wordt afgeloten
        out.println("</body></html>");
	}
}