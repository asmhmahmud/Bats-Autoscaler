/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package azuresimulator.predictor;

import edu.fiu.scg.util.CSVUtil;
import java.util.ArrayList;

/**
 *
 * @author Hasan
 */
public abstract class Predictor
{
        protected ArrayList<Double> sampleData ;
        protected boolean  predictorInitialized = false;
        
        public Predictor()
        {
                sampleData = new ArrayList<>();
                predictorInitialized = false;
        }
        
        public void addSample(double sampleValue)
        {
                sampleData.add(sampleValue);
        }
        
        public abstract double getPredictedValue(int slotNo);
        
        public void saveComparison(String fileName)
        {
                double [][] data = new double [sampleData.size()][2];
                String [] headers = new String []{"Actual Value", "Predicted Value"};
                
                
                for (int i = 0; i < data.length; i++)
                {
                        data[i][0] = sampleData.get(i);
                        data[i][1]= getPredictedValue(i);
                }
                CSVUtil.writeCSV(headers, data, fileName);
                System.out.println("Total Size: "+data.length);
        }

        public boolean isPredictorInitialized()
        {
                return predictorInitialized;
        }

        public void setPredictorInitialized(boolean predictorInitialized)
        {
                this.predictorInitialized = predictorInitialized;
        }
        
        
        
}
