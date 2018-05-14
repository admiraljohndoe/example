package my.custom.math;

public final class ColumnVector extends Vector {

  public ColumnVector(int countRowValues) {
    super(countRowValues, 1);
  }

  public ColumnVector(double[] byValues) {
    this(byValues.length);
    for (int position = 0; position < byValues.length; position++) {
      setValue(position, byValues[position]);
    }
  }

  protected ColumnVector(Matrix matrix) {
    super(matrix);
  }

  @Override
  public void setValue(int position, double value) {
    setValue(0, position, value);
  }
}
