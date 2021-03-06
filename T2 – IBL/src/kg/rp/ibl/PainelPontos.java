package kg.rp.ibl;

import static javax.swing.SwingConstants.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import kg.rp.ibl.util.*;
import kg.util.swing.*;

@SuppressWarnings("serial")
public class PainelPontos extends PainelBase
{
	private List<ParâmetrosDesenho> conjs;
	private Collection<Classificação<String>> todosPontos;
	private Map<String, Color> cores;
	
	private List<String> descrições;
	
	private BufferedImage imgFundo;
	private BufferedImage imgPontos;
	
	public PainelPontos(String título, int larg, int alt, Map<String, Color> cores)
	{
		super(larg, alt);
		j.setTitle(título);
		
		this.cores = cores;
		
		conjs = new LinkedList<>();
		todosPontos = new HashSet<>();
		
		descrições = new LinkedList<>();
		
		imgFundo = criarCamada();
		limpar(imgFundo, Color.WHITE);
	}

	public void adicionar(
			Collection<Classificação<String>> pontos, RectangularShape forma,
			//Function<Classificação<String>, Color> cores,
			double raio, boolean preencher)
	{
		conjs.add(new ParâmetrosDesenho(pontos, forma, raio, preencher));
		todosPontos.addAll(pontos);
	}
	
	public void adicionarDescrição(String desc)
	{
		descrições.add(desc);
	}
	
	
	public void pintar(int x, int y, Color cor)
	{
		imgFundo.setRGB(x, y, cor.getRGB());
	}
	
	
	@Override
	public void mostrar()
	{
		imgPontos = criarCamadaTransparente();
		// TODO mudar PainelBase.limpar() para usar isso
		Graphics2D gr = imgPontos.createGraphics();
		gr.setBackground(COR_TRANSPARENTE);
		gr.clearRect(0, 0, larguraTotal, alturaTotal);
		
		// Escala
		calcularEscala(todosPontos, Utilidades.classificaçãoParaPonto());
		
		// Desenha pontos
		for (ParâmetrosDesenho par : conjs)
			desenharPontos(imgPontos, par.pontos, Utilidades.classificaçãoParaPonto(),
					par.forma, par.raio, a -> cores.get(a.classe()), par.preencher);
		
		super.mostrar();
	}
	
	@Override
	protected void montarPainel()
	{
		GroupLayout layout = new GroupLayout(painel);
		painel.setLayout(layout);
		
		List<Component> legenda = new LinkedList<>();
		for (String classe : cores.keySet())
		{
			JLabel l = new JLabel(classe);
			l.setOpaque(true);
			Color cor = cores.get(classe);
			l.setBackground(cor);
			l.setForeground( (299 * cor.getRed() + 587 * cor.getGreen() + 114 * cor.getBlue()) / 1000d > 128 ?
					Color.BLACK : Color.WHITE);
			l.setHorizontalAlignment(CENTER);
			legenda.add(l);
		}
		
		GroupLayout.Group grupoHorizontalLegenda, grupoVerticalLegenda;
		grupoHorizontalLegenda = layout.createParallelGroup();
		grupoVerticalLegenda = layout.createSequentialGroup();
		for (Component c : legenda)
		{
			grupoHorizontalLegenda.addComponent(c);
			grupoVerticalLegenda.addComponent(c);
		}
		List<Component> ligarTamanho = new LinkedList<>(legenda);
		for (String desc : descrições)
		{
			JLabel l = new JLabel(desc);
			l.setHorizontalAlignment(CENTER);
			grupoHorizontalLegenda.addComponent(l);
			grupoVerticalLegenda.addComponent(l);
			ligarTamanho.add(l);
		}
		ligarTamanho.add(this);
		
		// Horizontal
		layout.setHorizontalGroup(
				layout.createParallelGroup()
						.addComponent(this)
						.addGroup(grupoHorizontalLegenda)
		);
		layout.linkSize(HORIZONTAL, ligarTamanho.toArray(new Component[0]));
		
		// Vertical
		layout.setVerticalGroup(
				layout.createSequentialGroup()
						.addComponent(this)
						.addGroup(grupoVerticalLegenda)
		);
	}
	
	@Override
	public void paint(Graphics gr)
	{
		gr.drawImage(imgFundo, 0, 0, null);
		gr.drawImage(imgPontos, 0, 0, null);
	}
	
	private static class ParâmetrosDesenho
	{
		Collection<Classificação<String>> pontos;
		RectangularShape forma;
		//Function<Classificação<String>, Color> cores;
		double raio;
		boolean preencher;
		
		public ParâmetrosDesenho(
				Collection<Classificação<String>> pontos, RectangularShape forma,
				//Function<Classificação<String>, Color> cores,
				double raio, boolean preencher)
		{
			this.pontos = pontos;
			this.forma = forma;
			//this.cores = cores;
			this.raio = raio;
			this.preencher = preencher;
		}
	}
}
