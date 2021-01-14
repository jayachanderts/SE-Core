package SeCorePlatform;

import java.math.BigDecimal;
import java.util.*;

/**
 * CoreDictionary is a dictionary used to initialize cost of the system and no. of systems.
 * 
 * @author JC
 *
 */
class CoreDictionary 
{
	static Hashtable<String, Map<String,Double>> regionCostDictionary = new Hashtable<String, Map<String,Double>>();
	static Hashtable<String, Integer> systemDictionary = new Hashtable<String, Integer>();

	public static void main(String[] args)
	{
        // Using a few dictionary Class methods
		// using put method
		Map<String,Double> map = new HashMap<String,Double>();
		map.put("large",0.12);
		map.put("xlarge",0.23);
		map.put("2xlarge",0.45);
		map.put("4xlarge",0.774);
		map.put("8xlarge",1.4);
		map.put("10xlarge",2.82);
		regionCostDictionary.put("us-east", map);
		map = new HashMap<String,Double>();
		map.put("large",0.14);
		map.put("2xlarge",0.413);
		map.put("4xlarge",0.89);
		map.put("8xlarge",1.3);
		map.put("10xlarge",2.97);
		regionCostDictionary.put("us-west", map);
		map = new HashMap<String,Double>();
		map.put("large",0.11);
		map.put("xlarge",0.20);
		map.put("4xlarge",0.67);
		map.put("8xlarge",1.18);
		regionCostDictionary.put("asia", map);
		systemDictionary.put("large",1);
		systemDictionary.put("xlarge",2);
		systemDictionary.put("2xlarge",4);
		systemDictionary.put("4xlarge",8);
		systemDictionary.put("8xlarge",16);
		systemDictionary.put("10xlarge",32);
	}
	
	public Hashtable<String, Map<String,Double>> getResionCostDictionary(){
		return regionCostDictionary;
	}
	public Hashtable<String, Integer> getSystemDictionary(){
		return systemDictionary;
	}
} 
/**
 * InstanceAllocator is the class to allocate the CPU instances based on the requirements 
 * 
 * @author JC
 *
 */
public class InstanceAllocator {
	
	/** the Ten XL constant*/
	private static final String TEN_XL = "10xlarge";
	/** the Eight XL constant*/
	private static final String EIGHT_XL = "8xlarge";
	/** the Four XL constant*/
	private static final String FOUR_XL = "4xlarge";
	/** the Two XL constant*/
	private static final String TWO_XL = "2xlarge";
	/** the XL constant*/
	private static final String XL = "xlarge";
	/** the large constant*/
	private static final String LARGE = "large"; 

	/**
	 * main is the main method of the Instance Allocator module
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner inputScanner = new Scanner(System.in);
		System.out.println("Total number of hours required: ");
		Integer requiredHours =  inputScanner.nextInt();
		System.out.println("Total number of CPU's required(If any): ");
		Integer requiredSystem = inputScanner.nextInt();
		System.out.println("Overall Cost estimated(if any): ");
		Double totalCost =  inputScanner.nextDouble();
		inputScanner.close();
		getCosts(requiredHours,requiredSystem,totalCost);
	}

	/**
	 * getCosts method to perform the calculations
	 * 
	 * @param requiredHours
	 * @param requiredSystem
	 * @param totalCost
	 */
	private static void getCosts(Integer requiredHours, Integer requiredSystem,
			Double totalCost) {
		CoreDictionary dictionary = new CoreDictionary();
		Hashtable<String, Map<String,Double>> regionCostDictionary = dictionary.getResionCostDictionary();
		Hashtable<String, Integer> systemDictionary =dictionary.getSystemDictionary();
		if(Objects.nonNull(requiredHours)&& Objects.nonNull(requiredSystem) && Objects.nonNull(totalCost)) {
			calculate(requiredHours,requiredSystem,totalCost,regionCostDictionary,systemDictionary);
		} else if(Objects.nonNull(requiredHours)&& Objects.nonNull(requiredSystem)) {
			calculate(requiredHours,requiredSystem,regionCostDictionary,systemDictionary);
		} else if(Objects.nonNull(requiredHours)&& Objects.nonNull(totalCost)) {
			calculate(requiredHours,totalCost,regionCostDictionary,systemDictionary);
		} else {
			System.out.println("No clear inputs for processing. Kinldy retry!!!!!");
		}
	}

