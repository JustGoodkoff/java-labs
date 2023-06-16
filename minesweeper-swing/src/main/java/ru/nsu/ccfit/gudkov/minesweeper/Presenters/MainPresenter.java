package ru.nsu.ccfit.gudkov.minesweeper.Presenters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ru.nsu.ccfit.gudkov.minesweeper.Application;
import ru.nsu.ccfit.gudkov.minesweeper.MainView;
import ru.nsu.ccfit.gudkov.minesweeper.Model.*;
import ru.nsu.ccfit.gudkov.minesweeper.StatView;
import ru.nsu.ccfit.gudkov.minesweeper.StringConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MainPresenter {
    private MinesweeperModel model;
    private MainView view;
    private ArrayList<Cell> cellsToUpdate;
    private boolean isFirstMove = true;

    private final int timerDelay = 1000;

    private Timer timer;
    private long time;

    public MainPresenter(MinesweeperModel model, MainView mainView) {
        this.model = model;
        this.view = mainView;

        mainView.addPresenter(this);
        mainView.createUI();

    }

    public void createTimer(JLabel timerLabel) {
        timer = new Timer(timerDelay, e -> {
            time += 1;
            timerLabel.setText(StringConstants.TIME_LABEL + time);
        });
        SwingUtilities.invokeLater(timer::start);
    }

    public JPanel createMainPanel() {
        int rows = model.getHeight();
        int cols = model.getWidth();

        JButton[][] buttons = new JButton[rows][cols];
        JPanel panel = new JPanel(new GridLayout(rows, cols));
        panel.setPreferredSize(new Dimension(cols * 25, rows * 25));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JButton button = new JButton();
                ImageIcon icon = new ImageIcon((Objects.requireNonNull(MainView.class.getClassLoader().getResource(StringConstants.CLOSED_PIC_PATH))));
                button.setIcon(icon);
                buttons[i][j] = button;
                panel.add(button);
                int coordX = j;
                int coordY = i;
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            MainPresenter.this.leftClick(coordX, coordY);
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            MainPresenter.this.rightClick(coordX, coordY);
                        }

                        try {
                            MainPresenter.this.updateField(buttons);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
        }
        return panel;
    }


    public void leftClick(int coordX, int coordY) {
        if (model.getGameState() == GameState.BOMBED || model.getGameState() == GameState.WIN) {
            return;
        }
        cellsToUpdate = new ArrayList<>();
        if (isFirstMove) {
            model.setGameState(GameState.IN_PROGRESS);
            model.fillCells(coordX, coordY);
            isFirstMove = false;
        }

        Cell[][] cells = model.getCells();
        if (!cells[coordY][coordX].isFlagged() && !cells[coordY][coordX].isOpen()) {
            if (cells[coordY][coordX].getContent() == CellContent.BOMB) {
                model.setGameState(GameState.BOMBED);
                timer.stop();
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
            timer.stop();

            File file = new File(StringConstants.STATISTIC_ABSOLUTE_PATH);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode;
            try {
                rootNode = objectMapper.readTree(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (time < rootNode.get(model.getMode()).asLong()) {
                ((ObjectNode) rootNode).put(model.getMode(), time);
                try {
                    objectMapper.writeValue(file, rootNode);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
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
                ImageIcon icon = new ImageIcon((Objects.requireNonNull(MainPresenter.class.getClassLoader().getResource(StringConstants.FLAG_PIC_PATH))));
                buttons[cell.getYCoord()][cell.getXCoord()].setIcon(icon);
            } else {
                ImageIcon icon = new ImageIcon((Objects.requireNonNull(MainPresenter.class.getClassLoader().getResource(StringConstants.CLOSED_PIC_PATH))));
                buttons[cell.getYCoord()][cell.getXCoord()].setIcon(icon);
            }
        }
        if (model.getGameState() == GameState.WIN) {
            JOptionPane.showMessageDialog(view.getContentPane(), StringConstants.WIN);
        } else if (model.getGameState() == GameState.BOMBED) {
            JOptionPane.showMessageDialog(view.getContentPane(), StringConstants.LOOSE);
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

    private void setMode(String mode) {
        File file = new File(Objects.requireNonNull(MainPresenter.class.getClassLoader().getResource(StringConstants.SETTINGS_PATH)).getFile());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ((ObjectNode) rootNode).put(StringConstants.MODE, mode);
        System.out.println(rootNode.get(StringConstants.MODE).asText());
        try {
            objectMapper.writeValue(file, rootNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void easyButtonClick() {
        setMode(StringConstants.EASY_MODE);
        view.dispose();
        Application.StartGame();
    }

    public void mediumButtonClick() {
        setMode(StringConstants.MEDIUM_MODE);
        view.dispose();
        Application.StartGame();
    }

    public void hardButtonClick() {
        setMode(StringConstants.HARD_MODE);
        view.dispose();
        Application.StartGame();
    }

    public void statisticButtonClick() {
        StatModel statModel = new StatModel();
        StatView statView = new StatView();
        new StatPresenter(statView, statModel);
    }

    public void showAboutDialog() {
        JOptionPane.showMessageDialog(view.getContentPane(), StringConstants.ABOUT_TEXT);

    }

    public void setSelectedMode(JRadioButtonMenuItem easy, JRadioButtonMenuItem medium, JRadioButtonMenuItem hard) {
        if (model.getMode().equals(StringConstants.EASY_MODE)) {
            easy.setSelected(true);
        }
        if (model.getMode().equals(StringConstants.MEDIUM_MODE)) {
            medium.setSelected(true);
        }
        if (model.getMode().equals(StringConstants.HARD_MODE)) {
            hard.setSelected(true);
        }
    }
}
