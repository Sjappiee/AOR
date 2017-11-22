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
    
    public MonthlySchedule (ArrayList<Nurse> nurses,ArrayList<Nurse> workPatterns){ // weekly schedule waar alle nurses al assigned zijn aan patterns
        int amount1 = 0;
        int amount2 = 0;
        WeeklySchedule [] schedules = new WeeklySchedule [4];
        WeeklySchedule weeklySchedule1 = new WeeklySchedule (nurses,workPatterns);
        //week 1
        weeklySchedule1.allProcesses();
        schedules[0] = weeklySchedule1;
        amount1 = weeklySchedule1.amountWithType(weeklySchedule1.getNurses())[0];
        amount2 = weeklySchedule1.amountWithType(weeklySchedule1.getNurses())[1];
        //week2,3,4
        for (int i = 1; i < 4; i++) {
            WeeklySchedule weeklySchedulei = new WeeklySchedule (nurses,workPatterns);
            int changeOrNot = randomBoolean(0);  //100% kans dat week 2 gwn idem aan week 1 is, 0%kans dat voor week 2 een nieuw schema word opgesteld
            if(changeOrNot == 0){
                weeklySchedulei = schedules[i-1];
            }
            else{
                weeklySchedulei.allProcesses();
            }
            schedules[i] = weeklySchedulei;
            //check of er in deze week meer nurses zijn dan in de vorige, if so, reset amounts EN add "00000000*" aan string vorige week / nurse
        }
        
        this.amountNurses1 = amount1;
        this.amountNurses2 = amount2;
        String monthSchedule1 = "";
        String monthSchedule2 = "";
        for (int i = 0; i < 4; i++) {  //per week: add nurses of each type to a different list + make a string of the schedules
            ArrayList <Nurse> scheduleType1 = new ArrayList <Nurse>();
            ArrayList <Nurse> scheduleType2 = new ArrayList <Nurse>();
            for (Nurse nurse : schedules[i].getNurses()) { //types opsplitsen
                if(nurse.getType() == 1){
                    scheduleType1.add(nurse);
                }
                if(nurse.getType() == 2){
                    scheduleType2.add(nurse);
                }
            }
            WeeklySchedule temp1 = new WeeklySchedule(scheduleType1); //type 1
            WeeklySchedule temp2 = new WeeklySchedule(scheduleType2); //type 2
            monthSchedule1 += temp1.ScheduleToString();
            monthSchedule2 += temp2.ScheduleToString();
        }
        this.schedule1 =   monthSchedule1;
        this.schedule2 =   monthSchedule2;
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
        System.out.println("schedule: " + schedule);
        System.out.println("wages: " + wages[0][0] + ","+ wages[0][1] + ","+ wages[1][0] + ","+ wages[1][1]);
        
        //wages
        int counter = 0;
        for (int w = 0; w < 4; w++) {  
            for (int n = 0; n < amountNurses; n++) { // nurses
                for (int i = 0; i < 5; i++) { //week
                    System.out.println(counter);
                    if(Character.getNumericValue(schedule.charAt(counter)) > 0){
                        System.out.println("shift: " + Character.getNumericValue(schedule.charAt(counter)));
                        cost += wages[0][Character.getNumericValue(schedule.charAt(counter))-1] ; //if type=1, wages[0][0] ,if type=2, wages[0][1]
                        labourHoursWeek += shiftHours;
                        System.out.println(" " + cost);
                    }
                     counter++;
                }
                for (int i = 5; i < 8; i++) { //weekend
                    System.out.println(counter);
                    if(Character.getNumericValue(schedule.charAt(counter)) > 0 ){
                        System.out.println("shift: " + Character.getNumericValue(schedule.charAt(counter)));
                        cost += wages[1][Character.getNumericValue(schedule.charAt(counter))-1] ; //if type=1, wages[1][0] ,if type=2, wages[1][1]
                        labourHoursWeekend += shiftHours;
                        System.out.println(" " + cost);
                    }
                    counter++;
                } 
            }
        }
        System.out.println("labourHoursWeek: " + labourHoursWeek);
        System.out.println("labourHoursWeekend: " + labourHoursWeekend);
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
        //GIGA FOR LUS VOOR ALLE NURSES!!! OM TOTALE SCORE TE BERKENEN
        //eerst degelijk berekenen oor 1 nurse
        for (int k = 0; k < 1; k++) {
        
        
        monthScheduleNurse = schedulesSpecificNurse (k,type);
            System.out.println("METHODE VOOR ONDERBREKINGEN ");
        //berekening aantal onderbrekingen! Getest en OK
        int amountOfInteruptions=0; //onderbreking = '101' of '202' OF opt einde '10' en maandag ook '1'
        for (int i = 0; i < monthScheduleNurse.size(); i++) { 
            String shift = monthScheduleNurse.get(i);
//            System.out.println(shift);
            if(shift.substring(5).equalsIgnoreCase("10") && shift.charAt(0) == '1') //in het uitzonderlijke geval van bv '1001110' Ook onderbreking (zaterdag-zondag-maandag)
            {
                amountOfInteruptions++;
            }
            if (shift.substring(0, 1).equalsIgnoreCase("01") && shift.charAt(6) == '1') //in het uitzonderlijke gevan van bv '0111001' Ook onderbreking (zondag-maandag-dinsdag)
            {
                amountOfInteruptions++;
            }
            
            if(shift.substring(5).equalsIgnoreCase("20") && shift.charAt(0) == '2') //in het uitzonderlijke geval van bv '1001110' Ook onderbreking (zaterdag-zondag-maandag)
            {
                amountOfInteruptions++;
            }
            if (shift.substring(0, 1).equalsIgnoreCase("02") && shift.charAt(6) == '2') //in het uitzonderlijke gevan van bv '0111001' Ook onderbreking (zondag-maandag-dinsdag)
            {
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
        ArrayList<Nurse> usedNurses = new ArrayList <Nurse> ();
        if (type ==1 )
        {
            usedNurses = nursesType1;
        }
        else
        {
            usedNurses = nursesType2;
        }
        int differenceWorkingDays = 0;
        for (int i = 0; i < monthScheduleNurse.size(); i++) //met nurse x, type x alle 4 de weken doorlopen!
        {
            
            int counter=0;
            String shift = monthScheduleNurse.get(i);
            for (int j = 0; j < shift.length(); j++) { //aantal werkdagen in een week berekenen.
                if (shift.charAt(j) != '0')
                {
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
                shift = shift.substring(index + 1);
                index2 = shift.indexOf("2");
                }
                AmountOfShiftChanges  += Integer.min(count, count2);
            }
            System.out.println("Amont of shift changes" + AmountOfShiftChanges);
        
        
        

        int ScoreNurse = breakFreeDaysPunishment*amountOfInteruptions + differenceWorkingDays*lowerWorkratePunishment +AmountOfShiftChanges*changeInShiftsPunishment;
        System.out.println(ScoreNurse);
        totalScore += ScoreNurse;
        System.out.println(totalScore);
        }
        
//       
//    eerst vermeningvudigen met hun schema X hun specifieke monthly pref
//    dan ook nog nog met de verschillende regels: 
//        vrije dagen na elkaar? OK
//        alle dagen werken dat je wil? Dus schema = ER?  OK 
//        veranderen in shiften?
        return 0;
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
            temp.add(schedule.substring(nurseNumber*8 + i*8*amountOfNurses, nurseNumber*8 + i*amountOfNurses*8+7));
        }
        
        return temp;
    }
//    public int calcPatientSat (){
//        
//    }
//    public int [] calcObjectiveFunctions () {
//        alle 3 berekenen en in een array zetten
//    }

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
