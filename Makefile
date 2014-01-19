run:
	pushd "src/main/groovy"; groovy com/jdstuart/amg/StbOptimizer.groovy ${input} 
	mv "src/main/groovy/top_networks.csv" $(CURDIR)