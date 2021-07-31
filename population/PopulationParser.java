package com.CIT594.project594.population;

import com.CIT594.project594.wrapper.Covid;
import com.CIT594.project594.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

/**
 * @author Chaoqun Cheng
 * @date 2021-07-2021/7/30-21:05
 */

public class PopulationParser extends Main {
    /**
     * save info about population per zip
     */
    private static TreeMap<Long, Long> PopPerZip ;

    /**
     * Singleton instance of this class
     */
    private volatile static PopulationParser popParser = null;

    /**
     * total population of all the zip
     */
    private long totalPopulation;

    private PopulationParser(){}

    /**
     * DLC check for getting singleton instance of this class
     * @return
     */
    public static PopulationParser getPopulationParser(){
        // if the instance is not already instantiated
        if(popParser==null){
            // synchronized for multithread, use class as lock monitor
            synchronized (PopulationParser.class){
                // double check
                if(popParser==null){
                    // create the instance
                    popParser = new PopulationParser();
                    PopPerZip = new TreeMap<>();
                }
            }
        }
        return popParser;
    }

    /**
     * getter for this map
     * @return
     */
    public static TreeMap<Long, Long> getPopPerZip() {
        return PopPerZip;
    }

    /**
     * getter for total population
     * @return
     */
    public long getTotalPopulation() {
        return totalPopulation;
    }

    /**
     * setter for total population
     * @param totalPopulation
     */
    public void setTotalPopulation(long totalPopulation) {
        this.totalPopulation = totalPopulation;
    }

    /**
     * read all lines from population.txt
     * get the total population
     * @param filename
     * @return total population
     */
    public Long parsePop(String filename){
        Long totalPop = 0l;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(filename)));
            String data;
            // read line by line
            while((data = br.readLine()) != null){
                // parse out the number and add to the sum
                totalPop += getPop(data);
                // update map for zip-population
                updateMap(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        setTotalPopulation(totalPop);
        return totalPop;
    }

    /**
     * update covidData map, add population into each zip's Covid instance
     */
    public void updateCovid(){
        for(Long zip : PopPerZip.keySet()){
            //create zip-Covid pair if not created before
            covidData.putIfAbsent(zip, new Covid());
            //get the Covid obj corresponding to the zip
            Covid curCovid =covidData.get(zip);
            //update the population info
            curCovid.setPop(PopPerZip.get(zip));
            curCovid.setZip(zip);
        }
    }

    /**
     * split each line into zipcode and number of pop
     * put them into the map, this map is for this class containing zip-population pair
     * @param line
     */
    public void updateMap(String line){
        // get zip
        Long zip = getZip(line);
        // get population
        Long pop = getPop(line);
        // make sure zip code is valid population!=0
        if(zip!=null && !zip.equals(0l) && !pop.equals(0l)){
            PopPerZip.putIfAbsent(zip, pop);
        }
    }

    /**
     * parse the one line from the population.txt
     * take out the population of this ZIP code
     * take condition that second string can not be converted to number just return 0
     * @param line
     * @return
     */
    private Long getPop(String line){
        String popStr = line.trim().split(" ")[1].trim();
        return tryCastStrToLong(popStr);
    }

    /**
     * parse out the zip number of each line
     * @param line
     * @return
     */
    private Long getZip(String line){
        String popStr = line.trim().split(" ")[0].trim();
        return tryCastStrToLong(popStr);
    }

    /**
     * try to cast this str to Long, return 0l if failed
     * @param str
     * @return
     */
    private Long tryCastStrToLong(String str){
        Long res = 0l;
        try{
            //ignore cases that can not be converted to Long
            res = Long.parseLong(str);
        }catch (Exception e){
            // do nothing just let res = 0
        }
        return res;
    }
}
