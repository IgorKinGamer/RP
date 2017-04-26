package kg.rp.clas.apl;

import java.awt.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.List;
import java.util.function.*;

import javax.swing.*;

import kg.rp.clas.*;
import kg.rp.clas.algo.*;
import kg.rp.clas.metr.*;
import kg.util.*;

public class Espiral
{
	private final Argumentos args;
	private double raioInicial, raioFinal, voltas, ppv, ruído;
	
	private final Random r;
	private final int vizinhos;
	private final Métrica métrica;
	
	public Espiral(Argumentos args, Random r, int vizinhos, Métrica métrica, boolean simples)
	{
		this.args = args;
		
		this.r = r;
		this.vizinhos = vizinhos;
		this.métrica = métrica;
		
		espiral(simples);
	}
	
	private void espiral(boolean simples)
	{
		// Valores padrões
		raioInicial = 30;
		raioFinal = 200;
		voltas = 3;
		ppv = 50;
		ruído = 0;
		
		List<DoubleConsumer> ações = Arrays.asList(
				d -> raioInicial = d,
				d -> raioFinal = d,
				d -> voltas = d,
				d -> ppv = d,
				d -> ruído = d
		);
		Iterator<DoubleConsumer> itAções = ações.iterator();
		while (args.tem() && itAções.hasNext())
			itAções.next().accept(Double.valueOf(args.próx()));
		
		if (simples)
			espiralSimples(
					raioInicial, raioFinal, // Raio incial e final
					voltas, ppv, // Voltas, pontos por volta
					ruído, // Ruído
					new KVizinhosMaisPróximos(vizinhos),
					métrica
			);
		else
			espiralDupla(
					raioInicial, raioFinal, // Raio incial e final
					voltas, ppv, // Voltas, pontos por volta
					ruído, // Ruído
					new KVizinhosMaisPróximos(vizinhos),
					métrica
			);
	}
	
	private void espiralSimples(
			double raioInicial, double raioFinal,
			// Voltas, pontos por volta
			double voltas, double ppv,
			double ruído,
			Classificador cldr,
			Métrica m
			)
	{
		// Raio
		final double crescRaio = raioFinal - raioInicial;
		// Ruído
		final double desvioBase = ruído*crescRaio/voltas;
		
		// Cria o conjunto de treinamento
		ConjuntoTreinamento<Integer> ct = new ConjuntoTreinamento<>(2);
		// Gera os pontos e adiciona ao conjunto
		double[] ponto = new double[2];
		for (int i = 0; i <= ppv*voltas; i++)
		{
			final double ang = 2*Math.PI * i/ppv,
					raio = raioInicial + i/(ppv*voltas) * crescRaio;
			// Fatores
			final double fx = Math.cos(ang), fy = Math.sin(ang);
			/* */ // Gera ruído variando o raio
			double desvio = (2 * r.nextDouble() - 1) * desvioBase;
			ponto[0] = (raio + desvio) * fx; // x
			ponto[1] = (raio + desvio) * fy; // y
			ct.adicionar(new Amostra(ponto), i);
		}
		
		// Interface
		List<Classificação<Integer>> pontos = new ArrayList<>();
		for (Classificação<Integer> p : ct)
			pontos.add(p);
		Function<Integer, Color> cores = i -> Color.getHSBColor(
				(float) ((i % 2 == 0 ? i : i+ppv/2)/(2*ppv)),
				1, 1
			);
		PainelDesenho<Integer> painel = new PainelDesenho<>(pontos, 480, 480,
				cores);
		
		escreverPontos(pontos);
		
		// Conjunto de teste
		for (int y = -240; y < 240; y++)
			for (int x = -240; x < 240; x++)
			{
				ponto[0] = x;
				ponto[1] = y;
				Amostra a = new Amostra(ponto);
				painel.pintar(x, y, cores.apply(cldr.classificar(ct, a, m)));
			}
		
		painel.mostrar();
	}
	
