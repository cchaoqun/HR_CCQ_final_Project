package com.CIT594.project594.wrapper;

import com.CIT594.project594.Main;
import com.CIT594.project594.util.ParserUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Chaoqun Cheng
 * @date 2021-07-2021/7/31-19:53
 */

public class Special extends Main {

    public Special() {
    }

    /**
     * get all the none zero vaccination ration according to choice
     *  choice =0 partial =1 full
     *  find all the corresponding market value per home
     * @param choice
     */
    public void getNoneZeroVacc(int choice){
        // get all fully vaccination rate's zip ignore zero
        List<Long> allFullZip = new ArrayList<>();
        for(Map.Entry<Long, Covid> entry : covidData.entrySet()){
            entry.getValue().getVacciPerCap(choice);
            // calculate all how many zip has not zero  vacc
            if(!entry.getValue().getVacciPerCap(choice).equals("0.0000")){
                allFullZip.add(entry.getKey());
            }
        }
        String ratio = choice==0?"partio":"fully";
        System.out.println("This is all the "+ratio+" vaccination ratio and corresponding average market value per home");
        getPropertyValue(allFullZip, ratio, choice);
        return ;
    }

    public void getPropertyValue(List<Long> zips, String ratio, int choice){
        for(Long zip : zips){
            Integer avgMarketValue = propertyParser.getPropertyInfo().get(zip).calAvgMarket();
            System.out.println("zip:"+zip
                    +ratio+" vaccination ratio "+ covidData.get(zip).getVacciPerCap(choice)
                    +" average market value per home:"+avgMarketValue);
        }
    }

}


