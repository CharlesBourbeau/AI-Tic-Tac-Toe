
public class State {

	public int[][] game = {{-1,-1,-1},{-1,-1,-1},{-1,-1,-1}};
	public int depth;
	//type of the player that played to create this state
	public int playerType;
	// enumeration of game status
	// the one away status means that there is an available case to make a player win his next turn
	public enum GameStatus{
		Nothing, Win, Tie, Loss
	};
	public boolean general_one_away = false;
	
	public boolean one_away_top_row = false;
	public boolean one_away_mid_row = false;
	public boolean one_away_bot_row = false;
	public boolean one_away_left_col = false;
	public boolean one_away_mid_col = false;
	public boolean one_away_right_col = false;
	public boolean one_away_left_right_cross = false;
	public boolean one_away_right_left_cross = false;
	//probability of winning the game
	public double win_percent;
	
	public GameStatus gameStatus;

	// state so we can backtrack a winning state to the play to make
	public State parentState;

	// constructor for the beggining with an empty game map 
	public State(int playerType) {
		depth = 0;
		// this player type is the opposite of the player starting the game
		this.playerType = playerType;
	}
	
	// constructor for the beginning of the game
	public State(int row, int col, int type) {
		this.game[row][col] = type;
		this.playerType = type;
		this.parentState = null;
		depth = 0;
	}
	
	// constructor for a new play in game
	public State(int[][] cur_game, int type) {
		this.game = cur_game;
		this.playerType = type;
		this.parentState = null;
		depth=0;
	}
	
	// constructor for state that follows from any play
	public State(State aState, int row, int col) {
		for(int x = 0; x < 3; x++) {
			for( int y = 0; y < 3; y++) {
				this.game[x][y] = aState.game[x][y];
			}
		}
		depth = aState.depth + 1;
		parentState = aState;
		
		if(aState.playerType == 0) {
			this.playerType = 1;
		}else {
			this.playerType = 0;
		}
		
		// insuring that the case chosen is really free to play on
		if(this.game[row][col] == -1) {
			this.game[row][col] = playerType;
		}else {
			System.out.println("A player has already chosen this case in the past.\n");
		}
	}
	
	// constructor for state that follows from any play
	public State(State aState, int row, int col, int playerType) {
		for(int x = 0; x < 3; x++) {
			for( int y = 0; y < 3; y++) {
				this.game[x][y] = aState.game[x][y];
			}
		}
		depth = aState.depth + 1;
		parentState = aState;
		
		this.playerType = playerType;
		
		// insuring that the case chosen is really free to play on
		if(this.game[row][col] == -1) {
			this.game[row][col] = playerType;
		}else {
			System.out.println("A player has already chosen this case in the past.\n");
		}
	}
	
