/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator.util;

import azuresimulator.AzureCloudlet;
import java.text.DecimalFormat;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;

/**
 *
 * @author amahm008
 */
public class CommonUtil
{

        public static void printCloudletList(List<Cloudlet> list)
        {
                int size = list.size();
                AzureCloudlet cloudlet;
                System.out.println("========== OUTPUT ==========");
                String formatString = "%6s%10s%10s%15s%15s%15s%15s%15s%15s%15s";
                System.out.format(formatString, "ID", "STATUS", "VM #","Sub. Time", "Arrv. Time","Length", "Start Time", "Finish Time", "CPU Time", "Wait. Time");
                System.out.println("");
                DecimalFormat dft = new DecimalFormat("###.###");
                for (int i = 0; i < size; i++)
                {
                        cloudlet = (AzureCloudlet)list.get(i);
                        System.out.format(
                                formatString,
                                cloudlet.getCloudletId(),
                                cloudlet.getStatus(),
                                cloudlet.getVmId(),
                                dft.format(cloudlet.getSubmissionTime()),
                                dft.format(cloudlet.getArrivalTime()),
                                dft.format(cloudlet.getCloudletLength()),
                                dft.format(cloudlet.getExecStartTime()),
                                dft.format(cloudlet.getFinishTime()),
                                dft.format(cloudlet.getActualCPUTime()),
                                dft.format(cloudlet.getWaitingTime()));
                        System.out.println("");
                }
        }

        
        public static void printStatisticsDetail(List<Cloudlet> list)
        {
                DecimalFormat dft = new DecimalFormat("###.###");

                int size = list.size();
                AzureCloudlet cloudlet;

                double totalTime = 0;
                for (int i = 0; i < size; i++)
                {
                        cloudlet = (AzureCloudlet) list.get(i);
                        //Log.print(indent + cloudlet.getCloudletId() + indent + indent);


                        if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS)
                        {

                                totalTime += (cloudlet.getFinishTime() - cloudlet.getArrivalTime());

                        }
                }
                double simValue = totalTime / size * 1000;
                System.out.println("Total Jobs: " + size);    
                System.out.println("Sim Total Delay: " + dft.format(totalTime) );
                System.out.println("Sim Average Delay: " + dft.format(simValue) );


        }

}
