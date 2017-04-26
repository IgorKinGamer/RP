package kg.rp.clas.metr;

import kg.rp.clas.*;

@FunctionalInterface
public interface Métrica
{
	double distância(Amostra a, Amostra b);
}
