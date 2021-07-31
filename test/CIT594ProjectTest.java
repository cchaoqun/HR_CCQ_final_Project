package com.CIT594.project594.test;

import com.CIT594.project594.Main;
import com.CIT594.project594.covid.CovidJsonParser;
import com.CIT594.project594.log.Logger;
import com.CIT594.project594.population.PopulationParser;
import com.CIT594.project594.property.PropertyParser;
import com.CIT594.project594.util.ParserUtils;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Chaoqun Cheng
 * @date 2021-07-2021/7/30-18:11
 */

public class CIT594ProjectTest {
    Main main = new Main();
    static String covid = "covid_data.json";
    static String property = "properties.csv";
    static String population = "population.txt";
    static String log = "finalProjectLog.txt";
    static String[] args1 = {covid, property, population, log};

    public static void main(String[] args) {
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

    @Test
    public void testProperty(){
        String header = "book_and_page\tbuilding_code\tbuilding_code_description\tcategory_code\tcategory_code_description\tcensus_tract\tcentral_air\tcross_reference\tdate_exterior_condition\tdepth\texempt_building\texempt_land\texterior_condition\tfireplaces\tfrontage\tfuel\tgarage_spaces\tgarage_type\tgeneral_construction\tgeographic_ward\thomestead_exemption\thouse_extension\thouse_number\tinterior_condition\tlocation\tmailing_address_1\tmailing_address_2\tmailing_care_of\tmailing_city_state\tmailing_street\tmailing_zip\tmarket_value\n";
        int market = 35;
        String header2 = "total_livable_area\ttype_heater\tunfinished\tunit\tutility\tview_type\tyear_built\tyear_built_estimate\tzip_code\tzoning\tobjectid\tlat\tlng\n";
        //倒数第13个
        int area = 13;
        int zip = 5;
        String data = "0\t\t32'6\" S TASKER ST        \t602048\tU50  \tROW CONV/APT 3 STY MASON\t2\tMulti Family\t750\tN\t\t\t65.17\t0\t0\t4\t0\t16\t\t0\t0\tA\t1\t0\t0\t1604\t4\t1604 S 4TH ST\tC/O CHRISTOPHER CATALAW\t\tKAREN  CATALANO\tSEWELL NJ\t100 GOLFVIEW DR\t08080-1836\t264800\n";


        String line = "0,,\"32'6\"\" S TASKER ST        \",0602048,U50  ,ROW CONV/APT 3 STY MASON,2,Multi Family,750,N,,,65.17,0.0,0.0,4,0,16.0,,0,0,A,01,0,00,01604,4,1604 S 4TH ST,C/O CHRISTOPHER CATALAW,,KAREN  CATALANO,SEWELL NJ,100 GOLFVIEW DR,08080-1836,264800.0,,2,4,D,3,0,,CATALANO KAREN           ,,011429900,E,,1986-10-15,012S090302     ,1986-01-08,22500.0,,,A,1002,87880,ST ,S,04TH,,205750.0,59050.0,F,1042.72,1800.0,H,,,,I,1900,Y,191481303,RM1  ,529788186,39.9281937976753,-75.1523680250966";
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
        int size = commaPos.size();
        System.out.println("zip:"+ line.substring(commaPos.get(size-5-1)+1, commaPos.get(size-5)));
        System.out.println("live_area:"+ line.substring(commaPos.get(size-13-1)+1, commaPos.get(size-13)));
        System.out.println("market_value:"+ line.substring(commaPos.get(35-1)+1, commaPos.get(35)));

    }

    @Test
    public void testTruncat(){
        String cur = "1500.9";
        System.out.println(ParserUtils.truncateDot(cur));
    }



    @Test
    public void testMarket(){
        PropertyParser p = new PropertyParser(property);
        p.parseFile();

    }

    @Test
    public void testForm(){
        double a = 5000.44;
        double b = 100.12;
        double v = a / b;
        int i = new Double(v).intValue();
        System.out.println(i);
        System.out.println(v);

    }

    @Test
    public void test(){
        String str = "0.000";
        System.out.println(str.compareTo("0"));
    }

    @Test
    public void testLog(){
        String file = "projectLog.txt";
        Logger log = Logger.getInstance(file);
        log.log(1231+"\n");
    }

}
