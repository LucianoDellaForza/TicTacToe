package com.lukakrivacevic;

import java.util.*;

public class Main {

    static char[][] grid;
    static List<List> winConditions;

    static Player player1;
    static Player player2;

    static boolean turn;
    static boolean gameOver;

    public static void main(String[] args) {
        startConfiguration();
        randomizeStartTurn();

        while (true) {
            if (!gameOver) {
                if (turn) {
                    play(player1);
                    if (player1.hasResigned()) {
                        System.out.println(player2.getName() + " has won.");
                        gameOver = true;
                    }
                    if (didIWin(player1)) {
                        System.out.println(player1.getName() + " has won.");
                        gameOver = true;
                    }
                    turn = !turn;
                } else {
                    play(player2);
                    if (player2.hasResigned()) {
                        System.out.println(player1.getName() + " has won.");
                        gameOver = true;
                    }
                    if (didIWin(player2)) {
                        System.out.println(player2.getName() + " has won.");
                        gameOver = true;
                    }
                    turn = !turn;
                }
                //grid has been filled without winner
                if (!gameOver && player1.markedPositions.size() + player2.markedPositions.size() == 9) {
                    System.out.println("It's a draw.");
                    gameOver = true;
                }

                // debug
//                System.out.print("player1 positions: ");
//                for (Integer el : player1.markedPositions) {
//                    System.out.print(el + " ");
//                }
//                System.out.println("");
//                System.out.print("player2 positions: ");
//                for (Integer el : player2.markedPositions) {
//                    System.out.print(el + " ");
//                }
//                System.out.println("");

            } else {
                System.out.println("Play a new game? (y/n)");
                Scanner sc = new Scanner(System.in);
                if (sc.nextLine().toLowerCase().equals("y")) {
                    startConfiguration();
                    randomizeStartTurn();
                    gameOver = false;
                } else {
                    break;
                }
            }
        }

        System.out.println("Game over - Thanks for playing!");
    }

    public static void printGrid() {
        for (char[] row : grid) {
            for (char c : row) {
                System.out.print(c);
            }
            System.out.println();
        }
    }

    public static void startConfiguration() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter player 1 name:");
        String name = scanner.nextLine();
        player1 = new Player(name);
        System.out.println("Enter player 2 name: (type 'AI' to play against computer)");
        name = scanner.nextLine();
        if (name.equals("AI"))
            name = name.toLowerCase();
        player2 = new Player(name);

