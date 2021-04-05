package edu.neu.info6205;

import edu.neu.info6205.model.City;
import edu.neu.info6205.model.Person;
import edu.neu.info6205.model.Point;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static  int days = 0; // world time

    // The entry of the whole program
    public static void main(String[] args){
        //Initial city and residents
        Person.setActivityRadius(10);
        Person.setActivityRate(1);

        City city = new City(1000, 300, new Point(400, 400));
        city.initInfected(10);

        System.out.printf("%-20s %-20s %-20s %-20s %-20s\n", "Days", "Susceptible", "Exposed", "Infected", "Removed");



        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                days++;
                city.update();

                System.out.printf("%-20d %-20d %-20d %-20d %-20d\n", days, city.getSusceptible(), city.getExposed(), city.getInfected(), city.getRemoved());

                if(city.getExposed() == 0 && city.getInfected() == 0){
                    timer.cancel();
                }
            }
        }, 0, 300);
    }
}