	private enum Classes
	{
		C1(Color.BLUE), C2(Color.GREEN);
		
		Color cor;
		Classes(Color c) { cor = c; }
	};
	private void espiralDupla(
			double raioInicial, double raioFinal,
			// Voltas, pontos por volta
			double voltas, double ppv,
			double ruído,
			Classificador cldr,
			Métrica m
	)
	{
		// Raio
		final double crescRaio = raioFinal - raioInicial;
		// Ruído
		final double desvioBase = ruído*crescRaio/voltas;
		
		// Cria o conjunto de treinamento
		ConjuntoTreinamento<Classes> ct = new ConjuntoTreinamento<>(2);
		// Gera os pontos e adiciona ao conjunto
		double[] ponto = new double[2];
		for (int i = 0; i <= ppv*voltas; i++)
		{
			final double ang = 2*Math.PI * i/ppv,
					raio = raioInicial + i/(ppv*voltas) * crescRaio;
			// Fatores
			final double fx = Math.cos(ang), fy = Math.sin(ang);
			/* */ // Gera ruído variando o raio
			double desvio = (2 * r.nextDouble() - 1) * desvioBase;
			ponto[0] = (raio + desvio) * fx; // x
			ponto[1] = (raio + desvio) * fy; // y
			ct.adicionar(new Amostra(ponto), Classes.C1);
			desvio = (2 * r.nextDouble() - 1) * desvioBase;
			ponto[0] = (-raio + desvio) * fx; // x
			ponto[1] = (-raio + desvio) * fy; // y
			ct.adicionar(new Amostra(ponto), Classes.C2);/**/
			/* * / // Gera ruído somando valores aleatórios a x e y
			final double px = raio*fx, py = raio*fy;
			ponto[0] = px + (2 * r.nextDouble() - 1)*desvioBase; // x
			ponto[1] = py + (2 * r.nextDouble() - 1)*desvioBase; // y
			ct.adicionar(new Amostra(ponto), Classes.C1);
			ponto[0] = -px + (2 * r.nextDouble() - 1)*desvioBase; // x
			ponto[1] = -py + (2 * r.nextDouble() - 1)*desvioBase; // y
			ct.adicionar(new Amostra(ponto), Classes.C2);/**/
		}
		
		// Interface
		List<Classificação<Classes>> pontos = new ArrayList<>();
		for (Classificação<Classes> p : ct)
			pontos.add(p);
		PainelDesenho<Classes> painel = new PainelDesenho<>(pontos, 480, 480, c -> c.cor);
		
		escreverPontos(pontos);
		
		// Conjunto de teste
		for (int y = -240; y < 240; y++)
			for (int x = -240; x < 240; x++)
			{
				ponto[0] = x;
				ponto[1] = y;
				Amostra a = new Amostra(ponto);
				painel.pintar(x, y, cldr.classificar(ct, a, m).cor);
			}
		
		painel.mostrar();
	}
	
	private <T> void escreverPontos(List<Classificação<T>> pontos)
	{
		String nomeSaída = "espiral.csv";
		try
		{
			// Cria arquivo
			File saída = new File(nomeSaída);
			FileOutputStream fos = new FileOutputStream(saída);
			OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
			
			// Escreve
			double[] coords = new double[2];
			for (Classificação<?> p : pontos)
			{
				Amostra a = p.amostra();
				coords[0] = a.atributo(0);
				coords[1] = a.atributo(1);
				LeitorCSV.escreverComVírgulasLinha(coords, osw);
			}
			
			// Libera recursos
			osw.close();
			fos.close();
			
			// Avisa
			JOptionPane.showMessageDialog(null, String.format(
					"Arquivo \"%s\" criado",
					nomeSaída
			));
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			JOptionPane.showMessageDialog(null, String.format(
					"Não foi possível criar o arquivo \"%s\"",
					nomeSaída
			));
		}
	}
}
