/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.fiu.scg.util;

import java.util.ArrayList;

/**
 *
 * @author amahm008
 */
public class MLab
{

        public static double sum(double[] data)
        {
                double total = 0;

                for (int i = 0; i < data.length; i++)
                {
                        total += data[i];
                }
                return total;
        }

        public static void mul(double[] data, double multiplicator)
        {

                for (int i = 0; i < data.length; i++)
                {
                        data[i] = data[i] * multiplicator;
                }

        }

        public static void div(double[] data, double divisor)
        {

                for (int i = 0; i < data.length; i++)
                {
                        data[i] = data[i] / divisor;
                }

        }

        public static double[] mulR(double[] data, double multiplicator)
        {

                double[] newData = new double[data.length];
                for (int i = 0; i < data.length; i++)
                {
                        newData[i] = data[i] * multiplicator;
                }

                return newData;

        }

        public static double[] addR(double[] data, double additionBase)
        {

                double[] newData = new double[data.length];
                for (int i = 0; i < data.length; i++)
                {
                        newData[i] = data[i] + additionBase;
                }

                return newData;

        }

        public static double[] addR(double[] data, double[] additionBase)
        {

                double[] newData = new double[data.length];
                for (int i = 0; i < data.length; i++)
                {
                        newData[i] = data[i] + additionBase[i];
                }

                return newData;

        }

        public static double[] mulR(double[] data, double[] multiplicator)
        {

                double[] newData = new double[data.length];
                for (int i = 0; i < data.length; i++)
                {
                        newData[i] = data[i] * multiplicator[i];
                }

                return newData;

        }

        public static double[] mulR(int[] data, double[] multiplicator)
        {

                double[] newData = new double[data.length];
                for (int i = 0; i < data.length; i++)
                {
                        newData[i] = data[i] * multiplicator[i];
                }

                return newData;

        }

        public static double[] divR(double[] data, double divisor)
        {

                double[] newData = new double[data.length];
                for (int i = 0; i < data.length; i++)
                {
                        newData[i] = data[i] / divisor;
                }

                return newData;

        }

        public static double[] divR(double[] data, double[] divisor)
        {

                double[] newData = new double[data.length];
                for (int i = 0; i < data.length; i++)
                {
                        newData[i] = data[i] / divisor[i];
                }

                return newData;

        }

        /**
         * Returns the maximum value in the array a[], -infinity if no such value.
         */
        public static double max(double[] a)
        {
                double max = Double.NEGATIVE_INFINITY;
                for (int i = 0; i < a.length; i++)
                {
                        if (Double.isNaN(a[i]))
                        {
                                return Double.NaN;
                        }
                        if (a[i] > max)
                        {
                                max = a[i];
                        }
                }
                return max;
        }

        /**
         * Returns the maximum value in the subarray a[lo..hi], -infinity if no such value.
         */
        public static double max(double[] a, int lo, int hi)
        {
                if (lo < 0 || hi >= a.length || lo > hi)
                {
                        throw new RuntimeException("Subarray indices out of bounds");
                }
                double max = Double.NEGATIVE_INFINITY;
                for (int i = lo; i <= hi; i++)
                {
                        if (Double.isNaN(a[i]))
                        {
                                return Double.NaN;
                        }
                        if (a[i] > max)
                        {
                                max = a[i];
                        }
                }
                return max;
        }

        /**
         * Returns the maximum value in the array a[], Integer.MIN_VALUE if no such value.
         */
        public static int max(int[] a)
        {
                int max = Integer.MIN_VALUE;
                for (int i = 0; i < a.length; i++)
                {
                        if (a[i] > max)
                        {
                                max = a[i];
                        }
                }
                return max;
        }

        /**
         * Returns the minimum value in the array a[], +infinity if no such value.
         */
        public static double min(double[] a)
        {
                double min = Double.POSITIVE_INFINITY;
                for (int i = 0; i < a.length; i++)
                {
                        if (Double.isNaN(a[i]))
                        {
                                return Double.NaN;
                        }
                        if (a[i] < min)
                        {
                                min = a[i];
                        }
                }
                return min;
        }

