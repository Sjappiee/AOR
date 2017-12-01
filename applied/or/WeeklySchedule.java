
package applied.or;

import java.util.ArrayList;
import java.util.Random;

public class WeeklySchedule {
    private ArrayList <Nurse> nurses = new ArrayList <Nurse> ();
    private ArrayList <Nurse> workPatterns = new ArrayList <Nurse> ();
    private int [][] prefScores = new int [workPatterns.size()][nurses.size()];  //workPatterns are columns, nurses are rows
    int [] rateInDays = {4,3,2,1}; //hangt af van SHIFTSYSTEM !!! LIJN 670 AANPASSEN
    float [] rates = {(float)1.0,(float)0.75,(float)0.50,(float)0.25};
    int amountShifts = 2; //uniek per lijn!

    public WeeklySchedule(ArrayList<Nurse> nurses, ArrayList<Nurse> workPatterns) {
        this.nurses = nurses;
        this.workPatterns = workPatterns;
        this.prefScores = null;   
    }
    
    public WeeklySchedule(ArrayList<Nurse> nurses) {
        this.nurses = nurses;
        this.workPatterns = null;
        this.prefScores = null;   
    }
    
    public void allProcesses (){
        addaptSchedule ();System.out.println("adapt done");
        recombineQuarterSchedules();System.out.println("recomb done");
        hireNurses();System.out.println("hire done");
        schedulingProcess();System.out.println("schedule done");
    }
    
    public void schedulingProcess (){
        prefScoreCalculation ();
        //methode om workrate patterns en nurses te matchen
        for (int k = 0; k < workPatterns.size(); k++) {
//            for(Nurse nurse: nurses){
//                System.out.println(nurse);
//            }
//            System.out.println("");
//            System.out.println("");
//            System.out.println("TO ASSIGN: " + workPatterns.get(k));
            // if listMinScore is lijst met alle nurses, allen met pref = 1000, dan zijn alle nurses opgeruikt => maak nieuwe nurse aan
            // !!! prefscore moet <10 opdat de nurse aan dat pattern mag worden assigned => nakijken of dit met deze pref kosten zo zou uitkomen
            String IDNurse = "";
            int randomOrNot = randomBoolean(100); //20% kans op een random schedule
            if(randomOrNot == 1){
                ArrayList <Nurse> temp= possibleNursesList(k);
                int randomIndex = 0;
                if(temp.size() == 1){
                    randomIndex = 0;
                }
                //else if (temp.size() == 0)
                else{
                    randomIndex = new Random().nextInt(temp.size());
//                    System.out.println(randomIndex);
                }
                IDNurse = temp.get(randomIndex).getNr();
//                System.out.println("completely random: ");
            }
            else{
                ArrayList <Nurse> temp = listMinScore(k);        // lijst met nurses die min prefscores bij een bepaald workpattern
                int subRandomOrNot = randomBoolean(100); //35% kans op een random schedule
                if(subRandomOrNot == 1){
                    int randomIndex2 = new Random().nextInt(temp.size());
                    IDNurse = temp.get(randomIndex2).getNr();
//                    System.out.println("sub random: ");
                }
                else{
                //nu uit deze lijst zoeken naar de nurse die het moeilijkste in te plannen is (dus de max prefscore som voor alle workpatterns heeft)
                int max = getSumRow(IDToIndex(temp.get(0).getNr(),nurses));       
                IDNurse = temp.get(0).getNr();
                for (Nurse nurse : temp) {
                    if (getSumRow(IDToIndex(nurse.getNr(),nurses)) > max){
                            max = getSumRow(IDToIndex(nurse.getNr(),nurses));
                            IDNurse = nurse.getNr();
                        }   
                }
//                System.out.println("optimal: ");
                }
                
                // deze nurse word gekoppeld aan het workpattern
            }
            nurses.get(IDToIndex(IDNurse,nurses)).setBinaryDayPlanning(workPatterns.get(k).getBinaryDayPlanning());
//            System.out.print("CHOSEN: " + nurses.get(IDToIndex(IDNurse,nurses)).toString());
//            System.out.println("");
//            System.out.println("");
            for (int i = 0; i < workPatterns.size(); i++) {
                prefScores[i][IDToIndex(IDNurse,nurses)] = 1000;
            }
        }
    }
    
