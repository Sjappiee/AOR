

package applied.or;

import java.io.IOException;
import java.util.ArrayList;
import jxl.write.WriteException;

public class AppliedOR {
 
    public static void main(String[] args) throws IOException, WriteException {
        java.util.Locale.setDefault(new java.util.Locale("en", "US"));

        int [] percantagesNotCyclic = {100,75,50,25,0};
        int [] percentagesRandomWeekly = {50};
        int [] percentagesSubrandomWeekly = {50};
        int [] departments = {0,1,2,3};
        int amountCombinations = 50;
        ArrayList<Nurse>[] nurseLists = (ArrayList<Nurse>[])new ArrayList[4];
        ArrayList<Nurse>[] patternsList = (ArrayList<Nurse>[])new ArrayList[4];
        
        for(int dep : departments){
            ExcellReader test =new ExcellReader();
            test.setInputFile("C:\\TEST AOR\\input 3x9 5-2.xls");
            nurseLists[dep] = test.readAllExceptCyclicSchedule(dep);  
            patternsList[dep] = test.readWorkPatterns(dep);
        }
        

        double minTotal = 10000000;
        double minCostTotal = 10000000;
        double minNurseSatTotal = 10000000;
        double minPatientSatTotal = 10000000;
        int optimalPercantageNotCyclic=0;
        int optimalPercentageRandomWeekly=0;
        int optimalPercentageSubrandomWeekly=0;
        MonthlySchedule [] optimalMonth = new MonthlySchedule [4];
        
        double [] [] Cost = new double [amountCombinations][4];
        double [] [] NurseSat = new double [amountCombinations][4];
        double [] [] PatientSat = new double [amountCombinations][4];
        double [] [] Total = new double [amountCombinations][4];
        String [][] Schedules1 = new String [amountCombinations][4];
        String [][] Schedules2 = new String [amountCombinations][4];

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
                        ArrayList <Nurse> nurses = cloneList(nurseLists[department]); //moet dan voor alle dptm gedaan worden
                        ArrayList <Nurse> workPatterns = cloneList(patternsList[department]);
                        Population population = new Population (nurses,workPatterns,percentageRandomWeekly,percentageSubrandomWeekly,percantageNotCyclic); 
//                        System.out.println(""); System.out.println("");System.out.println("Optimal method");
                        population.giveOptimal();
                        totalScore += population.getOptimalSchedule().getOptimalTotalScore();
                        totalCost += population.getOptimalSchedule().getOptimalCost();
                        totalNurseSat += population.getOptimalSchedule().getOptimalNurseSat();
                        totalPatientSat += population.getOptimalSchedule().getOptimalPatientSat();
                        schedules [department] = population.giveOptimal();
                        
                        Cost [i] [department] = population.getOptimalSchedule().getOptimalCost();
                        NurseSat [i][department] = population.getOptimalSchedule().getOptimalNurseSat();
                        PatientSat [i][department] = population.getOptimalSchedule().getOptimalPatientSat();
                        Total [i][department] = population.getOptimalSchedule().getOptimalTotalScore();
                        Schedules1 [i][department] = population.getOptimalSchedule().getSchedule1();
                        Schedules2 [i][department] = population.getOptimalSchedule().getSchedule2();
                        
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
                              optimalMonth [j] = schedules [j];
                          }
                      }
                    i++;
                       System.out.println(i);
                   }
               }
        }
        
        
        for (int j = 0; j < 4; j++) {
            System.out.println("FINAL OUTPUT DEPARTMENT " + j);
            System.out.println("COST");
            for (int k = 0; k < amountCombinations; k++) {
             System.out.println(Cost [k][j]);   
            }
            System.out.println("NURSESSAT");
            for (int k = 0; k < amountCombinations; k++) {
             System.out.println(NurseSat [k][j]);   
            }
            System.out.println("Patient Sat");
            for (int k = 0; k < amountCombinations; k++) {
             System.out.println(PatientSat [k][j]);   
            }
            System.out.println("Total " );
            for (int k = 0; k < amountCombinations; k++) {
             System.out.println(Total [k][j]);   
            }           
            
            String [] [] Schedules = new String [amountCombinations][4];
            for (int k = 0; k < 2; k++) {
                System.out.println("Schedule of Type: " + k);
                if(k==0)Schedules = Schedules1;
                if(k==1)Schedules = Schedules2;
                for (int l = 0; l < amountCombinations; l++) {
                    System.out.println(Schedules[l][j]);
                }
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
           
            ArrayList<Nurse> Nurses1 = optimalMonth[j].getNursesType1();
            ArrayList<Nurse> Nurses2 = optimalMonth[j].getNursesType2();
            String schema1 = optimalMonth[j].getSchedule1();
            String schema2 = optimalMonth[j].getSchedule2();
            int dep = j;
            ExcellWriter test2 = new ExcellWriter();
            test2.writeShiftToExcel(Nurses1, Nurses2, schema1, schema2, dep);
        }
        
//        
//        ExcellReader test =new ExcellReader();
//        test.setInputFile("C:\\TEST AOR\\input 3x9 5-2.xls");
//        ArrayList <Nurse> nurses = new ArrayList <Nurse> (); //moet dan voor alle dptm gedaan worden
//        ArrayList <Nurse> workPatterns = new ArrayList <Nurse> ();
//        nurses = test.readAllExceptCyclicSchedule(3);  
//        workPatterns = test.readWorkPatterns(3);
////            
//        WeeklySchedule week = new WeeklySchedule(nurses,workPatterns,50,50);   
//
//        week.allProcesses();
//        for(Nurse nurse:week.getNurses()){
//            System.out.println(nurse);
//        }
//
//        MonthlySchedule monthlySchedule = new MonthlySchedule(nurses,workPatterns,50,50,100);
//
//        monthlySchedule.calcTotalObjectiveFunction();
//        System.out.println("type1:" + monthlySchedule.getSchedule1());
//        System.out.println("amount nurses: " + monthlySchedule.getAmountNurses1());
//        System.out.println("type2: " + monthlySchedule.getSchedule2());
//        System.out.println("amount nurses: " + monthlySchedule.getAmountNurses2());

//        Population population = new Population (nurses,workPatterns,  50, 50, 100);
//        
//        System.out.println(population.getOptimalSchedule().getNursesType1());
            
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


     

