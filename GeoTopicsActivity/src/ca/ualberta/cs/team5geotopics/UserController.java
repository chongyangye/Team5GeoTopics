package ca.ualberta.cs.team5geotopics;

import android.graphics.Bitmap;

public class UserController {
	private User mUser;

	public UserController() {
		this.mUser = User.getInstance();
	}

	public UserController(User user){
		this.mUser = user;
	}
	/**
	 * Used to add OR remove a comment ID from the bookmark's list. If the ID
	 * exists its removed, else its added.
	 * 
	 * @param ID
	 *            The comment ID
	 */
	public void bookmark(CommentModel comment) {
		mUser.bookmark(comment);
	}

	/**
	 * This method is used to tell the user model that we are viewing a specific
	 * comment. This allows us to remove it from the want to read bookmarks list
	 * upon reading.
	 * 
	 * @param comment
	 *            The comment we are reading
	 */
	public void readingComment(CommentModel comment) {
		if (mUser.inBookmarks(comment)) {
			mUser.removeBookmark(comment);
		}
	}

	/**
	 * Used to add OR remove a comment ID from the favourites's list. If the ID
	 * exists its removed, else its added.
	 * 
	 * @param ID
	 *            The comment ID
	 */
	public void favourite(CommentModel comment) {
		mUser.favourite(comment);
	}
	
	public void updateProfile(String userName, String contactInfo, String bio, Bitmap profile){
		mUser.update(userName, contactInfo, bio, profile);
	}
}
