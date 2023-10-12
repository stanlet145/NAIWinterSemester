package pl.edu.pjatk.game.minmax;

import static pl.edu.pjatk.game.Othello.*;
import static pl.edu.pjatk.game.service.GameCoordinator.isValidMove;
import static pl.edu.pjatk.game.service.GameCoordinator.placePiece;

public class MinmaxImplementation {
    private static final int MAX_DEPTH = 6;

    /**
     * Get AiMove based on MinMax algorithm evaluation
     * @return int[] set of arguments for Ai move
     */
    public static int[] getAIMove() {
        return minimax(board, currentPlayer);
    }

    /**
     * Implements the Minimax algorithm to find the best move on the game board for the given player.
     *
     * @param board  The current game board.
     * @param player The player for whom to find the best move ('X' or 'O').
     *               MAX_DEPTH depth The maximum depth to explore in the game tree (controls AI's lookahead).
     * @return The best move as an array of two integers: [row, col].
     */
    private static int[] minimax(char[][] board, char player) {
        int bestScore = (player == 'X') ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int[] bestMove = {-1, -1};

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (isValidMove(row, col)) {
                    char[][] newBoard = copyBoard(board);
                    placePiece(row, col);

                    int score = minimaxHelper(newBoard, player, MinmaxImplementation.MAX_DEPTH - 1, player);

                    if ((player == 'X' && score > bestScore) || (player == 'O' && score < bestScore)) {
                        bestScore = score;
                        bestMove[0] = row;
                        bestMove[1] = col;
                    }
                }
            }
        }

        return bestMove;
    }
    /**
     * Recursively implements the Minimax algorithm to evaluate the game board for the given player.
     *
     * @param board The current game board represented as a 2D array of characters.
     * @param currentPlayer The player ('X' or 'O) currently making the move.
     * @param depth The maximum depth to explore in the game tree (controls AI's lookahead).
     * @param maximizingPlayer The player for whom to maximize the score (usually the AI player).
     * @return The best evaluation score for the current board state.
     */
    private static int minimaxHelper(char[][] board, char currentPlayer, int depth, char maximizingPlayer) {
        if (depth == 0 || isGameOver) {
            return evaluateBoard(board, maximizingPlayer);
        }

        int bestScore = (currentPlayer == 'X') ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (isValidMove(row, col)) {
                    char[][] newBoard = copyBoard(board);
                    placePiece(row, col);

                    int score = minimaxHelper(newBoard, (currentPlayer == 'X') ? 'O' : 'X', depth - 1, maximizingPlayer);

                    if (currentPlayer == maximizingPlayer) {
                        bestScore = Math.max(bestScore, score);
                    } else {
                        bestScore = Math.min(bestScore, score);
                    }
                }
            }
        }

        return bestScore;
    }
    /**
     * Creates a deep copy of the given game board represented as a 2D array of characters.
     *
     * @param board The original game board to be copied.
     * @return A new 2D array representing an identical copy of the original board.
     */
    private static char[][] copyBoard(char[][] board) {
        char[][] newBoard = new char[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, BOARD_SIZE);
        }
        return newBoard;
    }
    /**
     * Evaluates the current state of the game board for the given player.
     *
     * @param board The game board represented as a 2D array of characters.
     * @param player The player ('X' or 'O') for whom the board should be evaluated.
     * @return An integer representing the evaluation score for the current board state.
     *         Positive values indicate a favorable position for the player, and negative
     *         values indicate a favorable position for the opponent.
     */
    private static int evaluateBoard(char[][] board, char player) {
        int playerScore = 0;
        int opponentScore = 0;

        // Values for board positions (adjust according to your strategy)
        int[][] positionValues = {
                {100, -20, 10, 5, 5, 10, -20, 100},
                {-20, -50, -2, -2, -2, -2, -50, -20},
                {10, -2, -1, -1, -1, -1, -2, 10},
                {5, -2, -1, -1, -1, -1, -2, 5},
                {5, -2, -1, -1, -1, -1, -2, 5},
                {10, -2, -1, -1, -1, -1, -2, 10},
                {-20, -50, -2, -2, -2, -2, -50, -20},
                {100, -20, 10, 5, 5, 10, -20, 100}
        };

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == player) {
                    playerScore += positionValues[row][col];
                } else if (board[row][col] != ' ') {
                    opponentScore += positionValues[row][col];
                }
            }
        }

        return playerScore - opponentScore;
    }
}
