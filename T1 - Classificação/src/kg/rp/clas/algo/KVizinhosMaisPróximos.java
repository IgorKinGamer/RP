package kg.rp.clas.algo;

import java.util.*;

import kg.rp.clas.*;
import kg.rp.clas.metr.*;

public class KVizinhosMaisPr�ximos implements Classificador
{
	private static final String MSG_ERRO_CONJUNTO_PEQUENO =
			"Conjunto de treinamento pequeno demais";
	private static final String MSG_ERRO_K_NEGATIVO =
			"k negativo";
	
	/** Quantidade de vizinhos verificados. */
	private int k;
	
	/**
	 * 
	 * @param k N�mero de vizinhos mais pr�ximos; deve ser > 0
	 */
	public KVizinhosMaisPr�ximos(int k)
	{
		if (k < 0)
			throw new IllegalArgumentException(MSG_ERRO_K_NEGATIVO);
		this.k = k;
	}
	
	@Override
	public <C> C classificar(ConjuntoTreinamento<C> conjTreinamento,
			Amostra amostra, M�trica m�trica) // TODO
	{
		if (conjTreinamento.quantidadeAmostras() < k)
			throw new IllegalStateException(MSG_ERRO_CONJUNTO_PEQUENO);
		
		// O(n log k)
		NavigableSet<Medi��o<C>> kMenores = new TreeSet<>();
		Iterator<Classifica��o<C>> itCT = conjTreinamento.iterator();
		do // k primeiros
		{
			Classifica��o<C> cl = itCT.next();
			Amostra outra = cl.amostra();
			double dif = m�trica.dist�ncia(amostra, outra);
			kMenores.add(new Medi��o<>(cl, dif));
		}
		while (kMenores.size() < k);
		// Resto
		while (itCT.hasNext())
		{
			Classifica��o<C> cl = itCT.next();
			Amostra outra = cl.amostra();
			double dif = m�trica.dist�ncia(amostra, outra);
			if (dif < kMenores.last().valor)
			{
				kMenores.pollLast();
				kMenores.add(new Medi��o<>(cl, dif));
			}
		}
		
		// Encontra a classe mais bombada
		Map<C, Integer> escolhidas = new HashMap<>();
		for (Medi��o<C> med : kMenores)
		{
			C classe = med.cl.classe();
			if (!escolhidas.containsKey(classe))
				escolhidas.put(classe, 1);
			else
				escolhidas.put(classe, escolhidas.get(classe) + 1);
		}
		int vezes = 0;
		C classe = null;
		for (Map.Entry<C, Integer> e : escolhidas.entrySet())
		{
			if (e.getValue() > vezes)
			{
				vezes = e.getValue();
				classe = e.getKey();
			}
		}
		return classe;
	}

	private static class Medi��o<C> implements Comparable<Medi��o<C>>
	{
		Classifica��o<C> cl;
		double valor;
		
		Medi��o(Classifica��o<C> cl, double valor)
		{
			this.cl = cl;
			this.valor = valor;
		}
		
		public int compareTo(Medi��o<C> o)
		{
			if (valor != o.valor)
				return (int) Math.signum(valor - o.valor);
			return (int) Math.signum(cl.id() - o.cl.id());
		}
	}
}
