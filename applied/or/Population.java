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
     
    private MonthlySchedule startSchedule;
    private ArrayList <String> wholepopulation;

    public Population(MonthlySchedule startSchedule) {
        this.startSchedule = startSchedule;
        this.wholepopulation = null;
    }
    
    //GA komt hier
    
 
    public MonthlySchedule getStartSchedule() {
        return startSchedule;
    }

    public ArrayList<String> getWholepopulation() {
        return wholepopulation;
    }

    public void setStartSchedule(MonthlySchedule startSchedule) {
        this.startSchedule = startSchedule;
    }

    public void setWholepopulation(ArrayList<String> wholepopulation) {
        this.wholepopulation = wholepopulation;
    }
    
    
}
