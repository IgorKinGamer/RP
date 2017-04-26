package kg.rp.vor;

import static kg.rp.vor.…Tri‚ngulo.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.*;
import static org.junit.Assert.*;

import java.util.*;

import org.hamcrest.*;
import org.junit.*;

import kg.util.geom.*;

public class TestesTriangulador
{
	@Test(expected = IllegalArgumentException.class)
	public void poucosPontos()
	{
		new Triangulador().triangular(conjunto(1, 2, 3, 3.14));
	}
	
	@Test
	public void nenhumTri‚ngulo()
	{
		List<Tri‚ngulo> tri‚ngulos = triangular(-1, 0, 0, 0, 1, 0);
		
		assertTrue(tri‚ngulos.isEmpty());
	}
	
	@Test
	public void umTri‚ngulo()
	{
		double[] coords = new double[] {
				-1, 0,
				0, 0,
				1, 1
		};
		Set<Ponto> pontos = conjunto(coords);
		
		List<Tri‚ngulo> umTri‚ngulo = triangular(pontos);
		
		assertEquals(1, umTri‚ngulo.size());
		assertThat(umTri‚ngulo.get(0), ÈTri‚ngulo(pontos));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void doisTri‚ngulos()
	{
		double[] coords = new double[] {
				-1, 0,
				-1, 1,
				0, 0,
				1, 1
		};
		Set<Ponto> t1, t2;
		t1 = conjunto(-1, 0, -1, 1, 0, 0);
		t2 = conjunto(-1, 1, 0, 0, 1, 1);
		
		List<Tri‚ngulo> doisTri‚ngulos = triangular(coords);
		
		assertEquals("Quantidade de tri‚ngulos", 2, doisTri‚ngulos.size());
		assertThat(doisTri‚ngulos, hasItems(ÈTri‚ngulo(t1), ÈTri‚ngulo(t2)));
	}
	
	@Test
	public void pontosEmLinhaMaisUm()
	{
		final int numPontosLinha = 10;
		List<Ponto> pontosEmLinha = new LinkedList<>();
		for (int i = 0; i < numPontosLinha; i++)
			pontosEmLinha.add(new Ponto(-3.5 + i, 4.5 + i/2d));
		Ponto foraDaLinha = new Ponto(0, 10);
		
		// Lixo do hamcrest? contains...(Collection<Matcher<...>>)
		// em vez de contains...(Collection<? extend Matcher<...>>)
		Collection<Matcher<? super Tri‚ngulo>> testes = new LinkedList<>();
		Iterator<Ponto> itLinha = pontosEmLinha.iterator();
		Ponto anterior = itLinha.next();
		while (itLinha.hasNext())
		{
			Ponto atual = itLinha.next();
			Set<Ponto> tri‚ngulo = new HashSet<>();
			tri‚ngulo.add(anterior);
			tri‚ngulo.add(atual);
			tri‚ngulo.add(foraDaLinha);
			testes.add(ÈTri‚ngulo(tri‚ngulo));
			anterior = atual;
		}
		
		Set<Ponto> pontos = new HashSet<>(pontosEmLinha);
		pontos.add(foraDaLinha);
		
		
		List<Tri‚ngulo> tri‚ngulos = triangular(pontos);
		
		
		assertEquals("N˙mero de tri‚ngulos", numPontosLinha-1, tri‚ngulos.size());
		assertThat(tri‚ngulos, containsInAnyOrder(testes));
	}
	
	private Set<Ponto> conjunto(double... coords)
	{
		if (coords.length % 2 != 0)
			throw new IllegalArgumentException("N˙mero Ìmpar de coordenadas");
		
		Set<Ponto> pontos = new HashSet<>();
		for (int i = 0; i < coords.length; i += 2)
			pontos.add(new Ponto(coords[i], coords[i+1]));
		
		return pontos;
	}
	
	private List<Tri‚ngulo> triangular(double... coords)
	{
		return triangular(conjunto(coords));
	}
	
	private List<Tri‚ngulo> triangular(Set<Ponto> pontos)
	{
		return new Triangulador().triangular(pontos);
	}
	
	/*private boolean ÈTri‚ngulo(Set<Ponto> pontos, Tri‚ngulo t)
	{
		return pontos.contains(t.p1)
				&& pontos.contains(t.p2)
				&& pontos.contains(t.p3);
	}*/
}

class …Tri‚ngulo extends BaseMatcher<Tri‚ngulo>
{
	final Set<Ponto> pontos;
	
	public …Tri‚ngulo(Set<Ponto> pontos)
	{
		this.pontos = pontos;
	}
	
	@Override
	public boolean matches(Object item)
	{
		if (!(item instanceof Tri‚ngulo))
			return false;
		
		Tri‚ngulo t = (Tri‚ngulo) item;
		return pontos.contains(t.p1)
				&& pontos.contains(t.p2)
				&& pontos.contains(t.p3);
	}

	@Override
	public void describeTo(Description description)
	{
		description.appendText(pontos.toString());
	}
	
	public static …Tri‚ngulo ÈTri‚ngulo(Set<Ponto> pontos)
	{
		return new …Tri‚ngulo(pontos);
	}
}
