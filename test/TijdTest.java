import static org.junit.Assert.*;
import org.junit.Test;


public class TijdTest {

	@Test
	public void testVergelijken() {
		Tijd t0 = new Tijd();

		Tijd t1 = new Tijd(25, 12, 2007);
		Tijd t2 = new Tijd(25, 12, 2007, 0);
		assertFalse(t1.isVoor(t2));
		assertFalse(t2.isVoor(t1));
		assertTrue(t1.isGelijk(t2));


		Tijd t3 = new Tijd(25, 12, 2007, 1);
		assertTrue(t1.isVoor(t3));
		assertFalse(t3.isVoor(t1));
		assertFalse(t1.isGelijk(t3));

		Tijd t4 = new Tijd(26, 12, 2007, 1);
		assertTrue(t3.isVoor(t4));
		assertFalse(t4.isVoor(t3));
		assertFalse(t3.isGelijk(t4));

		assertEquals(26, t4.getDag());
		assertEquals(12, t4.getMaand());
		assertEquals(2007, t4.getJaar());
		assertEquals(1, t4.getUren());

		assertTrue(t0.isGelijk(new Tijd()));
	}

	@Test
	public void testIsWerktijd() {
		assertFalse(new Tijd(25, 12, 2007).isWerktijd());
		assertTrue(new Tijd(25, 12, 2007, Tijd.BEGIN_UUR).isWerktijd());
		assertTrue(new Tijd(25, 12, 2007, Tijd.EIND_UUR - 1).isWerktijd());
		assertFalse(new Tijd(25, 12, 2007, Tijd.EIND_UUR).isWerktijd());
	}

	@Test
	public void testEersteWerktijd() {
		// Zelfde dag
		Tijd t1 = new Tijd(25, 12, 2007);
		Tijd t2 = t1.eersteWerktijd();
		assertEquals("25-12-2007", t2.formatDatum());
		assertEquals(Tijd.BEGIN_UUR + ":00", t2.formatTijd());

		// Volgende dag
		t1 = new Tijd(25, 12, 2007, Tijd.EIND_UUR);
		t2 = t1.eersteWerktijd();
		assertEquals("26-12-2007", t2.formatDatum());
		assertEquals(Tijd.BEGIN_UUR + ":00", t2.formatTijd());

		// Nu
		t1 = new Tijd(25, 12, 2007, Tijd.BEGIN_UUR);
		assertEquals(t1, t1.eersteWerktijd());
	}

	@Test
	public void testLater() {
		Tijd t1 = new Tijd(25, 12, 2007, 2);
		assertTrue(t1.isGelijk(t1.later(0)));
		assertEquals("05:00", t1.later(3).formatTijd());

		Tijd t2 = t1.later(23);
		assertEquals("01:00", t2.formatTijd());
		assertEquals("26-12-2007", t2.formatDatum());
	}

}
