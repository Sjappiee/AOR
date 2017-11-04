
package applied.or;

import java.util.ArrayList;

public class Schedule {
    private ArrayList <Nurse> nurses = new ArrayList <Nurse> ();
    private ArrayList <Nurse> workPatterns = new ArrayList <Nurse> ();


    public Schedule(ArrayList<Nurse> nurses, ArrayList<Nurse> workPatterns) {
        this.nurses = nurses;
        this.workPatterns = workPatterns;
    }
    
    public ArrayList<Nurse> schedulingProcess (){
        
        
        
       //zoek per workPattern de nurse met min prefScore en koppel dan dat work pattern aan die nurse
       //welke nurse als er meerderen met dezelfde min score zijn?
       //volgorde van welke patterns je eerst aan een nurse koppelt beïnvloed welke nurses over zijn voor de patterns er na
       //   => totale eindkost is sterk afhankelijk van volgorde ! (oplossing zoeken) 
       // 2e schifting vinden voor nurses die gelijke score hebben! Zodat dit zeker niet afhankelijk is van nurse nummer
       return null;
    }
    
    public ArrayList <Nurse> ListMinScore (int workSchedule[][], int scheduleNr ) { //aparte lijst wordt nu bijgehouden met alle nurses in die de laagste preScore hebben. Afhankelijk van input schedule
        //moeten zeker nog iets vinden om bij creatie van dat schedule bepaalde workschedules EN nurses eruit te halen!!!
        
        ArrayList <Nurse> nursesLowScore = new ArrayList <Nurse> ();
        int min = getMinOfColumn (scheduleNr);
        for (int i = 0; i < nurses.size(); i++) { //gaat voor 1 schedule door alle nurses
            if (workSchedule [scheduleNr] [i] == min) {
                nursesLowScore.add(nurses.get(i));
            }
        }
       /* for (Nurse nurse : nursesLowScore) {
            System.out.println(nurse);
        }*/
        return nursesLowScore;
    }
    
    public int getMinOfColumn (int column) { //minimum van kolom vinden
        int [] [] temp = prefScoreCalculation();
        int min = temp [column] [0];
        for (int i = 0; i < nurses.size(); i++) {
            if (temp [column] [i] < min)
                min = temp [column] [i];
        }
        return min;
    }
    
    public int [][] prefScoreCalculation (){ //in orde! 
        // !!!!!! Denk wel dat we hier parameters gebruikt moeten worden zodat we een schema maken dat gebaseerd is op huidige input en niet op de originele input. 
        //BV: als schema 2,3,5 en nurse 32, 4, 12 er al uit zijn moeten deze nurses en schema's er ook al uit gaan aangezien andere methodes op deze input verder werken!!!!!
        //System.out.println(workPatterns.size() + " " + nurses.size());
        int [][] prefScores = new int [workPatterns.size()][nurses.size()]; //workPatterns are columns, nurses are rows
        for (int i = 0; i < workPatterns.size(); i++) {
            int [][] workPattern = workPatterns . get(i).getBinaryDayPlanning();
            for (int j = 0; j < nurses.size(); j++) {
                int[][] nursePref = nurses.get(j).getPreferences();
                int score = 0;
//                for (int k = 0; k < nursePref[0].length; k++) {         //shift 1, score voor elke dag (k) optellen
//                    score += nursePref[0][k] * workPattern[0][k];
//                }
//                for (int k = 0; k < nursePref[1].length; k++) {         //shift 2, score voor elke dag (k) optellen
//                    score += nursePref[1][k] * workPattern[1][k];
//                }
//                for (int k = 0; k < nursePref[2].length; k++) {         //free , score voor elke dag (k) optellen
//                    int free=0;     // 0 if not free, 1 if free
//                    if(workPattern [l][k] == 0 && workPattern [l][k] == 0){
//                        free = 1;
//                    }
//                    score += free*nursePref[2][k];
//                }
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
                prefScores[i][j] = score;
            }
        }
       /* for (int i = 0; i < nurses.size(); i++) {
            for (int j = 0; j < workPatterns.size(); j++) {
                System.out.print(prefScores [i] [j]);
            }
            System.out.println("");
        }*/
        return prefScores;
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
    
    
    
}
