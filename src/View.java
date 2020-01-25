import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;

/**
 * This class will host the GUI of the application implemented using javaFX.
 */
public class View extends Application {

    // Button that starts the game
    private Button restart_button;

    // List of labels representing cells in the GUI
    private static ArrayList<Label> grid_labels = new ArrayList<>();

    // Battlefield containing the current game
    private static Battlefield battlefield;

    // StateDataStructure holding the entire possible state tree
    private static StateDataStructure currentStateTree;

    // Boolean set to false as a prompt until the player is ready to play and presses start
    boolean playerReady = false;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Tic Tac Toe");
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10.0);
        gridPane.setHgap(10.0);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("black"), CornerRadii.EMPTY, Insets.EMPTY)));

        // create the 9 labels needed to populate the tic tac toe grid
        for(int row = 0; row < 3; row++){
            for(int col = 0; col < 3; col++) {
                Label newLabel = new Label();
                newLabel.setText("");
                newLabel.setPrefSize(100.0, 100.0);
                newLabel.setAlignment(Pos.CENTER);
                newLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), CornerRadii.EMPTY, Insets.EMPTY)));
                newLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        newLabel.setText("X");
                        int index = grid_labels.indexOf(newLabel);
                        // the index holds the information about the x and y coordinates
                        // x and y are reversed
                        int x = index / 3;
                        int y = index % 3;

                        battlefield.current_player = 1;
                        battlefield.play(x, y);

                        battlefield.current_player = 0;
                        AIPlay();

                    }
                });
                grid_labels.add(newLabel);
                gridPane.add(newLabel, col, row, 1, 1);
            }
        }

        restart_button = new Button();
        restart_button.setText("Restart");
        restart_button.setOnAction(e -> restartPressed());
        restart_button.setMinWidth(grid_labels.get(0).getPrefWidth());

        battlefield = new Battlefield();
        // AI will play if the current player is 0
        AIPlay();

        gridPane.add(restart_button, 1, 4, 1, 1);
        gridPane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.show();
        System.out.println("here");

    }

    public void restartPressed(){
        // Clear all the labels
        for(int i = 0; i < grid_labels.size(); i++) {
            Label current_label = grid_labels.get(i);
            current_label.setText("");
        }
        battlefield = new Battlefield();
        AIPlay();
        System.out.println("restart");
    }

    public static void AIPlay(){
        if (battlefield.current_player == 0){
            System.out.println("A.I. first");

            State currentState = new State(battlefield.current_game,1);
            currentState.endStateTest();
            if(currentState.gameStatus != State.GameStatus.Nothing) {
                return;
            }
            sNode currentNode = new sNode(currentState);
            currentStateTree = new StateDataStructure();
            // populate the tree with all the possible plays
            currentStateTree.generateSearchTree(currentNode);

            // compute the winning probabilities of every play
            currentStateTree.computeProbabilities(currentNode);

            sNode futureNode = currentStateTree.pickOptimalPlay(currentNode);
            futureNode.pState.stateDisplay();
            for(int i = 0 ; i < 3 ; i ++) {
                for(int j = 0 ; j < 3 ; j ++) {
                    if(battlefield.current_game[i][j] != futureNode.pState.game[i][j]){
                        int index = i * 3 + j;
                        Label label = grid_labels.get(index);
                        label.setText("O");
                        battlefield.current_game[i][j] = futureNode.pState.game[i][j];
                    }
                }
            }
        }
    }
}
