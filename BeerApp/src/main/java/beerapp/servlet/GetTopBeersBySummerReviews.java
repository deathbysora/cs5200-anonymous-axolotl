package beerapp.servlet;

import beerapp.dal.BeerReviewsDao;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/getTopBeersBySummerReviews")
public class GetTopBeersBySummerReviews extends HttpServlet {

    protected BeerReviewsDao beerReviewsDao;

    @Override
    public void init() throws ServletException {
        beerReviewsDao = BeerReviewsDao.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String> messages = new HashMap<String, String>();
        req.setAttribute("messages", messages);

        List<String> beerNames = new ArrayList<>();

        try {
            beerNames = beerReviewsDao.getTopBeersBySummerReviews();
            messages.put("success", "Displaying results for top summer beers.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e);
        }

        req.setAttribute("beerNames", beerNames);
        req.getRequestDispatcher("GetTopBeersBySummerReviews.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
