import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SwingTracker {

    public static void main(String[] args){
        while(true){
            System.out.println("Please input the name of a swing csv file to analyze (Q to quit)");

            //Get the filename from the user and open the file
            Scanner s = new Scanner(System.in);
            String userInput = s.nextLine();
            if(userInput.toLowerCase().equals("q")) break;
            File swingCSV = new File(userInput);
            testSwing(swingCSV);
        }
    }

    private static void testSwing(File swingCSV) {
        //Create a swing data structure with the csv
        Swing swingOne;
        try {
            swingOne = new Swing(swingCSV);
        } catch (IOException ex) {
            System.out.println("No file found named " + swingCSV.getName());
            return;
        }

        System.out.println("Examples of opertaions:\n");

        System.out.println("Back Search Continuity Within Range:");
        int result = Swing.backSearchContinuityWithinRange(swingOne.getAz(),swingOne.numRows()-1,0,0.7,0.8,10);
        System.out.println(result);

        System.out.println("Search Continuity Above Value:");
        result = Swing.searchContinuityAboveValue(swingOne.getAy(),10,1000,4,20);
        System.out.println(result);

        System.out.println("Search Continuity Above Value Two Singals:");
        result =  Swing.searchContinuityAboveValueTwoSignals(swingOne.getWx(),swingOne.getWy(),10,1000,-3,5.0,2);
        System.out.println(result);

        System.out.println("Search Multi Continuity Within Range:");
        ArrayList<Continuity> ranges = Swing.searchMultiContinuityWithinRange(swingOne.getAy(), 0, swingOne.numRows(), 8.0, 9.0, 2);
        for(Continuity c:ranges){
            System.out.println(c.start + "-" + c.end);
        }

        String impactIndex = Integer.toString(swingOne.findImpact(.1));
        System.out.println("Guess of impact index: " + impactIndex);  
    }
}
