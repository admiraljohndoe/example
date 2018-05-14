package my.custom.math.functions.activation;

import my.custom.math.functions.activation.ActivationFunction;

public class SigmoidFunction implements ActivationFunction {

  public double activate(double x) {
    return (1.0d / (1.0d + Math.exp(-1.0d * x)));
  }

}
