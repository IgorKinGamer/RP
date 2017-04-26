package kg.rp.ibl;

import java.util.*;

public class DescritorConceitualIBL1 extends DescritorConceitual
{
	public DescritorConceitualIBL1(int atrs)
	{
		super(new DistânciaEuclidiana(atrs));
	}
	
	@Override
	public void treinar(ConjuntoTreinamento ct)
	{
		Iterator<Classificação<String>> it = ct.iterator();
		
		// Coloca uma primeira amostra
		Classificação<String> inicial = it.next();
		fSim.atualizarNormalização(inicial.amostra());
		adicionar(inicial);
		// Processa as demais
		while (it.hasNext())
		{
			Classificação<String> a, similar;
			a = it.next();
			fSim.atualizarNormalização(a.amostra());
			similar = maisSimilar(a.amostra());
			
			if (a.classe().equals(similar.classe()))
				correta(a);
			else
				incorreta(a);
			
			adicionar(a);
		}
	}
	
	@Override
	public String classificar(Amostra a)
	{
		return maisSimilar(a).classe();
	}
	
	@Override
	public String toString()
	{
		return "IBL1";
	}
}
