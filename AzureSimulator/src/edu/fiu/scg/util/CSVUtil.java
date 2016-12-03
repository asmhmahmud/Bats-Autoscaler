/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.fiu.scg.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author amahm008
 */
public class CSVUtil
{

        private static String preDefinedDelimeter = ",";

        public static void writeCSV(String[] columnHeader, double[][] data, String fileName)
        {
                try
                {

                        FileWriter writer = new FileWriter(fileName);
                        if (columnHeader != null)
                        {
                                writer.append(columnHeader[0]);
                                for (int i = 1; i < columnHeader.length; i++)
                                {
                                        writer.append("," + columnHeader[i]);
                                }
                                writer.append("\n");
                        }

                        if (data != null)
                        {
                                for (double[] data1 : data)
                                {
                                        writer.append(data1[0] + "");
                                        for (int j = 1; j < data1.length; j++)
                                        {
                                                writer.append("," + data1[j]);
                                        }
                                        writer.append("\n");
                                }
                        }

                        writer.flush();
                        writer.close();

                }
                catch (IOException e)
                {
                        e.printStackTrace();
                }
        }

        public static void writeCSV(String[] columnHeader, double[] data)
        {

        }

        public static double[][] readCSV(String fileName, int rowSkip, int colSkip, int totalRow, int totalCol)
        {
                return readCSV(fileName, rowSkip, colSkip, totalRow, totalCol, preDefinedDelimeter);
        }

        public static double[][] readCSV(String fileName, int rowSkip, int colSkip, int totalRow, int totalCol, String delimeter)
        {

                ArrayList<String> stringValues = getStringValuesAsArrayList(fileName, rowSkip, colSkip, totalRow);
                return getArrayFromStrings(stringValues, totalCol, delimeter);

        }

        public static double[][] readCSV(String fileName, int rowSkip, int colSkip)
        {

                ArrayList<String> stringValues = getStringValuesAsArrayList(fileName, rowSkip, colSkip, Integer.MAX_VALUE);
                return getArrayFromStrings(stringValues, -1, preDefinedDelimeter);

        }

        public static double[][] readCSV(String fileName)
        {

                ArrayList<String> stringValues = getStringValuesAsArrayList(fileName, 0, 0, Integer.MAX_VALUE);
                return getArrayFromStrings(stringValues, -1, preDefinedDelimeter);

        }

        private static ArrayList<String> getStringValuesAsArrayList(String fileName, int rowSkip, int colSkip, int totalRow)
        {
                ArrayList<String> stringValues = new ArrayList<>();
                try
                {
                        File file = new File(fileName);
                        FileReader fileReader = new FileReader(file);

                        BufferedReader buffReader = new BufferedReader(fileReader);

                        for (int i = 0; i < rowSkip; i++)
                        {
                                buffReader.readLine(); //skipping the headers
                        }

                        String line = buffReader.readLine(); //skipping the headers

                        int curRow = 0;
                        for (int j = 0; line != null && j < totalRow; j++)
                        {
                                stringValues.add(line);
                                line = buffReader.readLine();
                        }

                        buffReader.close();
                        fileReader.close();

                }
                catch (Exception iOException)
                {
                        iOException.printStackTrace();
                }

                return stringValues;
        }

        private static double[][] getArrayFromStrings(ArrayList<String> values, int totalCol, String delimeter)
        {
                int totalRows = values.size();
                if (totalCol < 0)
                {
                        totalCol = values.get(0).split(delimeter).length;
                }
                double[][] doubleValues = new double[totalRows][totalCol];
                String currentValue;
                String[] splittedValues;
                for (int i = 0; i < totalRows; i++)
                {
                        currentValue = values.get(i);
                        splittedValues = currentValue.split(delimeter);
                        for (int j = 0; j < totalCol; j++)
                        {
                                doubleValues[i][j] = Double.parseDouble(splittedValues[j]);
                        }

                }

                return doubleValues;
        }

        public static String appendToEachCSV(String original, String appendedValue)
        {
                String[] originalValues = original.split(",");

                StringBuilder stBuilder = new StringBuilder();

                if (originalValues.length > 0)
                {
                        stBuilder.append(originalValues[0]);
                        stBuilder.append(appendedValue);

                        for (int i = 1; i < originalValues.length; i++)
                        {
                                stBuilder.append(",");
                                stBuilder.append(originalValues[i]);
                                stBuilder.append(appendedValue);

                        }
                }
                return stBuilder.toString();
        }

}
