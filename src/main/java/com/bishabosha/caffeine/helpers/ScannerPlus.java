/*
 * Copyright (c) 2017. Jamie Thompson <bishbashboshjt@gmail.com>
 */

package com.bishabosha.caffeine.helpers;

import java.io.Closeable;
import java.io.InputStream;
import java.util.Scanner;
import java.util.function.Consumer;

public class ScannerPlus implements Closeable {
	public Scanner input;
	
	public ScannerPlus(InputStream input) {
		this.input = new Scanner(input);
	}
	
	public void close() {
		input.close();
	}
	
	public String readLine() {
		if (input.hasNextLine()) return input.nextLine();
		return "";
	}
	
	public String readNext() {
		String result = "";
		if (input.hasNext()) result = input.next();
		readLine();
		return result;
	}
	
	public int readInt() {
		int result = Integer.MAX_VALUE;
		if (input.hasNextInt()) result = input.nextInt();
		readLine();
		return result;
	}
	
	public float readFloat() {
		float result = Float.NaN;
		if (input.hasNextFloat()) result = input.nextFloat();
		readLine();
		return result;
	}
	
	public double readDouble() {
		double result = Double.NaN;
		if (input.hasNextDouble()) result = input.nextDouble();
		readLine();
		return result;
	}
	
	public String[] readVector(int numValues) {
		return readVector(numValues, null);
	}
	
	public String[] readVector(int numValues, Consumer<String> consumer) {
		String[] result = new String[numValues];
		for (int i = 0; i < numValues; i++) {
			if (input.hasNext()) result[i] = input.next();
			if (consumer != null) consumer.accept(result[i]);
		}
		input.nextLine();
		return result;
	}
	
	public int[] readVectorInts(int numValues) {
		return readVectorInts(numValues, null);
	}
	
	public int[] readVectorInts(int numValues, Consumer<Integer> consumer) {
		int[] result = new int[numValues];
		for (int i = 0; i < numValues; i++) {
			if (input.hasNextInt()) result[i] = input.nextInt();
			if (consumer != null) consumer.accept(result[i]);
		}
		input.nextLine();
		return result;
	}
	
	public float[] readVectorFloats(int numValues) {
		return readVectorFloats(numValues, null);
	}
	
	public float[] readVectorFloats(int numValues, Consumer<Float> consumer) {
		float[] result = new float[numValues];
		for (int i = 0; i < numValues; i++) {
			if (input.hasNextFloat()) result[i] = input.nextFloat();
			if (consumer != null) consumer.accept(result[i]);
		}
		input.nextLine();
		return result;
	}
	
	public double[] readVectorDoubles(int numValues) {
		return readVectorDoubles(numValues, null);
	}
	
	public double[] readVectorDoubles(int numValues, Consumer<Double> consumer) {
		double[] result = new double[numValues];
		for (int i = 0; i < numValues; i++) {
			if (input.hasNextDouble()) result[i] = input.nextDouble();
			if (consumer != null) consumer.accept(result[i]);
		}
		input.nextLine();
		return result;
	}
	
	public String[][] readMatrix(int rows, int cols) {
		return readMatrix(rows, cols, null);
	}
	
	public String[][] readMatrix(int rows, int cols, Consumer<String> consumer) {
		String[][] result = new String[rows][];
		for (int i = 0; i < rows; i++) {
			result[i] = readVector(cols, consumer);
		}
		return result;
	}
	
	public int[][] readMatrixInts(int rows, int cols) {
		return readMatrixInts(rows, cols, null);
	}
	
	public int[][] readMatrixInts(int rows, int cols, Consumer<Integer> consumer) {
		int[][] result = new int[rows][];
		for (int i = 0; i < rows; i++) {
			result[i] = readVectorInts(cols, consumer);
		}
		return result;
	}
	
	public float[][] readMatrixFloat(int rows, int cols) {
		return readMatrixFloats(rows, cols, null);
	}
	
	public float[][] readMatrixFloats(int rows, int cols, Consumer<Float> consumer) {
		float[][] result = new float[rows][];
		for (int i = 0; i < rows; i++) {
			result[i] = readVectorFloats(cols, consumer);
		}
		return result;
	}
	
	public double[][] readMatrixDoubles(int rows, int cols) {
		return readMatrixDoubles(rows, cols, null);
	}
	
	public double[][] readMatrixDoubles(int rows, int cols, Consumer<Double> consumer) {
		double[][] result = new double[rows][];
		for (int i = 0; i < rows; i++) {
			result[i] = readVectorDoubles(cols, consumer);
		}
		return result;
	}
}
