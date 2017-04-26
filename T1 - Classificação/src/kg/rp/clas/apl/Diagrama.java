package kg.rp.clas.apl;

import java.util.*;

import kg.rp.vor.*;
import kg.util.*;
import kg.util.geom.*;

public class Diagrama
{
	private Argumentos args;
	
	private int largura, altura;
	
	public Diagrama(Argumentos args)
	{
		this.args = args;
		
		largura = altura = 480;
		
		diagrama();
	}
	
	private void diagrama()
	{
		String arquivo;
		
		arquivo = args.parg();
		
		// Lê o arquivo
		LeitorCSV leitor = new LeitorCSV();
		String[][] tabela = leitor.lerCSV(arquivo);
		
		Set<Ponto> pontos = new HashSet<>();
		for (String[] linha : tabela)
		{
			double x, y;
			x = Double.valueOf(linha[0]);
			y = Double.valueOf(linha[1]);
			pontos.add(new Ponto(x, y));
		}
		
		// Processa
		DiagramaVoronoi dv = new DiagramaVoronoi(pontos);
		
		// Mostra
		dv.mostrar(largura, altura);
	}
}
