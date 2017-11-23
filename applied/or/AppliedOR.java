 
package applied.or;

import java.io.IOException;
import java.util.ArrayList;

public class AppliedOR {
 
    public static void main(String[] args) throws IOException {
        java.util.Locale.setDefault(new java.util.Locale("en", "US"));
    /*    Process process = new Process();
       Department depA = new Department ('A',32);
       process.read_personnel_characteristics('A',32);
       int x;
       for(x=0;x<depA.getAmountNurses();x++){
       System.out.println(process.personelNr[x] + '\n');
       }*/
       
        ExcellReader test = new ExcellReader ();
        test.setInputFile("C:\\TEST AOR\\EchteTest.xls");
      
      /*  System.out.println(test.searchFirstRowNurse(3));
        System.out.println(test.searchLastRowNurse(3));
        System.out.println("");
        System.out.println(test.searchFirstRowWorkSchedule(3));
        System.out.println(test.searchLastRowWorkSchedule(3));
       */

       /*test.giveNurseNumber(1,14);
       test.giveBinaryDayPlanning(1,14);
       test.giveEmploymentRate(1,14);
       test.giveType(1,14);
       test.givePref(1,14);
       test.giveNumbPref(1,14);
       */    
        ArrayList <Nurse> nursesD = new ArrayList <Nurse> (); //moet dan voor alle dptm gedaan worden
        ArrayList <Nurse> workPatternsD = new ArrayList <Nurse> ();
      
        nursesD = test.readAllExceptCyclicSchedule(0);  
        workPatternsD = test.readWorkPatterns(0);
//        for (Nurse nurse : nursesD) {
//            System.out.println(nurse);
//        }


      /*  System.out.println("ALL PROCESSES CHECK");
      WeeklySchedule testSchedule = new WeeklySchedule (nursesD,workPatternsD);
      testSchedule.allProcesses();
      
        WeeklySchedule testSchedule = new WeeklySchedule (nursesD,workPatternsD);
        testSchedule.allProcesses();
        for (Nurse nurse : testSchedule.getNurses()) {
            System.out.println(nurse.toString());
        }*/

//        testSchedule.scheduleToString();
//      int [][]prefs = testSchedule.prefScoreCalculation();
//        for (int i = 0; i < nursesD.size(); i++) {
//            for (int j = 0; j < workPatternsD.size(); j++) {
//                System.out.print(prefs[j][i] + " ");
//      int [][] temp = testSchedule.prefScoreCalculation();      //ALTIJD, [workpattern][nurse]
//      testSchedule.schedulingProcess();
//            
      
//        for (int i = 0; i < 32; i++) {
//            for (int j = 0; j < 32; j++) {
//                System.out.print(temp[j][i] + " ");
//            }
//            System.out.println("");
//        }
//
       // testSchedule.addaptSchedule ();
//        for (Nurse pattern : workPatternsD) {
//            System.out.println(pattern.getNr());
//            System.out.println(pattern.BinaryPlanningToString ());
//        }
//        
//        System.out.println("EINDE MAIN DING");
//        System.out.println("VANAF NU NIEUWE TESTCODE");

       // testSchedule.recombineQuarterSchedules();

//
//        System.out.println("EINDE TESTEN STEHAN");
        
//        testSchedule.addaptSchedule ();
//        for (Nurse pattern : workPatternsD) {
//            System.out.println(pattern.getNr());
//            System.out.println(pattern.BinaryPlanningToString ());
//        }
//     
//
//        System.out.println("TINE HAAR GEPRUTS");
//        System.out.println("");
 //       testSchedule.hireNurses();
//        for(Nurse nurse : nursesD){
//           System.out.println(nurse.toString());
//        }

//        testSchedule.allProcesses ();
        
        MonthlySchedule monthlySchedule = new MonthlySchedule(nursesD,workPatternsD);
        System.out.println("type1:" + monthlySchedule.getSchedule1());
        System.out.println("amount nurses: " + monthlySchedule.getAmountNurses1());
        System.out.println("type2: " + monthlySchedule.getSchedule2());
        System.out.println("amount nurses: " + monthlySchedule.getAmountNurses2());
//        System.out.println(monthlySchedule.calcCost(1));
//       
//       monthlySchedule.calcNurseSat(2);
    }
}


     

