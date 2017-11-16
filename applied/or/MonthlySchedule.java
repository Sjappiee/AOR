package applied.or;

import java.util.ArrayList;

public class MonthlySchedule {
    private String schedule1; //type1
    private String schedule2; //type2
    
    public MonthlySchedule (ArrayList <Nurse> n){ //in main methodes uit WeeklySchedule toep, dan hier de nurse lijst met assigned patterns geven
        String monthSchedule1 = "";
        String monthSchedule2 = "";
        for (int i = 0; i < 4; i++) {
            WeeklySchedule weeklySchedule = new WeeklySchedule(n); //beide types
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
            monthSchedule1 = monthSchedule1 + "*" + string1;
            monthSchedule2 = monthSchedule2 + "*" + string2;
        }
        this.schedule1 =   monthSchedule1;
        this.schedule2 =   monthSchedule2;
    }
    
    public int calcCost (){
        
    }
    
    public int calcNurseSat (){
        
    }
    
    public int calcPatientSat (){
        
    }

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
