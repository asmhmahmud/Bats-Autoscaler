/*******************************
** @author A S M Hasan Mahmud
*******************************/

﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AutoScaler.LoadGen
{
    class ExpDistribution
    {
        private static Random rnd = null;
        private static int seed = 123456;

        public static double getExpRandomVal(double mean)
        {

            if (rnd == null)
            {
                rnd = new Random(seed);
            }


            double expRand = -Math.Log(rnd.NextDouble()) * mean;

            return expRand;
        }
    }
}
