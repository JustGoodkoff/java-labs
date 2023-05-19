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
        Properties properties = new Properties();
        MinesweeperModel model = new MinesweeperModel();
        // TODO: добавить передачу числа строк и столбцов в view
        MainView mainView = new MainView();
        try {
            MainPresenter mainPresenter = new MainPresenter(model, mainView);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::StartGame);
    }
}
