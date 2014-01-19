package com.jdstuart.amg

/**
 * 
 * @author jdstuart
 *
 */
class StbViewHistogram
{
   Map<StbNetwork, Integer> histogram = [:]
   
   StbViewHistogram()
   {
      StbNetwork.values().each { network ->
         histogram[network] = 0
      }
   }
   
   int getCountForNetwork(StbNetwork network)
   {
      return histogram[network]
   }
   
   def incrementNetworkCount(StbNetwork network)
   {
      histogram[network]++
   }
   
   def incrementNetworkCounts(List<StbNetwork> networks)
   {
      networks.each { network ->
         this.incrementNetworkCount(network)
      }
   }

   String toString()
   {
      StbNetwork.values().each {
         println "Views for $it : ${histogram[it]}"
      }
   }
   
   TreeMap<StbNetwork, Integer> sortedByCount()
   {
      ValueComparator vc = new ValueComparator(histogram)
      TreeMap<StbNetwork, Integer> sorted = new TreeMap(vc)
      sorted.putAll(histogram)
      return sorted
   }
   
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
   
   List<StbNetwork> getTopFiveNetworks()
   {
      return this.getTopNNetworks(5)
   }
}

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
         return ((map[network1] > map[network2]) ? -1 : 1) 
      }
      else
      {
         // Counts are equal; order alphabetically.
         return (network1.name > network2.name) ? 1 : -1
      }
   }
}