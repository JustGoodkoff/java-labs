package ru.nsu.ccfit.gudkov.minesweeper.Model;

public class Cell {

    private boolean isOpen = false;
    private boolean isFlagged = false;
    private CellContent content = CellContent.ZERO;

    public int getXCoord() {
        return xCoord;
    }

    public void setXCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }

    public void setYCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    private int xCoord;
    private int yCoord;

    Cell(int xCoord, int yCoord, CellContent content){
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.content = content;
    }

    public CellContent getContent() {
        return this.content;
    }

    public void setContent(CellContent content) {
        this.content = content;
    }

    public void setFlagged(boolean state) {
        this.isFlagged = state;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean state) {
        this.isOpen = state;
    }

}
