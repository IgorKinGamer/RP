package kg.rp.clas.metr;

import kg.rp.clas.*;

public class Dist�nciaEuclidiana implements M�trica
{
	@Override
	public double dist�ncia(Amostra a, Amostra b)
	{
		final int numAtr = a.tamanho();
		// Somat�rio dos quadrados das diferen�as
		double soma = 0;
		for (int i = 0; i < numAtr; i++)
		{
			double dif = a.atributo(i) - b.atributo(i); 
			soma += dif*dif;
		}
		// Raiz do somat�rio
		return Math.sqrt(soma);
	}
	
	@Override
	public String toString()
	{
		return "Dist�ncia Euclidiana";
	}
}
