package applied.or;

import java.util.ArrayList;
import java.util.Random;

public class MonthlySchedule {
    private String schedule1; //type1
    private double [][] wages1 = {{324,240},{437.4,324}}; //[weekday,weekend][s1=nacht,s2=dag (12h)]  !!SHIFTSYSTEM
    private String schedule2; //type2
    private double [][] wages2 = {{243,180},{328.05,243}}; //[weekday,weekend][s1,s2]  !!SHIFTSYSTEM
    private double fixedAdmCost = 100000 * 2 * 28;  //28dagen, 12h:100000, 9h:60000   * amount shifts (2or3)      !!SHIFTSYSTEM
    private int shiftHours = 12;  //!!SHIFTSYSTEM
    private int amountNurses1 = 0;
    private int amountNurses2 = 0;
    private ArrayList <Nurse> nursesType1 = new ArrayList<Nurse> ();
    private ArrayList <Nurse> nursesType2 = new ArrayList<Nurse> ();
    private int [] objectiveFunctions = new int [3];
    private double [] objectiveWeights = {0.5,0.5,0};
    
    public MonthlySchedule (ArrayList<Nurse> nurses,ArrayList<Nurse> workPatterns){ // weekly schedule waar alle nurses al assigned zijn aan patterns
        int [] [] amountPerTypePerWeek = new int [2][4]; //type 1 and 2
        String [] [] monthScheduleArray = new String [2][4]; // nodig voor verschillen in aantal nurses/week
        String [] monthSchedule = new String [2];
        WeeklySchedule temp1 = null;
        WeeklySchedule temp2 = null;
        WeeklySchedule weeklySchedule = new WeeklySchedule (nurses,workPatterns);
        
        //week1,2,3,4
        for (int i = 0; i < 4; i++) {
            if(i==0){ //week 1
                weeklySchedule.allProcesses();
                amountPerTypePerWeek [0][i] = weeklySchedule.amountWithType(weeklySchedule.getNurses())[0];
                amountPerTypePerWeek [1][i] = weeklySchedule.amountWithType(weeklySchedule.getNurses())[1];
                //opsplitsing per type
                ArrayList <Nurse> scheduleType1 = new ArrayList <Nurse>();
                ArrayList <Nurse> scheduleType2 = new ArrayList <Nurse>();
                for (Nurse nurse : weeklySchedule.getNurses()) { //types opsplitsen
                    if(nurse.getType() == 1) scheduleType1.add(nurse);
                    if(nurse.getType() == 2) scheduleType2.add(nurse);
                }
                temp1 = new WeeklySchedule(scheduleType1); //type 1
                temp2 = new WeeklySchedule(scheduleType2); //type 2
                monthScheduleArray [0][i] = temp1.ScheduleToString();
                monthScheduleArray [1][i] = temp2.ScheduleToString();
            }
            else{   //week 2,3,4
                int changeOrNot = randomBoolean(0);  //0%kans dat voor week 2 een nieuw schema word opgesteld
                if(changeOrNot == 1){
                    weeklySchedule.resetBinarySchedule();
                    weeklySchedule.schedulingProcess();
                }
                amountPerTypePerWeek [0][i] = weeklySchedule.amountWithType(weeklySchedule.getNurses())[0];
                amountPerTypePerWeek [1][i] = weeklySchedule.amountWithType(weeklySchedule.getNurses())[1];
                //opsplitsing per type
                ArrayList <Nurse> scheduleType1 = new ArrayList <Nurse>();
                ArrayList <Nurse> scheduleType2 = new ArrayList <Nurse>();
                for (Nurse nurse : weeklySchedule.getNurses()) { //types opsplitsen
                    if(nurse.getType() == 1) scheduleType1.add(nurse);
                    if(nurse.getType() == 2) scheduleType2.add(nurse);
                }
                temp1 = new WeeklySchedule(scheduleType1); //type 1
                temp2 = new WeeklySchedule(scheduleType2); //type 2
                monthScheduleArray [0][i] = temp1.ScheduleToString();
                monthScheduleArray [1][i] = temp2.ScheduleToString();
            }
            
        }
        //check differences in amount nurses / week
        int [] amount = new int [2];
        for (int i = 0; i < 2; i++) {
            int max = 0;
            for (int j = 0; j < 4; j++) {   //find max amount of nurses in a week
                if(amountPerTypePerWeek[i][j] > max) max = amountPerTypePerWeek[i][j]; 
            }
            amount [i] = max;
            for (int j = 0; j < 4; j++) { // add empty nurse schedules to the weeks with les nurses than max
                if(amountPerTypePerWeek[i][j] < max){
                    for (int k = 0; k < max - amountPerTypePerWeek[i][j]; k++) {
                        monthScheduleArray[i][j] += "0000000*";
                    }
                }
                if(j==0) monthSchedule [i] = monthScheduleArray[i][j]; 
                else monthSchedule [i] += monthScheduleArray[i][j];
            }
        }
        this.amountNurses1 = amount[0];
        this.amountNurses2 = amount[1];
        this.schedule1 =   monthSchedule [0];
        this.schedule2 =   monthSchedule [1];
        this.nursesType1 = temp1.getNurses();
        this.nursesType2 = temp2.getNurses();
    }
    