    public int IDToIndex (String ID, ArrayList <Nurse> list){     // de laatste 2 string elementen uit het ID omzetten naar een int, dit getal - 1 is dan de index van die nurse in de nurses lijst
        int index = 0;
        for(Nurse nurse: list){
            if(nurse.getNr().equals(ID)){
                index = list.indexOf(nurse);
            }
        }
        return index;
    }
    
    public ArrayList <Nurse> possibleNursesList(int scheduleNr){
        ArrayList <Nurse> possibleNursesList = new ArrayList <Nurse> ();
        double patternRate = workPatterns.get(scheduleNr).getEmploymentRate();
        int temp = 0;
         while (temp==0){
            double j = 0;
            while ( j < 0.76 && temp==0) {
                if(patternRate+j > 1.1 && patternRate != 1.00){
                    temp++;
//                    System.out.println("not enough nurses");
                }
                for (int i = 0; i < nurses.size(); i++) { //gaat voor 1 schedule door alle nurses
                    if (nurses.get(i).getBinaryDayPlanning()==null && nurses.get(i).getEmploymentRate() == workPatterns.get(scheduleNr).getEmploymentRate()+j
                            && nurses.get(i).getType() == workPatterns.get(scheduleNr).getType()) {
                    possibleNursesList.add(nurses.get(i));
                    }
                }
                if(!possibleNursesList.isEmpty()){
                    temp++;
                }
                j += 0.25;
            }
        }   
        return possibleNursesList;
    }
       
    public ArrayList <Nurse> listMinScore (int scheduleNr) { 
        ArrayList <Nurse> nursesLowScore = new ArrayList <Nurse> ();
        double patternRate = workPatterns.get(scheduleNr).getEmploymentRate();
        int temp = 0;
         while (temp==0){
            double j = 0;
            while ( j < 0.76 && temp==0) {
                if(patternRate+j > 1.1 && patternRate != 1.00){
                    temp++;
//                    System.out.println("not enough nurses");
                }
                for (int min = getMinOfColumn (scheduleNr); min < 1000; min++) {
                    for (int i = 0; i < nurses.size(); i++) { //gaat voor 1 schedule door alle nurses
                        if (prefScores [scheduleNr] [i] == min && nurses.get(i).getEmploymentRate() == workPatterns.get(scheduleNr).getEmploymentRate()+j
                                && nurses.get(i).getType() == workPatterns.get(scheduleNr).getType()) {
                            nursesLowScore.add(nurses.get(i));
                        }
                    }
                    if (nursesLowScore.isEmpty()){
//                    System.out.println("old min: " + min);
                    }
                    else{
                        min +=1000;
                    }
                }   
                if(!nursesLowScore.isEmpty()){
                    temp++;
                }
                j += 0.25;
            }
        }
        //zoek nurses met zelfde workrate als schedule
//            for (int min = getMinOfColumn (scheduleNr); min < 1000; min++) {
//                for (int i = 0; i < nurses.size(); i++) { //gaat voor 1 schedule door alle nurses
//                    if (prefScores [scheduleNr] [i] == min && nurses.get(i).getEmploymentRate() == workPatterns.get(scheduleNr).getEmploymentRate()
//                            && nurses.get(i).getType() == workPatterns.get(scheduleNr).getType()) {
//                        nursesLowScore.add(nurses.get(i));
//                    }
//                }
//                if (nursesLowScore.isEmpty()){
////                    System.out.println("old min: " + min);
//                }
//                else{
//                    min +=1000;
//                }
//            }
//        // als er geen zijn met zelfde, zoek nurses met hogere  
//            if (nursesLowScore.isEmpty()) {
//               for (int min = getMinOfColumn (scheduleNr); min < 1000; min++) {
//                    for (int i = 0; i < nurses.size(); i++) { //gaat voor 1 schedule door alle nurses
//                        if (prefScores [scheduleNr] [i] == min && nurses.get(i).getEmploymentRate() >= workPatterns.get(scheduleNr).getEmploymentRate()
//                                && nurses.get(i).getType() == workPatterns.get(scheduleNr).getType()) {
//                            nursesLowScore.add(nurses.get(i));
//                        }
//                    }
//                    if (nursesLowScore.isEmpty()){
////                        System.out.println("old min: " + min);
//                    }
//                    else{
//                        min +=1000;
//                    }
//                }
//            }
//        for (Nurse nurse: nursesLowScore){
//            System.out.println(nurse.toString());
//        }
//        System.out.println("");
//   
        return nursesLowScore;
    }
    
