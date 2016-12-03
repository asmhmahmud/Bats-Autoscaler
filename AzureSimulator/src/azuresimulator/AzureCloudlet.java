/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator;

import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;

/**
 *
 * @author Hasan
 */
public class AzureCloudlet extends Cloudlet
{
        private boolean servedFromCache = false;

        private double arrivalTime;
        /**
         * Allocates a new Cloudlet object. The Cloudlet length, input and
         * output file sizes should be greater than or equal to 1. By default
         * this constructor sets the history of this object.
         *
         * @param cloudletId the unique ID of this Cloudlet
         * @param cloudletLength the length or size (in MI) of this cloudlet to
         * be executed in a PowerDatacenter
         * @param cloudletFileSize the file size (in byte) of this cloudlet
         * <tt>BEFORE</tt> submitting to a PowerDatacenter
         * @param cloudletOutputSize the file size (in byte) of this cloudlet
         * <tt>AFTER</tt> finish executing by a PowerDatacenter
         * @param pesNumber the pes number
         * @param utilizationModelCpu the utilization model cpu
         * @param utilizationModelRam the utilization model ram
         * @param utilizationModelBw the utilization model bw
         * @pre cloudletID >= 0
         * @pre cloudletLength >= 0.0
         * @pre cloudletFileSize >= 1
         * @pre cloudletOutputSize >= 1
         * @post $none
         */
        public AzureCloudlet(
                final int cloudletId,
                final long cloudletLength,
                final int pesNumber,
                final long cloudletFileSize,
                final long cloudletOutputSize,
                final UtilizationModel utilizationModelCpu,
                final UtilizationModel utilizationModelRam,
                final UtilizationModel utilizationModelBw)        
        {
                super(
                        cloudletId,
                        cloudletLength,
                        pesNumber,
                        cloudletFileSize,
                        cloudletOutputSize,
                        utilizationModelCpu,
                        utilizationModelRam,
                        utilizationModelBw);
                
        }

        /**
         * Allocates a new Cloudlet object. The Cloudlet length, input and
         * output file sizes should be greater than or equal to 1.
         *
         * @param cloudletId the unique ID of this cloudlet
         * @param cloudletLength the length or size (in MI) of this cloudlet to
         * be executed in a PowerDatacenter
         * @param cloudletFileSize the file size (in byte) of this cloudlet
         * <tt>BEFORE</tt> submitting to a PowerDatacenter
         * @param cloudletOutputSize the file size (in byte) of this cloudlet
         * <tt>AFTER</tt> finish executing by a PowerDatacenter
         * @param record record the history of this object or not
         * @param fileList list of files required by this cloudlet
         * @param pesNumber the pes number
         * @param utilizationModelCpu the utilization model cpu
         * @param utilizationModelRam the utilization model ram
         * @param utilizationModelBw the utilization model bw
         * @pre cloudletID >= 0
         * @pre cloudletLength >= 0.0
         * @pre cloudletFileSize >= 1
         * @pre cloudletOutputSize >= 1
         * @post $none
         */
        public AzureCloudlet(
                final int cloudletId,
                final long cloudletLength,
                final int pesNumber,
                final long cloudletFileSize,
                final long cloudletOutputSize,
                final UtilizationModel utilizationModelCpu,
                final UtilizationModel utilizationModelRam,
                final UtilizationModel utilizationModelBw,
                final boolean record,
                final List<String> fileList)
        {
                super(
                        cloudletId,
                        cloudletLength,
                        pesNumber,
                        cloudletFileSize,
                        cloudletOutputSize,
                        utilizationModelCpu,
                        utilizationModelRam,
                        utilizationModelBw,
                        record,
                        fileList);
                
        }

        /**
         * Allocates a new Cloudlet object. The Cloudlet length, input and
         * output file sizes should be greater than or equal to 1. By default
         * this constructor sets the history of this object.
         *
         * @param cloudletId the unique ID of this Cloudlet
         * @param cloudletLength the length or size (in MI) of this cloudlet to
         * be executed in a PowerDatacenter
         * @param cloudletFileSize the file size (in byte) of this cloudlet
         * <tt>BEFORE</tt> submitting to a PowerDatacenter
         * @param cloudletOutputSize the file size (in byte) of this cloudlet
         * <tt>AFTER</tt> finish executing by a PowerDatacenter
         * @param fileList list of files required by this cloudlet
         * @param pesNumber the pes number
         * @param utilizationModelCpu the utilization model cpu
         * @param utilizationModelRam the utilization model ram
         * @param utilizationModelBw the utilization model bw
         * @pre cloudletID >= 0
         * @pre cloudletLength >= 0.0
         * @pre cloudletFileSize >= 1
         * @pre cloudletOutputSize >= 1
         * @post $none
         */
        public AzureCloudlet(
                final int cloudletId,
                final long cloudletLength,
                final int pesNumber,
                final long cloudletFileSize,
                final long cloudletOutputSize,
                final UtilizationModel utilizationModelCpu,
                final UtilizationModel utilizationModelRam,
                final UtilizationModel utilizationModelBw,
                final List<String> fileList)
        {
                super(
                        cloudletId,
                        cloudletLength,
                        pesNumber,
                        cloudletFileSize,
                        cloudletOutputSize,
                        utilizationModelCpu,
                        utilizationModelRam,
                        utilizationModelBw,
                        fileList);
                
        }

        /**
         * Allocates a new Cloudlet object. The Cloudlet length, input and
         * output file sizes should be greater than or equal to 1.
         *
         * @param cloudletId the unique ID of this cloudlet
         * @param cloudletLength the length or size (in MI) of this cloudlet to
         * be executed in a PowerDatacenter
         * @param cloudletFileSize the file size (in byte) of this cloudlet
         * <tt>BEFORE</tt> submitting to a PowerDatacenter
         * @param cloudletOutputSize the file size (in byte) of this cloudlet
         * <tt>AFTER</tt> finish executing by a PowerDatacenter
         * @param record record the history of this object or not
         * @param pesNumber the pes number
         * @param utilizationModelCpu the utilization model cpu
         * @param utilizationModelRam the utilization model ram
         * @param utilizationModelBw the utilization model bw
         * @pre cloudletID >= 0
         * @pre cloudletLength >= 0.0
         * @pre cloudletFileSize >= 1
         * @pre cloudletOutputSize >= 1
         * @post $none
         */
        public AzureCloudlet(
                final int cloudletId,
                final long cloudletLength,
                final int pesNumber,
                final long cloudletFileSize,
                final long cloudletOutputSize,
                final UtilizationModel utilizationModelCpu,
                final UtilizationModel utilizationModelRam,
                final UtilizationModel utilizationModelBw,
                final boolean record)        
        {
                super(cloudletId, cloudletLength, pesNumber, cloudletFileSize, cloudletOutputSize, utilizationModelCpu, utilizationModelRam, utilizationModelBw, record);
                
        }

        /**
         * @return the arrivalTime
         */
        public double getArrivalTime()
        {
                return arrivalTime;
        }

        /**
         * @param arrivalTime the arrivalTime to set
         */
        public void setArrivalTime(double arrivalTime)
        {
                this.arrivalTime = arrivalTime;
        }

        public boolean isServedFromCache()
        {
                return servedFromCache;
        }

        public void setServedFromCache(boolean servedFromCache)
        {
                this.servedFromCache = servedFromCache;
        }
}
