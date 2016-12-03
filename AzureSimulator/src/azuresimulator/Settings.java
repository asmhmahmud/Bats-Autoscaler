/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator;

import azuresimulator.predictor.AccuratePredictor;
import azuresimulator.predictor.Predictor;
import java.text.DecimalFormat;

/**
 *
 * @author Hasan
 */
public class Settings
{

        public static DecimalFormat dft = new DecimalFormat("###.###");
        public static final double[] PERCENTILE_LIST = new double[]
        {
                85, 90, 92, 95, 97, 98, 99,100
        };     
        
      
        public static final int PROCESSOR_MAX_MIPS = 1000;
        public static final String VMM = "Xen";
        
        public static final double TIME_SLOT_LENGTH = 15 * 60;//in second
        public static final double REACTIVE_FEEDBACK_DELAY = 4.75 * 60;
        public static final double VM_CREATE_DELAY = 0* 60;
        public static final int TOTAL_TIME_SLOTS = 15;

        public static final int WORKLOAD_MULTIPLIER = 20000;

        public static final int NUMBER_OF_HOSTS = 200;
        public static final int NUMBER_OF_PROCESSOR_PER_HOST = 6;

        
        public static final String inputFileName = "load.csv";
        public static final int REPEAT_EACH_WORKLOAD_ROW = 1;
        public static final int WORKLOAD_ROW_START = 0;
        public static final String RUBIS_DIST_FILE_NAME = "RBDist.csv";
        public static final String ALGORITHM_PREFIX = "Perc";
        public static final Predictor predictor= new AccuratePredictor();
        
        public static final double TOTAL_PERF_OPT_BUDGET = 1512;
        public static final double TOTAL_BUDGET = TOTAL_PERF_OPT_BUDGET * 0.80;/*1278* 0.90 ;*/
        public static final double MIN_DELAY = 0.6000;
        
        public static final double MAX_DELAY = 1.500;
        public static final int MAX_VM = 200;
        public static final double BUDGET_INCREMENT = 0;

        public static final double RESERVE_INSTANCE_DISCOUNT = 0.80;
        public static final boolean ENABLE_JOB_SCHEDULING = true;
        public static final double STANDARD_V = 5;        

        
        
        
        
}
