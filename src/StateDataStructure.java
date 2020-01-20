public class StateDataStructure {
	
	int counter = 0;

	/**
	 * Recursive function to generate the search tree and populate each of the node's child_nodes array list.
	 * Implementation decision: One way to make our algorithm perform better is to assume that whenever 
	 * an opponent occupies two cases in a row and the next case making him win is available, he will pick
	 * that case for his next play. In other words, it is too unsafe to assume that a player will potentially
	 * not pay attention and miss his winning play. This creates unfortunate probability calculations.
	 */
	
	public void generateSearchTree(sNode aNode) {
		//aNode.pState.stateDisplay();
		counter++;
		System.out.println(counter);
		// return when the game is either won, lost or drawn
		if(aNode.pState.gameStatus != State.GameStatus.Nothing) {
			return;
		}
		if(aNode.pState.playerType == 0 && aNode.pState.general_one_away) {
			// we are now in a situation where the opponent will win on his next move.
			// we first figure out where this next move is 
			if(aNode.pState.one_away_top_row) {
				for(int i = 0; i < 3; i++) {
					// we have found the winning move for the opponent.
					// this is the only move we will generate as the child node
					if(aNode.pState.game[0][i] == -1) {
						State losingState = new State(aNode.pState,0,i);
						losingState.endStateTest();
						sNode losingTemp = new sNode(losingState);
						aNode.child_nodes.add(losingTemp);
						return;
					}
				}
			}
			if(aNode.pState.one_away_mid_row) {
				for(int i = 0; i < 3; i++) {
					// we have found the winning move for the opponent.
					// this is the only move we will generate as the child node
					if(aNode.pState.game[1][i] == -1) {
						State losingState = new State(aNode.pState,1,i);
						losingState.endStateTest();
						sNode losingTemp = new sNode(losingState);
						aNode.child_nodes.add(losingTemp);
						return;
					}
				}
			}
			if(aNode.pState.one_away_bot_row) {
				for(int i = 0; i < 3; i++) {
					// we have found the winning move for the opponent.
					// this is the only move we will generate as the child node
					if(aNode.pState.game[2][i] == -1) {
						State losingState = new State(aNode.pState,2,i);
						losingState.endStateTest();
						sNode losingTemp = new sNode(losingState);
						aNode.child_nodes.add(losingTemp);
						return;
					}
				}
			}
			if(aNode.pState.one_away_left_col) {
				for(int i = 0; i < 3; i++) {
					// we have found the winning move for the opponent.
					// this is the only move we will generate as the child node
					if(aNode.pState.game[i][0] == -1) {
						State losingState = new State(aNode.pState,i,0);
						losingState.endStateTest();
						sNode losingTemp = new sNode(losingState);
						aNode.child_nodes.add(losingTemp);
						return;
					}
				}
			}
			if(aNode.pState.one_away_mid_col) {
				for(int i = 0; i < 3; i++) {
					// we have found the winning move for the opponent.
					// this is the only move we will generate as the child node
					if(aNode.pState.game[i][1] == -1) {
						State losingState = new State(aNode.pState,i,1);
						losingState.endStateTest();
						//losingState.stateDisplay();
						//System.out.println("game status = " + losingState.gameStatus);
						sNode losingTemp = new sNode(losingState);
						aNode.child_nodes.add(losingTemp);
						return;
					}
				}
			}
			if(aNode.pState.one_away_right_col) {
				for(int i = 0; i < 3; i++) {
					// we have found the winning move for the opponent.
					// this is the only move we will generate as the child node
					if(aNode.pState.game[i][2] == -1) {
						State losingState = new State(aNode.pState,i,2);
						losingState.endStateTest();
						sNode losingTemp = new sNode(losingState);
						aNode.child_nodes.add(losingTemp);
						return;
					}
				}
			}
			if(aNode.pState.one_away_left_right_cross) {
				for(int i = 0; i < 3; i++) {
					// we have found the winning move for the opponent.
					// this is the only move we will generate as the child node
					if(aNode.pState.game[i][i] == -1) {
						State losingState = new State(aNode.pState,i,i);
						losingState.endStateTest();
						sNode losingTemp = new sNode(losingState);
						aNode.child_nodes.add(losingTemp);
						return;
					}
				}
			}
			if(aNode.pState.one_away_right_left_cross) {
				for(int i = 0; i < 3; i++) {
					int j = 2;
					// we have found the winning move for the opponent.
					// this is the only move we will generate as the child node
					if(aNode.pState.game[i][j] == -1) {
						State losingState = new State(aNode.pState,i,j);
						losingState.endStateTest();
						sNode losingTemp = new sNode(losingState);
						aNode.child_nodes.add(losingTemp);
						return;
					}
					j--;
				}
			}
			
		}
		//search for -1 value in the game double array of the current state to know what 
		//next state to generate, store them in a node and append that node to the child nodes
		for(int i = 0; i < 3; i++) {
			for(int j=0; j < 3; j++) {
				if(aNode.pState.game[i][j] == -1) {
					State nextState = new State(aNode.pState,i,j);
					//change the gameStatus value of this new state.
					nextState.endStateTest();
					sNode temp = new sNode(nextState);
					aNode.child_nodes.add(temp);
				}
			}
		}
		//generate the child nodes of each child node
		for(int k = 0; k < aNode.child_nodes.size(); k++) {
			generateSearchTree(aNode.child_nodes.get(k));
		}
	}
	
	/**
	 * Function that recursively computes the probabilities of winning of each child nodes.
	 * This function will determine a winning percentage value for every state inside the search tree. 
	 */
	
	public void computeProbabilities(sNode aNode) {
		for(int i =0; i < aNode.child_nodes.size(); i++) {
			sNode tempNode = aNode.child_nodes.get(i);
			if(tempNode.child_nodes.size() > 0) {
				computeProbabilities(tempNode);
			}
			// being careful to ignore the ties in computing the win %
			if(tempNode.pState.gameStatus == State.GameStatus.Win) {
				tempNode.pState.win_percent = 1.0;
			}
			else if(tempNode.pState.gameStatus == State.GameStatus.Loss) {
				tempNode.pState.win_percent = 0.0;
			}
			else if(tempNode.pState.gameStatus == State.GameStatus.Nothing) {
				// incrementing a temporary win percentage
				double temp_win_percent = 0.0;
				// keeping track of the number of non-tie states
				// but being careful of division by zero
				int num_of_non_ties = 0;
				for(int j = 0; j < tempNode.child_nodes.size(); j++) {
					if(tempNode.pState.gameStatus != State.GameStatus.Tie) {
						temp_win_percent += tempNode.child_nodes.get(j).pState.win_percent;
						num_of_non_ties++;
					}
				}
				if(num_of_non_ties != 0) {
					tempNode.pState.win_percent = temp_win_percent/ (double) num_of_non_ties;
				}else {
					tempNode.pState.win_percent = 0.0;
				}
				//System.out.println(tempNode.pState.win_percent);
			}
		}
	}
	
	/**
	 * This function picks out the highest win percentage out of all the possible immediate child of 
	 * the current game state, i.e. it picks out the next play.
	 */	
	
	public sNode pickOptimalPlay(sNode aNode) {
		sNode recordNode = null;
		for(int i = 0; i < aNode.child_nodes.size(); i++) {
			System.out.println("Winning percentage : " + aNode.child_nodes.get(i).pState.win_percent);
			sNode currentNode = aNode.child_nodes.get(i);
			currentNode.pState.stateDisplay();
			if(recordNode == null) {
				recordNode = currentNode;
			}else {
				if(recordNode.pState.win_percent < currentNode.pState.win_percent) {
					recordNode = currentNode;
				}
				if(recordNode.pState.win_percent == 0 
						&& recordNode.pState.win_percent == currentNode.pState.win_percent)
				{
					if(aNode.pState.general_one_away) {
						recordNode = currentNode;
					}
				}
			}
		}
		return recordNode;
	}
	
}