        /**
         * Returns the minimum value in the subarray a[lo..hi], +infinity if no such value.
         */
        public static double min(double[] a, int lo, int hi)
        {
                if (lo < 0 || hi >= a.length || lo > hi)
                {
                        throw new RuntimeException("Subarray indices out of bounds");
                }
                double min = Double.POSITIVE_INFINITY;
                for (int i = lo; i <= hi; i++)
                {
                        if (Double.isNaN(a[i]))
                        {
                                return Double.NaN;
                        }
                        if (a[i] < min)
                        {
                                min = a[i];
                        }
                }
                return min;
        }

        /**
         * Returns the minimum value in the array a[], Integer.MAX_VALUE if no such value.
         */
        public static int min(int[] a)
        {
                int min = Integer.MAX_VALUE;
                for (int i = 0; i < a.length; i++)
                {
                        if (a[i] < min)
                        {
                                min = a[i];
                        }
                }
                return min;
        }

        /**
         * Returns the average value in the array a[], NaN if no such value.
         */
        public static double mean(double[] a)
        {
                if (a.length == 0)
                {
                        return Double.NaN;
                }
                double sum = sum(a);
                return sum / a.length;
        }

        /**
         * Returns the average value in the subarray a[lo..hi], NaN if no such value.
         */
        public static double mean(double[] a, int lo, int hi)
        {
                int length = hi - lo + 1;
                if (lo < 0 || hi >= a.length || lo > hi)
                {
                        throw new RuntimeException("Subarray indices out of bounds");
                }
                if (length == 0)
                {
                        return Double.NaN;
                }
                double sum = sum(a, lo, hi);
                return sum / length;
        }

        /**
         * Returns the average value in the array a[], NaN if no such value.
         */
        public static double mean(int[] a)
        {
                if (a.length == 0)
                {
                        return Double.NaN;
                }
                double sum = 0.0;
                for (int i = 0; i < a.length; i++)
                {
                        sum = sum + a[i];
                }
                return sum / a.length;
        }

        /**
         * Returns the sample variance in the array a[], NaN if no such value.
         */
        public static double var(double[] a)
        {
                if (a.length == 0)
                {
                        return Double.NaN;
                }
                double avg = mean(a);
                double sum = 0.0;
                for (int i = 0; i < a.length; i++)
                {
                        sum += (a[i] - avg) * (a[i] - avg);
                }
                return sum / (a.length - 1);
        }

        /**
         * Returns the sample variance in the subarray a[lo..hi], NaN if no such value.
         */
        public static double var(double[] a, int lo, int hi)
        {
                int length = hi - lo + 1;
                if (lo < 0 || hi >= a.length || lo > hi)
                {
                        throw new RuntimeException("Subarray indices out of bounds");
                }
                if (length == 0)
                {
                        return Double.NaN;
                }
                double avg = mean(a, lo, hi);
                double sum = 0.0;
                for (int i = lo; i <= hi; i++)
                {
                        sum += (a[i] - avg) * (a[i] - avg);
                }
                return sum / (length - 1);
        }

        /**
         * Returns the sample variance in the array a[], NaN if no such value.
         */
        public static double var(int[] a)
        {
                if (a.length == 0)
                {
                        return Double.NaN;
                }
                double avg = mean(a);
                double sum = 0.0;
                for (int i = 0; i < a.length; i++)
                {
                        sum += (a[i] - avg) * (a[i] - avg);
                }
                return sum / (a.length - 1);
        }

        /**
         * Returns the population variance in the array a[], NaN if no such value.
         */
        public static double varp(double[] a)
        {
                if (a.length == 0)
                {
                        return Double.NaN;
                }
                double avg = mean(a);
                double sum = 0.0;
                for (int i = 0; i < a.length; i++)
                {
                        sum += (a[i] - avg) * (a[i] - avg);
                }
                return sum / a.length;
        }

