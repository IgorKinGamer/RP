package kg.rp.clas.algo;

import kg.rp.clas.*;
import kg.rp.clas.metr.*;

@FunctionalInterface
public interface Classificador
{
	/**
	 * Classifica uma amostra com base em um <i>conjunto de treinamento</i>,
	 * i.e., um conjunto de classifica��es, que s�o amostras j� classificadas.
	 * 
	 * @param conjTreinamento Conjunto de amostras j� classificadas usado para
	 * determinar a classe de {@code amostra}
	 * @param amostra Amostra a ser classificada
	 * @return A classe em que a amostra melhor se encaixa segundo o julgamento
	 * deste classificador com base no conjunto de treinamento
	 */
	<C> C classificar(ConjuntoTreinamento<C> conjTreinamento,
			Amostra amostra, M�trica m�trica);
	
	/**
	 * 
	 * @param conjTreinamento Conjunto de amostras j� classificadas usado para
	 * determinar a classe de {@code amostra}
	 * @param amostra Amostra a ser classificada
	 * @return
	 */
	default <C> Classifica��o<C> pegarClassifica��o(
			ConjuntoTreinamento<C> conjTreinamento,
			Amostra amostra, M�trica m�trica)
	{
		return new Classifica��o<C>(amostra,
				classificar(conjTreinamento, amostra, m�trica));
	}
}
