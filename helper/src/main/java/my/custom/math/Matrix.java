package my.custom.math;

import my.custom.math.functions.activation.ActivationFunction;

public class Matrix {

  private int rows = 0;
  private int columns = 0;

  private String name = null;

  private double[][] matrix;

  public Matrix(int columns, int rows) {
    this.rows = rows;
    this.columns = columns;
    this.matrix = new double[rows][columns];
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  protected double[][] getMatrix() {
    return this.matrix;
  }

  protected Matrix(Matrix matrix) {
    this.matrix = matrix.getMatrix();
    this.rows = matrix.getRows();
    this.columns = matrix.getColumns();
  }

  public int getRows() {
    return this.rows;
  }

  public int getColumns() {
    return this.columns;
  }

  public double getValue(int column, int row) {
    return this.matrix[row][column];
  }

  public void setValue(int column, int row, double value) {
    this.matrix[row][column] = value;
  }

  public void setAllValuesTo(double value) {
    for (int row = 0; row < this.rows; row++) {
      for (int column = 0; column < this.columns; column++) {
        this.setValue(column, row, value);
      }
    }
  }

  private void throwsIfDifferentDimensions(Matrix other) {
    if (this.getRows() != other.getRows()) {
      throw new IllegalArgumentException("different count of rows");
    }

    if (this.getColumns() != other.getColumns()) {
      throw new IllegalArgumentException("different count of columns");
    }
  }

  public Matrix transpone() throws Exception {
    Matrix result = new Matrix(this.getRows(), this.getColumns());
    for (int row = 0; row < this.rows; row++) {
      for (int column = 0; column < this.columns; column++) {
        double thisValue = this.getValue(column, row);
        result.setValue(row, column, thisValue);
      }
    }
    return result;
  }

  public Matrix add(Matrix other) {
    throwsIfDifferentDimensions(other);

    Matrix result = new Matrix(this.getColumns(), this.getRows());

    for (int row = 0; row < this.rows; row++) {
      for (int column = 0; column < this.columns; column++) {
        double thisValue = this.getValue(column, row);
        double otherValue = other.getValue(column, row);
        result.setValue(column, row, thisValue + otherValue);
      }
    }
    return result;
  }

  public void add(double delta) {
    for (int row = 0; row < this.rows; row++) {
      for (int column = 0; column < this.columns; column++) {
        double value = this.getValue(column, row);
        this.setValue(column, row, value + delta);
      }
    }
  }

  public Matrix subtract(Matrix other) {
    throwsIfDifferentDimensions(other);
    Matrix result = new Matrix(this.getColumns(), this.getRows());

    for (int row = 0; row < this.rows; row++) {
      for (int column = 0; column < this.columns; column++) {
        double thisValue = this.getValue(column, row);
        double otherValue = other.getValue(column, row);
        result.setValue(column, row, thisValue - otherValue);
      }
    }
    return result;
  }

  public Matrix multiply(Matrix other) {

    int l = this.getRows();
    int n = other.getColumns();

    int m_this = this.getColumns();
    int m_other = other.getRows();

    if (m_this != m_other) {
      throw new IllegalArgumentException("dimensions are incorrect for multiplication");
    }

    Matrix result = new Matrix(n, l);

    for (int j = 0; j < m_this; j++) {
      for (int i = 0; i < l; i++) {
        for (int k = 0; k < n; k++) {
          double a_i_j = this.getValue(j, i);
          double b_j_k = other.getValue(k, j);
          double c_i_k = a_i_j * b_j_k;
          double previous_c_i_k = result.getValue(k, i);
          result.setValue(k, i, previous_c_i_k + c_i_k);
        }
      }
    }

    return result;
  }

  public void multiply(double scalar) {
    for (int row = 0; row < this.rows; row++) {
      for (int column = 0; column < this.columns; column++) {
        double value = this.getValue(column, row);
        this.setValue(column, row, value * scalar);
      }
    }
  }

  @Override
  public String toString() {
    String result = "";

    if (this.name != null) {
      result += "Matrix " + this.name + "\n";
    }

    for (int row = 0; row < this.rows; row++) {
      for (int column = 0; column < this.columns; column++) {
        result += "\t\t" + this.matrix[row][column];
      }
      result += "\n";
    }
    return result;
  }

  public Matrix activate(ActivationFunction activationFunction) {
    Matrix result = new Matrix(this.getColumns(), this.getRows());

    for (int row = 0; row < this.rows; row++) {
      for (int column = 0; column < this.columns; column++) {
        double thisValue = this.getValue(column, row);
        double activated = activationFunction.activate(thisValue);
        result.setValue(column, row, activated);
      }
    }
    return result;
  }
}
