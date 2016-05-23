/*
 *Datum: 22-6-2008
 *Authors:	-Ellen Kilsdonk
 *			-Oussama Bouhcine
 *			-Nikita van Wijk
 *
 *De GebruikerWijzigen class wordt gebruikt om gegevens van een gebruiker te wijzigen
 */
 
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.*;

public class GebruikerWijzigen extends HttpServlet
{
	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
	{	res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		
		try
		{	// connectie maken met de database
			DatabaseConnectie database = new DatabaseConnectie();
			
			// de ingevoerde gegevens worden in variabale gezet
			String naam = req.getParameter("name");
			String new_naam = req.getParameter("new_name");
			String rol =  req.getParameter("rol");
			String oud_pw = req.getParameter("password");
			String new_pw = req.getParameter("new_password");
			String oud_pw2 = org.apache.catalina.realm.RealmBase.Digest(oud_pw, "MD5", null);
			String new_pw2 = org.apache.catalina.realm.RealmBase.Digest(new_pw, "MD5", null);
			
			// start html pagina
			out.println("<HTML><HEAD><link rel='stylesheet' type='text/css' href='css/hoofd.css' /><TITLE>");
   			out.println("Gewijzigde patientgegevens");
    		out.println("</TITLE><meta http-equiv=\"refresh\" content=\"3; URL=../files/beheerder/gebruiker-invoeren.html\"></HEAD><BODY> ");
    		
    		//kijken of alle gegevens zijn ingevoerd, zo niet wordt er een foutmelding teruggegeven
			if(naam.equals("")||oud_pw.equals(""))
			{
				out.println("<h3> Niet alle gegevens zijn ingevoerd </h3>");
				out.println("<input type='button' value='opnieuw' onClick='history.back()'");
			}
			
			// de wijziging wordt aan de database doorgegeven
			else 
			{	if(!rol.equals(""))
				{	String statement = "UPDATE User_Roles SET U_Role='"+rol+"' WHERE U_Naam='"+naam+"'";
					database.executeStatement(statement);
				}
				if(!new_pw.equals(""))
				{	String statement = "UPDATE User SET U_Paswoord='"+new_pw2+"' WHERE U_Naam="+naam+" AND U_Paswoord="+oud_pw2;
					database.executeStatement(statement);
				}
				if(!new_naam.equals(""))
				{	String statement = "UPDATE User SET U_Naam='"+new_naam+"' WHERE U_Naam='"+naam+"' AND U_Paswoord='"+oud_pw2+"'";
					String statement2 = "UPDATE User_Roles SET U_Naam='"+new_naam+"' WHERE U_Naam='"+naam+"'";
					database.executeStatement(statement);
					database.executeStatement(statement2);
				}
				
				// de gewijzigde gegevens worden aan de gebruiker getoond
				out.println("<H3 align='center'>" + "Gewijzigde gebruikers gegevens" + "</H3>");
	  			out.println("<TABLE border='1'>");
	  			out.println("<tr>");
	  			out.println("<th> Naam </th> <th> Rol </th> <th> Paswoord </th>");
	  			out.println("</tr>");
	  			out.println("<tr>");
	  			out.println("<td>" + new_naam + "</td><td>" + rol + "</td><td>" + new_pw + "</td>");
	  			out.println("</tr>");
				out.println("</TABLE>");
			}
			
		}
		catch (Exception e)
		{	
			out.println("<p>Error:"+e+"</p>");
		}	
		
		// html pagina wordt afgesloten	
		out.println("</BODY></HTML>");
		out.close();
		
	}
}