import java.io.FileNotFoundException;

import graphics.BasicOceanCanvas;
import ocean.BasicDirections;
import ocean.Coordinate;
import ocean.Direction;
import ocean.Ocean;
import ocean.Traversal;

///////////// Exercices à compléter /////////////

/**************************************
 * Question 1 : parcours en profondeur
 **************************************/

/**
 * Parcours en profondeur
 */
class DepthFirst extends Traversal {
	/**
	 * Implémente le parcours en profondeur
	 */
	@Override
	public boolean traverse(Ocean ocean, Coordinate start) {
		if (ocean == null || start == null)
			return false;
		// TODO: implémenter le parcours en profondeur (Question 1)
		if(!ocean.isValid(start)) return false;
		if(ocean.isWall(start) || ocean.isMarked(start)) return false;
		if(ocean.isNemoAt(start)) {
			ocean.setMark(start,path);
			return true;
		}
		ocean.setMark(start);
		for(Direction d : ocean.directions()) {
			Coordinate next = start.moveTo(d);
			if(!ocean.isValid(next)) continue;
			if(traverse(ocean, next)) {
				ocean.setMark(start, path);
				return true;
			}
		}
		ocean.setMark(start, deadEnd);
		return false;
	}

	/**
	 * Lance la visualisation du parcours de l'océan dont la carte se trouve dans
	 * {@code no-sharks.txt}
	 * 
	 * @param args non utilisé
	 * @throws FileNotFoundException lorsque la carte n'est pas accessible
	 */
	public static void main(String[] args) throws FileNotFoundException {
		String fileName = (args.length < 1) ? "no-sharks.txt" : args[0];

		// on crée un nouveau océan à partir de la carte sans requins
		// et on y associe un nouveau rapporteur qui sera utilisé pour suivre toutes les
		// explorations à la suite
		Ocean noSharks = new Ocean(fileName, "data/" + fileName, BasicDirections.values());

		// on lance le parcours en profondeur
		System.out.println("Test parcours en profondeur (depth first search)");
		noSharks.reporters().add(new BasicOceanCanvas("No Sharks - DFS")).exploreUsing(new DepthFirst());
	}
}
