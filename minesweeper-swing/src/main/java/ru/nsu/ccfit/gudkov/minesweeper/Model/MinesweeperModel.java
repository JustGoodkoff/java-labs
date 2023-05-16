package ru.nsu.ccfit.gudkov.minesweeper.Model;

import java.util.ArrayList;

public class MinesweeperModel {
    private Cell[][] cells = new Cell[9][9];
    private int bombs = 12;
    private int maxFlags = 10;
    private int currentUsedFlags = 0;
    private int height = 9;
    private int width = 9;

    public int getCurrentOpenedCells() {
        return currentOpenedCells;
    }

    public void setCurrentOpenedCells(int currentOpenedCells) {
        this.currentOpenedCells = currentOpenedCells;
    }

    private int currentOpenedCells = 0;

    public int getMaxFlags() {
        return maxFlags;
    }

    public void setMaxFlags(int maxFlags) {
        this.maxFlags = maxFlags;
    }

    public int getCurrentUsedFlags() {
        return currentUsedFlags;
    }

    public void increaseCurrentUsedFlags() {
        this.currentUsedFlags++;
    }

    public void increaseCurrentOpenedCells() {
        this.currentOpenedCells++;
    }

    public void decreaseCurrentUsedFlags() {
        this.currentUsedFlags--;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    private GameState gameState = GameState.IN_PROGRESS;

    public int getBombs() {
        return bombs;
    }



    public Cell[][] getCells() {
        return cells;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }


    public MinesweeperModel() {

    }

    public ArrayList<Cell> getAllBombs() {
        ArrayList<Cell> bombs = new ArrayList<>();

        for(int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (cells[i][j].getContent() == CellContent.BOMB) {
                    bombs.add(cells[i][j]);
                }
            }
        }
        return bombs;
    }

    public void fillCells(int coordX, int coordY) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = new Cell(j, i, CellContent.ZERO);
            }
        }

        int bombsRemainder = bombs;

//        System.out.println(xRandCoord);
//        System.out.println(yRandCoord);
        while (bombsRemainder > 0) {
            int xRandCoord = (int) (Math.random() * 100 % width);
            int yRandCoord = (int) (Math.random() * 100 % height);
            if (cells[yRandCoord][xRandCoord].getContent() != CellContent.BOMB && !(xRandCoord == coordX && yRandCoord == coordY)) {
                cells[yRandCoord][xRandCoord] = new Cell(xRandCoord, yRandCoord, CellContent.BOMB);
                bombsRemainder--;
                updateNearCells(xRandCoord, yRandCoord);
            }
        }
    }

    private void updateNearCells(int xRandCoord, int yRandCoord) {
        for (int i = yRandCoord - 1; i < yRandCoord + 2; i++) {
            for (int j = xRandCoord - 1; j < xRandCoord + 2; j++) {
                if (i >= 0 && i < width && j >= 0 && j < height) {
                    if (cells[i][j].getContent() != CellContent.BOMB) {
                        cells[i][j].setContent(cells[i][j].getContent().nextNumber());
                    }
                }
            }
        }
    }
}
