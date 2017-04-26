package kg.util.mat;

import org.la4j.matrix.dense.*;

public class Álgebra
{
	public static double[][] covariância(double[][] dados)
	{
		double[][] desvios = desvios(dados);
		
		int amostras, atributos;
		amostras = dados.length;
		atributos = dados[0].length;
		
		double[][] matriz = new double[atributos][atributos];
		
		for (int i = 0; i < atributos; i++)
			for (int j = 0; j < atributos; j++)
				for (int k = 0; k < amostras; k++)
					matriz[i][j] += desvios[k][i]*desvios[k][j];
		
		for (int i = 0; i < atributos; i++)
			for (int j = 0; j < atributos; j++)
				matriz[i][j] /= amostras - 1;
		
		//Matrix matriz = new Basic2DMatrix(desvios).transpose().multiply(new Basic2DMatrix(desvios));
		
		return matriz;
	}
	
	public static double[][] desvios(double[][] dados)
	{
		int linhas, colunas;
		linhas = dados.length;
		colunas = dados[0].length;
		
		double[] médias = média(dados);
		
		double[][] desvios = new double[linhas][colunas];
		for (int l = 0; l < linhas; l++)
			for (int c = 0; c < colunas; c++)
				desvios[l][c] = dados[l][c] - médias[c];
		
		return desvios;
	}
	
	public static double[] média(double[][] dados)
	{
		int amostras, atributos;
		amostras = dados.length;
		atributos = dados[0].length;
		
		double[] média = new double[atributos];
		
		for (double[] linha : dados)
			for (int i = 0; i < atributos; i++)
				média[i] += linha[i];
		
		for (int i = 0; i < atributos; i++)
			média[i] /= amostras;
		
		return média;
	}

	public static double[][] menosLinha(double[][] matriz, double[] vetor)
	{
		if (vazia(matriz))
			return matriz;
		
		int linhas, colunas;
		linhas = matriz.length;
		colunas = matriz[0].length;
		double[][] r = new double[linhas][colunas];
		
		for (int l = 0; l < linhas; l++)
			for (int c = 0; c < colunas; c++)
				r[l][c] = matriz[l][c] - vetor[c];
		
		return r;
	}
	

	public static double[] normalizar(double[] vetor)
	{
		if (vazio(vetor))
			return vetor;
		
		int tam = vetor.length;
		
		double min, max, dif;
		min = Double.MAX_VALUE;
		max = Double.MIN_VALUE;
		for (int i = 0; i < tam; i++)
		{
			double v = vetor[i];
			min = Math.min(min, v);
			max = Math.max(max, v);
		}
		dif = max - min;
		
		double[] r = new double[tam];
		for (int i = 0; i < tam; i++)
			r[i] = (vetor[i] - min)/dif;
		
		return r;
	}
	
	
	public static double[][] normalizar(double[][] matriz)
	{
		if (vazia(matriz))
			return matriz;
		
		int linhas, colunas;
		linhas = matriz.length;
		colunas = matriz[0].length;
		
		double min, max, dif;
		min = Double.MAX_VALUE;
		max = Double.MIN_VALUE;
		for (int l = 0; l < linhas; l++)
			for (int c = 0; c < colunas; c++)
			{
				double v = matriz[l][c];
				min = Math.min(min, v);
				max = Math.max(max, v);
			}
		dif = max - min;
		
		double[][] r = new double[linhas][colunas];
		for (int l = 0; l < linhas; l++)
			for (int c = 0; c < colunas; c++)
				r[l][c] = (matriz[l][c] - min)/dif;
		
		return r;
	}
	
	
	public static boolean vazio(double[] v)
	{
		return v.length == 0;
	}
	
	public static boolean vazia(double[][] m)
	{
		return m.length == 0 || m[0].length == 0;
	}
}
