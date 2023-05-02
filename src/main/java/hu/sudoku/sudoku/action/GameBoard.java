package hu.sudoku.sudoku.action;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GameBoard {
    public static final int SUDOKU_SIZE = 9;
    public static final int BLOCK_SIZE = SUDOKU_SIZE / 3;
    public static final int EMPTY_VALUE = 0;
    public static final Set<Integer> ALL_POSSIBLE_VALUES = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

    private final Random random = new Random();
    private final int[][] data;
    private int[][] newPuzzle;

    public GameBoard() {
        this.data = new int[SUDOKU_SIZE][SUDOKU_SIZE];
        this.newPuzzle = new int[SUDOKU_SIZE][SUDOKU_SIZE];
    }

    public GameBoard(int[][] data) {
        this.newPuzzle = new int[SUDOKU_SIZE][SUDOKU_SIZE];
        validateInputSize(data);
        this.data = data;
    }

    //játéktábla generálása "back-track" módszer segítségével
    public void createPuzzle(int numbersOfFreePlaces) {
        solve(1, false);
        var allFilledPositions = getAllFilledPositions();
        var position = allFilledPositions.get(random.nextInt(allFilledPositions.size()));

        RemoveStep step = new RemoveStep(null, position, getActualNumber(position));
        clearFromBoard(position);

        int counter = 1;

        while (true) {
            //minden egyes ciklusban keresünk legalább 2 megoldást (0, 1, 2 megoldás)
            int count = solve(2, true);
            if (count == 1) {
                if (counter == numbersOfFreePlaces) {
                    newPuzzle = Arrays.stream(data)
                            .map(int[]::clone)
                            .toArray(int[][]::new);
                    break;
                }
                allFilledPositions = getAllFilledPositions();
                allFilledPositions.removeAll(step.getAlreadyRemovedPositions());
                //ha amit már próbáltunk és ami lehetséges annak metszete üres halmaz, akkor:
                if (allFilledPositions.isEmpty()) {
                    RemoveStep previous = step.getPrevious();
                    if (previous == null) {
                        throw new IllegalStateException("Not possible to create puzzle!");
                    }
                    setActualNumber(step.getPosition(), step.getNumber());
                    counter--;
                    previous.getAlreadyRemovedPositions().add(step.getPosition());
                    step = previous;
                    continue;
                }
                position = allFilledPositions.get(random.nextInt(allFilledPositions.size()));
                step = new RemoveStep(step, position, getActualNumber(position));
                clearFromBoard(position);
                counter++;
            } else {
                // step back
                RemoveStep previous = step.getPrevious();
                if (previous == null) {
                    throw new IllegalStateException("Not possible to create puzzle!");
                }
                setActualNumber(step.getPosition(), step.getNumber());
                counter--;
                previous.getAlreadyRemovedPositions().add(step.getPosition());
                step = previous;
            }
        }
    }

    public void solve() {
        solve(1, false);
    }

    private int solve(int maxCount, boolean undoChanges) {
        //kezdő pozíció inicializálás
        Position position = findNextEmptyPosition(Position.factory(0, -1))
                .orElseThrow(() -> new IllegalStateException("The table is already full!"));
        GenerateStep step = new GenerateStep(null, position, getPossibleNumbers(position));

        int count = 0;

        while (count < maxCount) {
            //ha vannak még lehetséges lépések,
            if (!step.getPossibleNumbers().isEmpty()) {
                // akkor abból kiválasztom az 0. indexűt és kitörlöm. (a remove visszatér a kitörölt számmal)
                Integer value = step.getPossibleNumbers().remove(0);
                setActualNumber(position, value);
                //ha a minden cella kitöltésre került, akkor megnöveljük a lehetséges találatokat 1-el
                if (isAllCellsAreFilled()) {
                    count++;
                    continue;
                }
                Position error = position;
                position = findNextEmptyPosition(position)
                        .orElseThrow(() -> new IllegalStateException("No more positions to fill: " + error));
                step = new GenerateStep(step, position, getPossibleNumbers(position));
                //utolsó lépés visszavonása, csak az üres mezőket vizsgálja!
            } else {
                clearFromBoard(position);
                step = step.getPrevious();
                if (step == null) {
                    return count;
                }
                position = step.getPosition();
            }
        }

        //Eredeti állapot visszaállítása. Minden "0" érték visszaállítása.
        if (undoChanges) {
            while (step != null) {
                clearFromBoard(step.getPosition());
                step = step.getPrevious();
            }
        }
        return count;
    }

    private List<Position> getAllFilledPositions() {
        List<Position> result = new ArrayList<>();
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int column = 0; column < SUDOKU_SIZE; column++) {
                if (!isEmpty(row, column)) {
                    result.add(new Position(row, column));
                }
            }
        }
        return result;
    }

    private void clearFromBoard(Position position) {
        data[position.row()][position.column()] = EMPTY_VALUE;
    }

    private boolean isEmpty(int row, int column) {
        return data[row][column] == EMPTY_VALUE;
    }

    private boolean isEmpty(Position position) {
        return data[position.row()][position.column()] == EMPTY_VALUE;
    }

    private boolean isAllCellsAreFilled() {
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int column = 0; column < SUDOKU_SIZE; column++) {
                if (isEmpty(row, column)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Visszatér a lehetséges értékekkel.
     *
     * @param row    tömbindex szerinti sor (0-8)
     * @param column tömbindex szerinti oszlopb(0-8)
     * @return lehetséges számok
     */
    public Set<Integer> getPossibleNumbers(int row, int column) {
        var possibleNumbers = getPossibleNumbersInRow(row);
        possibleNumbers.retainAll(getPossibleNumbersInColumn(column));
        possibleNumbers.retainAll(getPossibleNumbersInBlock(
                convertCoordinates(row, column)
        ));

        return possibleNumbers;
    }

    public Set<Integer> getPossibleNumbers(Position position) {
        return getPossibleNumbers(position.row(), position.column());
    }

    private int convertCoordinates(int row, int column) {
        return (row / 3) * 3 + column / 3;
    }

    public Set<Integer> getPossibleNumbersInRow(int row) {
        var result = new HashSet<>(ALL_POSSIBLE_VALUES);

        for (int i = 0; i < SUDOKU_SIZE; i++) {
            result.remove(data[row][i]);
        }
        return result;
    }

    public Set<Integer> getPossibleNumbersInColumn(int column) {
        var result = new HashSet<>(ALL_POSSIBLE_VALUES);

        for (int i = 0; i < SUDOKU_SIZE; i++) {
            result.remove(data[i][column]);
        }
        return result;
    }

    public Set<Integer> getPossibleNumbersInBlock(int bNumber) {
        var rowStart = (bNumber / 3) * 3;
        var columnStart = (bNumber % 3) * 3;
        var result = new HashSet<>(ALL_POSSIBLE_VALUES);

        for (int h = 0; h < SUDOKU_SIZE / 3; h++) {
            for (int v = 0; v < SUDOKU_SIZE / 3; v++) {
                result.remove(data[rowStart + h][columnStart + v]);
            }
        }
        return result;
    }

    private void validateInputSize(int[][] data) {
        if (data.length != SUDOKU_SIZE) {
            throw new IllegalArgumentException("Sudoku size must be 9x9!");
        }
        for (int[] d : data) {
            if (d.length != SUDOKU_SIZE) {
                throw new IllegalArgumentException("Sudoku size must be 9x9!");
            }
        }
    }

    private int getActualNumber(Position position) {
        return data[position.row()][position.column()];
    }

    private void setActualNumber(Position position, int value) {
        data[position.row()][position.column()] = value;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            if (row % BLOCK_SIZE == 0) {
                builder.append("+------+------+------+\n");
            }
            for (int column = 0; column < SUDOKU_SIZE; column++) {
                if (column % BLOCK_SIZE == 0) {
                    builder.append("|");
                }
                builder.append(getActualNumber(Position.factory(row, column)))
                        .append(" ");
            }
            builder.append("|\n");
        }
        builder.append("+------+------+------+");
        return builder.toString();
    }

    private Optional<Position> findNextEmptyPosition(Position position) {
        Position current = position;
        while (current.hasNext()) {
            current = current.next();
            if (isEmpty(current)) {
                return Optional.of(current);
            }
        }
        return Optional.empty();
    }

    public List<Integer> getFlat() {
        List<Integer> output = new ArrayList<>();
        for (int[] row : data) {
            for (int value : row) {
                output.add(value);
            }
        }
        return output;
    }

    public List<Integer> getFlatNewPuzzle() {
        List<Integer> output = new ArrayList<>();
        for (int[] row : newPuzzle) {
            for (int value : row) {
                output.add(value);
            }
        }
        return output;
    }

    public int[][] getNewPuzzle() {
        return newPuzzle;
    }

    public void printInitialPuzzle() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            if (row % BLOCK_SIZE == 0) {
                builder.append("+------+------+------+\n");
            }
            for (int column = 0; column < SUDOKU_SIZE; column++) {
                if (column % BLOCK_SIZE == 0) {
                    builder.append("|");
                }
                builder.append(newPuzzle[row][column])
                        .append(" ");
            }
            builder.append("|\n");
        }
        builder.append("+------+------+------+");
        System.out.println(builder);
    }
}