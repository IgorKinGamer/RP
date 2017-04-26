package kg.rp.vor;

import java.util.*;

import kg.util.geom.*;

public class Triangulador
{
	private final LinkedHashMap<Aresta, OrigemAresta> próximas;
	private final List<Triângulo> triângulos;
	
	/** Ponto usado durante a triangulação. */
	private Ponto p3;
	private double tMín;
	
	public Triangulador()
	{
		próximas = new LinkedHashMap<>();
		triângulos = new LinkedList<>();
	}
	
	public List<Triângulo> triangular(Set<Ponto> pontos)
	{
		if (pontos.size() < 3)
			throw new IllegalArgumentException("Menos de 3 pontos");
		
		// Faz cópia do conjunto original
		pontos = new HashSet<>(pontos);
		
		// Encontra um triângulo inicial
		encontrarTriânguloInicial(pontos);
		
		// Para cada aresta na lista de próximas, buscar ponto à esquerda p3
		// (se houver) com o qual se tem o menor t
		while (!próximas.isEmpty())
		{
			Iterator<Map.Entry<Aresta, OrigemAresta>> it = próximas.entrySet().iterator();
			Map.Entry<Aresta, OrigemAresta> arestaComOrigem = it.next();
			it.remove();
			
			Aresta p1p2 = arestaComOrigem.getKey();
			encontrarTriângulo(p1p2, pontos);
			
			// Encontrou algum ponto à esquerda
			if (p3 != null)
			{
				Ponto p1, p2;
				p1 = p1p2.p1;
				p2 = p1p2.p2;
				
				// Monta o triângulo
				Triângulo trNovo = new Triângulo(p1, p2, p3, centro(p1p2, tMín));
				triângulos.add(trNovo);
				
				// Pega triângulo de origem
				OrigemAresta origemAresta = arestaComOrigem.getValue();
				Triângulo trOrigem = origemAresta.triângulo;
				int lado = origemAresta.lado;
				
				// Liga os triângulos (o de origem da aresta e o novo)
				trOrigem.atribuirAdjacente(lado, trNovo);
				trNovo.atribuirAdjacente(1, trOrigem);
				
				// Novas arestas
				arestaNova(p3, p2, trNovo, 2);
				arestaNova(p1, p3, trNovo, 3);
			}
		}
		
		return triângulos;
	}
	
	private void encontrarTriânguloInicial(Set<Ponto> pontos)
	{
		Aresta p1p2 = arestaInicial(pontos);
		encontrarTriângulo(p1p2, pontos);
		if (p3 == null)
		{
			p1p2 = new Aresta(p1p2.p2, p1p2.p1); // Inverte
			encontrarTriângulo(p1p2, pontos);
		}
		if (p3 != null)
		{
			Ponto p1, p2;
			p1 = p1p2.p1;
			p2 = p1p2.p2;
			
			// Monta o triângulo
			Triângulo t = new Triângulo(p1, p2, p3, centro(p1p2, tMín));
			triângulos.add(t);
			
			// Novas arestas
			arestaNova(p2, p1, t, 1);
			arestaNova(p3, p2, t, 2);
			arestaNova(p1, p3, t, 3);
		}
		else
			System.err.println("Aviso: conjunto de pontos intriangulável");
	}
	
	private void encontrarTriângulo(Aresta p1p2, Set<Ponto> pontos)
	{
		Ponto p1, p2;
		p1 = p1p2.p1;
		p2 = p1p2.p2;
		p3 = null;
		tMín = Double.POSITIVE_INFINITY;
		
		// Precisamos passar por todos os pontos MENOS p1 e p2
		pontos.remove(p1);
		pontos.remove(p2);
		pontos.stream()
				// Se está à esquerda
				.filter(p -> p1p2.pontoÀEsquerda(p))
				.forEachOrdered(p ->
				{
					double t = t(p1p2, p);
					if (t < tMín)
					{
						p3 = p;
						tMín = t;
					}
				});
		// Devolve p1 e p2 ao conjunto
		pontos.add(p1);
		pontos.add(p2);
	}
	
	private void arestaNova(Ponto p1, Ponto p2, Triângulo origemNova, int ladoNovo)
	{
		Aresta a = new Aresta(p1, p2);
		if (próximas.containsKey(a))
		{
			OrigemAresta origemVelhaAresta = próximas.remove(a);
			Triângulo origemVelha = origemVelhaAresta.triângulo;
			int ladoVelho = origemVelhaAresta.lado;
			
			origemNova.atribuirAdjacente(ladoNovo, origemVelha);
			origemVelha.atribuirAdjacente(ladoVelho, origemNova);
		}
		else
			próximas.put(a, new OrigemAresta(origemNova, ladoNovo));
	}
	
	/**
	 * Retorna uma aresta formada por um ponto qualquer e o ponto mais próximo
	 * dele
	 * @param pontos Pontos
	 * @return Uma aresta
	 */
	private Aresta arestaInicial(Set<Ponto> pontos)
	{
		Iterator<Ponto> it = pontos.iterator();
		Ponto p1 = it.next(), p2 = null;
		double distMín = Double.POSITIVE_INFINITY;
		do
		{
			Ponto o = it.next();
			double d = p1.distância(o);
			if (d < distMín)
			{
				p2 = o;
				distMín = d;
			}
		}
		while (it.hasNext());
		
		return new Aresta(p1, p2);
	}
	
	/**
	 * Retorna um valor {@code t} proporcional a quão deslocado para esquerda do ponto
	 * médio de p1 e p2 (do ponto de vista do vetor p1->p2) está o circuncentro
	 * do triângulo (p1, p2, ponto).
	 * Pode ser negativo, se {@code ponto} estiver à direita de p1->p2.
	 */
	private double t(Aresta p1p2, Ponto ponto)
	{
		Vetor p = new Vetor(ponto);
		Vetor v = p1p2.v.perpendicular();
		// Usa p1 como base (poderia ser p2)
		Vetor b = new Vetor(p1p2.p1);
		Vetor bp = new Vetor(b, p);
		
		Vetor c = new Vetor(p1p2.pontoMédio());
		
		double cima, baixo;
		cima = p.normaAoQuadrado() - b.normaAoQuadrado()
				- 2*c.produtoInterno(bp);
		baixo = 2*v.produtoInterno(bp);
		return cima/baixo;
	}
	
	/**
	 * Circuncentro do triângulo p1, p2, p3, dado que
	 * <ul>
	 * <li>p1p2 é a aresta p1->p2</li>
	 * <li>{@code t == t(p1p2, p3)}</li>
	 * </ul>
	 * @param p1p2 Alguma aresta
	 * @param t {@code t(p1p2, p3)} para algum {@code p3}
	 * @return Circuncentro do triângulo p1, p2, p3
	 */
	private Ponto centro(Aresta p1p2, double t)
	{
		return p1p2.pontoMédio().mais(p1p2.v.perpendicular().vezes(t));
	}
	
	private class OrigemAresta
	{
		final Triângulo triângulo;
		final int lado;
		OrigemAresta(Triângulo o, int l)
		{
			triângulo = o;
			lado = l;
		}
	}
}

/*
Entrada:
	Conjunto de pontos (amostras...)
Saída:
	Conjunto de triângulos, cada um com a informação de
	qual é o triângulo (se houver) adjacente por cada
	aresta e qual o seu circuncentro.
*/
