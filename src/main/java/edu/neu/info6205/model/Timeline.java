package edu.neu.info6205.model;

import edu.neu.info6205.helper.PersonStatus;

import java.util.Arrays;
import java.util.List;

/**
 * Calculate the latest daily epidemic situation
 *
 * @author Joseph Yuanhao Li
 * @date 4/11/21 02:47
 */
public class Timeline implements Runnable {

    private Residence residence;

    public Timeline(Residence residence){
        this.residence = residence;
    }

    @Override
    public void run() {
        update(residence.getResidents());
    }

    // calculate the status of city and residents in next day
    public void update(Person[] residents){

        Arrays.sort(residents, Person.xComparator);
        spreadVirus(residents);

        Arrays.sort(residents, Person.yComparator);
        spreadVirus(residents);

        // reset S E I R population
        residence.resetSEIR();

        long susceptible = 0, exposed = 0, infected = 0, removed = 0;

        // count S E I R population
        for(Person p : residents){
            p.update();

            if(residence.ifOutOfArea(p)) p.setLocation(residence.randomPoint());

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

        residence.setSEIR(susceptible, exposed, infected, removed);

//        System.out.printf("update finish\n");
    }

    private void spreadVirus(Person[] residents){
        Virus virus = residence.getVirus();

        for (int i = 0; i < residents.length; i++){
            Person p1 = residents[i];

            if(p1.getStatus() == PersonStatus.Removed) continue;

            for(int j = i + 1; j < Math.min(residents.length, i + 10); j++){
                Person p2 = residents[j];

                if(p2.getX() - p1.getX() > virus.getInfectiousRadius()) continue;

                if(p2.getStatus() == PersonStatus.Removed) continue;

                if(p1.isContagious() ^ p2.isContagious()){
                    if(Person.distance(p1, p2) <= virus.getInfectiousRadius()){
                        if(p1.isContagious()) p2.setStatus(PersonStatus.Exposed);
                        else p1.setStatus(PersonStatus.Exposed);
                    }
                }
            }
        }
    }
}
