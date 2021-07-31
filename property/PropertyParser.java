package com.CIT594.project594.property;

import com.CIT594.project594.Main;
import com.CIT594.project594.util.ParserUtils;
import com.CIT594.project594.wrapper.Property;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Chaoqun Cheng
 * @date 2021-07-2021/7/31-17:27
 */

public class PropertyParser extends Main {
    private static final int MARKET_VALUE_POS = 35;
    private static final int TOTAL_LIVEABLE_AREA_POS = 13;
    private static final int ZIP_CODE_POS = 5;
    private String filename;
    /**
     * key: zip code
     * Property: contains zip, total market value in this zip, total liveable area of all houses in this zip code
     */
    private static TreeMap<Long, Property> propertyInfo;

    public PropertyParser(){}

    public PropertyParser(String _filename){
        filename = _filename;
        propertyInfo = new TreeMap<>();
        parseFile();
        //log
        log.log(filename);
    }

    /**
     * parse file
     */
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
                // parse and update this line's info
                parseField(data);
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
     * parse out the zip market value and total liveable area field from the line
     *          zip field is in the last 5 field
     *          total liveable area is in the last 13 field
     *          market value is in the first 35 field,
     *          parse those field based on the number of column of each line
     * @param line
     */
    private void parseField(String line){
        List<Integer> commaPos = ParserUtils.getCommaPos(line);
        int size = commaPos.size();
        // parse field
        String zip = line.substring(commaPos.get(size-ZIP_CODE_POS-1)+1, commaPos.get(size-ZIP_CODE_POS));
        // if zip is not valid return
        zip = zip.trim();
        if(zip==null || zip.length()<5){
            return;
        }
        // only keep the first 5 digits
        zip = zip.substring(0,5);
        String marketVal = line.substring(commaPos.get(MARKET_VALUE_POS-1)+1, commaPos.get(MARKET_VALUE_POS));
        String liveableArea = line.substring(commaPos.get(size-TOTAL_LIVEABLE_AREA_POS-1)+1, commaPos.get(size-TOTAL_LIVEABLE_AREA_POS));
        // cast those value to Long Double
        Long lZip = ParserUtils.tryCastStrToLong(zip);
        Double DMarketVal = ParserUtils.tryCastStrToDouble(marketVal);
        Double DLiveableArea = ParserUtils.tryCastStrToDouble(liveableArea);
        // update those field into the map
        updatePropertyInfo(lZip,DMarketVal, DLiveableArea);
    }

    /**
     * add corresponding zip market value totalliveablearea into map
     * @param zip
     * @param market
     * @param area
     */
    private void updatePropertyInfo(Long zip, Double market, Double area){
        if(zip==null || zip.equals(0l)){
            return;
        }
        propertyInfo.putIfAbsent(zip, new Property());
        Property curPro = propertyInfo.get(zip);
        // increase the number of home in this zip
        curPro.incrPropertyNum();
        curPro.setZip(zip);
        // add up the total liveable area and market value
        curPro.setMarketValue(curPro.getMarketValue()+market);
        curPro.setTotalLiveableArea((curPro.getTotalLiveableArea()+area));
    }

    /**
     * Show specific zip's average market value
     * @param zip
     */
    public void showOneZipMarket(String zip){
        Long lZip = ParserUtils.tryCastStrToLong(zip);
        if(lZip.equals(0l) || !propertyInfo.containsKey(lZip)){
            System.out.println(0);
        }else{
            Property pro = propertyInfo.get(lZip);
            System.out.println(lZip+" "+pro.calAvgMarket());
        }
    }

    /**
     * Show specific zip's average area value
     * @param zipChooseArea
     */
    public void showOneZipArea(String zipChooseArea) {
        Long lZip = ParserUtils.tryCastStrToLong(zipChooseArea);
        if(lZip.equals(0l) || !propertyInfo.containsKey(lZip)){
            System.out.println(0);
        }else{
            Property pro = propertyInfo.get(lZip);
            System.out.println(lZip+" "+pro.calAvgTotalLiveArea());
        }
    }

    /**
     * show average market value of each zip, total market value / number of homes
     */
    public void showMarketPerHome(){
        for(Map.Entry<Long, Property> entry : propertyInfo.entrySet()){
            Long zip = entry.getKey();
            Property curPro = entry.getValue();
            System.out.println(zip+" "+ curPro.calAvgMarket());
        }
    }

    /**
     * get total market value of this zip
     * @return
     */
    public Integer getZipMarketPerCap(String zip, Long population){
        Long lZip = ParserUtils.tryCastStrToLong(zip);
        // if zip invalid, did not contains zip, or population is 0 return 0
        if(lZip.equals(0l) || !propertyInfo.containsKey(lZip) || population.equals(0l)){
            return 0;
        }
        Double res = propertyInfo.get(lZip).getMarketValue()/population;
        Integer i = new Double(res).intValue();
        return i;
    }


    public TreeMap<Long, Property> getPropertyInfo() {
        return propertyInfo;
    }


}
