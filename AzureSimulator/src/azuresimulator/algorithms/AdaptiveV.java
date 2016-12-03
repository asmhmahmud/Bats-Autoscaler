/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.algorithms;

import azuresimulator.Settings;

/**
 *
 * @author Hasan
 */
public class AdaptiveV extends BATS //implements ResourceProvisioner
{

        protected ResourceProvisioner redirectResProv;
        private double initialV;
        
        private static final double MINIMUM_V= 0.0001;

        public AdaptiveV(String name)
        {
                super(name);
                initialV = Settings.STANDARD_V;
        }

        private double getOptimalV(int slotNo)
        {
                double optV ;
                double totalBudSoFar = getTotalBudget(slotNo);
                double totalCostSoFar = getTotalCost(slotNo);

                double ratio = totalBudSoFar/totalCostSoFar;
                double beta = 50;//for FIU workload
                //double beta = 50;//for Wiki workload
                //optV = getV() * Math.pow(ratio, 2);
                optV = getV() + beta* (totalBudSoFar/(slotNo+1) - totalCostSoFar/(slotNo+1));
                optV = Math.max(MINIMUM_V, optV);
                
                return optV;
        }

        @Override
        public Decision startOfSlot(int timeSlotNo)
        {

                if (timeSlotNo == 0)
                {
                        setV(initialV);
                }
                else if (timeSlotNo % 12 == 0)
                {
                        setV(getOptimalV(timeSlotNo-1));
                }

                return super.startOfSlot(timeSlotNo);
        }

        public double getInitialV()
        {
                return initialV;
        }

        public void setInitialV(double initialV)
        {
                this.initialV = initialV;
        }

}
