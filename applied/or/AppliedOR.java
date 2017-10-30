
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
      
       
       /*
       test.searchFirstRow(1);
       test.searchLastRow(1);
       test.giveNurseNumber(1,14);
       test.giveBinaryDayPlanning(1,14);
       test.giveEmploymentRate(1,14);
       test.giveType(1,14);
       test.givePref(1,14);
       test.giveNumbPref(1,14);
       */
//       
      ArrayList <Nurse> nursesD = new ArrayList <Nurse> (); //moet dan voor alle dptm gedaan worden
      ArrayList <Nurse> workPatternsD = new ArrayList <Nurse> ();
      
      nursesD = test.readAllExceptCyclicSchedule(3);
      workPatternsD = test.readWorkPatterns(1);
        System.out.println(nursesD.get(21).getSpecificPreference(0, 6));
        System.out.println(nursesD.get(21).getSpecificPreference(1, 6));
        System.out.println(nursesD.get(21).getSpecificPreference(2, 6));
        
      int[][] t = new int [2][7];
      Nurse nurse = new Nurse();
        for (Nurse nurse1 : workPatternsD) {
            System.out.println(nurse1);
        }
      nurse = workPatternsD.get(5); 
      t=nurse.getBinaryDayPlanning();
      System.out.println(t[1][5]);

      /*Schedule testSchedule = new Schedule (nursesD,workPatternsD);
      int [][] prefScores = testSchedule.prefScoreCalculation();
      System.out.println(prefScores[0][0]);
      System.out.println(prefScores[2][2]);
      System.out.println(prefScores[3][3]);
      System.out.println(prefScores[12][12]); */
      
    }

    } 

