package kg.rp.ibl;

import java.util.*;

public abstract class DescritorConceitual
{
	private List<Classifica��o<String>> corretas, incorretas;
	private Set<Classifica��o<String>> amostras;
	
	protected final Fun��oSimilaridade fSim;
	
	
	
	public DescritorConceitual(Fun��oSimilaridade fs)
	{
		corretas = new LinkedList<>();
		incorretas = new LinkedList<>();
		amostras = new HashSet<>();
		
		fSim = fs;
	}
	
	
	
	public abstract void treinar(ConjuntoTreinamento ct);
	
	public abstract String classificar(Amostra a);
	
	public Collection<Classifica��o<String>> amostras()
	{
		return Collections.unmodifiableSet(amostras);
	}
	
	public List<Classifica��o<String>> corretas()
	{
		return Collections.unmodifiableList(corretas);
	}
	
	public List<Classifica��o<String>> incorretas()
	{
		return Collections.unmodifiableList(incorretas);
	}
	
	
	
	protected void adicionar(Classifica��o<String> nova)
	{
		amostras.add(nova);
	}
	
	protected void remover(Classifica��o<String> inst)
	{
		amostras.remove(inst);
	}
	
	protected void correta(Classifica��o<String> a)
	{
		corretas.add(a);
	}
	
	protected void incorreta(Classifica��o<String> a)
	{
		incorretas.add(a);
	}
	
	protected Classifica��o<String> maisSimilar(Amostra a)
	{
		Classifica��o<String> maisSim = null;
		double simM�x = Double.NEGATIVE_INFINITY;
		
		for (Classifica��o<String> outra : amostras)
			{
				double sim = fSim.aplicar(a, outra.amostra());
				if (sim > simM�x)
				{
					maisSim = outra;
					simM�x = sim;
				}
			}
		
		return maisSim;
	}
}
