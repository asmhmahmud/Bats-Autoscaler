/*******************************
** @author A S M Hasan Mahmud
*******************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azuresimulator;

import java.text.DecimalFormat;

/**
 *
 * @author Hasan
 */
public class SettingsWiki
{

        public static DecimalFormat dft = new DecimalFormat("###.###");
    
        
//
//        public static final int PROCESSOR_MAX_MIPS = 1000;
//        public static final String VMM = "Xen";
//        public static final double TIME_SLOT_LENGTH = 10 * 60;//in second
//        public static final int TOTAL_TIME_SLOTS = 31 * 24;
//
//        public static final int WORKLOAD_MULTIPLIER = 15000;
//
//
//        public static final int NUMBER_OF_HOSTS = 50;
//        public static final int NUMBER_OF_PROCESSOR_PER_HOST = 8;
//        //public static final double TOTAL_BUDGET=  1597 * 0.80;
//
//        //public static final double UNIT_VM_COST=  0.02;
//        public static final double REACTIVE_FEEDBACK_DELAY = 2.5 * 60;
//
//
//        //File names
//        public static final String inputFileName = "wiki_trace.csv";
//        public static final int REPEAT_EACH_WORKLOAD_ROW = 6;
//        public static final int WORKLOAD_ROW_START = 20*24;
//        /*public static final String inputFileName = "load_msr_azr.csv";*/
//        public static final String RUBIS_DIST_FILE_NAME = "RBDist.csv";
//
//        public static final int VM_CREATE_DELAY = 0;
//
//        /**
//         * ********************* Algorithm Param *********
//         */
//        public static final double TOTAL_BUDGET = 1334 * 0.93 * 6;
//        public static final double MIN_DELAY = 0.520;
//        public static final double MAX_DELAY = 3.500;
//        public static final int MAX_VM = 200;

        /**
         * **************** (Revision: 63) ********* Azure Simulation Validation
         * settings  *****************
         */
        
        
//        public static final int PROCESSOR_MAX_MIPS = 1000;
//        public static final String VMM = "Xen";
//        public static final double TIME_SLOT_LENGTH = 15 * 60;//in second
//
//        public static final int TOTAL_TIME_SLOTS = 48;
//        public static final int MAX_VM = 20;
//
//        public static final int WORKLOAD_MULTIPLIER = 900;
//        public static final int NUMBER_OF_HOSTS = 50;
//        public static final int NUMBER_OF_PROCESSOR_PER_HOST = 8;
//        
//        public static final double REACTIVE_FEEDBACK_DELAY = 2.5 * 60;
//        public static final int REPEAT_EACH_WORKLOAD_ROW = 1;
//        public static final int WORKLOAD_ROW_START = 0;
//        
//        //File names
//        public static final String inputFileName = "load_msr_azr.csv";
//        public static final String RUBIS_DIST_FILE_NAME = "RBDist.csv";
//
//        //public static final int VM_CREATE_DELAY = 5*60;
//        public static final int VM_CREATE_DELAY = 0;
//
//        /**
//         * ********************* Algorithm Param *********
//         */
//        public static final double TOTAL_BUDGET = 8.5;
//        public static final double MIN_DELAY = 0.520;
//        public static final double MAX_DELAY = 1.500;
        /**
         * **********************************                   ******************************************
         */
        
        
        
        /**************   Benchmark ******************************/
        
                public static final int PROCESSOR_MAX_MIPS = 1000;
                public static final String VMM = "Xen";
                public static final double TIME_SLOT_LENGTH = 15 * 60;//in second
                public static final int TOTAL_TIME_SLOTS = 31 * 24;
        
                public static final int WORKLOAD_MULTIPLIER = 15000;
        
        
                public static final int NUMBER_OF_HOSTS = 50;
                public static final int NUMBER_OF_PROCESSOR_PER_HOST = 8;
                //public static final double TOTAL_BUDGET=  1597 * 0.80;
        
                //public static final double UNIT_VM_COST=  0.02;
                //public static final double REACTIVE_FEEDBACK_DELAY = 1.25 * 60;
                public static final double REACTIVE_FEEDBACK_DELAY = 1.25 * 60;
        
        
                //File names
                public static final String inputFileName = "wiki_trace.csv";
                public static final int REPEAT_EACH_WORKLOAD_ROW = 1;
                public static final int WORKLOAD_ROW_START = 20*24;
                public static final String   RUBIS_DIST_FILE_NAME = "RBDist.csv";        
                public static final double VM_CREATE_DELAY = 2.5*60;
        
                /**
                 * ********************* Algorithm Param *********
                 */
                public static final double TOTAL_BUDGET = 1150;/*1278* 0.90 ;*/
                public static final double MIN_DELAY = 0.520;
                public static final double MAX_DELAY = 3.500;
                public static final int MAX_VM = 200;


                public static final double RESERVE_INSTANCE_DISCOUNT = 0.80;
                public static final boolean ENABLE_JOB_SCHEDULING = false;
        
        
        
        
}
