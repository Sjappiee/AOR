/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applied.or;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 *
 * @author stephan
 */
public class ExcellReader {
    private String inputFile;
    private ArrayList <Nurse> nurses;
    private ArrayList <Nurse> workPatterns;
    int [] rateInDays = {4,3,2,1}; //AANPASSEN NAAR SHIFTSYSTEM
    int amountShifts = 3;
    
    
    public void setInputFile (String inputFile) { //nodig voor inlezen excel document
        this.inputFile = inputFile;
        this.nurses = new ArrayList <Nurse> ();
        this.workPatterns = new ArrayList <Nurse> ();
    }
    
    public ArrayList<Nurse> readAllExceptCyclicSchedule (int sheetNr) throws IOException { //methode om alle data in te lezen EN in de nurse objecten te steken
        File inputWorkbook = new File (inputFile);
        Workbook w;
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            int startRow = searchFirstRowNurse(sheetNr);
            int lastRow = searchLastRowNurse(sheetNr);
            
            for (int i = startRow; i <= lastRow; i++) { //gaat door een rij, kolom per kolom. Dan weer volgende rij enzovoort
                String nr = giveNurseNumber(sheetNr,i);
                //int [] [] dayPlanning = giveBinaryDayPlanning(i);
                float emplRate = giveEmploymentRate(sheetNr,i);
                int type = giveType(sheetNr,i);
                String prefString = givePref(sheetNr,i);
                int [] [] prefNum = giveNumbPref(sheetNr,i);
                int [] [] monthPreferences = giveMonthPref(sheetNr, i);

                
                Nurse Temp = new Nurse (nr, emplRate,type,prefString, prefNum, monthPreferences);
                nurses.add(Temp);
            }
            
//            for (Nurse nurse : nurses) {
//                System.out.println(nurse);
//            }
        }
        catch (BiffException e) {
            e.printStackTrace();
        }
        return nurses;
    }
    
        public ArrayList <Nurse> readWorkPatterns (int sheetNr) throws IOException { //methode om alle data in te lezen EN in de nurse objecten te steken
        // AANPASSEn: juiste first en lastrow methode
            File inputWorkbook = new File (inputFile);
        Workbook w;
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            int startRow = searchFirstRowWorkSchedule(sheetNr);
            int lastRow = searchLastRowWorkSchedule(sheetNr);
            
            for (int i = startRow; i <= lastRow; i++) { //gaat door een rij, kolom per kolom. Dan weer volgende rij enzovoort
                
                int [] [] dayPlanning = giveBinaryDayPlanning(sheetNr,i);
                int type = givePatternType(sheetNr,i);
                String nr = givePatternNumber (sheetNr, i);
                float rate = calcPatternRate (dayPlanning);
                
//            for (int j = 0; j < 7; j++) {
//                System.out.print(dayPlanning [0] [j]);
//            }
//            System.out.println("");
//            for (int j = 0; j < 7; j++) {
//                System.out.print(dayPlanning [1] [j]);
//            }
//            System.out.println("");
//                
                
                Nurse Temp = new Nurse (nr, rate, dayPlanning, type);
                workPatterns.add(Temp);
//            }
//            
//            for (Nurse workPattern : workPatterns) {
//                System.out.println(Arrays.toString(workPattern.getBinaryDayPlanning()));
            }
        }
        catch (BiffException e) {
            e.printStackTrace();
        }
        return workPatterns;
    }
        
    public int searchFirstRowNurse (int sheetNr) throws IOException { //methode voor het vinden van de eerste cell ongeacht department etc.
        // zelfde methode nog eens maar dan voor worpatterns + in excel kollom toev
        File inputWorkbook = new File (inputFile);
        Workbook w;
        int startRow = 0;
        int startColumn = 0;
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            Sheet sheet = w.getSheet(sheetNr); //om eerste sheet te nemen van excel bestand
            int i = 0;
            while (i < sheet.getRows())
            {
                Cell cell = sheet.getCell (startColumn, i);
                if (cell.getContents().contains("30"))
                {   
                    startRow = i;
                    i = i + 1000000000;
                }
                else {
                    i++;
                }
            }
          }
          catch (BiffException e) {
              e.printStackTrace();
        }
