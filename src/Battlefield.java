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
	
//	public void figuringOutBestPlay() {
//		State og_State = new State(current_game, current_player);
//		State[] generatedStates = og_State.generateStates();
//		for(int i = 0; i < generatedStates.length; i++) {
//			sNode temp = new sNode(generatedStates[i]);
//			stateQueue.enqueue(temp);
//		}
//		boolean winFound = false;
//		boolean lossFound = false;
//		sNode temp = null;
//		while(!winFound && !lossFound) {
//			System.out.println("new node");
//			sNode cur_node = stateQueue.dequeue();	
//			if(cur_node == null) break;// the queue is empty
//			State[] deeperStates = cur_node.pState.generateStates();
//			// for loop to enqueue the generated states
//			for(int i = 0; i < deeperStates.length; i++) {
//				temp = new sNode(deeperStates[i]);				
//				// restricting what gets enqueued
//				// do not enqueue if a tie 
//				if(temp.pState.isTie == false) {
//					stateQueue.enqueue(temp);
//				}
//			}
//			cur_node.pState.endStateTest();
//			// stop when we have found a winning state
//			if(cur_node.pState.isWin == true) {
//				winFound = true;
//				while(cur_node.pState.parentState != null) {
//					if(cur_node.pState.parentState.parentState == null) break;
//					cur_node.pState = cur_node.pState.parentState;
//				}
//				// we have found the next play, which is the child of the ultimate parent state
//				// it is under the variable cur_node
//				// we will now change the game map to reflect this cur_node, this will be our play.
//				System.out.println("winnnna");
//				current_game = cur_node.pState.game;					
//			}
//			//stop if we have found a loss with depth of 1
//			// we need to block this loss before anything else
//			if(cur_node.pState.isLoss) {
//				System.out.println(cur_node.pState.depth);
//				if(cur_node.pState.depth <= 2) {
//					System.out.println("here");
//					lossFound = true;
//					// now figure out where the loss is and what coordinates
//					// upper row
//					if(cur_node.pState.game[0][0] != -1 && cur_node.pState.game[0][0] == cur_node.pState.game[0][1] && cur_node.pState.game[0][1] == cur_node.pState.game[0][2]) {
//						for(int j =0; j< 3; j++ ) {
//							if(cur_node.pState.parentState.game[0][j] == -1) {
//								State defenseState = new State(cur_node.pState.parentState,0,j,0);
//								current_game = defenseState.game;
//							}
//						}
//					}
//					// middle row
//					else if(cur_node.pState.game [1][0] != -1 && cur_node.pState.game[1][0] == cur_node.pState.game[1][1] && cur_node.pState.game[1][1] == cur_node.pState.game[1][2]) {
//						for(int j =0; j< 3; j++ ) {
//							if(cur_node.pState.parentState.game[1][j] == -1) {
//								State defenseState = new State(cur_node.pState.parentState,1,j,0);
//								current_game = defenseState.game;
//							}
//						}
//					}
//					// lower row
//					else if(cur_node.pState.game [2][0] != -1 && cur_node.pState.game[2][0] == cur_node.pState.game[2][1] && cur_node.pState.game[2][1] == cur_node.pState.game[2][2]) {
//						for(int j =0; j< 3; j++ ) {
//							if(cur_node.pState.parentState.game[2][j] == -1) {
//								State defenseState = new State(cur_node.pState.parentState,2,j,0);
//								current_game = defenseState.game;
//							}
//						}
//					}
//					// left col
//					else if(cur_node.pState.game [0][0] != -1 && cur_node.pState.game[0][0] == cur_node.pState.game[1][0] && cur_node.pState.game[1][0] == cur_node.pState.game[2][0]) {
//						for(int j =0; j< 3; j++ ) {
//							if(cur_node.pState.parentState.game[j][0] == -1) {
//								State defenseState = new State(cur_node.pState.parentState,j,0,0);
//								current_game = defenseState.game;
//							}
//						}
//					}
//					// middle col
//					else if(cur_node.pState.game [0][1] != -1 && cur_node.pState.game[0][1] == cur_node.pState.game[1][1] && cur_node.pState.game[1][1] == cur_node.pState.game[2][1]) {
//						for(int j =0; j < 3; j++ ) {
//							if(cur_node.pState.parentState.game[j][1] == -1) {
//								//System.out.println("found");
//								//cur_node.pState.stateDisplay();
//								//System.out.println("parent state");
//								//cur_node.pState.parentState.stateDisplay();
//								State defenseState = new State(cur_node.pState.parentState.parentState,j,1,0);
//								current_game = defenseState.game;								
//							}
//						}
//					}
//					//bottom col
//					else if(cur_node.pState.game [0][2] != -1 && cur_node.pState.game[0][2] == cur_node.pState.game[1][2] && cur_node.pState.game[1][2] == cur_node.pState.game[2][2]) {
//						for(int j =0; j< 3; j++ ) {
//							if(cur_node.pState.parentState.game[j][2] == -1) {
//								State defenseState = new State(cur_node.pState.parentState,j,2,0);
//								current_game = defenseState.game;
//							}
//						}
//					}
//					// left to right cross
//					else if(cur_node.pState.game [0][0] != -1 && cur_node.pState.game[0][0] == cur_node.pState.game[1][1] && cur_node.pState.game[1][1] == cur_node.pState.game[2][2]) {
//						for(int j =0; j< 3; j++ ) {
//							int y = 0;
//							if(cur_node.pState.parentState.game[j][y] == -1) {
//								State defenseState = new State(cur_node.pState.parentState,j,y,0);
//								current_game = defenseState.game;
//							}
//							y++;
//						}
//					}
//					// right to left cross
//					else if(cur_node.pState.game [0][2] != -1 && cur_node.pState.game[0][2] == cur_node.pState.game[1][1] && cur_node.pState.game[1][1] == cur_node.pState.game[2][0]) {
//						for(int j =0; j< 3; j++ ) {
//							int y=2;
//							if(cur_node.pState.parentState.game[j][y] == -1) {
//								State defenseState = new State(cur_node.pState.parentState,j,y,0);
//								current_game = defenseState.game;
//							}
//							y--;
//						}
//					}
//				}
//			}
//			
//		}
//		// we have found our winner or loser so we can dequeue in preparation for the next queue of states
//		stateQueue.emptyQueue();
//		
//	}

}
