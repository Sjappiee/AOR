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
    private ArrayList <Nurse> nurses = new ArrayList <Nurse> ();
    private ArrayList <Nurse> workPatterns = new ArrayList <Nurse> ();
    
    public void setInputFile (String inputFile) { //nodig voor inlezen excel document
        this.inputFile = inputFile;
    }
    
    public ArrayList<Nurse> readAllExceptCyclicSchedule (int sheetNr) throws IOException { //methode om alle data in te lezen EN in de nurse objecten te steken
        File inputWorkbook = new File (inputFile);
        Workbook w;
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            int startRow = searchFirstRow(sheetNr);
            int lastRow = searchLastRow(sheetNr);
            
            for (int i = startRow; i <= lastRow; i++) { //gaat door een rij, kolom per kolom. Dan weer volgende rij enzovoort
                String nr = giveNurseNumber(sheetNr,i);
                //int [] [] dayPlanning = giveBinaryDayPlanning(i);
                float emplRate = giveEmploymentRate(sheetNr,i);
                int type = giveType(sheetNr,i);
                String prefString = givePref(sheetNr,i);
                int [] [] prefNum = giveNumbPref(sheetNr,i);
                
                Nurse Temp = new Nurse (nr, emplRate,type,prefString,prefNum);
                nurses.add(Temp);
            }
            
          /*  for (Nurse nurse : nurses) {
                System.out.println(nurse);
            }*/
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
            int startRow = searchFirstRow(sheetNr);
            int lastRow = searchLastRow(sheetNr);
            
            for (int i = startRow; i <= lastRow; i++) { //gaat door een rij, kolom per kolom. Dan weer volgende rij enzovoort
                
                int [] [] dayPlanning = giveBinaryDayPlanning(sheetNr,i);
                int type = givePatternType(sheetNr,i);
                String nr = givePatternNumber (sheetNr, i);
                
            /*for (int j = 0; j < 7; j++) {
                System.out.print(dayPlanning [0] [j]);
            }
            System.out.println("");
            for (int j = 0; j < 7; j++) {
                System.out.print(dayPlanning [1] [j]);
            }
            System.out.println("");*/
                
                
                Nurse Temp = new Nurse (nr, dayPlanning, type);
                workPatterns.add(Temp);
            }
            
            /*for (Nurse workPattern : workPatterns) {
                System.out.println(Arrays.toString(workPattern.getBinaryDayPlanning()));
            }*/
        }
        catch (BiffException e) {
            e.printStackTrace();
        }
        return workPatterns;
    }
        
    public int searchFirstRow (int sheetNr) throws IOException { //methode voor het vinden van de eerste cell ongeacht department etc.
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
        //System.out.println(startRow);
          return startRow;
    }
    
        public int searchLastRow (int sheetNr) throws IOException { //methode voor het vinden van de laatste cell ongeacht department en hoeveel nurses er daar gepland zijn.
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
        //    System.out.println(lastRow-1);
          return lastRow-1;
    }
        
        public String givePref (int sheetNr, int row) throws IOException {//retourneert de preferentie van nurse in huidige rij
            
        File inputWorkbook = new File (inputFile);
        Workbook w;
        String pref ="";
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            Sheet sheet = w.getSheet(sheetNr); //om eerste sheet te nemen van excel bestand
            Cell cell = sheet.getCell(17, row);
            pref += cell.getContents();
            
        }
        catch (BiffException e) {
              e.printStackTrace();
        }
        //System.out.println(pref);
        return pref;
        }
        
        
        public int giveType (int sheetNr, int row) throws IOException {//retourneert het type van de nurse in de rij
            
        File inputWorkbook = new File (inputFile);
        Workbook w;
        int type = 0;
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            Sheet sheet = w.getSheet(sheetNr); //om eerste sheet te nemen van excel bestand
            Cell cell = sheet.getCell(16, row);
            type = Integer.parseInt(cell.getContents());
            
        }
        catch (BiffException e) {
              e.printStackTrace();
        }
       // System.out.println(type);
        return type;
        }
        
        public int givePatternType (int sheetNr, int row) throws IOException {//retourneert het type van de nurse in de rij
            
        File inputWorkbook = new File (inputFile);
        Workbook w;
        int type = 0;
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            Sheet sheet = w.getSheet(sheetNr); //om eerste sheet te nemen van excel bestand
            Cell cell = sheet.getCell(16, row);
            type = Integer.parseInt(cell.getContents());
            
        }
        catch (BiffException e) {
              e.printStackTrace();
        }
       // System.out.println(type);
        return type;
        }
        
        public float giveEmploymentRate (int sheetNr, int row) throws IOException {//retourneert de employmentrate van de nurse in de rij
            
        File inputWorkbook = new File (inputFile);
        Workbook w;
        float EmploymentRate =0;
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            Sheet sheet = w.getSheet(sheetNr); //om eerste sheet te nemen van excel bestand
            Cell cell = sheet.getCell(15, row);
            EmploymentRate = Float.parseFloat(cell.getContents());
            
        }
        catch (BiffException e) {
              e.printStackTrace();
        }
       // System.out.println(EmploymentRate);
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
        //System.out.println(number);
        return number;
        }
        
        public String givePatternNumber (int sheetNr, int row) throws IOException {//retourneert het nursenummer van de nurse in de rij
            
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
        //System.out.println(number);
        return number;
        }
        
        public int [] [] giveBinaryDayPlanning (int sheetNr, int row) throws IOException {//retourneert de dagplanning van de nurse in de rij.
            //OPGELET!!!! DIT WORDT GEGEVEN ALS STRING OMDAT IK ALS INTEGER HET NIET VOND!!!
            
        File inputWorkbook = new File (inputFile);
        Workbook w;
        int rows = 2;
        int columns = 7;
        int [] [] binaryPlanning = new int [rows] [columns];
        int counter1 = 0;
        int counter2 = 0;
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            Sheet sheet = w.getSheet(sheetNr); //om eerste sheet te nemen van excel bestand
            
            for (int j = 1; j < 14; j= j+2) {
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
            
            for (int j = 2; j <15 ; j= j +2) {
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
        }

        catch (BiffException e) {
              e.printStackTrace();
        }

         /*   for (int j = 0; j < columns; j++) {
                System.out.print(binaryPlanning [0] [j]);
            }
            System.out.println("");
            for (int j = 0; j < columns; j++) {
                System.out.print(binaryPlanning [1] [j]);
            }
            System.out.println("");*/
            
        return binaryPlanning;
        }
        
        public int[][] giveNumbPref (int sheetNr, int row) throws IOException {
            
        File inputWorkbook = new File (inputFile);
        Workbook w;
        int [][] preferences = new int [3][7];
        
        int counter1 = 0;
        int counter2 = 0;
        int counter3 = 0;
        
        try{
            w = Workbook.getWorkbook(inputWorkbook); //workbook in java initialiseren 
            Sheet sheet = w.getSheet(sheetNr); //om eerste sheet te nemen van excel bestand
            for (int k=18;k<39;k++){
               Cell cell = sheet.getCell(k, row);
               
               if (k%3==0){ //veelvoud van 3, is shift 1
                   preferences[0][counter1] = Integer.parseInt(cell.getContents());
                   counter1+=1;
               }
               else if (k%3==1){ //shift 2
                  preferences[1][counter2] = Integer.parseInt(cell.getContents()); 
                   counter2+=1;
                } 
               
               else { //free shift
                  preferences[2][counter3] = Integer.parseInt(cell.getContents());    
                   counter3+=1;
                }
               
               
            }
        }
        catch (BiffException e) {
              e.printStackTrace();
        }

        return preferences;
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
