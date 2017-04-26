package tst;

import java.awt.*;

import javax.swing.*;

public class TesteJFrame
{
	@SuppressWarnings("serial")
	public static void main(String[] args)
	{
		JFrame janela = new JFrame("Este é o título");
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.add(new JPanel() {{setPreferredSize(new Dimension(240, 240));}});
		janela.pack();
		janela.setVisible(true);
		
		while (true);
	}
}
