package com.CIT594.project594.covid;

import com.CIT594.project594.wrapper.Vaccination;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Chaoqun Cheng
 * @date 2021-07-2021/7/30-22:25
 */

public class CovidCSVParser extends CovidProcessor implements CovidParser{
    /**
     * filename to be parse
     */
    private String filename;

    public CovidCSVParser(){}

    /**
     * constructor initialize the filename to be parsed
     * @param _filename
     */
    public CovidCSVParser(String _filename){
        this.filename = _filename;
    }

    /**
     * parse file
     */
    @Override
    public void parseFile() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(filename)));
            String data;
            // ignore first line
            br.readLine();
            // read line by line
            while((data = br.readLine()) != null){
                data = data.trim();
                parseParFull(data);
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
    }

    /**
     * parse out zip partial vaccinate and fully vaccinate number people
     * zip is between the first and second comma
     * partial is between the second last comma and last comma
     * full is between last comma and end of string
     * @param line
     */
    public void parseParFull(String line){
        // parse out the index of all the comma
        List<Integer> commaPos = getCommaPos(line);
        int size = commaPos.size();
        //zip is between the first and second comma
        String zip = line.substring(commaPos.get(1)+1, commaPos.get(2));
        //partial is between the second last comma and last comma
        String full = line.substring(commaPos.get(size-2)+1, commaPos.get(size-1));
        //full is between last comma and end of string
        String partial = line.substring(commaPos.get(size-3)+1, commaPos.get(size-2));
        // try to convert to Long
        Long lZip = tryCastStrToLong(zip);
        Long lPartial = tryCastStrToLong(partial);
        Long lFull = tryCastStrToLong(full);
        // save in the vacciPerCap map in the father class
        updateParFull(lZip, lPartial, lFull);
    }

    /**
     * parse out all the comma's index in the string line return as a list of indexes
     * @param line
     * @return
     */
    public List<Integer> getCommaPos(String line){
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
     * update both partial and full vaccinated people for corresponding zip
     * @param zip
     * @param partial
     * @param full
     */
    public void updateParFull(Long zip, Long partial, Long full){
        if(zip==null){
            return;
        }
        vacciPerCap.putIfAbsent(zip, new Vaccination(0l,0l));
        // try to cast those two string format number into integer
        Long parInt = partial==null?0l:partial;
        Long fullInt = full==null?0l:full;
        // update two number to the vaccination object associate with zip code
        vacciPerCap.get(zip).updateParFull(parInt, fullInt);
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
