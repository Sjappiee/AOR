
package applied.or;

import java.util.ArrayList;

public class Schedule {
    private ArrayList <Nurse> nurses = new ArrayList <Nurse> ();
    private ArrayList <Nurse> workPatterns = new ArrayList <Nurse> ();
    //private ArrayList <Nurse> nursesLowScore = new ArrayList <Nurse> ();
    private int [][] prefScores = new int [workPatterns.size()][nurses.size()];  //workPatterns are columns, nurses are rows


    public Schedule(ArrayList<Nurse> nurses, ArrayList<Nurse> workPatterns) {
        this.nurses = nurses;
        this.workPatterns = workPatterns;
        this.prefScores = null;   
    }
    
    public ArrayList<Nurse> schedulingProcess (){
        //methode om workrate patterns en nurses te matchen
        for (int k = 0; k < workPatterns.size(); k++) {
            // if listMinScore is lijst met alle nurses, allen met pref = 1000, dan zijn alle nurses opgeruikt => maak nieuwe nurse aan
            // !!! prefscore moet <10 opdat de nurse aan dat pattern mag worden assigned => nakijken of dit met deze pref kosten zo zou uitkomen
            ArrayList <Nurse> temp= listMinScore(k);        // lijst met nurses die min prefscores bij een bepaald workpattern
            //nu uit deze lijst zoeken naar de nurse die het moeilijkste in te plannen is (dus de max prefscore som voor alle workpatterns heeft)
            int max = getSumRow(IDToIndex(temp.get(0).getNr(),nurses));       
            String IDmax = temp.get(0).getNr();

            for (Nurse nurse : temp) {
                if (getSumRow(IDToIndex(nurse.getNr(),nurses)) > max)
                        {
                            max = getSumRow(IDToIndex(nurse.getNr(),nurses));
                            IDmax = nurse.getNr();
                        }   
            }
            // deze nurse word gekoppeld aan het workpattern
            nurses.get(IDToIndex(IDmax,nurses)).setBinaryDayPlanning(workPatterns.get(k).getBinaryDayPlanning());

            for (int i = 0; i < workPatterns.size(); i++) {
                prefScores[i][IDToIndex(IDmax,nurses)] = 1000;
            }
            for (int i = 0; i < nurses.size(); i++) {
                for (int j = 0; j < workPatterns.size(); j++) {
                System.out.print(prefScores[j][i] +" ");
                }
                System.out.println("");
            }
            int [][] binaryPlanning = nurses.get(IDToIndex(IDmax,nurses)).getBinaryDayPlanning();
            for (int j = 0; j < 7; j++) {
                    System.out.print(binaryPlanning [0] [j]);
                }
                System.out.println("");
                for (int j = 0; j < 7; j++) {
                    System.out.print(binaryPlanning [1] [j]);
                }
            System.out.println("");
            System.out.println(IDmax);
            System.out.println(max);
        }
       return null;
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
    
    public ArrayList <Nurse> listMinScore (int scheduleNr,  int [][] prefScores) { 
//te maken: een if zodat enkel nurse into consideration komen die ook dezelfde OF MEER employment rate hebben!        
//lijst van nurses die allen dezelfde min prefscore hebben bij een geg patroon
        ArrayList <Nurse> nursesLowScore = new ArrayList <Nurse> ();
        int min = getMinOfColumn (scheduleNr);

        for (int i = 0; i < nurses.size(); i++) { //gaat voor 1 schedule door alle nurses
            if (prefScores [scheduleNr] [i] == min && nurses.get(i).getEmploymentRate() >= EmploymentRateSchedule(scheduleNr)
                    && nurses.get(i).getType() == workPatterns.get(scheduleNr).getType()) {
                nursesLowScore.add(nurses.get(i));
            }
        }
        
        return nursesLowScore;
    }
       
    public ArrayList <Nurse> listMinScore (int scheduleNr) { 
        ArrayList <Nurse> nursesLowScore = new ArrayList <Nurse> ();
        //zoek nurses met zelfde workrate als schedule
            for (int min = getMinOfColumn (scheduleNr); min < 1000; min++) {
                for (int i = 0; i < nurses.size(); i++) { //gaat voor 1 schedule door alle nurses
                    if (prefScores [scheduleNr] [i] == min && nurses.get(i).getEmploymentRate() == EmploymentRateSchedule(scheduleNr)
                            && nurses.get(i).getType() == workPatterns.get(scheduleNr).getType()) {
                        nursesLowScore.add(nurses.get(i));
                    }
                }

                if (nursesLowScore.isEmpty())
                {
                    System.out.println("old min: " + min);
                }
                else{
                    min +=1000;
                }
            }
        // als er geen zijn met zelfde, zoek nurses met hogere  
            if (nursesLowScore.isEmpty()) {
               for (int min = getMinOfColumn (scheduleNr); min < 1000; min++) {
                    for (int i = 0; i < nurses.size(); i++) { //gaat voor 1 schedule door alle nurses
                        if (prefScores [scheduleNr] [i] == min && nurses.get(i).getEmploymentRate() >= EmploymentRateSchedule(scheduleNr)
                                && nurses.get(i).getType() == workPatterns.get(scheduleNr).getType()) {
                            nursesLowScore.add(nurses.get(i));
                        }
                    }

                    if (nursesLowScore.isEmpty())
                    {
                        System.out.println("old min: " + min);
                    }
                    else{
                        min +=1000;
                    }
                }
            }

         for (Nurse nurse : nursesLowScore) {

                System.out.println(nurse);
            }
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

    
    public int getSumRow (int row, int [][] prefScores){       // som van alle prefscores van 1 nurse, over alle workpatterns heen
        int [] [] temp = prefScores;
        int sum = 0;
        for (int i = 0; i < workPatterns.size(); i++) {
            sum += temp[i][row];
        }
        return sum;
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
            double workRate = EmploymentRateSchedule (i);
            //calc prefscores for each nurse for the pattern i
            for (int j = 0; j < nurses.size(); j++) {
                int[][] nursePref = nurses.get(j).getPreferences();
                float employementRate = nurses.get(j).getEmploymentRate();
                int score = 0;

                for (int k = 0; k < 7; k++) {                             // days
                  int notFree = 0;
                  for (int l = 0; l < 2; l++) {
                      score += nursePref[l][k] * workPattern [l][k];
                      if(workPattern [l][k] !=0){
                          notFree+=1;
                      }
                  }
                  if(notFree==0){                                       //pref score for free
                      score += nursePref[2][k];
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
    
    public double EmploymentRateSchedule (int scheduleNr) 
    {
        ArrayList <Nurse> temp = workPatterns; //we werken met de workpatterns dat ingeladen zijn bij bepaald department bij de creatie van het schedule.
        double rate = 0 ; 
        //door beide lijnen van de array kolom per kolom gaan en optellen en /4. Hoogste employment rate retourneren (dus telkens gaan we 0 en iets anders hebben)
        for (int i = 0; i < 7; i++) { //in 2 afzonderlijke for lussen
            rate += temp.get(scheduleNr).getBinaryDayPlanning() [0][i];   
        }
        
        for (int i = 0; i < 7; i++) { //in 2 afzonderlijke for lussen
            rate += temp.get(scheduleNr).getBinaryDayPlanning() [1][i];
        }

        return (rate/4);
    }
    
    public void addaptSchedule () {
        prefScoreCalculation ();
        int [][] temp = prefScores;
        int [] rateInDays = {4,3,2,1}; //hangt af van SHIFTSYSTEM
        for (int k = 1; k < 3; k++) { // voor nurse type 1 en 2
            int [] amountsNurses = amountWithRates (nurses, k);
            for (int i = 0; i < 4; i++) { //voor elke rate die gesplitst kan worden 
                int [] amountsPatterns = amountWithRates (workPatterns, k);
                if(amountsPatterns[i] < amountsPatterns[i]){ //check of er gesplitst moet worden
                    //welke schedules je gaat splitsen
                    int amountToSplit = amountsPatterns[i] - amountsNurses[i];
                    String [] patternsSplit = getPatternsToSplit (temp,k,i, amountToSplit);
                    //splits
                    int [][] restPatterns = new int [14][0]; // [11111112222222][#nieuwe rest schedules]
                    int shiftDays = rateInDays [i-1];
                    for(String patternID : patternsSplit){
                        int [][] newPattern = new int [2][7];
                        int [][] splitPattern = workPatterns.get(IDToIndex(patternID,workPatterns)).getBinaryDayPlanning();
                        int counter1 = 0;
                        for (int j = 0; j < 7; j++) { //per dag de shift overlopen!!! (niet omgekeert)
                            for (int l = 0; l < 2; l++) {
                                if(splitPattern[l][j] == 1){
                                    if (counter1 < shiftDays){
                                        newPattern[l][j] = 1;
                                        counter1++;
                                    }
                                    else{
                                        if(l == 0){
                                            int counter2 = 0;
                                            while (restPatterns[j][counter2] == 1 || restPatterns[j+7][counter2] == 1 || getAmountShifts (restPatterns, counter2, 14) == shiftDays){
                                                counter2++;
                                            }
                                            restPatterns[j][counter2] = 1;
                                        }
                                        if (l == 1){
                                            int counter2 = 0;
                                            while (restPatterns[j][counter2] == 1 || restPatterns[j+7][counter2] == 1 || getAmountShifts (restPatterns, counter2, 14) == shiftDays){
                                                counter2++;
                                            }
                                        }
                                       
                                    }
                                }
                            }
                        }
                        Nurse pattern = new Nurse(getNewIDPattern (), newPattern, k);
                        workPatterns.add(pattern);
                        workPatterns.remove(IDToIndex(patternID,workPatterns));
                    }
                    //maak pattern uit de restjes
                    for (int j = 0; j < getLengthArray(restPatterns,14); j++) {
                        int [][] newPattern = new int [2][7];
                        for (int l = 0; l < 7; l++) {
                            newPattern [0][l] = restPatterns[l][j];
                        }
                        for (int l = 7; l < 14; l++) {
                            newPattern [1][l-7] = restPatterns[l][j];
                        }
                        Nurse newPattern2 = new Nurse (getNewIDPattern (),newPattern,k);
                        workPatterns.add(newPattern2);
                    }
                }
                    
                //herbereken prefscores om volgende rate te kunnen splitsen
                prefScoreCalculation ();
            }
        }
    }
    
    public String [] getPatternsToSplit ( int [][] temp, int type, int rate, int amountToSplit){ //temp = prefScores, maar waarin word aangepast
         int [] rates = {100,75,50,25};
         String [] patternsSplit = new String [amountToSplit]; 
         for (int j = 0; j < amountToSplit; j++) {
            int max = 0;       
            String IDmax = "none";
            for (Nurse pattern : workPatterns) {
                if (getSumColumn(IDToIndex(pattern.getNr(),workPatterns),temp) > max && pattern.getType() == type && pattern.getEmploymentRate() == rates[rate]/100){
                    max = getSumColumn(IDToIndex(pattern.getNr(),workPatterns),temp);
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
    
    public int getAmountShifts (int [][] restPatterns, int row, int amountColummns){
        int counter = 0;
        for (int i = 0; i < amountColummns; i++) {
            if(restPatterns[i][row] == 1){
                counter++;
            }
        }
        return counter;
    }
    
    public int getLengthArray (int [][] array, int amountColumns ){
        int max = 0;
        for (int i = 0; i < amountColumns; i++) {
            if(array[i].length > max){
                max = array[i].length;
            }
        }
        return max;
    }
    
    public String getNewIDPattern (){
        String lastID = workPatterns.get(workPatterns.size()-1).getNr();
        int lastNr = Integer.parseInt(lastID.substring(lastID.length()-2));
        int newNr = lastNr + 1;
        String prefix = lastID.substring(0,4); // bv: WSD0
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
    
    
    
}
