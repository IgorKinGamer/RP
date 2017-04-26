package kg.rp.ibl;

public interface FunçãoSimilaridade
{
	double aplicar(Amostra a, Amostra b);
	
	void atualizarNormalização(Amostra a);
}
