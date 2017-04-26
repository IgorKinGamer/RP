package kg.rp.vor;

import kg.util.geom.*;

public class Tri�ngulo
{
	/** V�rtices do tri�ngulo, no sentido anti-hor�rio. */
	public final Ponto p1, p2, p3;
	public Ponto[] v�rtices;
	/** Circuncentro deste tri�ngulo. */
	public final Ponto cc;
	
	/** Arestas do tri�ngulo. {@code ai} liga {@code pi} ao pr�ximo ponto no sentido anti-hor�rio. */
	public final Aresta a1, a2, a3;
	private Aresta[] arestas;
	
	private Tri�ngulo[] ts;
	
	public Tri�ngulo(Ponto p1, Ponto p2, Ponto p3, Ponto cc)
	{
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		v�rtices = new Ponto[] {p1, p2, p3};
		this.cc = cc;
		a1 = new Aresta(p1, p2);
		a2 = new Aresta(p2, p3);
		a3 = new Aresta(p3, p1);
		arestas = new Aresta[] {a1, a2, a3};
		
		ts = new Tri�ngulo[3];
	}
	
	public Ponto v�rtice(int v)
	{
		return v�rtices[v-1];
	}
	
	public Aresta aresta(int a)
	{
		return arestas[a-1];
	}
	
	/**
	 * lado 1 � a aresta p1->p2, 2 � a p2->p3 e 3 � a p3->p1
	 * @param lado
	 * @param adj
	 */
	public void atribuirAdjacente(int lado, Tri�ngulo adj)
	{
		--lado;
		
		if (ts[lado] != null)
			throw new IllegalArgumentException("Lado j� atribu�do: " + lado);
		
		ts[lado] = adj;
	}
	
	public Tri�ngulo pegarAdjacente(int lado)
	{
		return ts[lado-1];
	}
	
	@Override
	public String toString()
	{
		return "(" + p1 + ", " + p2 + ", " + p3 + ")";
	}
}
