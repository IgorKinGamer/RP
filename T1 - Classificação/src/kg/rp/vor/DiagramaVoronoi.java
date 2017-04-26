package kg.rp.vor;

import java.util.*;

import kg.rp.vor.vis.*;
import kg.util.geom.*;

public class DiagramaVoronoi
{
	private final Set<Ponto> pontos;
	private final Set<Aresta> arestasTri�ngulos, arestas, arestasInfinitas;
	
	public DiagramaVoronoi(Set<Ponto> pontos)
	{
		this.pontos = new HashSet<>(pontos);
		arestasTri�ngulos = new HashSet<>();
		arestas = new HashSet<>();
		arestasInfinitas = new HashSet<>();
		
		List<Tri�ngulo> tri�ngulos = new Triangulador().triangular(pontos);
		
		for (Tri�ngulo t : tri�ngulos)
		{
			for (int lado = 1; lado <= 3; lado++)
			{
				arestasTri�ngulos.add(t.aresta(lado));
				
				Tri�ngulo adj = t.pegarAdjacente(lado);
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
				arestasTri�ngulos, arestas, arestasInfinitas);
		p.mostrar();
	}
}
