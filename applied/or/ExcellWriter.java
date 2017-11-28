/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applied.or;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *
 * @author Lissa
 */
public class ExcellWriter {

    public void writeScheduleToExcel(ArrayList<Nurse> Nurses1, ArrayList<Nurse> Nurses2, String schema1, String schema2, int dep) throws IOException, WriteException {
        File f = new File("C:\\TEST AOR\\OutputSchedule.xls");
        WritableWorkbook myexcel = Workbook.createWorkbook(f);
        String departement = null;
        if (dep == 0) {
            departement = "A";
        }
        if (dep == 1) {
            departement = "B";
        }
        if (dep == 2) {
            departement = "C";
        }
        if (dep == 3) {
            departement = "D";
        }
        WritableSheet mysheet = myexcel.createSheet("Case E MonthlyRosterDep" + departement + " Output", dep);

        int nNurses1 = Nurses1.size();
        int nNurses2 = Nurses2.size();

        //rij 1 om de week te zetten
        for (int w = 0; w < 4; w++) {
            Label headerweek = new Label(1 + 21 * w, 0, "Week " + (w + 1));
            mysheet.addCell(headerweek);

        }
        //rij 2 om de dagen van de week te zetten
        for (int j = 0; j < 4; j++) {

            Label headerdag1 = new Label(21 * j + 1, 1, "Mon");
            mysheet.addCell(headerdag1);
            Label headerdag2 = new Label(21 * j + 4, 1, "Tue");
            mysheet.addCell(headerdag2);
            Label headerdag3 = new Label(21 * j + 7, 1, "Wed");
            mysheet.addCell(headerdag3);
            Label headerdag4 = new Label(21 * j + 10, 1, "Thu");
            mysheet.addCell(headerdag4);
            Label headerdag5 = new Label(21 * j + 13, 1, "Fri");
            mysheet.addCell(headerdag5);
            Label headerdag6 = new Label(21 * j + 16, 1, "Sat");
            mysheet.addCell(headerdag6);
            Label headerdag7 = new Label(21 * j + 19, 1, "Sun");
            mysheet.addCell(headerdag7);

        }
        //rij 3 als onderonderheader, om de shiften per dag te zetten
        Label headernummer = new Label(0, 2, "Personeelsnummer");
        mysheet.addCell(headernummer);
        for (int s = 0; s < 28; s++) {
            Label headershift1 = new Label(3 * s + 1, 2, "D");
            mysheet.addCell(headershift1);
            Label headershift2 = new Label(3 * s + 2, 2, "N");
            mysheet.addCell(headershift2);
            Label headershift3 = new Label(3 * s + 3, 2, "F");
            mysheet.addCell(headershift3);
        }
        Label headerrate = new Label(85, 2, "Employment rate");
        mysheet.addCell(headerrate);
        Label headertype = new Label(86, 2, "Type nurse");
        mysheet.addCell(headertype);

        for (int w = 0; w < 4; w++) {
            for (Nurse nurseA1 : Nurses1) {
                String Nr = nurseA1.getNr();
                float EmploymentRate = nurseA1.getEmploymentRate();
                int Type = nurseA1.getType();
                int index = Nurses1.indexOf(nurseA1);

                //rij 4 en verder als echte data
                Label l = new Label(0, index + 3, Nr);
                mysheet.addCell(l);
                //planning

                for (int j = 0; j < 7; j++) {
                    char shift = schema1.charAt(nNurses1 * w * 8 + j * 1 + index * 8);
                    //DAY
                    if (shift == '2') {
                        String planD = "1"; //set D
                        Label l2 = new Label(w * 21 + j * 3 + 1, index + 3, planD);
                        mysheet.addCell(l2);
                        String planN = "0"; //set N
                        Label l3 = new Label(w * 21 + j * 3 + 2, index + 3, planN);
                        mysheet.addCell(l3);
                        String planF = "0"; //set F
                        Label l4 = new Label(w * 21 + j * 3 + 3, index + 3, planF);
                        mysheet.addCell(l4);
                    }
                    //NIGHT
                    if (shift == '1') {
                        String planD = "0";
                        Label l2 = new Label(w * 21 + j * 3 + 1, index + 3, planD);
                        mysheet.addCell(l2);
                        String planN = "1";
                        Label l3 = new Label(w * 21 + j * 3 + 2, index + 3, planN);
                        mysheet.addCell(l3);
                        String planF = "0";
                        Label l4 = new Label(w * 21 + j * 3 + 3, index + 3, planF);
                        mysheet.addCell(l4);
                    }
                    //FREE
                    if (shift == '0') {
                        String planD = "0";
                        Label l2 = new Label(w * 21 + j * 3 + 1, index + 3, planD);
                        mysheet.addCell(l2);
                        String planN = "0";
                        Label l3 = new Label(w * 21 + j * 3 + 2, index + 3, planN);
                        mysheet.addCell(l3);
                        String planF = "1";
                        Label l4 = new Label(w * 21 + j * 3 + 3, index + 3, planF);
                        mysheet.addCell(l4);
                    }
                }

                String Rate = String.valueOf(EmploymentRate);
                Label l5 = new Label(85, index + 3, Rate);
                mysheet.addCell(l5);
                String type = String.valueOf(Type);
                Label l6 = new Label(86, index + 3, type);
                mysheet.addCell(l6);
            }
        }
        for (int w = 0; w < 4; w++) {
            for (Nurse nurseA2 : Nurses2) {
                String Nr = nurseA2.getNr();
                float EmploymentRate = nurseA2.getEmploymentRate();
                int Type = nurseA2.getType();
                int index = Nurses2.indexOf(nurseA2);

                //rij 4 en verder als echte data
                Label l = new Label(0, nNurses1 + index + 3, Nr);
                mysheet.addCell(l);
                //planning

                for (int j = 0; j < 7; j++) {
                    char shift = schema2.charAt(nNurses2 * w * 8 + j * 1 + index * 8);
                    //DAY
                    if (shift == '2') {
                        String planD = "1"; //set D
                        Label l2 = new Label(w * 21 + j * 3 + 1, nNurses1 + index + 3, planD);
                        mysheet.addCell(l2);
                        String planN = "0"; //set N
                        Label l3 = new Label(w * 21 + j * 3 + 2, nNurses1 + index + 3, planN);
                        mysheet.addCell(l3);
                        String planF = "0"; //set F
                        Label l4 = new Label(w * 21 + j * 3 + 3, nNurses1 + index + 3, planF);
                        mysheet.addCell(l4);
                    }
                    //NIGHT
                    if (shift == '1') {
                        String planD = "0";
                        Label l2 = new Label(w * 21 + j * 3 + 1, nNurses1 + index + 3, planD);
                        mysheet.addCell(l2);
                        String planN = "1";
                        Label l3 = new Label(w * 21 + j * 3 + 2, nNurses1 + index + 3, planN);
                        mysheet.addCell(l3);
                        String planF = "0";
                        Label l4 = new Label(w * 21 + j * 3 + 3, nNurses1 + index + 3, planF);
                        mysheet.addCell(l4);
                    }
                    //FREE
                    if (shift == '0') {
                        String planD = "0";
                        Label l2 = new Label(w * 21 + j * 3 + 1, nNurses1 + index + 3, planD);
                        mysheet.addCell(l2);
                        String planN = "0";
                        Label l3 = new Label(w * 21 + j * 3 + 2, nNurses1 + index + 3, planN);
                        mysheet.addCell(l3);
                        String planF = "1";
                        Label l4 = new Label(w * 21 + j * 3 + 3, nNurses1 + index + 3, planF);
                        mysheet.addCell(l4);
                    }
                }

                String Rate = String.valueOf(EmploymentRate);
                Label l5 = new Label(85, nNurses1 + index + 3, Rate);
                mysheet.addCell(l5);
                String type = String.valueOf(Type);
                Label l6 = new Label(86, nNurses1 + index + 3, type);
                mysheet.addCell(l6);
            }
        }
        myexcel.write();
        myexcel.close();
    }

