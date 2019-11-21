/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trimostomachine;

/**
 *
 * @author mats.andersson
 */
public class PositionMessage {

    private double xVal;
    private double zVal;

    public double getXVal() {
        return xVal;
    }

    public void setxVal(double xVal) {
        this.xVal = xVal;
    }

    public double getZVal() {
        return zVal;
    }

    public void setzVal(double zVal) {
        this.zVal = zVal;
    }

    public PositionMessage(double xVal, double zVal) {
        this.xVal = xVal;
        this.zVal = zVal;
    }
    

    
}
