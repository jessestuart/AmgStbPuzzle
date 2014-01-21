package com.jdstuart.amg

import java.text.SimpleDateFormat
import java.util.Date;

/**
 * Main entry point for processing. The StbOptimizer class:
 * <ul>
 *    <li>Initializes an {@link StbDatabase} of {@link StbDataPoint}s, sorted by date.</li>
 *    <li>Given a start and end date, generates a histogram of networks viewed during that period.</li>
 *    <li>Writes the top five most-viewed networks to file in CSV format.
 * </ul>
 * 
 * @author jdstuart
 *
 */
class StbOptimizer
{
   StbDatabase db
   
   /**
    * Initializes a {@link StbDatabase}} with data from a
    * comma-separated values file.
    * 
    * @param stbData CSV file passed in via command line.
    */
   void initializeDatabase(File stbData)
   {
      this.db = new StbDatabase(stbData)
   }
   
   /**
    * Given a start and end time, calculates the number of "views" 
    * (>= 5 min viewing time) per network within the given time 
    * range, by iterating through the {@link StbDatabase} and
    * calculating the number of unique views per device. 
    * 
    * Note: requires that the StbDatabase be initialized first, by calling
    * initializeDatabase().
    * 
    * @return A StbViewHistogram 
    */
   StbViewHistogram generateHistogramForTimeRange(Date start, Date end)
   {
      StbViewHistogram histogram = new StbViewHistogram()
      
      // For each device in the database, get a unique list of networks viewed within time range
      StbDeviceRegistry.getDeviceList().each { device ->
         println "Processing device : ${device.id}"
         List<StbDataPoint> points = db.getDataPointsForDevice(device)
         def networksViewedForDevice = getNetworksViewedForTimeRange(points, start, end)
         // Increment the histogram for each of the networks viewed
         histogram.incrementNetworkCounts(networksViewedForDevice)
      }
      
      // Sort the histogram by view count
      TreeMap sortedHistogram = histogram.sortedByCount()
      
      sortedHistogram.each { network, viewCount ->
         println "Views for ${network.name} : ${viewCount}"
      }
      
      return histogram
   }
   
   /**
    * Given a list of {@link StbDataPoint}s:
    * <ul>
    *    <li>First determine which points overlap with given time range.</li>
    *    <li>Of the points that do, determine those with an overlap of >= 5min.</li>
    *    <li>If overlap is >= 5min, add that point's network value to a unique list of networks</li>
    * </ul>
    * @return List of networks viewed during time range.
    */
   List<StbNetwork> getNetworksViewedForTimeRange(List<StbDataPoint> points, Date start, Date end)
   {
      
      List<StbNetwork> networksViewed = []
      
      (0..points.size()-1).each { i ->
         // No need to continue if network is already in list of networks viewed
         if (networksViewed.contains(points[i].network)) return;
         
         Date tuneIn = points[i].date
         Date tuneOut = points[i+1]?.date ?: end
         
         // If period constitutes a valid view, add network to the list. 
         if (this.isViewForTimeRange(tuneIn, tuneOut, start, end))
         {
            networksViewed << points[i].network
         }
      }
      println "Networks viewed: ${networksViewed}"
      return networksViewed 
   }
   
   /**
    * Returns true if tuneIn/tuneOut time range constitutes a valid view
    * (i.e., >= 5min viewing time within the specified time range);
    * else returns false.
    */
   boolean isViewForTimeRange(Date tuneIn, Date tuneOut, Date start, Date end)
   {
      final int secondsInFiveMinutes = 300
      final int secondsInFiveHours = 18000
      
      // Check if period tunes out before start, or tunes in after end (==> data not relevant)
      if (tuneOut.before(start) || tuneIn.after(end)) return false;
      
      // At least part of the viewing is within the range. Calculate how much.
      else
      {
         // Cut viewing off after five hours
         if (((tuneOut.getTime() - tuneIn.getTime()) / 1e3) > secondsInFiveHours)
         {
            // Reassign tuneOut time to five hours after tuneIn
            Calendar cal = Calendar.getInstance()
            cal.setTime(tuneIn)
            cal.add(Calendar.HOUR, 5)
            tuneOut = cal.getTime()
         }
         // If user tuned in before start time, we only care how much time elapsed
         // between start time and when they tuned out.
         if (tuneIn.before(start))
         {
            tuneIn = start
         }
         // Likewise if a user tunes out after the end time.
         if (tuneOut.after(end))
         {
            tuneOut = end
         }
         
         int secondsViewedInRange = (tuneOut.getTime() - tuneIn.getTime()) / 1e3
         
         return (secondsViewedInRange >= secondsInFiveMinutes)
      }
   }
   
   static void main(String[] args)
   {
      def _ticks = System.nanoTime()
      
      println "Beginning processing."
      String path = args[0]
      File stbData = new File(args[0])
      if (!stbData.exists()) throw new FileNotFoundException()
      
      StbOptimizer optimizer = new StbOptimizer()
      optimizer.initializeDatabase(stbData)
      
      // Create the start and end times (in this example, 5pm and 7pm on 8/22/13)
      String fivePM = '2013-08-22 17:00:00'
      String sevenPM = '2013-08-22 19:00:00'
      SimpleDateFormat format = new SimpleDateFormat('yyyy-MM-dd HH:mm:ss')  
      Date start = format.parse(fivePM)
      Date end = format.parse(sevenPM)
      
      // Build the histogram for the given time range, and pick out the top 5
      StbViewHistogram histogram = optimizer.generateHistogramForTimeRange(start, end)
      List top5 = histogram.getTopFiveNetworks()
      
      println "Time elapsed : ${(System.nanoTime() - _ticks) / 1e6}"
      
      // Output to csv file. Arbitrarily using current directory for this.
      String currentDir = System.getProperty('user.dir')
      File output = new File(currentDir, 'top_networks.csv')
      StringBuilder outputString = new StringBuilder()
      top5.each { network ->
         outputString.append("${network.name},${histogram.getCountForNetwork(network)}\n")
      }
      output.write(outputString.toString())
   }
}