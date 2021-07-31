package com.CIT594.project594;


import com.CIT594.project594.covid.CovidProcessor;
import com.CIT594.project594.log.Logger;
import com.CIT594.project594.population.PopulationParser;
import com.CIT594.project594.property.PropertyParser;
import com.CIT594.project594.util.ParserUtils;
import com.CIT594.project594.wrapper.Covid;
import com.CIT594.project594.wrapper.Special;

import java.io.File;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Chaoqun Cheng
 * @date 2021-07-2021/7/30-17:37
 */

public class Main {
    /**
     * 4 filename passed from the argas
     */
    public static String COVID_DATA ;
    public static String PROPERTY_VALUES;
    public static String POPULATION;
    public static String LOG;
    /**
     * regular expression for checking file format
     */
    public static final String jsonReg = ".json$";
    public static final String txtReg = ".txt$";
    public static final String csvReg = ".csv$";
    /**
     * based on passed in file format decide which covid file format is
     */
    public static boolean isJson;
    public static boolean isCSV;
    /**
     * some header for covid and property file, extracted out for convenience
     */
    public static final String PARTIAL = "partially_vaccinated";
    public static final String FULL = "fully_vaccinated";
    public static final String TIME = "etl_timestamp";
    public static final String ZIP = "zip_code";
    public static final String MARKET_VALUE = "market_value";
    public static final String TOTAL_LIVEABEL_AREA = "total_livable_area";

    /**
     * parser instance for different file
     */
    public static CovidProcessor covidProcessor;
    public static PopulationParser populationParser;
    public static PropertyParser propertyParser;
    public static Special special;
    public static Logger log;
    /**
     * integrated data including both population and partial and full vaccination
     */
    public static TreeMap<Long, Covid> covidData = new TreeMap<>();

    public static void main(String[] args) {
        // validation check of passed in file arguments
        checkFileValidation(args);
        // initialize the parser for calculating population, vaccination
        initParser();
        int userInput = -1;
        while(userInput!=0){
            userInput = Integer.parseInt(ParserUtils.getUserInput());
            System.out.println("BEGIN OUTPUT");
            switch (userInput){
                case 0:
                    System.out.println("exit");
                    //log
                    log.log(0);
                    break;
                case 1:
                    // get total population
                    System.out.println(populationParser.getTotalPopulation());
                    //log
                    log.log(1);
                    break;
                case 2:
                    //log
                    log.log(2);
                    // update pop partial and full into covidData
                    calTotalVacciPerCap();
                    // get user choice
                    int choice = ParserUtils.getUserChoose();
                    // show partial or full according to choice
                    showVacciPerCap(choice);
                    break;
                case 3:
                    // get zip
                    String zipChooseMarket = ParserUtils.getUserZip();
                    //log
                    log.log(zipChooseMarket);
                    // show avg market
                    propertyParser.showOneZipMarket(zipChooseMarket);
                    break;
                case 4:
                    // get zip
                    String zipChooseArea = ParserUtils.getUserZip();
                    //log
                    log.log(zipChooseArea);
                    // show avg area
                    propertyParser.showOneZipArea(zipChooseArea);
                    break;
                case 5:
                    // get user input zip
                    String zipChooseMarketPerCap = ParserUtils.getUserZip();
                    //log
                    log.log(zipChooseMarketPerCap);
                    // get corresponding population of this zip
                    Long pop = populationParser.getPopPerZip(zipChooseMarketPerCap);
                    // get the average market value per people
                    int marketPerCap = propertyParser.getZipMarketPerCap(zipChooseMarketPerCap, pop);
                    System.out.println(marketPerCap);
                    break;
                case 6:
                    // one zip's average fully vaccinate ratio compared to average property value
                    calTotalVacciPerCap();
                    // get user choice
                    int choiceSpecial = ParserUtils.getUserChoose();
                    special.getNoneZeroVacc(choiceSpecial);
                    break;
            }
            System.out.println("END OUTPUT");
        }

    }

