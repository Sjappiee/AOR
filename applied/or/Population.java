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
     
    private Schedule startSchedule;
    private ArrayList <String> wholepopulation;

    public Population(Schedule startSchedule) {
        this.startSchedule = startSchedule;
        this.wholepopulation = null;
    }
    
    public String ScheduleToString () {
        String output = "";
        ArrayList <String> temp = new ArrayList <String> (); //temp arrayList maken die exact gelijk is aan de nurses die uit het schedule komen
        for (int i = 0; i < startSchedule.getNurses().size() ; i++) { //alle schedules zitten nu dus in de temp lijst. Nu moeten we deze allemaal aan elkaar plakken, een 'lege week' niet meenemen. werken shift 1 = 1, werken shift 2 = 2, vrij = 3
            temp.add(startSchedule.getNurses().get(i).BinaryPlanningToString());
        }
        
        
        for (int i = 0; i < temp.size(); i++) {
            String firstPart = temp.get(i).substring(0, 7);
            String secondPart = temp.get(i).substring(7, 14);
            
            if (firstPart.equalsIgnoreCase("0000000")) {
                //nu is het deel aangepast naar enkel het deel dat we nodig hebben. Next: codes aanpassen
                String temp1 = secondPart.replace("0", "3");
                String temp2 = temp1.replace("1", "2");
                
                temp.set(i, temp2);
            }
            else {
                String temp1 = firstPart.replace("0", "3");
                temp.set(i, temp1);
            }
        }
       
        
        for (String string : temp) { 
            output += string + "*";
        }
        
        
        System.out.println(output);
        return output;
    }
        
    


    public Schedule getStartSchedule() {
        return startSchedule;
    }

    public ArrayList<String> getWholepopulation() {
        return wholepopulation;
    }

    public void setStartSchedule(Schedule startSchedule) {
        this.startSchedule = startSchedule;
    }

    public void setWholepopulation(ArrayList<String> wholepopulation) {
        this.wholepopulation = wholepopulation;
    }
    
    
}
