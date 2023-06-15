package ru.nsu.ccfit.gudkov.minesweeper;

import org.xml.sax.SAXException;
import ru.nsu.ccfit.gudkov.minesweeper.Model.MinesweeperModel;
import ru.nsu.ccfit.gudkov.minesweeper.Presenters.MainPresenter;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Properties;

public class Application {

    public static void StartGame() {

        MinesweeperModel model = null;
        try {
            model = new MinesweeperModel();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MainView mainView = new MainView();
        MainPresenter mainPresenter = new MainPresenter(model, mainView);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::StartGame);
    }
}
