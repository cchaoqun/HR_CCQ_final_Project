package com.CIT594.project594.covid;

import com.CIT594.project594.wrapper.Vaccination;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;

/**
 * @author Chaoqun Cheng
 * @date 2021-07-2021/7/30-22:25
 */

public class CovidJsonParser extends CovidProcessor implements CovidParser{
    /**
     * filename that this json parser will parse
     */
    private String filename;

    /**
     * constructors
     */
    public CovidJsonParser(){
    }

    /**
     * constructor for passing the filename to be passed
     * @param _filename
     */
    public CovidJsonParser(String _filename) {
        this.filename = "covid_data.json";
    }

    /**
     * parse file
     */
    @Override
    public void parseFile() {
        // json file parser
        JSONParser parser = new JSONParser();
        //fileReader read the json file
        FileReader fr = null;
        try{
            //get the array from json file
            fr = new FileReader(filename);
            Object obj = parser.parse(fr);
            // convert to JSONArray
            JSONArray arr = (JSONArray)obj;
            // iterate the array to get each json object
            for(Object cur : arr){
                JSONObject curJson = (JSONObject)cur;
                // parse time zip partial full
                String time = (String) curJson.get(TIME);
                Long zip = (Long)curJson.get(ZIP);
                Long partial = (Long) curJson.get(PARTIAL);
                Long full = (Long) curJson.get(FULL);
                // update the vaccination object associate with zip add the partial and full to vaccination
                updateParFull(zip, partial, full);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                if(fr != null)
                    fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

}
