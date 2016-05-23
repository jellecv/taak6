/*
 *Datum: 23-6-2008
 *Authors:	-Ellen Kilsdonk
 *			-Oussama Bouhcine
 *			-Nikita van Wijk
 *
 *De Logout class zorgt ervoor dat je gebruiker gegevens niet meer zijn opgeslagen maar dat je weer
 *verwezen wordt naar de startpagina, waar weer opnieuw ingelogd zal moeten worden. 
 */
 
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class Logout extends HttpServlet
{
	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException
	{	HttpSession session = req.getSession();
		session.invalidate();
		res.sendRedirect("../index.html");
	}
}