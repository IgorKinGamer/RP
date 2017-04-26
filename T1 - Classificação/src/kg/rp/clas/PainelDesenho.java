package kg.rp.clas;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import java.util.function.*;

import javax.swing.*;

import kg.rp.vis.*;

@SuppressWarnings("serial")
public class PainelDesenho<C> extends Painel
{
	private static final int RAIO_PONTO = 3;
	
	private final int x0, y0;
	
	private BufferedImage img¡reas, imgPontos;
	private Color corFundo;
	
	private boolean mostrar¡reas;
	
	public PainelDesenho(Collection<ClassificaÁ„o<C>> pontos, int larg, int alt, Function<C, Color> cores)
	{
		super(larg, alt);
		
		x0 = larg/2;
		y0 = alt/2;
		
		if (cores == null)
			cores = (c -> Color.BLACK);
		
		// Fundo
		img¡reas = criarCamada();
		corFundo = Color.WHITE;
		limpar(img¡reas, corFundo);
		
		// Frente
		imgPontos = criarCamada(BufferedImage.TYPE_INT_ARGB);
		limpar(imgPontos, new Color(0, true));
		desenharPontos(imgPontos, pontos, new Ellipse2D.Double(), RAIO_PONTO, cores, true);
		
		mostrar¡reas = false;
	}
	
	@Override
	protected void montarPainel()
	{
		super.montarPainel();
		
		j.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_SPACE)
					mostrar¡reas(!mostrar¡reas);
			}
		});
		
		JLabel l = new JLabel("Pressione <EspaÁo> para trocar entre ·reas e pontos");
		l.setAlignmentX(.5f);
		painel.add(l);
	}
	
	public void mostrar¡reas(boolean mostrar)
	{
		mostrar¡reas = mostrar;
		SwingUtilities.invokeLater(() -> repaint());
	}

	public void pintar(int x, int y, Color c)
	{
		img¡reas.setRGB(x0 + x, y0 - y - 1, c.getRGB());
	}
	
	@Override
	public void paint(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, larguraTotal, alturaTotal);
		if (mostrar¡reas)
			g.drawImage(img¡reas, 0, 0, null);
		else
			g.drawImage(imgPontos, 0, 0, null);
	}
}
