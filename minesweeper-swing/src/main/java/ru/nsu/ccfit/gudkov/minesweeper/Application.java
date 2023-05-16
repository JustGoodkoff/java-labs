package ru.nsu.ccfit.gudkov.minesweeper;

import ru.nsu.ccfit.gudkov.minesweeper.Model.MinesweeperModel;
import ru.nsu.ccfit.gudkov.minesweeper.Presenters.MainPresenter;

import javax.swing.*;

public class Application {

    public static void StartGame() {
        MinesweeperModel model = new MinesweeperModel();
        // TODO: добавить передачу числа строк и столбцов в view
        MainView mainView = new MainView();
        MainPresenter mainPresenter = new MainPresenter(model, mainView);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::StartGame);
    }
}
