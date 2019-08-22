/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

/**
 *
 * @author João Soares
 */
public class Move {

    private int[][] grid;
    private int point;

    public Move(int[][] grid, int point) {
        this.grid = grid;
        this.point = point;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int[][] getGrid() {
        return this.grid;
    }

    public int getPoint() {
        return this.point;
    }
}
