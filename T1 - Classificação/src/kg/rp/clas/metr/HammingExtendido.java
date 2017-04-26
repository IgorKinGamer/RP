package kg.rp.clas.metr;

import kg.rp.clas.*;

/**
 * Uma dist�ncia de Manhattan da vida.
 */
public class HammingExtendido implements M�trica
{
	@Override
	public double dist�ncia(Amostra a, Amostra b)
	{
		final int numAtr = a.tamanho();
		// Somat�rio das diferen�as em m�dulo
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
