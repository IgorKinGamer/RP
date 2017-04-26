package kg.rp.clas;

import java.util.*;

public class ConjuntoTreinamento<C> implements Iterable<Classificação<C>>
{
	private static final String MSG_TAMANHO_ERRADO =
			"Tamanho da amostra incorreto";
	private static final String MSG_CLASSIFICAÇÃO_REPETIDA =
			"Amostra já está no conjunto";
	
	/** Conjunto de amostras classificadas. */
	private Set<Classificação<C>> conj;
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
	 * Adiciona uma amostra classificada (classificação) a este conjunto.
	 * O tamanho da amostra deve ser o mesmo que {@link #tamanho()}.
	 * @param c A classificação a ser adicionada
	 */
	public void adicionar(Classificação<C> c)
	{
		Amostra a = c.amostra();
		if (a.tamanho() != tamanho)
			throw new IllegalArgumentException(MSG_TAMANHO_ERRADO);
		if (ids.contains(c.id()))
			throw new IllegalArgumentException(MSG_CLASSIFICAÇÃO_REPETIDA);
		conj.add(c);
		ids.add(c.id());
	}
	
	/**
	 * Adiciona uma amostra classificada a este conjunto.
	 * O mesmo que {@link #adicionar(Classificação)} passando
	 * {@code new Classificação<>(a, c)}.
	 * @param a A amostra a ser adicionada
	 * @param c A sua classe
	 */
	public void adicionar(Amostra a, C c)
	{
		adicionar(new Classificação<>(a, c));
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
	public Iterator<Classificação<C>> iterator()
	{
		return conj.iterator();
	}
}
