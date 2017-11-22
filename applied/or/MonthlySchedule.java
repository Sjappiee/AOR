package applied.or;

import java.util.ArrayList;
import java.util.Random;

public class MonthlySchedule {
    private String schedule1; //type1
    private double [][] wages1 = {{324,240},{437.4,324}}; //[weekday,weekend][s1=nacht,s2=dag (12h)]  !!SHIFTSYSTEM
    private String schedule2; //type2
    private double [][] wages2 = {{243,180},{328.05,243}}; //[weekday,weekend][s1,s2]  !!SHIFTSYSTEM
    private double fixedAdmCost = 100000 * 2 * 28;  //28dagen, 12h:100000, 9h:60000   * amount shifts (2or3)      !!SHIFTSYSTEM
    private int shiftHours = 12;  //!!SHIFTSYSTEM
    private int amountNurses1 = 0;
    private int amountNurses2 = 0;
    
    public MonthlySchedule (ArrayList<Nurse> nurses,ArrayList<Nurse> workPatterns){ // weekly schedule waar alle nurses al assigned zijn aan patterns
        int amount1 = 0;
        int amount2 = 0;
        WeeklySchedule [] schedules = new WeeklySchedule [4];
        WeeklySchedule weeklySchedule1 = new WeeklySchedule (nurses,workPatterns);
        //week 1
        weeklySchedule1.allProcesses();
        schedules[0] = weeklySchedule1;
        amount1 += weeklySchedule1.amountWithType(weeklySchedule1.getNurses())[0];
        amount2 += weeklySchedule1.amountWithType(weeklySchedule1.getNurses())[1];
        //week2,3,4
        for (int i = 1; i < 4; i++) {
            WeeklySchedule weeklySchedulei = new WeeklySchedule (nurses,workPatterns);
            int changeOrNot = randomBoolean(0);  //100% kans dat week 2 gwn idem aan week 1 is, 0%kans dat voor week 2 een nieuw schema word opgesteld
            if(changeOrNot == 0){
                weeklySchedulei = schedules[i-1];
            }
            else{
                weeklySchedulei.allProcesses();
            }
            schedules[i] = weeklySchedulei;
            amount1 += weeklySchedulei.amountWithType(weeklySchedulei.getNurses())[0];
            amount2 += weeklySchedulei.amountWithType(weeklySchedulei.getNurses())[1];
        }
        
        this.amountNurses1 = amount1;
        this.amountNurses2 = amount2;
        String monthSchedule1 = "";
        String monthSchedule2 = "";
        for (int i = 0; i < 4; i++) {  //per week: add nurses of each type to a different list + make a string of the schedules
            ArrayList <Nurse> scheduleType1 = new ArrayList <Nurse>();
            ArrayList <Nurse> scheduleType2 = new ArrayList <Nurse>();
            for (Nurse nurse : schedules[i].getNurses()) { //types opsplitsen
                if(nurse.getType() == 1){
                    scheduleType1.add(nurse);
                }
                if(nurse.getType() == 2){
                    scheduleType2.add(nurse);
                }
            }
            WeeklySchedule temp1 = new WeeklySchedule(scheduleType1); //type 1
            WeeklySchedule temp2 = new WeeklySchedule(scheduleType2); //type 2
            monthSchedule1 += temp1.ScheduleToString();
            monthSchedule2 += temp2.ScheduleToString();
        }
        this.schedule1 =   monthSchedule1;
        this.schedule2 =   monthSchedule2;
    }
    
    public int randomBoolean (int probOnOne){
        int randomBit = 0;
        float prob1 = probOnOne/10;
        int temp = new Random().nextInt(10);
        if(temp < prob1){
            randomBit = 1;
        }
        return randomBit;
    }
    
    
    public double calcCost (int type){ //toepasbaar op schedule1 en schedule2
        double cost = 0;
        int labourHoursWeek = 0;
        int labourHoursWeekend = 0;
        String schedule = null;
        double [][] wages = null;
        int amountNurses = 0;
        if(type == 1){
            schedule = schedule1;
            wages = wages1;
            amountNurses = amountNurses1;
        }
        else if (type == 2){
            schedule = schedule2;
            wages = wages2;
            amountNurses = amountNurses2;
        }
        System.out.println("schedule: " + schedule);
        System.out.println("wages: " + wages[0][0] + ","+ wages[0][1] + ","+ wages[1][0] + ","+ wages[1][1]);
        
        //wages
        int counter = 0;
        for (int w = 0; w < 4; w++) {  
            for (int n = 0; n < amountNurses; n++) { // nurses
                for (int i = 0; i < 5; i++) { //week
                    System.out.println(counter);
                    if(Character.getNumericValue(schedule.charAt(counter)) > 0){
                        System.out.println("shift: " + Character.getNumericValue(schedule.charAt(counter)));
                        cost += wages[0][Character.getNumericValue(schedule.charAt(counter))-1] ; //if type=1, wages[0][0] ,if type=2, wages[0][1]
                        labourHoursWeek += shiftHours;
                        System.out.println(" " + cost);
                    }
                     counter++;
                }
                for (int i = 5; i < 8; i++) { //weekend
                    System.out.println(counter);
                    if(Character.getNumericValue(schedule.charAt(counter)) > 0 ){
                        System.out.println("shift: " + Character.getNumericValue(schedule.charAt(counter)));
                        cost += wages[1][Character.getNumericValue(schedule.charAt(counter))-1] ; //if type=1, wages[1][0] ,if type=2, wages[1][1]
                        labourHoursWeekend += shiftHours;
                        System.out.println(" " + cost);
                    }
                    counter++;
                } 
            }
        }
        System.out.println("labourHoursWeek: " + labourHoursWeek);
        System.out.println("labourHoursWeekend: " + labourHoursWeekend);
        //administrative costs
        cost += fixedAdmCost + labourHoursWeek + labourHoursWeekend;
        return cost;
    }
 /*   
    public int calcNurseSat (){
       
    eerst vermeningvudigen met hun schema X hun specifieke monthly pref
    dan ook nog nog met de verschillende regels: 
        vrije dagen na elkaar?
        alle dagen werken dat je wil? Dus schema = ER? 
        veranderen in shiften?
        
    }
    
    public int calcPatientSat (){
        
    }
    public int [] calcObjectiveFunctions () {
        alle 3 berekenen en in een array zetten
    }
*/
    public String getSchedule1() {
        return schedule1;
    }

    public void setSchedule1(String schedule1) {
        this.schedule1 = schedule1;
    }

    public String getSchedule2() {
        return schedule2;
    }
    public void setSchedule2(String schedule2) {
        this.schedule2 = schedule2;
    }
    
    
    
}
