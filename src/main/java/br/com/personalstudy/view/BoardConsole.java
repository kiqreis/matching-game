package br.com.personalstudy.view;

import br.com.personalstudy.exceptions.ExitException;
import br.com.personalstudy.exceptions.ExplosionException;
import br.com.personalstudy.model.Board;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class BoardConsole {

  private Board board;
  private Scanner input = new Scanner(System.in);

  public BoardConsole(Board board) {
    this.board = board;

    executeGame();
  }

  private void executeGame() {
    try {
      boolean toContinue = true;

      while (toContinue) {

        cycleGame();

        System.out.print("Do you want to play again? (Y/n) ");
        String response = input.nextLine();

        if ("n".equalsIgnoreCase(response)) {
          toContinue = false;
        } else {
          board.restart();
        }
      }
    } catch (ExitException e) {
      System.out.println("Left the game");
    } finally {
      input.close();
    }
  }

  private void cycleGame() {
    try {
      while (!board.goalAchieved()) {

        System.out.println(board);

        String entered = enteredValue("Enter (x, y): ");

        Iterator<Integer> coordinates = Arrays.stream(entered.split(", "))
            .map(n -> Integer.parseInt(n.trim()))
            .iterator();

        entered = enteredValue("1) Open | 2) (Un)mark: ");

        if ("1".equals(entered)) {
          board.open(coordinates.next(), coordinates.next());
        } else if ("2".equals(entered)) {
          board.toMark(coordinates.next(), coordinates.next());
        }
      }

      System.out.println("You win!");
    } catch (ExplosionException e) {
      System.out.println(board);

      System.out.println("Game over");
    }
  }

  public String enteredValue(String text) {
    System.out.print(text);

    String entered = input.nextLine();

    if ("exit".equalsIgnoreCase(entered)) {
      throw new ExitException();
    }

    return entered;
  }
}
