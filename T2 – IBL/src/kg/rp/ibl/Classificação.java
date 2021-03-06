package kg.rp.ibl;

/**
 * Representa a classifica��o de uma amostra.
 * <p>
 * O tipo das classes pode ser uma enumera��o em casos em que todas as op��es
 * j� s�o previamente conhecidas.
 *
 * @param <C>
 */
public class Classifica��o<C>
{
	/** Id que ser� atribu�do � pr�xima classifica��o criada. */
	private static long pr�ximoId = 0;
	
	/** Id da classifica��o, �nico na aplica��o. */
	private final long id;
	
	/** A amostra classificada. */
	private Amostra amostra;
	/** A classe atribu�da � amostra. */
	private C classe;
	
	/**
	 * Cria uma classifica��o que representa que a amostra {@code a} � da classe
	 * {@code c}.
	 * @param a A amostra
	 * @param c A classe atribu�da a amostra
	 */
	public Classifica��o(Amostra a, C c)
	{
		id = pr�ximoId++;
		
		amostra = a;
		classe = c;
	}
	
	/**
	 * A amostra classificada.
	 * @return A amostra classificada
	 */
	public Amostra amostra()
	{
		return amostra;
	}
	
	/**
	 * A classe atribu�da � amostra.
	 * @return A classe atribu�da � amostra
	 */
	public C classe()
	{
		return classe;
	}
	
	@Override
	public String toString()
	{
		return String.format("(%s -> %s)", amostra.toString(), classe.toString());
	}
	
	/**
	 * Retorna o id da classifica��o, �nico na aplica��o.
	 * Usado para ordenar classifica��es id�nticas.
	 * @return O id da classifica��o
	 */
	public long id()
	{
		return id;
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof Classifica��o
				&& id == ((Classifica��o<?>) o).id;
				/*&& amostra.equals(((Classifica��o<?>) o).amostra)
				&& classe.equals(((Classifica��o<?>) o).classe);*/
	}
	
	@Override
	public int hashCode()
	{
		return amostra.hashCode() + 37*classe.hashCode();
	}
}
