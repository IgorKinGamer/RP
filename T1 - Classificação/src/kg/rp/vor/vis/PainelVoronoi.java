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
	private BufferedImage imgTri�ngulos, imgDiagrama, imgPontos;
	private Color corFundo;
	
	private final Collection<Ponto> pontos;
	private final Collection<Aresta> arestasTri�ngulos, arestas, arestasInfinitas;
	private final Set<Ponto> todosPontos;
	
	private boolean mostrarTriangula��o, mostrarDiagrama, mostrarPontos;
	
	private EstadoAmplia��o amplia��oAtual;
	
	public PainelVoronoi(int larg, int alt,
			Collection<Ponto> pontos, Collection<Aresta> arestasTri�ngulos,
			Collection<Aresta> arestas, Collection<Aresta> arestasInfinitas)
	{
		super(larg, alt);
		
		this.pontos = pontos;
		this.arestasTri�ngulos = arestasTri�ngulos;
		this.arestas = arestas;
		this.arestasInfinitas = arestasInfinitas;
		
		todosPontos = new HashSet<>(pontos);
		for (Aresta a : arestas)
		{
			todosPontos.add(a.p1);
			todosPontos.add(a.p2);
		}
		
		mostrarTriangula��o = mostrarDiagrama = mostrarPontos = true;
		
		amplia��oAtual = EstadoAmplia��o.PONTOS;
		calcularEscala(pontos, identity());
		
		corFundo = Color.WHITE;
		
		desenharCamadas();
	}
	
	private void desenharCamadas()
	{
		// Triangula��o
		imgTri�ngulos = criarCamadaTransparente();
		limpar(imgTri�ngulos, COR_TRANSPARENTE);
		desenharArestas(imgTri�ngulos, arestasTri�ngulos, .5, Color.GRAY);
		
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
		
		l = new JLabel("Use <+>, <-> e as setas direcionais para alterar a visualiza��o");
		l.setAlignmentX(.5f);
		painel.add(l);
		l = new JLabel("<P> para encaixar os pontos na janela");
		l.setAlignmentX(.5f);
		painel.add(l);
		l = new JLabel("<T> para encaixar o diagrama inteiro na janela");
		l.setAlignmentX(.5f);
		painel.add(l);
		l = new JLabel("Linhas cinza: triangula��o");
		l.setAlignmentX(.5f);
		painel.add(l);
		l = new JLabel("Linhas pretas: diagrama de Voronoi");
		l.setAlignmentX(.5f);
		painel.add(l);
		l = new JLabel("Linhas vermelhas: linhas infinitas do diagrama");
		l.setAlignmentX(.5f);
		painel.add(l);
		l = new JLabel("<1> para [n�o] exibir a triangula��o");
		l.setAlignmentX(.5f);
		painel.add(l);
		l = new JLabel("<2> para [n�o] exibir o diagrama");
		l.setAlignmentX(.5f);
		painel.add(l);
		l = new JLabel("<3> para [n�o] exibir os pontos");
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
							
					case VK_UP:    alterarPosi��o(0, 1, .1);
							break;
					case VK_DOWN:  alterarPosi��o(0, -1, .1);
							break;
					case VK_LEFT:  alterarPosi��o(1, 0, .1);
							break;
					case VK_RIGHT: alterarPosi��o(-1, 0, .1);
							break;
							
					case VK_T: mudou = alterarEscala(EstadoAmplia��o.TODOS_PONTOS);
							break;
					case VK_P: mudou = alterarEscala(EstadoAmplia��o.PONTOS);
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
						case VK_1: mostrarTriangula��o = !mostrarTriangula��o;
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
	
	private boolean alterarEscala(EstadoAmplia��o novo)
	{
		if (novo != EstadoAmplia��o.OUTRO && novo == amplia��oAtual)
			return false;
		
		amplia��oAtual = novo;
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
		amplia��oAtual = EstadoAmplia��o.OUTRO;
	}
	
	private void alterarPosi��o(double dx, double dy, double propor��o)
	{
		double compr = Math.max(larguraTotal, alturaTotal);
		x0 += dx*propor��o*compr;
		y0 += dy*propor��o*compr;
		amplia��oAtual = EstadoAmplia��o.OUTRO;
	}
	
	@Override
	public void paint(Graphics g)
	{
		g.setColor(corFundo);
		g.fillRect(0, 0, larguraTotal, alturaTotal);
		if (mostrarTriangula��o)
			g.drawImage(imgTri�ngulos, 0, 0, null);
		if (mostrarDiagrama)
			g.drawImage(imgDiagrama, 0, 0, null);
		if (mostrarPontos)
			g.drawImage(imgPontos, 0, 0, null);
	}
	
	private enum EstadoAmplia��o
	{
		PONTOS, TODOS_PONTOS, OUTRO
	}
}
