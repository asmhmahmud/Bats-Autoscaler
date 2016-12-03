/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.statistics;

/**
 *
 * @author Hasan
 */
public class ExpInput implements Cloneable
{

        public int globalSlotNo;
        public int vSlotNo;
        public double V;
        public double noOfClients;
        

        public ExpInput()
        {
                
        }
        public ExpInput( int globalSlotNo , int vSlotNo, double V, double noOfClients  )
        {
                this.globalSlotNo = globalSlotNo;
                this.vSlotNo = vSlotNo;
                this.V = V;
                this.noOfClients = noOfClients;
        }
        
        @Override
        public ExpInput clone()
        {
                ExpInput expInfo = new ExpInput(globalSlotNo, vSlotNo, V, noOfClients);  
                
                return expInfo;
        }
        
        
        public String getCSVFormattedData()
        {
                //System.out.println(expInfo.globalSlotNo);
                return globalSlotNo+","
                        +vSlotNo+","                        
                        +V+","
                        +noOfClients
                        ;
        }
        
        public static String getCSVHeader()
        {
                return "Global Slot No,"
                        + "V Slot No,"
                        +"V,"
                        + "Load"
                        ;
        }        
        
        
}