        /**
         * Returns the population variance in the subarray a[lo..hi], NaN if no such value.
         */
        public static double varp(double[] a, int lo, int hi)
        {
                int length = hi - lo + 1;
                if (lo < 0 || hi >= a.length || lo > hi)
                {
                        throw new RuntimeException("Subarray indices out of bounds");
                }
                if (length == 0)
                {
                        return Double.NaN;
                }
                double avg = mean(a, lo, hi);
                double sum = 0.0;
                for (int i = lo; i <= hi; i++)
                {
                        sum += (a[i] - avg) * (a[i] - avg);
                }
                return sum / length;
        }

        /**
         * Returns the sample standard deviation in the array a[], NaN if no such value.
         */
        public static double stddev(double[] a)
        {
                return Math.sqrt(var(a));
        }

        /**
         * Returns the sample standard deviation in the subarray a[lo..hi], NaN if no such value.
         */
        public static double stddev(double[] a, int lo, int hi)
        {
                return Math.sqrt(var(a, lo, hi));
        }

        /**
         * Returns the sample standard deviation in the array a[], NaN if no such value.
         */
        public static double stddev(int[] a)
        {
                return Math.sqrt(var(a));
        }

        /**
         * Returns the population standard deviation in the array a[], NaN if no such value.
         */
        public static double stddevp(double[] a)
        {
                return Math.sqrt(varp(a));
        }

        /**
         * Returns the population standard deviation in the subarray a[lo..hi], NaN if no such value.
         */
        public static double stddevp(double[] a, int lo, int hi)
        {
                return Math.sqrt(varp(a, lo, hi));
        }

        /**
         * Returns the sum of all values in the subarray a[lo..hi].
         */
        public static double sum(double[] a, int lo, int hi)
        {
                if (lo < 0 || hi >= a.length || lo > hi)
                {
                        throw new RuntimeException("Subarray indices out of bounds");
                }
                double sum = 0.0;
                for (int i = lo; i <= hi; i++)
                {
                        sum += a[i];
                }
                return sum;
        }

        /**
         * Returns the sum of all values in the array a[].
         */
        public static int sum(int[] a)
        {
                int sum = 0;
                for (int i = 0; i < a.length; i++)
                {
                        sum += a[i];
                }
                return sum;
        }

        public static int[] find(double[] data, double comparator, boolean eq)
        {

                ArrayList<Integer> indexList = new ArrayList();
                for (int i = 0; i < data.length; i++)
                {
                        if ((data[i] == comparator) == eq)
                        {
                                indexList.add(i);
                        }
                }

                int[] indexes = new int[indexList.size()];

                for (int i = 0; i < indexes.length; i++)
                {
                        indexes[i] = indexList.get(i);
                }

                return indexes;
        }

        public static int[] find(int[] data, int comparator, boolean eq)
        {

                ArrayList<Integer> indexList = new ArrayList();
                for (int i = 0; i < data.length; i++)
                {
                        if ((data[i] == comparator) == eq)
                        {
                                indexList.add(i);
                        }
                }

                int[] indexes = new int[indexList.size()];

                for (int i = 0; i < indexes.length; i++)
                {
                        indexes[i] = indexList.get(i);
                }

                return indexes;
        }

        public static double[] valuesAt(double[] data, int []indexes)
        {
                double [] newData = new double[indexes.length];
                for (int i = 0; i < indexes.length; i++)
                {
                        newData [i] = data[indexes[i]];
                }
                return newData;
        }

        public static double[] assign(double[] data, int[] index, double value)
        {

                double[] newData = data.clone();

                for (int i = 0; i < index.length; i++)
                {
                        newData[index[i]] = value;
                }

                return newData;
        }

        public static double[] assign(double[] data, int[] index, double[] value)
        {

                double[] newData = data.clone();

                for (int i = 0; i < index.length; i++)
                {
                        newData[index[i]] = value[i];
                }

                return newData;
        }

}