//        System.out.println(startRow);
          return startRow;
    }
    
    public int searchFirstRowWorkSchedule (int sheetNr) throws IOException { //methode voor het vinden van de eerste cell ongeacht department etc.
        // zelfde methode nog eens maar dan voor worpatterns + in excel kollom toev
        File inputWorkbook = new File (inputFile);
        Workbook w;
        int startRow = 0;
        int startColumn = 1;
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            Sheet sheet = w.getSheet(sheetNr); //om eerste sheet te nemen van excel bestand
            int i = 0;
            while (i < sheet.getRows())
            {
                Cell cell = sheet.getCell (startColumn, i);
                if (cell.getContents().contains("WS"))
                {   
                    startRow = i;
                    i = i + 1000000000;
                }
                else {
                    i++;
                }
            }
          }
          catch (BiffException e) {
              e.printStackTrace();
        }
        //System.out.println(startRow);
          return startRow;
    }
    
        public int searchLastRowNurse (int sheetNr) throws IOException { //methode voor het vinden van de laatste cell ongeacht department en hoeveel nurses er daar gepland zijn.
        // zelfde methode nog eens maar dan voor worpatterns + in excel kollom toev
            File inputWorkbook = new File (inputFile);
        Workbook w;
        int lastRow = 0;
        int startColumn = 0;
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            Sheet sheet = w.getSheet(sheetNr); //om eerste sheet te nemen van excel bestand
            int i = 0;
            while (i < sheet.getRows())
            {
                Cell cell = sheet.getCell (startColumn, i);
                if (cell.getContents().contains("Scheduled"))
                {   
                    lastRow = i;
                    i = i + 1000000000;
                }
                else {
                    i++;
                }
            }
          }
          catch (BiffException e) {
              e.printStackTrace();
        }
//        System.out.println(lastRow-1);
          return lastRow-1;
    }
        public int searchLastRowWorkSchedule (int sheetNr) throws IOException { //methode voor het vinden van de laatste cell ongeacht department en hoeveel nurses er daar gepland zijn.
        // zelfde methode nog eens maar dan voor worpatterns + in excel kollom toev
            File inputWorkbook = new File (inputFile);
        Workbook w;
        int lastRow = 0;
        int startColumn = 1;
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            Sheet sheet = w.getSheet(sheetNr); //om eerste sheet te nemen van excel bestand
            int i = 0;
            while (i < sheet.getRows())
            {
                Cell cell = sheet.getCell (startColumn, i);
                if (cell.getContents().contains("Scheduled"))
                {   
                    lastRow = i;
                    i = i + 1000000000;
                }
                else {
                    i++;
                }
            }
          }
          catch (BiffException e) {
              e.printStackTrace();
        }
        //System.out.println(lastRow-1);
          return lastRow-1;
    }
        
        public String givePref (int sheetNr, int row) throws IOException {//retourneert de preferentie van nurse in huidige rij
            
        File inputWorkbook = new File (inputFile);
        Workbook w;
        String pref ="";
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            Sheet sheet = w.getSheet(sheetNr); //om eerste sheet te nemen van excel bestand
            Cell cell = sheet.getCell(26, row); // 2shifts: 19, 3shifts: 26
            pref += cell.getContents();
            
        }
        catch (BiffException e) {
              e.printStackTrace();
        }
//        System.out.println(pref);
        return pref;
        }
        
        
        public int giveType (int sheetNr, int row) throws IOException {//retourneert het type van de nurse in de rij
            
        File inputWorkbook = new File (inputFile);
        Workbook w;
        int type = 0;
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            Sheet sheet = w.getSheet(sheetNr); //om eerste sheet te nemen van excel bestand
            Cell cell = sheet.getCell(25, row); //2shifts: 18, 3shifts 25
            type = Integer.parseInt(cell.getContents());
            
        }
        catch (BiffException e) {
              e.printStackTrace();
        }
//       System.out.println(type);
        return type;
        }
        
        public int givePatternType (int sheetNr, int row) throws IOException {//retourneert het type van de nurse in de rij
            
        File inputWorkbook = new File (inputFile);
        Workbook w;
        int type = 0;
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            Sheet sheet = w.getSheet(sheetNr); //om eerste sheet te nemen van excel bestand
            Cell cell = sheet.getCell(2, row);
            type = Integer.parseInt(cell.getContents());
            
        }
        catch (BiffException e) {
              e.printStackTrace();
        }
