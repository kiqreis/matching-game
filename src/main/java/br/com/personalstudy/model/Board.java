package br.com.personalstudy.model;

import br.com.personalstudy.exceptions.ExplosionException;

import java.util.ArrayList;
import java.util.List;

public class Board {

  private Integer lines;
  private Integer columns;
  private Integer mines;

  private final List<Field> fields = new ArrayList<>();

  public Board(Integer lines, Integer columns, Integer mines) {
    this.lines = lines;
    this.columns = columns;
    this.mines = mines;

    generateFieldS();
    associateNeighbors();
    drawMines();
  }

  public void open(int line, int column) {
    try {
      fields.stream()
          .filter(field -> field.getLine() == line && field.getColumn() == column)
          .findFirst()
          .ifPresent(Field::open);
    } catch (ExplosionException e) {
      fields.forEach(field -> field.setOpen(true));

      throw e;
    }
  }

  public void toMark(int line, int column) {
    fields.stream()
        .filter(field -> field.getLine() == line && field.getColumn() == column)
        .findFirst()
        .ifPresent(Field::toggle);
  }

  private void generateFieldS() {
    for (int i = 0; i < lines; i++) {
      for (int j = 0; j < columns; j++) {
        fields.add(new Field(i, j));
      }
    }
  }

  private void associateNeighbors() {
    for (Field f1 : fields) {
      for (Field f2 : fields) {
        f1.addNeighbor(f2);
      }
    }
  }

  private void drawMines() {
    long armedMines;

    do {
      int random = (int) (Math.random() * fields.size());

      armedMines = fields.stream()
          .filter(Field::isMined)
          .count();

      fields.get(random).undermine();
    } while (armedMines < mines);
  }

  public Boolean goalAchieved() {
    return fields.stream()
        .allMatch(Field::goalAchieved);
  }

  public void restart() {
    fields.forEach(Field::restart);

    drawMines();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    int index = 0;

    for (int i = 0; i < lines; i++) {
      for (int j = 0; j < columns; j++) {
        sb.append(" ");
        sb.append(fields.get(index));
        sb.append(" ");

        index++;
      }

      sb.append("\n");
    }

    return sb.toString();
  }
}
