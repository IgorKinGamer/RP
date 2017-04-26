package kg.rp.clas.metr;

import kg.rp.clas.*;

/**
 * Uma distância de Manhattan da vida.
 */
public class HammingExtendido implements Métrica
{
	@Override
	public double distância(Amostra a, Amostra b)
	{
		final int numAtr = a.tamanho();
		// Somatório das diferenças em módulo
		double dif = 0;
		for (int i = 0; i < numAtr; i++)
			dif += Math.abs(a.atributo(i) - b.atributo(i));
		return dif;
	}
	
	@Override
	public String toString()
	{
		return "Hamming Extendido";
	}
}
