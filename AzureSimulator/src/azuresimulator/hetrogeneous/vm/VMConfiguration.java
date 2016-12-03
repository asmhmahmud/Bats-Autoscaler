/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.hetrogeneous.vm;


/**
 *
 * @author Hasan
 */
public class VMConfiguration
{
        public double mips;
        public int numberOfPes;
        public int ram;
        public long bw;
        public long size;
        
        
      
        public static VMConfiguration getVMConfiguration(double mips, int numberOfPes, int ram, long bw, long size)
        {
                VMConfiguration vmConfig = new VMConfiguration();
                vmConfig.mips = mips;
                vmConfig.numberOfPes = numberOfPes;
                vmConfig.ram = ram;
                vmConfig.bw = bw;
                vmConfig.size = size;
                
                return vmConfig;
        }
        
        
        
}
