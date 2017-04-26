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
	private final M�trica m�trica;
	
	public Dados(Argumentos args, Random r, int vizinhos, M�trica m�trica)
	{
		this.args = args;
		
		this.r = r;
		this.vizinhos = vizinhos;
		this.m�trica = m�trica;
		
		conjuntoDeDados();
	}
	
	private void conjuntoDeDados()
	{
		String arquivo;
		boolean cabe�alho = false;
		boolean primeira = true;
		
		// Trata argumentos
		arquivo = args.parg();
		
		while (args.tem())
		{
			String s = args.pr�x();
			switch (s)
			{
				case "c": cabe�alho = true; break;
				case "f": primeira = false; break;
			}
		}
		
		// L� o arquivo
		LeitorCSV leitor = new LeitorCSV();
		String[][] tabela = leitor.lerCSV(arquivo);
		Stream<String[]> stream = Arrays.stream(tabela);
		
		// Pula cabe�alho (se houver)
		if (cabe�alho)
			stream = stream.skip(1);
		
		// Cria amostras
		boolean constPrimeira = primeira;
		int tam = tabela[cabe�alho ? 1 : 0].length-1;
		
		LinkedList<Classifica��oComOrigem> amostras = new LinkedList<>();
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
					
					amostras.add(new Classifica��oComOrigem(
							new Classifica��o<String>(new Amostra(am), classe),
							crua
					));
				}
		);
		
		// Separa em conjunto de treinamento e testes
		List<Classifica��o<String>> clTreinamento = new LinkedList<>();
		List<Classifica��o<String>> clCorretas = new LinkedList<>();
		List<Classifica��o<String>> clErradas = new LinkedList<>();
		ConjuntoTreinamento<String> ct = new ConjuntoTreinamento<>(tam);
		Collections.shuffle(amostras, r);
		int metade = amostras.size()/2;
		for (int i = 0; i < metade; i++)
		{
			Classifica��oComOrigem cco = amostras.removeFirst();
			ct.adicionar(cco.classif);
			clTreinamento.add(cco.classif);
			amsTreinamento.add(cco.origem);
		}
		
		// Testa
		Classificador classificador = new KVizinhosMaisPr�ximos(vizinhos);
		for (Classifica��oComOrigem cco : amostras)
		{
			Classifica��o<String> c = cco.classif;
			String classe = classificador.classificar(ct, c.amostra(), m�trica);
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
		String nomeSa�da = "Resultados.txt";
		JOptionPane.showMessageDialog(null,
				String.format("Amostras classificadas:\n"
						+ "%d corretas\n"
						+ "%d erradas\n"
						+ "Resultados no arquivo \"%s\"",
						amsCorretas.size(), amsErradas.size(), nomeSa�da
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
			File sa�da = new File(nomeSa�da);
			FileOutputStream fos = new FileOutputStream(sa�da);
			OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
			
			// Escreve
			// Cabe�alho
			osw.append(String.format("# RESULTADOS #\n\n"
					+ "Arquivo de entrada: " + arquivo                            + "\n"
					+ "\t" + ct.quantidadeAmostras() + " amostras de treinamento" + "\n"
					+ "\t" + amostras.size() + " amostras de teste"               + "\n"
					+ "Vizinhos no kNN: k = " + vizinhos                          + "\n"
					+ "M�trica: " + m�trica                                       + "\n"
					+ "\n"
			));
			// Dados
			/// Treinamento
			osw.append("Conjunto de treinamento:\n");
			if (cabe�alho)
				escreverComV�rgulasLinha(tabela[0], osw);
			for (String[] linha : amsTreinamento)
				escreverComV�rgulasLinha(linha, osw);
			osw.append('\n');
			/// Corretas
			osw.append("Classificadas corretamente:\n");
			if (cabe�alho)
				escreverComV�rgulasLinha(tabela[0], osw);
			for (String[] linha : amsCorretas)
				escreverComV�rgulasLinha(linha, osw);
			osw.append('\n');
			/// Erradas
			osw.append("Classificadas erroneamente:\n");
			if (cabe�alho)
				escreverComV�rgulasLinha(tabela[0], osw);
			for (String[] linha : amsErradas)
				escreverComV�rgulasLinha(linha, osw);
			
			// Libera recursos
			osw.close();
			fos.close();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			JOptionPane.showMessageDialog(null, String.format(
					"N�o foi poss�vel criar o arquivo \"%s\" com os resultados",
					nomeSa�da
			));
		}
	}
}

// NOTA: Colocando dentro do m�todo acima causa erro (em tempo de execu��o)
class Classifica��oComOrigem
{
	final Classifica��o<String> classif;
	final String[] origem;
	Classifica��oComOrigem(Classifica��o<String> classif, String[] origem)
	{
		this.classif = classif;
		this.origem = origem;
	}
}
