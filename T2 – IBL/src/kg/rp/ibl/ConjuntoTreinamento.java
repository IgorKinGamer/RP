package kg.rp.ibl;

import java.util.*;

public class ConjuntoTreinamento implements Iterable<Classifica��o<String>>
{
	private final List<Classifica��o<String>> amostras;
	
	public ConjuntoTreinamento(Collection<Classifica��o<String>> as)
	{
		amostras = new ArrayList<>(as);
	}
	
	@Override
	public Iterator<Classifica��o<String>> iterator()
	{
		Collections.shuffle(amostras);
		return Collections.unmodifiableList(amostras).iterator();
	}
}
