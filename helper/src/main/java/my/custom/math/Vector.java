package my.custom.math;

import java.lang.Exception;

public abstract class Vector extends Matrix {

  protected Vector(int rows, int columns) {
    super(columns, rows);
  }

  protected Vector(Matrix matrix) {
    super(matrix);
  }

  public abstract void setValue(int position, double value);

  @Override
  public Vector transpone() throws Exception {
    Matrix transponedMatrix = super.transpone();
    return convert(transponedMatrix);
  }

  public static Vector convert(Matrix matrix) throws Exception {
    Vector result;

    if (matrix.getRows() == 1) {
      result = new LineVector(matrix);
    } else if (matrix.getColumns() == 1) {
      result = new ColumnVector(matrix);
    } else {
      throw new NotAVectorException("A vector cannot be constructed.");
    }

    return result;
  }
}
