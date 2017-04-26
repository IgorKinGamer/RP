package kg.rp.ibl;

import java.util.*;
import java.util.stream.*;

public class LeitorAmostras
{
	public List<Classifica��o<String>> lerAmostrasClassificadas(String[][] linhas, int tamCerto)
	{
		List<Classifica��o<String>> amostras = new ArrayList<>(linhas.length);
		try
		{
			Stream<String[]> stream = Arrays.stream(linhas).skip(1);
			stream.forEach(linha ->
			{
				int tam = linha.length;
				if (tam != tamCerto)
					throw new IllegalArgumentException("Amostra com tamanho errado");
				
				double[] valores = new double[tam-1];
				for (int i = 0; i < tam-1; i++)
					valores[i] = Double.valueOf(linha[i]);
				
				amostras.add(new Classifica��o<String>(new Amostra(valores), linha[tam-1]));
			});
		}
		catch (NumberFormatException nfe)
		{
			throw new IllegalArgumentException("N�meros iv�lido");
		}
		return amostras;
	}
	
	public List<Amostra> lerAmostrasDeTamanho(String[][] linhas, int tamM�nimo)
	{
		List<Amostra> amostras = new ArrayList<>(linhas.length);
		try
		{
			Stream<String[]> stream = Arrays.stream(linhas).skip(1);
			stream.forEach(linha ->
			{
				int tam = linha.length;
				if (tam < tamM�nimo)
					throw new IllegalArgumentException("Amostra com tamanho errado");
				
				double[] valores = new double[tam-1];
				for (int i = 0; i < tam-1; i++)
					valores[i] = Double.valueOf(linha[i]);
				
				amostras.add(new Amostra(valores));
			});
		}
		catch (NumberFormatException nfe)
		{
			throw new IllegalArgumentException("N�meros iv�lido");
		}
		return amostras;
	}
}
