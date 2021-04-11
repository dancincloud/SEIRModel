package edu.neu.info6205;

/**
 * Main
 *
 * @author Joseph Yuanhao Li
 * @date 4/4/21 20:32
 */

import edu.neu.info6205.helper.CSVUtil;
import edu.neu.info6205.helper.ConfigParser;
import edu.neu.info6205.model.Residence;
import edu.neu.info6205.model.Person;
import edu.neu.info6205.model.Timeline;
import edu.neu.info6205.model.Virus;
import edu.neu.info6205.helper.Point;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static int days = 0; // world time

    // The entry of the whole program
    public static void main(String[] args){
        // output data
        List<String> logs = new ArrayList<String>();

        // set output file path
        String path = "/Users/yamato/Downloads/SEIR.csv";


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

        String head = String.format("%-20s,%-20s,%-20s,%-20s,%-20s", "Days", "Susceptible", "Exposed", "Infected", "Removed");
        logs.add(head);
        System.out.println(head.replace(',', ' '));

        Thread timelineThread = new Thread(new Timeline(residence));

        // set Daemon Thread
        Thread t = new Thread(() -> {
            while (true) {
                timelineThread.run();

                days++;

                String s = String.format("%-20d,%-20d,%-20d,%-20d,%-20d", days, residence.getSusceptible(), residence.getExposed(), residence.getInfected(), residence.getRemoved());
                logs.add(s);
                System.out.println(s.replace(',', ' '));

                if(residence.getExposed() == 0 && residence.getInfected() == 0){
                    System.out.printf("After fighting with Virus for %d days, the pandemic is over and human win!\n", days);

                    if(CSVUtil.exportCsv(new File(path), logs)) System.out.println("Write CSV Success: " + path);
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
