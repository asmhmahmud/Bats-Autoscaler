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
        public enum VMType
        {

                EXTRA_SMALL
                {
                        @Override
                        public String toString()
                        {
                                return "Extra Small";
                        }
                        
                        @Override
                        public VMConfiguration getVMConfiguration()
                        {
                                return VMConfiguration.getVMConfiguration(1000, 1, 512, 1000, 10000);

                        }
                        
                        public double getPrice()
                        {
                                return 0.02;
                        }
                        
                        
                },
                SMALL
                {
                        @Override
                        public String toString()
                        {
                                return "Small";
                        }
                        
                        @Override
                         public VMConfiguration getVMConfiguration()
                        {
                                return VMConfiguration.getVMConfiguration(1000, 3, 1024, 2000, 20000);
                        }                       
                        
                        public double getPrice()
                        {
                                return 0.08;
                        }                        
                        
                },
                MEDIUM
                {
                        @Override
                        public String toString()
                        {
                                return "Medium";
                        }
                        
                        @Override
                         public VMConfiguration getVMConfiguration()
                        {
                                return VMConfiguration.getVMConfiguration(1000, 5, 2048, 4000, 40000);

                        }    
                         
                        public double getPrice()
                        {
                                return 0.16;
                        }                                                
                        
                };
                
                
                public abstract VMConfiguration getVMConfiguration();
                public abstract double getPrice();
                public static int totalVMType()
                {
                        return 3;
                }
                
                public static VMType getVMTypeByTypeIndex(int vmTypeIndex)
                {
                        VMType vmType= null;
                       switch(vmTypeIndex)
                       {
                               case 0:
                                       vmType = EXTRA_SMALL;
                                       break;
                               case 1: 
                                       vmType = SMALL;
                                       break;
                               case 2:
                                       vmType = MEDIUM;
                                       break;
                       }
                       
                       return vmType;
                }
                
                
                
                public static int getVMTypeIndexByVMType(VMType vmType)
                {
                        int index = -1;
                       switch(vmType)
                       {
                               case EXTRA_SMALL:
                                       index = 0;
                                       break;
                               case SMALL: 
                                       index = 1;
                                       break;
                               case MEDIUM:
                                       index = 2;
                                       break;
                       }
                       
                       return index;
                }
                
        }
        