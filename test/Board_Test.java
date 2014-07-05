import pair.Pair;
import biome.model.board.Board;

public class Board_Test 
{
	public static void main(String[] args)
	{
		testDistance();
	}
	
	public static void testDistance()
	{
		Pair<Integer, Integer> coordA = new Pair<Integer, Integer>();
		coordA.setFirst(1);
		coordA.setSecond(1);
		
		Pair<Integer, Integer> coordB = new Pair<Integer, Integer>();
		coordB.setFirst(4);
		coordB.setSecond(3);
		
		Double distance = Board.distance(coordA, coordB);
		System.out.println("Distance betweent " + coordA + " and " + coordB + 
				": " +distance);
	}

}
