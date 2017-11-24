
package applied.or;

import java.util.ArrayList;
import java.util.Arrays;

public class Nurse {
    
    private String nr;
    private int [] [] binaryDayPlanning; //[shift][day]
    private float employmentRate;
    private int type;
    private String preferenceText;
    private int [][] preferences;
    private int [] [] MonthlyPreferences; //dit is een 2D array bestaande uit 5 rijen. Elke shift is een rij (vroeg, dag, laat, nacht, vrij) en 28 kolommen (voor elke dag in het schema)


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
        for (int i = 0; i < 7; i++) {
            temp += this.binaryDayPlanning [0][i];
        }
        for (int i = 0; i < 7; i++) {
            temp += this.binaryDayPlanning [1][i];
        }
        }
        
        return temp;
    }
    
    
    
    public String PrefsToString () {
        String temp = "";
        if (this.preferences != null) {
        for (int i = 0; i < 7; i++) {
            temp += this.preferences [0][i];
        }
        for (int i = 0; i < 7; i++) {
            temp += this.preferences [1][i];
        }
        for (int i = 0; i < 7; i++) {
            temp += this.preferences [2][i];
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
        /*for (int i = 0; i < 7; i++) {
            System.out.print(this.binaryDayPlanning [0][i]);
        } System.out.println("");
        for (int i = 0; i < 7; i++) {
            System.out.print(this.binaryDayPlanning [1] [i]);   
        }System.out.println("");*/
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
        /*for (int i = 0; i < 7; i++) {
            System.out.print(this.preferences [0][i]);
        } System.out.println("");
        for (int i = 0; i < 7; i++) {
            System.out.print(this.preferences [1] [i]);   
        } System.out.println("");
        for (int i = 0; i < 7; i++) {
            System.out.print(this.preferences [2] [i]);
        } System.out.println(""); */
        return preferences;
    }
    public int getSpecificPreference(int shift, int day){
        return preferences[shift][day];
    }

    public void setBinaryDayPlanning(int[][] binaryDayPlanning) {
        this.binaryDayPlanning = binaryDayPlanning;
    }
    
    public void setSpecificBinaryToOne (int shift, int index)
    {
        int [] [] temp = new int [2] [7];
        temp = this.binaryDayPlanning;
        
        temp [shift] [index] = 1;
    }
    
        public int getIndexof1 () {
        int [] [] temp = new int [2][7];
        int counterShift1 = 0;
        int counterShift2 = 0;
        int index = 0;
        temp = this.binaryDayPlanning;
        
        for (int i = 0; i < 7; i++) {
            if (temp [0][i] == 0)
            {
                counterShift1++;
            }
            else
            {
                index = counterShift1;
            }
        }
        
        for (int i = 0; i < 7; i++) {
            if (temp [1][i] == 0)
            {
                counterShift2++;
            }
            else
            {
                index=counterShift2;
            }
        }
        return index;
    }
        public ArrayList <Integer> getAllIndexesOf1 ()
        {
            ArrayList <Integer> indexesOfOne = new ArrayList <Integer> ();
            int [] [] temp = this.binaryDayPlanning;
            int counterShift1 = 0;
            int counterShift2 = 0;
            
            for (int i = 0; i < 7; i++) {
                if (temp [0][i] == 0)
                {
                    counterShift1++;
                }
                else
                {
                    indexesOfOne.add(counterShift1);
                }
            }
            for (int i = 0; i < 7; i++) {
                if(temp [1][i] == 0)
                {
                    counterShift2++;
                }
                else 
                {
                    indexesOfOne.add(counterShift2);
                    counterShift2++;
                }
            }
            return indexesOfOne;
        }
    
        public int getShiftType () {
            int counter1 =0;
            int counter2 = 0;
            for (int i = 0; i < 7; i++) {
                   
                if (BinaryPlanningToString().charAt(i) == '1') //tellen hoeveel x 1 en 2 in een werkschema staat
                {
                    
                counter1++;
                }
            }
            for (int i = 7; i < 14; i++) {
                if (BinaryPlanningToString().charAt(i) == '1')
                {
                    
                    counter2++;
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

    public void setEmploymentRate(float employmentRate) {
        this.employmentRate = employmentRate;
    }
        
}
