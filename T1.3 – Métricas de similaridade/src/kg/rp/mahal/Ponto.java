package kg.rp.mahal;

public class Ponto
{
	public final int x, y;
	
	public Ponto(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o)
	{
		return (o instanceof Ponto)
				&& x == ((Ponto) o).x
				&& y == ((Ponto) o).y;
	}
	
	@Override
	public int hashCode()
	{
		return x + 101*y;
	}
	
	@Override
	public String toString()
	{
		return String.format("(%d, %d)", x, y);
	}
}
