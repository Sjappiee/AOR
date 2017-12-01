package applied.or;

import java.util.ArrayList;
import java.util.Random;

public class MonthlySchedule {
    int [] rateInDays = {4,3,2,1}; //hangt af van SHIFTSYSTEM !!! calcNurseSat en getShiftTypeFromString AANPASSEN
    private double [][] wages1 = {{324,240},{437.4,324}}; //[weekday,weekend][s1=nacht,s2=dag (12h)]  !!SHIFTSYSTEM
    private double [][] wages2 = {{243,180},{328.05,243}}; //[weekday,weekend][s1,s2]  !!SHIFTSYSTEM
    private double fixedAdmCost = 60000 * 2 * 28;  //28dagen, 12h:100000, 9h:60000   * amount shifts (2or3)      !!SHIFTSYSTEM
    private int shiftHours = 12;  //!!SHIFTSYSTEM
    int amountShifts = 2; //of 3
    
    float [] rates = {(float)1.0,(float)0.75,(float)0.50,(float)0.25};    
    private String schedule1; //type1
    private String schedule2; //type2
    private int amountNurses1 = 0;
    private int amountNurses2 = 0;
    private ArrayList <Nurse> nursesType1 = new ArrayList<Nurse> ();
    private ArrayList <Nurse> nursesType2 = new ArrayList<Nurse> ();
    private int [][] objectiveFunctions = new int [2][3]; //per type, 3obj functions
    private double [] objectiveWeights = {0.25,0.6,0.15}; //cost, nurse, patient
    private int optimalCost = 0;
    private int optimalNurseSat = 0;
    private int optimalPatientSat = 0;
    private int percentageRandomWeekly;
    private int percentageSubrandomWeekly;
    private int percantageNotCyclic;
    private int optimalTotalScore = 0;


    
    public MonthlySchedule (ArrayList<Nurse> nurses,ArrayList<Nurse> workPatterns, int percentageRandomWeekly, int percentageSubrandomWeekly, int percantageNotCyclic){ // weekly schedule waar alle nurses al assigned zijn aan patterns
        int [] [] amountPerTypePerWeek = new int [2][4]; //type 1 and 2
        String [] [] monthScheduleArray = new String [2][4]; // nodig voor verschillen in aantal nurses/week/type
        String [] monthSchedule = new String [2]; // per type
        WeeklySchedule temp1 = null;
        WeeklySchedule temp2 = null;
        this.percentageRandomWeekly = percentageRandomWeekly;
        this.percentageSubrandomWeekly = percentageSubrandomWeekly;
        this.percantageNotCyclic = percantageNotCyclic;
        WeeklySchedule weeklySchedule = new WeeklySchedule (nurses,workPatterns,percentageRandomWeekly, percentageSubrandomWeekly);
        
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
                int changeOrNot = randomBoolean(percantageNotCyclic);  //0%kans dat voor week 2 een nieuw schema word opgesteld
                if(changeOrNot == 1){
                    weeklySchedule.resetBinarySchedule();
//                    System.out.println(weeklySchedule.getNurses());
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
        int [] amount = new int [2]; //per type
        for (int i = 0; i < 2; i++) { // per type
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
        System.out.println("scheduel 1: " +schedule1);
        System.out.println("Amount of nurses type 1: " + amountNurses1);
        System.out.println("schedule 2: " + schedule2);
        System.out.println("Amount of nurses type 2: " + amountNurses2);
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
    
    public void fireNurses (int type){
        ArrayList <Nurse> nurses = new ArrayList <Nurse>();
        int amountNurses = 0;
        String schedule = "";
        ArrayList <Integer> indexesToFire = new ArrayList <Integer> ();
       
             if(type == 1){ //type 1
                nurses = nursesType1;
                amountNurses = this.amountNurses1;
                schedule = schedule1;
            }
            else if (type == 2){ //type 2
                nurses = nursesType2;
                amountNurses = this.amountNurses2;
                schedule = schedule2;
            }
             
             System.out.println("PRE FIRE SCHEDULE" + schedule);

            for (int j = 0; j < nurses.size(); j++) {
                ArrayList <String> nurseSchedule = schedulesSpecificNurse (j, type);
                int counter = 0;
                for (int l = 0; l < nurseSchedule.size(); l++) {
                    if(nurseSchedule.get(l).equalsIgnoreCase("0000000"))
                        counter++;
                }
                if(counter == 4){ //fire
                    indexesToFire.add(j);
                }
            } 
  
            int nursesFired = 0;


            for (int f =0 ; f<indexesToFire.size(); f++) {
                //type 1
                     String temp1 = schedule.substring(0,(indexesToFire.get(f)-nursesFired)*8 + 0*8*(amountNurses)); //begin tot eerste onderbreking OK
                    String temp2 = schedule.substring((indexesToFire.get(f)-nursesFired)*8 + 0*8*(amountNurses)+8, (indexesToFire.get(f)-nursesFired)*8 + 0*8*(amountNurses)+8 +1*8*(amountNurses-1)); //alle nurses tot volgende punt
                    String temp3 = schedule.substring((indexesToFire.get(f)-nursesFired)*8 + 1*8*(amountNurses)+8, (indexesToFire.get(f)-nursesFired)*8 + 1*8*(amountNurses)+8 +1*8*(amountNurses-1)); //alle nurses tot volgende punt
                    String temp4 = schedule.substring((indexesToFire.get(f)-nursesFired)*8 + 2*8*(amountNurses)+8, (indexesToFire.get(f)-nursesFired)*8 + 2*8*(amountNurses)+8 +1*8*(amountNurses-1));  //alle nurses tot volgende punt
                    String temp5 = schedule.substring((indexesToFire.get(f)-nursesFired)*8 + 2*8*(amountNurses)+8 +1*8*(amountNurses-1)+8); 
                    
                    schedule = temp1 + temp2+temp3 + temp4 + temp5;
                    
                    nurses.remove(indexesToFire.get(f)-nursesFired);
                    amountNurses --;
                    nursesFired++;   
            }
            if (type==1) {
                amountNurses1=amountNurses;
                schedule1=schedule;
            }
            else if (type == 2) {
                amountNurses2= amountNurses;
                schedule2 = schedule;
            }  System.out.println("output schedule: " + schedule);
            System.out.println("Constructor schedule: " + schedule2);
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
//        System.out.println("COST: " + cost);
        optimalCost = (int) cost/200;
        return cost/200;
    }

    public void test () {
        for (Nurse nurse : nursesType1) {
            System.out.println(nurse);
        } System.out.println(""); System.out.println("");
        for (int i=0;i<nursesType1.size();i++) {
            System.out.println("nurse " + nursesType1.get(i).getNr());
            ArrayList <String> temp = new ArrayList <String> ();
            temp = schedulesSpecificNurse(i, 1);
            for (String string : temp) {
                System.out.println(string);
            }System.out.println("");
        }System.out.println("");
        

    }
    
    public int calcNurseSat (int type){ //later opdelen in verschillende methodes. Don't be a Tine
        ArrayList <String> monthScheduleNurse = new ArrayList <String> ();
        int breakFreeDaysPunishment = 20;
        int lowerWorkratePunishment = 5;
        int changeInShiftsPunishment = 50; //willen echt niet dat veel nurses 2 verschillende shiften in dezelfde week heeft
        int changeInShiftsBetweenWeeks = 10;
        
        int totalScore = 0;
        ArrayList <Nurse> usedNurses = new ArrayList <Nurse> ();
        if (type == 1) {usedNurses = this.nursesType1;}
        else {usedNurses = this.nursesType2;}
        
//        for (Nurse usedNurse : usedNurses) {
//            System.out.println(usedNurse);
//        }
               
        //GIGA FOR LUS VOOR ALLE NURSES!!! OM TOTALE SCORE TE BERKENEN
        for (int k = 0; k < usedNurses.size(); k++) { 
//            System.out.println(usedNurses.get(k));
            monthScheduleNurse = schedulesSpecificNurse (k,type);
//            for (String string : monthScheduleNurse) {
//                System.out.println(string);
//            }
//            System.out.println("METHODE VOOR ONDERBREKINGEN ");
        //berekening aantal onderbrekingen! Getest en OK
            int amountOfInteruptions=0; //onderbreking = '101' of '202' OF opt einde '10' en maandag ook '1' 
            //HOUDEN NOG GEEN REKENING MET 102, 201, 20   1 ETC...
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
//            System.out.println("total interuptions " + amountOfInteruptions);
//            System.out.println("METHODE VOOR EMPLOYMENT RATES");
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
                differenceWorkingDays += (int) ((usedNurses.get(k).getEmploymentRate()*rateInDays[0]) - counter);
            }
        
//            System.out.println("Total working days less than nurse wanted: " + differenceWorkingDays);
//            System.out.println("METHODE VOOR SHIFTWISSELS "); //ok
            int AmountOfShiftChanges=0;
        //aantal keer een shiftwissel in dezelfde week (berekenen hoeveel keer 1 in een week, hoeveel keer 2 en het min daarvan is #shiftwissels
            for (int i = 0; i < monthScheduleNurse.size(); i++) {
                
                String shift = monthScheduleNurse.get(i);
                String shift2 = monthScheduleNurse.get(i);
                
                int index = shift.indexOf("1"); //kijken of er 101 of 202 is in weekschema
                int count = 0;
                while (index != -1) {
                count++;
                shift = shift.substring(index + 1);
                index = shift.indexOf("1");
                }
                
                
                int index2 = shift2.indexOf("2"); //kijken of er 101 of 202 is in weekschema
                int count2 = 0;
                while (index2 != -1) {
                count2++;
                shift2 = shift2.substring(index2 + 1);
                index2 = shift2.indexOf("2");
                    
                }
                AmountOfShiftChanges  += Integer.min(count, count2);
            }
//            System.out.println("Amount of shift changes" + AmountOfShiftChanges);
//            System.out.println("");
//            
//            System.out.println("methode voor Wissels TUSSEN schema's in opeenvolgende weken. ENKEL OPEENVOLGEND!! dus 1-2 2-3 3-4 4-1");
            
            String shift1 = monthScheduleNurse.get(0);
            String shift2 = monthScheduleNurse.get(1);
            String shift3 = monthScheduleNurse.get(2);
            String shift4 = monthScheduleNurse.get(3);
           
            //eerste situatie instellen. Dus max 1 = max value van schema 1 
            //OK
            //beter: de meeste parameter opzoeken!! Dus als er 1x1 is en 3x2, dan is type shift == 2. GetShiftType is ook nog fout (in Nurse klasse)
            int differences = 0;
            int typeWeekFirst = getShiftTypeFromString(shift1);
            int typeWeekSecond = getShiftTypeFromString(shift2);
            int typeWeekThird = getShiftTypeFromString(shift3);
            int typeWeekFourth = getShiftTypeFromString(shift4) ;
            
            if (typeWeekFirst != typeWeekSecond)
            {
                differences ++;
            }
            if(typeWeekSecond != typeWeekThird)
            {
                differences++;
            }
            if (typeWeekThird != typeWeekFourth)
            {
                differences++;
            }
            if(typeWeekFourth!= typeWeekFirst)
            {
                differences++;
            }
            
//            System.out.println("DIFFERENT SHIFTS IN SUCCESSIVE WEEKS: " + differences);
            
            
            //door schema i gaan en kijken of deze hetzelfde max waarde heeft als het schema ervoor. Zo niet is het sws slect met zware penalty!!
            int monthScoreNurse = MonthlyPreferenceCalculation(k, type);

            int ScoreNurse = breakFreeDaysPunishment*amountOfInteruptions + differenceWorkingDays*lowerWorkratePunishment +AmountOfShiftChanges*changeInShiftsPunishment
                    + changeInShiftsBetweenWeeks*differences + monthScoreNurse;
//            System.out.println(ScoreNurse);
            totalScore += ScoreNurse;
//            System.out.println(totalScore);
        }
        
//       //nu nog vermenigvudigen met alle specifieke wensen!
//    eerst vermeningvudigen met hun schema X hun specifieke monthly pref
//    dan ook nog nog met de verschillende regels: 
//        vrije dagen na elkaar? OK
//        alle dagen werken dat je wil? Dus schema = ER?  OK 
//        veranderen in shiften?
//        System.out.println("NURSESAT: " + totalScore);
    optimalNurseSat=totalScore;    
    return totalScore;
    }
    
    
    
    public int MonthlyPreferenceCalculation (int nurseNumber, int type) {
        ArrayList <Nurse> usedNurses = new ArrayList <Nurse> ();
        if (type == 1) {usedNurses = this.nursesType1;}
        else {usedNurses = this.nursesType2;}
        
        int score = 0;
        ArrayList <String> monthlySchedule = schedulesSpecificNurse (nurseNumber,type);      
        for (int i = 0; i < monthlySchedule.size(); i++) { //voor weekly schedule 1-4
            int weeklyScore = 0;
            for (int j = 0; j < 7; j++) { //voor elke dag in de week!
                int currentValue = Character.getNumericValue(monthlySchedule.get(i).charAt(j));                
                if (currentValue == 0) {
                    weeklyScore += usedNurses.get(nurseNumber).getMonthlyPreferences()[4][j+(i*7)];
                }
                else if (currentValue == 1) { //we werken enkel met free, early en 
                   weeklyScore += usedNurses.get(nurseNumber).getMonthlyPreferences() [3] [j + (i*7)];
                }
                else if (currentValue == 2) {
                    weeklyScore += usedNurses.get(nurseNumber).getMonthlyPreferences() [1] [j + (i*7)];
                }           
            }
            score += weeklyScore;
            
        }
        //System.out.println("Month score of nurse: " + score);
        return score;
    }
    
    public int patientSatisfaction (int type) {
        //nurseteam vergelijken ma-di, di-woe, woe-do, ...  per week. # verschillen optellen en X penalty cost. over alle weken
        int NewNursePenalty = 10;
        int counter = 0;
        
        for (int j = 0; j < 4; j++) 
        { 
            for (int i = 0; i < 6; i++) { //vergelijk first day met next day van 1e maandag-dinsdag /di-woe/woe-do/do-vr/vr-za/za-zo

                ArrayList <Nurse> referenceDay = nursesWhoWorkOnDayInWeek(i, j, type);
            
            ArrayList <Nurse> DayAfter = nursesWhoWorkOnDayInWeek(i+1, j, type);            
            for (Nurse nurse : DayAfter) 
            {
                if (!referenceDay.contains(nurse))
                {
                    counter++;
                }
            }
            }
        } 
        for (int i = 0; i < 3; i++) {
            
            ArrayList <Nurse> referenceDay = nursesWhoWorkOnDayInWeek(6, i, type);
            ArrayList <Nurse> DayAfter = nursesWhoWorkOnDayInWeek(0, i+1, type);   

            for (Nurse nurse : DayAfter) {
                if (!referenceDay.contains(nurse))
                {counter++;
                } 
            }
        }
        
        //for days
//       System.out.println(counter);
        optimalPatientSat = (int) ((NewNursePenalty*counter) + 0.5*optimalNurseSat);
        return (int) (NewNursePenalty*counter + 0.5*optimalNurseSat);
    }
    
    public ArrayList <Nurse> nursesWhoWorkOnDayInWeek (int day, int week, int type) {
        ArrayList <String> monthScheduleNurse = new ArrayList <String> ();
        ArrayList <Nurse> usedNurses = new ArrayList <Nurse> ();
        ArrayList <Nurse> WorkOnDay = new ArrayList <Nurse> ();
        if (type == 1) {usedNurses = this.nursesType1;}
        else {usedNurses = this.nursesType2;}
        
        for (int k = 0; k < usedNurses.size(); k++) { //ga over elke nurse en haal de verschillende schema's op
            monthScheduleNurse = schedulesSpecificNurse (k,type);
        
            
            if (monthScheduleNurse.get(week).charAt(day) != '0')
            {WorkOnDay.add(usedNurses.get(k));}
        
        }
        return WorkOnDay;
    }
    
    public ArrayList <String> schedulesSpecificNurse (int nurseNumber, int type) { //geeft de 4 weekschema's van de nurse die ingegeven is
        int amountOfNurses=0;
        String schedule = null;
        ArrayList <String> temp = new ArrayList <String> ();
        
        if (type == 1)
        {
            amountOfNurses = this.amountNurses1;
            schedule = this.schedule1;
        }
        else if (type ==2)
        {
           amountOfNurses = this.amountNurses2;
           schedule = this.schedule2;
        }
        for (int i = 0; i < 4; i++) {
            temp.add(schedule.substring(nurseNumber*8 + i*8*(amountOfNurses), nurseNumber*8 + i*(amountOfNurses)*8+7));
        }
        
        return temp;
    }
//    public int calcPatientSat (){
//        
//    }
    public int [][] calcObjectiveFunctions () {
        for (int i = 0; i < 2; i++) { //per type
            System.out.println("TYPE:" + i);
            objectiveFunctions [i][0] = (int) ((calcCost (i+1))); //als i=0 dan type 1, als i=1 dan type 2
            System.out.println("Cost: " + objectiveFunctions [i][0]);
            objectiveFunctions [i][1] = calcNurseSat (i+1);
            System.out.println("NurseSat: " + objectiveFunctions [i][1]);
            objectiveFunctions [i][2] = patientSatisfaction(i+1); // berekening maar ook nog deel laten samenhangen met nurse satisfaction!
            System.out.println("PatientSat: " + objectiveFunctions [i][2]);
        }
        return objectiveFunctions;
    }
    
    public double calcTotalObjectiveFunction (){
        calcObjectiveFunctions();
        double total = 0;
        for (int i = 0; i < 2; i++) { //per type
            for (int j = 0; j < 3; j++) {
                total += objectiveFunctions [i][j] * objectiveWeights[j];
            }
        }
        System.out.println("TOTALSCORE: " + total);
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
    
     public int getShiftTypeFromString (String shift) {
            int counter1 =0;
            int counter2 = 0;
            
            for (int i = 0; i < 7; i++) {
             if (shift.charAt(i) == '1')
             {
                 counter1 ++;
             }
             else if (shift.charAt(i) == '2')
             {
                 counter2 ++;
             }
         }
            if (counter1 > counter2)
            {
                return 1;
            }
            else
            {
                return 2;
            }
     }

    public ArrayList<Nurse> getNursesType1() {
        return nursesType1;
    }

    public ArrayList<Nurse> getNursesType2() {
        return nursesType2;
    }
    
        public int getOptimalCost() {
        return optimalCost;
    }

    public int getOptimalNurseSat() {
        return optimalNurseSat;
    }

    public int getOptimalPatientSat() {
        return optimalPatientSat;
    }

    public int getOptimalTotalScore() {
        return (int) (objectiveWeights[0]*optimalCost+ objectiveWeights[1]*optimalNurseSat+objectiveWeights[2]*optimalPatientSat);
    }
    
    
    
}
