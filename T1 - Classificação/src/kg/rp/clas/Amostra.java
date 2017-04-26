package kg.rp.clas;

import java.util.*;

/**
 * Uma amostra que pode ser classificada para reconhecer padr�es.
 * Uma amostra � simplesmente um vetor de valores reais (<i>atributos</i>).
 * <p>
 * Amostras s�o imut�veis, ou seja, uma vez criada, os valores de uma amostra
 * nunca mudar�o.
 */
public final class Amostra
{
	private static final String MSG_VETOR_VAZIO = "Vetor de tamanho 0";
	
	/** Vetor de valores da amostra. */
	private List<Double> valores;
	
	/**
	 * Cria uma amostra a partir do vetor passado (em forma de lista).
	 * O tamanho da amostra ser� o tamanho do vetor ({@code vals.size()})
	 * @param vals Vetor com os valores dos atributos
	 */
	public Amostra(List<Double> vals)
	{
		if (vals.size() == 0)
			throw new IllegalArgumentException(MSG_VETOR_VAZIO);
		valores = new ArrayList<>(vals);
	}
	
	/**
	 * Cria uma amostra a partir do vetor passado (em forma de array).
	 * O tamanho da amostra ser� o tamanho do vetor ({@code vals.length})
	 * @param vals Vetor com os valores dos atributos
	 */
	public Amostra(double[] vals)
	{
		if (vals.length == 0)
			throw new IllegalArgumentException(MSG_VETOR_VAZIO);
		valores = new ArrayList<>(vals.length);
		for (double d : vals)
			valores.add(d);
	}
	
	/** 
	 * Retorna a quantidade de atributos (o tamanho) desta amostra.
	 * @return O tamanho desta amostra
	 */
	public int tamanho()
	{
		return valores.size();
	}
	
	/**
	 * Retorna o valor do i-�simo atributo.
	 * @param i �ndice do atributo
	 * @return O valor do i-�simo atributo
	 */
	public double atributo(int i)
	{
		return valores.get(i);
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o instanceof Amostra && valores.equals(((Amostra) o).valores);
	}

	@Override
	public int hashCode()
	{
		return valores.hashCode();
	}
}
