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
	 * Pega o pr�ximo argumento (argumento obrigat�rio).
	 * Se n�o existir, termina a aplica��o.
	 * @return O pr�ximo argumento.
	 */
	public String parg()
	{
		if (itArgs.hasNext())
			return itArgs.next();
		uso();
		return null; // Inating�vel
	}
	
	/**
	 * Tenta pegar o pr�ximo argumento. Se n�o houver, retorna null.
	 * @return O pr�ximo argumento ou {@code null}, se n�o existir.
	 */
	public String targ()
	{
		if (itArgs.hasNext())
			return itArgs.next();
		return null;
	}
	
	/**
	 * Pega o pr�ximo argumento, que deve ser um inteiro (argumento obrigat�rio).
	 * Se n�o existir, termina a aplica��o.
	 * @return O pr�ximo argumento.
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
		return 0; // Inating�vel
	}
	
	public boolean tem()
	{
		return itArgs.hasNext();
	}
	
	public String pr�x()
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