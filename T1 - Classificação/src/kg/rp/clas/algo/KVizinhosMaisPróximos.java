package kg.rp.clas.algo;

import java.util.*;

import kg.rp.clas.*;
import kg.rp.clas.metr.*;

public class KVizinhosMaisPróximos implements Classificador
{
	private static final String MSG_ERRO_CONJUNTO_PEQUENO =
			"Conjunto de treinamento pequeno demais";
	private static final String MSG_ERRO_K_NEGATIVO =
			"k negativo";
	
	/** Quantidade de vizinhos verificados. */
	private int k;
	
	/**
	 * 
	 * @param k Número de vizinhos mais próximos; deve ser > 0
	 */
	public KVizinhosMaisPróximos(int k)
	{
		if (k < 0)
			throw new IllegalArgumentException(MSG_ERRO_K_NEGATIVO);
		this.k = k;
	}
	
	@Override
	public <C> C classificar(ConjuntoTreinamento<C> conjTreinamento,
			Amostra amostra, Métrica métrica) // TODO
	{
		if (conjTreinamento.quantidadeAmostras() < k)
			throw new IllegalStateException(MSG_ERRO_CONJUNTO_PEQUENO);
		
		// O(n log k)
		NavigableSet<Medição<C>> kMenores = new TreeSet<>();
		Iterator<Classificação<C>> itCT = conjTreinamento.iterator();
		do // k primeiros
		{
			Classificação<C> cl = itCT.next();
			Amostra outra = cl.amostra();
			double dif = métrica.distância(amostra, outra);
			kMenores.add(new Medição<>(cl, dif));
		}
		while (kMenores.size() < k);
		// Resto
		while (itCT.hasNext())
		{
			Classificação<C> cl = itCT.next();
			Amostra outra = cl.amostra();
			double dif = métrica.distância(amostra, outra);
			if (dif < kMenores.last().valor)
			{
				kMenores.pollLast();
				kMenores.add(new Medição<>(cl, dif));
			}
		}
		
		// Encontra a classe mais bombada
		Map<C, Integer> escolhidas = new HashMap<>();
		for (Medição<C> med : kMenores)
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

	private static class Medição<C> implements Comparable<Medição<C>>
	{
		Classificação<C> cl;
		double valor;
		
		Medição(Classificação<C> cl, double valor)
		{
			this.cl = cl;
			this.valor = valor;
		}
		
		public int compareTo(Medição<C> o)
		{
			if (valor != o.valor)
				return (int) Math.signum(valor - o.valor);
			return (int) Math.signum(cl.id() - o.cl.id());
		}
	}
}
