package kg.rp.mahal;

import static java.lang.Math.*;

import java.util.*;

public class ListadorPontosRecebidos
{
	private final InterfaceRecebedoraPontos irp;
	private final Set<Ponto> pontos;
	
	public ListadorPontosRecebidos(InterfaceRecebedoraPontos irp)
	{
		this.irp = irp;
		pontos = new HashSet<>();
		
		irp.adicionarRecebedorPontos(new RecebedorPontos()
		{
			Ponto ini;
			
			public void iniciouSeleção(Ponto ini)
			{
				adicionarPonto(ini);
				this.ini = ini;
			}
			
			public void arrastou(Ponto fim)
			{
				int dx, dy, passos;
				dx = fim.x - ini.x;
				dy = fim.y - ini.y;
				passos = (int) max(abs(dx), abs(dy));
				
				for (int i = 1; i <= passos; i++)
				{
					float porcentagem = (float) i / passos;
					Ponto p = new Ponto(
							round(ini.x + porcentagem*dx),
							round(ini.y + porcentagem*dy));
					adicionarPonto(p);
				}
				
				ini = fim;
			}
			
			public boolean confirmar()
			{
				return pontos.size() > 0;
			}

			public void reiniciar()
			{
				pontos.clear();
			}

			public void abortar()
			{
				System.out.println("Seleção de pontos cancelada. Abortando.");
				System.exit(0);
			}
		});
	}
	
	private void adicionarPonto(Ponto p)
	{
		if (p.x >= 0 && p.x < irp.largura()
				&& p.y >= 0 && p.y < irp.altura())
			pontos.add(p);
	}
	
	public Collection<Ponto> pontos()
	{
		irp.receberPontos();
		return Collections.unmodifiableList(new ArrayList<>(pontos));
	}
}
