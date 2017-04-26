package kg.rp.mahal;

import static kg.util.mat.Álgebra.*;

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
		
		// Lê a imagem
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
		
		// Calcula covariância e inversa
		double[][] covariância, inversa = null;
		covariância = covariância(amostras);
		i("Covariância", covariância);
		
		try
		{
			inversa = new Basic2DMatrix(covariância)
					.withInverter(InverterFactory.GAUSS_JORDAN).inverse()
					.toDenseMatrix().toArray();
		}
		catch (IllegalArgumentException iae)
		{
			mostrarErro("Impossível avaliar similaridade a partir dos pontos selecionados.\n"
					+ "Tente selecionar pontos com cores mais variadas.");
		}
		
		// Aplica a cada ponto da imagem e trata o resultado e coloca numa imagem
		/// Para cada pixel, calcula a distância (a partir do vetor até a média)
		//// Subtrai a média
		//// Aplica função ao vetor resultante
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
		
		i("Média", média(amostras));
		double[][] desvios = menosLinha(pixels, média(amostras));
		//i("Desvios", desvios);
		ToDoubleFunction<double[]> mahalanobis = new FunçãoMahalanobis(inversa);
		double[] distâncias = new double[tam];
		for (int i = 0; i < tam; i++)
			distâncias[i] = mahalanobis.applyAsDouble(desvios[i]);
		/// Normaliza as distâncias
		double[] norm = normalizar(distâncias);
		/// Desenha numa imagem como tons de cinza
		BufferedImage imagemCinza = new ImagemCinza(norm, larg);
		new PainelMostradorImagem("Similaridade com a classe selecionada", imagemCinza);
		// Faz o mesmo usando distância euclidiana
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
			throw new RuntimeException("Tentando imprimir coleção diretamente");
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
