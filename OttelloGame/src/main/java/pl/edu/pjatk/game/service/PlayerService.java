package pl.edu.pjatk.game.service;

import java.util.Scanner;

import static pl.edu.pjatk.game.Othello.currentPlayer;

public class PlayerService {
    /**
     * Get player move coordinates in form of two column indexes given in two separate
     * command inputs
     *
     * @return int set of arguments representing coordinates of player movement
     */
    public int[] getPlayerMove() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Player " + currentPlayer + ", enter your move (row col): ");
        int row = scanner.nextInt();
        int col = scanner.nextInt();
        return new int[]{row, col};
    }
}
