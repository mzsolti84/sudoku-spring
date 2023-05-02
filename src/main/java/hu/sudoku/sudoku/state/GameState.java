package hu.sudoku.sudoku.state;

import org.springframework.stereotype.Component;

@Component
public class GameState {
    boolean created = false;

    public GameState() {
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }
}
