/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.statistics;

import edu.fiu.scg.util.ArrayUtil;
import edu.fiu.scg.util.CSVUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

/**
 *
 * @author Hasan
 */
public class DetailStat
{

       protected double [] percentileList;
       protected double[] percentiles;

        private ArrayList<Double> dataList;

        public DetailStat()
        {
                dataList = new ArrayList<>();
        }

        public void clearDataList()
        {
                dataList.clear();
        }

        public void add(double singleData)
        {
                dataList.add(singleData);
        }

        public String getCSVHeader()
        {
                String header = ArrayUtil.getCSVString(percentileList);
                header = CSVUtil.appendToEachCSV(header, " Percentile");
                return header;
        }        
        
        public String getCSVFormattedData()
        {
                
                return ArrayUtil.getCSVString(percentiles);
        }
        public void computePercentile()
        {
                percentiles = computePercentile(percentileList);
        }

        public double computePercentile(double percentile)
        {
                double [] percentileValues = computePercentile(new double [] {percentile});
                return percentileValues[0];
        }
        
        private double [] computePercentile(double [] lcPercentileList)
        {
                double [] percentileValues = new double[lcPercentileList.length];

                double[] allData = new double[dataList.size()];
                for (int i = 0; i < dataList.size(); i++)
                {
                        allData[i] = dataList.get(i).doubleValue();

                }

                Arrays.sort(allData);
                Percentile percentile = new Percentile();
                percentile.setData(allData);

                for (int i = 0; i < lcPercentileList.length; i++)
                {
                        percentileValues[i] = percentile.evaluate(lcPercentileList[i]);
                        //System.out.println("Percentile: " + getPercentiles()[i]);
                }
                return percentileValues;

        }

        public double[] getPercentiles()
        {
                return percentiles;
        }

        public double[] getPercentileList()
        {
                return percentileList;
        }

        public void setPercentileList(double[] percentileList)
        {
                this.percentileList = percentileList;
        }

}
