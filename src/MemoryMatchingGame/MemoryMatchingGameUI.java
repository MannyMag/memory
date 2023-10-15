package MemoryMatchingGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.awt.Component;
public class MemoryMatchingGameUI {
    private MemoryMatchingGame game;
    private JFrame frame;
    private JPanel cardPanel;
    private JLabel timerLabel;
    private JButton restartButton;
    private Timer gameTimer;
    private int elapsedTime;

    // Define class variables to keep track of the selected cards
    private int firstCardRow = -1;
    private int firstCardCol = -1;
    private int secondCardRow = -1;
    private int secondCardCol = -1;

    public MemoryMatchingGameUI() {
        game = new MemoryMatchingGame();
        frame = new JFrame("Memory Matching Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 600));
        Color backgroundColor = Color.GRAY;
        frame.getContentPane().setBackground(backgroundColor);

        setupGame();

        frame.pack();
        frame.setVisible(true);
    }
    private void setupGame() {
        // Initialize the game by setting up the card panel
        initializeCardPanel();
        initializeTimer();
        initializeRestartButton();
        startGameTimer(); // Start the game timer
    }

    // Initialize the timer with a delay of 1000ms (1 second)
    private void initializeTimer() {
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++; // Increment elapsed time in seconds
                updateTimerLabel(); // Update the timer label
            }
        });
        gameTimer.start();
    }

    // Start the timer
    private void startGameTimer () {
        gameTimer.start();
    }
    private void initializeCardPanel() {
        cardPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        frame.add(cardPanel, BorderLayout.CENTER);

        // Create card labels
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                createCardLabel(row, col);
            }
        }

        enableCardInteractions(true);  // Enable card interactions
    }

    private void createCardLabel(int row, int col) {
        JLabel cardLabel = new JLabel();
        cardLabel.setPreferredSize(new Dimension(80, 80));
        cardLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardLabel.setOpaque(true);
        cardLabel.setBackground(Color.GREEN);
        cardLabel.setForeground(Color.BLACK);
        cardLabel.setFont(new Font("Arial", Font.BOLD, 20));

        cardLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLabelClicked(row, col);
            }
        });

        cardPanel.add(cardLabel);
    }

    private void cardLabelClicked(int row, int col) {
        if (game.isGameOver()) {
            return; // Game over, ignore clicks
        }

        if (game.chooseCard(row, col)) {
            // Handle successful card selection (e.g., update UI)
            updateCardUI(row, col);
        } else {
            // Handle unsuccessful card selection (e.g., flip cards back)
            // Implement logic to flip unmatched cards back after a delay
            if (firstCardRow == -1) {
                // First card selected
                firstCardRow = row;
                firstCardCol = col;
            } else {
                // Second card selected
                secondCardRow = row;
                secondCardCol = col;

                // Disable card interactions temporarily
                enableCardInteractions(false);

                // Implement logic to flip unmatched cards back after a delay using a Timer
                Timer flipBackTimer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Code to flip back unmatched cards (based on firstCard and secondCard)
                        // ...
                        flipUnmatchedCardsBack(); // Call the method to flip back unmatched cards
                    }
                });
                flipBackTimer.setRepeats(false); // Execute only once
                flipBackTimer.start();
            }
        }

        if (game.isGameOver()) {
            endGame();
        }
    }

    private void flipUnmatchedCardsBack() {
        // Access the first card label
        JLabel firstCardLabel = (JLabel) cardPanel.getComponent(firstCardRow * 4 + firstCardCol);

        // Access the second card label
        JLabel secondCardLabel = (JLabel) cardPanel.getComponent(secondCardRow * 4 + secondCardCol);

        // Clear the text to hide the values
        firstCardLabel.setText("");
        secondCardLabel.setText("");

        // Reset the background color (you can set your preferred color)
        firstCardLabel.setBackground(Color.GREEN);
        secondCardLabel.setBackground(Color.GREEN);

        // Clear the first and second card positions
        firstCardRow = -1;
        firstCardCol = -1;
        secondCardRow = -1;
        secondCardCol = -1;

        enableCardInteractions(true); // Re-enable card interactions
    }

    private void updateCardUI(int row, int col) {
        // Implement logic to update card appearance based on the game state
        // For example, reveal card value and change background color
        if (game.hasWon()) {
            // Game has been won, display a win message or perform other actions
            // For example, you can set a win message as the card's text
            ((JLabel) cardPanel.getComponent(row * 4 + col)).setText("Matched!");
            // Change the background color of matched cards
            cardPanel.getComponent(row * 4 + col).setBackground(new Color(201, 174, 49));
            return; // No further updates needed
        }

        // Access the card label that was clicked
        JLabel cardLabel = (JLabel) cardPanel.getComponent(row * 4 + col);

        // Get the value of the card at the clicked position
        int cardValue = game.getCardValue(row, col); // Replace this with the appropriate method from your MemoryMatchingGame class

        // Display the card's value on the label
        cardLabel.setText(String.valueOf(cardValue));

        // Check if the card is matched and customize appearance accordingly
        if (game.isMatched(row, col)) {
            // For a matched card, set the background color to gold or a similar color
            cardLabel.setBackground(new Color(201, 174, 49));  // Gold color
        } else {
            cardLabel.setText("");
            // For unmatched cards, you can set a different background color (e.g., green)
            cardLabel.setBackground(Color.GREEN);
        }
    }

    private void updateTimerLabel() {
        int minutes = elapsedTime / 60;
        int seconds = elapsedTime % 60;
        timerLabel.setText("Time: " + minutes + ":" + String.format("%02d", seconds));
    }


    private void initializeRestartButton() {
        restartButton = new JButton("Restart");
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(restartButton);
        frame.add(buttonPanel, BorderLayout.WEST);
    }

    private void restartGame() {
        // Implement logic to restart the game
        // Reset game state and UI
        game.resetGame();

        // Reset timer and elapsed time
        elapsedTime = 0;
        updateTimerLabel();

        // Shuffle card positions
        shuffleCardPositions();

        // Flip all cards back over to hide their values
        flipAllCards(false);

        // Enable card interactions
        enableCardInteractions(true);
    }

    private void shuffleCardPositions() {
        Component[] cards = cardPanel.getComponents();

        List<Component> cardList = Arrays.asList(cards);
        Collections.shuffle(cardList);

        cardPanel.removeAll();

        for (Component card : cardList) {
            cardPanel.add(card);
        }

        cardPanel.validate();
        cardPanel.repaint();
    }

    private void flipAllCards( boolean show) {
        Component[] cards = cardPanel.getComponents();

        for (Component card : cards) {
            JLabel cardLabel = (JLabel) card;
            if (show) {
                cardLabel.setText("");  // Clear the card value
            }else {
                cardLabel.setText(" "); // Display a space to cover the cards
            }
            cardLabel.setBackground(Color.GREEN);  // Reset the background color
        }
    }

    private void enableCardInteractions(boolean enable) {
        Component[] cards = cardPanel.getComponents();

        for (Component card : cards) {
            card.setEnabled(enable);
        }
    }


    private void endGame() {
        // Implement logic to display game result (win/lose) and elapsed time
        // Update UI with "You win!" or "You lose!" message and game duration
        if (game.hasWon()) {
            timerLabel.setText("You win! Time:" + elapsedTime / 60 + " minutes" + elapsedTime % 60 + " seconds");
        } else {
            timerLabel.setText("You lose! Time:" + elapsedTime / 60 + " minutes" + elapsedTime % 60 + " seconds");
        }

        enableCardInteractions(false);  // Disable card interactions

        // Stop the game timer
        gameTimer.stop();

        // Gracefully exit the application
        System.exit(0);
    }

}