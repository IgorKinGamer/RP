package kg.rp.clas.algo;

import kg.rp.clas.*;
import kg.rp.clas.metr.*;

public class VizinhoMaisPróximo implements Classificador
{
	private static final String MSG_ERRO_CONJUNTO_VAZIO =
			"Conjunto de treinamento vazio";
	
	@Override
	public <C> C classificar(ConjuntoTreinamento<C> conjTreinamento,
			Amostra amostra, Métrica métrica)
	{
		if (conjTreinamento.quantidadeAmostras() == 0)
			throw new IllegalStateException(MSG_ERRO_CONJUNTO_VAZIO);
		
		double menorDif = Double.POSITIVE_INFINITY;
		C classe = null;
		for (Classificação<C> cl : conjTreinamento)
		{
			Amostra outra = cl.amostra();
			double dif = métrica.distância(amostra, outra);
			if (dif < menorDif)
			{
				menorDif = dif;
				classe = cl.classe();
			}
		}
		return classe;
	}

}
