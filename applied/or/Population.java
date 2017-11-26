/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applied.or;

import java.util.ArrayList;

/**
 *
 * @author stephan
 */
public class Population {
    
    private int amountReplications = 1;
    private int amountSchedules = 1;
    private ArrayList <MonthlySchedule> wholepopulation;

    public Population(ArrayList<Nurse> nurses,ArrayList<Nurse> workPatterns) {
        for (int i = 0; i < amountReplications; i++) {
            MonthlySchedule schedule = new MonthlySchedule(nurses,workPatterns);
            System.out.println(schedule.getAmountNurses1());
            System.out.println(schedule.getAmountNurses2());
            this.wholepopulation.add(schedule);
        }
    }
    
    public MonthlySchedule giveOptimal (){
        double min = 100000000;
        int scheduleNr = -1;
        for (MonthlySchedule schedule: wholepopulation) {
            double score = schedule.calcTotalObjectiveFunction();
            if(score < min){
                min = score;
                scheduleNr = wholepopulation.indexOf(schedule);
            }
        }
        System.out.println("optimal schedule index:" + scheduleNr);
        System.out.println("with a min score of: " + min);
        return wholepopulation.get(scheduleNr);
    }
    
    
 

    
    
}
