/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator;

import azuresimulator.statistics.ExpInput;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.distributions.ExponentialDistr;

/**
 *
 * @author Hasan
 */
public class WorkloadGen extends SimEntity
{

        private DatacenterBroker broker;
        private String distFileName;
        private ArrayList<Integer> distList;
        private boolean enableBandwidth = false;
        private boolean dispatchJobs = true;
        private int maxJobs;
        private double maxServiceTimeLength = 10000; // 10 seconds
        private ArrayList<ExpInput> workload;
        
        private static final int STATIC_COMM_DELAY = 100;
        
        public WorkloadGen(String name, String distFileName, ArrayList<ExpInput> workload)
        {
                super(name);
                this.distFileName = distFileName;

                distList = new ArrayList<>();
                readDistribution();
                maxJobs = distList.size();
                this.workload = workload;
        }
        
        public ExpInput getWorkload(int slot)
        {

                return workload.get(slot);
        }

        private void readDistribution()
        {
                
                try
                {
                        // FileReader reads text files in the default encoding.
                        FileReader fileReader = new FileReader(distFileName);

                        // Always wrap FileReader in BufferedReader.
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        String line;
                        //Skipping the header
                        bufferedReader.readLine();
                        while ((line = bufferedReader.readLine()) != null)
                        {
                                distList.add(Integer.parseInt(line)+STATIC_COMM_DELAY);

                        }

                        // Always close files.
                        fileReader.close();
                        bufferedReader.close();
                }
                catch (Exception ex)
                {

                        ex.printStackTrace();
                }
        }



