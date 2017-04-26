package kg.rp.clas;

import java.util.*;

public class ConjuntoTreinamento<C> implements Iterable<Classifica��o<C>>
{
	private static final String MSG_TAMANHO_ERRADO =
			"Tamanho da amostra incorreto";
	private static final String MSG_CLASSIFICA��O_REPETIDA =
			"Amostra j� est� no conjunto";
	
	/** Conjunto de amostras classificadas. */
	private Set<Classifica��o<C>> conj;
	private Set<Long> ids;
	
	/** Tamanho (quantidade de valores) das amostras deste conjunto. */
	private int tamanho;
	
	/**
	 * Cria um conjunto de treinamento que aceita amostras
	 * de {@link Amostra#tamanho() tamanho} {@code tam}.
	 * @param tam O tamanho das amostras aceitas neste conjunto
	 */
	public ConjuntoTreinamento(int tam)
	{
		conj = new HashSet<>();
		ids = new HashSet<>();
		tamanho = tam;
	}
	
	/**
	 * Adiciona uma amostra classificada (classifica��o) a este conjunto.
	 * O tamanho da amostra deve ser o mesmo que {@link #tamanho()}.
	 * @param c A classifica��o a ser adicionada
	 */
	public void adicionar(Classifica��o<C> c)
	{
		Amostra a = c.amostra();
		if (a.tamanho() != tamanho)
			throw new IllegalArgumentException(MSG_TAMANHO_ERRADO);
		if (ids.contains(c.id()))
			throw new IllegalArgumentException(MSG_CLASSIFICA��O_REPETIDA);
		conj.add(c);
		ids.add(c.id());
	}
	
	/**
	 * Adiciona uma amostra classificada a este conjunto.
	 * O mesmo que {@link #adicionar(Classifica��o)} passando
	 * {@code new Classifica��o<>(a, c)}.
	 * @param a A amostra a ser adicionada
	 * @param c A sua classe
	 */
	public void adicionar(Amostra a, C c)
	{
		adicionar(new Classifica��o<>(a, c));
	}
	
	/**
	 * Retorna a quantidade de amostras neste conjunto.
	 * @return A quantidade de amostras neste conjunto.
	 */
	public int quantidadeAmostras()
	{
		return conj.size();
	}
	
	/**
	 * Retorna o {@link Amostra#tamanho() tamanho} das amostras neste conjunto.
	 * @return O tamanho das amostras neste conjunto.
	 */
	public int tamanhoAmostras()
	{
		return tamanho;
	}
	
	@Override
	public Iterator<Classifica��o<C>> iterator()
	{
		return conj.iterator();
	}
}
