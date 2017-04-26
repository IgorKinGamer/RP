package kg.rp.mahal;

import java.awt.*;
import java.awt.image.*;

public class ImagemCinza extends BufferedImage
{
	public ImagemCinza(double[] dados, int larg)
	{
		super(larg, dados.length/larg, BufferedImage.TYPE_INT_RGB);
		
		if (dados.length % larg != 0)
			throw new RuntimeException("Dados e largura não combinam");
		
		for (int i = 0; i < dados.length; i++)
		{
			float v = (float) dados[i];
			setRGB(i % larg, i / larg, new Color(v, v, v).getRGB());
		}
	}
}
