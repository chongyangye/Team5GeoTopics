package ca.ualberta.cs.team5geotopics.test;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.team5geotopics.BrowseActivity;
import ca.ualberta.cs.team5geotopics.CommentListModel;
import ca.ualberta.cs.team5geotopics.CommentModel;

public class CommentListModelTests extends ActivityInstrumentationTestCase2<BrowseActivity> {

	public CommentListModelTests() {
		super(BrowseActivity.class);
	}
	
	public void testSortRepliesByProximityToLoc() {
		CommentListModel clm = new CommentListModel();
		List<CommentModel> lc = prepCommentList(10, .03, 15, false);
		
		for (int i = 0; i < lc.size(); i++) {
			clm.add(lc.get(i));
		}
		
		Location myLoc = new Location("My Location");
		myLoc.setLongitude(0);
		myLoc.setLatitude(0);
		
		assertTrue("The list is not null", clm.getList().size() > 0);
		assertTrue("The first element of the list is C", clm.getList().get(0).getmBody().equals("C"));
		assertTrue("The second element of the list is B", clm.getList().get(1).getmBody().equals("B"));
		assertTrue("The third element of the list is A", clm.getList().get(2).getmBody().equals("A"));
		
		clm.sortCommentsByProximityToLoc(myLoc);
		
		assertTrue("The first element of the list is B", clm.getList().get(0).getmBody().equals("B"));
		assertTrue("The second element of the list is A", clm.getList().get(1).getmBody().equals("A"));
		assertTrue("The first element of the list is C", clm.getList().get(2).getmBody().equals("C"));
	}
	
	public void testSortCommentsByFreshness() {
		CommentListModel clm = new CommentListModel();
		List<CommentModel> lc = prepCommentList(0.001, .02, 0.008, false);
		
		for (int i = 0; i < lc.size(); i++) {
			clm.add(lc.get(i));
		}
		
		Location myLoc = new Location("My Location");
		myLoc.setLongitude(0);
		myLoc.setLatitude(0);
		
		assertEquals("The size of the array should now be 3", 3, clm.getList().size());
		assertTrue("The first element of the list is C", clm.getList().get(0).getmBody().equals("C"));
		assertTrue("The second element of the list is B", clm.getList().get(1).getmBody().equals("B"));
		assertTrue("The third element of the list is A", clm.getList().get(2).getmBody().equals("A"));
		
		clm.sortCommentsByFreshness(myLoc);
		
		assertEquals("The size of the array should now be 2", 2, clm.getList().size());
		assertTrue("The first element of the list is C", clm.getList().get(0).getmBody().equals("C"));
		assertTrue("The second element of the list is A", clm.getList().get(1).getmBody().equals("A"));
	}

	/*
	 * Test SortTLCByPicture
	 * Handles Used Case 3
	 * 
	 * Initial order of comments C -> B -> A, after sort order should be A -> C -> B
	 * 
	 */
	public void testSortCommentsByPicture() {
		CommentListModel clm = new CommentListModel();
		List<CommentModel> lc = prepCommentList(10, 0.01, 9, true);
		
		Location myLoc = new Location("My Location");
		myLoc.setLongitude(0);
		myLoc.setLatitude(0);
		
		for (int i = 0; i < lc.size(); i++) {
			clm.add(lc.get(i));
		}
		
		assertTrue("The list is not null", clm.getList().size() > 0);
		assertTrue("The first element of the list is C", clm.getList().get(0).getmBody().equals("C"));
		assertTrue("The second element of the list is B", clm.getList().get(1).getmBody().equals("B"));
		assertTrue("The third element of the list is A", clm.getList().get(2).getmBody().equals("A"));
		clm.sortCommentsByPicture(myLoc);
		assertTrue("The first element of the list is C", clm.getList().get(0).getmBody().equals("C"));
		assertTrue("The second element of the list is A", clm.getList().get(1).getmBody().equals("A"));
		assertTrue("The third element of the list is B", clm.getList().get(2).getmBody().equals("B"));
	}
	
	
	/*
	 * This tests the functionality of sortCommentsByDate()
	 * 
	 * This adds three Comments to a list. The three comments are created 
	 * with a delay so that their dates are different. The comments are 
	 * created in the order: A -> B -> C and are originally put into the 
	 * list in order: C -> B -> A. After the sort it should be in order
	 * A -> B -> C
	 */
	public void testSortCommentsByDate() {
		CommentListModel clm = new CommentListModel();
		List<CommentModel> lc = prepCommentList(0.01, 0.02, 0.001, false);
		
		for (int i = 0; i < lc.size(); i++) {
			clm.add(lc.get(i));
		}
		
		assertTrue("List is not empty", clm.getList().size() > 0);
		assertTrue("The first element is originally C", clm.getList().get(0).getmBody().equals("C"));
		assertTrue("The first element is originally B", clm.getList().get(1).getmBody().equals("B"));
		assertTrue("The first element is originally A", clm.getList().get(2).getmBody().equals("A"));
		
		clm.sortAllCommentsByDate();
		
		assertTrue("The first element should be C", clm.getList().get(0).getmBody().equals("C"));
		assertTrue("The first element is originally B", clm.getList().get(1).getmBody().equals("B"));
		assertTrue("The first element is originally A", clm.getList().get(2).getmBody().equals("A"));
	}
	
	public List<CommentModel> prepCommentList(double latA, double latB, double latC,
			boolean hasPhoto) {
		Location locA = new Location("A");
		Location locB = new Location("B");
		Location locC = new Location("C");
		Bitmap bm = null;
		
		if (hasPhoto) {
			bm = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
		} 
		
		locC.setLatitude(latC);
		locC.setLongitude(0);
		
		locB.setLatitude(latB);
		locB.setLongitude(0);
		
		locA.setLatitude(latA);
		locA.setLongitude(0);
		
		CommentModel cA = new CommentModel(locA, "A", "A", bm, "A");
		try {
		    Thread.sleep(10);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		CommentModel cB = new CommentModel(locB, "B", "B", null, "B");
		try {
		    Thread.sleep(10);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		CommentModel cC = new CommentModel(locC, "C", "C", bm, "C");
		
		List<CommentModel> lc = new ArrayList<CommentModel>();
		lc.add(cC);
		lc.add(cB);
		lc.add(cA);
		return lc;	
	}

}