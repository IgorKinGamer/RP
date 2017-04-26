package kg.rp.mahal.vis;

import java.awt.image.*;
import java.io.*;

import javax.imageio.*;

import kg.rp.mahal.*;

public class TestePainelRecebedorPontos
{
	public static void main(String[] args) throws Exception
	{
		BufferedImage imagem = ImageIO.read(new File(args[0]));
		PainelRecebedorPontos prp = new PainelRecebedorPontos(imagem);
		
		prp.adicionarRecebedorPontos(new EscritorPontosRecebidos());
		
		i("Dimensões (largura, altura): %d, %d", prp.largura(), prp.altura());
		
		prp.receberPontos();
		
		/*i("Recepção de pontos terminada");
		i("####### PONTOS #######");
		i("##### FIM PONTOS #####");*/
	}
	
	private static class EscritorPontosRecebidos implements RecebedorPontos
	{
		Ponto pIni;
		
		public void iniciouSeleção(Ponto p)
		{
			pIni = p;
			i("Iniciou seleção em " + p);
		}
		
		public void arrastou(Ponto p)
		{
			i("Arrastou de %s para %s", pIni, p);
			pIni = p;
		}

		public boolean confirmar()
		{
			i("Tentando confirmar");
			return pIni != null;
		}

		public void reiniciar()
		{
			i("Reiniciando");
			pIni = null;
		}

		public void abortar()
		{
			i("Abortando (ou não)");
		}
	}
	
	static void i(Object msg)
	{
		System.out.println(msg);
	}
	
	static void i(String formato, Object... args)
	{
		System.out.println(String.format(formato, args));
	}
}
