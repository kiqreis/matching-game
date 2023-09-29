package br.com.personalstudy;

import br.com.personalstudy.model.Board;
import br.com.personalstudy.view.BoardConsole;

public class Application {

  public static void main(String[] args) {

    Board board = new Board(6, 6, 6);

    new BoardConsole(board);
  }
}
