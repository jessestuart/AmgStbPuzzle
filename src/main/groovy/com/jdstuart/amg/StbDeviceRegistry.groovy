package com.jdstuart.amg

class StbDeviceRegistry
{
   static Map registry = [:]
   
   static StbDevice getDeviceForId(String id)
   {
      return registry[id] ?: (registry[id] = new StbDevice(id))
   }
   
   static List<StbDevice> getDeviceList()
   {
      return this.registry.values().sort()
   }
}
