package hu.sudoku.sudoku.dto;

import java.util.List;
import hu.sudoku.sudoku.action.GameBoard;
import org.jetbrains.annotations.NotNull;

public record TableDto(List<Integer> gameboardData) {

    static public TableDto newGameFactory(@NotNull GameBoard game) {
        return new TableDto(game.getFlatNewPuzzle());
    }

    static public TableDto solvedGameFactory(@NotNull GameBoard game) {
        return new TableDto(game.getFlat());
    }
}
