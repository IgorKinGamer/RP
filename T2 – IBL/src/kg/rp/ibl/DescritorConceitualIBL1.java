package kg.rp.ibl;

import java.util.*;

public class DescritorConceitualIBL1 extends DescritorConceitual
{
	public DescritorConceitualIBL1(int atrs)
	{
		super(new Dist�nciaEuclidiana(atrs));
	}
	
	@Override
	public void treinar(ConjuntoTreinamento ct)
	{
		Iterator<Classifica��o<String>> it = ct.iterator();
		
		// Coloca uma primeira amostra
		Classifica��o<String> inicial = it.next();
		fSim.atualizarNormaliza��o(inicial.amostra());
		adicionar(inicial);
		// Processa as demais
		while (it.hasNext())
		{
			Classifica��o<String> a, similar;
			a = it.next();
			fSim.atualizarNormaliza��o(a.amostra());
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
