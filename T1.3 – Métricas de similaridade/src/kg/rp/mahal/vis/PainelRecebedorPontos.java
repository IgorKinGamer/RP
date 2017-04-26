package kg.rp.mahal.vis;

import static javax.swing.SwingConstants.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;

import javax.swing.*;
import javax.swing.GroupLayout.*;

import kg.rp.mahal.*;

@SuppressWarnings("serial")
public class PainelRecebedorPontos implements InterfaceRecebedoraPontos
{
	public static final Color COR_TRANSPARENTE = new Color(0, true);
	public static final Color COR_SELE��O = Color.RED;
	public static final float LARGURA_SELE��O = 5f;
	
	
	private JPanel painelBase;
	
	
	private RecebedorPontos rp;
	
	private final BufferedImage imagem;
	private final BufferedImage sele��o;
	private final Graphics2D grSele��o;
	private final Line2D.Double linha;
	private Ponto pIni;
	
	
	private final int largura, altura;
	
	/** Se a sele��o j� terminou. */
	private boolean pronto;
	
	
	/////// COMPONENTES ///////
	
	// Janela
	private JFrame janela;
	
	// Painel com a imagem
	private JPanel painelImagem;
	
	// Bot�es
	private JButton botRecome�ar, botConfirmar, botCancelar;
	
	// Textos
	private JLabel txtDicas;
	
	
	public PainelRecebedorPontos(BufferedImage imagem)
	{
		this.imagem = imagem;
		largura = imagem.getWidth();
		altura = imagem.getHeight();
		sele��o = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
		
		pronto = false;
		
		criarComponentes();
		registrarListeners();
		montarLayout();
		
		grSele��o = sele��o.createGraphics();
		grSele��o.setColor(COR_SELE��O);
		grSele��o.setBackground(COR_TRANSPARENTE);
		grSele��o.setStroke(new BasicStroke(LARGURA_SELE��O));
		linha = new Line2D.Double();
		grSele��o.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		limparSele��o();
	}
	
	private void criarComponentes()
	{
		painelBase = new JPanel(null);
		
		janela = new JFrame("Sele��o de pontos");
		janela.add(painelBase);
		
		painelImagem = new JPanel()
		{
			{ setPreferredSize(new Dimension(largura, altura)); }
			
			@Override
			public void paint(Graphics g)
			{
				g.drawImage(imagem, 0, 0, null);
				g.drawImage(sele��o, 0, 0, null);
			}
		};
		
		botConfirmar = new JButton("Confirmar sele��o");
		botRecome�ar = new JButton("Recome�ar sele��o");
		botCancelar  = new JButton("Cancelar");
		
		txtDicas = new JLabel("<Enter> para confirmar, <R> para recome�ar, <Esc> para cancelar");
	}
	
	private void registrarListeners()
	{
		// Painel de imagem
		painelImagem.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				pIni = new Ponto(e.getX(), e.getY());
				rp.iniciouSele��o(pIni);
				linha.setLine(pIni.x, pIni.y, pIni.x, pIni.y);
				grSele��o.draw(linha);
				
				painelBase.repaint();
			}
		});
		painelImagem.addMouseMotionListener(new MouseAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				Ponto pFim = new Ponto(e.getX(), e.getY());
				rp.arrastou(pFim);
				linha.setLine(pIni.x, pIni.y, pFim.x, pFim.y);
				grSele��o.draw(linha);
				pIni = pFim;
				
				painelBase.repaint();
			}
		});
		
		// Bot�es
		botConfirmar.addActionListener(e -> confirmarSele��o());
		botRecome�ar.addActionListener(e -> reiniciarSele��o());
		botCancelar.addActionListener(e -> cancelarSele��o());
		
		// Atalhos
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher()
		{
			public boolean dispatchKeyEvent(KeyEvent e)
			{
				switch (e.getKeyCode())
				{
					case KeyEvent.VK_ENTER:
						botConfirmar.doClick(200);
							break;
					case KeyEvent.VK_R:
						botRecome�ar.doClick(200);
							break;
					case KeyEvent.VK_ESCAPE:
						botCancelar.doClick(200);
							break;
					default:
						return false;
				}
				return true;
			}
		});
	}
	
	private void montarLayout()
	{
		// Layout
		GroupLayout layout = new GroupLayout(painelBase);
		painelBase.setLayout(layout);
		
		// Organiza layout
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		// Horizontal
		layout.setHorizontalGroup(
				layout.createParallelGroup(Alignment.CENTER, false)
						.addComponent(painelImagem)
						.addGroup
						(
								layout.createSequentialGroup()
										.addComponent(botConfirmar)
										.addComponent(botRecome�ar)
										.addComponent(botCancelar)
						)
						.addComponent(txtDicas)
		);
		layout.linkSize(HORIZONTAL, botRecome�ar, botConfirmar, botCancelar);
		
		// Vertical
		layout.setVerticalGroup(
				layout.createSequentialGroup()
						.addComponent(painelImagem)
						.addGroup
						(
								layout.createParallelGroup()
										.addComponent(botConfirmar)
										.addComponent(botRecome�ar)
										.addComponent(botCancelar)
						)
						.addComponent(txtDicas)
		);
	}
	
	
	private void limparSele��o()
	{
		grSele��o.clearRect(0, 0, largura, altura);
		/*grSele��o.setColor(COR_TRANSPARENTE);
		grSele��o.fillRect(0, 0, largura, altura);
		grSele��o.setColor(COR_SELE��O);*/
		painelBase.repaint();
	}
	
	
	private void pronto()
	{
		synchronized (painelImagem)
		{
			pronto = true;
			painelImagem.notify();
		}
		janela.dispose();
	}
	
	/////// FUN��ES PARA LISTENERS ///////
	
	private void confirmarSele��o()
	{
		if (rp.confirmar())
			pronto();
	}
	
	private void reiniciarSele��o()
	{
		rp.reiniciar();
		// Limpa sele��o
		limparSele��o();
	}
	
	private void cancelarSele��o()
	{
		//JOptionPane.showConfirmDialog(...);
		rp.abortar();
		pronto();
	}
	
	
	/////// M�TODOS SOBRESCRITOS ///////
	
	public void adicionarRecebedorPontos(RecebedorPontos rp)
	{
		this.rp = rp;
	};
	
	@Override
	public void receberPontos()
	{
		// Mostra a janela
		janela.pack();
		janela.setVisible(true);
		
		// Espera a sele��o acabar
		synchronized (painelImagem)
		{
			while (!pronto)
				try { painelImagem.wait(); }
				catch (InterruptedException ie) {}
		}
	}

	@Override
	public int largura()
	{
		return largura;
	}

	@Override
	public int altura()
	{
		return altura;
	}

}
