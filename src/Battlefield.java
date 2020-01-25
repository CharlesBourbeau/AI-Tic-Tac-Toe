import java.util.Random;
import java.util.Scanner;



public class Battlefield {

	public int[][] current_game;
	public int current_player;
	private static Random rd = new Random();

	public Battlefield() {
		current_game = new int[][]{{-1, -1, -1}, {-1, -1, -1}, {-1, -1, -1}};
		boolean AI_first = rd.nextBoolean();
		// the AI is the O player and the human is the X (1) player
		if (AI_first) {
			current_player = 0;
		} else {
			current_player = 1;
		}

	}

	
	public static void main(String[] args) {
		boolean AI_first = rd.nextBoolean();
		Scanner reader = new Scanner(System.in);
		Battlefield battlefield = new Battlefield();
//		StateDataStructure stateTree = new StateDataStructure();
//		State testState = new State(0,0,1);
//		testState.game[0][1] = 0;
//		testState.game[2][1] = 1;
//		testState.game[2][2] = 0;
//		testState.game[2][0] = 0;
//		testState.game[2][1] = 0;
//		testState.game[2][2] = 1;
//		testState.endStateTest();
//		System.out.println("current state = " + testState.gameStatus);
//		testState.stateDisplay();
//		sNode testNode = new sNode(testState);
//		stateTree.generateSearchTree(testNode);
//		stateTree.computeProbabilities(testNode);
//		System.out.println(stateTree.counter);
//		stateTree.pickOptimalPlay(testNode);
		
		StateDataStructure currentStateTree = new StateDataStructure();
		sNode futureNode;
		if(AI_first) {
		
			State emptyState = new State(1);
			emptyState.endStateTest();
			sNode emptyNode = new sNode(emptyState);
			currentStateTree.generateSearchTree(emptyNode);
			currentStateTree.computeProbabilities(emptyNode);
			futureNode = currentStateTree.pickOptimalPlay(emptyNode);
			futureNode.pState.stateDisplay();
			for(int i = 0 ; i < 3 ; i ++) {
				for(int j = 0 ; j < 3 ; j ++) {
					battlefield.current_game[i][j] = futureNode.pState.game[i][j];
				}
			}
		}
		while(true) {
			System.out.println("Enter coordinates : ");
			String s = reader.next();
			int input = Integer.parseInt(s);
			int x = input/10;
			int y = input%10;
			battlefield.play(x, y);
			State currentState = new State(battlefield.current_game,1);
			currentState.endStateTest();
			sNode currentNode = new sNode(currentState);
			currentStateTree = new StateDataStructure();
			currentStateTree.generateSearchTree(currentNode);
			currentStateTree.computeProbabilities(currentNode);
			futureNode = currentStateTree.pickOptimalPlay(currentNode);
			futureNode.pState.stateDisplay();
			for(int i = 0 ; i < 3 ; i ++) {
				for(int j = 0 ; j < 3 ; j ++) {
					battlefield.current_game[i][j] = futureNode.pState.game[i][j];
				}
			}
			battlefield.gameDisplay();
		}
		//reader.close();
	}
	
	//method to display the game 
	public void gameDisplay() {
		System.out.println("[" + current_game[0][0] + "," + current_game[0][1] + "," + current_game[0][2] + "," + "]\n" 
				+ "[" + current_game[1][0] + "," + current_game[1][1] + "," + current_game[1][2] + "," + "]\n" + 
				"[" + current_game[2][0] + "," + current_game[2][1] + "," + current_game[2][2] + "," + "]\n\n");
	}
	
	//method for the user to play and change the game map
	public void play(int x, int y) {
		if(current_game[x][y] == -1) {
			current_game[x][y] = this.current_player;
		}
	}
}
