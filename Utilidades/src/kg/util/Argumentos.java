package kg.util;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class Argumentos
{
	private Iterator<String> itArgs;
	
	public Argumentos(String[] args)
	{
		itArgs = Arrays.stream(args).iterator();
	}
	
	/**
	 * Pega o próximo argumento (argumento obrigatório).
	 * Se não existir, termina a aplicação.
	 * @return O próximo argumento.
	 */
	public String parg()
	{
		if (itArgs.hasNext())
			return itArgs.next();
		uso();
		return null; // Inatingível
	}
	
	/**
	 * Tenta pegar o próximo argumento. Se não houver, retorna null.
	 * @return O próximo argumento ou {@code null}, se não existir.
	 */
	public String targ()
	{
		if (itArgs.hasNext())
			return itArgs.next();
		return null;
	}
	
	/**
	 * Pega o próximo argumento, que deve ser um inteiro (argumento obrigatório).
	 * Se não existir, termina a aplicação.
	 * @return O próximo argumento.
	 */
	public int pint()
	{
		try
		{
			if (itArgs.hasNext())
				return Integer.valueOf(itArgs.next());
		}
		catch (NumberFormatException nfe) {}
		uso();
		return 0; // Inatingível
	}
	
	public boolean tem()
	{
		return itArgs.hasNext();
	}
	
	public String próx()
	{
		return itArgs.next();
	}
	
	public void uso()
	{
		try
		{
			// Imprime uso
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							new Object().getClass().getResourceAsStream("/Uso.txt"),
							StandardCharsets.UTF_8)
					);
			br.lines().forEachOrdered(s -> System.out.println(s));
			br.close();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		System.exit(0);
	}
}