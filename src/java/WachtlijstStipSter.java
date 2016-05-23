/*
 *Datum: 5-6-2008
 *Authors:	-Ellen Kilsdonk
 *			-Oussama Bouhcine
 *			-Nikita van Wijk
 *
 *In de WachtlijstStipSter klas wordt er een wachtlijst gemaakt waarin de patientgegevens op volgorde
 *van urgentie worden getoond. Ook zijn er hier buttons aanwezig waarop geklikt kan worden wanneer een
 *patient bewerkt of verwijderd moet worden.
 */


import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class WachtlijstStipSter extends HttpServlet
{
	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
	{	res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		
		// variabele declareren
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
		{	//connectie maken met de database
			DatabaseConnectie database = new DatabaseConnectie();
			
			//plaatjes voor de buttons "bewerken" en "verwijderen" ophalen
			String imagebewerken = "../images/bewerken.jpg";
			String imagedelete = "../images/delete.jpg";
			
			// de site wordt elke 60 seconde automatisch gerefreshed
			out.println("<html><head><link rel='stylesheet' type='text/css' href='../css/hoofd.css' />");
			out.println("<title>Wachtlijst</title><META HTTP-EQUIV=\"Refresh\" CONTENT=\"60\">");
			out.println("	</head><body>");
			
			// de huidige tijd en de kopjes van de tabel maken
			out.println("<h3 align='center'>Wachtlijst</h3><h4>Huidig tijd:<br>"+nu.toString().substring(0,16)+"</h4>");
			out.println("<table border='1'>");
			out.println("<tr><th>Naam</th><th>Geslacht</th><th>Primaire Klacht</th><th>Urgentie</th><th>Specialisme</th><th>Triage tijd</th><th>Wachten tot</th><th>Maximale wachttijd</th></tr>");
			
			// een statement object uit een connection object wordt gemaakt. De query om mensen
			// uit de database te halen en ze te ordenen op urgentie wordt hier gemaakt en uitgevoerd.
			// het resultaat wordt opgeslagen in een resultset

			String query = "SELECT P_ID, P_Naam, P_Geslacht, P_Primaire_klacht, P_Urgentie, Triage_tijd, P_Max_tijd, P_Specialisme FROM Patient ORDER BY P_Max_tijd";
			ResultSet rs = database.executeQuery(query);
			
			// Zolang er nog een volgende is in de resultset wordt het volgende gedaan:
			while(rs.next())
			{	// de patientgegevens worden uit de resultset gehaald en in variabele gezet
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
				
				// de waarden voor geslacht zijn in de database "1" en "2", deze worden hier omgezet
				// naar de waarden "man" en "vrouw"
				String gesl = "";
				if (geslacht==1)
					gesl = "man";
				else
					gesl = "vrouw";
				
				// De buttons om de gegevens te bewerken en te verwijderen worden hier gemaakt. Er
				// wordt verwezen naar een andere html pagina. Wanneer er op de button voor verwijderen
				// wordt geklikt wordt er eerst een bevestiging van de gebruiker gevraagd.
				out.println("<tr><td>"+naam+"</td><td>"+gesl+"</td><td>"+klacht+"</td><td class="+urgentie+">"+urgentie+"</td><td>"+spec+"</td><td>"+triage.toString().substring(0,16)+"</td><td>"+m+"</td><td>"+verschil+"</td>");
				out.println("<td><img src='"+imagebewerken+ "' class='a' title='gegevens bewerken' onClick=\"window.location='Bewerken?id="+patientID+"'\" onmouseover=\"this.style.cursor='pointer'\"></a>") ;
				out.println("</td><td><img src='"+imagedelete+"' class='a' title='pati&#235nt verwijderen'"); 
				out.println(" onClick=\"if (confirm('Weet U zeker dat U de pati&#235nt wilt verwijderen?')){window.location='Delete?id="+patientID+"'}\" onmouseover=\"this.style.cursor='pointer'\"></td></tr>");
			}
			
			// afsluiten van de html pagina
			out.println("</table><hr></body></html>");
			
		}
		catch (Exception e)
		{	
			out.println("<html><head><title></title></head><body><p>Error:"+e+"</p></body></html>");
	
		}
	}
}