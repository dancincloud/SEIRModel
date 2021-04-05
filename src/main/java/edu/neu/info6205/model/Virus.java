package edu.neu.info6205.model;

/**
 * Virus
 *
 * @author Joseph Yuanhao Li
 * @date 4/1/21 20:32
 */

public class Virus {
    private double R; // Basic reproduction number
    private double K; // Dispersion factor
    private double latentPeriod; // Average time of an individual is pre-infectious
    private double infectiousPeriod; // Average time of an individual is infectious

    private double infectiousRadius; // infectious radius

    private double recoveryRate;
    // 康复概率是γ

    private double infectedRate; // the rate which exposed people infect susceptible people
    // 潜伏者会传染易感者的概率为 β

    public Virus(double R, double K, double latentPeriod, double infectiousPeriod){
        this.R = R;
        this.K = K;
        this.latentPeriod = latentPeriod;
        this.infectiousPeriod = infectiousPeriod;
    }
}
