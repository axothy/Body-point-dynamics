package ru.axothy.mechanics.tensors;

import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.Arrays;

public class Tensor {
    public static final short DIMENSION = 3;
    private ArrayList<ArrayList<Double>> array = new ArrayList<>();

    public static final Tensor ZERO_TENSOR = new Tensor(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    public static final Tensor UNARY_TENSOR = new Tensor(1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0);

    private Tensor() {
        throw new AssertionError();
    }

    public Tensor(ArrayList<ArrayList<Double>> array) {
        this.array = array;
    }

    public Tensor(Double... values) {
        if (values.length != 9) {
            //exception
        }

        for (int i = 0; i < DIMENSION * DIMENSION - DIMENSION + 1; i += 3) {
            array.add(new ArrayList<Double>(Arrays.asList(values[i], values[i + 1], values[i + 2])));
        }
    }

    public Tensor(double[][] array) {
        for (int i = 0; i < DIMENSION; i++) {
            ArrayList<Double> inner = new ArrayList<>();
            for (int j = 0; j < DIMENSION; j++) {
                inner.add(array[i][j]);
            }
            this.array.add(inner);
        }
    }

    public ArrayList<ArrayList<Double>> getArray() {
        return this.array;
    }

    public Tensor multiply(double scalar) {
        ArrayList<ArrayList<Double>> outer = new ArrayList<>();
        for (int i = 0; i < DIMENSION; i++) {
            ArrayList<Double> inner = new ArrayList<>();
            for (int j = 0; j < DIMENSION; j++) {
                inner.add(scalar * getArray().get(i).get(j));
            }
            outer.add(inner);
        }

        return new Tensor(outer);
    }

    public Tensor subtract(Tensor tensor) {
        ArrayList<ArrayList<Double>> outer = new ArrayList<>();
        for (int i = 0; i < DIMENSION; i++) {
            ArrayList<Double> inner = new ArrayList<>();
            for (int j = 0; j < DIMENSION; j++) {
                inner.add(this.getArray().get(i).get(j) - tensor.getArray().get(i).get(j));
            }
            outer.add(inner);
        }

        return new Tensor(outer);
    }

    public Tensor add(Tensor tensor) {
        ArrayList<ArrayList<Double>> outer = new ArrayList<>();
        for (int i = 0; i < DIMENSION; i++) {
            ArrayList<Double> inner = new ArrayList<>();
            for (int j = 0; j < DIMENSION; j++) {
                inner.add(this.getArray().get(i).get(j) + tensor.getArray().get(i).get(j));
            }
            outer.add(inner);
        }

        return new Tensor(outer);
    }

    public Tensor multiply(Tensor tensor) {
        double[][] matrix = new double[DIMENSION][DIMENSION];

        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                for (int k = 0; k < DIMENSION; k++) {
                    matrix[i][j] += this.getArray().get(i).get(k) * tensor.getArray().get(k).get(j);
                }
            }
        }

        Double[] result = new Double[DIMENSION * DIMENSION];

        int k = 0;
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                result[k] = matrix[i][j];
                k++;
            }
        }

        return new Tensor(result);
    }

    public Point3D dotProduct(Point3D vector) {
        double[] points = new double[DIMENSION];

        for (int i = 0; i < DIMENSION; i++) {
            points[i] += vector.getX() * getArray().get(i).get(0);
            points[i] += vector.getY() * getArray().get(i).get(1);
            points[i] += vector.getZ() * getArray().get(i).get(2);
        }

        return new Point3D(points[0], points[1], points[2]);
    }

    public Tensor inverse() {
        double[][] matrix = new double[DIMENSION][DIMENSION];

        double determinant = 0;
        for (int i = 0; i < DIMENSION; i++) {
            determinant = determinant + (this.array.get(0).get(i) *
                    (this.array.get(1).get((i + 1) % 3) * this.array.get(2).get((i + 2) % 3) -
                            this.array.get(1).get((i + 2) % 3) * this.array.get(2).get((i + 1) % 3)));
        }

        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                matrix[i][j] = ((this.array.get((j + 1) % 3).get((i + 1) % 3) * this.array.get((j + 2) % 3).get((i + 2) % 3)) -
                        (this.array.get((j + 1) % 3).get((i + 2) % 3) * this.array.get((j + 2) % 3).get((i + 1) % 3))) / determinant;
            }
        }

        return new Tensor(matrix);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Tensor values:\n");
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                stringBuilder.append(array.get(i).get(j) + "  ");
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    //TODO implement
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public static void main(String[] args) {
        Tensor tensor = new Tensor(3.0, 2.0, 3.0, 2.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        Tensor tensor2 = new Tensor(0.0, 3.0, 4.0, 1.0, -1.0, 3.0, 3.0, 8.0, -2.0);

        System.out.println(tensor.add(tensor2));
    }
}
