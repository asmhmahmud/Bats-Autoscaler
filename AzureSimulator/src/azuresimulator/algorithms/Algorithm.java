/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.algorithms;

import azuresimulator.Settings;
import azuresimulator.runtimeDelayTable.RUBiSRuntimeDelay;
import azuresimulator.statistics.DataManager;
import azuresimulator.statistics.ExpInput;
import azuresimulator.statistics.ExpResult;
import azuresimulator.predictor.Predictor;
import java.util.ArrayList;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;

/**
 *
 * @author Hasan
 */
public abstract class Algorithm extends SimEntity
{

        protected ArrayList<ExpInput> workload;
        protected ArrayList<ExpResult> experimentResults;
        private String resultOutputFileName;
        //protected int currentTimeSlot;
        protected double budget;
        protected ResourceProvisioner resourceProvisioner;
        protected Predictor workloadPredictor;

        protected RUBiSRuntimeDelay runDelay;
                
        protected DataManager dtm;
        protected int dataSavingFrequency = 200;
        
        public Algorithm(String name)
        {
                super(name);
        }
        
        private void setBudget(double budget)
        {
                this.budget = budget;
               
        }
        
   
        public void setParameters(
                ResourceProvisioner resourceProvisioner,
                Predictor workloadPredictor,
                ArrayList<ExpInput> workload, 
                double budget , 
                RUBiSRuntimeDelay runDelay, 
                String outputFileName 
        
        )
        {
                this.resourceProvisioner = resourceProvisioner;
                this.workloadPredictor = workloadPredictor;
                
                this.workload = workload;
                experimentResults = new ArrayList<ExpResult>();
                resultOutputFileName = outputFileName;
                this.runDelay = runDelay;
                setBudget(budget);
                
                dtm = new DataManager(outputFileName, experimentResults);
        }

        abstract public Decision startOfSlot(int timeSlotNo);

        public void saveLastSlotResults(ExpResult expResult)
        {
                //expResult. = workload.get(currentTimeSlot);
                experimentResults.add(expResult);
        }

        /**
         * @return the totalSlot
         */
        public int getTotalTimeSlots()
        {
                int totalSlot = 0;

                try
                {
                        totalSlot = workload.size();
                }
                catch (Exception e)
                {
                        System.err.println("No workload found.");
                }

                return totalSlot;
        }
        
                /**
         * @return the totalSlot
         */
        public int getSlotRepeationPeriod()
        {
                int totalSlot = 0;

                try
                {
                        totalSlot = workload.size();
                }
                catch (Exception e)
                {
                        System.err.println("No workload found.");
                }

                return totalSlot;
        }
        

        /**
         * @return the workload
         */
        public ArrayList<ExpInput> getWorkload()
        {
                return workload;
        }

        /**
         * @param workload the workload to set
         */
        public void setWorkload(ArrayList<ExpInput> workload)
        {
                this.workload = workload;
        }

        /**
         * @return the resultOutputFileName
         */
        public String getResultOutputFileName()
        {
                return resultOutputFileName;
        }

        /**
         * @param resultOutputFileName the resultOutputFileName to set
         */
        public void setResultOutputFileName(String resultOutputFileName)
        {
                this.resultOutputFileName = resultOutputFileName;
        }

        public void saveExperimentResult()
        {
                
                dtm.saveExperimentResults();
        }

        public ExpInput getExpInput(int timeSlotNo)
        {
                return workload.get(timeSlotNo);
        }

        public double[] getHourlyBudget(double totalBudget)
        {
                int totalHour = workload.size();
                int noOfHourInDays = 24;
                double[] dividedBudget = new double[totalHour];


                double[] averageWorkload = new double[noOfHourInDays];

                for (int i = 0; i < totalHour; i++)
                {
                        averageWorkload[i % noOfHourInDays] += workload.get(i).noOfClients;
                }
                double totalWorkload = 0;
                for (int i = 0; i < averageWorkload.length; i++)
                {
                        totalWorkload += averageWorkload[i];
                }

                for (int i = 0; i < averageWorkload.length; i++)
                {
                        averageWorkload[i] = averageWorkload[i] / totalWorkload;
                }

                int noOfDays = workload.size() / noOfHourInDays;
                // Average workload contains the normalized value
                for (int i = 0; i < totalHour; i++)
                {
                        dividedBudget[i] = averageWorkload[i % noOfHourInDays] * (totalBudget / noOfDays);
                }

//                double totBud = 0;
//                for (int i = 0; i < totalHour; i++)
//                {
//                        totBud += dividedBudget[i];
//                }
//                System.out.println("Total Budget: " + totBud);
                
                
                return dividedBudget;

        }
        
        
                /**
         * @return the timeSlotLength
         */
        public double getTimeSlotLength()
        {
                return Settings.TIME_SLOT_LENGTH;
        }
        
        

        public void simulationFinished()
        {
                
        }
        
        
        @Override
        public void startEntity()
        {
                
        }

        @Override
        public void processEvent(SimEvent ev)
        {
                
        }

        @Override
        public void shutdownEntity()
        {
                
        }        
        
        
        public void endOfSlot(int slotNo, ExpResult expResult)
        {
                
        }
        
        public double getTotalCost(int timeSlotNo)
        {
                double totalCost =0;
                Decision cDecision;
                for (int i = 0; i < experimentResults.size() && i <= timeSlotNo; i++)
                {
                        cDecision = experimentResults.get(i).decission;
                        totalCost = totalCost + cDecision.getTotalCost();
                }
                return totalCost;
        }        

        public int getDataSavingFrequency()
        {
                return dataSavingFrequency;
        }

        public void setDataSavingFrequency(int dataSavingFrequency)
        {
                this.dataSavingFrequency = dataSavingFrequency;
        }

}
