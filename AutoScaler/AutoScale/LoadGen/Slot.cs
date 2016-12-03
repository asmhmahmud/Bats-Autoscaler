/*******************************
** @author A S M Hasan Mahmud
*******************************/

﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AutoScaler.LoadGen
{
    class Slot
    {
        //private List<ResponseDetail> responses;
        private String url;
        //private double arrivalRate;
        private TimeSpan duration;

        public Slot(String url, TimeSpan duration)
        {
            this.url = url;
            this.duration = duration;
        }
        public double getAverageResponseTime()
        {
            return 0;
        }

        public double getAverageConnectionTime()
        {
            return 0;
        }


        private void start()
        {
            for (int i = 0; i < 10; i++)
            {

            }
        }
    }
}
