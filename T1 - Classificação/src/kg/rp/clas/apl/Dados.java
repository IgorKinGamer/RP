package kg.rp.clas.apl;

import static kg.util.LeitorCSV.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

import javax.swing.*;

import kg.rp.clas.*;
import kg.rp.clas.algo.*;
import kg.rp.clas.metr.*;
import kg.rp.vis.*;
import kg.util.*;

public class Dados
{
	private final Argumentos args;

	private final Random r;
	private final int vizinhos;
	private final Métrica métrica;
	
	public Dados(Argumentos args, Random r, int vizinhos, Métrica métrica)
	{
		this.args = args;
		
		this.r = r;
		this.vizinhos = vizinhos;
		this.métrica = métrica;
		
		conjuntoDeDados();
	}
	
	private void conjuntoDeDados()
	{
		String arquivo;
		boolean cabeçalho = false;
		boolean primeira = true;
		
		// Trata argumentos
		arquivo = args.parg();
		
		while (args.tem())
		{
			String s = args.próx();
			switch (s)
			{
				case "c": cabeçalho = true; break;
				case "f": primeira = false; break;
			}
		}
		
		// Lê o arquivo
		LeitorCSV leitor = new LeitorCSV();
		String[][] tabela = leitor.lerCSV(arquivo);
		Stream<String[]> stream = Arrays.stream(tabela);
		
		// Pula cabeçalho (se houver)
		if (cabeçalho)
			stream = stream.skip(1);
		
		// Cria amostras
		boolean constPrimeira = primeira;
		int tam = tabela[cabeçalho ? 1 : 0].length-1;
		
		LinkedList<ClassificaçãoComOrigem> amostras = new LinkedList<>();
		List<String[]> amsTreinamento = new LinkedList<>();
		List<String[]> amsCorretas = new LinkedList<>();
		List<String[]> amsErradas = new LinkedList<>();
		stream.forEachOrdered(
				(String[] crua) ->
				{
					double[] am = new double[tam];
					String classe = null;
					Iterator<String> it = Arrays.stream(crua).iterator();
					if (constPrimeira)
						classe = it.next();
					for (int i = 0; i < tam; i++)
						am[i] = Double.valueOf(it.next());
					if (!constPrimeira)
						classe = it.next();
					
					amostras.add(new ClassificaçãoComOrigem(
							new Classificação<String>(new Amostra(am), classe),
							crua
					));
				}
		);
		
		// Separa em conjunto de treinamento e testes
		List<Classificação<String>> clTreinamento = new LinkedList<>();
		List<Classificação<String>> clCorretas = new LinkedList<>();
		List<Classificação<String>> clErradas = new LinkedList<>();
		ConjuntoTreinamento<String> ct = new ConjuntoTreinamento<>(tam);
		Collections.shuffle(amostras, r);
		int metade = amostras.size()/2;
		for (int i = 0; i < metade; i++)
		{
			ClassificaçãoComOrigem cco = amostras.removeFirst();
			ct.adicionar(cco.classif);
			clTreinamento.add(cco.classif);
			amsTreinamento.add(cco.origem);
		}
		
		// Testa
		Classificador classificador = new KVizinhosMaisPróximos(vizinhos);
		for (ClassificaçãoComOrigem cco : amostras)
		{
			Classificação<String> c = cco.classif;
			String classe = classificador.classificar(ct, c.amostra(), métrica);
			if (classe.equals(c.classe()))
			{
				clCorretas.add(cco.classif);
				amsCorretas.add(cco.origem);
			}
			else
			{
				clErradas.add(cco.classif);
				amsErradas.add(cco.origem);
			}
		}
		
		// Mostra
		String nomeSaída = "Resultados.txt";
		JOptionPane.showMessageDialog(null,
				String.format("Amostras classificadas:\n"
						+ "%d corretas\n"
						+ "%d erradas\n"
						+ "Resultados no arquivo \"%s\"",
						amsCorretas.size(), amsErradas.size(), nomeSaída
				)
		);
		if (tam == 2)
		{
			Painel painel = new PainelDados2D<String>(
					480, 480, clTreinamento, clCorretas, clErradas);
			painel.mostrar();
		}
		
		try
		{
			// Cria arquivo
			File saída = new File(nomeSaída);
			FileOutputStream fos = new FileOutputStream(saída);
			OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
			
			// Escreve
			// Cabeçalho
			osw.append(String.format("# RESULTADOS #\n\n"
					+ "Arquivo de entrada: " + arquivo                            + "\n"
					+ "\t" + ct.quantidadeAmostras() + " amostras de treinamento" + "\n"
					+ "\t" + amostras.size() + " amostras de teste"               + "\n"
					+ "Vizinhos no kNN: k = " + vizinhos                          + "\n"
					+ "Métrica: " + métrica                                       + "\n"
					+ "\n"
			));
			// Dados
			/// Treinamento
			osw.append("Conjunto de treinamento:\n");
			if (cabeçalho)
				escreverComVírgulasLinha(tabela[0], osw);
			for (String[] linha : amsTreinamento)
				escreverComVírgulasLinha(linha, osw);
			osw.append('\n');
			/// Corretas
			osw.append("Classificadas corretamente:\n");
			if (cabeçalho)
				escreverComVírgulasLinha(tabela[0], osw);
			for (String[] linha : amsCorretas)
				escreverComVírgulasLinha(linha, osw);
			osw.append('\n');
			/// Erradas
			osw.append("Classificadas erroneamente:\n");
			if (cabeçalho)
				escreverComVírgulasLinha(tabela[0], osw);
			for (String[] linha : amsErradas)
				escreverComVírgulasLinha(linha, osw);
			
			// Libera recursos
			osw.close();
			fos.close();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			JOptionPane.showMessageDialog(null, String.format(
					"Não foi possível criar o arquivo \"%s\" com os resultados",
					nomeSaída
			));
		}
	}
}

// NOTA: Colocando dentro do método acima causa erro (em tempo de execução)
class ClassificaçãoComOrigem
{
	final Classificação<String> classif;
	final String[] origem;
	ClassificaçãoComOrigem(Classificação<String> classif, String[] origem)
	{
		this.classif = classif;
		this.origem = origem;
	}
}