        public ArrayList<AzureCloudlet> generateWorkload(int slotNo, double slotLength, double startClockTime )
        {
                
                ExpInput expInput = workload.get(slotNo);
                
                // Fifth step: Create one Cloudlet
                ArrayList<AzureCloudlet> cloudletList = new ArrayList<>();

                // Cloudlet properties
                int mips = Settings.PROCESSOR_MAX_MIPS;
                double minServiceTime = CloudSim.getMinTimeBetweenEvents() * mips;


                double currentTime = startClockTime;
                double meanIntArrival = 4.25*7000; //interarrival in milisecond
                double thinkTimeDistrMean = Settings.TIME_SLOT_LENGTH; 
                ExponentialDistr expDist;
                ExponentialDistr thinkTimeDistr;
                
                
                Random rnd = new Random(1234);             
                
                
                double currentThinkTime=0;

                thinkTimeDistr = new ExponentialDistr(1234, thinkTimeDistrMean);
                int jobId = 0;
                for (int clIndex = 0; clIndex < expInput.noOfClients; clIndex++)
                {
                        expDist = new ExponentialDistr(clIndex, meanIntArrival);
                        
                        currentTime = startClockTime;
                        int jobIndex = 0;
                        currentThinkTime = thinkTimeDistr.sample();
                        
                        
                        Random jobDistIndex = new Random(clIndex);
                        int distIndex=jobDistIndex.nextInt(distList.size()-1)  ;
                        for (; jobIndex < maxJobs;   jobIndex++)
                        {
                               if(distIndex > distList.size() - 1)
                               {
                                       distIndex = 0;
                               }
                                       
                                       
                                //Probability for adding a back button
                                long length = (long) minServiceTime+2;
                                
                                //Back Probability is 25%
                                if(rnd.nextDouble() > 0.28)
                                {
                                        //length = (long) Math.max(minServiceTime + 1, distList.get(jobDistIndex.nextInt(distList.size()-1)));
                                        length = (long) Math.max(minServiceTime + 1, distList.get(distIndex++));
                                        
                                }

                                

                                length = (long) Math.min(maxServiceTimeLength, length);

                                //System.out.println("Length: " + length);
                                long fileSize = 100;
                                long outputSize = 100;
                                UtilizationModel utilizationModel = new UtilizationModelFull();

                                currentTime = currentTime + expDist.sample() * 0.001;
                                //System.out.println(currentTime);
                                
                                //slot length expired
                                if ((currentTime > startClockTime + slotLength ) )
                                {
                                        break;
                                }
                                //Session Expired
                                if ((currentTime - startClockTime) >= currentThinkTime)
                                {
                                        //System.out.println("Think Time Expired");
                                        break;
                                }                                


                                
                                
                                AzureCloudlet cloudlet = new AzureCloudlet(jobId, length, 1, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
                                cloudlet.setUserId(broker.getId());
                                cloudlet.setArrivalTime(currentTime);
                                cloudletList.add(cloudlet);
                                
                                if(dispatchJobs)
                                {
                                        send(broker.getId(), cloudlet.getArrivalTime()-startClockTime,ExperimentTags.CLOUDLET_ARRIVED, cloudlet);
                                        
                                }
                     
                                jobId++;

                               
                        }


                }
                
                System.out.println("Total Jobs: "+jobId);

                return cloudletList;
        }

        public void startEntity()
        {
                // sendNow(getId(),  ExperimentTags.SLOT_STARTED);
                //System.out.println("++++++++++++++Start");
        }

        public void processEvent(SimEvent ev)
        {
                switch (ev.getTag())
                {
                        // Resource characteristics request
                        case ExperimentTags.SLOT_STARTED:
                                //generateWorkload();
                                break;

                        // other unknown tags are processed by this method
                        default:
                                processOtherEvent(ev);
                                break;
                }
        }

        public void shutdownEntity()
        {
        }

        /**
         * @return the broker
         */
        public DatacenterBroker getBroker()
        {
                return broker;
        }

        /**
         * @param broker the broker to set
         */
        public void setBroker(DatacenterBroker broker)
        {
                this.broker = broker;
        }

        public void processOtherEvent(SimEvent ev)
        {
                System.out.println("Unknown event in WorkloadGen");
                System.exit(0);
        }

        public int getMaxJobs()
        {
                return maxJobs;
        }

        public void setMaxJobs(int maxJobs)
        {
                this.maxJobs = maxJobs;
        }

        /**
         * @return the dispatchJobs
         */
        public boolean getDispatchJobs()
        {
                return dispatchJobs;
        }

        /**
         * @param dispatchJobs the dispatchJobs to set
         */
        public void setDispatchJobs(boolean dispatchJobs)
        {
                this.dispatchJobs = dispatchJobs;
        }
        
        
}


//        private int totalJobs = 100000;
//        private double meanServiceTimeLength = 100;
//        private double meanInterArrival = 250;       
//        

//        public ArrayList<AzureCloudlet> generateTheoriticalWorkload(ExpInput expInput, double slotLength)
//        {
//                
//         
//                ExponentialDistr serviceTime = new ExponentialDistr(12135, meanServiceTimeLength);
//                ExponentialDistr arrivalDist = new ExponentialDistr(12345, meanInterArrival);
//                // Fifth step: Create one Cloudlet
//                ArrayList<AzureCloudlet> cloudletList = new ArrayList<>();
//
//                // Cloudlet properties
//                int mips = Settings.PROCESSOR_MAX_MIPS;
//                double minServiceTime = CloudSim.getMinTimeBetweenEvents() * mips;
//
//
//                double startClockTime = 0;
//                double currentTime = startClockTime;
//
//                for (int i = 0; i < totalJobs; i++)
//                {
//                        int id = i;
//                        //long length = Math.max(1, (long) meanServiceTimeLength.sample());
//
//                        long length = (long) Math.max(minServiceTime + 1, (long) serviceTime.sample());
//
//                        length = (long) Math.min(maxServiceTimeLength, length);
//
//                        //System.out.println("Length: " + length);
//                        long fileSize = 0;
//                        long outputSize = 0;
//                        UtilizationModel utilizationModel = new UtilizationModelFull();
//
//                        currentTime = currentTime + arrivalDist.sample() * 0.001;
//                        if (currentTime > startClockTime + slotLength)
//                        {
//                                break;
//                        }
//
//
//                        AzureCloudlet cloudlet = new AzureCloudlet(id, length, 1, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
//                        cloudlet.setUserId(broker.getId());
//                        cloudlet.setArrivalTime(currentTime);
//                        cloudletList.add(cloudlet);
//
//
//
//
//                        //send(broker.getId(), currentTime, ExperimentTags.CLOUDLET_ARRIVED,cloudlet);
//                        //System.out.println(broker.getId());
//                }
//
//
//                // submit cloudlet list to the broker
//                //broker.submitCloudletList(cloudletList);
//
//                return cloudletList;
//        }
