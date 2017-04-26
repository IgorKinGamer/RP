package kg.rp.vor;

import kg.util.geom.*;

public class Triângulo
{
	/** Vértices do triângulo, no sentido anti-horário. */
	public final Ponto p1, p2, p3;
	public Ponto[] vértices;
	/** Circuncentro deste triângulo. */
	public final Ponto cc;
	
	/** Arestas do triângulo. {@code ai} liga {@code pi} ao próximo ponto no sentido anti-horário. */
	public final Aresta a1, a2, a3;
	private Aresta[] arestas;
	
	private Triângulo[] ts;
	
	public Triângulo(Ponto p1, Ponto p2, Ponto p3, Ponto cc)
	{
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		vértices = new Ponto[] {p1, p2, p3};
		this.cc = cc;
		a1 = new Aresta(p1, p2);
		a2 = new Aresta(p2, p3);
		a3 = new Aresta(p3, p1);
		arestas = new Aresta[] {a1, a2, a3};
		
		ts = new Triângulo[3];
	}
	
	public Ponto vértice(int v)
	{
		return vértices[v-1];
	}
	
	public Aresta aresta(int a)
	{
		return arestas[a-1];
	}
	
	/**
	 * lado 1 é a aresta p1->p2, 2 é a p2->p3 e 3 é a p3->p1
	 * @param lado
	 * @param adj
	 */
	public void atribuirAdjacente(int lado, Triângulo adj)
	{
		--lado;
		
		if (ts[lado] != null)
			throw new IllegalArgumentException("Lado já atribuído: " + lado);
		
		ts[lado] = adj;
	}
	
	public Triângulo pegarAdjacente(int lado)
	{
		return ts[lado-1];
	}
	
	@Override
	public String toString()
	{
		return "(" + p1 + ", " + p2 + ", " + p3 + ")";
	}
}
