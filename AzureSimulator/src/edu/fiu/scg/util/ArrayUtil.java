/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.fiu.scg.util;

import java.lang.reflect.Array;

/**
 *
 * @author amahm008
 */
public class ArrayUtil
{

        public static <T> T[] concatenate(T[] A, T[] B)
        {
                int aLen = A.length;
                int bLen = B.length;

                @SuppressWarnings("unchecked")
                T[] C = (T[]) Array.newInstance(A.getClass().getComponentType(), aLen + bLen);
                System.arraycopy(A, 0, C, 0, aLen);
                System.arraycopy(B, 0, C, aLen, bLen);

                return C;
        }

        public static double[] concatenateArray(double[] A, double[] B)
        {
                int aLen = A.length;
                int bLen = B.length;

                @SuppressWarnings("unchecked")
                double[] C = (double[]) Array.newInstance(A.getClass().getComponentType(), aLen + bLen);
                System.arraycopy(A, 0, C, 0, aLen);
                System.arraycopy(B, 0, C, aLen, bLen);

                return C;
        }
        
        public static double [] convertToOneDimention(double [][] values)
        {
                int secondDimLength = values[0].length;
                double [] oneDimValue = new double [values.length * secondDimLength];
                int columnIndex = 0;
                for (int i = 0; i < values.length; i++)
                {
                        columnIndex = i * secondDimLength;
                        for (int j = 0; j < values[0].length; j++)
                        {
                                oneDimValue[columnIndex + j ] = values[i][j];
                        }

                }
                
                return oneDimValue;
        }
        
       
        
        public static String getCSVString(double[][] twoDimArray)
        {
                StringBuilder stBuilder = new StringBuilder();
                for (double[] twoDimArray1 : twoDimArray)
                {
                        stBuilder.append(getCSVString(twoDimArray1));
                        stBuilder.append("\n");
                }
                return stBuilder.toString();
        }
        

        public static String getCSVString(double[] oneDimArray)
        {
                StringBuilder stBuilder = new StringBuilder();
                
                if(oneDimArray.length > 0)
                {
                        stBuilder.append(oneDimArray[0]);
                }
                
                for (int i = 1; i < oneDimArray.length; i++)
                {
                        stBuilder.append(",");
                        stBuilder.append(oneDimArray[i]);
                        

                        
                }
                return stBuilder.toString();
        }
        
        public static String getCSVString(int[] oneDimArray)
        {
                StringBuilder stBuilder = new StringBuilder();
                
                if(oneDimArray.length > 0)
                {
                        stBuilder.append(oneDimArray[0]);
                }
                
                for (int i = 1; i < oneDimArray.length; i++)
                {
                        stBuilder.append(",");
                        stBuilder.append(oneDimArray[i]);
                        

                        
                }
                return stBuilder.toString();
        }        

     
        
}
