package edu.neu.info6205.model;

/**
 * City
 *
 * @author Joseph Yuanhao Li
 * @date 4/4/21 20:32
 */

import java.awt.*;
import java.util.*;
import java.util.List;

public class City {
    private long population;
    private long susceptible;
    private long exposed;
    private long infected;
    private long removed;

    private double radius; // the radius of the city

    private List<Person> residents;

    private Point center;

    private static final Random random = new Random();

    public City(long population, double radius, Point center){
        this.population = population;
        this.radius = radius;

        this.residents = new ArrayList<>();

        this.center = center;

        this.generateResidents();
    }

    public long getPopulation(){
        return population;
    }

    public List<Person> getResidents(){
        return residents;
    }

    public double getRadius(){
        return radius;
    }

    public double getCenterX(){
        return center.getX();
    }

    public double getCenterY(){
        return center.getY();
    }

    // check if the location is out of the city area
    public boolean ifOutOfArea(Person p){
        if(Math.abs(p.getX() - center.getX()) > radius || Math.abs(p.getY() - center.getY()) > radius) return true;
        return false;
    }

    // generate residents (total number is population)
    private void generateResidents(){
        for(int i = 0; i < population; i++){
            residents.add(personFactory());
        }
    }

    public void initInfected(long infected){
        for(int i = 0; i < infected; i++){
            residents.get(i).setStatus(PersonStatus.Infected);
        }
        this.infected = infected;
        this.susceptible = population - infected;
    }

    // calculate the status of city and residents in next day
    public void update(){
        Collections.sort(residents);
        for (int i = 0; i < residents.size(); i++){
            Person p1 = residents.get(i);

            if(p1.getStatus() == PersonStatus.Removed) continue;

            for(int j = i + 1; j < residents.size(); j++){
                Person p2 = residents.get(j);
                if(p2.getStatus() == PersonStatus.Removed) continue;

                if(p1.isContagious() ^ p2.isContagious()){
                    if(Person.distance(p1, p2) <= 5){
                        if(p1.isContagious()) p2.setStatus(PersonStatus.Exposed);
                        else p1.setStatus(PersonStatus.Exposed);
                    }
                }
            }
        }

        // reset SEIR
        susceptible = 0;
        exposed = 0;
        infected = 0;
        removed = 0;

        for(Person p : residents){
            p.update();

            //count SEIR
            switch (p.getStatus()) {
                case Susceptible: {
                    // susceptible
                    susceptible++;
                    break;
                }
                case Exposed: {
                    // exposed)
                    exposed++;
                    break;
                }

                case Infected: {
                    // infected
                    infected++;
                    break;
                }
                case Removed : {
                    //Recoverd or dead
                    removed++;
                    break;
                }
                default:{
                    System.out.println("Draw person: Wrong Status!");
                }
            }
        }
    }

    public long getSusceptible(){
        return susceptible;
    }

    public long getExposed(){
        return exposed;
    }

    public long getInfected(){
        return infected;
    }

    public long getRemoved() {
        return removed;
    }

    // nextGaussian() 99.74% in [-3,3]
    private Person personFactory(){
        return new Person(new Point(radius / 3.0 * random.nextGaussian() + getCenterX(), radius / 3.0 * random.nextGaussian() + getCenterY()));
    }
}
