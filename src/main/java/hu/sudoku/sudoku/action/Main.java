package hu.sudoku.sudoku.action;

//import command.Command;
//import command.ExitCommand;
//import command.NewCommand;
//
//import java.util.List;
//import java.util.Optional;
//
//public class Main {
//    public static void main(String[] args) {
//        GameBoard gameBoard = new GameBoard();
//
//        List<Command> commands = List.of(
//                new NewCommand(gameBoard),
//                new ExitCommand(gameBoard)
//        );
//
//        String[] cmdBits = new String[]{"new", "54"};  //hard game
//        Optional<Command> matchingCommand = findCommand(commands, cmdBits[0]);
//        matchingCommand.ifPresent(command -> command.execute(cmdBits));
//
//        gameBoard.solve();
//        System.out.println("Solve:");
//        System.out.println(gameBoard);
//        System.out.println("------------");
//        System.out.println(gameBoard.getFlatNewPuzzle());
//        System.out.println(gameBoard.getFlat());
//        System.out.println("------------");
//        gameBoard.printInitialPuzzle();
//
//    }
//
//    private static Optional<Command> findCommand(List<Command> commands, String instruction) {
//        for (Command command : commands) {
//            if (command.acceptsCommand(instruction)) {
//                return Optional.of(command);
//            }
//        }
//        return Optional.empty();
//    }
//}