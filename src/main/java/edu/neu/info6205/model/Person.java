package edu.neu.info6205.model;

/**
 * Person
 *
 * @author Joseph Yuanhao Li
 * @date 4/1/21 20:32
 */

import java.util.Random;

public class Person implements Comparable<Person> {
    private Point location;
    private PersonStatus status = PersonStatus.Susceptible;

    private static long latentPeriod = 14; // Average time of an individual is pre-infectious
    private static long infectiousPeriod = 30; // Average time of an individual is pre-infectious


    static final private Random random = new Random();

    /* The rate of people go outside everyday */
    static private double activityRate;

    static public void setActivityRate(double activityRate){
        Person.activityRate = activityRate;
    }

    static public double getActivityRate(){
        return Person.activityRate;
    }


    /* The active radius */
    static private double activityRadius;

    static public void setActivityRadius(double activityRadius){
        Person.activityRadius = activityRadius;
    }

    static public double getActivityRadius(){
        return  Person.activityRadius;
    }

    public Person(Point location ){
        this.location = location;
    }

    public Person(Point location, PersonStatus status){
        this.location = location;
        this.status = status;
    }

    private long duraion;  // The duration of current status

    public void setStatus(PersonStatus status){
        if(status != this.status){
            // status changed, reset duration
            duraion = 0;
        }
        this.status = status;
    }

    public PersonStatus getStatus(){
        if(status == PersonStatus.Exposed && duraion >= latentPeriod){
            setStatus(PersonStatus.Infected);
        }else if(status == PersonStatus.Infected && duraion >= infectiousPeriod - latentPeriod){
            setStatus(PersonStatus.Removed);
        }

        return status;
    }

    //if this person can infect others(infected or exposed more than half of virus's latentPeriod)
    public boolean isContagious(){
        return getStatus() == PersonStatus.Infected || (status == PersonStatus.Exposed && duraion >= latentPeriod / 2);
    }

    public double getX(){
        return location.getX();
    }

    public double getY(){
        return location.getY();
    }

    public void update(){
        duraion++;

        randomMove();
    }

    public void randomMove(){
        double dx = Person.activityRadius / 3.0 * random.nextGaussian();
        double dy = Person.activityRadius / 3.0 * random.nextGaussian();

        location = new Point(location.getX() + dx, location.getY() + dy);
    }

    public int compareTo(Person p){
        int cmp = Double.compare(getX(), p.getX());
        if(cmp != 0) return cmp;

        return Double.compare(getY(), getX());
    }

    public static double distance(Person p1, Person p2){
        return Point.distance(p1.location, p2.location);
    }
}
