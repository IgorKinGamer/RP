package kg.rp.clas.metr;

import kg.rp.clas.*;

public class DistânciaEuclidiana implements Métrica
{
	@Override
	public double distância(Amostra a, Amostra b)
	{
		final int numAtr = a.tamanho();
		// Somatório dos quadrados das diferenças
		double soma = 0;
		for (int i = 0; i < numAtr; i++)
		{
			double dif = a.atributo(i) - b.atributo(i); 
			soma += dif*dif;
		}
		// Raiz do somatório
		return Math.sqrt(soma);
	}
	
	@Override
	public String toString()
	{
		return "Distância Euclidiana";
	}
}
