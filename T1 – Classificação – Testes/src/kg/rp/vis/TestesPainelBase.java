package kg.rp.vis;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import kg.util.geom.*;
import kg.util.swing.*;

public class TestesPainelBase
{
	@Test
	public void transformaçãoInversa()
	{
		final int numPontos = 200;
		Random r = new Random(1337);
		List<Ponto> pontos = new ArrayList<>(numPontos);
		for (int i = 0; i < numPontos; i++)
			pontos.add(new Ponto(r.nextDouble(), r.nextDouble()));
		PainelBase pb = new PainelBase(200, 200, pontos, p -> p);
		
		for (Ponto p : pontos)
		{
			Ponto q = pb.transformarTelaParaMundo(pb.transformar(p));
			assertEquals(0, p.distância(q), 1e-14);
		}
	}
}
