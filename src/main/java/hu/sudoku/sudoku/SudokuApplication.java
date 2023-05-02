package hu.sudoku.sudoku;

import hu.sudoku.sudoku.action.GameBoard;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SudokuApplication {

	public static void main(String[] args) {
		GameBoard gameBoard = new GameBoard();
		int numbersOfFreePlaces = 42;
		gameBoard.createPuzzle(numbersOfFreePlaces);
		gameBoard.printInitialPuzzle();
		System.out.println("------------");
		gameBoard.solve();
		System.out.println("Solve:");
		System.out.println(gameBoard);
		Runtime.Version version = Runtime.version();
		System.out.println(version);

		SpringApplication.run(SudokuApplication.class, args);
	}

}
