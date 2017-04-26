package kg.rp.vor;

import java.util.*;

import kg.util.geom.*;

public class Triangulador
{
	private final LinkedHashMap<Aresta, OrigemAresta> pr�ximas;
	private final List<Tri�ngulo> tri�ngulos;
	
	/** Ponto usado durante a triangula��o. */
	private Ponto p3;
	private double tM�n;
	
	public Triangulador()
	{
		pr�ximas = new LinkedHashMap<>();
		tri�ngulos = new LinkedList<>();
	}
	
	public List<Tri�ngulo> triangular(Set<Ponto> pontos)
	{
		if (pontos.size() < 3)
			throw new IllegalArgumentException("Menos de 3 pontos");
		
		// Faz c�pia do conjunto original
		pontos = new HashSet<>(pontos);
		
		// Encontra um tri�ngulo inicial
		encontrarTri�nguloInicial(pontos);
		
		// Para cada aresta na lista de pr�ximas, buscar ponto � esquerda p3
		// (se houver) com o qual se tem o menor t
		while (!pr�ximas.isEmpty())
		{
			Iterator<Map.Entry<Aresta, OrigemAresta>> it = pr�ximas.entrySet().iterator();
			Map.Entry<Aresta, OrigemAresta> arestaComOrigem = it.next();
			it.remove();
			
			Aresta p1p2 = arestaComOrigem.getKey();
			encontrarTri�ngulo(p1p2, pontos);
			
			// Encontrou algum ponto � esquerda
			if (p3 != null)
			{
				Ponto p1, p2;
				p1 = p1p2.p1;
				p2 = p1p2.p2;
				
				// Monta o tri�ngulo
				Tri�ngulo trNovo = new Tri�ngulo(p1, p2, p3, centro(p1p2, tM�n));
				tri�ngulos.add(trNovo);
				
				// Pega tri�ngulo de origem
				OrigemAresta origemAresta = arestaComOrigem.getValue();
				Tri�ngulo trOrigem = origemAresta.tri�ngulo;
				int lado = origemAresta.lado;
				
				// Liga os tri�ngulos (o de origem da aresta e o novo)
				trOrigem.atribuirAdjacente(lado, trNovo);
				trNovo.atribuirAdjacente(1, trOrigem);
				
				// Novas arestas
				arestaNova(p3, p2, trNovo, 2);
				arestaNova(p1, p3, trNovo, 3);
			}
		}
		
		return tri�ngulos;
	}
	
	private void encontrarTri�nguloInicial(Set<Ponto> pontos)
	{
		Aresta p1p2 = arestaInicial(pontos);
		encontrarTri�ngulo(p1p2, pontos);
		if (p3 == null)
		{
			p1p2 = new Aresta(p1p2.p2, p1p2.p1); // Inverte
			encontrarTri�ngulo(p1p2, pontos);
		}
		if (p3 != null)
		{
			Ponto p1, p2;
			p1 = p1p2.p1;
			p2 = p1p2.p2;
			
			// Monta o tri�ngulo
			Tri�ngulo t = new Tri�ngulo(p1, p2, p3, centro(p1p2, tM�n));
			tri�ngulos.add(t);
			
			// Novas arestas
			arestaNova(p2, p1, t, 1);
			arestaNova(p3, p2, t, 2);
			arestaNova(p1, p3, t, 3);
		}
		else
			System.err.println("Aviso: conjunto de pontos intriangul�vel");
	}
	
	private void encontrarTri�ngulo(Aresta p1p2, Set<Ponto> pontos)
	{
		Ponto p1, p2;
		p1 = p1p2.p1;
		p2 = p1p2.p2;
		p3 = null;
		tM�n = Double.POSITIVE_INFINITY;
		
		// Precisamos passar por todos os pontos MENOS p1 e p2
		pontos.remove(p1);
		pontos.remove(p2);
		pontos.stream()
				// Se est� � esquerda
				.filter(p -> p1p2.ponto�Esquerda(p))
				.forEachOrdered(p ->
				{
					double t = t(p1p2, p);
					if (t < tM�n)
					{
						p3 = p;
						tM�n = t;
					}
				});
		// Devolve p1 e p2 ao conjunto
		pontos.add(p1);
		pontos.add(p2);
	}
	
