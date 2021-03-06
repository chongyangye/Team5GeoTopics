package ca.ualberta.cs.team5geotopics.test;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import ca.ualberta.cs.team5geotopics.BitmapJsonConverter;
import ca.ualberta.cs.team5geotopics.BrowseActivity;
import ca.ualberta.cs.team5geotopics.Cache;
import ca.ualberta.cs.team5geotopics.CacheIO;
import ca.ualberta.cs.team5geotopics.CommentListModel;
import ca.ualberta.cs.team5geotopics.GeoTopicsApplication;

import ca.ualberta.cs.team5geotopics.CommentModel;
import ca.ualberta.cs.team5geotopics.CommentSearch;
import ca.ualberta.cs.team5geotopics.TopLevelActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class F_CacheTests extends
		ActivityInstrumentationTestCase2<TopLevelActivity> {
	
	private Activity mActivity;

	public F_CacheTests() {
		super(TopLevelActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		GeoTopicsApplication application = GeoTopicsApplication.getInstance();
		application.setContext(mActivity);
	}

	public void testFileIO(){
		Cache cache = Cache.getInstance();
		String filename = "testFile";
		
		Bitmap pic = Bitmap.createBitmap(10,10 ,Bitmap.Config.ARGB_8888);

		ArrayList<CommentModel> acm = new ArrayList<CommentModel>();
		CommentModel comment1 = new CommentModel("1.2", "1.111", "body1", "Author1", "Title1", null, "testAuthorId");
		CommentModel comment2 = new CommentModel("2", "2", "body2", "Author2", "Title2", pic, "testAuthorId");
		
		
 		acm.add(comment1);
 		acm.add(comment2); //make a list of 2 comments
 		
 		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class, new BitmapJsonConverter());
		Gson gson = builder.create();
		
		String jsonString = gson.toJson(acm);
		CacheIO cacheIO = cache.getCacheIO();
		
		cacheIO.replaceFileHistory(jsonString, filename); //store to file 
		acm.clear();
		
		acm = cacheIO.load(filename); 
		
		assertTrue(!acm.isEmpty()); 
		
		assertTrue(acm.get(0).getmBody().equals(comment1.getmBody()));
		assertTrue(acm.get(0).getmAuthor().equals(comment1.getmAuthor()));
		Log.w("CacheTests", acm.get(0).getmTitle() + " " + comment1.getmTitle());
		assertTrue(acm.get(0).getmTitle().equals(comment1.getmTitle()));
		assertTrue(acm.get(0).getmPicture() == null);
		assertTrue(acm.get(0).getLat().equals(comment1.getLat()));
		assertTrue(acm.get(0).getLon().equals(comment1.getLon()));
		
		assertTrue(acm.get(1).getmBody().equals(comment2.getmBody()));
		assertTrue(acm.get(1).getmAuthor().equals(comment2.getmAuthor()));
		assertTrue(acm.get(1).getmTitle().equals(comment2.getmTitle()));
		assertTrue(acm.get(1).getLat().equals(comment2.getLat()));
		assertTrue(acm.get(1).getLon().equals(comment2.getLon()));
		
		
		assertTrue(acm.get(1).getmPicture().describeContents() == comment2.getmPicture().describeContents());
	}
	
	public void testCacheReadComment(){
		//this test need internet activity and assumes that their is a top level comment in Elasticsearch. For that reason this must run after the ES tests. 
		CommentListModel listModel = new CommentListModel();
		Cache cache = Cache.getInstance();
		CommentSearch search = new CommentSearch(listModel, cache);
		CacheIO cacheIO = cache.getCacheIO();
		
		ArrayList<CommentModel> acm = new ArrayList<CommentModel>(); //this is empty and written to disk to clear the cache. 
		
		
		cache.serializeAndWrite(acm, "history.sav");
		
		acm = cacheIO.load("history.sav"); 
		assertTrue(acm.isEmpty());
		
		Thread thread = search.pullTopLevel((BrowseActivity) mActivity);
		try{
			thread.join();
		}
		catch (InterruptedException e){
			Log.w("EsTestPullReplies", "Thread interrupt");
		}
		acm = cacheIO.load("history.sav"); 
		assertTrue(!acm.isEmpty());
		
	}

}