	/**
	 * calculate method to calculate the instance to be allocated when all the required inputs are provided.
	 * 
	 * @param requiredHours
	 * @param requiredSystem
	 * @param totalCost
	 * @param regionCostDictionary
	 * @param systemDictionary
	 */
	private static void calculate(Integer requiredHours, Integer requiredSystem, Double totalCost,
			Hashtable<String, Map<String,Double>> regionCostDictionary, Hashtable<String, Integer> systemDictionary) {
		double costPerHour = (double) (totalCost/requiredHours);
		double costAllocated = 0;
		int systemCount = 0;
		Integer asiaCount,eastCount,westCount;
		asiaCount = eastCount = westCount = 0;
		Map<String,Integer> asiaMap = new HashMap<String,Integer>();
		Map<String,Integer> eastMap = new HashMap<String,Integer>();
		Map<String,Integer> westMap = new HashMap<String,Integer>();
		int tempAsia,tempEast,tempWest;
		while (costAllocated >= costPerHour) {
			Integer remainingSystem = requiredSystem-systemCount;
			tempAsia = asiaCount;
			tempEast = eastCount;
			tempWest = westCount;
 			if(remainingSystem <= systemDictionary.get(TEN_XL)) {
				getValue(systemCount,costAllocated,TEN_XL, regionCostDictionary, systemDictionary,asiaCount,eastCount,westCount);
				formServerMap(tempAsia,tempEast,tempWest,asiaMap,eastMap,westMap,asiaCount,eastCount,westCount,TEN_XL);
			} else if(remainingSystem <= systemDictionary.get(EIGHT_XL)) {
				getValue(systemCount,costAllocated,EIGHT_XL, regionCostDictionary, systemDictionary,asiaCount,eastCount,westCount);
				formServerMap(tempAsia,tempEast,tempWest,asiaMap,eastMap,westMap,asiaCount,eastCount,westCount,EIGHT_XL);
			} else if(remainingSystem <= systemDictionary.get(FOUR_XL)) {
				getValue(systemCount,costAllocated,FOUR_XL, regionCostDictionary, systemDictionary,asiaCount,eastCount,westCount);
				formServerMap(tempAsia,tempEast,tempWest,asiaMap,eastMap,westMap,asiaCount,eastCount,westCount,FOUR_XL);
			} else if(remainingSystem <= systemDictionary.get(TWO_XL)) {
				getValue(systemCount,costAllocated,TWO_XL, regionCostDictionary, systemDictionary,asiaCount,eastCount,westCount);
				formServerMap(tempAsia,tempEast,tempWest,asiaMap,eastMap,westMap,asiaCount,eastCount,westCount,TWO_XL);
			} else if(remainingSystem <= systemDictionary.get(XL)) {
				getValue(systemCount,costAllocated,XL, regionCostDictionary, systemDictionary,asiaCount,eastCount,westCount);
				formServerMap(tempAsia,tempEast,tempWest,asiaMap,eastMap,westMap,asiaCount,eastCount,westCount,XL);
			} else {
				getValue(systemCount,costAllocated,LARGE, regionCostDictionary, systemDictionary,asiaCount,eastCount,westCount);
				formServerMap(tempAsia,tempEast,tempWest,asiaMap,eastMap,westMap,asiaCount,eastCount,westCount,LARGE);
			}
		}
		printResult(asiaMap,eastMap,westMap,requiredHours,asiaCount,eastCount,westCount);
	}
	
