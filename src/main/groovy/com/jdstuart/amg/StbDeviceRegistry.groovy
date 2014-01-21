package com.jdstuart.amg

/**
 * Maintains singleton instances of {@link StbDevice}s.
 * 
 * @author jdstuart
 *
 */
class StbDeviceRegistry
{
   /**
    * Maps a device id to corresponding {@link StbDevice}.
    */
   static Map registry = [:]
   
   /**
    * Returns singleton instance of device, given device id
    * @param id 
    * @return
    */
   static StbDevice getDeviceForId(String id)
   {
      return registry[id] ?: (registry[id] = new StbDevice(id))
   }
   
   /**
    * Returns a sorted list of all registered devices.
    */
   static List<StbDevice> getDeviceList()
   {
      return this.registry.values().sort()
   }
}
