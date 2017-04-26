package kg.rp.vor.vis;

import static java.awt.event.KeyEvent.*;
import static java.util.function.Function.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

import javax.swing.*;

import kg.rp.vis.*;
import kg.rp.vor.*;
import kg.util.geom.*;
import kg.util.swing.*;

@SuppressWarnings("serial")
public class PainelVoronoi extends PainelBase
{
	private BufferedImage imgTriângulos, imgDiagrama, imgPontos;
	private Color corFundo;
	
	private final Collection<Ponto> pontos;
	private final Collection<Aresta> arestasTriângulos, arestas, arestasInfinitas;
	private final Set<Ponto> todosPontos;
	
	private boolean mostrarTriangulação, mostrarDiagrama, mostrarPontos;
	
	private EstadoAmpliação ampliaçãoAtual;
	
	public PainelVoronoi(int larg, int alt,
			Collection<Ponto> pontos, Collection<Aresta> arestasTriângulos,
			Collection<Aresta> arestas, Collection<Aresta> arestasInfinitas)
	{
		super(larg, alt);
		
		this.pontos = pontos;
		this.arestasTriângulos = arestasTriângulos;
		this.arestas = arestas;
		this.arestasInfinitas = arestasInfinitas;
		
		todosPontos = new HashSet<>(pontos);
		for (Aresta a : arestas)
		{
			todosPontos.add(a.p1);
			todosPontos.add(a.p2);
		}
		
		mostrarTriangulação = mostrarDiagrama = mostrarPontos = true;
		
		ampliaçãoAtual = EstadoAmpliação.PONTOS;
		calcularEscala(pontos, identity());
		
		corFundo = Color.WHITE;
		
		desenharCamadas();
	}
	
	private void desenharCamadas()
	{
		// Triangulação
		imgTriângulos = criarCamadaTransparente();
		limpar(imgTriângulos, COR_TRANSPARENTE);
		desenharArestas(imgTriângulos, arestasTriângulos, .5, Color.GRAY);
		
		// Diagrama
		imgDiagrama = criarCamadaTransparente();
		limpar(imgDiagrama, COR_TRANSPARENTE);
		desenharArestas(imgDiagrama, arestas, 1, Color.BLACK);
		desenharArestas(imgDiagrama, arestasInfinitas,
				a -> new Aresta(a.p1, a.p1.mais(a.v.comTamanho((larguraTotal+alturaTotal)/escala))),
				1, Color.RED);
		
		// Pontos
		imgPontos = criarCamadaTransparente();
		limpar(imgPontos, COR_TRANSPARENTE);
		desenharPontos(imgPontos, pontos, identity(),
				new Ellipse2D.Double(), 3, p -> Color.BLACK, true);
	}
	
	private void redesenharCamadas()
	{
		desenharCamadas();
		
		SwingUtilities.invokeLater(this::repaint);
	}
	
	@Override
	protected void montarPainel()
	{
		super.montarPainel();
		
		JLabel l;
		
		l = new JLabel("Use <+>, <-> e as setas direcionais para alterar a visualização");
		l.setAlignmentX(.5f);
		painel.add(l);
		l = new JLabel("<P> para encaixar os pontos na janela");
		l.setAlignmentX(.5f);
		painel.add(l);
		l = new JLabel("<T> para encaixar o diagrama inteiro na janela");
		l.setAlignmentX(.5f);
		painel.add(l);
		l = new JLabel("Linhas cinza: triangulação");
		l.setAlignmentX(.5f);
		painel.add(l);
		l = new JLabel("Linhas pretas: diagrama de Voronoi");
		l.setAlignmentX(.5f);
		painel.add(l);
		l = new JLabel("Linhas vermelhas: linhas infinitas do diagrama");
		l.setAlignmentX(.5f);
		painel.add(l);
		l = new JLabel("<1> para [não] exibir a triangulação");
		l.setAlignmentX(.5f);
		painel.add(l);
		l = new JLabel("<2> para [não] exibir o diagrama");
		l.setAlignmentX(.5f);
		painel.add(l);
		l = new JLabel("<3> para [não] exibir os pontos");
		l.setAlignmentX(.5f);
		painel.add(l);
		
		j.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				boolean mudou = true;
				switch (e.getKeyCode())
				{
					case VK_ADD:      alterarEscala(2);
							break;
					case VK_SUBTRACT: alterarEscala(.5);
							break;
							
					case VK_UP:    alterarPosição(0, 1, .1);
							break;
					case VK_DOWN:  alterarPosição(0, -1, .1);
							break;
					case VK_LEFT:  alterarPosição(1, 0, .1);
							break;
					case VK_RIGHT: alterarPosição(-1, 0, .1);
							break;
							
					case VK_T: mudou = alterarEscala(EstadoAmpliação.TODOS_PONTOS);
							break;
					case VK_P: mudou = alterarEscala(EstadoAmpliação.PONTOS);
							break;
					
					default: mudou = false;
				}
				if (mudou)
					redesenharCamadas();
				else
				{
					mudou = true;
					switch (e.getKeyCode())
					{
						case VK_1: mostrarTriangulação = !mostrarTriangulação;
								break;
						case VK_2: mostrarDiagrama = !mostrarDiagrama;
								break;
						case VK_3: mostrarPontos = !mostrarPontos;
								break;
						
						default: mudou = false;
					}
					if (mudou)
						repaint();
				}
			}
		});
	}
	
	private boolean alterarEscala(EstadoAmpliação novo)
	{
		if (novo != EstadoAmpliação.OUTRO && novo == ampliaçãoAtual)
			return false;
		
		ampliaçãoAtual = novo;
		switch (novo)
		{
			case PONTOS:
				calcularEscala(pontos, identity());
				break;
			case TODOS_PONTOS:
				calcularEscala(todosPontos, identity());
				break;
			default:
		}
		return true;
	}
	
	private void alterarEscala(double fator)
	{
		escala *= fator;
		x0 = (x0 - larguraTotal/2)*fator + larguraTotal/2;
		y0 = (y0 - alturaTotal/2)*fator + alturaTotal/2;
		ampliaçãoAtual = EstadoAmpliação.OUTRO;
	}
	
	private void alterarPosição(double dx, double dy, double proporção)
	{
		double compr = Math.max(larguraTotal, alturaTotal);
		x0 += dx*proporção*compr;
		y0 += dy*proporção*compr;
		ampliaçãoAtual = EstadoAmpliação.OUTRO;
	}
	
	@Override
	public void paint(Graphics g)
	{
		g.setColor(corFundo);
		g.fillRect(0, 0, larguraTotal, alturaTotal);
		if (mostrarTriangulação)
			g.drawImage(imgTriângulos, 0, 0, null);
		if (mostrarDiagrama)
			g.drawImage(imgDiagrama, 0, 0, null);
		if (mostrarPontos)
			g.drawImage(imgPontos, 0, 0, null);
	}
	
	private enum EstadoAmpliação
	{
		PONTOS, TODOS_PONTOS, OUTRO
	}
}
