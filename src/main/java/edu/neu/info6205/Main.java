package edu.neu.info6205;

/**
 * Main
 *
 * @author Joseph Yuanhao Li
 * @date 4/4/21 20:32
 */

import edu.neu.info6205.helper.ConfigParser;
import edu.neu.info6205.model.Residence;
import edu.neu.info6205.model.Person;
import edu.neu.info6205.model.Timeline;
import edu.neu.info6205.model.Virus;
import edu.neu.info6205.helper.Point;

import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static int days = 0; // world time

    // The entry of the whole program
    public static void main(String[] args){
        // Initial virus
        Virus virus = Virus.buildByConfig("Virus.properties");

        ConfigParser.printObject(virus);


        // Initial city and residents
        Person.setActivityRadius(300); // m
        Person.setActivityRate(1);

        Residence residence = Residence.buildByConfig("Residence.properties");
        residence.setVirus(virus);
        residence.setInfected(10);
        residence.init();

        ConfigParser.printObject(residence);

        System.out.printf("%-20s %-20s %-20s %-20s %-20s\n", "Days", "Susceptible", "Exposed", "Infected", "Removed");

        Thread timelineThread = new Thread(new Timeline(residence));

        // set Daemon Thread
        Thread t = new Thread(() -> {
            while (true) {
                timelineThread.run();

                days++;

                System.out.printf("%-20d %-20d %-20d %-20d %-20d\n", days, residence.getSusceptible(), residence.getExposed(), residence.getInfected(), residence.getRemoved());

                if(residence.getExposed() == 0 && residence.getInfected() == 0){
                    System.out.printf("After fighting with Virus for %d days, the pandemic is over and human win!", days);
                    break;
                }

//                try {
//                    Thread.sleep(300);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        });
        t.setDaemon(true);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