	private void arestaNova(Ponto p1, Ponto p2, Tri�ngulo origemNova, int ladoNovo)
	{
		Aresta a = new Aresta(p1, p2);
		if (pr�ximas.containsKey(a))
		{
			OrigemAresta origemVelhaAresta = pr�ximas.remove(a);
			Tri�ngulo origemVelha = origemVelhaAresta.tri�ngulo;
			int ladoVelho = origemVelhaAresta.lado;
			
			origemNova.atribuirAdjacente(ladoNovo, origemVelha);
			origemVelha.atribuirAdjacente(ladoVelho, origemNova);
		}
		else
			pr�ximas.put(a, new OrigemAresta(origemNova, ladoNovo));
	}
	
	/**
	 * Retorna uma aresta formada por um ponto qualquer e o ponto mais pr�ximo
	 * dele
	 * @param pontos Pontos
	 * @return Uma aresta
	 */
	private Aresta arestaInicial(Set<Ponto> pontos)
	{
		Iterator<Ponto> it = pontos.iterator();
		Ponto p1 = it.next(), p2 = null;
		double distM�n = Double.POSITIVE_INFINITY;
		do
		{
			Ponto o = it.next();
			double d = p1.dist�ncia(o);
			if (d < distM�n)
			{
				p2 = o;
				distM�n = d;
			}
		}
		while (it.hasNext());
		
		return new Aresta(p1, p2);
	}
	
	/**
	 * Retorna um valor {@code t} proporcional a qu�o deslocado para esquerda do ponto
	 * m�dio de p1 e p2 (do ponto de vista do vetor p1->p2) est� o circuncentro
	 * do tri�ngulo (p1, p2, ponto).
	 * Pode ser negativo, se {@code ponto} estiver � direita de p1->p2.
	 */
	private double t(Aresta p1p2, Ponto ponto)
	{
		Vetor p = new Vetor(ponto);
		Vetor v = p1p2.v.perpendicular();
		// Usa p1 como base (poderia ser p2)
		Vetor b = new Vetor(p1p2.p1);
		Vetor bp = new Vetor(b, p);
		
		Vetor c = new Vetor(p1p2.pontoM�dio());
		
		double cima, baixo;
		cima = p.normaAoQuadrado() - b.normaAoQuadrado()
				- 2*c.produtoInterno(bp);
		baixo = 2*v.produtoInterno(bp);
		return cima/baixo;
	}
	
	/**
	 * Circuncentro do tri�ngulo p1, p2, p3, dado que
	 * <ul>
	 * <li>p1p2 � a aresta p1->p2</li>
	 * <li>{@code t == t(p1p2, p3)}</li>
	 * </ul>
	 * @param p1p2 Alguma aresta
	 * @param t {@code t(p1p2, p3)} para algum {@code p3}
	 * @return Circuncentro do tri�ngulo p1, p2, p3
	 */
	private Ponto centro(Aresta p1p2, double t)
	{
		return p1p2.pontoM�dio().mais(p1p2.v.perpendicular().vezes(t));
	}
	
	private class OrigemAresta
	{
		final Tri�ngulo tri�ngulo;
		final int lado;
		OrigemAresta(Tri�ngulo o, int l)
		{
			tri�ngulo = o;
			lado = l;
		}
	}
}

/*
Entrada:
	Conjunto de pontos (amostras...)
Sa�da:
	Conjunto de tri�ngulos, cada um com a informa��o de
	qual � o tri�ngulo (se houver) adjacente por cada
	aresta e qual o seu circuncentro.
*/
