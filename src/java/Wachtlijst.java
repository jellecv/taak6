/*
 *Datum: 5-6-2008
 *Authors:	-Ellen Kilsdonk
 *			-Oussama Bouhcine
 *			-Nikita van Wijk
 *
 *In deze wachtlijst class wordt er voor gezorgd dat alle gegegevens van alle patienten uit de database 
 *gehaald worden en dat deze patienten, geordend op urgentie, teruggegeven worden in de vorm van een wachtlijst
 */

import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class Wachtlijst extends HttpServlet
{
	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
	{	res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		
		// Variabele worden gedeclareerd
		int patientID;
		String naam;
		int geslacht;
		String klacht;
		String urgentie;
		String triagetijd;
		String maxtijd;
		String spec;
		Tijd huidig = new Tijd();
		Timestamp nu = huidig.getTijd();
		
		
		try
		{	// conncetie maken met de database
			DatabaseConnectie database = new DatabaseConnectie();
			
			// Het begin van de site wordt gemaakt. De site wordt automatisch gerefreshed elke 60 sec. 
			// Verder wordt het huidige tijdstip weergegeven en de kopjes boven de tabel worden gemaakt.
			out.println("<html><head><link rel='stylesheet' type='text/css' href='../css/hoofd.css' />");
			out.println("<title>Wachtlijst</title><META HTTP-EQUIV=\"Refresh\" CONTENT=\"60\"> </head><body>");
			out.println("<h3 align='center'>Wachtlijst</h3>");
			out.println("<h4>Huidig tijd:<br>"+nu.toString().substring(0,16)+"</h4>");
			out.println("<table border='1'>");
			out.println("<tr><th>Naam</th><th>Geslacht</th><th>Primaire Klacht</th><th>Urgentie</th><th>Specialisme</th><th>Triage tijd</th><th>Wachten tot</th><th>Maximale wachttijd</th></tr>");
			
			// Een statement object uit een connection object wordt aangemaakt. Er wordt een SQL query
			// gemaakt en uitgevoerd om de patientgegevens uit de database te halen. Deze gegevens
			// worden in een resultset gezet

			String query = "SELECT P_ID, P_Naam, P_Geslacht, P_Primaire_klacht, P_Urgentie, Triage_tijd, P_Max_tijd, P_Specialisme FROM Patient ORDER BY P_Max_tijd";
			ResultSet rs = database.executeQuery(query);
			
			// Zolang er nog een volgende patient in de resultset zit wordt het volgnede gedaan:
			while(rs.next())
			{	// Uit de resultset worden de gegens gehaald en in variabele gezet.
				patientID = rs.getInt("P_ID");
				naam = rs.getString("P_Naam");
				geslacht = rs.getInt("P_Geslacht");
				klacht = rs.getString("P_Primaire_klacht");
				urgentie = rs.getString("P_Urgentie");
				triagetijd = rs.getString("Triage_tijd");
				maxtijd = rs.getString("P_Max_tijd");
				spec = rs.getString("P_Specialisme");
				String verschil="";
				String m="";
				Timestamp triage = nu.valueOf(triagetijd);
				
				if(!maxtijd.equals("-"))
				{	
					Timestamp max = nu.valueOf(maxtijd);
					
					// Als de maximale wachttijd nog niet verstreken is en als de maxiamle wachttijd
					// gelijk is aan de tijd van nu wordt de vergelijk method uit de VergelijkUrgentie 
					// class aangroepen
					
					if (nu.before(max))
					{	long t = max.getTime();
						long n = nu.getTime();
						long v = t-n-3600000;
						Time ver = new Time(v);
						VergelijkUrgentie vergelijker = new VergelijkUrgentie(urgentie, ver.getTime());
						urgentie = vergelijker.vergelijk();
						verschil = ver.toString().substring(0,5);
					}	
					if (nu.equals(max))
					{	long t = max.getTime();
						long n = nu.getTime();
						long v = t-n-3600000;
						Time ver = new Time(v);
						VergelijkUrgentie vergelijker = new VergelijkUrgentie(urgentie, ver.getTime());
						urgentie = vergelijker.vergelijk();
						verschil = "De patient moet nu binnengehaald worden.";
					}
					
					// wanneer de maxiamle wachttijd al verstreken is wordt er teruggegeven dat de patient
					// al binnen had moeten zijn
					if(nu.after(max))
					{	urgentie = "rood";
						verschil = "Deze patient had al binnen moeten zijn!";
					}
				
					// De uitkomsten van het meten voor de urgentie worden in de database gezet, er wordt
					// hiervoor een sql query aangemaakt en uitgevoerd
					String statement = "UPDATE Patient SET P_Urgentie='"+ urgentie+"' WHERE P_ID="+patientID;
					database.executeStatement(statement);
					
					m = max.toString().substring(0,16);
				}

				//Het geslacht wordt in de database opgeslagen als "1" of "2", er wordt voor gezorgd dat
				// de gebruiker "man" of "vrouw" te zien zal krijgen.
				String gesl = "";
				if (geslacht==1)
					gesl = "man";
				else
					gesl = "vrouw";
				
				//Alle opgehaalde gegevens worden in de tabel gezet en dus door middel van de html
				//pagina, zichtbaar voor de gebruikers
				out.println("<tr><td>"+naam+"</td><td>"+gesl+"</td><td>"+klacht+"</td><td class="+urgentie+">"+urgentie+"</td><td>"+spec+"</td><td>"+triage.toString().substring(0,16)+"</td><td>"+m+"</td><td>"+verschil+"</td></tr>");
			}
			
			out.println("</table><hr></body></html>");
			
		}
		catch (Exception e)
		{	
			out.println("<html><head><title></title></head><body><p>Error:"+e+"</p></body></html>");
	
		}
	}
}