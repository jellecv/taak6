/*
 *Datum: 5-6-2008
 *Authors:	-Ellen Kilsdonk
 *			-Oussama Bouhcine
 *			-Nikita van Wijk
 *
 *De delete class wordt aangeroepen wanneer er in de wachtlijst geklikt wordt op de "delete-button"
 *bij een beepalde patient. Deze class zorgt ervoor dat de patient dan ook daadwerkelijk 
 *verwijderd wordt uit de database. 
 */

import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class Delete extends HttpServlet
{
	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
	{	res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		
		// aanmaken van de html pagina
        out.println("<html><head><meta http-equiv=\"refresh\" content=\"3; URL=WachtlijstStipSter\"><title>Delete Patient</title></head><body>");
        
        // het id van de patient wordt opgevraagd zodat de juiste patient verwijderd kan worden
		String PatientID = req.getParameter("id");

		try
		{	
			// connectie maken met de database
			DatabaseConnectie database = new DatabaseConnectie();
            
            // sql query aanmaken en uitvoeren waarmee de patient met het bepaalde id verwijderd wordt 
            // uit de database
            String statement = "Delete from Patient where P_ID = " +PatientID;
            
            database.executeStatement(statement);
            // wanneer de patient verwijderd is wordt dit op de html pagina aan de gebruiker verteld
            out.println("<h3 align='center'>Pati&#235nt verwijderd</h3>");
       
        }
        
        catch (Exception e)
        {   
            out.println("<p>Error:"+e+"</p>");
        }
        
        out.println("</body></html>");
	}
}