	/**
	 * calculate method to calculate the instance to be allocated when only required system and hours are provided.
	 * 
	 * @param requiredHours
	 * @param requiredSystem
	 * @param regionCostDictionary
	 * @param systemDictionary
	 */
	private static void calculate(Integer requiredHours, Integer requiredSystem,
			Hashtable<String, Map<String, Double>> regionCostDictionary, Hashtable<String, Integer> systemDictionary) {
		double costAllocated = 0;
		int systemCount = 0;
		Integer asiaCount,eastCount,westCount;
		asiaCount = eastCount = westCount = 0;
		Map<String,Integer> asiaMap = new HashMap<String,Integer>();
		Map<String,Integer> eastMap = new HashMap<String,Integer>();
		Map<String,Integer> westMap = new HashMap<String,Integer>();
		int tempAsia,tempEast,tempWest;
		while (requiredSystem >= systemCount) {
			Integer remainingSystem = requiredSystem-systemCount;
			tempAsia = asiaCount;
			tempEast = eastCount;
			tempWest = westCount;
 			if(remainingSystem <= systemDictionary.get(TEN_XL)) {
				getValue(systemCount,costAllocated,TEN_XL, regionCostDictionary, systemDictionary,asiaCount,eastCount,westCount);
				formServerMap(tempAsia,tempEast,tempWest,asiaMap,eastMap,westMap,asiaCount,eastCount,westCount,TEN_XL);
			} else if(remainingSystem <= systemDictionary.get(EIGHT_XL)) {
				getValue(systemCount,costAllocated,EIGHT_XL, regionCostDictionary, systemDictionary,asiaCount,eastCount,westCount);
				formServerMap(tempAsia,tempEast,tempWest,asiaMap,eastMap,westMap,asiaCount,eastCount,westCount,EIGHT_XL);
			} else if(remainingSystem <= systemDictionary.get(FOUR_XL)) {
				getValue(systemCount,costAllocated,FOUR_XL, regionCostDictionary, systemDictionary,asiaCount,eastCount,westCount);
				formServerMap(tempAsia,tempEast,tempWest,asiaMap,eastMap,westMap,asiaCount,eastCount,westCount,FOUR_XL);
			} else if(remainingSystem <= systemDictionary.get(TWO_XL)) {
				getValue(systemCount,costAllocated,TWO_XL, regionCostDictionary, systemDictionary,asiaCount,eastCount,westCount);
				formServerMap(tempAsia,tempEast,tempWest,asiaMap,eastMap,westMap,asiaCount,eastCount,westCount,TWO_XL);
			} else if(remainingSystem <= systemDictionary.get(XL)) {
				getValue(systemCount,costAllocated,XL, regionCostDictionary, systemDictionary,asiaCount,eastCount,westCount);
				formServerMap(tempAsia,tempEast,tempWest,asiaMap,eastMap,westMap,asiaCount,eastCount,westCount,XL);
			} else {
				getValue(systemCount,costAllocated,LARGE, regionCostDictionary, systemDictionary,asiaCount,eastCount,westCount);
				formServerMap(tempAsia,tempEast,tempWest,asiaMap,eastMap,westMap,asiaCount,eastCount,westCount,LARGE);
			}
		}
		printResult(asiaMap,eastMap,westMap,requiredHours,asiaCount,eastCount,westCount);
	}
	
