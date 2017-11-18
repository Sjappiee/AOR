package applied.or;

import java.util.ArrayList;

public class MonthlySchedule {
    private String schedule1; //type1
    private double [][] wages1 = {{324,240},{437.4,324}}; //[weekday,weekend][s1=nacht,s2=dag (12h)]  !!SHIFTSYSTEM
    private String schedule2; //type2
    private double [][] wages2 = {{243,180},{328.05,243}}; //[weekday,weekend][s1,s2]  !!SHIFTSYSTEM
    private double fixedAdmCost = 100000 * 2 * 28;  //28dagen, 12h:100000, 9h:60000   * amount shifts (2or3)      !!SHIFTSYSTEM
    private int shiftHours = 12;  //!!SHIFTSYSTEM
    
    
    public MonthlySchedule (WeeklySchedule weeklySchedule){ // weekly schedule waar alle nurses al assigned zijn aan patterns
        String monthSchedule1 = "";
        String monthSchedule2 = "";
        ArrayList <Nurse> scheduleType1 = new ArrayList <Nurse>();
        ArrayList <Nurse> scheduleType2 = new ArrayList <Nurse>();
        String string1;
        String string2;
        for (Nurse nurse : weeklySchedule.getNurses()) { //types opsplitsen
            if(nurse.getType() == 1){
                scheduleType1.add(nurse);
            }
            if(nurse.getType() == 2){
                scheduleType2.add(nurse);
            }
        }
        WeeklySchedule weeklySchedule1 = new WeeklySchedule(scheduleType1); //type 1
        WeeklySchedule weeklySchedule2 = new WeeklySchedule(scheduleType2); //type 2
        string1 = weeklySchedule1.ScheduleToString();
        string2 = weeklySchedule2.ScheduleToString();
        for (int i = 0; i < 4; i++) {
            monthSchedule1 = monthSchedule1 +  string1;
            monthSchedule2 = monthSchedule2 +  string2;
        }
        this.schedule1 =   monthSchedule1;
        this.schedule2 =   monthSchedule2;
    }
    
    public double calcCost (int type){ //toepasbaar op schedule1 en schedule2
        double cost = 0;
        int labourHours = 0;
        String schedule = null;
        double [][] wages = null;
        if(type == 1){
            schedule = schedule1;
            wages = wages1;
        }
        else if (type == 2){
            schedule = schedule2;
            wages = wages2;
        }
        System.out.println("schedule: " + schedule);
        System.out.println("wages: " + wages[0][0] + ","+ wages[0][1] + ","+ wages[1][0] + ","+ wages[1][1]);
        
        //wages
        int counter = 0;
        for (int w = 0; w < 5; w++) {     
            for (int i = 0; i < 6; i++) { //week
                System.out.println(counter);
                if(!schedule.substring(i, i+1).equals("*") && Character.getNumericValue(schedule.charAt(counter)) != 3){
                   cost += wages[0][Character.getNumericValue(schedule.charAt(counter))-1] ; //if type=1, wages[0][0] ,if type=2, wages[0][1]
                   labourHours += shiftHours;
                   System.out.println(" " + cost);
                }
                counter++;
                
            }
            for (int i = 6; i < 8; i++) { //weekend
                System.out.println(counter);
                if(!schedule.substring(i, i+1).equals("*") && Character.getNumericValue(schedule.charAt(counter)) != 3){
                   cost += wages[1][Character.getNumericValue(schedule.charAt(counter))-1] ; //if type=1, wages[1][0] ,if type=2, wages[1][1]
                   labourHours += shiftHours;
                   System.out.println(" " + cost);
                }
                counter++;
                
            }
        }
        //administrative costs
        //cost += fixedAdmCost + labourHours;
        return cost;
    }
 /*   
    public int calcNurseSat (){
        
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
