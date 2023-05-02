package hu.sudoku.sudoku.controller;

import hu.sudoku.sudoku.common.BaseApiResponse;
import hu.sudoku.sudoku.common.GameResponse;
import hu.sudoku.sudoku.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/public", produces="application/json")
@CrossOrigin(origins="*")
public class GameController {

    final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }


    @GetMapping("/newgame/{numbersOfFreePlaces}")
    public BaseApiResponse get(@PathVariable int numbersOfFreePlaces) {
        try {
            var newPuzzle = gameService.getNewPuzzle(numbersOfFreePlaces).gameboardData();
            System.out.println(newPuzzle);
            return GameResponse.builder()
                    .setMessage("OK")
                    .setStatus(HttpStatus.OK)
                    .setResponseObject(newPuzzle)
                    .build();
        } catch (Exception e) {
            return GameResponse.builder()
                    .setMessage(e.getMessage())
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setResponseObject(null)
                    .build();
        }
    }

    @GetMapping("/solvedgame")
    public BaseApiResponse solved() {
        try {
            var solvedPuzzle = gameService.getSolvedPuzzle().gameboardData();
            System.out.println(solvedPuzzle);
            return GameResponse.builder()
                    .setMessage("OK")
                    .setStatus(HttpStatus.OK)
                    .setResponseObject(solvedPuzzle)
                    .build();
        } catch (Exception e) {
            return GameResponse.builder()
                    .setMessage(e.getMessage())
                    .setStatus(HttpStatus.NOT_ACCEPTABLE)
                    .setResponseObject(null)
                    .build();
        }
    }
}
