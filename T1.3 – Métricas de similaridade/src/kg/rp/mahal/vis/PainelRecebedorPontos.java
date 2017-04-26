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
	public static final Color COR_SELEÇÃO = Color.RED;
	public static final float LARGURA_SELEÇÃO = 5f;
	
	
	private JPanel painelBase;
	
	
	private RecebedorPontos rp;
	
	private final BufferedImage imagem;
	private final BufferedImage seleção;
	private final Graphics2D grSeleção;
	private final Line2D.Double linha;
	private Ponto pIni;
	
	
	private final int largura, altura;
	
	/** Se a seleção já terminou. */
	private boolean pronto;
	
	
	/////// COMPONENTES ///////
	
	// Janela
	private JFrame janela;
	
	// Painel com a imagem
	private JPanel painelImagem;
	
	// Botões
	private JButton botRecomeçar, botConfirmar, botCancelar;
	
	// Textos
	private JLabel txtDicas;
	
	
	public PainelRecebedorPontos(BufferedImage imagem)
	{
		this.imagem = imagem;
		largura = imagem.getWidth();
		altura = imagem.getHeight();
		seleção = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
		
		pronto = false;
		
		criarComponentes();
		registrarListeners();
		montarLayout();
		
		grSeleção = seleção.createGraphics();
		grSeleção.setColor(COR_SELEÇÃO);
		grSeleção.setBackground(COR_TRANSPARENTE);
		grSeleção.setStroke(new BasicStroke(LARGURA_SELEÇÃO));
		linha = new Line2D.Double();
		grSeleção.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		limparSeleção();
	}
	
	private void criarComponentes()
	{
		painelBase = new JPanel(null);
		
		janela = new JFrame("Seleção de pontos");
		janela.add(painelBase);
		
		painelImagem = new JPanel()
		{
			{ setPreferredSize(new Dimension(largura, altura)); }
			
			@Override
			public void paint(Graphics g)
			{
				g.drawImage(imagem, 0, 0, null);
				g.drawImage(seleção, 0, 0, null);
			}
		};
		
		botConfirmar = new JButton("Confirmar seleção");
		botRecomeçar = new JButton("Recomeçar seleção");
		botCancelar  = new JButton("Cancelar");
		
		txtDicas = new JLabel("<Enter> para confirmar, <R> para recomeçar, <Esc> para cancelar");
	}
	
	private void registrarListeners()
	{
		// Painel de imagem
		painelImagem.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				pIni = new Ponto(e.getX(), e.getY());
				rp.iniciouSeleção(pIni);
				linha.setLine(pIni.x, pIni.y, pIni.x, pIni.y);
				grSeleção.draw(linha);
				
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
				grSeleção.draw(linha);
				pIni = pFim;
				
				painelBase.repaint();
			}
		});
		
		// Botões
		botConfirmar.addActionListener(e -> confirmarSeleção());
		botRecomeçar.addActionListener(e -> reiniciarSeleção());
		botCancelar.addActionListener(e -> cancelarSeleção());
		
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
						botRecomeçar.doClick(200);
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
										.addComponent(botRecomeçar)
										.addComponent(botCancelar)
						)
						.addComponent(txtDicas)
		);
		layout.linkSize(HORIZONTAL, botRecomeçar, botConfirmar, botCancelar);
		
		// Vertical
		layout.setVerticalGroup(
				layout.createSequentialGroup()
						.addComponent(painelImagem)
						.addGroup
						(
								layout.createParallelGroup()
										.addComponent(botConfirmar)
										.addComponent(botRecomeçar)
										.addComponent(botCancelar)
						)
						.addComponent(txtDicas)
		);
	}
	
	
	private void limparSeleção()
	{
		grSeleção.clearRect(0, 0, largura, altura);
		/*grSeleção.setColor(COR_TRANSPARENTE);
		grSeleção.fillRect(0, 0, largura, altura);
		grSeleção.setColor(COR_SELEÇÃO);*/
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
	
	/////// FUNÇÕES PARA LISTENERS ///////
	
	private void confirmarSeleção()
	{
		if (rp.confirmar())
			pronto();
	}
	
	private void reiniciarSeleção()
	{
		rp.reiniciar();
		// Limpa seleção
		limparSeleção();
	}
	
	private void cancelarSeleção()
	{
		//JOptionPane.showConfirmDialog(...);
		rp.abortar();
		pronto();
	}
	
	
	/////// MÉTODOS SOBRESCRITOS ///////
	
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
		
		// Espera a seleção acabar
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
