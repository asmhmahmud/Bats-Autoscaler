/*******************************
** @author A S M Hasan Mahmud
*******************************/

﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AutoScaler.AutoScale
{
    class SlotInfo
    {
        private static String delimeter = ",";
        public int slotNo;
        public DateTime slotStartTime;
        //public DateTime slotFinishTime;   
        public double measuredWorkload;
        public double predictedWorkload;
        public int instances;
        public double v;
        public double queueLength;
        public double responseTime;
        public double budDeficit;
        public double throughPut = 0;

        public String getCSVLines()
        {
            StringBuilder sb = new StringBuilder();
            sb.Append(slotNo);
            sb.Append(delimeter);
            sb.Append(slotStartTime);
            //sb.Append(delimeter);
            //sb.Append(measuredWorkload);
            sb.Append(delimeter);
            sb.Append(predictedWorkload);
            sb.Append(delimeter);
            sb.Append(instances);
            sb.Append(delimeter);
            sb.Append(v);
            sb.Append(delimeter);
            sb.Append(queueLength);
            sb.Append(delimeter);
            sb.Append(responseTime);
            sb.Append(delimeter);
            sb.Append(budDeficit);
            sb.Append(delimeter);
            sb.Append(throughPut);
            return sb.ToString();
        }
        public static String getCSVHeader()
        {
            StringBuilder sb = new StringBuilder();
            sb.Append("Slot No,");
            sb.Append("Start Time,");
            //sb.Append("Finish Time,");
            //sb.Append("Measured Workload (req/sec),");
            sb.Append("Predicted Workload (req/sec),");
            sb.Append("VM Instances,");
            sb.Append("V,");
            sb.Append("Queue Length,");
            sb.Append("Average Response Time (ms),");
            sb.Append("Budget Deficit,");
            sb.Append("Throughput");
            return sb.ToString();
        }


    }
}
