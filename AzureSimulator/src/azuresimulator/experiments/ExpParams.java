/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.experiments;

import azuresimulator.AzureBroker;
import azuresimulator.WorkloadGen;
import azuresimulator.algorithms.Algorithm;
import azuresimulator.loadbalancer.LoadBalancer;
import azuresimulator.predictor.Predictor;
import azuresimulator.runtimeDelayTable.RUBiSRuntimeDelay;
import org.cloudbus.cloudsim.Datacenter;

/**
 *
 * @author amahm008
 */
public class ExpParams
{

        public Datacenter datacenter;
        public AzureBroker broker;
        public WorkloadGen wLoadGen;
        public RUBiSRuntimeDelay rdt;
        public LoadBalancer loadBalancer;
        public Predictor predictor;
        public Algorithm algorithm;

        public void setParams(
                Datacenter datacenter,
                AzureBroker broker,
                WorkloadGen wLoadGen,
                RUBiSRuntimeDelay rdt,
                LoadBalancer loadBalancer,
                Predictor predictor,
                Algorithm algorithm
        )
        {
                this.datacenter = datacenter;
                this.broker = broker;
                this.wLoadGen = wLoadGen;
                this.rdt = rdt;
                this.loadBalancer = loadBalancer;
                this.predictor = predictor;
                this.algorithm = algorithm;
        }

}
