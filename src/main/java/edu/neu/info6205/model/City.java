package edu.neu.info6205.model;

/**
 * City
 *
 * @author Joseph Yuanhao Li
 * @date 4/4/21 20:32
 */

import edu.neu.info6205.helper.PersonStatus;
import edu.neu.info6205.helper.Point;

import java.util.*;
import java.util.List;

public class City {
    private static Virus virus; // the virus spreading in the city

    private long population; // total number of residents in the city
    private long susceptible; // total number of susceptible residents
    private long exposed;   // total number of exposed residents(Incubation period patient)
    private long infected; // total number of infected residents
    private long removed;  // total number of recovered or dead residents

    private double radius; // the radius of the city (mile)

    private List<Person> residents; // Person objects' list

    private Point center; // Coordinates of the city center (for drawing)

    private static final Random random = new Random();

    public City(long population, double radius, Point center){
        this.population = population;
        this.radius = radius;

        this.residents = new ArrayList<>();

        this.center = center;

        this.generateResidents();
    }

    /* initial condition  */
//    public static void setVirus(){
//
//    }


    // set the virus and initial infected population
    public void initInfected(Virus virus, long infected){
        City.virus = virus;

        Person.setVirus(virus);

        for(int i = 0; i < infected; i++){
            residents.get(i).setStatus(PersonStatus.Infected);
        }
        this.infected = infected;
        this.susceptible = population - infected;
    }

    /* Setter and Getter */
    public double getRadius(){
        return radius;
    }

    public double getCenterX(){
        return center.getX();
    }

    public double getCenterY(){
        return center.getY();
    }

    public List<Person> getResidents(){
        return residents;
    }

    public long getPopulation(){
        return population;
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

    /* Public Methods */
    // check if the location is out of the city area
    public boolean ifOutOfArea(Person p){
        if(Math.abs(p.getX() - center.getX()) > radius || Math.abs(p.getY() - center.getY()) > radius) return true;
        return false;
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

        // reset S E I R population
        susceptible = 0;
        exposed = 0;
        infected = 0;
        removed = 0;

        // count S E I R population
        for(Person p : residents){
            p.update();

            switch (p.getStatus()) {
                case Susceptible: {  // susceptible
                    susceptible++;
                    break;
                }
                case Exposed: { // exposed
                    exposed++;
                    break;
                }

                case Infected: { // infected
                    infected++;
                    break;
                }
                case Removed : { // Recovered or dead
                    removed++;
                    break;
                }
                default:{
                    System.out.println("Draw person: Wrong Status!");
                }
            }
        }
    }

    /* Private Methods */
    // generate residents (total number is population)
    private void generateResidents(){
        for(int i = 0; i < population; i++){
            residents.add(personFactory());
        }
    }

    // nextGaussian() 99.74% in [-3,3]
    private Person personFactory(){
        return new Person(new Point(radius / 3.0 * random.nextGaussian() + getCenterX(), radius / 3.0 * random.nextGaussian() + getCenterY()));
    }
}
