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
                if (checkWin()) {
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

    public boolean checkWin() {
        char[][] winConditions = {
                {'X', 'X', 'X'}, {'X', 'X', 'X'}, {'X', 'X', 'X'},
                {'X', 'X', 'X'}, {'X', 'X', 'X'}, {'X', 'X', 'X'},
                {'X', 'X', 'X'}, {'X', 'X', 'X'}
        };
        for (char[] condition : winConditions) {
            if (isEqual(condition, board)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEqual(char[] arr1, char[] arr2) {
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i] && arr1[i] != ' ') {
                return false;
            }
        }
        return true;
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
            System.out.print(board[i] + " ");
            if (i % 3 == 2) {
                System.out.println();
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Player player1 = new HumanPlayer('X');
        Player player2 = new AIPlayer('O', new SimpleAI());
        TicTacToe game = new TicTacToe(player1, player2);
        game.play();
    }
}