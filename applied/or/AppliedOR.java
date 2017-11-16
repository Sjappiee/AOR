 
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
      

      Schedule testSchedule = new Schedule (nursesD,workPatternsD);

//      int [][]prefs = testSchedule.prefScoreCalculation();
//        for (int i = 0; i < nursesD.size(); i++) {
//            for (int j = 0; j < workPatternsD.size(); j++) {
//                System.out.print(prefs[j][i] + " ");
      int [][] temp = testSchedule.prefScoreCalculation();      //ALTIJD, [workpattern][nurse]
      testSchedule.schedulingProcess();
      
      Population testPop = new Population (testSchedule);
      testPop.ScheduleToString();
      
      
//        for (int i = 0; i < 32; i++) {
//            for (int j = 0; j < 32; j++) {
//                System.out.print(temp[j][i] + " ");
//            }
//            System.out.println("");
//        }

      testSchedule.addaptSchedule ();
        for (Nurse pattern : workPatternsD) {
            System.out.println(pattern.getNr());
            System.out.println(pattern.BinaryPlanningToString ());
        }
      
      //testSchedule.schedulingProcess();
//      int [] rates = testSchedule.calcScheduleRateAmounts (1);
//        for (int i = 0; i < 4; i++) {
//            System.out.println(rates[i]);
//        }
    /*  for(Nurse nurse : testSchedule.getNurses()){
        for (int i = 0; i < 47; i++) {
            for (int j = 0; j < 47; j++) {
                System.out.println(nurse.getNr());
                System.out.print(nurse.getBinaryDayPlanning()[j][i] + " ");
            }
            System.out.println("");
        }    
      }*/
 


    }
}

     

