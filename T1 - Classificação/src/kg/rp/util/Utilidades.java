package kg.rp.util;

import java.util.function.*;

import kg.rp.clas.*;
import kg.rp.vor.*;
import kg.util.geom.*;

public class Utilidades
{
	public static final Function<Classifica��o<?>, Ponto> CLASSIFICA��O_PARA_PONTO =
			c ->
			{
				Amostra a = c.amostra();
				return new Ponto(a.atributo(0), a.atributo(1));
			};
			
	public static <C> Function<Classifica��o<C>, Ponto> classifica��oParaPonto()
	{
		return c ->
		{
			Amostra a = c.amostra();
			return new Ponto(a.atributo(0), a.atributo(1));
		};
	}
}
