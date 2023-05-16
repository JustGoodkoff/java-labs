package ru.nsu.ccfit.gudkov.minesweeper;

import ru.nsu.ccfit.gudkov.minesweeper.Presenters.MainPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Objects;

public class MainView extends JFrame{

    MainPresenter listener;

    MainView() {
        super("BorderLayoutTest");
        JButton[][] buttons = new JButton[9][9];

        setDefaultCloseOperation( EXIT_ON_CLOSE );
        setSize(30*9, 30*9);
        // Панель содержимого
        Container container = getContentPane();

        JPanel panel = new JPanel(new GridLayout(9, 9));
        panel.setSize(30*9, 30*9);
        for(int i = 0; i < 9; i++) {
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
                        }
                        else if (e.getButton() == MouseEvent.BUTTON3) {
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
        container.add(new JButton("Север" ), "North");
        container.add(new JButton("Юг"    ), "South");
        // При отсутствии 2-го параметра компонент размещается в центре
        container.add(panel);
        // Открываем окно
        setVisible(true);
    }

    public void addListener(MainPresenter presenter) {
        listener = presenter;
    }
}
