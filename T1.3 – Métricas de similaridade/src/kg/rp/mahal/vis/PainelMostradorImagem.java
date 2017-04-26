package kg.rp.mahal.vis;

import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

public class PainelMostradorImagem
{
	private final JPanel painel;
	
	@SuppressWarnings("serial")
	public PainelMostradorImagem(String título, BufferedImage imagem)
	{
		painel = new JPanel()
		{
			{ setPreferredSize(new Dimension(imagem.getWidth(), imagem.getHeight())); }
			
			public void paint(Graphics g)
			{
				g.drawImage(imagem, 0, 0, null);
			}
		};
		
		JFrame janela = new JFrame(título);
		janela.add(painel);
		janela.pack();
		janela.setVisible(true);
	}
}