	/**
	 * calculate method to calculate the instance to be allocated when only total cost and required hours are provided.
	 * 
	 * @param requiredHours
	 * @param totalCost
	 * @param regionCostDictionary
	 * @param systemDictionary
	 */
	private static void calculate(Integer requiredHours, Double totalCost,
			Hashtable<String, Map<String, Double>> regionCostDictionary, Hashtable<String, Integer> systemDictionary) {
		double costPerHour = (double) (totalCost/requiredHours);
		double costAllocated = 0;
		int systemCount = 0;
		Integer asiaCount,eastCount,westCount;
		asiaCount = eastCount = westCount = 0;
		Map<String,Integer> asiaMap = new HashMap<String,Integer>();
		Map<String,Integer> eastMap = new HashMap<String,Integer>();
		Map<String,Integer> westMap = new HashMap<String,Integer>();
		int tempAsia,tempEast,tempWest;
		while (costAllocated >= costPerHour) {
			Double remainingCost = costPerHour - costAllocated;
			tempAsia = asiaCount;
			tempEast = eastCount;
			tempWest = westCount;
			String systemSize = getSizeBasedOnCost(costPerHour,remainingCost,regionCostDictionary,systemDictionary);
 			if(systemSize.equals(TEN_XL)) {
				getValue(systemCount,costAllocated,TEN_XL, regionCostDictionary, systemDictionary,asiaCount,eastCount,westCount);
				formServerMap(tempAsia,tempEast,tempWest,asiaMap,eastMap,westMap,asiaCount,eastCount,westCount,TEN_XL);
			} else if(systemSize.equals(EIGHT_XL)) {
				getValue(systemCount,costAllocated,EIGHT_XL, regionCostDictionary, systemDictionary,asiaCount,eastCount,westCount);
				formServerMap(tempAsia,tempEast,tempWest,asiaMap,eastMap,westMap,asiaCount,eastCount,westCount,EIGHT_XL);
			} else if(systemSize.equals(FOUR_XL)) {
				getValue(systemCount,costAllocated,FOUR_XL, regionCostDictionary, systemDictionary,asiaCount,eastCount,westCount);
				formServerMap(tempAsia,tempEast,tempWest,asiaMap,eastMap,westMap,asiaCount,eastCount,westCount,FOUR_XL);
			} else if(systemSize.equals(TWO_XL)) {
				getValue(systemCount,costAllocated,TWO_XL, regionCostDictionary, systemDictionary,asiaCount,eastCount,westCount);
				formServerMap(tempAsia,tempEast,tempWest,asiaMap,eastMap,westMap,asiaCount,eastCount,westCount,TWO_XL);
			} else if(systemSize.equals(XL)) {
				getValue(systemCount,costAllocated,XL, regionCostDictionary, systemDictionary,asiaCount,eastCount,westCount);
				formServerMap(tempAsia,tempEast,tempWest,asiaMap,eastMap,westMap,asiaCount,eastCount,westCount,XL);
			} else {
				getValue(systemCount,costAllocated,LARGE, regionCostDictionary, systemDictionary,asiaCount,eastCount,westCount);
				formServerMap(tempAsia,tempEast,tempWest,asiaMap,eastMap,westMap,asiaCount,eastCount,westCount,LARGE);
			}
		}
		printResult(asiaMap,eastMap,westMap,requiredHours,asiaCount,eastCount,westCount);
	}

	/**
	 * getSizebasedOnCost method to get the size of the system required based on cost.
	 * 
	 * @param costPerHour
	 * @param remainingCost
	 * @param regionCostDictionary
	 * @return size of the system to be allocated
	 */
	private static String getSizeBasedOnCost(double costPerHour, Double remainingCost,
			Hashtable<String, Map<String, Double>> regionCostDictionary,Hashtable<String, Integer> systemDictionary) {
		Map<String,Double> lowCostMap =  getCostMap(regionCostDictionary,systemDictionary);
		if(remainingCost <= lowCostMap.get(LARGE)) {
			return LARGE;
		} else if(remainingCost <= lowCostMap.get(XL)) {
			return XL;
		} else if(remainingCost <= lowCostMap.get(TWO_XL)) {
			return TWO_XL;
		} else if(remainingCost <= lowCostMap.get(FOUR_XL)) {
			return FOUR_XL;
		} else if(remainingCost <= lowCostMap.get(EIGHT_XL)) {
			return EIGHT_XL;
		} else {
			return TEN_XL;
		}
	}

	/**
	 * @param regionCostDictionary
	 * @param systemDictionary
	 * @return cost map which is low compared to the regions available.
	 */
	private static Map<String, Double> getCostMap(Hashtable<String, Map<String, Double>> regionCostDictionary,
			Hashtable<String, Integer> systemDictionary) {
		Map<String,Double> lowCostMap = new HashMap<String,Double>();
		Map<String,Double> eastMap = regionCostDictionary.get("us-east");
		Map<String,Double> westMap = regionCostDictionary.get("us-west");
		Map<String,Double> asiaMap = regionCostDictionary.get("asia");
		for (Map.Entry<String,Integer> mapElement : systemDictionary.entrySet()) {
			String size = mapElement.getKey();
			Double asiaCost,eastCost,westCost;
			asiaCost = eastCost = westCost = 999D;
			if(asiaMap.containsKey(size)) {
				asiaCost = asiaMap.get(size);
			}
			if(eastMap.containsKey(size)) {
				eastCost = eastMap.get(size);
			}
			if(westMap.containsKey(size)) {
				westCost = westMap.get(size);
			}
			if(Double.compare(eastCost, westCost) <= 0) {
				if(Double.compare(eastCost, asiaCost) <= 0) {
					lowCostMap.put(size, eastCost);
				} else {
					lowCostMap.put(size, asiaCost);
				}
			} else {
				if(Double.compare(westCost, asiaCost) <= 0) {
					lowCostMap.put(size, westCost);
				} else {
					lowCostMap.put(size, asiaCost);
				}
			}
		}
		return lowCostMap;
	}

