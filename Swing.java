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

    public Swing(File swingData)throws IOException{

        BufferedReader br;
        String line;

        //Parse the csv file and put each value into an arraylist
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
    }

    //Returns the first index where data exceeds threshold for at least winLength samples.
    public static int searchContinuityAboveValue(ArrayList<Double> data,int indexBegin,int indexEnd,double threshold,int winLength){
        return searchContinuityWithinRange(data,indexBegin,indexEnd,threshold,Double.MAX_VALUE,winLength);
    }

    //Returns the first index where data is in between the threshold for at least winLength samples.
    public static int backSearchContinuityWithinRange(ArrayList<Double> data,int indexBegin,int indexEnd,double thresholdLo,double thresholdHi,int winLength){
        int curWinLength = 0;
        for(int i=indexBegin;i>indexEnd;i--){
            curWinLength = isInRange(data.get(i),thresholdLo,thresholdHi,curWinLength);
            if(curWinLength==winLength) return  i;//Finds the starting index
        }
        return -1;
    }    
    
    //Finds when two signals are both above the threshold for a specified length
    public static int searchContinuityAboveValueTwoSignals(ArrayList<Double> data1,ArrayList<Double> data2,int indexBegin,int indexEnd,double threshold1,double threshold2,int winLength){
        int data1Begin = 0, data2Begin = 0;//Where the first continuitity is found in the data
        while(data1Begin >= 0 && data2Begin >= 0){
            data1Begin = searchContinuityAboveValue(data1,indexBegin,indexEnd,threshold1,winLength);
            data2Begin = searchContinuityAboveValue(data2,indexBegin,indexEnd,threshold2,winLength);
            if(data1Begin==data2Begin) return data1Begin; //If there's continuity of winLength starting at the same point for both data sets
            indexBegin = Math.max(data1Begin,data2Begin); //index begin is the next possible point both data sets could have a continuity
        }
        return -1;
    }

    //Returns the the starting index and ending index of all continuous samples that are between the threshold at least winLength data points.
    public static ArrayList<Continuity> searchMultiContinuityWithinRange(ArrayList<Double> data,int indexBegin,int indexEnd,double thresholdLo,double thresholdHi,int winLength){
        ArrayList<Continuity> continuities = new ArrayList<>();
        while(indexBegin >= 0){
            //indexBegin is the first index where a continuity is found, continue searching unitl the endIndex of the continuity is found
            indexBegin = searchContinuityWithinRange(data,indexBegin,indexEnd,thresholdLo,thresholdHi,winLength);
            int endIndex = indexBegin+winLength-1;
            while(endIndex+1<data.size() && data.get(endIndex+1) < thresholdHi && data.get(endIndex+1)> thresholdLo) endIndex++;
            if(indexBegin>0||endIndex==data.size()-1){
                continuities.add(new Continuity(indexBegin,endIndex)); 
                indexBegin = endIndex + 2;
            }
        }
        return continuities;
    }    
 
    //Searches for the first series of winLength values that are between high and low
    private static int searchContinuityWithinRange(ArrayList<Double> data,int indexBegin,int indexEnd,double thresholdLo,double thresholdHi,int winLength){
        int curWinLength = 0;
        for(int i=indexBegin;i<indexEnd;i++){
            curWinLength = isInRange(data.get(i),thresholdLo, thresholdHi, curWinLength);
            if(curWinLength==winLength) return  i-curWinLength+1;//Finds the starting index
        }
        return -1;
    }

    //If the value is in the range we're looking for, it increments the winLength and returns it
    private static int isInRange(Double data,double thresholdLo, double thresholdHi, int curWinLength){
            if(data<thresholdHi && data>thresholdLo){
                return curWinLength+1;
            }else{
                return 0;
            }
    }

    //Attempts to guess at what time the likely impact of the baseball was
    public int findImpact(double smallChangeThreshold){
        //I think checking the acceleration of the y axis might be a good way to determine the rate of impact
        //First find the maximum difference between two indexes which should give us around the time that contact occured
        int maxDifIndex = 0;
        double maxDif = 0;
        for(int i=1;i<ay.size();i++){
            double dif = Math.abs(ay.get(i-1)-ay.get(i));
            if(dif>maxDif){
                maxDif = dif;
                maxDifIndex = i;
            }
        }
        //Then go back and find the previous time that there was less than .1 (or maybe a better number) change between indexes
        //Because whenever the y axis accelerometer went from changing slowing to more quickly could be the very start of impact
        for(int i=maxDifIndex; i>1; i--){
            if(Math.abs(ay.get(i-1)-ay.get(i))<smallChangeThreshold) return i;
        }
        return maxDifIndex;
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