        setupGridAndWinConditions();
    }

    public static void setupGridAndWinConditions() {
        grid = new char[][]{{' ', ' ', ' ', ' ', '1', ' ', ' ', ' ', '2', ' ', ' ', ' ', '3', ' ', ' ',},
                {' ', ' ', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~',},
                {'1', ' ', '|', ' ', ' ', ' ', '|', ' ', ' ', ' ', '|', ' ', ' ', ' ', '|',},
                {' ', ' ', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~',},
                {'2', ' ', '|', ' ', ' ', ' ', '|', ' ', ' ', ' ', '|', ' ', ' ', ' ', '|',},
                {' ', ' ', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~',},
                {'3', ' ', '|', ' ', ' ', ' ', '|', ' ', ' ', ' ', '|', ' ', ' ', ' ', '|',},
                {' ', ' ', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~',}
        };

        List horizTop = Arrays.asList(0, 1, 2);
        List horizMid = Arrays.asList(3, 4, 5);
        List horizBot = Arrays.asList(6, 7, 8);
        List verticLeft = Arrays.asList(0, 3, 6);
        List verticMid = Arrays.asList(1, 4, 7);
        List verticRight = Arrays.asList(2, 5, 8);
        List diagonalOne = Arrays.asList(0, 4, 8);
        List diagonalTwo = Arrays.asList(2, 4, 6);
        winConditions = new ArrayList<>();
        winConditions.add(horizTop);
        winConditions.add(horizMid);
        winConditions.add(horizBot);
        winConditions.add(verticLeft);
        winConditions.add(verticMid);
        winConditions.add(verticRight);
        winConditions.add(diagonalOne);
        winConditions.add(diagonalTwo);
    }

    private static void randomizeStartTurn() {
        Random random = new Random();
        turn = random.nextBoolean();
        if (turn) {
            player1.setPlayerNumber(1);
            player2.setPlayerNumber(2);
        } else {
            player1.setPlayerNumber(2);
            player2.setPlayerNumber(1);
        }
    }

    public static void play(Player player) {
        char mark = ' ';
        mark = (player.getPlayerNumber() == 1) ? 'X' : 'O';

        int position = -1;
        if (player.getName().equals("ai")) { // if AI is playing
            position = bestPlace();
        } else {
            System.out.println(player.getName() + " plays next: ");
            Scanner sc = new Scanner(System.in);
            while ((position = getInput(sc.nextLine(), player)) == -1) { }
            if (position == -2) //player resigned
                return;
        }

        player.markedPositions.add(position);
        //mark on grid
        switch (position) {
            case 0:
                grid[2][4] = mark;
                break;
            case 1:
                grid[2][8] = mark;
                break;
            case 2:
                grid[2][12] = mark;
                break;
            case 3:
                grid[4][4] = mark;
                break;
            case 4:
                grid[4][8] = mark;
                break;
            case 5:
                grid[4][12] = mark;
                break;
            case 6:
                grid[6][4] = mark;
                break;
            case 7:
                grid[6][8] = mark;
                break;
            case 8:
                grid[6][12] = mark;
                break;
            default:
                break;
        }
        printGrid();
    }

     //1) if resigned, 2) syntax of input, 3) boundaries, 4) is position marked already
    public static int getInput(String input, Player player) {
        int position = -1;
        if (input.toLowerCase().equals("resign")) {
            player.setResigned(true);
            position = -2;
        } else if (input.length() != 3 || input.charAt(1) != ' ' ||
                (input.charAt(0) - '0') < 0 || (input.charAt(0) - '0') > 9 || (input.charAt(2) - '0' < 0) || (input.charAt(2) - '0') > 9) {
            System.out.println("Invalid input: you must enter the x and y coordinates separated by spaces");
        } else {
            input = input.replaceAll("[^0-9]", "");
            Integer inputInt = Integer.parseInt(input);
            switch (inputInt) {
                case 11:
                    position = 0;
                    break;
                case 21:
                    position = 1;
                    break;
                case 31:
                    position = 2;
                    break;
                case 12:
                    position = 3;
                    break;
                case 22:
                    position = 4;
                    break;
                case 32:
                    position = 5;
                    break;
                case 13:
                    position = 6;
                    break;
                case 23:
                    position = 7;
                    break;
                case 33:
                    position = 8;
                    break;
                default:
                    System.out.println("Invalid input: those coordinates are outside the playable area");
                    position = -1;
                    break;
            }
        }
        if(player1.markedPositions.contains(position) || player2.markedPositions.contains(position)) {
            System.out.println("Invalid input: that space is already taken");
            position = -1;
        }
        return position;
    }

    public static boolean didIWin(Player player) {
        for (List tempList : winConditions) {
            if (player.markedPositions.containsAll(tempList))
                return true;
        }
        return false;
    }

    public static int bestPlace() {
        //first AI move -> grab center or top left corner (if center taken)
        if (player2.markedPositions.size() == 0) {
            if (!player1.markedPositions.contains(4)) {
                return 4;
            } else {
                return 0;
            }
        }

        //specific case
        if (player1.markedPositions.size() == 2 && player1.markedPositions.get(0) == 4 && player1.markedPositions.get(1) == 8)
            return 2;

        //attack first - try to win
        List<Integer> attackList = new ArrayList<>(player2.markedPositions);
        for (int i = 0; i < 9; i++) {
            attackList.add(i);
            for (List tempList : winConditions) {
                if(attackList.containsAll(tempList) && !player1.markedPositions.contains(i) && !player2.markedPositions.contains(i)) {
//                    attackList.remove(attackList.size()-1);
                    return i;
                }
            }
            attackList.remove(attackList.size()-1);
        }

        //defend
        List<Integer> defendList = new ArrayList<>(player1.markedPositions);
        for (int i = 0; i < 9; i++) {
            defendList.add(i);
            for (List tempList : winConditions) {
                if (defendList.containsAll(tempList) && !player1.markedPositions.contains(i) && !player2.markedPositions.contains(i)) {
//                    defendList.remove(defendList.size()-1);
                    return i;
                }
            }
            defendList.remove(defendList.size()-1);
        }

        //if neither -> random position
        for (int i = 0; i < 9; i++) {
            if (!player1.markedPositions.contains(i) && !player2.markedPositions.contains(i)) {
                return i;
            }
        }

        return -1; //never reached
    }

}
