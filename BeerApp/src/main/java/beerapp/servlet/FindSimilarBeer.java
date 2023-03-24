package beerapp.servlet;

import beerapp.dal.BeersDao;
import beerapp.model.Beer;
import beerapp.model.BeerReview;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * I imagine user will type in a beer name, and it will return a list
 */
@WebServlet("/findsimilarbeer")
public class FindSimilarBeer extends HttpServlet {

    protected BeersDao beerDao;

    @Override
    public void init() throws ServletException {
        beerDao = BeersDao.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
        // Map for storing messages.
        Map<String, String> messages = new HashMap<>();
        req.setAttribute("messages", messages);

        List<Beer> similarBeers = new ArrayList<>();

        // retrieve and validate name
        String beerId = req.getParameter("beerId");
        if (beerId == null || beerId.trim().isEmpty()) {
            messages.put("success", "Please enter a valid name.");
        } else {
            similarBeers.addAll(beerDao.getSimilarBeers(beerDao.getBeerById(Integer.valueOf(beerId))));
            messages.put("success", "Displaying results for " + beerId);
            // Save the previous search term, so it can be used as the default
            // in the input box when rendering FindUsers.jsp.
            messages.put("previousBeerId", beerId);
        }
        req.setAttribute("beers", similarBeers);

        req.getRequestDispatcher("/FindSimilarBeer.jsp").forward(req, resp);
    }

    // it looks like doPost and doGet are identical (from Bruce's code), idk why
    // but here it is...
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

        // Map for storing messages.
        Map<String, String> messages = new HashMap<>();
        req.setAttribute("messages", messages);

        List<Beer> similarBeers = new ArrayList<>();

        // retrieve and validate name
        String beerId = req.getParameter("beerId");
        if (beerId == null || beerId.trim().isEmpty()) {
            messages.put("success", "Please enter a valid name.");
        } else {
            similarBeers.addAll(beerDao.getSimilarBeers(beerDao.getBeerById(Integer.valueOf(beerId))));
            messages.put("success", "Displaying results for " + beerId);
            // Save the previous search term, so it can be used as the default
            // in the input box when rendering FindUsers.jsp.
            messages.put("previousBeerId", beerId);
        }
        req.setAttribute("beers", similarBeers);

        req.getRequestDispatcher("/FindSimilarBeer.jsp").forward(req, resp);
    }

}