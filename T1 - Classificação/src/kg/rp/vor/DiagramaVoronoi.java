package kg.rp.vor;

import java.util.*;

import kg.rp.vor.vis.*;
import kg.util.geom.*;

public class DiagramaVoronoi
{
	private final Set<Ponto> pontos;
	private final Set<Aresta> arestasTriângulos, arestas, arestasInfinitas;
	
	public DiagramaVoronoi(Set<Ponto> pontos)
	{
		this.pontos = new HashSet<>(pontos);
		arestasTriângulos = new HashSet<>();
		arestas = new HashSet<>();
		arestasInfinitas = new HashSet<>();
		
		List<Triângulo> triângulos = new Triangulador().triangular(pontos);
		
		for (Triângulo t : triângulos)
		{
			for (int lado = 1; lado <= 3; lado++)
			{
				arestasTriângulos.add(t.aresta(lado));
				
				Triângulo adj = t.pegarAdjacente(lado);
				if (adj != null)
					arestas.add(new Aresta(t.cc, adj.cc));
				else
					arestasInfinitas.add(new Aresta(
							t.cc,
							t.cc.mais(t.aresta(lado).v.perpendicular().vezes(-1))));
			}
		}
	}
	
	public void mostrar(int largura, int altura)
	{
		PainelVoronoi p = new PainelVoronoi(largura, altura, pontos,
				arestasTriângulos, arestas, arestasInfinitas);
		p.mostrar();
	}
}
