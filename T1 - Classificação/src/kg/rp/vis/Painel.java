package kg.rp.vis;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import java.util.function.*;

import kg.rp.clas.*;
import kg.rp.util.*;
import kg.util.swing.*;

@SuppressWarnings("serial")
public class Painel extends PainelBase
{
	public Painel(int larg, int alt)
	{
		super(larg, alt);
	}
	
	public Painel(int larg, int alt, Collection<Classifica��o<?>> pontos)
	{
		super(larg, alt, pontos, Utilidades.CLASSIFICA��O_PARA_PONTO);
	}
	
	protected <C> void desenharPontos(
			BufferedImage img,
			Collection<Classifica��o<C>> pontos,
			RectangularShape forma, double raio,
			Function<C, Color> cores, boolean preencher)
	{
		super.desenharPontos(img, pontos, Utilidades.classifica��oParaPonto(),
				forma, raio, c -> cores.apply(c.classe()), preencher);
	}
}
