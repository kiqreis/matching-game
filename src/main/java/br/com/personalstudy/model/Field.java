package br.com.personalstudy.model;

import br.com.personalstudy.exceptions.ExplosionException;

import java.util.ArrayList;
import java.util.List;

public class Field {
  private final Integer line;
  private final Integer column;

  private Boolean mined = false;
  private Boolean open = false;
  private Boolean marked = false;

  private List<Field> neighbors = new ArrayList<>();

  public Field(Integer line, Integer column) {
    this.line = line;
    this.column = column;
  }

  public Boolean addNeighbor(Field neighbor) {

    boolean differentLine = line != neighbor.line;
    boolean differentColumn = column != neighbor.column;
    boolean diagonal = differentLine && differentColumn;

    int deltaLine = Math.abs(line - neighbor.line);
    int deltaColumn = Math.abs(column - neighbor.column);
    int generalDelta = deltaLine + deltaColumn;

    if (generalDelta == 1 && !diagonal) {

      neighbors.add(neighbor);
      return true;
    } else if (generalDelta == 2 && diagonal) {

      neighbors.add(neighbor);
      return true;
    } else {
      return false;
    }
  }

  public void toggle() {

    if (!open) {
      marked = !marked;
    }
  }

  public Boolean open() {

    if (!open && !marked) {
      open = true;

      if (mined) {
        throw new ExplosionException();
      }

      if (safeNeighborhood()) {
        neighbors.forEach(Field::open);
      }

      return true;
    } else {
      return false;
    }
  }

  public Boolean safeNeighborhood() {
    return neighbors.stream()
        .noneMatch(neighbor -> neighbor.mined);
  }

  public Boolean isMarked() {
    return this.marked;
  }

  public Boolean isMined() {
    return mined;
  }

  public void undermine() {
    mined = true;
  }

  void setOpen(boolean open) {
    this.open = open;
  }

  public Boolean isOpen() {
    return open;
  }

  public Boolean isClosed() {
    return !isOpen();
  }

  public Boolean goalAchieved() {
    boolean uncovered = !mined && open;
    boolean protectedField = mined && marked;

    return uncovered || protectedField;
  }

  public Long minesInNeighborhood() {
    return neighbors.stream()
        .filter(neighbor -> neighbor.mined)
        .count();
  }

  public void restart() {
    open = false;
    mined = false;
    marked = false;
  }

  @Override
  public String toString() {
    if (marked) {
      return "x";
    } else if (open && mined) {
      return "*";
    } else if (open && this.minesInNeighborhood() > 0) {
      return Long.toString(minesInNeighborhood());
    } else if (open) {
      return " ";
    } else {
      return "#";
    }
  }

  public Integer getLine() {
    return line;
  }

  public Integer getColumn() {
    return column;
  }
}
