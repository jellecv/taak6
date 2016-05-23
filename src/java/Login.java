/*
 *Datum: 23-6-2008
 *Authors:	-Ellen Kilsdonk
 *			-Oussama Bouhcine
 *			-Nikita van Wijk
 *
 *De Login class zorgt ervoor dat je doorgelinkt wordt naar een pagina aan de hand van de "rol" die je 
 *hebt gekregen. De ene "rol" begint op een andere pagina dan de andere "rol".
 */
 
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class Login extends HttpServlet
{
	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
	{	// afhankelijk van welke rol je hebt gekregen wordt je naar een bepaalde pagina verwezen
		if(req.isUserInRole("triage"))
		{	
			res.sendRedirect("../files/triage/triagehome.html");
		}	
		if(req.isUserInRole("stip") || req.isUserInRole("ster"))
		{	res.sendRedirect("../files/stip-ster/stip-ster-home.html");
		}
		if(req.isUserInRole("overig"))
		{	res.sendRedirect("../files/overighome.html");
		}
		if(req.isUserInRole("beheerder"))
		{	res.sendRedirect("../files/beheerder/beheerder.html");
		}
		if(req.isUserInRole("receptie"))
		{	res.sendRedirect("../files/receptie/receptiehome.html");
		}
	}
}