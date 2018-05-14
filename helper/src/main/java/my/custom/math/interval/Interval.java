package my.custom.math.interval;

import java.math.BigDecimal;
import my.custom.math.interval.IntervalBoundaryType;

public final class Interval<T extends Number> {

  private T leftEndPoint;
  private T rightEndPoint;

  private IntervalBoundaryType leftBoundaryType;
  private IntervalBoundaryType rightBoundaryType;

  public Interval(IntervalBoundaryType leftType, T leftEndPoint, IntervalBoundaryType rightType, T rightEndPoint) {
    this.leftEndPoint = leftEndPoint;
    this.leftBoundaryType = leftType;

    this.rightEndPoint = rightEndPoint;
    this.rightBoundaryType = rightType;
  }

  public boolean isLeftOpen() {
    return leftBoundaryType == IntervalBoundaryType.OPEN;
  }

  public boolean isRightOpen() {
    return rightBoundaryType == IntervalBoundaryType.OPEN;
  }

  public boolean isOpen() {
    return this.isLeftOpen() && this.isRightOpen();
  }

  public boolean isEmpty() {
    BigDecimal left = new BigDecimal(this.leftEndPoint.toString());
    BigDecimal right = new BigDecimal(this.rightEndPoint.toString());
    int comparisonResult = left.compareTo(right);
    return comparisonResult > 0;
  }

  @Override
  public String toString() {
    String result = "";
    result += (this.isLeftOpen() ? "(" : "[");
    result += this.leftEndPoint;
    result += ";";
    result += this.rightEndPoint;
    result += (this.isRightOpen() ? ")" : "]");
    return result;
  }

}
