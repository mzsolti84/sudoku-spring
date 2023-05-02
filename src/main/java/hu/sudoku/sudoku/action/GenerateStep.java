package hu.sudoku.sudoku.action;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Getter
public class GenerateStep {

    private final GenerateStep previous;

    private final Position position;

    private final List<Integer> possibleNumbers;


    public GenerateStep(GenerateStep previous, Position position, Set<Integer> possibleNumbers) {
        this.previous = previous;
        this.position = position;
        this.possibleNumbers = new ArrayList<>(possibleNumbers);
        Collections.shuffle(this.possibleNumbers);
    }
}