	/**
	 * getValue method to calculate the allocated cost and systems based on the regions available in Core Dictionary.
	 * 
	 * @param systemCount
	 * @param costAllocated
	 * @param size
	 * @param regionCostDictionary
	 * @param systemDictionary
	 * @param asiaCount
	 * @param eastCount
	 * @param westCount
	 */
	private static void getValue(int systemCount, double costAllocated, String size,
			Hashtable<String, Map<String,Double>> regionCostDictionary, Hashtable<String, Integer> systemDictionary, Integer asiaCount, Integer eastCount, Integer westCount) {
		Double costPerHour = 0D;
		switch(size) {
		case TEN_XL:
			getCostPerHour(costPerHour,size, regionCostDictionary,asiaCount,eastCount,westCount);
			costAllocated = costPerHour + costAllocated;
			systemCount =  systemCount + systemDictionary.get(TEN_XL);
			break;
		case EIGHT_XL:
			getCostPerHour(costPerHour,size, regionCostDictionary,asiaCount,eastCount,westCount);
			costAllocated = costPerHour + costAllocated;
			systemCount =  systemCount + systemDictionary.get(EIGHT_XL);
			break;
		case FOUR_XL:
			getCostPerHour(costPerHour,size, regionCostDictionary,asiaCount,eastCount,westCount);
			costAllocated = costPerHour + costAllocated;
			systemCount =  systemCount + systemDictionary.get(FOUR_XL);
			break;
		case TWO_XL:
			getCostPerHour(costPerHour,size, regionCostDictionary,asiaCount,eastCount,westCount);
			costAllocated = costPerHour + costAllocated;
			systemCount =  systemCount + systemDictionary.get(TWO_XL);
			break;
		case XL:
			getCostPerHour(costPerHour,size, regionCostDictionary,asiaCount,eastCount,westCount);
			costAllocated = costPerHour + costAllocated;
			systemCount =  systemCount + systemDictionary.get(XL);
			break;
		case LARGE:
			getCostPerHour(costPerHour,size, regionCostDictionary,asiaCount,eastCount,westCount);
			costAllocated = costPerHour + costAllocated;
			systemCount =  systemCount + systemDictionary.get(LARGE);
			break;
		}
	}
	
	/**
	 * getCostPerHour method to get the cost per hour of the systems allocated based on the region.
	 * 
	 * @param costPerHour
	 * @param size
	 * @param regionCostDictionary
	 * @param asiaCount
	 * @param eastCount
	 * @param westCount
	 */
	private static void getCostPerHour(Double costPerHour, String size, Hashtable<String, Map<String,Double>> regionCostDictionary, Integer asiaCount, Integer eastCount, Integer westCount) {
		Map<String,Double> asiaMap = regionCostDictionary.get("asia");
		Map<String,Double> eastMap = regionCostDictionary.get("us-east");
		Map<String,Double> westMap = regionCostDictionary.get("us-west");
		double asiaCost,eastCost,westCost;
		asiaCost = eastCost = westCost = 999d;
		if(asiaMap.containsKey(size)) {
			asiaCost = asiaMap.get(size);
		}
        if(eastMap.containsKey(size)) {
        	eastCost = eastMap.get(size);
		}
        if(eastMap.containsKey(size)) {
        	westCost = westMap.get(size);
		}
		if(Double.compare(eastCost, westCost) <= 0) {
			if(Double.compare(eastCost, asiaCost) <= 0) {
				costPerHour =  eastCost;
				eastCount++;
			} else {
				costPerHour =  asiaCost;
				asiaCount++;
			}
		} else {
			if(Double.compare(westCost, asiaCost) <= 0) {
				costPerHour =  westCost;
				westCount++;
			} else {
				costPerHour =  asiaCost;
				asiaCount++;
			}
		}
	}
	
