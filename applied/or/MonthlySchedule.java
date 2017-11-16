package applied.or;

import java.util.ArrayList;

public class MonthlySchedule {
    private ArrayList <Nurse> schedule = new ArrayList <Nurse> ();
    
    public MonthlySchedule (ArrayList <Nurse> n,ArrayList <Nurse> w){
        WeeklySchedule weeklySchedule1 = new WeeklySchedule (n,w);
        WeeklySchedule weeklySchedule2 = new WeeklySchedule (n,w);
        WeeklySchedule weeklySchedule3 = new WeeklySchedule (n,w);
        WeeklySchedule weeklySchedule4 = new WeeklySchedule (n,w);
        //naar string
        //aan elkaar
    }
    
    public int calcCost (){
        
    }
    
    public int calcNurseSat (){
        
    }
    
    public int calcPatientSat (){
        
    }
    
}
