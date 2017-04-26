package kg.rp.ibl;

import java.util.*;

public class ConjuntoTreinamento implements Iterable<Classificação<String>>
{
	private final List<Classificação<String>> amostras;
	
	public ConjuntoTreinamento(Collection<Classificação<String>> as)
	{
		amostras = new ArrayList<>(as);
	}
	
	@Override
	public Iterator<Classificação<String>> iterator()
	{
		Collections.shuffle(amostras);
		return Collections.unmodifiableList(amostras).iterator();
	}
}