    public int getMinOfColumn (int column) { //minimum van kolom vinden
        double patternType = workPatterns.get(column).getType(); // column = schedulenr
        int [][] temp;
        int min;
        if (patternType == 1){      //eerste in de lijst is zowizo type 1
            temp = prefScores;
            min = temp [column] [0];
        }
        else{                       //laatste in de lijst is zowizo type 2
            temp = prefScores;
            min = temp [column] [nurses.size()-1];
        }
        for (int i = 0; i < nurses.size(); i++) {
            if (temp [column] [i] < min && nurses.get(i).getType() == workPatterns.get(column).getType()){
                min = temp [column] [i];
            }
        }
        //System.out.println(min);
        return min;
    }
  
    public int getSumRow (int row){
        int [] [] temp = prefScores;
        int sum = 0;
        for (int i = 0; i < workPatterns.size(); i++) {
            sum += temp[i][row];
        }
        return sum;
    }
    
    public int getSumColumn (int column,int [][] list){
        int sum = 0;
        for (int i = 0; i < nurses.size(); i++) {
            sum += list[column][i];
        }
        return sum;
    }
    
    
    public int [][] prefScoreCalculation (){ //in orde! 
        int [][] temp = new int [workPatterns.size()][nurses.size()];
        for (int i = 0; i < workPatterns.size(); i++) {
            int [][] workPattern = workPatterns . get(i).getBinaryDayPlanning();
            //calc workrate for the pattern i
            double workRate = workPatterns . get(i).getEmploymentRate();
            //calc prefscores for each nurse for the pattern i
            for (int j = 0; j < nurses.size(); j++) {
                int[][] nursePref = nurses.get(j).getPreferences();
                float employementRate = nurses.get(j).getEmploymentRate();
                int score = 0;

                for (int k = 0; k < 7; k++) {                             // days
                  int notFree = 0;
                  for (int l = 0; l < amountShifts; l++) {
                      score += nursePref[l][k] * workPattern [l][k];
                      if(workPattern [l][k] !=0){
                          notFree+=1;
                      }
                  }
                  if(notFree==0){                                       //pref score for free
                      score += nursePref[amountShifts][k];
                  }  
                }
                if (employementRate - workRate == 0.25){
                    score += 10;
                }
                if (employementRate - workRate == 0.5){
                    score += 20;
                }
                temp[i][j] = score;
            }
        }
       prefScores = temp; 
       return prefScores;
    }
    
        
        public double EmploymentRateSchedule (ArrayList <Nurse> usedList, int scheduleNr) 
    {
        ArrayList <Nurse> temp = usedList; //we werken met de workpatterns dat ingeladen zijn bij bepaald department bij de creatie van het schedule.
        double rate = 0 ;
        double employmentRate = 6;
        //door beide lijnen van de array kolom per kolom gaan en optellen en /4. Hoogste employment rate retourneren (dus telkens gaan we 0 en iets anders hebben)
        for (int j = 0; j < amountShifts; j++) {
            for (int i = 0; i < 7; i++) { 
                rate += temp.get(scheduleNr).getBinaryDayPlanning() [j][i];   
            }
        }
        if(rate == rateInDays[0]) employmentRate = 1.00;
        if(rate == rateInDays[1]) employmentRate = 0.75;
        if(rate == rateInDays[2]) employmentRate = 0.5;
        if(rate == rateInDays[3]) employmentRate = 0.25;

        return employmentRate;
    }
    
