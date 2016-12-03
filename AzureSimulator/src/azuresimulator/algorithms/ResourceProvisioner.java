/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package azuresimulator.algorithms;

/**
 *
 * @author Hasan
 */
public interface ResourceProvisioner
{
        public boolean updateResourceProvision(Decision decission, boolean enableVMCreateDelay);
}
