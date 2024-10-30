// (c) 2024 Roland Labana

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

interface Player {
    char getSymbol();

    void makeMove(TicTacToe game);
}

class HumanPlayer implements Player {
    private char symbol;

    public HumanPlayer(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public char getSymbol() {
        return symbol;
    }

    //todo - disallow move outside board bounds to avoid runtime error
    @Override
    public void makeMove(TicTacToe game) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter your move (" + symbol + "): ");
            int move = scanner.nextInt();
            if (game.isValidMove(move)) {
                game.makeMove(move, symbol);
                break;
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }
    }
}

class AIPlayer implements Player {
    private char symbol;
    private AI strategy;

    public AIPlayer(char symbol, AI strategy) {
        this.symbol = symbol;
        this.strategy = strategy;
    }

    @Override
    public char getSymbol() {
        return symbol;
    }

    @Override
    public void makeMove(TicTacToe game) {
        System.out.println(symbol + "'s AI is thinking...");
        int move = strategy.determineMove(game);
        game.makeMove(move, symbol);
    }
}

interface AI {
    int determineMove(TicTacToe game);
}

// This simply picks the next open square
class SimpleAI implements AI {
    @Override
    public int determineMove(TicTacToe game) {
        for (int i = 0; i < 9; i++) {
            if (game.isValidMove(i)) {
                return i;
            }
        }
        return -1; // No valid move
    }
}

class RandomAI implements AI {
    private Random random = new Random();

    @Override
    public int determineMove(TicTacToe game) {
        List<Integer> possibleMoves = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (game.isValidMove(i)) {
                possibleMoves.add(i);
            }
        }

        if (possibleMoves.isEmpty()) {
            return -1; // No valid moves
        }

        int randomIndex = random.nextInt(possibleMoves.size());
        return possibleMoves.get(randomIndex);
    }
}

class TicTacToe {
    private char[] board;
    private List<Player> players;

    public TicTacToe(Player player1, Player player2) {
        board = new char[9];
        for (int i = 0; i < 9; i++) {
            board[i] = ' ';
        }
        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
    }

    public void play() {
        while (true) {
            for (Player player : players) {
                displayBoard();
                player.makeMove(this);
                if (checkWin(board)) {
                    displayBoard();
                    System.out.println(player.getSymbol() + " wins!");
                    return;
                }
                if (isBoardFull()) {
                    displayBoard();
                    System.out.println("It's a draw!");
                    return;
                }
            }
        }
    }

    public boolean isValidMove(int move) {
        return board[move] == ' ' && move >= 0 && move <= 8;
    }

    public void makeMove(int move, char symbol) {
        board[move] = symbol;
    }

public boolean checkWin(char[]theBoard) {
    // Check rows
    for (int i = 0; i < 3; i++) {
        if (theBoard[i * 3] == theBoard[i * 3 + 1] && theBoard[i * 3] == theBoard[i * 3 + 2] && theBoard[i * 3] != ' ') {
            return true;
        }
    }

    // Check columns
    for (int i = 0; i < 3; i++) {
        if (theBoard[i] == theBoard[i + 3] && theBoard[i] == theBoard[i + 6] && theBoard[i] != ' ') {
            return true;
        }
    }

    // Check diagonals
    if (theBoard[0] == theBoard[4] && theBoard[0] == theBoard[8] && theBoard[0] != ' ') {
        return true;
    }
    if (theBoard[2] == theBoard[4] && theBoard[2] == theBoard[6] && theBoard[2] != ' ') {
        return true;
    }

    return false;
}

    public boolean isBoardFull() {
        for (char c : board) {
            if (c == ' ') {
                return false;
            }
        }
        return true;
    }

    public void displayBoard() {
    
        for (int i = 0; i < 9; i++) {
            System.out.print(" " + board[i] + " ");
            if (i % 3 == 2) {
                System.out.println();
                if (i != 8) {
                    System.out.println("-----------");
                }
            } else {
                System.out.print("|");
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Player player1 = new HumanPlayer('X');
        //Player player2 = new HumanPlayer('X');
        Player player2 = new AIPlayer('O', new SimpleAI());   // ie: "Jim-AI"
        //Player player2 = new AIPlayer('X', new RandomAI());
        TicTacToe game = new TicTacToe(player1, player2);
        game.play();
    }
}