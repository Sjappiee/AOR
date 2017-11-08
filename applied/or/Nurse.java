
package applied.or;

import java.util.ArrayList;
import java.util.Arrays;

public class Nurse {
    
    private String nr;
    private int [] [] binaryDayPlanning;
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

    public Nurse(String nr, int[][] binaryDayPlanning, int type) {
        this.binaryDayPlanning = binaryDayPlanning;
        this.type = type;
        this.nr = nr;
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
    
    public String printBinaryPlanning () {
        
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
    
    public String printPrefs () {
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
        return "Nurse{" + "nr=" + nr + ", binaryDayPlanning=" + printBinaryPlanning() + ", employmentRate=" + employmentRate + ", type=" + type + ", preferenceText=" + preferenceText + ", preferences=" + printPrefs() + '}';
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
    
    
    
   
}
