package com.jdstuart.amg

import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * Wrapper object for a "database" of {@link StbDataPoint}s.
 * Currently implemented using a simple hash table mapping a
 * {@link StbDevice} to its corresponding sorted list of
 * {@link StbDataPoint}s, but could be re-written to use an ORM 
 * framework such as Hibernate for scalability.
 * 
 * @author jdstuart
 *
 */
class StbDatabase
{
   /**
    * Data structure used to hold all STB data.
    * 
    * Simply maps the {@link StbDevice} to a sorted list of its
    * corresponding {@link StbDataPoint}s.
    */
   Map<StbDevice, List<StbDataPoint>> db = [:]
   
   /**
    * Copies the data from the csv file into a hash table database.
    */
   StbDatabase(File stbData)
   {
      println 'Initializing database.'
      // Read file line by line, dropping the header on the first line
      stbData.text.readLines().drop(1).each { line ->
         // Get raw strings from row, then convert into wrapper POJO's for type safety
         def row = line.split(',')
         String deviceId = row[0]
         String networkString = row[1].replace('!', '')
         String dateString = row[2]
         
         StbDevice device = StbDeviceRegistry.getDeviceForId(deviceId)
         StbNetwork network = StbNetwork.valueOf(networkString)
         DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
         Date date = format.parse(dateString)
         
         if (!db[device]) db[device] = [];
         db[device] << new StbDataPoint(network, date)
      }
      
      // Finally, sort all of the StbDataPoint lists, so the dates are
      // in chronological order.
      db.each { device, pointList ->
         pointList.sort()
      }
   }
   
   /**
    * Returns the list of {@link StbDataPoint}s corresponding to the
    * specified {@link StbDevice}.
    */
   def getDataPointsForDevice(StbDevice device)
   {
      return db[device]
   }
}
