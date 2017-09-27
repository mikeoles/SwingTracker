# SwingTracker

**Instructions**

SwingTracker.java shows a sample run of this program.  

Just compile with: javac SwingTracker.java

Run with: java SwingTracker

Choose csv file name to pass into the Swing class which contains the swing data.
Enter "Q" to quit

**Uses**

After the data from the csv file is loaded you can run different opperations on it:
- searchContinuityAboveValue(data, indexBegin, indexEnd, threshold, winLength)
- backSearchContinuityWithinRange(data, indexBegin, indexEnd, thresholdLo, thresholdHi, winLength)
- searchContinuityAboveValueTwoSignals(data1, data2, indexBegin, indexEnd, threshold1, threshold2, winLength)
- searchMultiContinuityWithinRange(data, indexBegin, indexEnd, thresholdLo, thresholdHi, winLength)
- findImpact();
