package MemoryMatchingGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryMatchingGame {
    private int[][] board; // 4x4 grid for cards
    private boolean[][] revealed; // To keep track of revealed cards
    private int numAttempts; // Number of attempts made by the player
    private int numMatches; // Number of card pairs matched
    private int totalPairs; // Total pairs of cards in the game
    private boolean gameWon; // Flag to indicate if the game is won

    public MemoryMatchingGame() {
        totalPairs = 8; // 4x4 cards, 8 pairs

        board = new int[4][4];
        revealed = new boolean[4][4];
        numAttempts = 0;
        numMatches = 0;
        gameWon = false;

        initializeBoard();
    }

    private void initializeBoard() {
        // Create a list of card values (numbers and letters)
        List<Integer> cardValues = new ArrayList<>();
        for (int i = 1; i <= totalPairs; i++) {
            cardValues.add(i);
            cardValues.add(i);
        }
        Collections.shuffle(cardValues);

        // Assign card values to the board randomly
        int index = 0;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                board[row][col] = cardValues.get(index);
                index++;
            }
        }
    }

    public boolean isMatched(int row, int col) {
        // Check if the card at the given position is already matched
        return revealed[row][col];
    }

    public int getCardValue(int row, int col) {
        return board[row][col]; // Return the value of the card at the specified position
    }

    public int getNumRevealed() {
        // Get the number of currently revealed cards
        int count = 0;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (revealed[row][col]) {
                    count++;
                }
            }
        }
        return count;
    }

    public int[] getRevealedCards() {
        // Get the positions of the currently revealed cards
        int[] revealedCards = new int[2];
        int index = 0;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (revealed[row][col]) {
                    revealedCards[index++] = board[row][col];
                }
            }
        }
        return revealedCards;
    }

    public boolean checkMatch() {
        // Check if the two revealed cards match
        int[] revealedCards = getRevealedCards();
        return revealedCards[0] == revealedCards[1];
    }

    public void resetGame() {
        numAttempts = 0;
        numMatches = 0;
        gameWon = false;

        initializeBoard();
    }

    public boolean isGameOver() {
        // Check if the game is over (all pairs matched or attempts exhausted)
        if (numMatches == totalPairs || numAttempts >= 40) {
            gameWon = true;
            return true;
        }
        return false;
    }

    public boolean chooseCard(int row, int col) {
        // Handle player's card selection and check for matches
        if (revealed[row][col] || isGameOver()) {
            return false; // Ignore clicks on revealed cards or after the game is over
        }

        revealed[row][col] = true;
        numAttempts++;

        if (getNumRevealed() == 2) {
            if (checkMatch()) {
                numMatches++;
                if (numMatches == totalPairs) {
                    gameWon = true;
                }
            } else {
                // Implement logic to flip unmatched cards back after a delay
            }
        }

        return true;
    }

    public boolean hasWon() {
        return gameWon;
    }
}