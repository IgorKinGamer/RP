package kg.rp.ibl;

import java.awt.*;

import javax.swing.*;

import kg.util.swing.*;

public class JanelaTexto
{
	public JanelaTexto(String t�tulo, String texto)
	{
		JTextArea �rea = new JTextArea(texto, 20, 80);
		�rea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		�rea.setEditable(false);
		
		JScrollPane rolamento = new JScrollPane(�rea);
		
		JPanel painel = new JPanel();
		painel.add(rolamento);
		
		JFrame janela = new JFrame(t�tulo);
		janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Janelas.registrarFechamentoJanela(janela);
		
		janela.add(painel);
		janela.pack();
		janela.setVisible(true);
	}
}