//       System.out.println(type);
        return type;
        }
        
        public float giveEmploymentRate (int sheetNr, int row) throws IOException {//retourneert de employmentrate van de nurse in de rij
            
        File inputWorkbook = new File (inputFile);
        Workbook w;
        float EmploymentRate =0;
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            Sheet sheet = w.getSheet(sheetNr); //om eerste sheet te nemen van excel bestand
            Cell cell = sheet.getCell(24, row); //2shifts: 17, 3shifts 24
            EmploymentRate = Float.parseFloat(cell.getContents());
            
        }
        catch (BiffException e) {
              e.printStackTrace();
        }
//       System.out.println(EmploymentRate);
        return EmploymentRate;
        }
        
        public String giveNurseNumber (int sheetNr, int row) throws IOException {//retourneert het nursenummer van de nurse in de rij
            
        File inputWorkbook = new File (inputFile);
        Workbook w;
        String number ="";
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            Sheet sheet = w.getSheet(sheetNr); //om eerste sheet te nemen van excel bestand
            Cell cell = sheet.getCell(0, row);
            number = cell.getContents();
            
        }
        catch (BiffException e) {
              e.printStackTrace();
        }
//        System.out.println(number);
        return number;
        }
        
        public String givePatternNumber (int sheetNr, int row) throws IOException {//retourneert het nursenummer van de nurse in de rij
            
        File inputWorkbook = new File (inputFile);
        Workbook w;
        String number ="";
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            Sheet sheet = w.getSheet(sheetNr); //om eerste sheet te nemen van excel bestand
            Cell cell = sheet.getCell(1, row);
            number = cell.getContents();
            
        }
        catch (BiffException e) {
              e.printStackTrace();
        }
//        System.out.println(number);
        return number;
        }
        
        public int [] [] giveBinaryDayPlanning (int sheetNr, int row) throws IOException {//retourneert de dagplanning van de nurse in de rij.
           
            
        File inputWorkbook = new File (inputFile);
        Workbook w;
        int rows = amountShifts;
        int columns = 7;
        int [] [] binaryPlanning = new int [rows] [columns];
        int counter1 = 0;
        int counter2 = 0;
        int counter3 = 0;
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            Sheet sheet = w.getSheet(sheetNr); //om eerste sheet te nemen van excel bestand
            
            for (int j = 3; j < 22; j= j+amountShifts) {//2shifts: j<16, 3shifts: j<23
                Cell cell = sheet.getCell(j, row);
                if (cell.getContents().isEmpty()) {
                    binaryPlanning [0] [counter1] = 0;
                }
                else
                {
                binaryPlanning [0] [counter1] = Integer.parseInt(cell.getContents());
                }
                counter1++;
            }
            
            for (int j = 4; j <23 ; j= j +amountShifts) {//2shifts: j<17, 3shifts: j<24
                Cell cell = sheet.getCell(j, row);
                if (cell.getContents().isEmpty()) 
                {
                    binaryPlanning [1] [counter2] = 0;
                }
                else 
                {
                    binaryPlanning [1] [counter2] = Integer.parseInt (cell.getContents());
                }
                counter2++;
            }
            // 3DE SHIFT
            for (int j = 5; j <24 ; j= j +3) {
                Cell cell = sheet.getCell(j, row);
                if (cell.getContents().isEmpty()) 
                {
                    binaryPlanning [2] [counter3] = 0;
                }
                else 
                {
                    binaryPlanning [2] [counter3] = Integer.parseInt (cell.getContents());
                }
                counter3++;
            }
        }

        catch (BiffException e) {
              e.printStackTrace();
        }

