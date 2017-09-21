import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Swing {

    private ArrayList<Integer> time = new ArrayList<>();
    private ArrayList<Double> ax = new ArrayList<>();
    private ArrayList<Double> ay = new ArrayList<>();
    private ArrayList<Double> az = new ArrayList<>();
    private ArrayList<Double> wx = new ArrayList<>();
    private ArrayList<Double> wy = new ArrayList<>();
    private ArrayList<Double> wz = new ArrayList<>();

    public Swing(File swingData){

        BufferedReader br = null;
        String line = "";

		//Parse the csv file and put each value into an arraylist
        try {
            br = new BufferedReader(new FileReader(swingData));
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                time.add(Integer.parseInt(row[0]));
                ax.add(Double.parseDouble(row[1]));
                ay.add(Double.parseDouble(row[2]));
                az.add(Double.parseDouble(row[3]));
                wx.add(Double.parseDouble(row[4]));
                wy.add(Double.parseDouble(row[5]));
                wz.add(Double.parseDouble(row[6]));
            }
        } catch (IOException e) {
            System.out.println("Could not open file " + swingData.getName());
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Returns the first index where data exceeds threshold for at least winLength samples.
    public static int searchContinuityAboveValue(ArrayList<Double> data,int indexBegin,int indexEnd,double threshold,int winLength){
        int curWinLength = 0;
        for(int i=indexBegin;i<indexEnd;i++){
            if(data.get(i)>threshold){
                curWinLength++;
            }else{
                curWinLength = 0;
            }
            if(curWinLength==winLength){
                return  i-curWinLength+1;//Finds the starting index
            }
        }
        return -1;
    }

    //Returns the first index where data is in between the threshold for at least winLength samples.
    public static int backSearchContinuityWithinRange(ArrayList<Double> data,int indexBegin,int indexEnd,double thresholdLo,double thresholdHi,int winLength){
        int curWinLength = 0;
        for(int i=indexBegin;i<indexEnd;i++){
            if(data.get(i)<thresholdHi && data.get(i)>thresholdLo){
                curWinLength++;
            }else{
                curWinLength = 0;
            }
            if(curWinLength==winLength){
                return  i-curWinLength+1;//Finds the starting index
            }
        }
        return -1;
    }

    public static int searchContinuityAboveValueTwoSignals(ArrayList<Double> data1,ArrayList<Double> data2,int indexBegin,int indexEnd,double threshold1,double threshold2,int winLength){
        int curWinLength = 0;
        for(int i=indexBegin;i<indexEnd;i++){
            if(data1.get(i)>threshold1 && data2.get(i)>threshold2){
                curWinLength++;
            }else{
                curWinLength = 0;
            }
            if(curWinLength==winLength){
                return  i-curWinLength+1;//Finds the starting index
            }
        }
        return -1;
    }

    //Returns the the starting index and ending index of all continuous samples that are between the threshold at least winLength data points.
    public static ArrayList<Continuity> searchMultiContinuityWithinRange(ArrayList<Double> data,int indexBegin,int indexEnd,double thresholdLo,double thresholdHi,int winLength){
        ArrayList<Continuity> continuities = new ArrayList<>();
        int curWinLength = 0;
        for(int i=0;i<indexEnd;i++){
            if(data.get(i)<thresholdHi && data.get(i)>thresholdLo){
                curWinLength++;
            }else{
                if(curWinLength>=winLength){
                    continuities.add(new Continuity(i-curWinLength,i-1));
                }
                curWinLength = 0;
            }
        }
        //Check if it ended with a continuity
        if(curWinLength>=winLength){
            continuities.add(new Continuity(indexEnd-curWinLength,indexEnd-1));
        }
        return continuities;
    }

    //Attempts to guess at what time the likely impact of the baseball was
    public int findImpact(){
        //I think checking the acceleration of the y axis might be a good way to determine the rate of impact
        //The bat should rapidly decelerate when it makes contact with the ball
        for(int i=1;i<ay.size();i++){
            if(ay.get(i-1)-ay.get(i)>1){
                return i-1;
            }
        }
        return -1;
    }

    public int numRows(){
        return time.size();
    }

    //Getters
    public ArrayList<Double> getAx() {return ax;}
    public ArrayList<Double> getAy() {return ay;}
    public ArrayList<Double> getAz() {return az;}
    public ArrayList<Double> getWx() {return wx;}
    public ArrayList<Double> getWy() {return wy;}
    public ArrayList<Double> getWz() {return wz;}
}