    //===============Problem2===============
    /**
     * create parser for population and covid
     *  let the
     */
    public static void initParser(){
        log = Logger.getInstance(LOG);
        //log
        log.log(COVID_DATA+" "+PROPERTY_VALUES+" "+POPULATION+" "+LOG);
        // create parser for population file
        populationParser = PopulationParser.getPopulationParser();
        // parse total population
        populationParser.parsePop(POPULATION);
        // create parser for covid file
        covidProcessor = new CovidProcessor(COVID_DATA);
        // create property parser for property.csv
        propertyParser = new PropertyParser(PROPERTY_VALUES);
        special = new Special();
    }

    /**
     * update population of each zip in the corresponding Covid instance in the map covidData
     *      populationParser has map that contain each zip's population
     *          will update this population info into corresponding's CovidData
     *      covidParser has map that contain each zip's partial and fully vaccinated number of people
     *          will update this partial and fully vaccinated number of people into corresponding's CovidData
     */
    public static void calTotalVacciPerCap(){
        // update population of each zip
        populationParser.updateCovid();
        // update partial and fully vaccinated people of each zip
        covidProcessor.updateCovid();

    }

    /**
     * print out the vaccination percent of each zip in the format of <zip><whitespace><percent(partial||full)>
     */
    public static void showVacciPerCap(int choice){
        // Iterate through the covidData of each zip
        for(Long zip : covidData.keySet()){
            // calculate the percent of partial and fully vaccination
            // pass 1 represent fully and 0 for partial
            String percent = covidData.get(zip).getVacciPerCap(choice);
            System.out.println(zip.toString()+" "+percent);
        }
    }

    //===========================Checking arguments=========================
    /**
     * Check file's number,
     * initialize the filename variable
     * check covid data validation
     * check all files exists and can open for read
     * @param args
     * @return
     */
    public static boolean checkFileValidation(String[] args){
        // check number of arguments == 4
        checkArgsNumber(args);
        // init filename
        initFilename(args);
        //check format of covid_data file must be either .csv or .json
        checkCovidFileFormat(COVID_DATA);
        // check existence of file and permission
        checkFileExistPerm(args);
        return true;
    }



    /**
     * Check whether those files are all exists and can open for read
     * @param args
     * @return
     */
    public static boolean checkFileExistPerm(String[] args){
        File covid = new File(COVID_DATA);
        File property = new File(PROPERTY_VALUES);
        File population = new File(POPULATION);
//        File log = new File(LOG);
        if(!covid.exists() || !covid.canRead()){
            throw new RuntimeException(COVID_DATA+" is not found or can not read");
        }
        if(!property.exists() || !property.canRead()){
            throw new RuntimeException(PROPERTY_VALUES+" is not found or can not read");
        }
        if(!population.exists() || !population.canRead()){
            throw new RuntimeException(POPULATION+" is not found or can not read");
        }
//        if(!log.exists() || !log.canRead()){
//            throw new RuntimeException(LOG+" is not found or can not read");
//        }
        return true;
    }

    /**
     * Check if the number of runtime arguments is correct
     * @param args
     * @return true when args.length==4 otherwise throw exception
     */
    public static boolean checkArgsNumber(String[] args){
        //1. number of runtime args is wrong
        if(args==null || args.length!=4){
            throw new RuntimeException("Wrong number of args");
        }
        return true;
    }

    /**
     * Initialize the filename
     * @param args
     */
    public static void initFilename(String[] args){
        //initialize the file name
        COVID_DATA = args[0];
        PROPERTY_VALUES = args[1];
        POPULATION = args[2];
        LOG = args[3];
    }


    /**
     * Determine which kind of covid data file csv / json
     * @param filename
     * @return true when filename is either .csv or .json
     */
    public static boolean checkCovidFileFormat(String filename){
        // parse json file
        Pattern jsonP = Pattern.compile(jsonReg, Pattern.CASE_INSENSITIVE);
        Matcher jsonM = jsonP.matcher(filename);
        // parse csv file
        Pattern csvP = Pattern.compile(csvReg, Pattern.CASE_INSENSITIVE);
        Matcher csvM = csvP.matcher(filename);
        // determine which file format
        if(jsonM.find()){
            isJson = true;
        }else if(csvM.find()){
            isCSV = true;
        }else{
            throw new RuntimeException("Covid data file is not json or csv format:"+COVID_DATA);
        }
        return true;
    }

    public static TreeMap<Long, Covid> getCovidData() {
        return covidData;
    }
}
