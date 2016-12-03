/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package azuresimulator.loadbalancer;

import org.cloudbus.cloudsim.Cloudlet;

/**
 *
 * @author Hasan
 */
public interface LoadBalancerFeedback
{
        public void dispatchCloudlet(Cloudlet cloudlet);
}
