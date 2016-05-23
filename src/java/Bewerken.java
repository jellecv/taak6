/*
 *Datum: 22-06-2008
 *Authors:	-Ellen Kilsdonk
 *			-Oussama Bouhcine
 *			-Nikita van Wijk
 *
 *De Bewerken class wordt aangeroepen wanneer er in de wachtlijst geklikt wordt op de "bewerken-button"
 *bij een bepaalde patient. Deze class zorgt ervoor dat de gegevens van de patient veranderd kunnen 
 *worden. 
 */
 
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class Bewerken extends HttpServlet
{
	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
	{	res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		
		String naam;
		int geslacht;
		String klacht;
		String urgentie;
		String spec;
		
		// het id van de patient wordt opgevraagd zodat de juiste patient bewerkt kan worden
		String id = req.getParameter("id");
		
		// de html pagina wordt aangemaakt
		out.println("<html><head><link rel='stylesheet' type='text/css' href='../css/patient.css' />");
		out.println("<title>Wijzig pati&#235ntgegevens</title></head><body>");
		out.println("<h3 align='center'>Wijzig pati&#235ntgegevens</h3>");
		
		try
		{	
			// connectie maken met de database
			DatabaseConnectie database = new DatabaseConnectie();
			
			// sql query aanmaken en uitvoeren waarmee de patient met het juiste id uit de database 
			// wordt gehaald, de gegevens worden in een resultset geplaatst	
			String query = "SELECT P_ID, P_Naam, P_Geslacht, P_Primaire_klacht, P_Urgentie, P_Specialisme FROM Patient WHERE P_ID="+id;
			ResultSet rs = database.executeQuery(query);
			
			while(rs.next())
			{	// de gegevens uit de resultset worden in variabele gezet
				int patientID = rs.getInt("P_ID");
				naam = rs.getString("P_Naam");
				geslacht = rs.getInt("P_Geslacht");
				klacht = rs.getString("P_Primaire_klacht");
				urgentie = rs.getString("P_Urgentie");
				spec = rs.getString("P_Specialisme");
				
				// Er wordt een html formulier teruggegeven waarin de gegevens van de patient worden
				// getoond, de gegevens kunnen worden veranderd. Wanneer er op de "wijzig" button 
				// gedrukt wordt, wordt de "Update" class aangeroepen
				out.println("<form action=\"Update?urg='"+urgentie+"'\" method=\"get\">");
				out.println("	<fieldset>");
				out.println("		<legend>Pati&#235ntgegevens:</legend>");
				out.println("		<table>");
				out.println("			<tr><th>Naam</th><td><input type=\"text\" name=\"name\" value=\""+naam+"\"></td></tr>");
				out.println("			<tr><th>Geslacht</th>");
				
				if (geslacht==1)
				{	out.println("				<td>Man <input type=\"radio\" checked=\"checked\" name=\"sex\" value=\"man\">");
					out.println("					Vrouw <input type=\"radio\" name=\"sex\" value=\"vrouw\"></td>");
				}
				else
				{	out.println("				<td>Man <input type=\"radio\" name=\"sex\" value=\"man\">");
					out.println("					Vrouw <input type=\"radio\" checked=\"checked\" name=\"sex\" value=\"vrouw\"></td>");			
				}
				out.println("			</tr>");
				out.println("			<tr><th>Primaire klacht</th><td><input type=\"text\" value=\""+klacht+"\" name=\"klacht\"></td></tr>");
				out.println("			<tr><th>Urgentie</th><td><select name=\"urgentie\">");
				
				// de urgentie kan worden veranderd
				if(urgentie.equals("rood"))
				{	out.println("			<option selected value=\"rood\" class=\"rood\">Rood</option>");
					out.println("			<option value=\"oranje\" class=\"oranje\">Oranje</option>");
					out.println("			<option value=\"geel\" class=\"geel\">Geel</option>");
					out.println("			<option value=\"groen\" class=\"groen\">Groen</option>");
					out.println("			<option value=\"blauw\" class=\"blauw\">Blauw</option>");
				}
				else if(urgentie.equals("oranje"))
				{	out.println("			<option value=\"rood\" class=\"rood\">Rood</option>");
					out.println("			<option selected value=\"oranje\" class=\"oranje\">Oranje</option>");
					out.println("			<option value=\"geel\" class=\"geel\">Geel</option>");
					out.println("			<option value=\"groen\" class=\"groen\">Groen</option>");
					out.println("			<option value=\"blauw\" class=\"blauw\">Blauw</option>");
				}
				else if(urgentie.equals("geel"))
				{	out.println("			<option value=\"rood\" class=\"rood\">Rood</option>");
					out.println("			<option value=\"oranje\" class=\"oranje\">Oranje</option>");
					out.println("			<option selected value=\"geel\" class=\"geel\">Geel</option>");
					out.println("			<option value=\"groen\" class=\"groen\">Groen</option>");
					out.println("			<option value=\"blauw\" class=\"blauw\">Blauw</option>");
				}
				else if(urgentie.equals("groen"))
				{	out.println("			<option value=\"rood\" class=\"rood\">Rood</option>");
					out.println("			<option value=\"oranje\" class=\"oranje\">Oranje</option>");
					out.println("			<option value=\"geel\" class=\"geel\">Geel</option>");
					out.println("			<option selected value=\"groen\" class=\"groen\">Groen</option>");
					out.println("			<option value=\"blauw\" class=\"blauw\">Blauw</option>");
				}
				else if(urgentie.equals("blauw"))
				{	out.println("			<option value=\"rood\" class=\"rood\">Rood</option>");
					out.println("			<option value=\"oranje\" class=\"oranje\">Oranje</option>");
					out.println("			<option value=\"geel\" class=\"geel\">Geel</option>");
					out.println("			<option value=\"groen\" class=\"groen\">Groen</option>");
					out.println("			<option selected value=\"blauw\" class=\"blauw\">Blauw</option>");
				}
				else if(urgentie.equals("niet bepaald"))
				{	out.println("			<option selected value=\"niet bepaald\">Niet bepaald</option>");
					out.println("			<option value=\"rood\" class=\"rood\">Rood</option>");
					out.println("			<option value=\"oranje\" class=\"oranje\">Oranje</option>");
					out.println("			<option value=\"geel\" class=\"geel\">Geel</option>");
					out.println("			<option value=\"groen\" class=\"groen\">Groen</option>");
					out.println("			<option value=\"blauw\" class=\"blauw\">Blauw</option>");
				}
				
				out.println("			</select></td></tr>");
				out.println("			<tr><th>Specialisme</th><td><select name=\"specialisme\">");
				
				// het specialisme kan worden veranderd
					out.println("			<option");
					 if(spec.equals("SEH")) { out.println(" selected"); }
					out.println(">SEH</option>");
					out.println("			<option");
					 if(spec.equals("INT")) { out.println(" selected"); }
					out.println(">INT</option>");
					out.println("			<option");
					 if(spec.equals("CARDO")) { out.println(" selected"); }
					out.println(">CARDO</option>");
					out.println("			<option");
					 if(spec.equals("KIND")) { out.println(" selected"); }
					out.println(">KIND</option>");
					out.println("			<option");
					 if(spec.equals("NEURO")) { out.println(" selected"); }
					out.println(">NEURO</option>");
					out.println("			<option");
					 if(spec.equals("ORTH")) { out.println(" selected"); }
					out.println(">ORTH</option>");
					out.println("			<option");
					 if(spec.equals("UROL")) { out.println(" selected"); }
					out.println(">UROL</option>");
					out.println("			<option");
					 if(spec.equals("LONG")) { out.println(" selected"); }
					out.println(">LONG</option>");
					out.println("			<option");
					 if(spec.equals("MDL")) { out.println(" selected"); }
					out.println(">MDL</option>");
					out.println("			<option");
					 if(spec.equals("ONCO")) { out.println(" selected"); }
					out.println(">ONCO</option>");
					out.println("			<option");
					 if(spec.equals("HEAM")) { out.println(" selected"); }
					out.println(">HEAM</option>");
					out.println("			<option");
					 if(spec.equals("Overig")) { out.println(" selected"); }
					out.println(">Overig</option>");

				out.println("			</select></td></tr>");
				
				// Het id en de urgentie van de patient wordt ook meegegeven.
				out.println("			<tr><td><input type=\"hidden\" name=\"id\" value=\""+patientID+"\">");
				out.println("			<tr><td><input type=\"hidden\" name=\"urg\" value=\""+urgentie+"\">");
				out.println("			<tr><td><input type=\"submit\" value=\"wijzig\"></td></tr>");
				out.println("		</table>");
				out.println("	</fieldset>");
			out.println("	</form>");
			
			}
			
			out.println("</body>");
			out.println("</html>");
		}
		catch (Exception e)
		{	
			out.println("<html><head><title></title></head><body><p>Error:"+e+"</p></body></html>");
	
		}
	}
}