package com.CIT594.project594.wrapper;


/**
 * @author Chaoqun Cheng
 * @date 2021-07-2021/7/31-17:36
 */

public class Property {
    private Long zip;
    private Double marketValue;
    private Double totalLiveableArea;
    private int propertyNum;
    public Property(){
        zip = 0l;
        marketValue = 0.0;
        totalLiveableArea = 0.0;
        propertyNum = 0;

    }
    public Property(Long _zip, Double _marketValue, Double _totalLiveableArea){
        zip = _zip;
        marketValue = _marketValue;
        totalLiveableArea = _totalLiveableArea;
    }

    /**
     * calculate the average market value
     *  total market value / number of homes
     * @return
     */
    public Integer calAvgMarket(){
        if(propertyNum==0){
            return 0;
        }
        Double res = marketValue/propertyNum;
        // only keep the integer part
        Integer i = new Double(res).intValue();
        return i;
    }

    public Integer calAvgTotalLiveArea(){
        if(propertyNum==0){
            return 0;
        }
        Double res = totalLiveableArea/propertyNum;
        // only keep the integer part
        Integer i = new Double(res).intValue();
        return i;
    }

    public int getPropertyNum() {
        return propertyNum;
    }

    public void incrPropertyNum(){
        this.propertyNum++;
    }

    public void setPropertyNum(int propertyNum) {
        this.propertyNum = propertyNum;
    }

    public void setZip(Long zip) {
        this.zip = zip;
    }

    public void setMarketValue(Double marketValue) {
        this.marketValue = marketValue;
    }

    public void setTotalLiveableArea(Double totalLiveableArea) {
        this.totalLiveableArea = totalLiveableArea;
    }

    public Long getZip() {
        return zip;
    }

    public Double getMarketValue() {
        return marketValue;
    }

    public Double getTotalLiveableArea() {
        return totalLiveableArea;
    }
}
