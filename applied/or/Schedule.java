
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
            int max = getSumRow(IDToPrefRow(temp.get(0).getNr()));       
            String IDmax = temp.get(0).getNr();

            for (Nurse nurse : temp) {
                if (getSumRow(IDToPrefRow(nurse.getNr())) > max)
                        {
                            max = getSumRow(IDToPrefRow(nurse.getNr()));
                            IDmax = nurse.getNr();
                        }   
            }
            // deze nurse word gekoppeld aan het workpattern
            nurses.get(IDToPrefRow(IDmax)).setBinaryDayPlanning(workPatterns.get(0).getBinaryDayPlanning());

            for (int i = 0; i < workPatterns.size(); i++) {
                prefScores[i][IDToPrefRow(IDmax)] = 1000;
            }
            for (int i = 0; i < nurses.size(); i++) {
                for (int j = 0; j < workPatterns.size(); j++) {
                System.out.print(prefScores[j][i] +" ");
                }
                System.out.println("");
            }
            int [][] binaryPlanning = nurses.get(IDToPrefRow(IDmax)).getBinaryDayPlanning();
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
    
    public int IDToPrefRow (String ID){     // de laatste 2 string elementen uit het ID omzetten naar een int, dit getal - 1 is dan de index van die nurse in de nurses lijst
        int row;
        row = Integer.parseInt(ID.substring(ID.length()-2));
        return row-1;
    }
    
    public int IDToPrefColumn (String ID){     // de laatste 2 string elementen uit het ID omzetten naar een int, dit getal - 1 is dan de index van die nurse in de nurses lijst
        int column;
        column = Integer.parseInt(ID.substring(ID.length()-2));
        return column-1;
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
    
    public void addaptSchedules (){
       int [] amountsPatterns =  calcScheduleRateAmounts (); // 100:0, 75:1, 50:2, 25:3
       int [] rates = {100,75,50,25};
        for (int i = 0; i < 4; i++) {
            int amountNeeded = amountsPatterns [i];
            int amountSplit = amountWithRates (workPatterns)[i] - amountNeeded;
            int [] patternsSplit = new int [amountSplit]; 
            int [][] temp = prefScores;
            for (int j = 0; j < amountSplit; j++) {
                int max = getSumColumn(0,temp);       
                String IDmax = workPatterns.get(0).getNr();
                for (Nurse pattern : workPatterns) {
                    if (getSumColumn(IDToPrefColumn(pattern.getNr()),temp) > max){
                            max = getSumColumn(IDToPrefColumn(pattern.getNr()),temp);
                            IDmax = pattern.getNr();
                        }   
                }
                patternsSplit[j] = IDToPrefColumn(IDmax);
                for (int k = 0; k < nurses.size(); k++) {
                    temp[IDToPrefColumn(IDmax)][k] = 0;
                }
            }
            int [] amountsPatternsFromSplit = calcAmountPatternsFromSplit(amountSplit,rates[i]);
            int counter = 0;
            for (int j = 0; j < 4; j++) {
                if(amountsPatternsFromSplit[j] != 0){
                    for (int k = 0; k < amountsPatternsFromSplit[j]; k++) {
                    //methode om patterns te splitsen
                    }
                }
            }
        }
    }
    
    public int [] calcScheduleRateAmounts (){
        int [] amountsNurses = amountWithRates (nurses);
        int [] amountsPatterns = amountWithRates (workPatterns);
        int nurses100 = amountsNurses[0];
        int nurses75 = amountsNurses[1];
        int nurses50 = amountsNurses[2];
        int nurses25 = amountsNurses[3];
        int patterns100 = amountsPatterns [0];
        int patterns75 = amountsPatterns [1];
        int patterns50 = amountsPatterns [2];
        int patterns25 = amountsPatterns [3];
        
        if(nurses100 < patterns100){    //als minder nurses met rate 100 dan patterns => split patterns
            int difference = patterns100 - nurses100;
            int [] amountsPatternsFromSplit = calcAmountPatternsFromSplit(difference,100);
            patterns75 += amountsPatternsFromSplit[1];
            patterns50 += amountsPatternsFromSplit[2];
        }
        if(nurses75 < patterns75){
            int difference = patterns75 - nurses75;
            int [] amountsPatternsFromSplit = calcAmountPatternsFromSplit(difference,75);
            patterns50 += amountsPatternsFromSplit[2];
            patterns25 += amountsPatternsFromSplit[3];
        }
        if(nurses50 < patterns50){
            int difference = patterns50 - nurses50;
            int [] amountsPatternsFromSplit = calcAmountPatternsFromSplit(difference,50);
            patterns25 += amountsPatternsFromSplit[3];
        }
        amountsPatterns [0] = patterns100;
        amountsPatterns [1] = patterns75;
        amountsPatterns [2] = patterns50;
        amountsPatterns [3] = patterns25;
        return amountsPatterns;
    }
    
    public int [] calcAmountPatternsFromSplit (int amountSplit, int rate){
        int [] amountsPatternsFromSplit = new int [4]; //0:100, 1:75, 2:50, 3:25
        amountsPatternsFromSplit [0] = 0;  //geen enkele rate kan gesplitst worden in patterns van 100
        if(rate == 100){
            amountsPatternsFromSplit [1] = (int) Math.floor(amountSplit / 3) *4;
            int rest = amountSplit - (int) Math.floor(amountSplit / 3) *3;
            amountsPatternsFromSplit [2] = rest * 2;
        }
        if(rate == 75){
            amountsPatternsFromSplit [2] = (int) Math.floor(amountSplit/2) *3;
            int rest = amountSplit - (int) Math.floor(amountSplit/2) *2;
            amountsPatternsFromSplit [3] = rest * 3;
        }
        if(rate == 50){
            amountsPatternsFromSplit [3] = amountSplit*2;
        }
        return amountsPatternsFromSplit;
    }
    
    public int [] amountWithRates (ArrayList<Nurse> list){
        int [] amounts = new int [4];
        double j = 1.00;
        int counter = 0;
        while (j>0.24) {
            int amount = 0;
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getEmploymentRate() == j)
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
    
    
    
}
