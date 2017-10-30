
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

    public Nurse(int[][] binaryDayPlanning) {
        this.binaryDayPlanning = binaryDayPlanning;
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

    @Override
    public String toString() {
        return "Nurse{" + "nr=" + nr + ", binaryDayPlanning=" + Arrays.toString(binaryDayPlanning) + ", employmentRate=" + employmentRate + ", type=" + type + ", preferenceText=" + preferenceText + ", preferences=" + Arrays.toString(preferences) + '}';
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
    
    
    
   
}
