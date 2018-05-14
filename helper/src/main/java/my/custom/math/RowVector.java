package my.custom.math;

public final class RowVector extends Vector {
  
  public RowVector(int countColumnValues) {
    super(1, countColumnValues);
  }
  
  public RowVector(double[] byValues) {
    this(byValues.length);
    for (int position = 0; position < byValues.length; position++) {
      setValue(position, byValues[position]);
    }
  }
  
  protected RowVector(Matrix matrix) {
    super(matrix);
  }

  public void setValue(int position, double value) {
    setValue(0, position, value);
  }
}
