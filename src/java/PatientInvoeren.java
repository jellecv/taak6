/*
 *Datum: 5-6-2008
 *Authors:	-Ellen Kilsdonk
 *			-Oussama Bouhcine
 *			-Nikita van Wijk
 *
 *In de "PatientInvoeren" class wordt het mogelijk om patienten die in een html formulier ingevuld
 *worden ook daadwerkelijk in de database op te slaan. 
 */
 
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.*;

public class PatientInvoeren extends HttpServlet
{
	public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
	{	res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		
		try
		{	//connectie maken met de database
			DatabaseConnectie database = new DatabaseConnectie();
			
			// De gegevens die in het html formulier zijn ingevuld worden opgehaald
			String naam = req.getParameter("name");
			String manVrouw =  req.getParameter("sex");
			String klacht = req.getParameter("klacht");
			String urgentie = req.getParameter("urgentie");
			String specialisme = req.getParameter("specialisme");
			
			String t;
			String m;
			
			if(!urgentie.equals("niet bepaald"))
			{	Tijd triagetijd = new Tijd();
				Tijd maxtijd = new Tijd();
				// de tijd van nu wordt opgehaald
				Timestamp triage = triagetijd.getTijd();
				t = triage.toString();
				
				// het hangt af van welke kleur er voor de urgentie is meegegeven hoeveel tijd
				// er voor de maximale wachttijd wordt ingesteld
				int max=0;
				if (urgentie.equals("oranje"))
					max = 10;
				if (urgentie.equals("geel"))
					max = 60;	
				if (urgentie.equals("groen"))
					max = 120;
				if (urgentie.equals("blauw"))
					max = 240;
				
				maxtijd = maxtijd.minLater(max);
				Timestamp maximum = maxtijd.getTijd();
				m = maximum.toString();
			}
			else
			{	Tijd triagetijd = new Tijd();
				Timestamp triage = triagetijd.getTijd();
				t = triage.toString();
				m = "-";
			}

			
			// omdat geslacht voor de gebruiker zichtbaar is als "man" of "vrouw", maar in de database
			// wordt opgeslagen als "1" of "2", wordt dat hier omgezet naar de goede waarde voor in de
			// database
			int geslacht=0;
			if(manVrouw.equals("man"))
			 	geslacht =1 ;
			if(manVrouw.equals("vrouw"))
				geslacht = 2;
			
			// aanmaken van statement object uit connection object
			
			// start html pagina
			out.println("<HTML><HEAD><link rel='stylesheet' type='text/css' href='../css/hoofd.css' /><TITLE>");
   			out.println("Patientgegevens invoeren");
    		out.println("</TITLE><meta http-equiv=\"refresh\" content=\"3; URL=WachtlijstStipSter\"></HEAD><BODY> ");
    		
    		//kijken of alle gegevens zijn ingevoerd, zo niet wordt er een foutmelding teruggegeven
			if(naam.equals("")||klacht.equals("")||specialisme.equals("")||geslacht == 0)
			{
				out.println("<h3> Niet alle gegevens zijn ingevoerd </h3>");
				out.println("<input type='button' value='opnieuw' onClick='history.back()'");
			}
			
			else 
			{
				// SQL query wordt gemaakt en uitgevoerd om de patient met de huiste gegevens in de 
				// database te zetten
				String statement = "INSERT INTO Patient(P_Naam, P_Geslacht, P_Primaire_klacht, P_Urgentie, P_Specialisme, Triage_tijd, P_Max_tijd) " +
								   "VALUES('"+naam+"', "+geslacht+", '"+klacht+"', '"+urgentie+"', '"+specialisme+"', '"+t+"', '"+m+"')";
				database.executeStatement(statement);
				System.out.println(statement);
				
				// Op de html pagina komen de zojuist ingevoerde patientgegevens te staan
				out.println("<H3 align='center'>" + "Ingevoerde pati&#235nt gegevens" + "</H3>");
	  			out.println("<TABLE border='1'>");
	  			out.println("<tr>");
	  			out.println("<th> Naam </th> <th> Geslacht </th> <th> Primaire klacht </th>" +
	  						"<th> Urgentie </th> <th> Specialisme </th> <th> Triagetijd </th> <th> Maxtijd </th>");
	  			out.println("</tr>");
	  			out.println("<tr>");
	  			out.println("<td>" + naam + "</td><td>" + manVrouw + "</td><td>" + klacht + "</td>" +
	  								"<td class='"+urgentie+"'>" + urgentie + "</td><td>" + specialisme + "</td>" +
	  									"<td>" + t + "</td>" + "<td>" + m + "</td>");
	  			out.println("</tr>");
				out.println("</TABLE>");
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