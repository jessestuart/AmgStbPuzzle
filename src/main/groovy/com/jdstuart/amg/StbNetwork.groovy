package com.jdstuart.amg

/**
 * Enum of network names, to ensure type safety.
 * 
 * (This could easily be converted into a NetworkRegistry type class 
 *  if the number of networks were to be arbitrarily large.)
 *  
 * @author jdstuart
 *
 */
enum StbNetwork
{
   ABC('ABC'),
   AMC('AMC'),
   BIO('BIO'),
   BRAVO('BRAVO'),
   CC('CC'),
   CW('CW'),
   DISCOVERY('DISCOVERY'),
   E('E!'),
   EPIX('EPIX'),
   FOX('FOX'),
   FX('FX'),
   HBO('HBO'),
   HBO2('HBO2'),
   HBOW('HBOW'),
   HISTORY('HISTORY'),
   ID('ID'),
   MTV('MTV'),
   MTV2('MTV2'),
   NATGEO('NATGEO'),
   NBC('NBC'),
   SCIENCE('SCIENCE'),
   SHO('SHO'),
   SHO2('SHO2'),
   STARZ('STARZ'),
   TBS('TBS'),
   VH1('VH1');
   
   String name
   
   StbNetwork(String name)
   {
      this.name = name
   }
}
