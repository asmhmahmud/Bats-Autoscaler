/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package azuresimulator.predictor;

/**
 *
 * @author Hasan
 */
public class FIUWorkLoadPredictor extends Predictor
{
        private int prevSampleToUse = 3;
        private int repetativePatternLength = 1;
        private double [] weights = new double [] {  10,5, 3};
        
        public FIUWorkLoadPredictor()
        {
                super();
                
                
        }
        
 
        public void initWeightsToAvg()
        {
                weights = new double[prevSampleToUse];
                for (int i = 0; i < weights.length; i++)
                {
                        weights[i]=1;
                }
        }

        @Override
        public double getPredictedValue(int slotNo)
        {
               double value = 0;
               int currentIndex = slotNo - repetativePatternLength;
               int count=0;
                
               if(slotNo < repetativePatternLength)
               {
                       value = sampleData.get(slotNo);
               }
               else
               {
               double weightsTotal = 0;
                for (; count < prevSampleToUse && currentIndex >=  0; count++)
                {
                        weightsTotal = weightsTotal + weights[count];
                        value = value +  sampleData.get(currentIndex) * weights[count];
                        currentIndex = currentIndex - repetativePatternLength;
                }
               
                if(weightsTotal > 0)
                {
                                value = value / weightsTotal;
                }
               }
               return value;
        }

//                @Override
//        public double getPredictedValue(int slotNo)
//        {
//               double value = 0;
//               if(slotNo > 1)
//               {
//                       value =2* sampleData.get(slotNo-1) - sampleData.get(slotNo-2);
//               }
//               else
//               {
//                       value = sampleData.get(slotNo);
//               }
//               return value;
//        }

        
        /**
         * @return the prevSampleToUse
         */
        public int getPrevSampleToUse()
        {
                return prevSampleToUse;
        }

        /**
         * @param prevSampleToUse the prevSampleToUse to set
         * @param weight weights of the samples
         */
        public void setPrevSampleToUse(int prevSampleToUse, double [] weights)
        {
                if (prevSampleToUse != weights.length)
                {
                        System.err.println("Number of weights is not equal to number of previous samples to use");
                        System.exit(0);
                }
                this.prevSampleToUse = prevSampleToUse;
                this.weights = weights;
                
                //normalizeWeight();
        }
        

        /**
         * @return the repetativePatternLength
         */
        public int getRepetativePatternLength()
        {
                return repetativePatternLength;
        }

        /**
         * @param repetativePatternLength the repetativePatternLength to set
         */
        public void setRepetativePatternLength(int repetativePatternLength)
        {
                this.repetativePatternLength = repetativePatternLength;
        }

        /**
         * @return the weights
         */
        public double[] getWeights()
        {
                return weights;
        }

        
}
