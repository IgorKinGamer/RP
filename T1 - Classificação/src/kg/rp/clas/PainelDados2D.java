package kg.rp.clas;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import kg.rp.util.*;
import kg.rp.vis.*;

@SuppressWarnings("serial")
public class PainelDados2D<C> extends Painel
{
	private BufferedImage img;
	private final Set<C> classes;
	private Map<C, Color> coresClasses;
	
	public PainelDados2D(
			int larg, int alt, List<Classificação<C>> treino,
			List<Classificação<C>> corretos, List<Classificação<C>> errados)
	{
		super(larg, alt);
		
		int tam = treino.size() + corretos.size() + errados.size();
		List<Classificação<C>> pontos = new ArrayList<>(tam);
		pontos.addAll(treino);
		pontos.addAll(corretos);
		pontos.addAll(errados);
		calcularEscala(pontos, Utilidades.classificaçãoParaPonto());
		
		classes = new HashSet<>();
		for (Classificação<C> c : pontos)
			classes.add(c.classe());
		
		coresClasses = new HashMap<>();
		int numClasses = classes.size(), i = 0;
		for (C classe : classes)
			coresClasses.put(classe,
					Color.getHSBColor((float)i++/numClasses, 1, 1)
			);
		
		img = criarCamada();
		limpar(img, Color.WHITE);
		
		RectangularShape formaTreino = new Ellipse2D.Double();
		RectangularShape formaTeste = new Rectangle2D.Double();
		desenharPontos(img, treino, formaTreino, 3, classe -> coresClasses.get(classe), true);
		desenharPontos(img, corretos, formaTeste, 4, classe -> coresClasses.get(classe), true);
		desenharPontos(img, errados, formaTeste, 4, classe -> coresClasses.get(classe), false);
		
	}
	
	@Override
	protected void montarPainel()
	{
		super.montarPainel();
		
		JLabel l;
		for (String s : new String[]
				{
					"Círculos: Conjunto de treinamento",
					"Quadrados: Conjunto de teste",
					"- Preenchidos: classificados corretamente",
					"- Vazios: classificados erroneamente",
					"Cores:"
				})
		{
			l = new JLabel(s);
			l.setAlignmentX(.5f);
			painel.add(l);
		}
		for (C classe : classes)
		{
			l = new JLabel(classe.toString());
			l.setAlignmentX(.5f);
			l.setOpaque(true);
			Color cor = coresClasses.get(classe);
			l.setBackground(cor);
			l.setForeground( (299 * cor.getRed() + 587 * cor.getGreen() + 114 * cor.getBlue()) / (255*1000d) > .5 ?
					Color.BLACK : Color.WHITE);
			painel.add(l);
		}
	}
	
	@Override
	public void paint(Graphics g)
	{
		g.drawImage(img, 0, 0, null);
	}
}
