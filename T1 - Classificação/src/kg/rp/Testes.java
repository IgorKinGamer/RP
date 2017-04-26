package kg.rp;

import java.util.*;

import kg.rp.clas.apl.*;
import kg.rp.clas.metr.*;
import kg.util.*;

public class Testes
{
	static int vizinhos;
	static M�trica m�trica;
	static Argumentos args;
	static Random r;
	
	public static void main(String[] argumentos)
	{
		/*String[][] amostras = new LeitorCSV().lerCSV("german_credit.csv");
		for (String[] amostra : amostras)
			i(Arrays.deepToString(amostra));*/
		
		args = new Argumentos(argumentos);
		
		// Padr�es
		vizinhos = 1;
		m�trica = new Dist�nciaEuclidiana();
		r = new Random();
		
		String s = args.parg();
		while (s.charAt(0) == '-')
		{
			switch (s.charAt(1))
			{
				case 'a':
					r = new Random(1337);
					break;
				case 'v':
					vizinhos = Integer.valueOf(s.substring(2));
					break;
				case 'm':
					if (s.charAt(2) == 'h')
						m�trica = new HammingExtendido();
					else if (s.charAt(2) != 'e')
						args.uso();
					break;
			}
			s = args.parg();
		}
		
		switch (s)
		{
			case "simples":
				new Espiral(args, r, vizinhos, m�trica, true);  break;
			case "dupla":
				new Espiral(args, r, vizinhos, m�trica, false); break;
			case "dados": case "dataset":
				new Dados(args, r, vizinhos, m�trica);          break;
			case "voronoi":
				new Diagrama(args);                             break;
			default:
				args.uso();
		}
	}
	
	
	////////// UTILIDADES //////////
	
	public static void i(Object msg)
	{
		System.out.println(msg);
	}
}
