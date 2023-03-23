package BeerApp.dal;

import BeerApp.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Statement;


public class BeerCommentDao {
    protected ConnectionManager connectionManager;
	
	// Single pattern: instantiation is limited to one object.
	private static BeerCommentDao instance = null;
	protected BeerCommentDao() {
		connectionManager = new ConnectionManager();
	}
	public static BeerCommentDao getInstance() {
		if(instance == null) {
			instance = new BeerCommentDao();
		}
		return instance;
	}

    public BeerComment create(BeerComment beerComment) throws SQLException {
        String insertBeerComment = "INSERT INTO BeerComment(Content,Created,ReviewId,UserName) VALUES(?,?,?,?);";
        Connection connection = null;
		PreparedStatement insertStmt = null;
		ResultSet resultKey = null;
		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertBeerComment,
				Statement.RETURN_GENERATED_KEYS);
			insertStmt.setString(1, beerComment.getContent());
			insertStmt.setTimestamp(2, new Timestamp(beerComment.getCreated().getTime()));
			insertStmt.setInt(3, beerComment.getBeerReview().getReviewId());
			insertStmt.setString(4, beerComment.getPerson().getUserName());
			insertStmt.executeUpdate();
			
			// Retrieve the auto-generated key and set it, so it can be used by the caller.
			resultKey = insertStmt.getGeneratedKeys();
			int commentId = -1;
			if(resultKey.next()) {
				commentId = resultKey.getInt(1);
			} else {
				throw new SQLException("Unable to retrieve auto-generated key.");
			}
			beerComment.setCommentId(commentId);
			return beerComment;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(connection != null) {
                connection.close();
            }
            if(insertStmt != null) {
                insertStmt.close();
            }
            if(resultKey != null) {
                resultKey.close();
            }
        }
    }

    public BeerComment getBeerCommentFromCommentId(int commentId) {
        String selectBeerComment = "SELECT CommentId,Content,Created,ReviewId,UserName FROM BeerComment WHERE CommentId=?;";
        Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
        try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectBeerComment);
			selectStmt.setString(1, commentId);
			results = selectStmt.executeQuery();
            BeerReviewDao beerReviewDao = BeerReviewDao.getInstance();
            PersonDao personDao = PersonDao.getInstance();
			if(results.next()) {
				int resultCommentId = results.getInt("CommentId");
                String content = results.getString("Content");
                Date created = new Date(results.getTimestamp("Created").getTime());
                int reviewId = results.getInt("ReviewId");
                String userName = results.getString("UserName");

                BeerReview beerReview = new BeerReview(reviewId);
				Persons person = new Persons(userName);
                BeerComment beerComment = new BeerComment(resultCommentId, content, created, beerReview, person);
				return beerComment;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(selectStmt != null) {
				selectStmt.close();
			}
			if(results != null) {
				results.close();
			}
		}
		return null;
    }

    public List<BeerComment> getBeerCommentArrayForPerson(Person person) {
        List<BeerComment> beerComments = new ArrayList<BeerComment>();
        String selectBeerComment = "SELECT CommentId,Content,Created,ReviewId,UserName FROM BeerComment WHERE UserName=?;";
        Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
        try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectBeerComment);
			selectStmt.setString(1, person.getUserName());
			results = selectStmt.executeQuery();
            BeerReviewDao beerReviewDao = BeerReviewDao.getInstance();
			if(results.next()) {
				int commentId = results.getInt("CommentId");
                String content = results.getString("Content");
                Date created = new Date(results.getTimestamp("Created").getTime());
                int reviewId = results.getInt("ReviewId");

                BeerReview beerReview = new BeerReview(reviewId);
                BeerComment beerComment = new BeerComment(resultCommentId, content, created, beerReview, person);
                beerComments.add(beerComment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(selectStmt != null) {
				selectStmt.close();
			}
			if(results != null) {
				results.close();
			}
		}
		return beerComments;
    }

    public List<BeerComment> getBeerCommentArrayForReview(BeerReview beerReview) {
        List<BeerComment> beerComments = new ArrayList<BeerComment>();
        String selectBeerComment = "SELECT CommentId,Content,Created,ReviewId,UserName FROM BeerComment WHERE ReviewId=?;";
        Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;
        try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectBeerComment);
			selectStmt.setString(1, beerReview.getReviewId());
			results = selectStmt.executeQuery();
            PersonsDao personDao = PersonsDao.getInstance();
			if(results.next()) {
				int commentId = results.getInt("CommentId");
                String content = results.getString("Content");
                Date created = new Date(results.getTimestamp("Created").getTime());
                int reviewId = results.getInt("ReviewId");
                String userName = results.getString("UserName");

                Persons person = personsDao.getPersonFromUserName(userName);
                BeerComment beerComment = new BeerComment(commentId, content, created, beerReview, person);
                beerComments.add(beerComment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(selectStmt != null) {
				selectStmt.close();
			}
			if(results != null) {
				results.close();
			}
		}
		return beerComments;
    }

    public BeerComment updateContent(BeerComment beerComment, String newContent) throws SQLException {
		String updateBeerComment = "UPDATE BeerComment SET Content=?,Created=? WHERE CommentId=?;";
		Connection connection = null;
		PreparedStatement updateStmt = null;
		try {
			connection = connectionManager.getConnection();
			updateStmt = connection.prepareStatement(updateBeerComment);
			updateStmt.setString(1, newContent);
			Date newCreatedTimestamp = new Date();
			updateStmt.setTimestamp(2, new Timestamp(newCreatedTimestamp.getTime()));
			updateStmt.setInt(3, beerComment.getCommentId());
			updateStmt.executeUpdate();

			beerComment.setContent(newContent);
			beerComment.setCreated(newCreatedTimestamp);
			return beerComment;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(updateStmt != null) {
				updateStmt.close();
			}
		}
	}

    public BeerComment delete(BeerComment beerComment) throws SQLException {
		String deleteBeerComment = "DELETE FROM BeerComment WHERE CommentId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteBeerComment);
			deleteStmt.setInt(1, beerComment.getCommentId());
			deleteStmt.executeUpdate();

			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(connection != null) {
				connection.close();
			}
			if(deleteStmt != null) {
				deleteStmt.close();
			}
		}
	}

    
    
}
