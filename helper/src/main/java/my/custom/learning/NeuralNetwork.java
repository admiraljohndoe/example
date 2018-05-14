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
   * @param countNodesOnLayer
   */
  private static void throwIfValuesAreInvalid(int[] countNodesOnLayer) {
    if (countNodesOnLayer == null || countNodesOnLayer.length == 0) {
      throw new IllegalArgumentException("no network specified");
    }

    for (int pos = 0; pos < countNodesOnLayer.length; pos++) {
      int count = countNodesOnLayer[pos];
      if (count <= 0) {
        throw new IllegalArgumentException("count of nodes on layer " + pos + " cannot be negativ or 0");
      }
    }
  }

  /**
   * constructor of the neural network
   * 
   * @param activationFunction an activation function
   * @param countNodesOnLayer count of nodes on layer 1 is represented by an integer value on position 0, and so on (no 0 values allowed in this array)
   * @param learningRate how fast learning will be done
   */
  public NeuralNetwork(ActivationFunction activationFunction, int[] countNodesOnLayer, double learningRate) {
    throwIfValuesAreInvalid(countNodesOnLayer);
    this.activationFunction = activationFunction;
    this.alpha = learningRate;
    this.weights = new Matrix[countNodesOnLayer.length - 1];
    for (int pos = 0; pos < countNodesOnLayer.length - 1; pos++) {
      int count = countNodesOnLayer[pos];
      int countNext = countNodesOnLayer[pos + 1];
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

  /**
   * queries the neural network and returns a ColumnVector containing the values of the output layer for a given input
   *    
   * @param input
   * @return
   */
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

  /**
   * trains the neural network for a given input and for results which should be returned when querying the neural network
   * 
   * @param input
   * @param expectedResults
   */
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
