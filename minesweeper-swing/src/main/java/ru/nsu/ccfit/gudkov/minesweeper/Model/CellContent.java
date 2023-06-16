package ru.nsu.ccfit.gudkov.minesweeper.Model;

public enum CellContent {
    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    BOMB,
    BOMBED;

    CellContent nextNumber() {
        return CellContent.values()[this.ordinal() + 1];
    }
}
