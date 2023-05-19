package ru.nsu.ccfit.gudkov.minesweeper.Presenters;

import ru.nsu.ccfit.gudkov.minesweeper.Model.StatModel;
import ru.nsu.ccfit.gudkov.minesweeper.StatView;

public class StatPresenter {

    private StatView statView;
    private StatModel statModel;


    public StatPresenter(StatView statView, StatModel statModel) {
        this.statModel = statModel;
        this.statView = statView;

        statModel.getStatistic();
        statView.addListener(this);

    }



}
