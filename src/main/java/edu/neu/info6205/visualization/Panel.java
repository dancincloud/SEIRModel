package edu.neu.info6205.visualization;

/**
 * Panel
 *
 * @author Joseph Yuanhao Li
 * @date 4/1/21 20:32
 */

import edu.neu.info6205.helper.CSVUtil;
import edu.neu.info6205.model.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Panel extends JPanel implements Runnable {
    private Residence residence;
    private Timeline timeline;
    private Thread timelineThread;

    private String path;
    private List<String> logs;

    public Panel(Residence residence, Measure measure, String path, List<String> logs) {
        super();

        this.residence = residence;
        this.timeline = new Timeline(residence, measure);
        this.timelineThread = new Thread(this.timeline);

        this.path = path;
        this.logs = logs;

        this.setBackground(new Color(0x000000));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        drawData(g);

        drawResidence(g);

        String s = String.format("%-15d,%-15d,%-15d,%-15d,%-15d,%-15d,%-10.4f,%-10.4f", timeline.getDays(), residence.getSusceptible(), residence.getExposed(), residence.getInfected(), residence.getRecovered(), residence.getDead(), timeline.getR(),timeline.getK());
        logs.add(s);
        System.out.println(s.replace(',', ' '));

        if(residence.getExposed() == 0 && residence.getInfected() == 0){
            System.out.printf("After fighting with Virus for %d days, the pandemic is over and human win!\n", timeline.getDays());

            if(CSVUtil.exportCsv(new File(path), logs)) System.out.println("Write CSV Success: " + path);
            return;
        }

        //calculate city's next status
        timelineThread.run();
        try {
            timelineThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.repaint();
    }

    private void drawResidence(Graphics g){
        g.setColor(new Color(0xffffff));
        g.drawRect( (int) (residence.getDrawX() - residence.getDrawRadius()), (int) (residence.getDrawY() - residence.getDrawRadius()), (int) residence.getDrawRadius() * 2, (int) residence.getDrawRadius() * 2);

        //draw person nodes
        Person[] residents = residence.getResidents();
        if (residents == null) {
            return;
        }

        // set color according to PersonStatus
        for (Person person : residents) {
            if(residence.ifOutOfArea(person)) continue;
            switch (person.getStatus()) {
                case Susceptible: {
                    g.setColor(new Color(0x33A1C9));
                    break;
                }
                case Exposed: {
                    g.setColor(new Color(0xE3A869));
                    break;
                }

                case Infected: {
                    g.setColor(new Color(0xff0000));
                    break;
                }
                case Removed : {
                    g.setColor(new Color(0xC0C0C0));
                    break;
                }
//                case Dead : {
//                    g.setColor(new Color(0xFFD700));
//                    break;
//                }
                default:{
                    System.out.println("Draw person: Wrong Status!");
                }
            }

            // draw person node
            g.fillOval((int) (residence.mappingToDrawing(person.getX()) + residence.getDrawX()), (int) (residence.mappingToDrawing(person.getY()) + residence.getDrawY()), 1, 1);
        }
    }

    private void drawData(Graphics g){
        int padding = 100;
        int captionStartOffsetX = (int) (residence.getDrawX() + residence.getDrawRadius() + padding);
        int captionStartOffsetY = 100;
        int captionSize = 40;

        g.setFont(new Font("Helvetica",Font.PLAIN,24));

        g.setColor(Color.WHITE);
        g.drawString("Statistics Overview", captionStartOffsetX, captionStartOffsetY);


        captionStartOffsetY += 2 * captionSize;
        g.setColor(new Color(0xffffff));
        g.drawString(String.format("%-20s: %10d", "Days", (int) timeline.getDays()), captionStartOffsetX, captionStartOffsetY);

        captionStartOffsetY += captionSize;
        g.setColor(Color.WHITE);
        g.drawString(String.format("%-20s: %10d", "Population", residence.getPopulation()), captionStartOffsetX, captionStartOffsetY);

        captionStartOffsetY += captionSize;
        g.setColor(new Color(0x33A1C9));
        g.drawString(String.format("%-21s: %10d", "Heathy Population", residence.getSusceptible()), captionStartOffsetX, captionStartOffsetY);


        captionStartOffsetY += captionSize;
        g.setColor(new Color(0xE3A869));
        g.drawString(String.format("%-20s: %10d", "Exposed Population", residence.getExposed()), captionStartOffsetX, captionStartOffsetY);

        captionStartOffsetY += captionSize;
        g.setColor(new Color(0xff0000));
        g.drawString(String.format("%-22s: %10d", "Infected Population", residence.getInfected()), captionStartOffsetX, captionStartOffsetY);
        
        captionStartOffsetY += captionSize;
        g.setColor(new Color(0xC0C0C0));
        g.drawString(String.format("%-19s: %10d", "Removed Population", residence.getRemoved()), captionStartOffsetX, captionStartOffsetY);
    }

//    public Timer timer = new Timer();
//
//    class MyTimerTask extends TimerTask {
//        @Override
//        public void run() {
//            Panel.this.repaint();
//            days++;
//
//            System.out.printf("%-20d %-20d %-20d %-20d %-20d\n", days, residence.getSusceptible(), residence.getExposed(), residence.getInfected(), residence.getRemoved());
//
//            if(residence.getExposed() == 0 && residence.getInfected() == 0){
//                timer.cancel();
//                System.out.printf("After fighting with Virus for %d days, the pandemic is over and human win!", days);
//            }
//        }
//    }

    @Override
    public void run() {
        System.out.printf("%-20s %-20s %-20s %-20s %-20s\n", "Days", "Susceptible", "Exposed", "Infected", "Removed");

//        timer.schedule(new MyTimerTask(), 0, 5000);
    }
}