    public void hireNurses (){
        int[][] preferencesWeek = new int [amountShifts+1][7];  // "+1" = free
        for (int i = 0; i < amountShifts+1; i++) {
            for (int j = 0; j < 7; j++) {
                preferencesWeek[i][j] = 5;
            }
        }
        int[][] preferencesMonth = new int [5][28]; 
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 28; j++) {
                preferencesMonth[i][j] = 5;
            }
        }
        for (int k = 1; k < 3; k++) { //per type (1,2)
            int [] amountsNurses = amountWithRates (nurses, k);
            int [] amountsPatterns = amountWithRates (workPatterns, k);
            // 100%
            int rate = 0;
            if(amountsNurses[rate] < amountsPatterns[rate]){
                    int amount = amountsPatterns[rate] - amountsNurses[rate];
                    for (int j = 0; j < amount; j++){ 
                        Nurse newNurse = new Nurse(getNewIDNurse (k), rates[rate], k, " ",preferencesWeek, preferencesMonth);
                        nurses.add(newNurse);
                    }
                }
            // 75%
            rate = 1;
            if(amountsNurses[rate] < amountsPatterns[rate]){
                int amount = amountsPatterns[rate] - amountsNurses[rate];
                if(amount > (amountsNurses[rate-1] - amountsPatterns[rate-1])){
                    for (int j = 0; j < amount; j++){ 
                        Nurse newNurse = new Nurse(getNewIDNurse (k), rates[rate], k, " ",preferencesWeek, preferencesMonth);
                        nurses.add(newNurse);
                    }
                 }
            }
            // 50%
            rate = 2;
            if(amountsNurses[rate] < amountsPatterns[rate]){
                int amount = amountsPatterns[rate] - amountsNurses[rate];
                if(amount > (amountsNurses[rate-1] - amountsPatterns[rate-1] + amountsNurses[rate-2] - amountsPatterns[rate-2])){
                    for (int j = 0; j < amount; j++){ 
                        Nurse newNurse = new Nurse(getNewIDNurse (k), rates[rate], k, " ",preferencesWeek, preferencesMonth);
                        nurses.add(newNurse);
                    }
                 }
            }
            // 25%
            rate = 3;
            if(amountsNurses[rate] < amountsPatterns[rate]){
                int amount = amountsPatterns[rate] - amountsNurses[rate];
                if(amount > (amountsNurses[rate-1] - amountsPatterns[rate-1] + amountsNurses[rate-2] - amountsPatterns[rate-2] + amountsNurses[rate-3] - amountsPatterns[rate-3] )){
                    for (int j = 0; j < amount; j++){ 
                        Nurse newNurse = new Nurse(getNewIDNurse (k), rates[rate], k, " ",preferencesWeek, preferencesMonth);
                        nurses.add(newNurse);
                    }
                 }
            }
//            for (int i = 0; i < 4; i++) { //per rate (100,75,50,25)
//                if(amountsNurses[i] < amountsPatterns[i]){
//                    int amount = amountsPatterns[i] - amountsNurses[i];
//                    for (int j = 0; j < amount; j++){ 
//                        Nurse newNurse = new Nurse(getNewIDNurse (k), rates[i], k, " ",preferencesWeek, preferencesMonth);
//                        nurses.add(newNurse);
//                    }
//                }
//            }
        }
