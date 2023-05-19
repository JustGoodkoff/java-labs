package ru.nsu.ccfit.gudkov.minesweeper;

import ru.nsu.ccfit.gudkov.minesweeper.Presenters.StatPresenter;

import javax.swing.*;
import java.awt.*;
import java.net.ContentHandler;

public class StatView extends JFrame {

    StatPresenter presenter;
    public StatView() {
        super("123");


        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(30 * 9, 30 * 9);
        // Панель содержимого
        Container container = getContentPane();
        container.setLayout(new FlowLayout(FlowLayout.CENTER));
        Container container1 = new Container();
        JPanel easePanel = new JPanel();
        easePanel.setLayout((new BoxLayout(easePanel, BoxLayout.Y_AXIS)));
        easePanel.add(new JButton("qwe"));
        easePanel.add(new JButton("qwe"));
        easePanel.add(new JButton("qwe"));

        JPanel mediumPanel = new JPanel();
        mediumPanel.setLayout((new BoxLayout(mediumPanel, BoxLayout.Y_AXIS)));
        mediumPanel.add(new JButton("1qwe"));
        mediumPanel.add(new JButton("1qwe"));
        mediumPanel.add(new JButton("1qwe"));

        JPanel hardPanel = new JPanel();
        hardPanel.setLayout((new BoxLayout(hardPanel, BoxLayout.Y_AXIS)));
        hardPanel.add(new JButton("2qwe"));
        hardPanel.add(new JButton("2qwe"));
        hardPanel.add(new JButton("2qwe"));

        container.add(easePanel);
        container.add(hardPanel);
        container.add(mediumPanel);


        setVisible(true);

    }

    public void addListener(StatPresenter presenter) {
        this.presenter = presenter;
    }





}
