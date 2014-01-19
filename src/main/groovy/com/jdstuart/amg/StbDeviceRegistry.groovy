package com.jdstuart.amg

class StbDeviceRegistry
{
   static Map registry = [:]
   
   static StbDevice getDeviceForId(String id)
   {
      if (!registry[id])
      {
         registry[id] = new StbDevice(id)
      }
      return registry[id]
   }
   
   static List<StbDevice> getDeviceList()
   {
      return this.registry.values().sort()
   }
}
