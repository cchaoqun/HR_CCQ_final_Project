package com.CIT594.project594.covid;

import com.CIT594.project594.util.ParserUtils;
import com.CIT594.project594.wrapper.Covid;
import com.CIT594.project594.Main;
import com.CIT594.project594.wrapper.Vaccination;

import java.util.TreeMap;

/**
 * @author Chaoqun Cheng
 * @date 2021-07-2021/7/30-21:46
 */

public class CovidProcessor extends Main {
    /**
     * TreeMap that saving all the zip's fully and partial vaccination info
     */
    public static TreeMap<Long, Vaccination> vacciPerCap;

    /**
     * parser for parsing covid data
     */
    protected static CovidParser covidParser;

    public CovidProcessor(){}

    /**
     * constructor for initialize map, create parser for corresponding file format, parsing file
     * @param filename
     */
    public CovidProcessor(String filename){
        // create treemap for saving zip-Vaccination pair
        vacciPerCap = new TreeMap<>();
        // create parser for this filename
        createParser(filename);
        // parse file
        parseFile();
        //log
        log.log(filename);
    }

    /**
     * create parser based on filename
     * @param filename
     */
    public void createParser(String filename){
        // whether it is json or csv file is determined in main func
        // isCSV and isJson determined already
        if(isCSV){
            covidParser = new CovidCSVParser(filename);
        }else if(isJson) {
            covidParser = new CovidJsonParser(filename);
        }else{
            throw new RuntimeException("Invalid covid data filename");
        }
    }

    /**
     * parse file
     */
    public void parseFile(){
        covidParser.parseFile();
    }

    /**
     * update covidData map update partial and fully vaccinated people info
     */
    public void updateCovid(){
        for(Long zip : vacciPerCap.keySet()){
            // create Covid instance if not exists
            covidData.putIfAbsent(zip, new Covid());
            // get Covid for this zip
            Covid curCovid = covidData.get(zip);
            // update partial and full
            curCovid.setPartial(vacciPerCap.get(zip).getPartial());
            curCovid.setFull(vacciPerCap.get(zip).getFully());
        }
    }

    /**
     * getter for map vacciPerCap
     * @return
     */
    public static TreeMap<Long, Vaccination> getVacciPerCap() {
        return vacciPerCap;
    }


}

























