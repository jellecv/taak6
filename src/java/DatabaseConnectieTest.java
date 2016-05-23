

import java.sql.*;
import java.security.*;

public class DatabaseConnectieTest
{
	public static void main(String[] args)
	{	try
		{	DatabaseConnectie database = new DatabaseConnectie();
			String query = "SELECT * FROM Patient";
			
			ResultSet rs = database.executeQuery(query);
			
			while(rs.next())
				{	String naam = rs.getString("P_Naam");
					
					System.out.println(naam);
				}
			

		}
		catch (Exception e)
		{	System.out.println("***Exception:\n"+e);
	      	e.printStackTrace();
		}
	}
}