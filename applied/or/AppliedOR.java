
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
       
        System.out.println("KAK");
       
//       test.giveNurseNumber(0,14);
//       test.giveBinaryDayPlanning(0,14);
//       test.giveEmploymentRate(0,4);
//       test.giveType(0,14);
//       test.givePref(0,14);
//       test.giveNumbPref(0,4);
//       
       ArrayList <Nurse> nursesD = new ArrayList <Nurse> (); //moet dan voor alle dptm gedaan worden
       ArrayList <Nurse> workPatternsD = new ArrayList <Nurse> ();
      nursesD = test.readAllExceptCyclicSchedule(3);
      workPatternsD = test.readWorkPatterns(3);
      
      int[][] t = new int [2][7];
      Nurse nurse = new Nurse();
      nurse = workPatternsD.get(5);
      t=nurse.getBinaryDayPlanning();
        System.out.println(t[1][5]);
      
        Schedule testSchedule = new Schedule (nursesD,workPatternsD);
        int [][] prefScores = testSchedule.prefScoreCalculation();
        System.out.println(prefScores[0][0]);
        System.out.println(prefScores[2][2]);
        System.out.println(prefScores[3][3]);
        System.out.println(prefScores[12][12]);
    }

    } 

