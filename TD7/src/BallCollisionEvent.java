
// 3.2 COLLISION ENTRE DEUX BILLES 

public class BallCollisionEvent extends Event {
	Ball a, b;
	// ...
	int i,j;
	
	BallCollisionEvent(Billiard billard, double time, Ball a, Ball b) {
		super(billard, time);
		
		this.a = a;
		this.b = b;
		// TODO (2 lignes)
		this.i = a.nbCollisions();
		this.j = b.nbCollisions();
	}
	
	@Override
	boolean isValid() {
		// TODO (1 ligne)
		return this.i==this.a.nbCollisions()&&this.j==this.b.nbCollisions();
	}
	
	@Override
	void process() {
		// TODO (3 lignes)
		this.b.collideBall(a);
		this.billiard.predictCollisions(b);
		this.billiard.predictCollisions(a);
	}

}

