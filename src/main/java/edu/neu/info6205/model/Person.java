package edu.neu.info6205.model;

/**
 * Person
 *
 * @author Joseph Yuanhao Li
 * @date 4/1/21 20:32
 */

import edu.neu.info6205.helper.PersonStatus;
import edu.neu.info6205.helper.Point;

import java.util.Random;

public class Person implements Comparable<Person> {

    private static Virus virus; // Virus spreading in people

    private Point location;
    private PersonStatus status = PersonStatus.Susceptible;

    static final private Random random = new Random();

    /* The rate of people go outside everyday */
    static private double activityRate;

    /* The active radius */
    static private double activityRadius;

    private long duraion;  // The duration of current status

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


    /* Setter and Getter */
    public static void setVirus(Virus virus){
        Person.virus = virus;
    }

    public void setStatus(PersonStatus status){
        this.status = status;
    }

    public PersonStatus getStatus(){
        if(status == PersonStatus.Infected && duraion > virus.getRecoveryPeriod()){
            duraion = 0;
            setStatus(PersonStatus.Removed);
        }else if(status == PersonStatus.Exposed && duraion > virus.getLatentPeriod()){
            setStatus(PersonStatus.Infected);
        }

        return status;
    }

    static public void setActivityRate(double activityRate){
        Person.activityRate = activityRate;
    }

    static public double getActivityRate(){
        return Person.activityRate;
    }

    static public void setActivityRadius(double activityRadius){
        Person.activityRadius = activityRadius;
    }

    public double getX(){
        return location.getX();
    }

    public double getY(){
        return location.getY();
    }


    //if this person can infect others(people who has been infected or has been exposed for more than half of virus's latentPeriod)
    public boolean isContagious(){
        return status == PersonStatus.Infected || (status == PersonStatus.Exposed && duraion > virus.getLatentPeriod());
    }

    public void update(){
        // count the days which virus exist in this person
        if(status == PersonStatus.Exposed || status == PersonStatus.Infected) duraion++;

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
