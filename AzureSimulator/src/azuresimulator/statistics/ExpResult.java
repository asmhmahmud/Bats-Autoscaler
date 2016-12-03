/*******************************
** @author A S M Hasan Mahmud
*******************************/

package azuresimulator.statistics;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import azuresimulator.algorithms.Decision;
import azuresimulator.algorithms.ReactiveDecision;
import java.util.Date;

/**
 *
 * @author Hasan
 */
public class ExpResult
{

        public Decision decission;

        public long startTime;
        public long timeTaken; //In second
        public double totalDelay;
        public long submittedJobs;
        public long finishedJobs;
        public double workloadLevel;
        private DetailStat detailStat;

        private String delayMetricHeader = "95th Percentile";
        public double delayMetric;

        public ExpResult()
        {

        }

        public double getDelayMetric()
        {
                return delayMetric;
        }

                public double getAverageDelay()
        {
                return totalDelay/ finishedJobs;
        }
                
        public String getCSVFormattedData()
        {
                //System.out.println(expInfo.globalSlotNo);
                return startTime + ","
                        + timeTaken + ","
                        + workloadLevel + ","
                        + decission.getCSVFormattedData() + ","
                        + totalDelay + ","
                        + submittedJobs + ","
                        + finishedJobs + ","
                        + delayMetric + ","
                        + getAverageDelay()+","
                        +(detailStat == null ? "" : detailStat.getCSVFormattedData());
        }

        public String getCSVHeader()
        {
                String header = "Empty Header";
                if (decission != null)
                {
                        header = "Start Time,"
                                + "Time taken (Sec),"
                                + "Load,"
                                + decission.getCSVHeader() + ","
                                + "Total delay,"
                                + "Submitted Jobs,"
                                + "Finished Jobs,"
                                + delayMetricHeader + ","
                                + "Average Delay,"
                                + (detailStat == null ? "" : detailStat.getCSVHeader());
                }
                return header;
        }

        /**
         * @param detailStat the detailStat to set
         */
        public void setDetailStat(DetailStat detailStat)
        {
                this.detailStat = detailStat;
        }

        /**
         * @return the detailStat
         */
        public DetailStat getDetailStat()
        {
                return detailStat;
        }

}
