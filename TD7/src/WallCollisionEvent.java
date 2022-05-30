
// 3.1 COLLISION AVEC UNE BANDE

public class WallCollisionEvent extends Event {
	// Les champs suivants sont hérités de Event:
	// Billiard billiard;
	// final double time;
	
	Direction direction;
	Ball ball;
	int nbCollisions;
	
	WallCollisionEvent(Billiard billiard, double time, Ball ball, Direction direction) {
		super(billiard, time);
		this.ball = ball;
		this.direction = direction;
		this.nbCollisions = ball.nbCollisions();
	}
	
	@Override
	boolean isValid() {
		// TODO (1 ligne)
		return this.nbCollisions==ball.nbCollisions() ;
	}
	
	@Override
	void process() {
		// TODO (2 lignes)
		this.ball.collideWall(this.direction);
		this.billiard.predictCollisions(this.ball);
	}
}
