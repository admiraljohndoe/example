package my.custom.math;

public final class LineVector extends Vector {
  
  public LineVector(int countColumnValues) {
    super(1, countColumnValues);
  }
  
  public LineVector(double[] byValues) {
    this(byValues.length);
    for (int position = 0; position < byValues.length; position++) {
      setValue(position, byValues[position]);
    }
  }
  
  protected LineVector(Matrix matrix) {
    super(matrix);
  }

  public void setValue(int position, double value) {
    setValue(0, position, value);
  }
}
