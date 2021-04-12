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

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Main {
    // The entry of the whole program
    public static void main(String[] args){
        // output data
        List<String> logs = new ArrayList<String>();

        // set output file path
        String path = "/Users/yamato/Downloads/SEIR.csv";


        // Initial virus
        Virus virus = Virus.buildByConfig("Virus/Covid-19.properties");

        ConfigParser.printObject(virus);


        // Initial city and residents
        Person.setActivityRadius(300); // m
        Person.setActivityRate(1);

        Residence residence = Residence.buildByConfig("Residence/Ideal.properties");
        residence.setVirus(virus);
        residence.init();

        ConfigParser.printObject(residence);

        String head = String.format("%-15s,%-15s,%-15s,%-15s,%-15s,%-15s,%-10s,%-10s", "Days", "Susceptible", "Exposed", "Infected", "Recovered", "Dead", "R", "K");
        logs.add(head);
        System.out.println(head.replace(',', ' '));

        Timeline timeline = new Timeline(residence);

        Thread timelineThread = new Thread(timeline);

        // set Daemon Thread
        Thread t = new Thread(() -> {
            while (true) {
                timelineThread.run();

                String s = String.format("%-15d,%-15d,%-15d,%-15d,%-15d,%-15d,%-10.2f,%-10.2f", timeline.getDays(), residence.getSusceptible(), residence.getExposed(), residence.getInfected(), residence.getRecovered(), residence.getDead(), timeline.getR(),timeline.getK());
                logs.add(s);
                System.out.println(s.replace(',', ' '));

                if(residence.getExposed() == 0 && residence.getInfected() == 0){
                    System.out.printf("After fighting with Virus for %d days, the pandemic is over and human win!\n", timeline.getDays());

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
