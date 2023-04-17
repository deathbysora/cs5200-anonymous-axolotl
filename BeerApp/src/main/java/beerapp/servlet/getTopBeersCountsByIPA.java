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

@WebServlet("/getTopBeersCountsByIPA")
public class getTopBeersCountsByIPA extends HttpServlet {

    private BeerReviewsDao beersReviewsDao;

    @Override
    public void init() throws ServletException {
    	beersReviewsDao = BeerReviewsDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> messages = new HashMap<>();
        request.setAttribute("messages", messages);
        try {
            List<String> beerNames = beersReviewsDao.getTopBeersCountsByIPA();
            request.setAttribute("beerNames", beerNames);
            messages.put("success", "Top 10 IPA beers have been retrieved.");
            request.getRequestDispatcher("/displayIPA.jsp").forward(request, response);
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
