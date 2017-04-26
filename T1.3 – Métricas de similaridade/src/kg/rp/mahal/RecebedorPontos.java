package kg.rp.mahal;

public interface RecebedorPontos
{
	void iniciouSeleção(Ponto p);
	
	void arrastou(Ponto p);
	
	/**
	 * Verifica se a seleção até então é válida.
	 * Caso não seja, o chamador deve continuar a seleção ou abortar.
	 * @return {@code true} se a seleção foi aceita, {@code false} caso
	 * contrário
	 */
	boolean confirmar();
	
	void reiniciar();
	
	void abortar();
}