    public int randomBoolean (int probOnOne){
        int randomBit = 0;
        float prob1 = probOnOne/10;
        int temp = new Random().nextInt(10);
        if(temp < prob1){
            randomBit = 1;
        }
        return randomBit;
    }
    
    
    public double calcCost (int type){ //toepasbaar op schedule1 en schedule2
        double cost = 0;
        int labourHoursWeek = 0;
        int labourHoursWeekend = 0;
        String schedule = null;
        double [][] wages = null;
        int amountNurses = 0;
        if(type == 1){
            schedule = schedule1;
            wages = wages1;
            amountNurses = amountNurses1;
        }
        else if (type == 2){
            schedule = schedule2;
            wages = wages2;
            amountNurses = amountNurses2;
        }
        
        //wages
        int counter = 0;
        for (int w = 0; w < 4; w++) {  
            for (int n = 0; n < amountNurses; n++) { // nurses
                for (int i = 0; i < 5; i++) { //week
                    if(Character.getNumericValue(schedule.charAt(counter)) > 0){
                        cost += wages[0][Character.getNumericValue(schedule.charAt(counter))-1] ; //if type=1, wages[0][0] ,if type=2, wages[0][1]
                        labourHoursWeek += shiftHours;
                    }
                     counter++;
                }
                for (int i = 5; i < 8; i++) { //weekend
                    if(Character.getNumericValue(schedule.charAt(counter)) > 0 ){
                        cost += wages[1][Character.getNumericValue(schedule.charAt(counter))-1] ; //if type=1, wages[1][0] ,if type=2, wages[1][1]
                        labourHoursWeekend += shiftHours;
                    }
                    counter++;
                } 
            }
        }
        //administrative costs
        cost += fixedAdmCost + labourHoursWeek + labourHoursWeekend;
        return cost;
    }

    public int calcNurseSat (int type){ //later opdelen in verschillende methodes. Don't be a Tine
        ArrayList <String> monthScheduleNurse = new ArrayList <String> ();
        int breakFreeDaysPunishment = 5;
        int lowerWorkratePunishment = 5;
        int changeInShiftsPunishment = 5;
        int totalScore = 0;
        ArrayList <Nurse> usedNurses = new ArrayList <Nurse> ();
        if (type == 1) {usedNurses = this.nursesType1;}
        else {usedNurses = this.nursesType2;}
        
        for (Nurse usedNurse : usedNurses) {
            System.out.println(usedNurse);
        }
        
        System.out.println(usedNurses.size());
        for(Nurse nurse: usedNurses){
            System.out.println(nurse);
        }    
            
        
//        for (int i = 0; i < 4; i++) {
//            System.out.println(monthScheduleNurse.get(i));
//        }
        
        //GIGA FOR LUS VOOR ALLE NURSES!!! OM TOTALE SCORE TE BERKENEN
        //eerst degelijk berekenen oor 1 nurse
        for (int k = 0; k < usedNurses.size(); k++) {
            monthScheduleNurse = schedulesSpecificNurse (k,type);
            System.out.println("METHODE VOOR ONDERBREKINGEN ");
        //berekening aantal onderbrekingen! Getest en OK
            int amountOfInteruptions=0; //onderbreking = '101' of '202' OF opt einde '10' en maandag ook '1'
            for (int i = 0; i < monthScheduleNurse.size(); i++) { 
                String shift = monthScheduleNurse.get(i);
//            System.out.println(shift);
                if(shift.substring(5).equalsIgnoreCase("10") && shift.charAt(0) == '1'){ //in het uitzonderlijke geval van bv '1001110' Ook onderbreking (zaterdag-zondag-maandag)
                    amountOfInteruptions++;
                }
                if (shift.substring(0, 1).equalsIgnoreCase("01") && shift.charAt(6) == '1'){ //in het uitzonderlijke gevan van bv '0111001' Ook onderbreking (zondag-maandag-dinsdag)
                    amountOfInteruptions++;
                }
                if(shift.substring(5).equalsIgnoreCase("20") && shift.charAt(0) == '2'){ //in het uitzonderlijke geval van bv '1001110' Ook onderbreking (zaterdag-zondag-maandag)
                    amountOfInteruptions++;
                }
                if (shift.substring(0, 1).equalsIgnoreCase("02") && shift.charAt(6) == '2'){ //in het uitzonderlijke gevan van bv '0111001' Ook onderbreking (zondag-maandag-dinsdag)
                    amountOfInteruptions++;
                }
                int index = shift.indexOf("101"); //kijken of er 101 of 202 is in weekschema
                int count = 0;
                while (index != -1) {
                    count++;
                    shift = shift.substring(index + 1);
                    index = shift.indexOf("101");
                }
            
                int index2 = shift.indexOf("202"); //kijken of er 101 of 202 is in weekschema
                int count2 = 0;
                while (index2 != -1) {
                    count2++;
                    shift = shift.substring(index2 + 1);
                    index2 = shift.indexOf("202");
                }
                amountOfInteruptions+= count;
                amountOfInteruptions+= count2;
            }
            System.out.println("total interuptions " + amountOfInteruptions);
            System.out.println("METHODE VOOR EMPLOYMENT RATES");
            //bekijken of elke nurse wel even veel werkt als ze wil. Zoniet: penalty score!!

            int differenceWorkingDays = 0;
            for (int i = 0; i < monthScheduleNurse.size(); i++){ //met nurse x, type x alle 4 de weken doorlopen!
                int counter=0;
                String shift = monthScheduleNurse.get(i);
                for (int j = 0; j < shift.length(); j++) { //aantal werkdagen in een week berekenen.
                    if (shift.charAt(j) != '0'){
                        counter++;
                    }
                }
                differenceWorkingDays += (int) ((usedNurses.get(k).getEmploymentRate()*4) - counter);
            }
        
            System.out.println("Total working days less than nurse wanted: " + differenceWorkingDays);
            System.out.println("METHODE VOOR SHIFTWISSELS ");
            int AmountOfShiftChanges=0;
        //aantal keer een shiftwissel in dezelfde week (berekenen hoeveel keer 1 in een week, hoeveel keer 2 en het min daarvan is #shiftwissels
            for (int i = 0; i < monthScheduleNurse.size(); i++) {
                
                String shift = monthScheduleNurse.get(i);
                int index = shift.indexOf("1"); //kijken of er 101 of 202 is in weekschema
                int count = 0;
                while (index != -1) {
                count++;
                shift = shift.substring(index + 1);
                index = shift.indexOf("1");
                }
                
                int index2 = shift.indexOf("2"); //kijken of er 101 of 202 is in weekschema
                int count2 = 0;
                while (index2 != -1) {
                count2++;
                shift = shift.substring(index2 + 1);
                index2 = shift.indexOf("2");
                }
                AmountOfShiftChanges  += Integer.min(count, count2);
            }
            System.out.println("Amount of shift changes" + AmountOfShiftChanges);

            int ScoreNurse = breakFreeDaysPunishment*amountOfInteruptions + differenceWorkingDays*lowerWorkratePunishment +AmountOfShiftChanges*changeInShiftsPunishment;
            System.out.println(ScoreNurse);
            totalScore += ScoreNurse;
            System.out.println(totalScore);
        }
        
//       //nu nog vermenigvudigen met alle specifieke wensen!
//    eerst vermeningvudigen met hun schema X hun specifieke monthly pref
//    dan ook nog nog met de verschillende regels: 
//        vrije dagen na elkaar? OK
//        alle dagen werken dat je wil? Dus schema = ER?  OK 
//        veranderen in shiften?
        return totalScore;
    }
    
    
    
    
    