//        for (Nurse nurse: nurses) {
//            System.out.println(nurse);
//        }
    }
    
    public void addaptSchedule () {
        prefScoreCalculation ();
        int [][] temp = prefScores;
        for (int k = 1; k < 3; k++) { // voor nurse type 1 en 2
            int [] amountsNurses = amountWithRates (nurses, k);
            // rate 100%
            int rate = 0;
            int [] amountsPatterns = amountWithRates (workPatterns, k);
            if(amountsNurses[rate] < amountsPatterns[rate]){ //check of er gesplitst moet worden
//                    System.out.println(amountsPatterns[i] + " " + amountsNurses[i]);
                int amountToSplit = amountsPatterns[rate] - amountsNurses[rate];
                String [] patternsSplit = getPatternsToSplit (temp,k,rate, amountToSplit);
                splitPatterns(patternsSplit,k,rate);
                prefScoreCalculation ();
                temp = prefScores;                      
            }
            // rate 75%
            rate = 1;
            amountsPatterns = amountWithRates (workPatterns, k);
            if(amountsNurses[rate] < amountsPatterns[rate]){ //check of er gesplitst moet worden
//                    System.out.println(amountsPatterns[i] + " " + amountsNurses[i]);
                int amountToSplit = amountsPatterns[rate] - amountsNurses[rate];
                if(amountToSplit > (amountsNurses[rate-1] - amountsPatterns[rate-1])){
                    String [] patternsSplit = getPatternsToSplit (temp,k,rate, amountToSplit);
                    splitPatterns(patternsSplit,k,rate);
                    prefScoreCalculation ();
                    temp = prefScores;
                }
            }
            // rate 50%
            rate = 2;
            amountsPatterns = amountWithRates (workPatterns, k);
            if(amountsNurses[rate] < amountsPatterns[rate]){ //check of er gesplitst moet worden
//                    System.out.println(amountsPatterns[i] + " " + amountsNurses[i]);
                int amountToSplit = amountsPatterns[rate] - amountsNurses[rate];
                if(amountToSplit > ((amountsNurses[rate-1] - amountsPatterns[rate-1])+(amountsNurses[rate-2] - amountsPatterns[rate-2]))){
                    String [] patternsSplit = getPatternsToSplit (temp,k,rate, amountToSplit);
                    splitPatterns(patternsSplit,k,rate);
                    prefScoreCalculation (); 
                 }
            }
        }
    }
    
    public void splitPatterns (String [] patternsSplit, int type, int rate){
        int [][] restPatterns = new int [7*amountShifts][100]; // [11111112222222][#nieuwe rest schedules (gwn heel groot getal)]
        int shiftDays = rateInDays [rate+1];
        int [] days = new int [7*amountShifts];
        for (int i = 0; i < 7*amountShifts; i++) {
            days[i] = i;
        }
        //maak splitsingen en sla restjes op
        for(String patternID : patternsSplit){
            int [][] newPattern = new int [amountShifts][7];
//            int [][] splitPattern = workPatterns.get(IDToIndex(patternID,workPatterns)).getBinaryDayPlanning();
            String splitPattern = workPatterns.get(IDToIndex(patternID,workPatterns)).BinaryPlanningToString ();
            int counter1 = 0;
            int [] sequence = shuffleArray(days);
            for (int i = 0; i < sequence.length; i++) {
                if(Character.getNumericValue( splitPattern.charAt(sequence[i])) == 1){            
                        for (int s = 0; s < amountShifts; s++) {
                            if ((s*7-1) < sequence[i] && sequence[i] < ((s+1)*7)) {
                                //int shift = s;
                                if (counter1 < shiftDays){
                                    newPattern[s][sequence[i]-7*s] = 1;
                                    counter1++;
                                }
                                else{
//                            System.out.println("s1= "+ restPatterns[sequence[i]][counter2]);
//                            System.out.println("s2= "+ restPatterns[sequence[i]][counter2]);
//                            System.out.println("already in rest= "+ getAmountShiftsRest (restPatterns, counter2, 14));
                                    int counter2 = 0;

                                        while (restPatterns[sequence[i]][counter2] == 1 || getAmountShiftsRest (restPatterns, counter2, 7*amountShifts) == shiftDays){
                                            counter2++;
                                        }
                                        restPatterns[sequence[i]][counter2] = 1;
                                    
//                            System.out.println("rest(" + sequence[i] + "," + counter2 + ") = "+ restPatterns[sequence[i]][counter2]);
                                    
                                }
                            }
                        }
                        
                    
                }
            }
            Nurse pattern = new Nurse(getNewIDPattern (),calcPatternRate(newPattern), newPattern, type);
            workPatterns.add(pattern);
            workPatterns.remove(IDToIndex(patternID,workPatterns));
            
        }
        //maak pattern uit de restjes
        for (int j = 0; j < getLengthArray(restPatterns,amountShifts*7,100); j++) {
            for (int s = 0; s < amountShifts; s++) {
                int [][] newPattern= new int [amountShifts][7];
                for (int l = s*7; l < 7*(s+1); l++) {
                    if(restPatterns[l][j] == 1){
                        newPattern = new int [amountShifts][7];
                        newPattern [s][l-s*7] = restPatterns[l][j];
                        Nurse newPattern2 = new Nurse (getNewIDPattern (),(float) 0.25,newPattern,type);
                        workPatterns.add(newPattern2);
                    }
                }
                
            }

        }
//        System.out.println("AFTER SPLIT FOR RATE: " + rate);
//        for(Nurse nurse: workPatterns){
//            System.out.println(nurse);
//        }
//        System.out.println("");
    }

    
    public String [] getPatternsToSplit ( int [][] temp, int type, int rate, int amountToSplit){ //temp = prefScores, maar waarin word aangepast
//        System.out.println(rate + " " + amountToSplit); 
        String [] patternsSplit = new String [amountToSplit]; 
         for (int j = 0; j < amountToSplit; j++) {
            int max = 0;       
            String IDmax = "none";
            for (Nurse pattern : workPatterns) {
//                System.out.println(IDToIndex(pattern.getNr(),workPatterns) + " " + pattern.getNr() + " "  + pattern.getEmploymentRate() + " " + getSumColumn(IDToIndex(pattern.getNr(),workPatterns),temp));
                if (getSumColumn(IDToIndex(pattern.getNr(),workPatterns),temp) > max && pattern.getType() == type && pattern.getEmploymentRate() == rates[rate])
                {
                    max = getSumColumn(IDToIndex(pattern.getNr(),workPatterns),temp);
//                    System.out.println(max);
                    IDmax = pattern.getNr();
                    }   
                }
            
                patternsSplit[j] = IDmax;
                for (int m = 0; m < nurses.size(); m++) {
                    temp[IDToIndex(IDmax,workPatterns)][m] = 0;
                }
            }
         return patternsSplit;
    }
    
    public int getAmountShiftsRest (int [][] restPatterns, int row, int amountColummns){
        int counter = 0;
        for (int i = 0; i < amountColummns; i++) {
            if(restPatterns[i][row] == 1){
                counter++;
            }
        }
        return counter;
//        System.out.println("EINDE ADAPT SCHEDULE");
//        System.out.println("");
    }
       
    public void recombineQuarterSchedules () {
        ArrayList <Nurse> temp = new ArrayList <Nurse> ();
        int indexFirstRest = searchFirstIndexOfRestSchedules();
        temp = searchQuarterSchedules(); //nu hebben we de lijst met alle werkschema's die en rate van .25 hebben.
        
//        System.out.println("LIJST MET RESTSCHEMA'S");
//        for (Nurse nurse : temp) {
//            System.out.println(nurse);
//        }
//        System.out.println("");
        //lijst met .25 proberen hercombineren tot schema's

        for (int j = 0; j < temp.size(); j++) {
            if(j == temp.size()-1){
//                System.out.println("last");
            }
//            System.out.println("TOEVOEGNAAN: " + temp.get(j));
            
//            System.out.println("shiftType: "+ temp.get(j).getShiftType());
            for (int i = j+1; i < temp.size(); i++) { //herkennen wat er te combineren valt en dit dan doen (in de tijdelijke lijst               
//                System.out.println("opties: "+ temp.get(i));
//                System.out.println("indexes: " + temp.get(i).getIndexof1());
//                System.out.println("shiftType: " + temp.get(i).getShiftType());
//                System.out.println("indexes of TOETEVOEGEN: " + temp.get(j).getAllIndexesOf1());
                if (!temp.get(j).getAllIndexesOf1().contains(temp.get(i).getIndexof1()) && temp.get(j).getShiftType() == temp.get(i).getShiftType() && temp.get(j).getType()== temp.get(i).getType() && temp.get(j).getAllIndexesOf1().size() < rateInDays[0]) {//indien combinable //AANPASSEN NAAR GET ALLE INDEXES OF 1 VOOR HUIDIGE OBJECT
                    
                    
                    temp.get(j).setSpecificBinaryToOne(temp.get(i).getShiftType()-1, temp.get(i).getIndexof1());
                    temp.remove(i);
//                    System.out.println("recombine");
//                    System.out.println("new: " + temp.get(j));
                    i--;}
            }
            
        } 
        for (int i = 0; i < temp.size(); i++) { //juiste ER instellen dat data nog steeds klopt
            temp.get(i).setEmploymentRate((float) EmploymentRateSchedule(temp, i));
        }
        for (int j = indexFirstRest; j<workPatterns.size();j++){ //de algemene worklijst aanpassen. Dus alle restschema's eruit smijten en dan de nieuwe gecombineerde temp list toevoegen
            workPatterns.remove(j);
            j--;
        }
        for (int i = 0; i < temp.size(); i++) {
            workPatterns.add(temp.get(i));
        }
//        System.out.println("AFTER RECOMBINE: ");
//        for(Nurse nurse:workPatterns){
//            System.out.println(nurse);
//        }
//        System.out.println("");
//        System.out.println("");
    }   
    
    public ArrayList <Nurse> searchQuarterSchedules () {
        ArrayList <Nurse> temp = new ArrayList <Nurse> ();
        for (Nurse workPattern : workPatterns) {
            if (workPattern.getEmploymentRate() == 0.25){
                temp.add(workPattern);
            }
        }
        return temp;
    }
    
    public int searchFirstIndexOfRestSchedules () {
        int counter =0;
        for (int i = 0; i < workPatterns.size(); i++) {
            if (workPatterns.get(i).getEmploymentRate() == 0.25){
                i+=100;
            }
            else{
                counter ++;
            }
        }
        return counter;
    }
    
    public int getLengthArray (int [][] array, int amountColumns , int amountRows){
        int counter1 = 0;
        for (int i = 0; i < amountRows; i++) {
            int counter2 = 0;
            for (int j = 0; j < amountColumns; j++) {
                if(array[j][i] == 1){
                    counter2++;
                }
            }
            if(counter2 != 0){
                counter1++;
            }
        }
        return counter1;
    }
    
    public String getNewIDPattern (){
        String lastID = workPatterns.get(workPatterns.size()-1).getNr();
        int lastNr = Integer.parseInt(lastID.substring(lastID.length()-2));
        int newNr = lastNr + 1;
        String prefix = lastID.substring(0,4); // bv: WSD0
        String newID = prefix + newNr;
        return newID;
    }
    
    public String getNewIDNurse (int type){
        String lastID = nurses.get(nurses.size()-1).getNr();
        int lastNr = Integer.parseInt(lastID.substring(lastID.length()-2));
        int newNr = lastNr + 1;
        String prefix = "H30" + type + lastID.substring(3, 5); // bv: WSD0
        String newID = prefix + newNr;
        return newID;
    }

    public int [] amountWithRates (ArrayList<Nurse> list, int type){
        int [] amounts = new int [4];
        double j = 1.00;
        int counter = 0;
        while (j>0.24) {
            int amount = 0;
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getEmploymentRate() == j && list.get(i).getType() == type)
                    amount++; 
            }
            amounts[counter] = amount;
            counter++;
            j = j - 0.25;
        }
        return amounts;
    }
    
    public float calcPatternRate (int [] [] dayPlanning){
            int rateInDays1 = 0;
            double rate = 0;
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 2; j++) {
                    if(dayPlanning[j][i] == 1){
                        rateInDays1++;
                    }
                }
            }
            if(rateInDays1 == rateInDays[0]) rate = rates[0];
            if(rateInDays1 == rateInDays[1]) rate = rates[1];
            if(rateInDays1 == rateInDays[2]) rate = rates[2];
            if(rateInDays1 == rateInDays[3]) rate = rates[3];
            
            return (float)rate;
        }
    
    
    private int [] shuffleArray(int[] array){
        int index, temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--){
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return array;
    }
    
    public int [] amountWithType (ArrayList<Nurse> list){
        int counter; 
        int [] amounts = new int [2];
        for (int i = 1; i < 3; i++) {
            counter=0;
            for(Nurse nurse: list){
                if(nurse.getType() == i){
                    counter++;
                }
            }
            amounts[i-1] = counter;
        }
        return amounts;
    }
    
    public ArrayList<Nurse> getNurses() {
        return nurses;
    }

    public void setNurses(ArrayList<Nurse> nurses) {
        this.nurses = nurses;
    }

    public ArrayList<Nurse> getWorkPatterns() {
        return workPatterns;
    }

    public void setWorkPatterns(ArrayList<Nurse> workPatterns) {
        this.workPatterns = workPatterns;
    }

    public int[][] getPrefScores() {
        return prefScores;
    }

    public void setPrefScores(int[][] prefScores) {
        this.prefScores = prefScores;
    }
    
    

    @Override
    public String toString() {
        return "Schedule{" + "nurses=" + nurses + ", workPatterns=" + workPatterns + ", prefScores=" + prefScores + '}';
    }
    
    public String ScheduleToString () {
        String output = "";
        ArrayList<String> temp = new ArrayList <String> ();
        for (int i = 0; i < nurses.size(); i++) {
            temp.add(nurses.get(i).BinaryPlanningToString());
        }
        
        for (int i = 0; i < temp.size(); i++) { //nu voor elke string van 14 delen juist toevoegen
        //beide = 0, eerste = 0 en tweede niet, tweede = 0 en eerste niet
            if(temp.get(i).equals("")){
                output+= "0000000*";
            }
            else{
                for (int j = 0; j < 7; j++) {
                    if (temp.get(i).charAt(j) == '0' && temp.get(i).charAt(j+7) == '0' /* && temp.get(i).charAt(j+14) == '0' */){
                        output+= "0";
                    }
                    else if (temp.get(i).charAt(j) == '1' && temp.get(i).charAt(j+7) == '0' /* && temp.get(i).charAt(j+14) == '0' */){ 
                        output+= "1";
                    }
                    else if (temp.get(i).charAt(j) == '0' && temp.get(i).charAt(j+7) == '1' /* && temp.get(i).charAt(j+14) == '0' */){ 
                        output+= "2";
                    }
               //SHIFTSYSTEM:3 => alles uit comment zetten in if's + de if hieronder
//               else if (temp.get(i).charAt(j) == '0' && temp.get(i).charAt(j+7) == '0' /* && temp.get(i).charAt(j+14) == '1' */){ 
//                   output+= "3";
//               }
                }
                output += "*";
            }
        }
        return output;
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
    
    public void resetBinarySchedule(){
        for(Nurse nurse:nurses){
            nurse.setBinaryDayPlanning(null);
        }
    }
        
}
    
    
    
    
