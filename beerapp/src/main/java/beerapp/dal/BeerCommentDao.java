package beerapp.dal;

import beerapp.model.*;

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
		if (instance == null) {
			instance = new BeerCommentDao();
		}
		return instance;
	}

	public BeerComment create(BeerComment beerComment) throws SQLException {
		String insertBeerComment = "INSERT INTO BeerComment(Content,Created,ReviewId,UserName) VALUES(?,?,?,?);";
		ResultSet resultKey = null;
		try (
			Connection connection = connectionManager.getConnection();
			PreparedStatement insertStmt = connection.prepareStatement(insertBeerComment,
					Statement.RETURN_GENERATED_KEYS);
		) {
			insertStmt.setString(1, beerComment.getContent());
			insertStmt.setTimestamp(2, new Timestamp(beerComment.getCreated().getTime()));
			insertStmt.setInt(3, beerComment.getBeerReview().getId());
			insertStmt.setString(4, beerComment.getPerson().getUsername());
			insertStmt.executeUpdate();

			// Retrieve the auto-generated key and set it, so it can be used by the caller.
			resultKey = insertStmt.getGeneratedKeys();
			int commentId = -1;
			if (resultKey.next()) {
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
			if (resultKey != null) {
				resultKey.close();
			}
		}
	}

	public BeerComment getBeerCommentFromCommentId(int commentId) throws SQLException {
		String selectBeerComment = "SELECT CommentId,Content,Created,ReviewId,UserName FROM BeerComment WHERE CommentId=?;";
		ResultSet results = null;
		try (
			Connection connection = connectionManager.getConnection();
			PreparedStatement selectStmt = connection.prepareStatement(selectBeerComment);
		) {
			selectStmt.setInt(1, commentId);
			results = selectStmt.executeQuery();
			BeerReviewsDao beerReviewDao = BeerReviewsDao.getInstance();
			PersonsDao personDao = PersonsDao.getInstance();
			if (results.next()) {
				int resultCommentId = results.getInt("CommentId");
				String content = results.getString("Content");
				Date created = new Date(results.getTimestamp("Created").getTime());
				int reviewId = results.getInt("ReviewId");
				String userName = results.getString("UserName");

				BeerReview beerReview = beerReviewDao.getReviewById(reviewId);
				Person person = personDao.getPersonByUsername(userName);
				BeerComment beerComment = new BeerComment(resultCommentId, content, created, beerReview, person);
				return beerComment;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (results != null) {
				results.close();
			}
		}
		return null;
	}

	public List<BeerComment> getBeerCommentArrayForPerson(Person person) throws SQLException {
		List<BeerComment> beerComments = new ArrayList<BeerComment>();
		String selectBeerComment = "SELECT CommentId,Content,Created,ReviewId,UserName FROM BeerComment WHERE UserName=?;";
		ResultSet results = null;
		
		try (
			Connection connection = connectionManager.getConnection();
			PreparedStatement selectStmt = connection.prepareStatement(selectBeerComment);
			
		) {
			selectStmt.setString(1, person.getUsername());
			results = selectStmt.executeQuery();
			BeerReviewsDao beerReviewDao = BeerReviewsDao.getInstance();
			if (results.next()) {
				int commentId = results.getInt("CommentId");
				String content = results.getString("Content");
				Date created = new Date(results.getTimestamp("Created").getTime());
				int reviewId = results.getInt("ReviewId");

				BeerReview beerReview = beerReviewDao.getReviewById(reviewId);
				BeerComment beerComment = new BeerComment(commentId, content, created, beerReview, person);
				beerComments.add(beerComment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
		}
		return beerComments;
	}

	public List<BeerComment> getBeerCommentArrayForReview(BeerReview beerReview) throws SQLException {
		List<BeerComment> beerComments = new ArrayList<BeerComment>();
		String selectBeerComment = "SELECT CommentId,Content,Created,ReviewId,UserName FROM BeerComment WHERE ReviewId=?;";
		ResultSet results = null;
		try (
			Connection connection = connectionManager.getConnection();
			PreparedStatement selectStmt = connection.prepareStatement(selectBeerComment);
		) {
			selectStmt.setInt(1, beerReview.getId());
			results = selectStmt.executeQuery();
			PersonsDao personDao = PersonsDao.getInstance();
			if (results.next()) {
				int commentId = results.getInt("CommentId");
				String content = results.getString("Content");
				Date created = new Date(results.getTimestamp("Created").getTime());
				int reviewId = results.getInt("ReviewId");
				String userName = results.getString("UserName");

				Person person = personDao.getPersonByUsername(userName);
				BeerComment beerComment = new BeerComment(commentId, content, created, beerReview, person);
				beerComments.add(beerComment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
		}
		return beerComments;
	}

	public BeerComment updateContent(BeerComment beerComment, String newContent) throws SQLException {
		String updateBeerComment = "UPDATE BeerComment SET Content=?,Created=? WHERE CommentId=?;";
		try (
			Connection connection = connectionManager.getConnection();
			PreparedStatement updateStmt = connection.prepareStatement(updateBeerComment);
		) {
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
		}
	}

	public BeerComment delete(BeerComment beerComment) throws SQLException {
		String deleteBeerComment = "DELETE FROM BeerComment WHERE CommentId=?;";

		try (
			Connection connection = connectionManager.getConnection();
			PreparedStatement deleteStmt = connection.prepareStatement(deleteBeerComment);
		) {
			deleteStmt.setInt(1, beerComment.getCommentId());
			deleteStmt.executeUpdate();

			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
		}
	}

}
