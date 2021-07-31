package com.CIT594.project594.util;

import com.CIT594.project594.Main;
import com.CIT594.project594.property.PropertyParser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Chaoqun Cheng
 * @date 2021-07-2021/7/31-17:43
 */

/**
 * Utils Class for parsing file
 */
public class ParserUtils {
    public static final String CHOOSE_PARTIAL = "partial";
    public static final String CHOOSE_FULL = "full";
    public static final String DIGIT_4 = "0.0000";
    public static final int NUM_DIGIT = 10000;
    /**
     *  file formater
     */
    private static DecimalFormat df = new DecimalFormat(DIGIT_4);


    /**
     * parse out all the comma's index in the string line return as a list of indexes
     * @param line
     * @return
     */
    public static List<Integer> getCommaPos(String line){
        line = line.trim();
        String reg = "[,]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(line);
        List<Integer> commaPos = new ArrayList<>();
        // 0 represent the start of line
        commaPos.add(0);
        while(m.find()){
            //add the comma's index into list
            commaPos.add(m.start());
        }
        commaPos.add(line.length());
        // end of the line
        return commaPos;
    }

    /**
     * try to cast this str to Long, return 0l if failed
     * @param str
     * @return
     */
    public static Long tryCastStrToLong(String str){
        Long res = 0l;
        try{
            //ignore cases that can not be converted to Long
            res = Long.parseLong(str.trim());
        }catch (Exception e){
            // do nothing just let res = 0l
        }
        return res;
    }

    /**
     * try to cast this str to Double, return 0l if failed
     * @param str
     * @return
     */
    public static Double tryCastStrToDouble(String str){
        Double res = 0.0;
        try{
            //ignore cases that can not be converted to Double
            res = Double.parseDouble(str);
        }catch (Exception e){
            // do nothing just let res = 0.0
        }
        return res;
    }

    /**
     * try to cast the str to integer, if failed return 0
     * @param str
     * @return
     */
    public static int castInt(String str){
        int res = 0;
        try{
            res = Integer.parseInt(str);
        }catch (Exception e){
        }
        return res;
    }

    public static String truncateDot(String str){
        int len = str.length();
        int index = 0;
        while(str.charAt(index)!='.'){
            index++;
        }
        return str.substring(0,index);
    }

    /**
     * format the double input truncated at 4 digits padding with 0 if less than 4 digits
     * @param input
     * @return
     */
    public static String formatDouble(double input){
        int temp = (int)(input*NUM_DIGIT);
        input = temp*1.0/NUM_DIGIT;
        return df.format(input);
    }

    /**
     * get user input for choice of 0-6
     */
    public static String getUserInput(){
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

    /**
     * get user input for choice of zip code's average market value
     *
     */
    public static String getUserZip(){
        Scanner input = new Scanner(System.in);
        String get = "Please enter zip code ";
        String userInput = "";
        System.out.println(get);
        System.out.print(">");
        System.out.flush();
        userInput = input.next();
        return userInput;
    }

    /**
     * get current time
     * @return
     */
    public static String getCurrentTime(){
        return System.currentTimeMillis()+"";
    }

}
