package kg.util;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class LeitorCSV
{
	private Charset codificação;
	
	public LeitorCSV()
	{
		codificação = StandardCharsets.UTF_8;
	}
	
	public String[][] lerCSV(String arquivo)
	{
		return lerCSV(new File(arquivo));
	}
	
	public String[][] lerCSV(File arquivo)
	{
		try
		{
			// Abre o arquivo
			InputStreamReader isr = new InputStreamReader(new FileInputStream(arquivo), codificação);
			BufferedReader br = new BufferedReader(isr);
			
			// Transforma cada linha em um String[]
			List<String[]> linhas = new LinkedList<>();
			br.lines().forEachOrdered(
					strLinha ->
					{
						List<String> linha = new LinkedList<>();
						StringBuilder s = new StringBuilder();
						strLinha.chars().forEachOrdered(
								c ->
								{
									if (c == ',')
									{
										linha.add(s.toString());
										s.setLength(0);
									}
									else
										s.append((char)c);
								}
								);
						linha.add(s.toString());
						linhas.add(linha.toArray(new String[linha.size()]));
					}
					);
			br.close();
			return linhas.toArray(new String[linhas.size()][]);
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			return null;
		}
	}
	
	
	public static <T> void escreverComVírgulasLinha(T[] dados, OutputStreamWriter dest) throws IOException
	{
		dest.append(dados[0].toString());
		for (int i = 1; i < dados.length; i++)
			dest.append(',').append(dados[i].toString());
		dest.append('\n');
	}
	
	public static <T> void escreverComVírgulas(double[] dados, OutputStreamWriter dest) throws IOException
	{
		dest.append(Double.toString(dados[0]));
		for (int i = 1; i < dados.length; i++)
			dest.append(',').append(Double.toString(dados[i]));
	}
	
	public static <T> void escreverComVírgulasLinha(double[] dados, OutputStreamWriter dest) throws IOException
	{
		dest.append(Double.toString(dados[0]));
		for (int i = 1; i < dados.length; i++)
			dest.append(',').append(Double.toString(dados[i]));
		dest.append('\n');
	}
	
}
