package com.jdstuart.amg

import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * 
 * @author jdstuart
 *
 */
class StbDatabase
{
   Map<StbDevice, List<StbDataPoint>> db = [:]
   
   StbDatabase(File stbData)
   {
      stbData.text.readLines().drop(1).each { line ->
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
      
      db.each { device, pointList ->
         pointList.sort()
      }
   }
   
   def getPointsForDevice(String id)
   {
      StbDevice device = StbDeviceRegistry.getDeviceForId(id)
      return db[device]
   }
   
   def getDb()
   {
      return this.db
   }
   
   static main(String[] args)
   {
      def _ticks = System.nanoTime()
      
      File data = new File('src/main/resources/input.csv')
      StbDatabase db = new StbDatabase(data)
      
      def elapsed = System.nanoTime() - _ticks
      println "elapsed : ${elapsed / 1e6}"
      println db.getPointsForDevice("BA20VY674I") 
   }
}