	//method that generates all possible states from any given state and return the array of these states
	public State[] generateStates() {
		int nextPlayerType;
		if(playerType == 0) {
			nextPlayerType = 1;
		}else {
			nextPlayerType = 0;
		}
		int numberOfPossibleStates = 0;
		//iterating to find the number of possible states, which will be the size of the state array
		for(int x = 0; x < 3; x++) {
			for( int y = 0; y < 3; y++) {
				if (game[x][y] == -1 ) {
					numberOfPossibleStates++;
				}
			}
		}
		// initiate state array
		State[] possibleStates = new State[numberOfPossibleStates];
		//loop again to populate the state array
		int i = 0; // position within the state array
		for(int x = 0; x < 3; x++) {
			for( int y = 0; y < 3; y++) {
				if (game[x][y] == -1 ) {
					// create the new state with the play at x,y
					State temp = new State(this, x, y);
					//temp.stateDisplay();
					possibleStates[i] = temp;
					i++;
					// is the array full?
					//if(i == numberOfPossibleStates) break;
				}
			}
		}
		return possibleStates;
		
	}

	
	// methods to test is this is a win state, a loss state or a tie state FOR THE AI !! not the human
	public void endStateTest() {
		// upper row
		if(game [0][0] != -1 && game[0][0] == game[0][1] && game[0][1] == game[0][2]) {
			if(game[0][0] == 0) {
				this.gameStatus = GameStatus.Win;
				return; 
			}else {
				this.gameStatus = GameStatus.Loss;
				return;
			}
		}
		// middle row
		if(game [1][0] != -1 && game[1][0] == game[1][1] && game[1][1] == game[1][2]) {
			if(game[1][0] == 0) {
				this.gameStatus = GameStatus.Win;
				return; 
			}else {
				this.gameStatus = GameStatus.Loss;
				return;
			}
		}
		// lower row
		if(game [2][0] != -1 && game[2][0] == game[2][1] && game[2][1] == game[2][2]) {
			if(game[2][0] == 0) {
				this.gameStatus = GameStatus.Win;
				return; 
			}else {
				this.gameStatus = GameStatus.Loss;
				return;
			}
		}
		// left col
		if(game [0][0] != -1 && game[0][0] == game[1][0] && game[1][0] == game[2][0]) {
			if(game[0][0] == 0) {
				this.gameStatus = GameStatus.Win;
				return; 
			}else {
				this.gameStatus = GameStatus.Loss;
				return;
			}
		}
		// middle col
		if(game [0][1] != -1 && game[0][1] == game[1][1] && game[1][1] == game[2][1]) {
			if(game[0][1] == 0) {
				this.gameStatus = GameStatus.Win;
				return; 
			}else {
				this.gameStatus = GameStatus.Loss;
				return;
			}
		}
		//bottom col
		if(game [0][2] != -1 && game[0][2] == game[1][2] && game[1][2] == game[2][2]) {
			if(game[0][2] == 0) {
				this.gameStatus = GameStatus.Win;
				return; 
			}else {
				this.gameStatus = GameStatus.Loss;
				return;
			}
		}
		// left to right cross
		if(game [0][0] != -1 && game[0][0] == game[1][1] && game[1][1] == game[2][2]) {
			if(game[0][0] == 0) {
				this.gameStatus = GameStatus.Win;
				return; 
			}else {
				this.gameStatus = GameStatus.Loss;
				return;
			}
		}
		// right to left cross
		if(game [0][2] != -1 && game[0][2] == game[1][1] && game[1][1] == game[2][0]) {
			if(game[0][2] == 0) {
				this.gameStatus = GameStatus.Win;
				return; 
			}else {
				this.gameStatus = GameStatus.Loss;
				return;
			}
		}
		// -------------------------------------------------------------
		// one away checks (24 possible one-away combinations)
		// for the opposite player, so only check for cases with value 1
		//--------------------------------------------------------------
		if(				// ------------------ top row
				(game[0][0] == 1 && game[0][1] == 1 && game[0][2] == -1) ||
				(game[0][0] == -1 && game[0][1] == 1 && game[0][2] == 1) ||
				(game[0][0] == 1 && game[0][1] == -1 && game[0][2] == 1) ) 
		{
				this.one_away_top_row = true;
				this.general_one_away = true;
				this.gameStatus = GameStatus.Nothing;
				return;
				
				
		}
		if(		// ------------------- mid row
				(game[1][0] == -1 && game[1][1] == 1 && game[1][2] == 1) ||
				(game[1][0] == 1 && game[1][1] == 1 && game[1][2] == -1) ||
				(game[1][0] == 1 && game[1][1] == -1 && game[1][2] == 1) ) 
		{
			this.one_away_mid_row = true;
			this.general_one_away = true;
			this.gameStatus = GameStatus.Nothing;
			return;
		}
		if(		// ------------------- bot row
				(game[2][0] == -1 && game[2][1] == 1 && game[2][2] == 1) ||
				(game[2][0] == 1 && game[2][1] == 1 && game[2][2] == -1) ||
				(game[2][0] == 1 && game[2][1] == -1 && game[2][2] == 1) ) 
		{
			this.one_away_bot_row = true;
			this.general_one_away = true;
			this.gameStatus = GameStatus.Nothing;
			return;
		}
		if(		// ------------------- left col
				(game[0][0] == -1 && game[1][0] == 1 && game[2][0] == 1) ||
				(game[0][0] == 1 && game[1][0] == 1 && game[2][0] == -1) ||
				(game[0][0] == 1 && game[1][0] == -1 && game[2][0] == 1) ) 
		{
			this.one_away_left_col = true;
			this.general_one_away = true;
			this.gameStatus = GameStatus.Nothing;
			return;
		}
		if(		// ------------------- mid col
				(game[0][1] == -1 && game[1][1] == 1 && game[2][1] == 1) ||
				(game[0][1] == 1 && game[1][1] == 1 && game[2][1] == -1) ||
				(game[0][1] == 1 && game[1][1] == -1 && game[2][1] == 1) ) 
		{
			this.one_away_mid_col = true;
			this.general_one_away = true;
			this.gameStatus = GameStatus.Nothing;
			return;
		}
		if(		// ------------------- right col
				(game[0][2] == -1 && game[1][2] == 1 && game[2][2] == 1) ||
				(game[0][2] == 1 && game[1][2] == 1 && game[2][2] == -1) ||
				(game[0][2] == 1 && game[1][2] == -1 && game[2][2] == 1) ) 
		{
			this.one_away_right_col = true;
			this.general_one_away = true;
			this.gameStatus = GameStatus.Nothing;
			return;
		}
		if(		// ------------------- left right cross
				(game[0][0] == -1 && game[1][1] == 1 && game[2][2] == 1) ||
				(game[0][0] == 1 && game[1][1] == -1 && game[2][2] == 1) ||
				(game[0][0] == 1 && game[1][1] == 1 && game[2][2] == -1) ) 
		{
			this.one_away_left_right_cross = true;
			this.general_one_away = true;
			this.gameStatus = GameStatus.Nothing;
			return;
		}
		if(		// ------------------- right left cross
				(game[0][2] == -1 && game[1][1] == 1 && game[2][0] == 1) ||
				(game[0][2] == 1 && game[1][1] == -1 && game[2][0] == 1) ||
				(game[0][2] == 1 && game[1][1] == 1 && game[2][0] == -1) ) 
		{
			this.one_away_right_left_cross = true;
			this.general_one_away = true;
			this.gameStatus = GameStatus.Nothing;
			return;
		}
		this.gameStatus = GameStatus.Tie;
		// determine if it is really a tie, if there is really no -1 in the double array
		for(int x = 0; x < 3; x++) {
			for( int y = 0; y < 3; y++) {
				if (game[x][y] == -1 ) {
					this.gameStatus = GameStatus.Nothing;
					return;
				}
			}
		}
		
	}
	
	// displaying the state in the console 
	
	public void stateDisplay() {
		System.out.println("[" + game[0][0] + "," + game[0][1] + "," + game[0][2] + "," + "]\n" 
				+ "[" + game[1][0] + "," + game[1][1] + "," + game[1][2] + "," + "]\n" + 
				"[" + game[2][0] + "," + game[2][1] + "," + game[2][2] + "," + "]\n\n");
	}
	
	
	
	
	
	
	
	
}
