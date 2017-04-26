package kg.rp.vor;

import static org.junit.Assert.*;

import org.junit.*;

import kg.util.geom.*;

public class TestesAresta
{
	@Test
	public void construtorCoordenadas()
	{
		Aresta aresta = new Aresta(3, 4, -2, 7);
		Ponto p1 = new Ponto(3, 4);
		Ponto p2 = new Ponto(-2, 7);
		
		Assert.assertEquals(p1, aresta.p1);
		Assert.assertEquals(p2, aresta.p2);
	}
	
	
	@Test
	public void ponto¿EsquerdaCima()
	{
		Aresta aresta = new Aresta(0, 0, 0, 1);
		Ponto pontoCima = new Ponto(-5, 7);
		
		assertTrue(aresta.ponto¿Esquerda(pontoCima));
	}
	
	@Test
	public void ponto¿EsquerdaMeio()
	{
		Aresta aresta = new Aresta(0, 0, 0, 1);
		Ponto pontoMeio = new Ponto(-5, 0);
		
		assertTrue(aresta.ponto¿Esquerda(pontoMeio));
	}
	
	@Test
	public void ponto¿EsquerdaBaixo()
	{
		Aresta aresta = new Aresta(0, 0, 0, 1);
		Ponto pontoBaixo = new Ponto(-5, -7);
		
		assertTrue(aresta.ponto¿Esquerda(pontoBaixo));
	}
	
	@Test
	public void pontoCentro() throws Exception
	{
		Aresta aresta = new Aresta(0, 0, 0, 1);
		Ponto ponto = new Ponto(0, 0);
		
		assertFalse(aresta.ponto¿Esquerda(ponto));
	}
	
	@Test
	public void ponto¿Direita() throws Exception
	{
		Aresta aresta = new Aresta(0, 0, 0, 1);
		Ponto ponto = new Ponto(3, 0);
		
		assertFalse(aresta.ponto¿Esquerda(ponto));
	}
	
	
	@Test
	public void pontoMÈdio()
	{
		Ponto p1, p2;
		p1 = new Ponto(2, 9);
		p2 = new Ponto(-5, 7);
		Aresta aresta = new Aresta(p1, p2);
		
		assertEquals(Ponto.pontoMÈdio(p1, p2), aresta.pontoMÈdio());
	}
}
