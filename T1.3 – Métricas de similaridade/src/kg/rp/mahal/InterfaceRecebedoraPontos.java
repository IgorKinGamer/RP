package kg.rp.mahal;

/**
 * Deve receber um RecebedorPontos.
 */
public interface InterfaceRecebedoraPontos
{
	void adicionarRecebedorPontos(RecebedorPontos rp);

	void receberPontos();

	int largura();

	int altura();
}
