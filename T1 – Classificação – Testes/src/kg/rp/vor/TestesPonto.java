package kg.rp.vor;

import static org.junit.Assert.*;

import org.junit.*;

import kg.util.geom.*;

public class TestesPonto
{
	@Test
	public void construtor()
	{
		Ponto ponto = new Ponto(-3.14, 222);
		
		assertEquals(-3.14, ponto.x, 0);
		assertEquals(222, ponto.y, 0);
	}
	
	@Test
	public void pontoMédio()
	{
		Ponto p1, p2;
		p1 = new Ponto(2, 9);
		p2 = new Ponto(-5, 7);
		
		assertEquals(new Ponto(-1.5, 8), Ponto.pontoMédio(p1, p2));
	}
}
