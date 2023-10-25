package pl.edu.pjatk.game.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.edu.pjatk.game.model.Board;

@Getter
@AllArgsConstructor
public class BoardService {
    private final Board board;
    private char[][] gameBoard;

    /**
     * initialize game board for Othello with starting points for
     * Black and White team in the middle of the board
     */
    public void initializeBoard() {
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.size(); j++) {
                gameBoard[i][j] = ' ';
            }
        }
        gameBoard[3][3] = 'X';
        gameBoard[3][4] = 'O';
        gameBoard[4][3] = 'O';
        gameBoard[4][4] = 'X';
    }

    /**
     * Game Board preview for console output
     * Function goes for all game rows
     * and prints current state of the rows
     */
    public void displayBoard() {
        System.out.println("  0 1 2 3 4 5 6 7");
        for (int i = 0; i < board.size(); i++) {
            System.out.print(i + " ");
            for (int j = 0; j < board.size(); j++) {
                System.out.print(gameBoard[i][j] + " ");
            }
            System.out.println();
        }
    }
}
