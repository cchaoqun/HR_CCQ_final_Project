package com.CIT594.project594.test;

import com.CIT594.project594.Main;
import com.CIT594.project594.covid.CovidJsonParser;
import com.CIT594.project594.population.PopulationParser;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Chaoqun Cheng
 * @date 2021-07-2021/7/30-18:11
 */

public class CIT594ProjectTest {
    Main main = new Main();
    String covid = "covid_data.json";
    String property = "properties.csv";
    String population = "population.txt";
    String log = "trivial_log.log";
    String[] args1 = {covid, property, population, log};

    public static void main(String[] args) {
        String covid = "covid_data.csv";
        String property = "properties.csv";
        String population = "population.txt";
        String log = "trivial_log.log";
        String[] args1 = {covid, property, population, log};
        Main.main(args1);
    }

    /**
     * check for file validation
     * passed
     */
    @Test
    public void testFileValid(){
        main.main(args1);
    }

    /**
     * test for calculate total population
     */
    @Test
    public void testTotalPop(){
        PopulationParser pop = PopulationParser.getPopulationParser();
        Long totalPop = pop.parsePop(population);
        System.out.println("Total population is :"+totalPop);
    }

    /**
     *
     */
    @Test
    public void testDoubleFormat(){
        double d = 0.1;
        int d1 = (int)(d*10000);
        double res = d1*1.0/10000;
        DecimalFormat df = new DecimalFormat("0.0000");
        System.out.println(df.format(res));
    }

    /**
     *
     */
    @Test
    public void testJson(){
        CovidJsonParser covidJsonParser =new CovidJsonParser("covid_data.json");
        covidJsonParser.parseFile();

    }

    @Test
    public void testCovid(){
//        Covid covid = new Covid();
//        covid.pop = 10000l;
//        covid.partial = 1l;
//        covid.full = 2l;
//        System.out.println(covid.getVacciPerCap(0));
//        System.out.println(covid.getVacciPerCap(1));

    }

    @Test
    public void testCsv(){
//        String str = "\"2021-03-29 17:20:02\",19144,42598,3146,151,523,5591,3155";
        String str = "\"2021-03-27 17:20:02\",19194,24,,,,,";
        str = str.trim();
        int len = str.length();
        String reg = "[,]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        int count = 1;
        int[] pos = new int[9];
        pos[8] = str.length();
        while(m.find()){
            pos[count++] = m.start();
            System.out.println(count);
            System.out.println(m.start());
            System.out.println(str.substring(m.start(), m.end()));
            System.out.println(m.end());
        }
        System.out.println(Arrays.toString(pos));
        for(int i=pos.length-1; i>=pos.length-3; i--){
            System.out.println(str.substring(pos[i-1]+1, pos[i]).equals(""));
        }

    }

}
