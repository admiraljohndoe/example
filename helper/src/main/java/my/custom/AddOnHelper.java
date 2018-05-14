package my.custom;

import my.custom.math.Matrix;
import my.custom.math.ColumnVector;
import my.custom.math.functions.activation.ActivationFunction;
import my.custom.math.functions.activation.SigmoidFunction;

import my.custom.learning.NeuralNetwork;

public class AddOnHelper {

  public static void main(String[] args) {
    try {
      ActivationFunction activationFunction = new SigmoidFunction();
      NeuralNetwork n = new NeuralNetwork(activationFunction, new int[] { 2, 2, 2 }, 0.1d);

      {
        Matrix pdw = new Matrix(2, 2);
        pdw.setValue(0, 0, 3.0d);
        pdw.setValue(0, 1, 1.0d);

        pdw.setValue(1, 0, 2.0d);
        pdw.setValue(1, 1, 7.0d);

        n.setWeightsOfLayer(0, pdw);
      }

      {
        Matrix pdw = new Matrix(2, 2);
        pdw.setValue(0, 0, 2.0d);
        pdw.setValue(0, 1, 1.0d);

        pdw.setValue(1, 0, 3.0d);
        pdw.setValue(1, 1, 4.0d);

        n.setWeightsOfLayer(1, pdw);
      }

      ColumnVector input = new ColumnVector(2);
      input.setName("input");
      input.setValue(0, 0.4d);
      input.setValue(1, 0.5d);
      // System.out.println(input);

      ColumnVector expectedOutput = new ColumnVector(2);
      expectedOutput.setName("expected output");
      expectedOutput.setValue(0, 0.8d);
      expectedOutput.setValue(1, 0.5d);
      // System.out.println(expectedOutput);

      n.train(input, expectedOutput);
    } catch (Exception e) {
      System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
    }
  }
}
