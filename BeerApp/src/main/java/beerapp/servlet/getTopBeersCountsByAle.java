package beerapp.servlet;

import beerapp.dal.BeerReviewsDao;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/getTopBeersCountsByAle")
public class getTopBeersCountsByAle extends HttpServlet {

    private BeerReviewsDao beerReviewsDao;

    @Override
    public void init() throws ServletException {
    	beerReviewsDao = BeerReviewsDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> messages = new HashMap<>();
        request.setAttribute("messages", messages);
        try {
            List<String> beerNames = beerReviewsDao.getTopBeersCountsByAle();
            request.setAttribute("beerNames", beerNames);
            messages.put("success", "Top 10 Ale beers have been retrieved.");
            request.getRequestDispatcher("/displayAle.jsp").forward(request, response);
        } catch (Exception e) {
            messages.put("success", "Error: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
