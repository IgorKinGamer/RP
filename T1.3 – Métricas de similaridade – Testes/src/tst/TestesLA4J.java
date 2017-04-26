package tst;

import org.la4j.*;
import org.la4j.vector.*;
import org.la4j.vector.dense.*;

public class TestesLA4J
{
	public static void main(String[] args)
	{
		DenseVector v = new BasicVector(new double[] {1, 7, 2, -1, 4});
		double r = LinearAlgebra.OO_PLACE_INNER_PRODUCT.apply(v, v);
		System.out.println(r);
	}
}
