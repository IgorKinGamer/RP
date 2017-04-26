package kg.rp.mahal;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.util.List;

public class LeitorPontosImagem
{
	private BufferedImage imagem;
	private Collection<Ponto> pontos;
	
	public LeitorPontosImagem(BufferedImage imagem, Collection<Ponto> pontos)
	{
		this.imagem = imagem;
		this.pontos = pontos;
	}
	
	public double[][] amostras()
	{
		List<double[]> l = new ArrayList<>(pontos.size());
		
		for (Ponto p : pontos)
		{
			Color cor = new Color(imagem.getRGB(p.x, p.y));
			l.add(new double[] {cor.getRed(), cor.getGreen(), cor.getBlue()});
		}
		
		return l.toArray(new double[pontos.size()][]);
	}
}
