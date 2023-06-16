package ru.nsu.ccfit.gudkov.minesweeper.Presenters;

import ru.nsu.ccfit.gudkov.minesweeper.Model.StatModel;
import ru.nsu.ccfit.gudkov.minesweeper.StatView;
import ru.nsu.ccfit.gudkov.minesweeper.StringConstants;

import javax.swing.*;

public class StatPresenter {

    private final StatView statView;
    private final StatModel statModel;


    public StatPresenter(StatView statView, StatModel statModel) {
        this.statModel = statModel;
        this.statView = statView;
        statView.addListener(this);
        statView.createUI();
    }

    public void addData(String mode, JPanel panel) {
        if (mode.equals(StringConstants.EASY_MODE)) {
            panel.add(new JLabel(String.valueOf(statModel.getEasyRecord())));
        }
        if (mode.equals(StringConstants.MEDIUM_MODE)) {
            panel.add(new JLabel(String.valueOf(statModel.getMediumRecord())));
        }
        if (mode.equals(StringConstants.HARD_MODE)) {
            panel.add(new JLabel(String.valueOf(statModel.getHardRecord())));
        }
    }



}
