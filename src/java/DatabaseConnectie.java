/*
 *Datum: 5-6-2008
 *Authors:	-Ellen Kilsdonk
 *			-Oussama Bouhcine
 *			-Nikita van Wijk
 *
 *De DatabaseConnectie class zorgt ervoor dat er connectie gemaakt kan worden met een database. Er wordt gezorgd dat er 
 *meerdere connecties tegelijk gemaakt kunnen worden.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DatabaseConnectie
{	
	public DatabaseConnectie()
	throws ClassNotFoundException, SQLException
	{	aantalConnections = 50;
    	
    	connections = new Connection[aantalConnections];
    	beschikbaarheid = new boolean[aantalConnections];
    	con = null;
    	
    	for(int i = 0; i < aantalConnections; i++) 
    		{
      			beschikbaarheid[i] = true;
    		}
		
		try 
		{
            Class.forName("com.mysql.jdbc.Driver");
            // set this to a MySQL DB you have on your machine
            
            String url =
            "jdbc:mysql://localhost/triaid";
           
            con = DriverManager.getConnection(url,"CENSUUR","CENSUUR");
            
            for(int i = 0; i < aantalConnections; i++) 
    		{
      			connections[i] = con;
    		}
		}
		catch (Exception e) {
            System.out.println("Error: " + e);
        }
	}
	
	// de connectie wordt teruggegeven
	private Connection getConnection()
	{	con = null;
	
		for(int i=0; i<aantalConnections; i++)
		{	if (beschikbaarheid[i]==true)
			{	con = connections[i];
				beschikbaarheid[i] = false;
			}
		}
		
		if(con!=null)	
			System.out.println("Database connectie gelukt!");
		
		return con; 
	}
	
	// de connectie wordt gesloten
	private void closeConnection(Connection terug)
	{	for(int i=0; i<aantalConnections; i++)
		{	if(terug==connections[i])
			{	beschikbaarheid[i] = true;
				System.out.println("Database-connectie afgesloten");
			}
		}	
	}
	
	public ResultSet executeQuery(String query)
	{	Connection c = getConnection();
		ResultSet rs = null;
		try{
	
		// aanmaken van statement object uit connection object
		PreparedStatement stmt = c.prepareStatement(query);
				
		// aanmaken van ResultSet object uit statement object
		rs = stmt.executeQuery();	
			
		}
		catch (Exception e)
		{	System.out.println("***Exception:\n"+e);
	      	e.printStackTrace();
		}	
			
		closeConnection(c);
			
		return rs;	
	}
	
	public void executeStatement(String statement)
	{	Connection c = getConnection();

		try{
	
		// aanmaken van statement object uit connection object
		PreparedStatement stmt = c.prepareStatement(statement);
				
		// aanmaken van ResultSet object uit statement object
		stmt.executeUpdate();	
			
		}
		catch (Exception e)
		{	System.out.println("***Exception:\n"+e);
	      	e.printStackTrace();
		}	
			
		closeConnection(c);
			
	}
	
	private boolean[] beschikbaarheid;
	private Connection con;
	private Connection[] connections;
	private int aantalConnections;
	
}

