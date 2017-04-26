package kg.rp.mahal;

import java.util.function.*;

import org.la4j.*;
import org.la4j.matrix.dense.*;
import org.la4j.vector.dense.*;

public class FunçãoMahalanobis implements ToDoubleFunction<double[]>
{
	private final Matrix inversa;
	
	public FunçãoMahalanobis(double[][] inv)
	{
		inversa = new Basic2DMatrix(inv);
	}
	
	public double applyAsDouble(double[] entr)
	{
		Vector v = new BasicVector(entr);
		return v.multiply(inversa).apply(LinearAlgebra.OO_PLACE_INNER_PRODUCT, v);
	}
}
