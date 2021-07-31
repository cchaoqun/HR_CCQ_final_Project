package com.CIT594.project594.wrapper;

import java.text.DecimalFormat;

/**
 * @author Chaoqun Cheng
 * @date 2021-07-2021/7/30-23:55
 */

/**
 * Wrapper class for integrating population and partial && full vaccination
 */
public class Covid {
    /**
     * 10^n represent how n digits we want to keep after .
     */
    private int numDigit = 10000;
    /**
     * zip code of this covid
     */
    private Long zip;
    /**
     * number of partial vaccinated people
     */
    private Long partial;
    /**
     * number of fully vaccinated people
     */
    private Long full;
    /**
     * time
     */
    private String time;
    /**
     * number of people of this zip
     */
    private Long pop;
    /**
     * decimal number formatter into 4 digit
     */
    private DecimalFormat df = new DecimalFormat("0.0000");
    /**
     * partial vaccination percentage
     */
    private double partialVacPerc;
    /**
     * fully vaccination percentage
     */
    private double fullyVacPerc;

    /**
     * Constructor with no params, initialize those field
     */
    public Covid(){
        zip = 0l;
        partial = 0l;
        full = 0l;
        pop = 0l;
        time = "";
    }

    /**
     * constructor with params
     * @param _zip
     * @param _partial
     * @param _full
     * @param _time
     */
    public Covid(Long _zip, Long _partial, Long _full, String _time){
        zip = _zip;
        partial = _partial;
        full = _full;
        time = _time;
    }

    /**
     * calculate the vaccination of this zip
     * @param isPartial 0 is calculate partial and 1 is calculate fully
     * @return
     */
    public String getVacciPerCap(int isPartial){
        double res = 0.0;
        // calculate the partial percentage
        if(isPartial==0){
            res = partial*1.0/pop;
            this.partialVacPerc = res;
        }else{
            // calculate the fully percentage
            res = full*1.0/pop;
            this.fullyVacPerc = res;
        }
        // format it into truncated at 4 digit, right padding with 0
        return formatDouble(res);
    }

    /**
     * format the double input truncated at 4 digits padding with 0 if less than 4 digits
     * @param input
     * @return
     */
    public String formatDouble(double input){
        int temp = (int)(input*numDigit);
        input = temp*1.0/numDigit;
        return df.format(input);
    }

    /**
     * getter and setter
     * @return
     */
    public Long getPartial() {
        return partial;
    }

    public Long getFull() {
        return full;
    }

    public Long getPop() {
        return pop;
    }

    public double getPartialVacPerc() {
        return partialVacPerc;
    }

    public double getFullyVacPerc() {
        return fullyVacPerc;
    }

    public Long getZip() {
        return zip;
    }

    public void setZip(Long zip) {
        this.zip = zip;
    }

    public void setPop(Long pop) {
        this.pop = pop;
    }

    public void setPartial(Long partial) {
        this.partial = partial;
    }

    public void setFull(Long full) {
        this.full = full;
    }

    public void setPartialVacPerc(double partialVacPerc) {
        this.partialVacPerc = partialVacPerc;
    }

    public void setFullyVacPerc(double fullyVacPerc) {
        this.fullyVacPerc = fullyVacPerc;
    }
}
