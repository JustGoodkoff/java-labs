package ru.nsu.ccfit.gudkov.minesweeper;

import ru.nsu.ccfit.gudkov.minesweeper.Presenters.StatPresenter;

import javax.swing.*;
import java.awt.*;

public class StatView extends JFrame {

    private StatPresenter presenter;

    public StatView() {
        super(StringConstants.STATISTIC_TITLE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(30 * 9, 15 * 9);
        setVisible(true);
    }

    public void createUI() {
        Container container = getContentPane();
        container.setLayout(new FlowLayout(FlowLayout.CENTER));
        JPanel easePanel = new JPanel();
        easePanel.setLayout((new BoxLayout(easePanel, BoxLayout.Y_AXIS)));
        easePanel.add(new JLabel(StringConstants.EASY_MODE.toUpperCase()));
        presenter.addData(StringConstants.EASY_MODE, easePanel);


        JPanel mediumPanel = new JPanel();
        mediumPanel.setLayout((new BoxLayout(mediumPanel, BoxLayout.Y_AXIS)));
        mediumPanel.add(new JLabel(StringConstants.MEDIUM_MODE.toUpperCase()));
        presenter.addData(StringConstants.MEDIUM_MODE, mediumPanel);


        JPanel hardPanel = new JPanel();
        hardPanel.setLayout((new BoxLayout(hardPanel, BoxLayout.Y_AXIS)));
        hardPanel.add(new JLabel(StringConstants.HARD_MODE.toUpperCase()));
        presenter.addData(StringConstants.HARD_MODE, hardPanel);


        container.add(easePanel);
        container.add(hardPanel);
        container.add(mediumPanel);
    }

    public void addListener(StatPresenter presenter) {
        this.presenter = presenter;
    }


}
