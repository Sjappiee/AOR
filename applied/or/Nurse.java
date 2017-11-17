
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


    public Nurse(String nr, float employmentRate, int type, String preferenceText, int[][] preferences) {
        this.nr = nr;
        this.binaryDayPlanning = null;
        this.employmentRate = employmentRate;
        this.type = type;
        this.preferenceText = preferenceText;
        this.preferences = preferences;
    }

    public Nurse(String nr, float employmentRate, int[][] binaryDayPlanning, int type) {
        this.binaryDayPlanning = binaryDayPlanning;
        this.type = type;
        this.nr = nr;
        this.employmentRate = employmentRate;
    }

    public Nurse(String nr, int[][] binaryDayPlanning, float employmentRate, int type, String preferenceText, int[][] preferences) {
        this.nr = nr;
        this.binaryDayPlanning = binaryDayPlanning;
        this.employmentRate = employmentRate;
        this.type = type;
        this.preferenceText = preferenceText;
        this.preferences = preferences;
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
    
    @Override
    public String toString() {
        return "Nurse{" + "nr=" + nr + ", binaryDayPlanning=" + BinaryPlanningToString() + ", employmentRate=" + employmentRate + ", type=" + type + ", preferenceText=" + preferenceText + ", preferences=" + PrefsToString() + '}';
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
            if (BinaryPlanningToString().substring(0, 7).equalsIgnoreCase("0000000"))
            {
                return 2;
            }
            else
            {
                return 1;
            }
        }
}
