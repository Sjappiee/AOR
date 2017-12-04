
package applied.or;

import java.util.ArrayList;
import java.util.Arrays;

public class Nurse implements Cloneable {
    int [] rateInDays = {5,4,3,2,1}; //hangt af van SHIFTSYSTEM !!! LIJN 670 en getShiftType () AANPASSEN als we met 3 shifts werken!
    float [] rates = {(float)1.0,(float)0.80,(float)0.60,(float)0.40,(float)0.20};
    int amountShifts = 3; //uniek per systeem
    
    private String nr;
    private int [] [] binaryDayPlanning; //[shift][day]
    private float employmentRate;
    private int type;
    private String preferenceText;
    private int [][] preferences;
    private int [] [] MonthlyPreferences; //dit is een 2D array bestaande uit 5 rijen. Elke shift is een rij (vroeg, dag, laat, nacht, vrij) en 28 kolommen (voor elke dag in het schema)


    public Nurse(String nr, float employmentRate,int[][] binaryDayPlanning, int type, String preferenceText, int[][] preferences, int[][] monthlyPreferences) {
        this.nr = nr;
        this.binaryDayPlanning = binaryDayPlanning;
        this.employmentRate = employmentRate;
        this.type = type;
        this.preferenceText = preferenceText;
        this.preferences = preferences;
        this.MonthlyPreferences = monthlyPreferences;
    }
    
    public Nurse(String nr, float employmentRate, int type, String preferenceText, int[][] preferences) {
        this.nr = nr;
        this.binaryDayPlanning = null;
        this.employmentRate = employmentRate;
        this.type = type;
        this.preferenceText = preferenceText;
        this.preferences = preferences;
        this.MonthlyPreferences = null;
    }

    public Nurse(String nr, float employmentRate, int[][] binaryDayPlanning, int type) {
        this.binaryDayPlanning = binaryDayPlanning;
        this.type = type;
        this.nr = nr;
        this.employmentRate = employmentRate;
        this.MonthlyPreferences = null;
    }

    public Nurse(String nr, int[][] binaryDayPlanning, float employmentRate, int type, String preferenceText, int[][] preferences) {
        this.nr = nr;
        this.binaryDayPlanning = binaryDayPlanning;
        this.employmentRate = employmentRate;
        this.type = type;
        this.preferenceText = preferenceText;
        this.preferences = preferences;
        this.MonthlyPreferences = null;
    }

    public Nurse(String nr, float employmentRate, int type, String preferenceText, int[][] preferences, int[][] MonthlyPreferences) {
        this.nr = nr;
        this.employmentRate = employmentRate;
        this.type = type;
        this.preferenceText = preferenceText;
        this.preferences = preferences;
        this.MonthlyPreferences = MonthlyPreferences;
    }
        public Nurse() {
    }
    
    public String BinaryPlanningToString () {
        
        String temp = "";
        if (this.binaryDayPlanning != null) {
            for (int s = 0; s < amountShifts; s++) {
                for (int i = 0; i < 7; i++) {
                    temp += this.binaryDayPlanning [s][i];
                }
            }
        }
        return temp;
    }

    public String PrefsToString () {
        String temp = "";
        if (this.preferences != null) {
            for (int s = 0; s < amountShifts+1; s++) { //"+1" = free
                for (int i = 0; i < 7; i++) {
                    temp += this.preferences [s][i];
                }
            }
        }
        return temp;
    }
    
    public String MonthlyPrefsToString () {
        String temp = "";
        if (this.MonthlyPreferences != null)
        {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 28; j++) {
                    temp = temp + this.MonthlyPreferences[i][j] + " ";
                }
            }
        }
            return temp;
            }
    
    @Override
    public String toString() {
        return "Nurse{" + "nr=" + nr + ", binaryDayPlanning=" + BinaryPlanningToString() + ", employmentRate=" + employmentRate + ", type=" + type + 
                ", preferenceText=" + preferenceText + ", preferences=" + PrefsToString() +
                ", monthlyPreferences =" + MonthlyPrefsToString() + '}';
        
    }

    public int[][] getMonthlyPreferences() {
        return MonthlyPreferences;
    }

    public void setMonthlyPreferences(int[][] MonthlyPreferences) {
        this.MonthlyPreferences = MonthlyPreferences;
    }

    public String getNr() {
        return nr;
    }

    public int[][] getBinaryDayPlanning() {
        return binaryDayPlanning;
    }

    public float getEmploymentRate() {
        return employmentRate;
    }

    public int getType() {
        return type;
    }

    public String getPreferenceText() {
        return preferenceText;
    }

    public int[][] getPreferences() {
        return preferences;
    }
    public int getSpecificPreference(int shift, int day){
        return preferences[shift][day];
    }

    public void setBinaryDayPlanning(int[][] binaryDayPlanning) {
        this.binaryDayPlanning = binaryDayPlanning;
    }
    
    public void setSpecificBinaryToOne (int shift, int index){
        this.binaryDayPlanning [shift] [index] = 1;
    }
    
    public int getIndexof1 () {
        int index = 0;
        for (int s = 0; s < amountShifts; s++) {
            int counterShift = 0;
            for (int i = 0; i < 7; i++) {
                if (this.binaryDayPlanning [s][i] == 0){
                    counterShift++;
                }
                else{
                    index = counterShift;
                }
            }
        }
        return index;
    }
    
    public ArrayList <Integer> getAllIndexesOf1 (){
        ArrayList <Integer> indexesOfOne = new ArrayList <Integer> ();
        int counterShift1 = 0;
        for (int s = 0; s < amountShifts; s++) {
            counterShift1 = 0;
            for (int i = 0; i < 7; i++) {
                if (this.binaryDayPlanning [s][i] == 0){
                    counterShift1++;
                }
                else{
                    indexesOfOne.add(counterShift1);
                }
            }
        }
        return indexesOfOne;
    }
    
    public int getShiftType () {
        int counter1 =0;
        int counter2 = 0;
        int counter3 = 0;
        ArrayList <Integer> counters = new ArrayList <Integer> ();
        for (int i = 0; i < 7; i++) {
            if (BinaryPlanningToString().charAt(i) == '1'){ //tellen hoeveel x 1 en 2 in een werkschema staat
            counter1++;
            }
        }
        for (int i = 7; i < 14; i++) {
            if (BinaryPlanningToString().charAt(i) == '1'){
                counter2++;
            }
        }
        //3de shift
        for (int i = 14; i < 21; i++) {
            if (BinaryPlanningToString().charAt(i) == '1'){
                counter3++;
            }
        }
        counters.add(counter1);
        counters.add(counter2);
        counters.add(counter3);
        
        int max = counters.get(0);
        int shift = 0;
        
        for (int i = 0; i < amountShifts; i++) {
            if (counters.get(i)> max)
            {
                max = counters.get(i);
                shift = i;
                       
            }
        }
        
        return shift+1;
    }

    public void setEmploymentRate(float employmentRate) {
        this.employmentRate = employmentRate;
    }
     
    @Override
    public Nurse clone(){
        Nurse nurse2 = new Nurse(nr, employmentRate, binaryDayPlanning, type, preferenceText, preferences,MonthlyPreferences);
        return nurse2;
    }
    
}
