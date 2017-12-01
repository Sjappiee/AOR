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
    
    private int amountReplications = 10000;
    private ArrayList <MonthlySchedule> wholepopulation = new ArrayList <MonthlySchedule> ();
    private MonthlySchedule optimalSchedule = null;

    public Population(ArrayList<Nurse> nursesExcel,ArrayList<Nurse> workPatternsExcel,  int percentageRandomWeekly, int percentageSubrandomWeekly, int percantageNotCyclic) {
        for (int i = 0; i < amountReplications; i++) {
            ArrayList<Nurse> nurses = cloneList(nursesExcel);
            ArrayList<Nurse> workPatterns = cloneList(workPatternsExcel);
            MonthlySchedule schedule = new MonthlySchedule(nurses,workPatterns,percentageRandomWeekly,percentageSubrandomWeekly, percantageNotCyclic);
            this.wholepopulation.add(schedule);  
            schedule = null;
        }
    }
    
    public MonthlySchedule giveOptimal (){
        System.out.println("SEARCH FOR THE OPTIMAL");
        double min = 100000000;
        int scheduleNr = -1;
        for (MonthlySchedule schedule: wholepopulation) {
            System.out.println("schedule: " + (wholepopulation.indexOf(schedule)+1));
            System.out.println("SCHEDULE1: " + schedule.getSchedule1());
            System.out.println("SCHEDULE2: " + schedule.getSchedule2());
            System.out.println("DIFFERENT SCORES: ");
            double score = schedule.calcTotalObjectiveFunction();
            System.out.println("score: " + score);
            if(score < min){
                min = score;
                scheduleNr = wholepopulation.indexOf(schedule);
            }
            System.out.println("");
            System.out.println("");
        }
        System.out.println("optimal schedule index:" + scheduleNr);
        System.out.println("with a min score of: " + min);
        optimalSchedule =wholepopulation.get(scheduleNr);
        return wholepopulation.get(scheduleNr);
    }
    
    public static ArrayList<Nurse> cloneList(ArrayList<Nurse> list) {
    ArrayList<Nurse> cloneList = new ArrayList<Nurse>(list.size());
    for (Nurse item : list){
        Nurse temp = item.clone();
        cloneList.add(temp);
    }
    return cloneList;
}

    public int getAmountReplications() {
        return amountReplications;
    }

    public ArrayList<MonthlySchedule> getWholepopulation() {
        return wholepopulation;
    }

    public MonthlySchedule getOptimalSchedule() {
        return optimalSchedule;
    }
 

    
    
}
