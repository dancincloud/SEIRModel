package edu.neu.info6205.model;

import edu.neu.info6205.helper.ConfigParser;

import java.util.Properties;

/**
 * Virus
 *
 * @author Joseph Yuanhao Li
 * @date 4/1/21 20:32
 */

public class Virus {
    private double R; // Basic reproduction number
    private double K; // Dispersion factor


    private double infectiousRadius; // infectious radius

    private double recoveryRate; // the rate which infected people can  γ
    private double incidenceRate; // the rate which infected people show symptoms

    private double latentPeriod; // Average time of an individual is pre-infectious
    private double infectiousPeriod; // Average time of an individual is infectious


    public Virus(){}

    public static Virus buildVirusFromConfig(String filePath){
        return (Virus) ConfigParser.configToClass(filePath, Virus.class);
    }

    /* Setter and Getter */
    public double getR() {
        return R;
    }

    public void setR(double r) {
        R = r;
    }

    public double getK() {
        return K;
    }

    public void setK(double k) {
        K = k;
    }

    public double getInfectiousRadius() {
        return infectiousRadius;
    }

    public void setInfectiousRadius(double infectiousRadius) {
        this.infectiousRadius = infectiousRadius;
    }

    public double getRecoveryRate() {
        return recoveryRate;
    }

    public void setRecoveryRate(double recoveryRate) {
        this.recoveryRate = recoveryRate;
    }

    public double getIncidenceRate() {
        return incidenceRate;
    }

    public void setIncidenceRate(double incidenceRate) {
        this.incidenceRate = incidenceRate;
    }

    public double getLatentPeriod() {
        return latentPeriod;
    }

    public void setLatentPeriod(double latentPeriod) {
        this.latentPeriod = latentPeriod;
    }

    public double getInfectiousPeriod() {
        return infectiousPeriod;
    }

    public void setInfectiousPeriod(double infectiousPeriod) {
        this.infectiousPeriod = infectiousPeriod;
    }

    /* Public Methods */
    // latentPeriod + infectiousPeriod
    public double getRecoveryPeriod() {
        return latentPeriod + infectiousPeriod;
    }
}