    public void writeShiftToExcel(ArrayList<Nurse> Nurses1, ArrayList<Nurse> Nurses2, String schema1, String schema2, int dep) throws IOException, WriteException {
        File f = new File("C:\\TEST AOR\\OutputShift.xls");
        WritableWorkbook myexcel = Workbook.createWorkbook(f);
        String departement = null;
        if (dep == 0) {
            departement = "A";
        }
        if (dep == 1) {
            departement = "B";
        }
        if (dep == 2) {
            departement = "C";
        }
        if (dep == 3) {
            departement = "D";
        }
        int minass = 28;
        int maxass = 0;
        int minconsass = 28;
        int maxconsass = 0;
        int minconsdayass = 28;
        int maxconsdayass = 0;
        int minconsnightass = 28;
        int maxconsnightass = 0;
        int minconsfreeass = 28;
        int maxconsfreeass = 0;
        int mindayass = 28;
        int maxdayass = 0;
        int minnightass = 28;
        int maxnightass = 0;
        int minfreeass = 28;
        int maxfreeass = 0;
        WritableSheet mysheet = myexcel.createSheet("Case E MonthlyRosterDep" + departement + " Output", dep);

        int nNurses1 = Nurses1.size();
        int nNurses2 = Nurses2.size();

        //rij 1 om de week te zetten
        for (int w = 0; w < 4; w++) {
            Label headerweek = new Label(1 + 7 * w, 0, "Week " + (w + 1));
            mysheet.addCell(headerweek);

        }
        //rij 2 om de dagen van de week te zetten
        Label headernummer = new Label(0, 1, "Personeelsnummer");
        mysheet.addCell(headernummer);
        for (int j = 0; j < 4; j++) {

            Label headerdag1 = new Label(7 * j + 1, 1, "Mon");
            mysheet.addCell(headerdag1);
            Label headerdag2 = new Label(7 * j + 2, 1, "Tue");
            mysheet.addCell(headerdag2);
            Label headerdag3 = new Label(7 * j + 3, 1, "Wed");
            mysheet.addCell(headerdag3);
            Label headerdag4 = new Label(7 * j + 4, 1, "Thu");
            mysheet.addCell(headerdag4);
            Label headerdag5 = new Label(7 * j + 5, 1, "Fri");
            mysheet.addCell(headerdag5);
            Label headerdag6 = new Label(7 * j + 6, 1, "Sat");
            mysheet.addCell(headerdag6);
            Label headerdag7 = new Label(7 * j + 7, 1, "Sun");
            mysheet.addCell(headerdag7);

        }
        Label headerrate = new Label(29, 1, "Employment rate");
        mysheet.addCell(headerrate);
        Label headertype = new Label(30, 1, "Type nurse");
        mysheet.addCell(headertype);

        double countidenticalw = 0;
        for (Nurse nurseA1 : Nurses1) {
            int countass1 = 0;
            int countdayass1 = 0;
            int countnightass1 = 0;
            int countfreeass1 = 0;
            int consass11 = 0; // voor type 1 nurse 1 consecutive shift
            int consass12 = 0;  // voor type 1 nurse 2 consecutive shifts
            int consass13 = 0;
            int consass14 = 0;
            int consass15 = 0; // wss nog opdelen in min en max ook want er wordt niet met count gewerkt
            int consass16 = 0;
            int consass17 = 0;  // voor type 1 nurse 7 consecutive shifts
            int consass18 = 0; // ik ga tot max 8 omdat je werkt met 4-3 dus max 4 op het einde en 4 in het begin
            int consdayass11 = 0;
            int consdayass12 = 0;
            int consdayass13 = 0;
            int consdayass14 = 0;
            int consdayass15 = 0;
            int consdayass16 = 0;
            int consdayass17 = 0;
            int consdayass18 = 0;
            int consnightass11 = 0;
            int consnightass12 = 0;
            int consnightass13 = 0;
            int consnightass14 = 0;
            int consnightass15 = 0;
            int consnightass16 = 0;
            int consnightass17 = 0;
            int consnightass18 = 0;
            int consfreeass11 = 0;
            int consfreeass12 = 0;
            int consfreeass13 = 0;
            int consfreeass14 = 0;
            int consfreeass15 = 0;
            int consfreeass16 = 0;
            int consfreeass17 = 0;
            int consfreeass18 = 0;

            for (int w = 0; w < 4; w++) {
                String Nr = nurseA1.getNr();
                float EmploymentRate = nurseA1.getEmploymentRate();
                int Type = nurseA1.getType();
                int index = Nurses1.indexOf(nurseA1);

                //rij 4 en verder als echte data
                Label l = new Label(0, index + 2, Nr);
                mysheet.addCell(l);
                //planning

                for (int j = 0; j < 7; j++) {
                    char shift = schema1.charAt(nNurses1 * w * 8 + j * 1 + index * 8);
                    char shift1 = 0;
                    char shift2 = 0;
                    char shift3 = 0;
                    char shift4 = 0;
                    char shift5 = 0;
                    char shift6 = 0;
                    char shift7 = 0;

                    // Constraint #consecutive assignments
                    if (j + 1 < 7) {
                        shift1 = schema1.charAt(nNurses1 * w * 8 + (j + 1) * 1 + index * 8);
                    }
                    if (w < 3) {
                        if (j + 1 > 6) {
                            shift1 = schema1.charAt(nNurses1 * (w + 1) * 8 + (j - 6) * 1 + index * 8);
                        }
                    }
                    if (j + 2 < 7) {
                        shift2 = schema1.charAt(nNurses1 * w * 8 + (j + 2) * 1 + index * 8);
                    }
                    if (w < 3) {
                        if (j + 2 > 6) {
                            shift2 = schema1.charAt(nNurses1 * (w + 1) * 8 + (j - 5) * 1 + index * 8);
                        }
                    }
                    if (j + 3 < 7) {
                        shift3 = schema1.charAt(nNurses1 * w * 8 + (j + 3) * 1 + index * 8);
                    }
                    if (w < 3) {
                        if (j + 3 > 6) {
                            shift3 = schema1.charAt(nNurses1 * (w + 1) * 8 + (j - 4) * 1 + index * 8);
                        }
                    }
                    if (j + 4 < 7) {
                        shift4 = schema1.charAt(nNurses1 * w * 8 + (j + 4) * 1 + index * 8);
                    }
                    if (w < 3) {
                        if (j + 4 > 6) {
                            shift4 = schema1.charAt(nNurses1 * (w + 1) * 8 + (j - 3) * 1 + index * 8);
                        }
                    }
                    if (j + 5 < 7) {
                        shift5 = schema1.charAt(nNurses1 * w * 8 + (j + 5) * 1 + index * 8);
                    }
                    if (w < 3) {
                        if (j + 5 > 6) {
                            shift5 = schema1.charAt(nNurses1 * (w + 1) * 8 + (j - 2) * 1 + index * 8);
                        }
                    }
                    if (j + 6 < 7) {
                        shift6 = schema1.charAt(nNurses1 * w * 8 + (j + 6) * 1 + index * 8);
                    }
                    if (w < 3) {
                        if (j + 6 > 6) {
                            shift6 = schema1.charAt(nNurses1 * (w + 1) * 8 + (j - 1) * 1 + index * 8);
                        }
                    }
                    if (w < 3) {
                        if (j + 7 > 6) {
                            shift7 = schema1.charAt(nNurses1 * (w + 1) * 8 + j * 1 + index * 8);
                        }
                    }
                    if (shift == '1' || shift == '2') {
                        consass11 = 1;
                    }
                    if ((shift == '1' || shift == '2') && (shift1 == '1' || shift1 == '2')) {
                        consass12 = 2;
                    }
                    if ((shift == '1' || shift == '2') && (shift1 == '1' || shift1 == '2') && (shift2 == '1' || shift2 == '2')) {
                        consass13 = 3;
                    }
                    if ((shift == '1' || shift == '2') && (shift1 == '1' || shift1 == '2') && (shift2 == '1' || shift2 == '2') && (shift3 == '1' || shift3 == '2')) {
                        consass14 = 4;
                    }
                    if ((shift == '1' || shift == '2') && (shift1 == '1' || shift1 == '2') && (shift2 == '1' || shift2 == '2') && (shift3 == '1' || shift3 == '2')
                            && (shift4 == '1' || shift4 == '2')) {
                        consass15 = 5;
                    }
                    if ((shift == '1' || shift == '2') && (shift1 == '1' || shift1 == '2') && (shift2 == '1' || shift2 == '2') && (shift3 == '1' || shift3 == '2')
                            && (shift4 == '1' || shift4 == '2') && (shift5 == '1' || shift5 == '2')) {
                        consass16 = 6;
                    }
                    if ((shift == '1' || shift == '2') && (shift1 == '1' || shift1 == '2') && (shift2 == '1' || shift2 == '2') && (shift3 == '1' || shift3 == '2')
                            && (shift4 == '1' || shift4 == '2') && (shift5 == '1' || shift5 == '2') && (shift6 == '1' || shift6 == '2')) {
                        consass17 = 7;
                    }
                    if ((shift == '1' || shift == '2') && (shift1 == '1' || shift1 == '2') && (shift2 == '1' || shift2 == '2') && (shift3 == '1' || shift3 == '2')
                            && (shift4 == '1' || shift4 == '2') && (shift5 == '1' || shift5 == '2') && (shift6 == '1' || shift6 == '2') && (shift7 == '1' || shift7 == '2')) {
                        consass18 = 8;
                    }
                    // Constraint #consecutive assignments per shift type
                    // DAY
                    if (shift == '2') {
                        consdayass11 = 1;
                    }
                    if ((shift == '2') && (shift1 == '2')) {
                        consdayass12 = 2;
                    }
                    if ((shift == '2') && (shift1 == '2') && (shift2 == '2')) {
                        consdayass13 = 3;
                    }
                    if ((shift == '2') && (shift1 == '2') && (shift2 == '2') && (shift3 == '2')) {
                        consdayass14 = 4;
                    }
                    if ((shift == '2') && (shift1 == '2') && (shift2 == '2') && (shift3 == '2') && (shift4 == '2')) {
                        consdayass15 = 5;
                    }
                    if ((shift == '2') && (shift1 == '2') && (shift2 == '2') && (shift3 == '2') && (shift4 == '2') && (shift5 == '2')) {
                        consdayass16 = 6;
                    }
                    if ((shift == '2') && (shift1 == '2') && (shift2 == '2') && (shift3 == '2') && (shift4 == '2') && (shift5 == '2')
                            && (shift6 == '2')) {
                        consdayass17 = 7;
                    }
                    if ((shift == '2') && (shift1 == '2') && (shift2 == '2') && (shift3 == '2') && (shift4 == '2') && (shift5 == '2')
                            && (shift6 == '2') && (shift7 == '2')) {
                        consdayass18 = 8;
                    }
                    //NIGHT
                    if (shift == '1') {
                        consnightass11 = 1;
                    }
                    if ((shift == '1') && (shift1 == '1')) {
                        consnightass12 = 2;
                    }
                    if ((shift == '1') && (shift1 == '1') && (shift2 == '1')) {
                        consnightass13 = 3;
                    }
                    if ((shift == '1') && (shift1 == '1') && (shift2 == '1') && (shift3 == '1')) {
                        consnightass14 = 4;
                    }
                    if ((shift == '1') && (shift1 == '1') && (shift2 == '1') && (shift3 == '1') && (shift4 == '1')) {
                        consnightass15 = 5;
                    }
                    if ((shift == '1') && (shift1 == '1') && (shift2 == '1') && (shift3 == '1') && (shift4 == '1') && (shift5 == '1')) {
                        consnightass16 = 6;
                    }
                    if ((shift == '1') && (shift1 == '1') && (shift2 == '1') && (shift3 == '1') && (shift4 == '1') && (shift5 == '1')
                            && (shift6 == '1')) {
                        consnightass17 = 7;
                    }
                    if ((shift == '1') && (shift1 == '1') && (shift2 == '1') && (shift3 == '1') && (shift4 == '1') && (shift5 == '1')
                            && (shift6 == '1') && (shift7 == '1')) {
                        consnightass18 = 8;
                    }
                    //FREE
                    if (shift == '0') {
                        consfreeass11 = 1;
                    }
                    if ((shift == '0') && (shift1 == '0')) {
                        consfreeass12 = 2;
                    }
                    if ((shift == '0') && (shift1 == '0') && (shift2 == '0')) {
                        consfreeass13 = 3;
                    }
                    if ((shift == '0') && (shift1 == '0') && (shift2 == '0') && (shift3 == '0')) {
                        consfreeass14 = 4;
                    }
                    if ((shift == '0') && (shift1 == '0') && (shift2 == '0') && (shift3 == '0') && (shift4 == '0')) {
                        consfreeass15 = 5;
                    }
                    if ((shift == '0') && (shift1 == '0') && (shift2 == '0') && (shift3 == '0') && (shift4 == '0') && (shift5 == '0')) {
                        consfreeass16 = 6;
                    }
                    if ((shift == '0') && (shift1 == '0') && (shift2 == '0') && (shift3 == '0') && (shift4 == '0') && (shift5 == '0')
                            && (shift6 == '0')) {
                        consfreeass17 = 7;
                    }
                    if ((shift == '0') && (shift1 == '0') && (shift2 == '0') && (shift3 == '0') && (shift4 == '0') && (shift5 == '0')
                            && (shift6 == '0') && (shift7 == '0')) {
                        consfreeass18 = 8;
                    }
                    //DAY + Constraint #assignment per shift
                    if (shift == '2') {
                        String plan = "Day";
                        Label l2 = new Label(w * 7 + j * 1 + 1, index + 2, plan);
                        mysheet.addCell(l2);
                        countdayass1 += 1;
                    }
                    //NIGHT + Constraint #assignment per shift
                    if (shift == '1') {
                        String plan = "Night";
                        Label l2 = new Label(w * 7 + j * 1 + 1, index + 2, plan);
                        mysheet.addCell(l2);
                        countnightass1 += 1;
                    }
                    //FREE + Constraint #assignment per shift
                    if (shift == '0') {
                        String plan = "Free";
                        Label l2 = new Label(w * 7 + j * 1 + 1, index + 2, plan);
                        mysheet.addCell(l2);
                        countfreeass1 += 1;
                    }
                    //Constraint #assigments
                    if (shift == '1' || shift == '2') {
                        countass1 += 1;
                    }
                }
                // Constraint identical weekend
                char shiftSAT = schema1.charAt(nNurses1 * w * 8 + 5 * 1 + index * 8);
                char shiftSUN = schema1.charAt(nNurses1 * w * 8 + 6 * 1 + index * 8);
                if (shiftSAT == shiftSUN) {
                    countidenticalw += 1;
                }

                String Rate = String.valueOf(EmploymentRate);
                Label l5 = new Label(29, index + 2, Rate);
                mysheet.addCell(l5);
                String type = String.valueOf(Type);
                Label l6 = new Label(30, index + 2, type);
                mysheet.addCell(l6);
            }
            // Min and max assignments
            if (countass1 < minass) {
                minass = countass1;
            }
            if (countass1 > maxass) {
                maxass = countass1;
            }
            // Min and max assignments per shift type
            if (countdayass1 < mindayass) {
                mindayass = countdayass1;
            }
            if (countdayass1 > maxdayass) {
                maxdayass = countdayass1;
            }
            if (countnightass1 < minnightass) {
                minnightass = countnightass1;
            }
            if (countnightass1 > maxnightass) {
                maxnightass = countnightass1;
            }
            if (countfreeass1 < minfreeass) {
                minfreeass = countfreeass1;
            }
            if (countfreeass1 > maxfreeass) {
                maxfreeass = countfreeass1;
            }
            // Min and max consecutive assignments
            if (consass11 > 0) {
                if (consass11 < minconsass) {
                    minconsass = consass11;
                }
                if (consass11 > maxconsass) {
                    maxconsass = consass11;
                }
            }
            if (consass12 > 0) {
                if (consass12 < minconsass) {
                    minconsass = consass12;
                }
                if (consass12 > maxconsass) {
                    maxconsass = consass12;
                }
            }
            if (consass13 > 0) {
                if (consass13 < minconsass) {
                    minconsass = consass13;
                }
                if (consass13 > maxconsass) {
                    maxconsass = consass13;
                }
            }
            if (consass14 > 0) {
                if (consass14 < minconsass) {
                    minconsass = consass14;
                }
                if (consass14 > maxconsass) {
                    maxconsass = consass14;
                }
            }
            if (consass15 > 0) {
                if (consass15 < minconsass) {
                    minconsass = consass15;
                }
                if (consass15 > maxconsass) {
                    maxconsass = consass15;
                }
            }
            if (consass16 > 0) {
                if (consass16 < minconsass) {
                    minconsass = consass16;
                }
                if (consass16 > maxconsass) {
                    maxconsass = consass16;
                }
            }
            if (consass17 > 0) {
                if (consass17 < minconsass) {
                    minconsass = consass17;
                }
                if (consass17 > maxconsass) {
                    maxconsass = consass17;
                }
            }
            if (consass18 > 0) {
                if (consass18 < minconsass) {
                    minconsass = consass18;
                }
                if (consass18 > maxconsass) {
                    maxconsass = consass18;
                }
            }
            // Min and max consecutive assignments per shift type
            // day
            if (consdayass11 > 0) {
                if (consdayass11 < minconsdayass) {
                    minconsdayass = consdayass11;
                }
                if (consdayass11 > maxconsdayass) {
                    maxconsdayass = consdayass11;
                }
            }
            if (consdayass12 > 0) {
                if (consdayass12 < minconsdayass) {
                    minconsdayass = consdayass12;
                }
                if (consdayass12 > maxconsdayass) {
                    maxconsdayass = consdayass12;
                }
            }
            if (consdayass13 > 0) {
                if (consdayass13 < minconsdayass) {
                    minconsdayass = consdayass13;
                }
                if (consdayass13 > maxconsdayass) {
                    maxconsdayass = consdayass13;
                }
            }
            if (consdayass14 > 0) {
                if (consdayass14 < minconsdayass) {
                    minconsdayass = consdayass14;
                }
                if (consdayass14 > maxconsdayass) {
                    maxconsdayass = consdayass14;
                }
            }
            if (consdayass15 > 0) {
                if (consdayass15 < minconsdayass) {
                    minconsdayass = consdayass15;
                }
                if (consdayass15 > maxconsdayass) {
                    maxconsdayass = consdayass15;
                }
            }
            if (consdayass16 > 0) {
                if (consdayass16 < minconsdayass) {
                    minconsdayass = consdayass16;
                }
                if (consdayass16 > maxconsdayass) {
                    maxconsdayass = consdayass16;
                }
            }
            if (consdayass17 > 0) {
                if (consdayass17 < minconsdayass) {
                    minconsdayass = consdayass17;
                }
                if (consdayass17 > maxconsdayass) {
                    maxconsdayass = consdayass17;
                }
            }
            if (consdayass18 > 0) {
                if (consdayass18 < minconsdayass) {
                    minconsdayass = consdayass18;
                }
                if (consdayass18 > maxconsdayass) {
                    maxconsdayass = consdayass18;
                }
            }
            // night
            if (consnightass11 > 0) {
                if (consnightass11 < minconsnightass) {
                    minconsnightass = consnightass11;
                }
                if (consnightass11 > maxconsnightass) {
                    maxconsnightass = consnightass11;
                }
            }
            if (consnightass12 > 0) {
                if (consnightass12 < minconsnightass) {
                    minconsnightass = consnightass12;
                }
                if (consnightass12 > maxconsnightass) {
                    maxconsnightass = consnightass12;
                }
            }
            if (consnightass13 > 0) {
                if (consnightass13 < minconsnightass) {
                    minconsnightass = consnightass13;
                }
                if (consnightass13 > maxconsnightass) {
                    maxconsnightass = consnightass13;
                }
            }
            if (consnightass14 > 0) {
                if (consnightass14 < minconsnightass) {
                    minconsnightass = consnightass14;
                }
                if (consnightass14 > maxconsnightass) {
                    maxconsnightass = consnightass14;
                }
            }
            if (consnightass15 > 0) {
                if (consnightass15 < minconsnightass) {
                    minconsnightass = consnightass15;
                }
                if (consnightass15 > maxconsnightass) {
                    maxconsnightass = consnightass15;
                }
            }
            if (consnightass16 > 0) {
                if (consnightass16 < minconsnightass) {
                    minconsnightass = consnightass16;
                }
                if (consnightass16 > maxconsnightass) {
                    maxconsnightass = consnightass16;
                }
            }
            if (consnightass17 > 0) {
                if (consnightass17 < minconsnightass) {
                    minconsnightass = consnightass17;
                }
                if (consnightass17 > maxconsnightass) {
                    maxconsnightass = consnightass17;
                }
            }
            if (consnightass18 > 0) {
                if (consnightass18 < minconsnightass) {
                    minconsnightass = consnightass18;
                }
                if (consnightass18 > maxconsnightass) {
                    maxconsnightass = consnightass18;
                }
            }
            // free
            if (consfreeass11 > 0) {
                if (consfreeass11 < minconsfreeass) {
                    minconsfreeass = consfreeass11;
                }
                if (consfreeass11 > maxconsfreeass) {
                    maxconsfreeass = consfreeass11;
                }
            }
            if (consfreeass12 > 0) {
                if (consfreeass12 < minconsfreeass) {
                    minconsfreeass = consfreeass12;
                }
                if (consfreeass12 > maxconsfreeass) {
                    maxconsfreeass = consfreeass12;
                }
            }
            if (consfreeass13 > 0) {
                if (consfreeass13 < minconsfreeass) {
                    minconsfreeass = consfreeass13;
                }
                if (consfreeass13 > maxconsfreeass) {
                    maxconsfreeass = consfreeass13;
                }
            }
            if (consfreeass14 > 0) {
                if (consfreeass14 < minconsfreeass) {
                    minconsfreeass = consfreeass14;
                }
                if (consfreeass14 > maxconsfreeass) {
                    maxconsfreeass = consfreeass14;
                }
            }
            if (consfreeass15 > 0) {
                if (consfreeass15 < minconsfreeass) {
                    minconsfreeass = consfreeass15;
                }
                if (consfreeass15 > maxconsfreeass) {
                    maxconsfreeass = consfreeass15;
                }
            }
            if (consfreeass16 > 0) {
                if (consfreeass16 < minconsfreeass) {
                    minconsfreeass = consfreeass16;
                }
                if (consfreeass16 > maxconsfreeass) {
                    maxconsfreeass = consfreeass16;
                }
            }
            if (consfreeass17 > 0) {
                if (consfreeass17 < minconsfreeass) {
                    minconsfreeass = consfreeass17;
                }
                if (consfreeass17 > maxconsfreeass) {
                    maxconsfreeass = consfreeass17;
                }
            }
            if (consfreeass18 > 0) {
                if (consfreeass18 < minconsfreeass) {
                    minconsfreeass = consfreeass18;
                }
                if (consfreeass18 > maxconsfreeass) {
                    maxconsfreeass = consfreeass18;
                }
            }
        }

        for (Nurse nurseA2 : Nurses2) {
            int countass2 = 0;
            int countdayass2 = 0;
            int countnightass2 = 0;
            int countfreeass2 = 0;
            int consass21 = 0;  // voor type 2 nurse 1 consecutive shift
            int consass22 = 0;  // voor type 2 nurse 2 consecutive shifts
            int consass23 = 0;
            int consass24 = 0;
            int consass25 = 0;
            int consass26 = 0;
            int consass27 = 0;  // voor type 2 nurse 7 consecutive shifts
            int consass28 = 0; // ik ga tot max 8 omdat je werkt met 4-3 dus max 4 op het einde en 4 in het begin
            int consdayass21 = 0;
            int consdayass22 = 0;
            int consdayass23 = 0;
            int consdayass24 = 0;
            int consdayass25 = 0;
            int consdayass26 = 0;
            int consdayass27 = 0;
            int consdayass28 = 0;
            int consnightass21 = 0;
            int consnightass22 = 0;
            int consnightass23 = 0;
            int consnightass24 = 0;
            int consnightass25 = 0;
            int consnightass26 = 0;
            int consnightass27 = 0;
            int consnightass28 = 0;
            int consfreeass21 = 0;
            int consfreeass22 = 0;
            int consfreeass23 = 0;
            int consfreeass24 = 0;
            int consfreeass25 = 0;
            int consfreeass26 = 0;
            int consfreeass27 = 0;
            int consfreeass28 = 0;

            for (int w = 0; w < 4; w++) {
                String Nr = nurseA2.getNr();
                float EmploymentRate = nurseA2.getEmploymentRate();
                int Type = nurseA2.getType();
                int index = Nurses2.indexOf(nurseA2);

                //rij 3 en verder als echte data
                Label l = new Label(0, nNurses1 + index + 2, Nr);
                mysheet.addCell(l);
                //planning

                for (int j = 0; j < 7; j++) {
                    char shift = schema2.charAt(nNurses2 * w * 8 + j * 1 + index * 8);
                    char shift1 = 0;
                    char shift2 = 0;
                    char shift3 = 0;
                    char shift4 = 0;
                    char shift5 = 0;
                    char shift6 = 0;
                    char shift7 = 0;

                    // Constraint #consecutive assignments
                    if (j + 1 < 7) {
                        shift1 = schema2.charAt(nNurses1 * w * 8 + (j + 1) * 1 + index * 8);
                    }
                    if (w < 3) {
                        if (j + 1 > 6) {
                            shift1 = schema2.charAt(nNurses1 * (w + 1) * 8 + (j - 6) * 1 + index * 8);
                        }
                    }
                    if (j + 2 < 7) {
                        shift2 = schema2.charAt(nNurses1 * w * 8 + (j + 2) * 1 + index * 8);
                    }
                    if (w < 3) {
                        if (j + 2 > 6) {
                            shift2 = schema2.charAt(nNurses1 * (w + 1) * 8 + (j - 5) * 1 + index * 8);
                        }
                    }
                    if (j + 3 < 7) {
                        shift3 = schema2.charAt(nNurses1 * w * 8 + (j + 3) * 1 + index * 8);
                    }
                    if (w < 3) {
                        if (j + 3 > 6) {
                            shift3 = schema2.charAt(nNurses1 * (w + 1) * 8 + (j - 4) * 1 + index * 8);
                        }
                    }
                    if (j + 4 < 7) {
                        shift4 = schema2.charAt(nNurses1 * w * 8 + (j + 4) * 1 + index * 8);
                    }
                    if (w < 3) {
                        if (j + 4 > 6) {
                            shift4 = schema2.charAt(nNurses1 * (w + 1) * 8 + (j - 3) * 1 + index * 8);
                        }
                    }
                    if (j + 5 < 7) {
                        shift5 = schema2.charAt(nNurses1 * w * 8 + (j + 5) * 1 + index * 8);
                    }
                    if (w < 3) {
                        if (j + 5 > 6) {
                            shift5 = schema2.charAt(nNurses1 * (w + 1) * 8 + (j - 2) * 1 + index * 8);
                        }
                    }
                    if (j + 6 < 7) {
                        shift6 = schema2.charAt(nNurses1 * w * 8 + (j + 6) * 1 + index * 8);
                    }
                    if (w < 3) {
                        if (j + 6 > 6) {
                            shift6 = schema2.charAt(nNurses1 * (w + 1) * 8 + (j - 1) * 1 + index * 8);
                        }
                    }
                    if (w < 3) {
                        if (j + 7 > 6) {
                            shift7 = schema2.charAt(nNurses1 * (w + 1) * 8 + j * 1 + index * 8);
                        }
                    }
                    if (shift == '1' || shift == '2') {
                        consass21 = 1;
                    }
                    if ((shift == '1' || shift == '2') && (shift1 == '1' || shift1 == '2')) {
                        consass22 = 2;
                    }
                    if ((shift == '1' || shift == '2') && (shift1 == '1' || shift1 == '2') && (shift2 == '1' || shift2 == '2')) {
                        consass23 = 3;
                    }
                    if ((shift == '1' || shift == '2') && (shift1 == '1' || shift1 == '2') && (shift2 == '1' || shift2 == '2') && (shift3 == '1' || shift3 == '2')) {
                        consass24 = 4;
                    }
                    if ((shift == '1' || shift == '2') && (shift1 == '1' || shift1 == '2') && (shift2 == '1' || shift2 == '2') && (shift3 == '1' || shift3 == '2')
                            && (shift4 == '1' || shift4 == '2')) {
                        consass25 = 5;
                    }
                    if ((shift == '1' || shift == '2') && (shift1 == '1' || shift1 == '2') && (shift2 == '1' || shift2 == '2') && (shift3 == '1' || shift3 == '2')
                            && (shift4 == '1' || shift4 == '2') && (shift5 == '1' || shift5 == '2')) {
                        consass26 = 6;
                    }
                    if ((shift == '1' || shift == '2') && (shift1 == '1' || shift1 == '2') && (shift2 == '1' || shift2 == '2') && (shift3 == '1' || shift3 == '2')
                            && (shift4 == '1' || shift4 == '2') && (shift5 == '1' || shift5 == '2') && (shift6 == '1' || shift6 == '2')) {
                        consass27 = 7;
                    }
                    if ((shift == '1' || shift == '2') && (shift1 == '1' || shift1 == '2') && (shift2 == '1' || shift2 == '2') && (shift3 == '1' || shift3 == '2')
                            && (shift4 == '1' || shift4 == '2') && (shift5 == '1' || shift5 == '2') && (shift6 == '1' || shift6 == '2') && (shift7 == '1' || shift7 == '2')) {
                        consass28 = 8;
                    }
                    // Constraint #consecutive assignments per shift type
                    // DAY
                    if (shift == '2') {
                        consdayass21 = 1;
                    }
                    if ((shift == '2') && (shift1 == '2')) {
                        consdayass22 = 2;
                    }
                    if ((shift == '2') && (shift1 == '2') && (shift2 == '2')) {
                        consdayass23 = 3;
                    }
                    if ((shift == '2') && (shift1 == '2') && (shift2 == '2') && (shift3 == '2')) {
                        consdayass24 = 4;
                    }
                    if ((shift == '2') && (shift1 == '2') && (shift2 == '2') && (shift3 == '2') && (shift4 == '2')) {
                        consdayass25 = 5;
                    }
                    if ((shift == '2') && (shift1 == '2') && (shift2 == '2') && (shift3 == '2') && (shift4 == '2') && (shift5 == '2')) {
                        consdayass26 = 6;
                    }
                    if ((shift == '2') && (shift1 == '2') && (shift2 == '2') && (shift3 == '2') && (shift4 == '2') && (shift5 == '2')
                            && (shift6 == '2')) {
                        consdayass27 = 7;
                    }
                    if ((shift == '2') && (shift1 == '2') && (shift2 == '2') && (shift3 == '2') && (shift4 == '2') && (shift5 == '2')
                            && (shift6 == '2') && (shift7 == '2')) {
                        consdayass28 = 8;
                    }
                    //NIGHT
                    if (shift == '1') {
                        consnightass21 = 1;
                    }
                    if ((shift == '1') && (shift1 == '1')) {
                        consnightass22 = 2;
                    }
                    if ((shift == '1') && (shift1 == '1') && (shift2 == '1')) {
                        consnightass23 = 3;
                    }
                    if ((shift == '1') && (shift1 == '1') && (shift2 == '1') && (shift3 == '1')) {
                        consnightass24 = 4;
                    }
                    if ((shift == '1') && (shift1 == '1') && (shift2 == '1') && (shift3 == '1') && (shift4 == '1')) {
                        consnightass25 = 5;
                    }
                    if ((shift == '1') && (shift1 == '1') && (shift2 == '1') && (shift3 == '1') && (shift4 == '1') && (shift5 == '1')) {
                        consnightass26 = 6;
                    }
                    if ((shift == '1') && (shift1 == '1') && (shift2 == '1') && (shift3 == '1') && (shift4 == '1') && (shift5 == '1')
                            && (shift6 == '1')) {
                        consnightass27 = 7;
                    }
                    if ((shift == '1') && (shift1 == '1') && (shift2 == '1') && (shift3 == '1') && (shift4 == '1') && (shift5 == '1')
                            && (shift6 == '1') && (shift7 == '1')) {
                        consnightass28 = 8;
                    }
                    //FREE
                    if (shift == '0') {
                        consfreeass21 = 1;
                    }
                    if ((shift == '0') && (shift1 == '0')) {
                        consfreeass22 = 2;
                    }
                    if ((shift == '0') && (shift1 == '0') && (shift2 == '0')) {
                        consfreeass23 = 3;
                    }
                    if ((shift == '0') && (shift1 == '0') && (shift2 == '0') && (shift3 == '0')) {
                        consfreeass24 = 4;
                    }
                    if ((shift == '0') && (shift1 == '0') && (shift2 == '0') && (shift3 == '0') && (shift4 == '0')) {
                        consfreeass25 = 5;
                    }
                    if ((shift == '0') && (shift1 == '0') && (shift2 == '0') && (shift3 == '0') && (shift4 == '0') && (shift5 == '0')) {
                        consfreeass26 = 6;
                    }
                    if ((shift == '0') && (shift1 == '0') && (shift2 == '0') && (shift3 == '0') && (shift4 == '0') && (shift5 == '0')
                            && (shift6 == '0')) {
                        consfreeass27 = 7;
                    }
                    if ((shift == '0') && (shift1 == '0') && (shift2 == '0') && (shift3 == '0') && (shift4 == '0') && (shift5 == '0')
                            && (shift6 == '0') && (shift7 == '0')) {
                        consfreeass28 = 8;
                    }
                    //DAY + Constraint #assignment per shift
                    if (shift == '2') {
                        String plan = "Day";
                        Label l2 = new Label(w * 7 + j * 1 + 1, nNurses1 + index + 2, plan);
                        mysheet.addCell(l2);
                        countdayass2 += 1;
                    }
                    //NIGHT + Constraint #assignment per shift
                    if (shift == '1') {
                        String plan = "Night";
                        Label l2 = new Label(w * 7 + j * 1 + 1, nNurses1 + index + 2, plan);
                        mysheet.addCell(l2);
                        countnightass2 += 1;
                    }
                    //FREE + Constraint #assignment per shift
                    if (shift == '0') {
                        String plan = "Free";
                        Label l2 = new Label(w * 7 + j * 1 + 1, nNurses1 + index + 2, plan);
                        mysheet.addCell(l2);
                        countfreeass2 += 1;
                    }
                    //Constraint #assigments
                    if (shift == '1' || shift == '2') {
                        countass2 += 1;
                    }
                }
                // Constraint identical weekend
                char shiftSAT = schema2.charAt(nNurses2 * w * 8 + 5 * 1 + index * 8);
                char shiftSUN = schema2.charAt(nNurses2 * w * 8 + 6 * 1 + index * 8);
                if (shiftSAT == shiftSUN) {
                    countidenticalw += 1;
                }

                String Rate = String.valueOf(EmploymentRate);
                Label l5 = new Label(29, nNurses1 + index + 2, Rate);
                mysheet.addCell(l5);
                String type = String.valueOf(Type);
                Label l6 = new Label(30, nNurses1 + index + 2, type);
                mysheet.addCell(l6);
            }
            // Min and max assignments
            if (countass2 < minass) {
                minass = countass2;
            }
            if (countass2 > maxass) {
                maxass = countass2;
            }
            // Min and max assignments per shift type
            if (countdayass2 < mindayass) {
                mindayass = countdayass2;
            }
            if (countdayass2 > maxdayass) {
                maxdayass = countdayass2;
            }
            if (countnightass2 < minnightass) {
                minnightass = countnightass2;
            }
            if (countnightass2 > maxnightass) {
                maxnightass = countnightass2;
            }
            if (countfreeass2 < minfreeass) {
                minfreeass = countfreeass2;
            }
            if (countfreeass2 > maxfreeass) {
                maxfreeass = countfreeass2;
            }
            // Min and max consecutive assignments
            if (consass21 > 0) {
                if (consass21 < minconsass) {
                    minconsass = consass21;
                }
                if (consass21 > maxconsass) {
                    maxconsass = consass21;
                }
            }
            if (consass22 > 0) {
                if (consass22 < minconsass) {
                    minconsass = consass22;
                }
                if (consass22 > maxconsass) {
                    maxconsass = consass22;
                }
            }
            if (consass23 > 0) {
                if (consass23 < minconsass) {
                    minconsass = consass23;
                }
                if (consass23 > maxconsass) {
                    maxconsass = consass23;
                }
            }
            if (consass24 > 0) {
                if (consass24 < minconsass) {
                    minconsass = consass24;
                }
                if (consass24 > maxconsass) {
                    maxconsass = consass24;
                }
            }
            if (consass25 > 0) {
                if (consass25 < minconsass) {
                    minconsass = consass25;
                }
                if (consass25 > maxconsass) {
                    maxconsass = consass25;
                }
            }
            if (consass26 > 0) {
                if (consass26 < minconsass) {
                    minconsass = consass26;
                }
                if (consass26 > maxconsass) {
                    maxconsass = consass26;
                }
            }
            if (consass27 > 0) {
                if (consass27 < minconsass) {
                    minconsass = consass27;
                }
                if (consass27 > maxconsass) {
                    maxconsass = consass27;
                }
            }
            if (consass28 > 0) {
                if (consass28 < minconsass) {
                    minconsass = consass28;
                }
                if (consass28 > maxconsass) {
                    maxconsass = consass28;
                }
            }
            // Min and max consecutive assignments per shift type
            // day
            if (consdayass21 > 0) {
                if (consdayass21 < minconsdayass) {
                    minconsdayass = consdayass21;
                }
                if (consdayass21 > maxconsdayass) {
                    maxconsdayass = consdayass21;
                }
            }
            if (consdayass22 > 0) {
                if (consdayass22 < minconsdayass) {
                    minconsdayass = consdayass22;
                }
                if (consdayass22 > maxconsdayass) {
                    maxconsdayass = consdayass22;
                }
            }
            if (consdayass23 > 0) {
                if (consdayass23 < minconsdayass) {
                    minconsdayass = consdayass23;
                }
                if (consdayass23 > maxconsdayass) {
                    maxconsdayass = consdayass23;
                }
            }
            if (consdayass24 > 0) {
                if (consdayass24 < minconsdayass) {
                    minconsdayass = consdayass24;
                }
                if (consdayass24 > maxconsdayass) {
                    maxconsdayass = consdayass24;
                }
            }
            if (consdayass25 > 0) {
                if (consdayass25 < minconsdayass) {
                    minconsdayass = consdayass25;
                }
                if (consdayass25 > maxconsdayass) {
                    maxconsdayass = consdayass25;
                }
            }
            if (consdayass26 > 0) {
                if (consdayass26 < minconsdayass) {
                    minconsdayass = consdayass26;
                }
                if (consdayass26 > maxconsdayass) {
                    maxconsdayass = consdayass26;
                }
            }
            if (consdayass27 > 0) {
                if (consdayass27 < minconsdayass) {
                    minconsdayass = consdayass27;
                }
                if (consdayass27 > maxconsdayass) {
                    maxconsdayass = consdayass27;
                }
            }
            if (consdayass28 > 0) {
                if (consdayass28 < minconsdayass) {
                    minconsdayass = consdayass28;
                }
                if (consdayass28 > maxconsdayass) {
                    maxconsdayass = consdayass28;
                }
            }
            // night
            if (consnightass21 > 0) {
                if (consnightass21 < minconsnightass) {
                    minconsnightass = consnightass21;
                }
                if (consnightass21 > maxconsnightass) {
                    maxconsnightass = consnightass21;
                }
            }
            if (consnightass22 > 0) {
                if (consnightass22 < minconsnightass) {
                    minconsnightass = consnightass22;
                }
                if (consnightass22 > maxconsnightass) {
                    maxconsnightass = consnightass22;
                }
            }
            if (consnightass23 > 0) {
                if (consnightass23 < minconsnightass) {
                    minconsnightass = consnightass23;
                }
                if (consnightass23 > maxconsnightass) {
                    maxconsnightass = consnightass23;
                }
            }
            if (consnightass24 > 0) {
                if (consnightass24 < minconsnightass) {
                    minconsnightass = consnightass24;
                }
                if (consnightass24 > maxconsnightass) {
                    maxconsnightass = consnightass24;
                }
            }
            if (consnightass25 > 0) {
                if (consnightass25 < minconsnightass) {
                    minconsnightass = consnightass25;
                }
                if (consnightass25 > maxconsnightass) {
                    maxconsnightass = consnightass25;
                }
            }
            if (consnightass26 > 0) {
                if (consnightass26 < minconsnightass) {
                    minconsnightass = consnightass26;
                }
                if (consnightass26 > maxconsnightass) {
                    maxconsnightass = consnightass26;
                }
            }
            if (consnightass27 > 0) {
                if (consnightass27 < minconsnightass) {
                    minconsnightass = consnightass27;
                }
                if (consnightass27 > maxconsnightass) {
                    maxconsnightass = consnightass27;
                }
            }
            if (consnightass28 > 0) {
                if (consnightass28 < minconsnightass) {
                    minconsnightass = consnightass28;
                }
                if (consnightass28 > maxconsnightass) {
                    maxconsnightass = consnightass28;
                }
            }
            // free
            if (consfreeass21 > 0) {
                if (consfreeass21 < minconsfreeass) {
                    minconsfreeass = consfreeass21;
                }
                if (consfreeass21 > maxconsfreeass) {
                    maxconsfreeass = consfreeass21;
                }
            }
            if (consfreeass22 > 0) {
                if (consfreeass22 < minconsfreeass) {
                    minconsfreeass = consfreeass22;
                }
                if (consfreeass22 > maxconsfreeass) {
                    maxconsfreeass = consfreeass22;
                }
            }
            if (consfreeass23 > 0) {
                if (consfreeass23 < minconsfreeass) {
                    minconsfreeass = consfreeass23;
                }
                if (consfreeass23 > maxconsfreeass) {
                    maxconsfreeass = consfreeass23;
                }
            }
            if (consfreeass24 > 0) {
                if (consfreeass24 < minconsfreeass) {
                    minconsfreeass = consfreeass24;
                }
                if (consfreeass24 > maxconsfreeass) {
                    maxconsfreeass = consfreeass24;
                }
            }
            if (consfreeass25 > 0) {
                if (consfreeass25 < minconsfreeass) {
                    minconsfreeass = consfreeass25;
                }
                if (consfreeass25 > maxconsfreeass) {
                    maxconsfreeass = consfreeass25;
                }
            }
            if (consfreeass26 > 0) {
                if (consfreeass26 < minconsfreeass) {
                    minconsfreeass = consfreeass26;
                }
                if (consfreeass26 > maxconsfreeass) {
                    maxconsfreeass = consfreeass26;
                }
            }
            if (consfreeass27 > 0) {
                if (consfreeass27 < minconsfreeass) {
                    minconsfreeass = consfreeass27;
                }
                if (consfreeass27 > maxconsfreeass) {
                    maxconsfreeass = consfreeass27;
                }
            }
            if (consfreeass28 > 0) {
                if (consfreeass28 < minconsfreeass) {
                    minconsfreeass = consfreeass28;
                }
                if (consfreeass28 > maxconsfreeass) {
                    maxconsfreeass = consfreeass28;
                }
            }
        }

        WritableSheet sheetconstraint = myexcel.createSheet("Case E ConstraintsDep" + departement + " Output", dep + 4);

        Label headernaam = new Label(0, 0, "Department " + departement);
        sheetconstraint.addCell(headernaam);
        Label assignments = new Label(0, 1, "NUMBER OF ASSIGNMENTS");
        sheetconstraint.addCell(assignments);
        Label minassignments = new Label(0, 2, "Minimum");
        sheetconstraint.addCell(minassignments);
        Label maxassignments = new Label(1, 2, "Maximum");
        sheetconstraint.addCell(maxassignments);
        Label min1 = new Label(0, 3, "" + minass);
        sheetconstraint.addCell(min1);
        Label max1 = new Label(1, 3, "" + maxass);
        sheetconstraint.addCell(max1);

        Label consassignments = new Label(0, 5, "NUMBER OF CONSECUTIVE ASSIGNMENTS");
        // nu geeft dit het (min en max ) aantal weer van dagen werken na elkaar dus min =2  betekend dat als je werkt het minstens 2 dagen na elkaar is
        // max = 6 betekend dat je maximaal 6 dagen achter elkaar werkt.
        sheetconstraint.addCell(consassignments);
        Label minconsassignments = new Label(0, 6, "Minimum");
        sheetconstraint.addCell(minconsassignments);
        Label maxconsassignments = new Label(1, 6, "Maximum");
        sheetconstraint.addCell(maxconsassignments);
        Label min2 = new Label(0, 7, "" + minconsass);
        sheetconstraint.addCell(min2);
        Label max2 = new Label(1, 7, "" + maxconsass);
        sheetconstraint.addCell(max2);

        Label consshiftassignments = new Label(0, 9, "NUMBER OF CONSECUTIVE ASSIGNMENTS PER SHIFT TYPE");
        sheetconstraint.addCell(consshiftassignments);
        Label minconsshiftassignments = new Label(0, 10, "Minimum");
        sheetconstraint.addCell(minconsshiftassignments);
        Label maxconsshiftassignments = new Label(1, 10, "Maximum");
        sheetconstraint.addCell(maxconsshiftassignments);
        Label min3 = new Label(0, 11, "" + minconsdayass);
        sheetconstraint.addCell(min3);
        Label max3 = new Label(1, 11, "" + maxconsdayass);
        sheetconstraint.addCell(max3);
        Label cda = new Label(2, 11, "Day shift");
        sheetconstraint.addCell(cda);
        Label min4 = new Label(0, 12, "" + minconsnightass);
        sheetconstraint.addCell(min4);
        Label max4 = new Label(1, 12, "" + maxconsnightass);
        sheetconstraint.addCell(max4);
        Label cna = new Label(2, 12, "Night shift");
        sheetconstraint.addCell(cna);
        Label min5 = new Label(0, 13, "" + minconsfreeass);
        sheetconstraint.addCell(min5);
        Label max5 = new Label(1, 13, "" + maxconsfreeass);
        sheetconstraint.addCell(max5);
        Label cfa = new Label(2, 13, "Free shift");
        sheetconstraint.addCell(cfa);

        Label shiftassignments = new Label(0, 15, "NUMBER OF ASSIGNMENTS PER SHIFT TYPE");
        sheetconstraint.addCell(shiftassignments);
        Label minshiftassignments = new Label(0, 16, "Minimum");
        sheetconstraint.addCell(minshiftassignments);
        Label maxshiftassignments = new Label(1, 16, "Maximum");
        sheetconstraint.addCell(maxshiftassignments);
        Label min6 = new Label(0, 17, "" + mindayass);
        sheetconstraint.addCell(min6);
        Label max6 = new Label(1, 17, "" + maxdayass);
        sheetconstraint.addCell(max6);
        Label da = new Label(2, 17, "Day shift");
        sheetconstraint.addCell(da);
        Label min7 = new Label(0, 18, "" + minnightass);
        sheetconstraint.addCell(min7);
        Label max7 = new Label(1, 18, "" + maxnightass);
        sheetconstraint.addCell(max7);
        Label na = new Label(2, 18, "Night shift");
        sheetconstraint.addCell(na);
        Label min8 = new Label(0, 19, "" + minfreeass);
        sheetconstraint.addCell(min8);
        Label max8 = new Label(1, 19, "" + maxfreeass);
        sheetconstraint.addCell(max8);
        Label fa = new Label(2, 19, "Free shift");
        sheetconstraint.addCell(fa);

        Label identicalweekend = new Label(0, 21, "IDENTICAL WEEKEND CONSTRAINT");
        sheetconstraint.addCell(identicalweekend);
        Label identical = new Label(0, 22, (countidenticalw / ((nNurses1 + nNurses2) * 4)) * 100 + "%");
        sheetconstraint.addCell(identical);

        myexcel.write();
        myexcel.close();
    }
}
