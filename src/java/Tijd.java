import java.util.*;
import java.sql.*;

/** Immutable class die een tijdstip (tijd in uren + datum) representeert.
 * Er wordt gebruik gemaakt van de Gregoriaanse kalender.
 * Voorbeelden van gebruik:
 * <pre>
 *   Tijd nu = new Tijd(); // de tijd waarop dit object werd aangemaakt
 *   Tijd straks = nu.later(2); // twee uur na nu
 *   Tijd kerstavond = new Tijd(24, 12, 2007, 20);
 *   if (nu.isVoor(kerstavond)) System.out.println("Nog even wachten!");
 *   Tijd afspraak = kerstavond.eersteWerktijd();
 *   System.out.println(afspraak.formatDatum() + " " + afspraak.formatTijd());
 *   // resultaat is 25-12-2007 14:00, want dat is de eerst volgende werktijd (deze class kent geen vrije dagen)
 * </pre>
 **/
public class Tijd {
  /** Maakt een Tijd-object voor het huidige tijdstip, waarbij het uur wordt gesteld op het laatst verstreken uur. */
  public Tijd() {
    tijd = new GregorianCalendar();
    
  }

  /** Maakt een Tijd-object voor de gegeven datum (dag, maand, jaar), middernacht */
  public Tijd(int dag, int maand, int jaar) {
   tijd = new GregorianCalendar(jaar, maand - 1, dag);
  }

  /** Maakt een Tijd-object voor de gegeven datum (dag, maand, jaar) en de gegeven tijd (in uren) */
  public Tijd(int dag, int maand, int jaar, int uren) {
    tijd = new GregorianCalendar(jaar, maand - 1, dag, uren, 0);
  }

  private Tijd(GregorianCalendar deTijd) {
    tijd = deTijd;
  }

  /** Geeft true terug als deze Tijd voor de gegeven tijd ligt (vroeger is) */
  public boolean isVoor(Tijd andereTijd) {
    return tijd.getTimeInMillis() < andereTijd.tijd.getTimeInMillis();
  }

  /** Geeft true als andereTijd gelijk is aan deze Tijd. */
  public boolean isGelijk(Tijd andereTijd) {
	  return tijd.equals(andereTijd.tijd);
  }

  /** Geeft een nieuwe tijd terug die het gegeven aantal uren later is
   * dan deze Tijd */
  public Tijd later(int uren) {
    GregorianCalendar later = (GregorianCalendar) tijd.clone();
    later.add(Calendar.HOUR, uren);
    return new Tijd(later);
  }
  
   /** Geeft een nieuwe tijd terug die het gegeven aantal minuten later is
   * dan deze Tijd */
  public Tijd minLater(int minuten) {
    GregorianCalendar later = (GregorianCalendar) tijd.clone();
    later.add(Calendar.MINUTE, minuten);
    return new Tijd(later);
  }

  /** Geeft de dag (datum) van deze Tijd terug */
  public int getDag() {
    return tijd.get(Calendar.DAY_OF_MONTH);
  }

  /** Geeft de maand van deze Tijd terug */
  public int getMaand() {
    return tijd.get(Calendar.MONTH) + 1;
  }

  /** Geeft het jaar van deze Tijd terug */
  public int getJaar() {
    return tijd.get(Calendar.YEAR);
  }

  /** Geeft het uur van deze Tijd terug */
  public int getUren() {
    return tijd.get(Calendar.HOUR_OF_DAY);
  }

  /** Geeft true terug als deze Tijd onder werkuren valt (zie
   * BEGIN_UUR en EIND_UUR) */
  public boolean isWerktijd() {
    int uur = getUren();
    return uur >= BEGIN_UUR && uur < EIND_UUR;
  }

  /** Geeft de eerstvolgende Tijd terug vanaf deze Tijd
   * die tijdens werkuren is.
   * @return deze Tijd indien dat werktijd is, anders een nieuw Tijd-object met de eerst volgende werktijd*/
  public Tijd eersteWerktijd() {
    if (isWerktijd()) return this;

    int uur = getUren();
    Tijd werktijd;
    if (uur < BEGIN_UUR) {
      werktijd = new Tijd(getDag(), getMaand(), getJaar(), BEGIN_UUR);
    } else { // naar volgende dag
      GregorianCalendar nieuweTijd = (GregorianCalendar)tijd.clone();
      nieuweTijd.add(Calendar.HOUR_OF_DAY, ETMAAL - uur + BEGIN_UUR);
      werktijd = new Tijd(nieuweTijd);
    }

    return werktijd;
  }

  /** Geeft een stringrepresentatie van de tijd (zonder datum) terug */
  public String formatTijd() {
    int uren = tijd.get(Calendar.HOUR_OF_DAY);
    String urenTekst;
    if (uren < 10) urenTekst = "0" + uren; else urenTekst = "" + uren;

    int minuten = tijd.get(Calendar.MINUTE);
    String minutenTekst;
    if (minuten < 10) minutenTekst = "0" + minuten; else minutenTekst = "" + minuten;

    return urenTekst + ":" + minutenTekst;
  }

  /** Geeft een stringrepresentatie van de datum terug */
  public String formatDatum() {
    return tijd.get(Calendar.DAY_OF_MONTH) + "-" + (tijd.get(Calendar.MONTH) + 1) +
      "-" + tijd.get(Calendar.YEAR);
  }
  
  public Timestamp getTijd()
  { /*int maand = tijd.get(Calendar.MONTH);
  	int dag = tijd.get(Calendar.DAY_OF_MONTH);
  	int jaar = tijd.get(Calendar.YEAR);
  	int uur = tijd.get(Calendar.HOUR);
  	int min = tijd.get(Calendar.MINUTE);
  	int sec = tijd.get(Calendar.SECOND);
  	*/
  	
  	long t = tijd.getTimeInMillis();
  	Timestamp time = new Timestamp(t);
  	return time;
  }


  /** Het tijdstip waarop een werkdag begint (een heel uur) */
  public static final int BEGIN_UUR = 14;

  /** Het tijdstip waarop een werkdag eindigt (een heel uur) */
  public static final int EIND_UUR = 17;

  private static final int ETMAAL = 24;

  private GregorianCalendar tijd;
}
