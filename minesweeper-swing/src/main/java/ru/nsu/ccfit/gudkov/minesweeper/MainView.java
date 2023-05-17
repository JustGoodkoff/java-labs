package ru.nsu.ccfit.gudkov.minesweeper;

import ru.nsu.ccfit.gudkov.minesweeper.Presenters.MainPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class MainView extends JFrame {

    MainPresenter listener;

    public MainView() {
        super("BorderLayoutTest");
        JButton[][] buttons = new JButton[9][9];

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(30 * 9, 30 * 9);
        // Панель содержимого
        Container container = getContentPane();

        JPanel panel = new JPanel(new GridLayout(9, 9));
        panel.setSize(30 * 9, 30 * 9);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JButton button = new JButton();
                ImageIcon icon = new ImageIcon((Objects.requireNonNull(MainView.class.getClassLoader().getResource("./CLOSED.png"))));
                button.setIcon(icon);
                buttons[i][j] = button;
                panel.add(button);
                int coordX = j;
                int coordY = i;
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            System.out.println(String.valueOf(coordX) + " " + String.valueOf(coordY));
                            listener.leftClick(coordX, coordY);
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            listener.rightClick(coordX, coordY);
                        }

                        try {
                            listener.updateField(buttons);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
        }
        JMenuBar menuBar = new JMenuBar();

        JMenu game = new JMenu("Game");
        JMenuItem newGameItem = new JMenuItem("New Game");
        JMenuItem aboutGameItem = new JMenuItem("About");

        JMenu highScore = new JMenu("High Scores");

        JMenu settings = new JMenu("Settings");
        JRadioButtonMenuItem easy = new JRadioButtonMenuItem("Easy: field 9x9, 10 mines");
        JRadioButtonMenuItem medium = new JRadioButtonMenuItem("Medium: field 16x16, 40 mines");
        JRadioButtonMenuItem hard = new JRadioButtonMenuItem("Hard: field 16x30, 99 mines");


        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(easy);
        buttonGroup.add(medium);
        buttonGroup.add(hard);
        easy.setSelected(true);
        settings.add(easy);
        settings.add(medium);
        settings.add(hard);

        easy.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                try {
                    listener.easyButtonClick();
                    System.out.println(123);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });



        highScore.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                System.out.println("1234567890");
            }
        });

        newGameItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                dispose();
                Application.StartGame();
            }
        });

        aboutGameItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                JOptionPane.showMessageDialog(getContentPane(), "Minesweeper!");
                JFrame newf = new JFrame("213");
                newf.setVisible(true);
            }
        });

        game.add(newGameItem);
        game.add(aboutGameItem);
        menuBar.add(game);
        menuBar.add(highScore);
        menuBar.add(settings);
        container.add(menuBar, "North");
        container.add(new JButton("Юг"), "South");
        // При отсутствии 2-го параметра компонент размещается в центре
        container.add(panel);
        // Открываем окно
        setVisible(true);
    }

    public void addListener(MainPresenter presenter) {
        listener = presenter;
    }
}
