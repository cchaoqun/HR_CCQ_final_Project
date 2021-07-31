package com.CIT594.project594;


import com.CIT594.project594.covid.CovidProcessor;
import com.CIT594.project594.population.PopulationParser;
import com.CIT594.project594.wrapper.Covid;

import java.io.File;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Chaoqun Cheng
 * @date 2021-07-2021/7/30-17:37
 */

public class Main {
    public static  String COVID_DATA ;
    public static  String PROPERTY_VALUES;
    public static  String POPULATION;
    public static  String LOG;
    public static final String jsonReg = ".json$";
    public static final String txtReg = ".txt$";
    public static final String csvReg = ".csv$";
    public static boolean isJson;
    public static boolean isCSV;
    public static final String PARTIAL = "partially_vaccinated";
    public static final String FULL = "fully_vaccinated";
    public static final String TIME = "etl_timestamp";
    public static final String ZIP = "zip_code";
    public static final String CHOOSE_PARTIAL = "partial";
    public static final String CHOOSE_FULL = "full";
    public static CovidProcessor covidProcessor;
    public static PopulationParser populationParser;
    public static TreeMap<Long, Covid> covidData = new TreeMap<>();

    public static void main(String[] args) {
        // validation check of passed in file arguments
        checkFileValidation(args);
        // initialize the parser for calculating population, vaccination
        initParser();
        int userInput = -1;
        while(userInput!=0){
            userInput = Integer.parseInt(getUserPrompt());
            System.out.println("BEGIN OUTPUT");
            switch (userInput){
                case 0:
                    System.out.println("exit");
                    break;
                case 1:
                    System.out.println(populationParser.getTotalPopulation());
                    break;
                case 2:
//                    System.out.println(COVID_DATA);
                    // update pop partial and full into covidData
                    calTotalVacciPerCap();
                    // get user choice
                    int choice = getUserChoose();
                    // show partial or full according to choice
                    showVacciPerCap(choice);
                    break;
                case 3:
                    System.out.println("3");
                    break;
                case 4:
                    System.out.println("4");
                    break;
                case 5:
                    System.out.println("5");
                    break;
                case 6:
                    System.out.println("6");
                    break;
            }
            System.out.println("END OUTPUT");
        }

    }
    /**
     * get user input for choice of 0-6
     */
    public static String getUserPrompt(){
        Scanner input = new Scanner(System.in);
        String invalid = "Invalid input integer, please enter an valid input (0-6)";
        String get = "Please enter an valid integer from 0 to 6";
        String userInput = "";
        System.out.println(get);
        System.out.print(">");
        System.out.flush();
        while(true){
            userInput = input.next();
            if(!validInput(userInput)){
                System.out.println(invalid);
                System.out.print(">");
                System.out.flush();
            }else{
                break;
            }
        }
        return userInput;
    }

    /**
     * check whether the input is an integer that range from 0-6
     * @param str
     * @return
     */
    public static boolean validInput(String str){
        if(str==null || str.length()!=1){
            return false;
        }
        char cur = str.charAt(0);
        return 0<= cur-'0' && cur-'0'<=6;
    }

    /**
     * get user input for choice of partial || full
     * return 0 for partial and 1 for full
     */
    public static int getUserChoose(){
        Scanner input = new Scanner(System.in);
        String invalid = "Invalid input please enter partial or full";
        String get = "Please enter partial or full";
        String userInput = "";
        System.out.println(get);
        System.out.print(">");
        System.out.flush();
        int choice = -1;
        while(true){
            userInput = input.next();
            choice = validChoice(userInput);
            if(choice==-1){
                System.out.println(invalid);
                System.out.print(">");
                System.out.flush();
            }else{
                break;
            }
        }
        return choice;
    }

    /**
     * user prompt for choose to see partial or full vaccination
     * @param str
     * @return
     */
    public static int validChoice(String str){
        if(str==null){
            return -1;
        }
        if(str.equals(CHOOSE_PARTIAL)){
            return 0;
        }
        if(str.equals(CHOOSE_FULL)){
            return 1;
        }
        return -1;
    }



    //===============Problem2===============
    /**
     * create parser for population and covid
     *  let the
     */
    public static void initParser(){
        // create parser for population file
        populationParser = PopulationParser.getPopulationParser();
        // parse total population
        populationParser.parsePop(POPULATION);
        // create parser for covid file
        covidProcessor = new CovidProcessor(COVID_DATA);
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
        File log = new File(LOG);
        if(!covid.exists() || !covid.canRead()){
            throw new RuntimeException(COVID_DATA+" is not found or can not read");
        }
        if(!property.exists() || !property.canRead()){
            throw new RuntimeException(PROPERTY_VALUES+" is not found or can not read");
        }
        if(!population.exists() || !population.canRead()){
            throw new RuntimeException(POPULATION+" is not found or can not read");
        }
        if(!log.exists() || !log.canRead()){
            throw new RuntimeException(LOG+" is not found or can not read");
        }
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
}
