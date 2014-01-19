package com.jdstuart.amg

import java.text.SimpleDateFormat
import java.util.Date;
import java.util.List;

class StbOptimizer
{
   StbDatabase db
   
   def initializeDatabase(File stbData)
   {
      this.db = new StbDatabase(stbData)
   }
   
   def optimizeForTimeRange(Date start, Date end)
   {
      StbViewHistogram histogram = new StbViewHistogram()
      
      db.getDb().each { device, points ->
         println "Processing device : ${device.id}"
         def networksViewedForDevice = getNetworksViewedForTimeRange(points, start, end)
         histogram.incrementNetworkCounts(networksViewedForDevice)
      }
      
      def sortedHistogram = histogram.sortedByCount()
      
      sortedHistogram.each { network, viewCount ->
         println "Views for ${network.name} : ${viewCount}"
      }
      
      return histogram
   }
   
   def getNetworksViewedForTimeRange(List<StbDataPoint> points, Date start, Date end)
   {
      final int secondsInFiveMinutes = 300
      final int secondsInFiveHours = 18000
      List<StbNetwork> networksViewed = []
      
      (0..points.size()-1).each { i ->
         // No need to continue if network is already in list of networks viewed
         if (networksViewed.contains(points[i].network)) return;
         
         Date tuneIn = points[i].date
         Date tuneOut = points[i+1]?.date ?: end
         
         // Check if set was turned off
         if ((tuneOut.getTime() - tuneIn.getTime()) / 1e3 > 18000) return;
         // Check if period tunes out before start, or tunes in after end (==> data not relevant)
         if (tuneOut.before(start) || tuneIn.after(end)) return;
         
         // Otherwise calculate the number of seconds viewed within specified range
         else
         {
            // At least part of the viewing is within the range. Calculate how much.
            
            if (tuneIn.before(start))
            {
               tuneIn = start
            }
            if (tuneOut.after(end))
            {
               tuneOut = end
            }
            println "Viewing $tuneIn to $tuneOut partially in range. Network: ${points[i].network}"
            println "tune in: ${tuneIn.getTime()}, tune out: ${tuneOut.getTime()}"
            def secondsViewedInRange = (tuneOut.getTime() - tuneIn.getTime()) / 1e3
            println "seconds: ${secondsViewedInRange}"
            
            if (secondsViewedInRange >= secondsInFiveMinutes)
            {
               networksViewed << points[i].network
            }
         }
      }
      println "Networks viewed : $networksViewed"
      return networksViewed 
   }
   
   static void main(String[] args)
   {
      String path = args[0]
      File stbData = new File(args[0])
      if (!stbData.exists()) throw new FileNotFoundException()
      
      StbOptimizer optimizer = new StbOptimizer()
      optimizer.initializeDatabase(stbData)
      Date start = new SimpleDateFormat('yyyy-MM-dd HH:mm:ss').parse('2013-08-22 17:00:00')
      Date end = new SimpleDateFormat('yyyy-MM-dd HH:mm:ss').parse('2013-08-22 19:00:00')
      StbViewHistogram histogram = optimizer.optimizeForTimeRange(start, end)
      List top5 = histogram.getTopFiveNetworks()
      println top5
      
      // Output to csv file. Arbitrarily using current directory for this.
      String currentDir = System.getProperty('user.dir')
      File output = new File(currentDir, 'top_networks.csv')
      StringBuilder outputString = new StringBuilder()
      top5.each { network ->
         outputString.append("${network.name},${histogram.getCountForNetwork(network)}\n")
      }
      println outputString
      output.write(outputString.toString())
      
   }
}
