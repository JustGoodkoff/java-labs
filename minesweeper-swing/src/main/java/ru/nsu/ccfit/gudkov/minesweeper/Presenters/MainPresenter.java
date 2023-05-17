package ru.nsu.ccfit.gudkov.minesweeper.Presenters;

import ru.nsu.ccfit.gudkov.minesweeper.MainView;
import ru.nsu.ccfit.gudkov.minesweeper.Model.Cell;
import ru.nsu.ccfit.gudkov.minesweeper.Model.CellContent;
import ru.nsu.ccfit.gudkov.minesweeper.Model.GameState;
import ru.nsu.ccfit.gudkov.minesweeper.Model.MinesweeperModel;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

public class MainPresenter {
    MinesweeperModel model;
    MainView view;
    private ArrayList<Cell> cellsToUpdate;
    private boolean isFirstMove = true;


    public MainPresenter(MinesweeperModel model, MainView mainView) {
        this.model = model;
        this.view = mainView;
        mainView.addListener(this);
    }

    public void leftClick(int coordX, int coordY) {
        if (model.getGameState() == GameState.BOMBED || model.getGameState() == GameState.WIN) {
            return;
        }

        System.out.println(coordX);
        System.out.println(coordY);
        cellsToUpdate = new ArrayList<>();
        if (isFirstMove) {
            model.setGameState(GameState.IN_PROGRESS);
            model.fillCells(coordX, coordY);
            isFirstMove = false;
            for (int i = 0; i < model.getHeight(); i++) {
                for (int j = 0; j < model.getWidth(); j++) {
                    System.out.print(model.getCells()[i][j].getContent());
                    System.out.print(" ");
                }
                System.out.print("\n");
            }
        }

        Cell[][] cells = model.getCells();
        if (!cells[coordY][coordX].isFlagged() && !cells[coordY][coordX].isOpen()) {
            if (cells[coordY][coordX].getContent() == CellContent.BOMB) {
                model.setGameState(GameState.BOMBED);
                for (Cell cell : model.getAllBombs()) {
                    cell.setOpen(true);
                }
                cellsToUpdate.addAll(model.getAllBombs());
            } else if (cells[coordY][coordX].getContent() == CellContent.ZERO) {
                openNearCells(coordX, coordY, cells, cellsToUpdate);
            } else {
                cells[coordY][coordX].setOpen(true);
                model.increaseCurrentOpenedCells();
                cellsToUpdate.add(cells[coordY][coordX]);
            }
        }
        if (model.getCurrentOpenedCells() == model.getWidth() * model.getWidth() - model.getBombs() && model.getGameState() == GameState.IN_PROGRESS) {
            model.setGameState(GameState.WIN);
        }
    }


    public void rightClick(int coordX, int coordY) {
        if (model.getGameState() == GameState.BOMBED || model.getGameState() == GameState.WIN) {
            return;
        }
        Cell[][] cells = model.getCells();
        if (!isFirstMove && !cells[coordY][coordX].isOpen()) {
            cellsToUpdate = new ArrayList<>();
            if (cells[coordY][coordX].isFlagged()) {
                cells[coordY][coordX].setFlagged(false);
                model.decreaseCurrentUsedFlags();
            } else {
                if (model.getCurrentUsedFlags() < model.getMaxFlags()) {
                    cells[coordY][coordX].setFlagged(true);
                    model.increaseCurrentUsedFlags();
                }
            }
            cellsToUpdate.add(cells[coordY][coordX]);
        }
    }


    public void updateField(JButton[][] buttons) throws IOException {
        for (Cell cell : cellsToUpdate) {
            if (cell.isOpen()) {
                if (cell.isFlagged()) {
                    model.decreaseCurrentUsedFlags();
                    cell.setFlagged(false);
                }
                ImageIcon icon = new ImageIcon((Objects.requireNonNull(MainPresenter.class.getClassLoader().getResource("./" + cell.getContent().toString() + ".png"))));
                buttons[cell.getYCoord()][cell.getXCoord()].setIcon(icon);
            } else if (cell.isFlagged()) {
                ImageIcon icon = new ImageIcon((Objects.requireNonNull(MainPresenter.class.getClassLoader().getResource("./FLAG.png"))));
                buttons[cell.getYCoord()][cell.getXCoord()].setIcon(icon);
            } else {
                ImageIcon icon = new ImageIcon((Objects.requireNonNull(MainPresenter.class.getClassLoader().getResource("./CLOSED.png"))));
                buttons[cell.getYCoord()][cell.getXCoord()].setIcon(icon);
            }
        }
        if (model.getGameState() == GameState.WIN) {
            JOptionPane.showMessageDialog(view.getContentPane(), "WIN");
        } else if (model.getGameState() == GameState.BOMBED) {
            JOptionPane.showMessageDialog(view.getContentPane(), "LOOSER");
        }
    }

    private void openNearCells(int coordX, int coordY, Cell[][] cells, ArrayList<Cell> cellsToUpdate) {
        cells[coordY][coordX].setOpen(true);
        model.increaseCurrentOpenedCells();
        cellsToUpdate.add(cells[coordY][coordX]);
        if (cells[coordY][coordX].getContent() == CellContent.ZERO) {
            for (int i = coordY - 1; i < coordY + 2; i++) {
                for (int j = coordX - 1; j < coordX + 2; j++) {
                    if (i >= 0 && i < model.getHeight() && j >= 0 && j < model.getWidth()) {
                        if (!cells[i][j].isOpen()) {
                            openNearCells(j, i, cells, cellsToUpdate);
                        }
                    }
                }
            }
        }
    }

    public void easyButtonClick() throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = MainPresenter.class.getClassLoader().getResourceAsStream("./settings.properties");
        properties.load(inputStream);
        properties.put("h", "10");
        properties.setProperty("h", "10");
        properties.setProperty("width", "10");
        properties.setProperty("flags", "10");
        properties.setProperty("mines", "10");
        System.out.println(properties.getProperty("width"));
        properties.replace("width", "100");
        System.out.println(properties.getProperty("width"));


    }

}
