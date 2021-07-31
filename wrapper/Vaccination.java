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

    //    public void addPartial(Long val){
//        this.partial += val;
//    }
//
//    /**
//     * add this val to fully
//     * @param val
//     */
//    public void addFull(Long val){
//        this.fully += val;
//    }
//
//    /**
//     * add both par and full to partial and fully
//     * @param par
//     * @param fully
//     */
//    public void addParFull(Long par, Long fully){
//        this.partial += par;
//        this.fully += fully;
//    }
}
