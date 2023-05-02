package hu.sudoku.sudoku.action;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RemoveStep {
    private final RemoveStep previous;
    private final Position position;
    private final int number;

    private final List<Position> alreadyRemovedPositions;

    public RemoveStep(RemoveStep previous, Position position, int number, List<Position> alreadyRemovedPositions) {
        this.previous = previous;
        this.position = position;
        this.number = number;
        this.alreadyRemovedPositions = alreadyRemovedPositions;
    }

    public RemoveStep(RemoveStep previous, Position position, int number) {
        this.previous = previous;
        this.position = position;
        this.number = number;
        this.alreadyRemovedPositions = new ArrayList<>();
    }
}
