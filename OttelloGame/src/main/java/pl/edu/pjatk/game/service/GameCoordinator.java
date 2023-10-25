package pl.edu.pjatk.game.service;

import static pl.edu.pjatk.game.Othello.*;

public class GameCoordinator {

    public static void playerTurn(int[] move, BoardService boardService) {
        if (isValidMove(move[0], move[1])) {
            placePiece(move[0], move[1]);
            if (checkForWin()) {
                boardService.displayBoard();
                System.out.println("Player " + currentPlayer + " wins!");
                isGameOver = true;
            } else {
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            }
        } else {
//            System.out.println("Invalid move. Try again.");
        }
    }

    /**
     * function checks for
     *
     * @param row given row index
     * @param col given column index
     * @return true if move is valid, false if not
     */
    public static boolean isValidMove(int row, int col) {
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE || board[row][col] != ' ') {
            return false;
        }
        return canFlip(row, col);
    }

    /**
     * Based on opponent pieces,
     * search for those which can be flipped by initial row and column indexes
     * and possible movements within the game
     *
     * @param row given row index
     * @param col given column index
     * @return true if it can be flipped, false if not
     */
    public static boolean canFlip(int row, int col) {
        char opponent = (currentPlayer == 'X') ? 'O' : 'X';

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (isDrDcEqualZero(dr, dc)) continue;

                int r = row + dr;
                int c = col + dc;
                boolean valid = false;

                while (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE) {
                    if (board[r][c] == ' ') break;
                    if (board[r][c] == opponent) {
                        r += dr;
                        c += dc;
                    } else if (board[r][c] == currentPlayer) {
                        valid = true;
                        break;
                    }
                }

                if (valid) return true;
            }
        }

        return false;
    }

    /**
     * 1. Make player movement
     * by given current row and column index, peek
     * current played shape and select opponent based on opposite shape to it
     * 2.  Go Through each possible movement iteration
     * 3. Check sequence of opponent pieces for given direction
     * 4. Flip opponents pieces to current player favor
     *
     * @param row given row index
     * @param col given column index
     */
    public static void placePiece(int row, int col) {
        board[row][col] = currentPlayer;
        char opponent = (currentPlayer == 'X') ? 'O' : 'X';

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (isDrDcEqualZero(dr, dc)) continue;

                int r = row + dr;
                int c = col + dc;
                boolean flipped = false;

                while (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE) {
                    if (board[r][c] == ' ') break;
                    if (board[r][c] == opponent) {
                        r += dr;
                        c += dc;
                    } else if (board[r][c] == currentPlayer) {
                        flipped = true;
                        break;
                    }
                }

                if (flipped) {
                    r = row + dr;
                    c = col + dc;
                    while (board[r][c] == opponent) {
                        board[r][c] = currentPlayer;
                        r += dr;
                        c += dc;
                    }
                }
            }
        }
    }

    /**
     * Check if condition for player win has been met
     *
     * @return true if win , false if not
     */
    public static boolean checkForWin() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isDrDcEqualZero(int dr, int dc) {
        return dr == 0 && dc == 0;
    }
}
