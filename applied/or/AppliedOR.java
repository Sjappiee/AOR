 
package applied.or;

import java.io.IOException;
import java.util.ArrayList;
import jxl.write.WriteException;

public class AppliedOR {
 
    public static void main(String[] args) throws IOException, WriteException {
        java.util.Locale.setDefault(new java.util.Locale("en", "US"));
    /*    Process process = new Process();
       Department depA = new Department ('A',32);
       process.read_personnel_characteristics('A',32);
       int x;
       for(x=0;x<depA.getAmountNurses();x++){
       System.out.println(process.personelNr[x] + '\n');
       }*/
       
        ExcellReader test = new ExcellReader ();
        test.setInputFile("C:\\TEST AOR\\input 2x12 4-3.xls");
      
//        System.out.println(test.searchFirstRowNurse(3));
//        System.out.println(test.searchLastRowNurse(3));
//        System.out.println("");
//        System.out.println(test.searchFirstRowWorkSchedule(3));
//        System.out.println(test.searchLastRowWorkSchedule(3));
       

//        System.out.println(test.giveNurseNumber(1,14));
//       System.out.println(test.giveBinaryDayPlanning(1,14));
//       System.out.println(test.giveEmploymentRate(1,14));
//       System.out.println(test.giveType(1,14));
//       System.out.println(test.givePref(1,14));
//       System.out.println(test.giveNumbPref(1,14));
       
       
        ArrayList <Nurse> nursesD = new ArrayList <Nurse> (); //moet dan voor alle dptm gedaan worden
        ArrayList <Nurse> workPatternsD = new ArrayList <Nurse> ();
//
        nursesD = test.readAllExceptCyclicSchedule(0);  
        workPatternsD = test.readWorkPatterns(0);
//   
//        MonthlySchedule monthlySchedule = new MonthlySchedule(nursesD,workPatternsD);
//
//        monthlySchedule.fireNurses(1);
//        monthlySchedule.fireNurses(2);
//        monthlySchedule.calcNurseSat(1);
//        monthlySchedule.calcTotalObjectiveFunction();
//        monthlySchedule.patientSatisfaction(2);
//        System.out.println("type1:" + monthlySchedule.getSchedule1());
////        System.out.println("amount nurses: " + monthlySchedule.getAmountNurses1());
//        System.out.println("type2: " + monthlySchedule.getSchedule2());

//        System.out.println("amount nurses: " + monthlySchedule.getAmountNurses2());
//        String schema1 = monthlySchedule.getSchedule1();
//        String schema2 = monthlySchedule.getSchedule2();
//        ArrayList <Nurse> nursesType1 = new ArrayList<Nurse> ();
//        nursesType1 = monthlySchedule.getNursesType1();
//        ArrayList <Nurse> nursesType2 = new ArrayList<Nurse> ();
//        nursesType2 = monthlySchedule.getNursesType2();
//        
//        monthlySchedule.patientSatisfaction(1);
//        monthlySchedule.patientSatisfaction(2);

        
        Population population = new Population (nursesD,workPatternsD); System.out.println(""); System.out.println("");System.out.println("Optimal method");
        population.giveOptimal();System.out.println("EIND");
            
//        ExcellWriter PrintOplossing = new ExcellWriter();
//        PrintOplossing.writeScheduleToExcel(nursesType1, nursesType2, schema1, schema2, 0);
//        PrintOplossing.writeShiftToExcel(nursesType1, nursesType2, schema1, schema2, 0);
        
        
    }
}


     

