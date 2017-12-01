 
package applied.or;

import java.io.IOException;
import java.util.ArrayList;
import jxl.write.WriteException;

public class AppliedOR {
 
    public static void main(String[] args) throws IOException, WriteException {
        java.util.Locale.setDefault(new java.util.Locale("en", "US"));
        int [] percantagesNotCyclic = {100,75,50,25,0};
        int [] percentagesRandomWeekly = {100,75,50,25,0};
        int [] percentagesSubrandomWeekly = {100,75,50,25,0};
        int [] departments = {0,1,2,3};
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
        
        double minTotaal = 10000000;
        double minCostTotal = 10000000;
        double minNurseSatTotal = 10000000;
        double minPatientSatTotal = 10000000;
        int optimalPercantageNotCyclic;
        int optimalPercentageRandomWeekly;
        int optimalPercentageSubrandomWeekly;
        MonthlySchedule [] optimalSchedules = new MonthlySchedule [4]; //per department
        for (int percantageNotCyclic : percantagesNotCyclic) {
               for (int percentageRandomWeekly : percentagesRandomWeekly){
                   for(int percentageSubrandomWeekly : percentagesSubrandomWeekly){
                      double totalScore=0;
                      double totalCost = 0;
                      double totalNurseSat = 0;
                      double totalPatientSat = 0;
                      MonthlySchedule [] schedules = new MonthlySchedule [4]; //per department
                      for(int department : departments){
                        ArrayList <Nurse> nurses = new ArrayList <Nurse> (); //moet dan voor alle dptm gedaan worden
                        ArrayList <Nurse> workPatterns = new ArrayList <Nurse> ();
                        nurses = test.readAllExceptCyclicSchedule(department);  
                        workPatterns = test.readWorkPatterns(department);
                        Population population = new Population (nurses,workPatterns,percentageRandomWeekly,percentageSubrandomWeekly,percantageNotCyclic); System.out.println(""); System.out.println("");System.out.println("Optimal method");
                        population.giveOptimal();
                        //totalScore += population.getOptimalSchedule().g;
                        totalCost += population.getOptimalSchedule().getOptimalCost();
                        totalNurseSat += population.getOptimalSchedule().getOptimalNurseSat();
                        totalPatientSat += population.getOptimalSchedule().getOptimalPatientSat();
                        schedules [department] = population.giveOptimal();
                      } 
                      if(totalScore < minTotaal){
                        optimalPercantageNotCyclic = percantageNotCyclic;
                        optimalPercentageRandomWeekly = percentageRandomWeekly;
                        optimalPercentageSubrandomWeekly = percentageSubrandomWeekly;
                        minTotal = totalScore;
                        minCostTotal = totalCost;
                        minNurseSatTotal = totalNurseSat;
                        minPatientSatTotal = totalPatientSat;
                          for (int i = 0; i < 4; i++) { //per department
                              optimalSchedules = schedules;
                          }
                        
                      }
                      
                   }
               }
        }
     
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

        
            
//        ExcellWriter PrintOplossing = new ExcellWriter();
//        PrintOplossing.writeScheduleToExcel(nursesType1, nursesType2, schema1, schema2, 0);
//        PrintOplossing.writeShiftToExcel(nursesType1, nursesType2, schema1, schema2, 0);
        
        
    }
}


     

