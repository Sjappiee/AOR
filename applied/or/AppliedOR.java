

package applied.or;

import java.io.IOException;
import java.util.ArrayList;
import jxl.write.WriteException;

public class AppliedOR {
 
    public static void main(String[] args) throws IOException, WriteException {
        java.util.Locale.setDefault(new java.util.Locale("en", "US"));
//        int [] percantagesNotCyclic = {100,75,50,25,0};
//        int [] percentagesRandomWeekly = {100,50,0};
//        int [] percentagesSubrandomWeekly = {100,50,0};

        int [] percantagesNotCyclic = {100,75,50,25,0};
        int [] percentagesRandomWeekly = {50};
        int [] percentagesSubrandomWeekly = {50};
        int [] departments = {0,1,2,3};
    /*    Process process = new Process();
       Department depA = new Department ('A',32);
       process.read_personnel_characteristics('A',32);
       int x;
       for(x=0;x<depA.getAmountNurses();x++){
       System.out.println(process.personelNr[x] + '\n');
       }*/
       
        ExcellReader testA = new ExcellReader ();
        testA.setInputFile("C:\\TEST AOR\\input 2x12 4-3.xls");
                
        ExcellReader testB = new ExcellReader ();
        testB.setInputFile("C:\\TEST AOR\\input 2x12 4-3.xls");
                
        ExcellReader testC = new ExcellReader ();
        testC.setInputFile("C:\\TEST AOR\\input 2x12 4-3.xls");
               
        ExcellReader testD = new ExcellReader ();
        testD.setInputFile("C:\\TEST AOR\\input 2x12 4-3.xls");
      

 
        double minTotal = 10000000;
        double minCostTotal = 10000000;
        double minNurseSatTotal = 10000000;
        double minPatientSatTotal = 10000000;
        int optimalPercantageNotCyclic=0;
        int optimalPercentageRandomWeekly=0;
        int optimalPercentageSubrandomWeekly=0;
        
        double [] [] Cost = new double [10][4];
        double [] [] NurseSat = new double [10][4];
        double [] [] PatientSat = new double [10][4];
        double [] [] Total = new double [10][4];

        ArrayList <String> schedule = new ArrayList<>();
        int i =0;
        String [] [] SchedulesOutput = new String [2][4]; //per department
        for (int percantageNotCyclic : percantagesNotCyclic) {
               for (int percentageRandomWeekly : percentagesRandomWeekly){
                   for(int percentageSubrandomWeekly : percentagesSubrandomWeekly){
                      double totalScore=0;
                      double totalCost = 0;
                      double totalNurseSat = 0;
                      double totalPatientSat = 0;
                      MonthlySchedule [] schedules = new MonthlySchedule [4]; //per department
                      for(int department : departments){
                          ExcellReader test =new ExcellReader();
                          test.setInputFile("C:\\TEST AOR\\input 2x12 4-3.xls");
                        ArrayList <Nurse> nurses = new ArrayList <Nurse> (); //moet dan voor alle dptm gedaan worden
                        ArrayList <Nurse> workPatterns = new ArrayList <Nurse> ();
                        nurses = test.readAllExceptCyclicSchedule(department);  
                        workPatterns = test.readWorkPatterns(department);
                        Population population = new Population (nurses,workPatterns,percentageRandomWeekly,percentageSubrandomWeekly,percantageNotCyclic); System.out.println(""); System.out.println("");System.out.println("Optimal method");
                        population.giveOptimal();
                        totalScore += population.getOptimalSchedule().getOptimalTotalScore();
                          System.out.println(population.getOptimalSchedule().getOptimalTotalScore());
                        totalCost += population.getOptimalSchedule().getOptimalCost();
                        totalNurseSat += population.getOptimalSchedule().getOptimalNurseSat();
                        totalPatientSat += population.getOptimalSchedule().getOptimalPatientSat();
                        schedules [department] = population.giveOptimal();
                        
                        Cost [i] [department] = population.getOptimalSchedule().getOptimalCost();
                        NurseSat [i][department] = population.getOptimalSchedule().getOptimalNurseSat();
                        PatientSat [i][department] = population.getOptimalSchedule().getOptimalPatientSat();
                        Total [i][department] = population.getOptimalSchedule().getOptimalTotalScore();
                        
                      } 
                      if(totalScore < minTotal){
                        optimalPercantageNotCyclic = percantageNotCyclic;
                        optimalPercentageRandomWeekly = percentageRandomWeekly;
                        optimalPercentageSubrandomWeekly = percentageSubrandomWeekly;
                        minTotal = totalScore;
                        minCostTotal = totalCost;
                        minNurseSatTotal = totalNurseSat;
                        minPatientSatTotal = totalPatientSat;
                              for (int j = 0; j < 4; j++) {
                              SchedulesOutput[0][j] = schedules[j].getSchedule1();
                              SchedulesOutput[1][j] = schedules[j].getSchedule2();
                          }

                      }
                    i++;  
                   }
               }
        }
        
        
        for (int j = 0; j < 4; j++) {
            System.out.println("FINAL OUTPUT DEPARTMENT " + j);
            System.out.println("COST");
            for (int k = 0; k < 10; k++) {
             System.out.println(Cost [k][j]);   
            }
            System.out.println("NURSESSAT");
            for (int k = 0; k < 10; k++) {
             System.out.println(NurseSat [k][j]);   
            }
            System.out.println("Patient Sat");
            for (int k = 0; k < 10; k++) {
             System.out.println(PatientSat [k][j]);   
            }
            System.out.println("Total " );
            for (int k = 0; k < 10; k++) {
             System.out.println(Total [k][j]);   
            }           
        }
        
        System.out.println("WINNAAR ");
        System.out.println("Not cyclic percentage: " + optimalPercantageNotCyclic);
        System.out.println("optimalPercentageRandomWeekly: " + optimalPercentageRandomWeekly);
        System.out.println("optimalPercentageSubrandomWeekly: " + optimalPercentageSubrandomWeekly);
        System.out.println("minTotal: " + minTotal);
        System.out.println("minCostTotal " + minCostTotal);
        System.out.println("minNurseSatTotal " + minNurseSatTotal);
        System.out.println("minPatientSatTotal " + minPatientSatTotal);
        
        for (int j = 0; j < 4; j++) {
            for (int k = 0; k < 2; k++) {
                System.out.println("DEPARTEMENET " + j );
                System.out.println("Type " + k);
                System.out.println(SchedulesOutput[k][j]);
            }
        }
        
        
//     
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
    
        public static ArrayList<Nurse> cloneList(ArrayList<Nurse> list) {
    ArrayList<Nurse> cloneList = new ArrayList<Nurse>(list.size());
    for (Nurse item : list){
        Nurse temp = item.clone();
        cloneList.add(temp);
    }
    return cloneList;
}
        
}


     

