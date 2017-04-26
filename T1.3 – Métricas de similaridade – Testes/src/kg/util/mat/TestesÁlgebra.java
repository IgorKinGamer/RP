package kg.util.mat;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.la4j.*;
import org.la4j.matrix.dense.*;

public class Testes¡lgebra
{
	Random r;
	
	@Before
	public void configurar()
	{
		r = new Random(1337);
	}

	@Test
	public void covari‚ncia()
	{
		final double limite = 10;
		final int amostras = 10;
		double[][] dados = new double[amostras][3];
		for (int i = 0; i < amostras; i++)
			for (int j = 0; j < 3; j++)
				dados[i][j] = limite*r.nextDouble();
		Matrix minhaMatriz = new Basic2DMatrix(¡lgebra.covari‚ncia(dados));
		
		Matrix matriz = new Basic2DMatrix(¡lgebra.desvios(dados))
			.transpose().multiplyByItsTranspose().divide(amostras - 1);
		Matrix diferenÁa = matriz.subtract(minhaMatriz);
		
		//System.out.println(new Basic2DMatrix(dados));
		//System.out.println(new Basic2DMatrix(¡lgebra.desvios(dados)));
		
		//System.out.println(minhaMatriz);
		//System.out.println(matriz);
		for (double d : diferenÁa)
			assertEquals(0, d, 1e-6);
	}
}
