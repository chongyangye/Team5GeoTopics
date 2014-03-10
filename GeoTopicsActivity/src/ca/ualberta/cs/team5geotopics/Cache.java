package ca.ualberta.cs.team5geotopics;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class Cache extends AModel<AView> {
	private ArrayList<CommentModel> mHistory;

	private static Cache myself = new Cache();

	private Cache() {
		this.mHistory = new ArrayList<CommentModel>();
	}

	public static Cache getInstance() {
		return myself;
	}
	
	public void clearHistory() {
		mHistory.clear();
	}
	
	public void replaceHistory(ArrayList<CommentModel> mHistory, Context context){
		this.mHistory = mHistory;
		this.notifyViews();
		Log.w("Cache-write myCommentsData", "Replace History First");
		//this.writeMyHistory(context, mHistory);
	}
	
	public void addToHistory(CommentModel comment, Context context) {
		mHistory.add(comment);
		this.notifyViews();
		this.writeMyHistory(context, mHistory);
	}
	
	

	// Stubb. Will write the my comments array to disk

	private void writeMyComments(Context context) {

	}
	
	/*
	 * Author: Kevin Tambascio
	 * URL: https://www.tambascio.org/kevin/android/gson-and-android/ (March 6th, 2014)
	 */
	private void writeComments(String name, Context context, ArrayList<CommentModel> savedList) {
//-----------------------------------------------------
		/*use of GraphAdapterBuilder adapted from http://stackoverflow.com/questions/10036958/the-easiest-way-to-remove-the-bidirectional-recursive-relationships
		 by Jesse Wilson taken 2014-03-07 */
		GsonBuilder gsonBuilder = new GsonBuilder();
		new GraphAdapterBuilder()
		    .addType(CommentModel.class)
		    .registerOn(gsonBuilder);
		Gson gson = gsonBuilder.create();
//-----------------------------------------------------
		
		String myCommentsData;
		
		FileOutputStream fos = null;
		try {
			fos = context.openFileOutput(name, Context.MODE_PRIVATE);
			for (int i=0; i<mHistory.size(); i++){
				myCommentsData = gson.toJson(mHistory.get(i)) + "\n"; //delineate comment model elements with newline
				fos.write(myCommentsData.getBytes());
				Log.w("Cache-write myCommentsData", myCommentsData);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Make sure filename is correct (history.sav, bookmarks.sav or favourites.sav) and this will return an arraylist
	 * of the contents of the cache.
	 * 
     * Modified from LonelyTwitter Author:Joshua Campbell 2014-01-24
	 */
	public ArrayList<CommentModel> loadFromCache(Context context, String filename) {
	    Gson gson = new Gson(); 
	    ArrayList<CommentModel> resultList = new ArrayList<CommentModel>();
	    FileInputStream fis = null; 
	    try { 
	        fis = context.openFileInput(filename); 
	        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
	        String line = in.readLine();
	        while (line != null) {
				resultList.add(gson.fromJson(line, CommentModel.class));
				line = in.readLine();
	        }
	    }
	    catch(JsonSyntaxException e) { 
	    	e.printStackTrace();
	    	//TODO: print from system.err stream in LogCat
	    }
	    catch (FileNotFoundException e) { 
	    	e.printStackTrace();
	    } 
	    catch (IOException e) {
			e.printStackTrace();
		}
	    finally { 
	        try { 
	            if(fis != null)
	                fis.close();
	        }
	        catch (IOException e)  { 
	        	e.printStackTrace();
	        }
	    }
		return resultList;
	}

	private void writeMyHistory(Context context, ArrayList<CommentModel> mHistory) {
		writeComments("history.sav", context, mHistory);
	}

	private void writeMyBookmarks(Context context, ArrayList<CommentModel> mBookmarks) {
		writeComments("bookmarks.sav", context, mBookmarks);
	}

	private void writeMyFavourites(Context context, ArrayList<CommentModel> mFavourites) {
		writeComments("favourites.sav", context, mFavourites);
	}
	
	public ArrayList<CommentModel> getHistory() {
		return this.mHistory;
	}
	
	/*
	 * No Longer need this dummy data but keeping it around just in case we need it again.
	 * Can delete once we no longer feel we need it for testing anything.
	 */
	/*
	//Load the cache with dummy data
		private void dummyData() {
			Location l1 = new Location("l1");
			Location l2 = new Location("l2");
			Location l3 = new Location("l3");
			l1.setLatitude(0);
			l1.setLongitude(0.001);
			l2.setLatitude(0);
			l2.setLongitude(2);
			l3.setLatitude(0);
			l3.setLongitude(0.008);
			
			CommentModel tlc1 = new CommentModel(l1, "I am indestructable!!", "Superman", "Info about superman");
			try {
			    Thread.sleep(10);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			CommentModel tlc2 = new CommentModel(l2, "I am a pansy", "Spiderman", "Info about spiderman");
			try {
			    Thread.sleep(10);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			CommentModel tlc3 = new CommentModel(l3, "I can't feel my legs guys", "Professor X", "Info about Professor X");
			
			tlc1.addReply(new CommentModel(l2, "Not if I have Kryptonite!", "Anonymoose"));
			tlc2.addReply(new CommentModel(l1, "I am sure someone loves you", "Green Goblin"));
			
			mHistory.add(tlc3);
			mHistory.add(tlc2);
			mHistory.add(tlc1);
		*/
}
