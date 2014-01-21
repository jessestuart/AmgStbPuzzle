package com.jdstuart.amg

/**
 * Wrapper object for a histogram of view counts per network.
 * 
 * @author jdstuart
 *
 */
class StbViewHistogram
{
   Map<StbNetwork, Integer> histogram = [:]
   
   StbViewHistogram()
   {
      // Initialize all histogram values to zero
      StbNetwork.values().each { network ->
         histogram[network] = 0
      }
   }
   
   /**
    * Returns the histogram count for specified {@link StbNetwork}.
    */
   int getCountForNetwork(StbNetwork network)
   {
      return histogram[network]
   }
   
   /**
    * Increments the histogram count for a single {@link StbNetwork}.
    * 
    * @param network The network to be incremented.
    */
   def void incrementNetworkCount(StbNetwork network)
   {
      histogram[network]++
   }
   
   /**
    * Increments the histogram count for each of the {@link StbNetwork}s in a list.
    * @param networks The networks to be incremented.
    */
   def void incrementNetworkCounts(List<StbNetwork> networks)
   {
      networks.each { network ->
         this.incrementNetworkCount(network)
      }
   }
   
   /**
    * Returns a list of the top five networks, sorted by view count.
    */
   List<StbNetwork> getTopFiveNetworks()
   {
      return this.getTopNNetworks(5)
   }
   
   /**
    * Returns a list of the top 'n' networks, sorted by view count.
    */
   List<StbNetwork> getTopNNetworks(int n)
   {
      List<StbNetwork> topN = []
            
            def sorted = this.sortedByCount()
            int count = 0
            sorted.each { network, viewCount ->
            if (count < n)
            {
               topN << network
               count++
            }
      }
      return topN
   }
   
   /**
    * Returns a TreeMap transformation of the histogram sorted by view count.
    */
   TreeMap<StbNetwork, Integer> sortedByCount()
   {
      ValueComparator vc = new ValueComparator(histogram)
      TreeMap<StbNetwork, Integer> sorted = new TreeMap(vc)
      sorted.putAll(histogram)
      return sorted
   }
   
   String toString()
   {
      StbNetwork.values().each { network ->
         println "Views for $network : ${histogram[network]}"
      }
   }
}

/**
 * Utility class to sort histogram by view count, or, in case
 * of tie, alphabetically.
 * 
 * @author jdstuart
 *
 */
class ValueComparator implements Comparator<StbNetwork>
{
   Map<StbNetwork, Integer> map
   
   public ValueComparator(Map map)
   {
      this.map = map
   }

   @Override
   public int compare(StbNetwork network1, StbNetwork network2)
   {
      if (!(map[network1] == map[network2]))
      {
         return (map[network1] > map[network2]) ? -1 : 1 
      }
      else
      {
         // Counts are equal; order alphabetically.
         return (network1.name > network2.name) ? 1 : -1
      }
   }
}