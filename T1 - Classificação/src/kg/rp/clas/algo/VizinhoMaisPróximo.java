package kg.rp.clas.algo;

import kg.rp.clas.*;
import kg.rp.clas.metr.*;

public class VizinhoMaisPr�ximo implements Classificador
{
	private static final String MSG_ERRO_CONJUNTO_VAZIO =
			"Conjunto de treinamento vazio";
	
	@Override
	public <C> C classificar(ConjuntoTreinamento<C> conjTreinamento,
			Amostra amostra, M�trica m�trica)
	{
		if (conjTreinamento.quantidadeAmostras() == 0)
			throw new IllegalStateException(MSG_ERRO_CONJUNTO_VAZIO);
		
		double menorDif = Double.POSITIVE_INFINITY;
		C classe = null;
		for (Classifica��o<C> cl : conjTreinamento)
		{
			Amostra outra = cl.amostra();
			double dif = m�trica.dist�ncia(amostra, outra);
			if (dif < menorDif)
			{
				menorDif = dif;
				classe = cl.classe();
			}
		}
		return classe;
	}

}