	/**
	 * formServerMap method to get the servers updated on each region based on the allocations made earlier.
	 * 
	 * @param tempAsia
	 * @param tempEast
	 * @param tempWest
	 * @param asiaMap
	 * @param eastMap
	 * @param westMap
	 * @param asiaCount
	 * @param eastCount
	 * @param westCount
	 * @param size
	 */
	private static void formServerMap(int tempAsia, int tempEast, int tempWest, Map<String, Integer> asiaMap,
			Map<String, Integer> eastMap, Map<String, Integer> westMap, Integer asiaCount, Integer eastCount,
			Integer westCount,String size) {
		if(tempAsia != asiaCount) {
			asiaMap.put(size, asiaCount);
		} else if(tempEast != eastCount) {
			eastMap.put(size, eastCount);
		} else if(tempWest != westCount) {
			westMap.put(size, westCount);
		}
	}
	
	/**
	 * printResult method to print the output in the desired format.
	 * 
	 * @param asiaMap
	 * @param eastMap
	 * @param westMap
	 * @param requiredHours
	 * @param asiaCount
	 * @param eastCount
	 * @param westCount
	 */
	private static void printResult(Map<String, Integer> asiaMap, Map<String, Integer> eastMap,
			Map<String, Integer> westMap, Integer requiredHours, Integer asiaCount, Integer eastCount,
			Integer westCount) {
		List<Map<String,Object>> resultList = new ArrayList<>();
		Map<String,Object> resultMap = new HashMap<String,Object>();
        if(eastCount > 0) {
        	resultMap.put("region", "us-east");
        	resultMap.put("total_costs", getCostBasedOnRegion(eastMap,"us-east",requiredHours));
        	resultMap.put("servers", eastMap);
        	resultList.add(resultMap);
        }
        if(westCount > 0) {
        	resultMap = new HashMap<String,Object>();
        	resultMap.put("region", "us-west");
        	resultMap.put("total_costs", getCostBasedOnRegion(westMap,"us-west",requiredHours));
        	resultMap.put("servers", westMap);
        	resultList.add(resultMap);
		}
		if(asiaCount > 0) {
			resultMap = new HashMap<String,Object>();
			resultMap.put("region", "asia");
			resultMap.put("total_costs", getCostBasedOnRegion(asiaMap,"asia",requiredHours));
			resultMap.put("servers", asiaMap);
			resultList.add(resultMap);
		}
		System.out.println(resultList);
	}

	/**
	 * getCostBasedOnRegion method to calculate total costs for each regions in which the systems are allocated.
	 * 
	 * @param regionMap
	 * @param region
	 * @param requiredHours
	 * @return cost based on region
	 */
	private static BigDecimal getCostBasedOnRegion(Map<String, Integer> regionMap,String region, Integer requiredHours) {
		CoreDictionary dictionary = new CoreDictionary();
		Hashtable<String, Map<String,Double>> regionCostDictionary = dictionary.getResionCostDictionary();
		Map<String,Double> regionCostMap = new HashMap<String,Double>();
		if(region.equals("us-east")) {
			regionCostMap = regionCostDictionary.get("us-east");
		} else if(region.equals("us-west")) {
			regionCostMap = regionCostDictionary.get("us-west");
		} else {
			regionCostMap = regionCostDictionary.get("asia");
		}
		BigDecimal cost = new BigDecimal(0);
		BigDecimal hours = new BigDecimal(requiredHours);
		for (Map.Entry<String,Integer> mapElement : regionMap.entrySet()) { 
			BigDecimal serverCost = new BigDecimal(regionCostMap.get(mapElement.getKey()));
			cost = cost.add(serverCost.multiply(new BigDecimal((Integer) mapElement.getValue())).multiply(hours));
		}
		return cost;
	}
}
