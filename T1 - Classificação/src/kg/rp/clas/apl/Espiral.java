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
	private double raioInicial, raioFinal, voltas, ppv, ru�do;
	
	private final Random r;
	private final int vizinhos;
	private final M�trica m�trica;
	
	public Espiral(Argumentos args, Random r, int vizinhos, M�trica m�trica, boolean simples)
	{
		this.args = args;
		
		this.r = r;
		this.vizinhos = vizinhos;
		this.m�trica = m�trica;
		
		espiral(simples);
	}
	
	private void espiral(boolean simples)
	{
		// Valores padr�es
		raioInicial = 30;
		raioFinal = 200;
		voltas = 3;
		ppv = 50;
		ru�do = 0;
		
		List<DoubleConsumer> a��es = Arrays.asList(
				d -> raioInicial = d,
				d -> raioFinal = d,
				d -> voltas = d,
				d -> ppv = d,
				d -> ru�do = d
		);
		Iterator<DoubleConsumer> itA��es = a��es.iterator();
		while (args.tem() && itA��es.hasNext())
			itA��es.next().accept(Double.valueOf(args.pr�x()));
		
		if (simples)
			espiralSimples(
					raioInicial, raioFinal, // Raio incial e final
					voltas, ppv, // Voltas, pontos por volta
					ru�do, // Ru�do
					new KVizinhosMaisPr�ximos(vizinhos),
					m�trica
			);
		else
			espiralDupla(
					raioInicial, raioFinal, // Raio incial e final
					voltas, ppv, // Voltas, pontos por volta
					ru�do, // Ru�do
					new KVizinhosMaisPr�ximos(vizinhos),
					m�trica
			);
	}
	
	private void espiralSimples(
			double raioInicial, double raioFinal,
			// Voltas, pontos por volta
			double voltas, double ppv,
			double ru�do,
			Classificador cldr,
			M�trica m
			)
	{
		// Raio
		final double crescRaio = raioFinal - raioInicial;
		// Ru�do
		final double desvioBase = ru�do*crescRaio/voltas;
		
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
			/* */ // Gera ru�do variando o raio
			double desvio = (2 * r.nextDouble() - 1) * desvioBase;
			ponto[0] = (raio + desvio) * fx; // x
			ponto[1] = (raio + desvio) * fy; // y
			ct.adicionar(new Amostra(ponto), i);
		}
		
		// Interface
		List<Classifica��o<Integer>> pontos = new ArrayList<>();
		for (Classifica��o<Integer> p : ct)
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
			double ru�do,
			Classificador cldr,
			M�trica m
	)
	{
		// Raio
		final double crescRaio = raioFinal - raioInicial;
		// Ru�do
		final double desvioBase = ru�do*crescRaio/voltas;
		
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
			/* */ // Gera ru�do variando o raio
			double desvio = (2 * r.nextDouble() - 1) * desvioBase;
			ponto[0] = (raio + desvio) * fx; // x
			ponto[1] = (raio + desvio) * fy; // y
			ct.adicionar(new Amostra(ponto), Classes.C1);
			desvio = (2 * r.nextDouble() - 1) * desvioBase;
			ponto[0] = (-raio + desvio) * fx; // x
			ponto[1] = (-raio + desvio) * fy; // y
			ct.adicionar(new Amostra(ponto), Classes.C2);/**/
			/* * / // Gera ru�do somando valores aleat�rios a x e y
			final double px = raio*fx, py = raio*fy;
			ponto[0] = px + (2 * r.nextDouble() - 1)*desvioBase; // x
			ponto[1] = py + (2 * r.nextDouble() - 1)*desvioBase; // y
			ct.adicionar(new Amostra(ponto), Classes.C1);
			ponto[0] = -px + (2 * r.nextDouble() - 1)*desvioBase; // x
			ponto[1] = -py + (2 * r.nextDouble() - 1)*desvioBase; // y
			ct.adicionar(new Amostra(ponto), Classes.C2);/**/
		}
		
		// Interface
		List<Classifica��o<Classes>> pontos = new ArrayList<>();
		for (Classifica��o<Classes> p : ct)
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
	
	private <T> void escreverPontos(List<Classifica��o<T>> pontos)
	{
		String nomeSa�da = "espiral.csv";
		try
		{
			// Cria arquivo
			File sa�da = new File(nomeSa�da);
			FileOutputStream fos = new FileOutputStream(sa�da);
			OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
			
			// Escreve
			double[] coords = new double[2];
			for (Classifica��o<?> p : pontos)
			{
				Amostra a = p.amostra();
				coords[0] = a.atributo(0);
				coords[1] = a.atributo(1);
				LeitorCSV.escreverComV�rgulasLinha(coords, osw);
			}
			
			// Libera recursos
			osw.close();
			fos.close();
			
			// Avisa
			JOptionPane.showMessageDialog(null, String.format(
					"Arquivo \"%s\" criado",
					nomeSa�da
			));
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			JOptionPane.showMessageDialog(null, String.format(
					"N�o foi poss�vel criar o arquivo \"%s\"",
					nomeSa�da
			));
		}
	}
}
