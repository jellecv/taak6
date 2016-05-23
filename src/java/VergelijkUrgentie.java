/*
 *Datum: 5-6-2008
 *Authors:	-Ellen Kilsdonk
 *			-Oussama Bouhcine
 *			-Nikita van Wijk
 *
 *In deze class wordt de tijd vergeleken die staat voor hoelang de patient nog mag wachten met de 
 *kleurcode die hierhaan toegekend is. Wanneer dit niet meer klopt wordt de kleurcode verandert. 
 */

import java.sql.*;

public class VergelijkUrgentie {

    public VergelijkUrgentie(String urgentie, long verschil) {
    	
    	urg = urgentie;
    	ver = verschil+3600000;
    }
    
    // In deze method wordt er voor de kleur die nu bij de patient toegekend is gekeken of deze kleur
    // nog wel juist is of dat er zoveel tijd verstreken is dat er een andere kleur zou moeten worden
    // toegekend. Als dit het geval is wordt de urgentie op een andere kleur gezet. De kleur wordt
    // aan het eind teruggegeven.
    public String vergelijk()
    {	if(ver==0 || ver<0)
    	{	urg = "rood";
    	}
    	else if(ver==600000 || ver<600000)
    	{	urg = "oranje";
    	}
    	else if(ver==3600000 || ver<3600000)
    	{	urg = "geel";
    	}
    	else if(ver==7200000 || ver<7200000)
    	{	urg = "groen";
    	}
    	
    	return urg;
    }
    
    //velden
    private String urg;
    private long ver;
    
}