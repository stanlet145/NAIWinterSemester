package pl.edu.pjatk.game;

import pl.edu.pjatk.game.minmax.MinmaxImplementation;
import pl.edu.pjatk.game.model.Board;
import pl.edu.pjatk.game.service.BoardService;
import pl.edu.pjatk.game.service.PlayerService;

import static pl.edu.pjatk.game.service.GameCoordinator.*;

public class Othello {
    /**
     * initialize board static data
     */
    public static final int BOARD_SIZE = 8;
    public static final Board boardData = new Board(BOARD_SIZE);
    public static char[][] board = new char[boardData.size()][boardData.size()];
    /**
     * give current player assignment of X char
     */
    public static char currentPlayer = 'X';
    public static boolean isGameOver = false;


    /**
     * Initialize game board state and while game over condition
     * play the game for two players
     * Player make move
     * switch turns
     * Ai make move
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        BoardService boardService = new BoardService(boardData, board);
        boardService.initializeBoard();
        PlayerService playerService = new PlayerService();
        boolean playerTurn = true;
        while (!isGameOver) {
            if (playerTurn) {
                boardService.displayBoard();
                int[] move = playerService.getPlayerMove();
                playerTurn(move, boardService);
            } else {
                int[] aiMove = MinmaxImplementation.getAIMove();
                playerTurn(aiMove, boardService);
            }
            playerTurn = !playerTurn;
        }
    }
}
