
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
        //werkt voor 1 schema en nurse. MAAR: hoort empl rate bij een bepaald schema of een bepaalde wens van een nurse? Ik zou zeggen bij wens nurse en we kunnen dit al dan niet voorzien als de nood er is gezien input van de rest.
     // nog for loop maken en elk workpattern overlopen  
        // if listMinScore is lijst met alle nurses, allen met pref = 1000, dan zijn alle nurses opgeruikt => maak nieuwe nurse aan
        // !!! prefscore moet <10 opdat de nurse aan dat pattern mag worden assigned => nakijken of dit met deze pref kosten zo zou uitkomen
        ArrayList <Nurse> temp= listMinScore(0);        // lijst met nurses die min prefscores bij een bepaald workpattern
        //nu uit deze lijst zoeken naar de nurse die het moeilijkste in te plannen is (dus de max prefscore som voor alle workpatterns heeft)
        int max = getSumRow(IDToPrefRow(temp.get(0).getNr())-1);       
        String IDmax = temp.get(0).getNr();
        
        for (Nurse nurse : temp) {
            if (getSumRow(IDToPrefRow(nurse.getNr())-1) > max)
                    {
                        max = getSumRow(IDToPrefRow(nurse.getNr())-1);
                        IDmax = nurse.getNr();
                    }   
        }
        // deze nurse word gekoppeld aan het workpattern
        nurses.get(IDToPrefRow(IDmax)-1).setBinaryDayPlanning(workPatterns.get(0).getBinaryDayPlanning());
        
        for (int i = 0; i < workPatterns.size(); i++) {
            prefScores[i][IDToPrefRow(IDmax)-1] = 1000;
            }
        for (int i = 0; i < 47; i++) {
            for (int j = 0; j < 47; j++) {
            System.out.print(prefScores[j][i] +" ");
            }
            System.out.println("");
        }
        int [][] binaryPlanning = nurses.get(IDToPrefRow(IDmax)-1).getBinaryDayPlanning();
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
        
       return null;
    }
    
    public int IDToPrefRow (String ID){     // de laatste 2 string elementen uit het ID omzetten naar een int, dit getal - 1 is dan de index van die nurse in de nurses lijst
        int row;
        row = Integer.parseInt(ID.substring(ID.length()-2));
        return row;
    }
    
    public ArrayList <Nurse> listMinScore (int scheduleNr,  int [][] prefScores) { 
//te maken: een if zodat enkel nurse into consideration komen die ook dezelfde OF MEER employment rate hebben!        
//lijst van nurses die allen dezelfde min prefscore hebben bij een geg patroon
        ArrayList <Nurse> nursesLowScore = new ArrayList <Nurse> ();
        int min = getMinOfColumn (scheduleNr);

        for (int i = 0; i < nurses.size(); i++) { //gaat voor 1 schedule door alle nurses
            if (prefScores [scheduleNr] [i] == min && nurses.get(i).getEmploymentRate() >= EmploymentRateSchedule(scheduleNr)) {
                nursesLowScore.add(nurses.get(i));
            }
        }     
        return nursesLowScore;
    }
       
        public ArrayList <Nurse> listMinScore (int scheduleNr) { 
        ArrayList <Nurse> nursesLowScore = new ArrayList <Nurse> ();
        int min = getMinOfColumn (scheduleNr);

        for (int i = 0; i < nurses.size(); i++) { //gaat voor 1 schedule door alle nurses
            if (prefScores [scheduleNr] [i] == min && nurses.get(i).getEmploymentRate() >= EmploymentRateSchedule(scheduleNr)) {
                nursesLowScore.add(nurses.get(i));
            }
        }
            for (Nurse nurse : nursesLowScore) {
                System.out.println(nurse);
            }
        return nursesLowScore;
    }
    
    public int getMinOfColumn (int column) { //minimum van kolom vinden
        int [] [] temp = prefScoreCalculation();
        int min = temp [column] [0];
        for (int i = 0; i < nurses.size(); i++) {
            if (temp [column] [i] < min)
                min = temp [column] [i];
        }
        System.out.println(min);
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
    
    
    public int [][] prefScoreCalculation (){ //in orde! 
        int [][] temp = new int [workPatterns.size()][nurses.size()];
        for (int i = 0; i < workPatterns.size(); i++) {
            int [][] workPattern = workPatterns . get(i).getBinaryDayPlanning();
            //calc workrate for the pattern i
            double amountWorkDays = 0;
            for (int k = 0; k < 7; k++) {
                for (int l = 0; l < 2; l++) {
                    if(workPattern [l][k] !=0){
                        amountWorkDays +=1.0;
                    }
                }
            }
            double fulltime = 4.0;    //bij 2 SHIFT system: "fulltime" = 4workdays, bij 3 shift system: 5
            double workRate = amountWorkDays/fulltime;            
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
