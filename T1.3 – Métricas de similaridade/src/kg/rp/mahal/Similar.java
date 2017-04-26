package kg.rp.mahal;

import static kg.util.mat.�lgebra.*;

import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.function.*;

import javax.imageio.*;
import javax.swing.*;

import org.la4j.LinearAlgebra.*;
import org.la4j.matrix.dense.*;
import org.la4j.vector.dense.*;

import kg.rp.mahal.vis.*;
import kg.util.*;
import kg.util.mat.*;

public class Similar
{
	private static Argumentos args;
	
	public static void main(String[] argumentos)
	{
		args = new Argumentos(argumentos);
		
		// TODO Criar "Uso.txt"
		String nomeImagem = args.parg();
		
		// L� a imagem
		BufferedImage imagem = null;
		try
		{
			imagem = ImageIO.read(new File(nomeImagem));
			if (imagem == null)
				throw new IOException();
		}
		catch (IOException e)
		{
			String msg = String.format(
					"Ocorreu algum problema lendo o arquivo %s como uma imagem",
					nomeImagem);
			System.out.println(msg);
			System.exit(0);
		}
		
		// Mostra o painel para selecionar pontos da imagem
		InterfaceRecebedoraPontos irp = new PainelRecebedorPontos(imagem);
		Collection<Ponto> pontos = new ListadorPontosRecebidos(irp).pontos();
		i("Pontos: ", pontos);
		
		// Monta tabela de amostras
		LeitorPontosImagem lpi = new LeitorPontosImagem(imagem, pontos);
		double[][] amostras = lpi.amostras();
		i("Amostras", amostras);
		
		// Calcula covari�ncia e inversa
		double[][] covari�ncia, inversa = null;
		covari�ncia = covari�ncia(amostras);
		i("Covari�ncia", covari�ncia);
		
		try
		{
			inversa = new Basic2DMatrix(covari�ncia)
					.withInverter(InverterFactory.GAUSS_JORDAN).inverse()
					.toDenseMatrix().toArray();
		}
		catch (IllegalArgumentException iae)
		{
			mostrarErro("Imposs�vel avaliar similaridade a partir dos pontos selecionados.\n"
					+ "Tente selecionar pontos com cores mais variadas.");
		}
		
		// Aplica a cada ponto da imagem e trata o resultado e coloca numa imagem
		/// Para cada pixel, calcula a dist�ncia (a partir do vetor at� a m�dia)
		//// Subtrai a m�dia
		//// Aplica fun��o ao vetor resultante
		int larg, alt, tam;
		Raster dados;
		larg = imagem.getWidth();
		alt = imagem.getHeight();
		tam = alt*larg;
		dados = imagem.getData();
		double[][] pixels = new double[tam][3];
		for (int y = 0; y < alt; y++)
			for (int x = 0; x < larg; x++)
				dados.getPixel(x, y, pixels[y*larg + x]);
		/*i("Pixels:");
		i(pixels);*/
		
		i("M�dia", m�dia(amostras));
		double[][] desvios = menosLinha(pixels, m�dia(amostras));
		//i("Desvios", desvios);
		ToDoubleFunction<double[]> mahalanobis = new Fun��oMahalanobis(inversa);
		double[] dist�ncias = new double[tam];
		for (int i = 0; i < tam; i++)
			dist�ncias[i] = mahalanobis.applyAsDouble(desvios[i]);
		/// Normaliza as dist�ncias
		double[] norm = normalizar(dist�ncias);
		/// Desenha numa imagem como tons de cinza
		BufferedImage imagemCinza = new ImagemCinza(norm, larg);
		new PainelMostradorImagem("Similaridade com a classe selecionada", imagemCinza);
		// Faz o mesmo usando dist�ncia euclidiana
	}
	
	
	/////// ERRO ///////
	
	static void mostrarErro(String msg)
	{
		JOptionPane.showMessageDialog(null, msg);
		i(msg);
		System.exit(0);
	}
	
	
	/////// IMPRIMIR ///////
	
	static void i(Object o)
	{
		if (o.getClass().isArray())
			throw new RuntimeException("Tentando imprimir array diretamente");
		if (o instanceof Collection)
			throw new RuntimeException("Tentando imprimir cole��o diretamente");
		System.out.println(o);
	}
	
	static void i(String nome, Collection<?> c)
	{
		if (nome != null)
			System.out.println(nome + ":");
		System.out.println(c);
	}
	
	static void i(String nome, double[] v)
	{
		if (nome != null)
			System.out.println(nome + ":");
		i(new BasicVector(v));
	}
	
	static void i(String nome, double[][] m)
	{
		if (nome != null)
			System.out.println(nome + ":");
		i(new Basic2DMatrix(m));
	}
}
