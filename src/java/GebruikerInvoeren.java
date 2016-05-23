/*
 *Datum: 5-6-2008
 *Authors:	-Ellen Kilsdonk
 *			-Oussama Bouhcine
 *			-Nikita van Wijk
 *
 *In de "GebruikerInvoeren" class wordt het mogelijk om nieuwe gebruikers toe te voegen
 */
 
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GebruikerInvoeren extends HttpServlet
{
	public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
	{	res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		
		try
		{	//connectie maken met de database
			DatabaseConnectie database = new DatabaseConnectie();
			
			// De gegevens die in het hetml formulier zijn ingevuld worden opgehaald
			String naam = req.getParameter("name");
			String rol =  req.getParameter("rol");
			String password = req.getParameter("password");
			String pass = org.apache.catalina.realm.RealmBase.Digest(password, "MD5", null);
			
			// Er wordt gekeken of de opgegeven naam voor de gebruiker al bestaat in de 
			// database, als dit het geval is wordt dit aan de gebruiker gemeld
			String query = "SELECT U_Naam FROM User WHERE U_Naam='"+naam+"'";
	        
	        ResultSet rs = database.executeQuery(query);
	        if(rs.next())
	        {
	        	out.println("<html><head><title></title></head><body>");
	   			out.println("<h3>Gebruiker met de naam '"+naam+"' bestaat al, kies een andere naam</h3>");
	   			out.println("<input type='button' value='opnieuw' onClick='history.back()'");
	   			out.println("</body></html>");
	        }	
	        else
	        {
				// start html pagina
				out.println("<HTML><HEAD><link rel='stylesheet' type='text/css' href='css/hoofd.css' /><TITLE>");
	   			out.println("Gebruiker invoeren");
	    		out.println("</TITLE><meta http-equiv=\"refresh\" content=\"3; URL=../files/beheerder/gebruiker-invoeren.html\"></HEAD><BODY> ");
	    		
	    		//kijken of alle gegevens zijn ingevoerd, zo niet wordt er een foutmelding teruggegeven
				if(naam.equals("")||rol.equals("")||password.equals(""))
				{
					out.println("<h3> Niet alle gegevens zijn ingevoerd </h3>");
					out.println("<input type='button' value='opnieuw' onClick='history.back()'");
				}
				
				else 
				{
					// SQL query wordt gemaakt en uitgevoerd om de gebruiker met de juiste gegevens in de 
					// database te zetten
					String statement = "INSERT INTO User (U_Naam, U_Paswoord) VALUES ('"+ naam+ "', '"+pass+"')";
					String statement2 = "INSERT INTO User_Roles (U_Naam, U_Role) VALUES ('"+naam+"', '"+rol+"')";
					database.executeStatement(statement);
					database.executeStatement(statement2);
					
					// Op de html pagina komen de zojuist ingevoerde gebruikersgegevens te staan
					out.println("<H3 align='center'>" + "Ingevoerde gebruikers gegevens" + "</H3>");
		  			out.println("<TABLE border='1'>");
		  			out.println("<tr>");
		  			out.println("<th> Naam </th> <th> Rol </th> <th> Password </th>");
		  			out.println("</tr>");
		  			out.println("<tr>");
		  			out.println("<td>" + naam + "</td><td>" + rol + "</td><td>" + password + "</td>");
		  			out.println("</tr>");
					out.println("</TABLE>");
				}
	        }
	        
		}
		
		catch (Exception e)
		{	
			out.println("<p>Error:"+e+"</p>");
		}	
		
		// html pagina wordt gesloten	
		out.println("</BODY></HTML>");
		out.close();
		
	}
}