package kg.rp.clas.algo;

import kg.rp.clas.*;
import kg.rp.clas.metr.*;

@FunctionalInterface
public interface Classificador
{
	/**
	 * Classifica uma amostra com base em um <i>conjunto de treinamento</i>,
	 * i.e., um conjunto de classificações, que são amostras já classificadas.
	 * 
	 * @param conjTreinamento Conjunto de amostras já classificadas usado para
	 * determinar a classe de {@code amostra}
	 * @param amostra Amostra a ser classificada
	 * @return A classe em que a amostra melhor se encaixa segundo o julgamento
	 * deste classificador com base no conjunto de treinamento
	 */
	<C> C classificar(ConjuntoTreinamento<C> conjTreinamento,
			Amostra amostra, Métrica métrica);
	
	/**
	 * 
	 * @param conjTreinamento Conjunto de amostras já classificadas usado para
	 * determinar a classe de {@code amostra}
	 * @param amostra Amostra a ser classificada
	 * @return
	 */
	default <C> Classificação<C> pegarClassificação(
			ConjuntoTreinamento<C> conjTreinamento,
			Amostra amostra, Métrica métrica)
	{
		return new Classificação<C>(amostra,
				classificar(conjTreinamento, amostra, métrica));
	}
}
