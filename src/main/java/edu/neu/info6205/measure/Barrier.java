package edu.neu.info6205.measure;

import edu.neu.info6205.helper.ConfigParser;
import edu.neu.info6205.model.Virus;

/**
 * Barrier
 *
 * Applicable for people who have been infected with the virus for more than the threshold number of days.
 * The probability of people complying with the quarantine is equal to "effective".
 * People's movable radius is equal to "radius".
 *
 * @author Joseph Yuanhao Li
 * @date 4/4/21 20:32
 */

public class Barrier {
    private boolean enable; // if enable this measure
    private double effective; // Actual rate of people obey the rules
    private int threshold; // People who get virus more than threshold days will be apply
    private double radius; // People's movable radius

    public Barrier(){}

    public static Barrier buildByConfig(String filePath){
        return (Barrier) ConfigParser.configToClass(filePath, Barrier.class);
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public double getEffective() {
        return effective;
    }

    public void setEffective(double effective) {
        this.effective = effective;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