    public ArrayList <String> schedulesSpecificNurse (int nurseNumber, int type) { //geeft de 4 weekschema's van de nurse die ingegeven is
        int amountOfNurses;
        String schedule = null;
        ArrayList <String> temp = new ArrayList <String> ();
        
        if (type == 1)
        {
            amountOfNurses = this.amountNurses1;
            schedule = schedule1;
        }
        else 
        {
           amountOfNurses = this.amountNurses2;
           schedule = this.schedule2;
        }

        for (int i = 0; i < 4; i++) {
            temp.add(schedule.substring(nurseNumber*8 + i*8*(amountOfNurses), nurseNumber*8 + i*(amountOfNurses)*8+7));
            System.out.println(schedule.substring(nurseNumber*8 + i*8*(amountOfNurses), nurseNumber*8 + i*(amountOfNurses)*8+7));

        }
        
        return temp;
    }
//    public int calcPatientSat (){
//        
//    }
    public int [] calcObjectiveFunctions (int type) {
        objectiveFunctions [0] = (int) calcCost (type);
        objectiveFunctions [1] = calcNurseSat (type);
        objectiveFunctions [2] = objectiveFunctions [1]; // VOORLOPIG
        return objectiveFunctions;
    }
    
    public double calcTotalObjectiveFunction (int type){
        calcObjectiveFunctions(type);
        double total = objectiveWeights[0]*objectiveFunctions[0] + objectiveWeights[1]*objectiveFunctions[1] + objectiveWeights[2]*objectiveFunctions[2];
        return total;
    }
    
    
    public String getSchedule1() {
        return schedule1;
    }

    public void setSchedule1(String schedule1) {
        this.schedule1 = schedule1;
    }

    public String getSchedule2() {
        return schedule2;
    }
    public void setSchedule2(String schedule2) {
        this.schedule2 = schedule2;
    }

    public int getAmountNurses1() {
        return amountNurses1;
    }

    public void setAmountNurses1(int amountNurses1) {
        this.amountNurses1 = amountNurses1;
    }

    public int getAmountNurses2() {
        return amountNurses2;
    }

    public void setAmountNurses2(int amountNurses2) {
        this.amountNurses2 = amountNurses2;
    }
    
    
    
}
