package kg.rp.mahal;

public interface RecebedorPontos
{
	void iniciouSele��o(Ponto p);
	
	void arrastou(Ponto p);
	
	/**
	 * Verifica se a sele��o at� ent�o � v�lida.
	 * Caso n�o seja, o chamador deve continuar a sele��o ou abortar.
	 * @return {@code true} se a sele��o foi aceita, {@code false} caso
	 * contr�rio
	 */
	boolean confirmar();
	
	void reiniciar();
	
	void abortar();
}
