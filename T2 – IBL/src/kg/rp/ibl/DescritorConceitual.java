package kg.rp.ibl;

import java.util.*;

public abstract class DescritorConceitual
{
	private List<Classificação<String>> corretas, incorretas;
	private Set<Classificação<String>> amostras;
	
	protected final FunçãoSimilaridade fSim;
	
	
	
	public DescritorConceitual(FunçãoSimilaridade fs)
	{
		corretas = new LinkedList<>();
		incorretas = new LinkedList<>();
		amostras = new HashSet<>();
		
		fSim = fs;
	}
	
	
	
	public abstract void treinar(ConjuntoTreinamento ct);
	
	public abstract String classificar(Amostra a);
	
	public Collection<Classificação<String>> amostras()
	{
		return Collections.unmodifiableSet(amostras);
	}
	
	public List<Classificação<String>> corretas()
	{
		return Collections.unmodifiableList(corretas);
	}
	
	public List<Classificação<String>> incorretas()
	{
		return Collections.unmodifiableList(incorretas);
	}
	
	
	
	protected void adicionar(Classificação<String> nova)
	{
		amostras.add(nova);
	}
	
	protected void remover(Classificação<String> inst)
	{
		amostras.remove(inst);
	}
	
	protected void correta(Classificação<String> a)
	{
		corretas.add(a);
	}
	
	protected void incorreta(Classificação<String> a)
	{
		incorretas.add(a);
	}
	
	protected Classificação<String> maisSimilar(Amostra a)
	{
		Classificação<String> maisSim = null;
		double simMáx = Double.NEGATIVE_INFINITY;
		
		for (Classificação<String> outra : amostras)
			{
				double sim = fSim.aplicar(a, outra.amostra());
				if (sim > simMáx)
				{
					maisSim = outra;
					simMáx = sim;
				}
			}
		
		return maisSim;
	}
}
