/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.algorithms;

import azuresimulator.hetrogeneous.vm.VMType;
import azuresimulator.runtimeDelayTable.RUBiSRuntimeDelay;
import edu.fiu.scg.util.MLab;

/**
 *
 * @author amahm008
 */
public class HetLoadDist
{

        public static LoadDistribution getOptimalLoadDist(double load, int[] capacity, RUBiSRuntimeDelay runDelay)
        {
                LoadDistribution ldDist = new LoadDistribution();
                ldDist.totalLoad = load;

                double[] workload_assign_conf = new double[]
                {
                        1, 3, 5
                };

                double[] tot_cap = MLab.mulR(capacity, workload_assign_conf);

                double[] assign_frac = MLab.divR(tot_cap, MLab.sum(tot_cap));

                double[] load_assign = MLab.mulR(assign_frac, load);

                double[] rd = new double[]
                {
                        0.1, 0.1, 0.1
                };

                for (int i = 0; i < VMType.totalVMType() && i < capacity.length; i++)
                {
                        if (capacity[i] != 0)
                        {
                                rd[i] = runDelay.getDelay(capacity[i], (int) load_assign[i], i);
                        }
                }


                int itr = 1;

                double[] check = rd;
                double maxv;

                double [] multFac;
                while (MLab.stddev(check) > 0.004 && itr < 15)

                {
                        multFac = MLab.divR(check ,MLab.min(check));

                        maxv = MLab.max(multFac);

                        multFac = MLab.addR( multFac, 5*maxv) ;
                        multFac = MLab.divR( multFac, 6*maxv) ;
                        
                        multFac = MLab.divR(assign_frac , multFac);
                        assign_frac = MLab.divR(multFac , MLab.sum(multFac));
                        load_assign = MLab.mulR(assign_frac,load);
                        
                        
                        for (int i = 0; i < VMType.totalVMType() && i < capacity.length; i++)
                        {
                                if (capacity[i] != 0)
                                {
                                        rd[i] = runDelay.getDelay(capacity[i], (int) load_assign[i], i);
                                }
                        }
                        
                        itr = itr + 1;

                        check = rd;
                        
                        int [] indexes = MLab.find(capacity, 0, true);
                        double [] values = MLab.valuesAt(check, MLab.find(capacity, 0, false));
                        
                        double min = MLab.min(values);
                        check = MLab.assign(check, indexes, min);


                }

                
//                d = rd;
//                d_tot = sum(load_assign . * rd) . / sum(load_assign);
//                load_dist = load_assign;
//                end
//                
                
                
                ldDist.loadFractions = load_assign;
                ldDist.delayFractions = rd;
                
                
                

                return ldDist;
        }

}
