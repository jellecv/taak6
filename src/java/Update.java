/*
 *Datum: 23-6-2008
 *Authors:	-Ellen Kilsdonk
 *			-Oussama Bouhcine
 *			-Nikita van Wijk
 *
 *Vanuit de Bewerken class wordt je doorgestuurd naar de Update class. In deze Update class worden de 
 *gegevens in de database opgeslagen.
 */

import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.*;

public class Update extends HttpServlet
{
	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
	{	res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		
		try
		{	// connectie maken met de database
			DatabaseConnectie database = new DatabaseConnectie();
			
			// de ingevoerde waarden worden in variabele gezet
			String id = req.getParameter("id");
			String naam = req.getParameter("name");
			String manVrouw =  req.getParameter("sex");
			String klacht = req.getParameter("klacht");
			String new_urgentie = req.getParameter("urgentie");
			String specialisme = req.getParameter("specialisme");
			String urg = req.getParameter("urg");
			System.out.println(urg);
			
			int geslacht=0;
			if(manVrouw.equals("man"))
			 	geslacht =1 ;
			if(manVrouw.equals("vrouw"))
				geslacht = 2;
			
			// start html pagina
			out.println("<HTML><HEAD><link rel='stylesheet' type='text/css' href='../css/hoofd.css' /><TITLE>");
   			out.println("Gewijzigde patientgegevens");
    		out.println("</TITLE><meta http-equiv=\"refresh\" content=\"3; URL=WachtlijstStipSter\"></HEAD><BODY> ");
    		
    		//kijken of alle gegevens zijn ingevoerd, zo niet wordt er een foutmelding teruggegeven
			if(naam.equals("")||klacht.equals("")||new_urgentie.equals("")||specialisme.equals("")||geslacht == 0)
			{
				out.println("<h3> Niet alle gegevens zijn ingevoerd </h3>");
				out.println("<input type='button' value='opnieuw' onClick='history.back()'");
			}
			// als alle gegevens wel zijn ingevoerd dan wordt er een sql query gemaakt en uitgevoerd
			// waarbij de patient met het betreffende id gewijzigd wordt in de database
			else 
			{
				String statement = "UPDATE Patient SET P_Naam='"+naam+"', P_Geslacht="+geslacht+", P_Primaire_klacht='"+klacht+
									"', P_Urgentie='"+ new_urgentie+"', P_Specialisme='"+specialisme+"' " +
								   "WHERE P_ID="+id;
				database.executeStatement(statement);
				
				// er wordt een tabel getoond aan de gebruiker met de nieuwe gegevens
				out.println("<H3 align='center'>" + "Gewijzigde pati&#235nt gegevens" + "</H3>");
	  			out.println("<TABLE border='1'>");
	  			out.println("<tr>");
	  			out.println("<th> Naam </th> <th> Geslacht </th> <th> Primaire klacht </th>" +
	  						"<th> Urgentie </th> <th> Specialisme </th>");
	  			out.println("</tr>");
	  			out.println("<tr>");
	  			out.println("<td>" + naam + "</td><td>" + manVrouw + "</td><td>" + klacht + "</td>" +
	  								"<td class='"+new_urgentie+"'>" + new_urgentie + "</td><td>" + specialisme + "</td>");
	  			out.println("</tr>");
				out.println("</TABLE>");
			}
			
			// als de urgentie veranderd is dan wordt ook de triagetijd aangepast in de database
			if(!urg.equals(new_urgentie))
			{	Tijd triagetijd = new Tijd();
				Tijd maxtijd = new Tijd();
				// de tijd van nu wordt opgehaald
				Timestamp triage = triagetijd.getTijd();
				
				// het hangt af van welke kleur er voor de urgentie is meegegeven hoeveel tijd
				// er voor de maximale wachttijd wordt ingesteld
				int max=0;
				if (new_urgentie.equals("oranje"))
					max = 10;
				if (new_urgentie.equals("geel"))
					max = 60;	
				if (new_urgentie.equals("groen"))
					max = 120;
				if (new_urgentie.equals("blauw"))
					max = 240;
				
				maxtijd = maxtijd.minLater(max);
				Timestamp maximum = maxtijd.getTijd();
				
				// SQL query wordt gemaakt en uitgevoerd om de urgentie te wijzigen
				String statement = "UPDATE Patient SET Triage_tijd='"+ triage.toString()+"', P_Max_tijd='"+
					maximum.toString() +"' WHERE P_ID="+id;
				database.executeStatement(statement);
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