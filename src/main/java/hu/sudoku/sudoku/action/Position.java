package hu.sudoku.sudoku.action;

public record Position(int row, int column) {
    public static Position factory(int fRow, int fColumn) {
        return new Position(fRow, fColumn);
    }

    public boolean hasNext() {
        return row < GameBoard.SUDOKU_SIZE - 1 || column < GameBoard.SUDOKU_SIZE - 1;
    }

    public Position next() {
        int newColumn = column + 1;
        int carryOver = newColumn / GameBoard.SUDOKU_SIZE;
        return new Position(row + carryOver, newColumn % GameBoard.SUDOKU_SIZE);
    }
}
