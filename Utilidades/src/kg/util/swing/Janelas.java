package kg.util.swing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Janelas
{
	public static void registrarFechamentoJanela(JFrame janela)
	{
		KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		
		KeyEventDispatcherLambdaProxy eu = KeyEventDispatcherLambdaProxy.proxy();
		eu.definir(
				e ->
				{
					if (janela.isFocused() && e.getID() == KeyEvent.KEY_PRESSED
							&& e.getKeyCode() == KeyEvent.VK_ESCAPE)
					{
						janela.dispatchEvent(new WindowEvent(janela, WindowEvent.WINDOW_CLOSING));
						kfm.removeKeyEventDispatcher(eu);
						return true;
					}
					return false;
				}
		);
		kfm.addKeyEventDispatcher(eu);
	}
	
	public static class KeyEventDispatcherLambdaProxy implements KeyEventDispatcher
	{
		KeyEventDispatcher eu;
		
		@Override
		public boolean dispatchKeyEvent(KeyEvent e)
		{
			return eu.dispatchKeyEvent(e);
		}
		
		void definir(KeyEventDispatcher ked)
		{
			this.eu = ked;
		}
		
		static KeyEventDispatcherLambdaProxy proxy()
		{
			return new KeyEventDispatcherLambdaProxy();
		}
	}
}