//            for (int j = 0; j < columns; j++) {
//                System.out.print(binaryPlanning [0] [j]);
//            }
//            System.out.println("");
//            for (int j = 0; j < columns; j++) {
//                System.out.print(binaryPlanning [1] [j]);
//            }
//            System.out.println("");
            
        return binaryPlanning;
        }
        
        public int[][] giveNumbPref (int sheetNr, int row) throws IOException {
            
        File inputWorkbook = new File (inputFile);
        Workbook w;
        int [][] preferences = new int [amountShifts+1][7]; //2shifts: 3, 3shifts
        
        int counter1 = 0;
        int counter2 = 0;
        int counter3 = 0;
        int counter4 = 0;
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            Sheet sheet = w.getSheet(sheetNr); //om eerste sheet te nemen van excel bestand
            for (int k=27;k<54;k++){ //2shifts: k=20; k<41, 3shifts: k=27; k< 54
               Cell cell = sheet.getCell(k, row);
               
               if (k%(amountShifts+1)==amountShifts){ //veelvoud van 3, is shift 1
                   preferences[0][counter1] = Integer.parseInt(cell.getContents());
                   counter1+=1;
               }
               else if (k%(amountShifts+1)==0){ //shift 2
                  preferences[1][counter2] = Integer.parseInt(cell.getContents()); 
                   counter2+=1;
                }
               //3de shift
               else if (k%(amountShifts+1)==1){
                   preferences[2][counter4] = Integer.parseInt(cell.getContents()); 
                   counter4+=1;
               }
               
               else { //free shift
                  preferences[3][counter3] = Integer.parseInt(cell.getContents());    
                   counter3+=1;
                }
               
               
            }
        }
        catch (BiffException e) {
              e.printStackTrace();
        }
        
//        for (int j = 0; j < 7; j++) {
//                System.out.print(preferences [0] [j]);
//            }
//            System.out.print("     ");
//            for (int j = 0; j < 7; j++) {
//                System.out.print(preferences [1] [j]);
//            }
//            System.out.print("     ");
//            for (int j = 0; j < 7; j++) {
//                System.out.print(preferences [2] [j]);
//            }
//            System.out.print("     ");
//            for (int j = 0; j < 7; j++) {
//                System.out.print(preferences [3] [j]);
//            }
//            System.out.println("");
//            
        return preferences;
        }
        
        public float calcPatternRate (int [] [] dayPlanning){
            int rateInDaysint = 0;
            double rate = 0;
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < amountShifts; j++) {
                    if(dayPlanning[j][i] == 1){
                        rateInDaysint++;
                    }
                }
            }
            if(rateInDaysint == rateInDays[0]) rate = 1.0; // afhankelijk van SHIFTSYSTEM
            if(rateInDaysint == rateInDays[1]) rate = 0.75;
            if(rateInDaysint == rateInDays[2]) rate = 0.50;
            if(rateInDaysint == rateInDays[3]) rate = 0.25;
            
            return (float)rate;
        }
        
        //checken!!!
               
        public int[][] giveMonthPref (int sheetNr, int row) throws IOException {
            
        File inputWorkbook = new File (inputFile);
        Workbook w;
        int [][] monthPreferences = new int [5][28];
        
        int counter1 = 0;
        int counter2 = 0;
        int counter3 = 0;
        int counter4 = 0;
        int counter5 = 0;
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            Sheet sheet = w.getSheet(sheetNr); 
            
            //om eerste sheet te nemen van excel bestand
            for (int k= 41;k<181;k++){
               Cell cell = sheet.getCell(k, row);
               
               if (k%5==1){ //41 early
                   monthPreferences[0][counter1] = Integer.parseInt(cell.getContents());
                   counter1+=1;
               }
               else if (k%5==2){ //42 day
                  monthPreferences[1][counter2] = Integer.parseInt(cell.getContents()); 
                   counter2+=1;
                } 
               else if (k%5==3){ //43 late
                  monthPreferences[2][counter3] = Integer.parseInt(cell.getContents()); 
                   counter3+=1;
                } 
               else if (k%5==4){ //44 night
                  monthPreferences[3][counter4] = Integer.parseInt(cell.getContents()); 
                   counter4+=1;
                } 
               else { //45 free
                  monthPreferences[4][counter5] = Integer.parseInt(cell.getContents());    
                   counter5+=1;
                }
               
            } 
        }
        catch (BiffException e) {
              e.printStackTrace();
        }
//            for (int j = 0; j < 5; j++) {
//                
//            
//            for (int i = 0; i < 28; i++) {
//                
//                System.out.print(monthPreferences [j] [i] + " ");
//            }  System.out.println("");
//            }
        
        return monthPreferences;
    }

        /*
        
        TE MAKEN
        
        -give preference OK
        give type OK
        -give employment rate OK
        -give number nurse OK
        -give binairy schedule OK
        */

    public ArrayList<Nurse> getNurses() {
        return nurses;
    }
}
