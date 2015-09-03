import java.io.*;
import java.util.*;

class StableMarriage{
	
	private static int noOfCouples;
	private static int[][] menPrefs;
	private static int[][] womenPrefs;

	public static void main(String[] args) {
		System.out.println("Enter the number of couples: ");
		Scanner scan = new Scanner(System.in);
		noOfCouples = scan.nextInt();

		menPrefs = new int[noOfCouples][noOfCouples];
		womenPrefs = new int[noOfCouples][noOfCouples];

		//fill prefs from 0 to noOfCouples-1 and then shuffle to get randomized prefs for each man and woman
		for(int i=0; i<noOfCouples; ++i){
			for(int j=0; j<noOfCouples; ++j){
				menPrefs[i][j] = j;
				womenPrefs[i][j] = j;
			}
		}
		for(int i=0; i<noOfCouples; ++i){
			shuffleArray(menPrefs[i]);
			shuffleArray(womenPrefs[i]);
		}

		//print prefs for men
		System.out.println("Preferences of men: ");
		for(int i=0; i<noOfCouples; ++i){
			System.out.print(i + ": ");
			for(int j=0; j<noOfCouples; ++j){
				System.out.print(menPrefs[i][j] + " ");
			}
			System.out.println("");
		}
		
		//print prefs for women
		System.out.println("Preferences of women: ");
		for(int i=0; i<noOfCouples; ++i){
			System.out.print(i + ": ");
			for(int j=0; j<noOfCouples; ++j){
				System.out.print(womenPrefs[i][j] + " ");
			}
			System.out.println("");
		}

		//call the matching algorithm
		matchThem(menPrefs, womenPrefs);
	}

	//Implementing Fisher–Yates shuffle
	public static void shuffleArray(int[] ar)
	{
		Random rnd = new Random();
		for (int i=ar.length-1; i>0; --i){
			int index = rnd.nextInt(i+1);
			int temp = ar[index];
			ar[index] = ar[i];
			ar[i] = temp;
		}
	}

	//generate random number between given range
	public static int randInt(int min, int max){
		Random rand = new Random();
		int randNum = rand.nextInt((max - min) + 1) + min;
		return randNum;
	}
	
	//The Gale-Shapley Algorithm to match men and women
	public static void matchThem(int[][] menPrefs, int[][] womenPrefs){
		//set of free men
		Set<Integer> setOfFreeMen = new HashSet<Integer>();
		for(int i=0; i<noOfCouples; ++i){
			setOfFreeMen.add(i);
		}

		//set of free women
		Set<Integer> setOfFreeWomen = new HashSet<Integer>();
		for(int i=0; i<noOfCouples; ++i){
			setOfFreeWomen.add(i);
		}

		//engaged men and women stored in array[woman]
		int[] engagedArray = new int[noOfCouples];
		for(int i=0; i<noOfCouples; ++i){
			engagedArray[i] = 0;
		}

		//while there are free men yet to be engaged
		while(!(setOfFreeMen.isEmpty())){
			
			//choose a man from the set of free men
			Iterator setOfFreeMenIterator = setOfFreeMen.iterator();
			int tempMan = 0;
			if(setOfFreeMenIterator.hasNext()){
				tempMan = (int) setOfFreeMenIterator.next();
			}

			//check if man has already proposed to a woman and choose next woman from man's pref list
			int tempWoman = 0;
			for(int i=0; i<noOfCouples; ++i){
				if(menPrefs[tempMan][i] == -1){
					continue;
				}
				else{
					tempWoman = menPrefs[tempMan][i];
					break;
				}
			}
			//DEBUG
			//System.out.println("tempMan: " + tempMan + " tempWoman: " + tempWoman);

			//variable which tells who the woman is engaged to
			int engagedTo = 0;

			//if chosenWoman is not engaged
			if(setOfFreeWomen.contains(tempWoman)){	
				//DEBUG
				//System.out.println(tempWoman + " not engaged");
				
				//add woman and man to hashmap
				engagedArray[tempWoman] = tempMan;
				//remove man and woman from free sets
				setOfFreeWomen.remove(tempWoman);
				setOfFreeMen.remove(tempMan);
			}
			else{
				engagedTo = engagedArray[tempWoman];
				//DEBUG
				//System.out.println(tempWoman + " engagedTo " + engagedTo);
				
				//if chosenWoman is engaged but chosenMan is more prefered than one engaged to 
				if(checkManPref(tempWoman, tempMan, engagedTo)){
					//DEBUG
					//System.out.println(tempMan + " preferred more");
					
					//add woman and man to hashmap
					engagedArray[tempWoman] = tempMan;
					//add engagedTo to set of free men
					setOfFreeMen.add(engagedTo);
					//remove tempMan from set of free men
					setOfFreeMen.remove(tempMan);
				}
				//if chosenMan is below the engaged man in the woman's pref list
				else{
					//DEBUG
					//System.out.println(tempMan + " not preferred more");
					
					//remove tempWoman from tempMan's prefs
					for(int i=0; i<noOfCouples; ++i){
						if(menPrefs[tempMan][i] == tempWoman){
							menPrefs[tempMan][i] = -1;
						}
					}
				}
			}
		}

		//print the matches
		System.out.println("Matches: ");
		for(int i=0; i<noOfCouples; ++i){
			System.out.println("W: " + i + " & " + "M: " + engagedArray[i]);
		}
	}

	//returns true if chosenWoman prefers chosenMan more
	private static boolean checkManPref(int chosenWoman, int chosenMan, int engagedTo){
		int a = 0; int b = 0;
		for(int i=0; i<noOfCouples; ++i){
			if(womenPrefs[chosenWoman][i] == chosenMan){
				a = i;
			}
			else if(womenPrefs[chosenWoman][i] == engagedTo){
				b = i;
			}
		}
		if(a<b){
			return true;
		}
		else{
			return false;
		}
	}
}