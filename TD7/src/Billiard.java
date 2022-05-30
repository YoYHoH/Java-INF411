import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Vector;

//2 PROGRAMMATION DU BILLARD

public class Billiard {

	Vector<Ball> balls;
	double currentTime;
	PriorityQueue<Event> eventQueue;

	Billiard() {
		eventQueue = new PriorityQueue<Event>();
		balls = new Vector<Ball>();
	}

	// 2.1 LA BOUCLE DES ÉVÈNEMENTS
	
	void eventLoop() {
		// TODO (7-10 lignes)
		while(!eventQueue.isEmpty()) {
			Event e = eventQueue.poll();
			if(e.isValid()) {
				double t = e.time - currentTime;
				for(Ball b : balls) {
					b.forward(t);
				}
				currentTime = e.time;
				e.process();
			}
		}
	}
	
	
	// 2.2 PRÉDICTION DES COLLISIONS

	void predictCollisions(Ball b) {
		// TODO (~10 lignes)
		for(Ball bb : balls) {
			if(bb.equals(b)) continue;
			double t = b.nextBallCollision(bb);
			if(t<0) continue;
			double time = currentTime + t;
			eventQueue.add(new BallCollisionEvent(this, time, b, bb));
		}
		double t1 =  b.nextWallCollision(Direction.Horizontal);
		if(t1>=0)
			eventQueue.add(new WallCollisionEvent(this, currentTime+t1, b, Direction.Horizontal));
		double t2 =  b.nextWallCollision(Direction.Vertical);
		if(t2>=0)
			eventQueue.add(new WallCollisionEvent(this, currentTime+t2, b, Direction.Vertical));
		
	}

	void initialize() {
		// TODO (2 lignes)
		for(Ball b : balls)
			predictCollisions(b);
	}

	void run() {
		initialize();
		eventLoop();
	}
	
	// Lit une configuration initiale depuis un fichier
	Billiard(String path) throws FileNotFoundException {
		this();

		Scanner input = new Scanner(new File(path));
		input.useLocale(Locale.ENGLISH);  // pour que java reconnaisse "1.2" plutôt que "1,2"

		input.nextInt();	// Le premier entier contient le nombre de lignes.
							// Mais comme on utilise un tableau redimensionnable, on l'ignore.
		while (input.hasNext()) {
			double x = input.nextDouble();
			double y = input.nextDouble();
			double vx = input.nextDouble();
			double vy = input.nextDouble();
			double radius = input.nextDouble();
			double mass = input.nextDouble();
			int r = input.nextInt();
			int g = input.nextInt();
			int b = input.nextInt();

			balls.add(new Ball(x, y, vx, vy, radius, mass, new Color(r, g, b)));
		}
	}
}
