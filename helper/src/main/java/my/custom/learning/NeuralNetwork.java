package my.custom.learning;

import java.util.Random;
import my.custom.math.Matrix;
import my.custom.math.ColumnVector;
import my.custom.math.Vector;
import my.custom.math.functions.activation.ActivationFunction;

/***
 * This class represents a neural network.
 */
public class NeuralNetwork {

  private ActivationFunction activationFunction;

  /**
   * the weights of the neural network on index i are the weights between layer i and i+1
   */
  private Matrix[] weights;

  /**
   * learning rate
   */
  private double alpha = 0.1d;

  /**
   * this method checks the specified counts of nodes for each layer
   * 
   * @param countNodesOnlayer
   */
  private static void throwIfValuesAreInvalid(int[] countNodesOnlayer) {
    if (countNodesOnlayer == null || countNodesOnlayer.length == 0) {
      throw new IllegalArgumentException("no network specified");
    }

    for (int pos = 0; pos < countNodesOnlayer.length; pos++) {
      int count = countNodesOnlayer[pos];
      if (count <= 0) {
        throw new IllegalArgumentException("count of nodes on layer " + pos + " cannot be negativ or 0");
      }
    }
  }

  public NeuralNetwork(ActivationFunction activationFunction, int[] countNodesBetweenLayerAndSucceedingLayer, double learningRate) {
    throwIfValuesAreInvalid(countNodesBetweenLayerAndSucceedingLayer);
    this.activationFunction = activationFunction;
    this.alpha = learningRate;
    this.weights = new Matrix[countNodesBetweenLayerAndSucceedingLayer.length - 1];
    for (int pos = 0; pos < countNodesBetweenLayerAndSucceedingLayer.length - 1; pos++) {
      int count = countNodesBetweenLayerAndSucceedingLayer[pos];
      int countNext = countNodesBetweenLayerAndSucceedingLayer[pos + 1];
      Matrix m = new Matrix(countNext, count);
      initializeMatrix(m);
      weights[pos] = m;
    }
  }

  /**
   * returns the weights between layer - 1 and layer
   * 
   * @param lowerLayerIndex
   * @return
   */
  public Matrix getWeightsOfLayer(int lowerLayerIndex) {
    Matrix result = this.weights[lowerLayerIndex];
    return result;
  }

  /**
   * set the weights between layer - 1 and layer
   * 
   * @param lowerLayerIndex
   * @param weights
   * @throws Exception
   */
  public void setWeightsOfLayer(int lowerLayerIndex, Matrix weights) throws Exception {
    this.weights[lowerLayerIndex] = weights;
  }

  /**
   * returns all the weights
   * 
   * @return
   */
  public Matrix[] getWeights() {
    return this.weights;
  }

  /**
   * initializes weights in a given matrix
   * @param m
   */
  private void initializeMatrix(Matrix m) {
    Random random = new Random();
    for (int row = 0; row < m.getRows(); row++) {
      for (int col = 0; col < m.getColumns(); col++) {
        double x = activationFunction.activate(random.nextDouble());
        m.setValue(col, row, x);
      }
    }
  }

  public ColumnVector query(ColumnVector input) throws Exception {
    Matrix intermediate = input;
    for (int currentHandledWeightsBetweenLayers = 0; currentHandledWeightsBetweenLayers < this.weights.length; currentHandledWeightsBetweenLayers++) {
      Matrix weightsOfLayer = this.getWeightsOfLayer(currentHandledWeightsBetweenLayers);
      intermediate = weightsOfLayer.multiply(intermediate);
      intermediate = intermediate.activate(this.activationFunction);
    }
    ColumnVector output = (ColumnVector) Vector.convert(intermediate);
    return output;
  }

  public void train(ColumnVector input, ColumnVector expectedResults) throws Exception {

    int outputsIndex = 0;
    Matrix[] outputsOfLayers = new Matrix[this.weights.length + 1];

    outputsOfLayers[outputsIndex] = input;

    // get the output and all the intermediate results as type Matrix
    {
      Matrix intermediate = input;
      for (int currentHandledWeightsBetweenLayers = 0; currentHandledWeightsBetweenLayers < this.weights.length; currentHandledWeightsBetweenLayers++) {
        Matrix weightsOfLayer = this.getWeightsOfLayer(currentHandledWeightsBetweenLayers);
        intermediate = weightsOfLayer.multiply(intermediate);
        intermediate = intermediate.activate(this.activationFunction);

        outputsIndex++;
        outputsOfLayers[outputsIndex] = intermediate;
      }
    }

    ColumnVector one = new ColumnVector(expectedResults.getRows());
    one.setAllValuesTo(1.0d);

    // now do the training stuff ... run backwards through the neural network
    Matrix e = expectedResults;
    for (int layer = outputsOfLayers.length - 1; layer > 0; layer--) {

      Matrix w = this.getWeightsOfLayer(layer - 1);
      Matrix wTransponed = w.transpone();
      {
        Matrix oOutputs = outputsOfLayers[layer];
        Matrix oInputs = outputsOfLayers[layer - 1].transpone();

        for (int kNodesCount = 0; kNodesCount < oOutputs.getRows(); kNodesCount++) {

          // calculate the sums
          double sumj = 0.0d;

          for (int jNodesCount = 0; jNodesCount < oInputs.getColumns(); jNodesCount++) {
            double wjk = w.getValue(jNodesCount, kNodesCount);
            double oj = oInputs.getValue(jNodesCount, 0);
            sumj += (wjk * oj);
          }

          sumj = this.activationFunction.activate(sumj);

          for (int jNodesCount = 0; jNodesCount < oInputs.getColumns(); jNodesCount++) {
            double wjk = w.getValue(jNodesCount, kNodesCount);
            double ojInput = oInputs.getValue(jNodesCount, 0);
            double ej = e.getValue(0, jNodesCount);
            double wjkNew = (-1.0d) * ej * sumj * (1.0d - sumj) * ojInput;
            wjk = wjk + ((-1.0d) * (this.alpha * wjkNew));
            w.setValue(jNodesCount, kNodesCount, wjk);
          }
        }
      }
      // w is already updated
      e = wTransponed.multiply(e);
    }

  }
}
