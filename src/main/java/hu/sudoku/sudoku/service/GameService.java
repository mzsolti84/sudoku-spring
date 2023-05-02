package hu.sudoku.sudoku.service;

import hu.sudoku.sudoku.dto.TableDto;
import hu.sudoku.sudoku.action.GameBoard;
import hu.sudoku.sudoku.state.GameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    final GameBoard gameBoard;
    final GameState state;

    @Autowired
    public GameService(GameState state) {
        this.state = state;
        this.gameBoard = new GameBoard();
    }

    public TableDto getNewPuzzle(int numbersOfFreePlaces) {
        if (numbersOfFreePlaces < 15 ||numbersOfFreePlaces > 54) throw new IllegalStateException("Invalid numbersOfFreePlaces!");
        gameBoard.createPuzzle(numbersOfFreePlaces);
        state.setCreated(true);
        return TableDto.newGameFactory(gameBoard);
    }

    public TableDto getSolvedPuzzle() {
        if (!state.isCreated()) throw new IllegalStateException("Game not initialized!");
        gameBoard.solve();
        return TableDto.solvedGameFactory(gameBoard);
    }
}
