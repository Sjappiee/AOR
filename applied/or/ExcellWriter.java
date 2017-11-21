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

    public void writeScheduleToExcel(ArrayList<Nurse> lijst) throws IOException, WriteException {
        File f = new File("C:\\TEST AOR\\output.xls");
        WritableWorkbook myexcel = Workbook.createWorkbook(f);
        WritableSheet mysheet = myexcel.createSheet("mySheet", 0);

        for (Nurse nurseA : lijst) {
            String Nr = nurseA.getNr();
            // data om planning te testen, want voorlopig is de planning leeg en een lege planning kan ik niet afdrukken
            // ik heb ook voorlopig enkel de eerste week die ik kan wegschrijven, want waar is de data van de andere 3 weken??
            // getBinaryDayPlanning() geeft enkel data van 1 week
            int[][] DagPlan = new int[2][7];
            DagPlan[0][0] = 1;  // werken shift D op MA
            DagPlan[0][1] = 1;  // werken shift D op DI
            DagPlan[0][2] = 1;  // werken shift D op WOE
            DagPlan[0][3] = 1;  // werken shift D op DO
            DagPlan[0][4] = 1;  // werken shift D op VR
            DagPlan[0][5] = 0;  // werken shift D op ZA
            DagPlan[0][6] = 0;  // werken shift D op ZO
            DagPlan[1][0] = 0;  // werken shift N op MA
            DagPlan[1][1] = 0;  // werken shift N op DI
            DagPlan[1][2] = 0;  // werken shift N op WO
            DagPlan[1][3] = 0;  // werken shift N op DO
            DagPlan[1][4] = 0;  // werken shift N op VR
            DagPlan[1][5] = 0;  // werken shift N op ZA
            DagPlan[1][6] = 0;  // werken shift N op ZO

            nurseA.setBinaryDayPlanning(DagPlan);
            int[][] planning = nurseA.getBinaryDayPlanning();
            float EmploymentRate = nurseA.getEmploymentRate();
            int Type = nurseA.getType();
            int index = lijst.indexOf(nurseA);

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

            //rij 4 en verder als echte data
            Label l = new Label(0, index + 3, Nr);
            mysheet.addCell(l);
            //planning
            for (int j = 0; j < 7; j++) {
                 //DAY
                if (planning[0][j] == 0) {
                    String plan = "0";
                    Label l2 = new Label(3 * j + 1, index + 3, plan);
                    mysheet.addCell(l2);
                }
                if (planning[0][j] == 1) {
                    String plan = "1";
                    Label l2 = new Label(3 * j + 1, index + 3, plan);
                    mysheet.addCell(l2);
                }
                //NIGHT
                if (planning[1][j] == 0) {
                    String plan = "0";
                    Label l3 = new Label(3 * j + 2, index + 3, plan);
                    mysheet.addCell(l3);
                }
                if (planning[1][j] == 1) {
                    String plan = "1";
                    Label l3 = new Label(3 * j + 2, index + 3, plan);
                    mysheet.addCell(l3);
                }
                //FREE
                if (planning[0][j] == 0 && planning[1][j] == 0) {
                    String plan = "1";
                    Label l4 = new Label(3 * j + 3, index + 3, plan);
                    mysheet.addCell(l4);
                }
                if ((planning[0][j] == 0 && planning[1][j] == 1) || (planning[0][j] == 1 && planning[1][j] == 0)) {
                    String plan = "0";
                    Label l4 = new Label(3 * j + 3, index + 3, plan);
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
        myexcel.write();
        myexcel.close();
    }
}
