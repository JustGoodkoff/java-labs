package ru.nsu.ccfit.gudkov.minesweeper.Presenters;

import org.xml.sax.SAXException;
import ru.nsu.ccfit.gudkov.minesweeper.Application;
import ru.nsu.ccfit.gudkov.minesweeper.MainView;
import ru.nsu.ccfit.gudkov.minesweeper.Model.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import ru.nsu.ccfit.gudkov.minesweeper.StatView;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class MainPresenter {
    MinesweeperModel model;
    MainView view;
    private ArrayList<Cell> cellsToUpdate;
    private boolean isFirstMove = true;




    public MainPresenter(MinesweeperModel model, MainView mainView) throws ParserConfigurationException, IOException, SAXException {
        this.model = model;
        this.view = mainView;

        mainView.addListener(this);
//        InputStream inputStream = MainPresenter.class.getClassLoader().getResourceAsStream("./settings.properties");
//        //creating a constructor of file class and parsing an XML file
//        File file = new File("F:\\XMLFile.xml");
////an instance of factory that gives a document builder
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
////an instance of builder to parse the specified xml file
//        DocumentBuilder db = dbf.newDocumentBuilder();
//        Document doc = db.parse(file);
//        doc.getDocumentElement().normalize();
//        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());


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
//        InputStream inputStream = MainPresenter.class.getClassLoader().getResourceAsStream("./settings.properties");
//        view.dispose();
        Application.StartGame();
    }

    public void statisticButtonClick() {
        StatModel statModel = new StatModel();
        StatView statView = new StatView();
        StatPresenter statPresenter = new StatPresenter(statView, statModel);
    }



//    public void createUI(int height, int width){
//        JButton[][] buttons = new JButton[height][width];
//
//        view.setDefaultCloseOperation(EXIT_ON_CLOSE);
//        view.setSize(30 * height, 30 * width);
//        // Панель содержимого
//        Container container = view.getContentPane();
//
//        JPanel panel = new JPanel(new GridLayout(height, width));
//        panel.setSize(30 * width, 30 * height);
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                JButton button = new JButton();
//                ImageIcon icon = new ImageIcon((Objects.requireNonNull(MainView.class.getClassLoader().getResource("./CLOSED.png"))));
//                button.setIcon(icon);
//                buttons[i][j] = button;
//                panel.add(button);
//                int coordX = j;
//                int coordY = i;
//                button.addMouseListener(new MouseAdapter() {
//                    @Override
//                    public void mousePressed(MouseEvent e) {
//                        if (e.getButton() == MouseEvent.BUTTON1) {
//                            System.out.println(String.valueOf(coordX) + " " + String.valueOf(coordY));
//                            leftClick(coordX, coordY);
//                        } else if (e.getButton() == MouseEvent.BUTTON3) {
//                            rightClick(coordX, coordY);
//                        }
//
//                        try {
//                            updateField(buttons);
//                        } catch (IOException ex) {
//                            throw new RuntimeException(ex);
//                        }
//                    }
//                });
//            }
//        }
//        JMenuBar menuBar = new JMenuBar();
//
//        JMenu game = new JMenu("Game");
//        JMenuItem newGameItem = new JMenuItem("New Game");
//        JMenuItem aboutGameItem = new JMenuItem("About");
//
//        JMenu highScore = new JMenu("High Scores");
//
//        JMenu settings = new JMenu("Settings");
//        JRadioButtonMenuItem easy = new JRadioButtonMenuItem("Easy: field 9x9, 10 mines");
//        JRadioButtonMenuItem medium = new JRadioButtonMenuItem("Medium: field 16x16, 40 mines");
//        JRadioButtonMenuItem hard = new JRadioButtonMenuItem("Hard: field 16x30, 99 mines");
//
//
//        ButtonGroup buttonGroup = new ButtonGroup();
//        buttonGroup.add(easy);
//        buttonGroup.add(medium);
//        buttonGroup.add(hard);
//        easy.setSelected(true);
//        settings.add(easy);
//        settings.add(medium);
//        settings.add(hard);
//
//        easy.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                super.mousePressed(e);
//                try {
//                    easyButtonClick();
//                    System.out.println(123);
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//        });
//
//
//
//        highScore.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                super.mousePressed(e);
//                System.out.println("1234567890");
//
//                StatModel statModel = new StatModel();
//                StatView statView = new StatView();
//                StatPresenter statPresenter = new StatPresenter(statView, statModel);
//
//            }
//        });
//
//        newGameItem.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                super.mousePressed(e);
//                view.dispose();
//                Application.StartGame();
//            }
//        });
//
//        aboutGameItem.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                super.mousePressed(e);
//                JOptionPane.showMessageDialog(view.getContentPane(), "Minesweeper!");
//                JFrame newf = new JFrame("213");
//                newf.setVisible(true);
//            }
//        });
//
//        game.add(newGameItem);
//        game.add(aboutGameItem);
//        menuBar.add(game);
//        menuBar.add(highScore);
//        menuBar.add(settings);
//        container.add(menuBar, "North");
//        container.add(new JButton("Юг"), "South");
//        // При отсутствии 2-го параметра компонент размещается в центре
//        container.add(panel);
//        // Открываем окно
//        view.setVisible(true);
//    }

}
