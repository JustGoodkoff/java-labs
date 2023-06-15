package ru.nsu.ccfit.gudkov.minesweeper;

import ru.nsu.ccfit.gudkov.minesweeper.Presenters.MainPresenter;

import javax.swing.*;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class MainView extends JFrame {
    private MainPresenter presenter;

    public MainView() {
        super(StringConstants.MINESWEEPER_TITLE);

    }

    public void createUI() {

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Панель содержимого
        Container container = getContentPane();

        JPanel panel = presenter.createMainPanel();
        JMenuBar menuBar = new JMenuBar();

        JMenu game = new JMenu(StringConstants.GAME);
        JMenuItem newGameItem = new JMenuItem(StringConstants.NEW_GAME);
        JMenuItem aboutGameItem = new JMenuItem(StringConstants.ABOUT);

        JMenu highScore = new JMenu(StringConstants.HIGH_SCORES);

        JMenu settings = new JMenu(StringConstants.SETTINGS);
        JRadioButtonMenuItem easy = new JRadioButtonMenuItem(StringConstants.EASY_MODE_PARAMS);
        JRadioButtonMenuItem medium = new JRadioButtonMenuItem(StringConstants.MEDIUM_MODE_PARAMS);
        JRadioButtonMenuItem hard = new JRadioButtonMenuItem(StringConstants.HARD_MODE_PARAMS);


        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(easy);
        buttonGroup.add(medium);
        buttonGroup.add(hard);

        presenter.setSelectedMode(easy, medium, hard);

        settings.add(easy);
        settings.add(medium);
        settings.add(hard);

        easy.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                presenter.easyButtonClick();

            }
        });

        medium.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                presenter.mediumButtonClick();
            }
        });

        hard.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                presenter.hardButtonClick();
            }
        });


        highScore.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                presenter.statisticButtonClick();
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
                presenter.showAboutDialog();
            }
        });

        game.add(newGameItem);
        game.add(aboutGameItem);
        menuBar.add(game);
        menuBar.add(highScore);
        menuBar.add(settings);
        JLabel timerLabel = new JLabel(StringConstants.START_TIME_LABEL);

        presenter.createTimer(timerLabel);

        container.add(menuBar, StringConstants.NORTH);
        container.add(timerLabel, StringConstants.SOUTH);
        container.add(panel);
        pack();
        setResizable(false);
        setVisible(true);
    }


    public void addPresenter(MainPresenter presenter) {
        this.presenter = presenter;
    }
}
