/*******************************
** @author A S M Hasan Mahmud
*******************************/

﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AutoScaler.LoadGen
{
    class Experiment
    {
        private List<Slot> slotList;
        public Experiment()
        {
            slotList= new List<Slot>();
        }

        public void addSlot(Slot slot)
        {
            slotList.Add(slot);
        }


    }
}
