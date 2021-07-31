package com.CIT594.project594.wrapper;

/**
 * @author Chaoqun Cheng
 * @date 2021-07-2021/7/31-15:09
 */

public class Vaccination{
    /**
     * number of partial vaccinated people
     */
    private Long partial;
    /**
     * number of fully vaccinated people
     */
    private Long fully;

    /**
     * constructor with no params
     */
    public Vaccination(){}

    /**
     * constructor with parameters
     * @param _partial
     * @param _fully
     */
    public Vaccination(Long _partial, Long _fully){
        partial = _partial;
        fully = _fully;
    }

    /**
     * update both partial and fully vaccinated number of people,
     * @param par
     * @param fully
     */
    public void updateParFull(Long par, Long fully){
        this.partial = par;
        this.fully = fully;
    }

    @Override
    public String toString(){
        return ": "+partial+" "+fully;
    }

    //===================Getter and Setter====================
    public void setPartial(Long partial) {
        this.partial = partial;
    }

    public void setFully(Long fully) {
        this.fully = fully;
    }

    public Long getPartial() {
        return partial;
    }

    public Long getFully() {
        return fully;
    }


}
