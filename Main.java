import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        System.out.println("Please input the name of the swing csv file to analyze");

        //Get the filename from the user and open the file
        Scanner s = new Scanner(System.in);
        String filename = s.nextLine();
        File swingCSV = new File(filename);

        //Create a swing data structure with the csv
        Swing swingOne = new Swing(swingCSV);

        System.out.println("Examples of opertaions:");

        int result = Swing.backSearchContinuityWithinRange(swingOne.getAx(),0,swingOne.numRows(),-1.0,1.0,3);
        System.out.println(result);

        result = Swing.searchContinuityAboveValue(swingOne.getAy(),0,1000,4.0,20);
        System.out.println(result);

        result =  Swing.searchContinuityAboveValueTwoSignals(swingOne.getWx(),swingOne.getWy(),10,1000,-3,5.0,2);
        System.out.println(result);

        ArrayList<Continuity> ranges = Swing.searchMultiContinuityWithinRange(swingOne.getAy(), 0, swingOne.numRows(), 8.0, 9.0, 2);
        for(Continuity c:ranges){
            System.out.println(c.start + "-" + c.end);
        }

        String impactIndex = Integer.toString(swingOne.findImpact());
        System.out.println("Guess of impact index: " + impactIndex);
    }
}