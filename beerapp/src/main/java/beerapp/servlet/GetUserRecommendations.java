package beerapp.servlet;

import beerapp.dal.BeerReviewsDao;
import beerapp.dal.BeersDao;
import beerapp.model.Beer;
import beerapp.model.BeerReview;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/getuserrecommendations")
public class GetUserRecommendations extends HttpServlet {

    protected BeerReviewsDao beerReviewsDao;

    @Override
    public void init() throws ServletException {
        beerReviewsDao = BeerReviewsDao.getInstance();
    }

    /**
     * @return true if all keys exist within {@link HttpServletRequest#getParameterMap()}.
     */
    private static boolean requestContainsParameters(HttpServletRequest request, String... keys) {
        return Arrays.stream(keys).allMatch(key -> request.getParameterMap().containsKey(key));
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Map for storing messages.
        // Map<String, String> messages = new HashMap<>();
        // req.setAttribute("messages", messages);
        List<List<String>> top10Recs = new ArrayList<>();

        if (!requestContainsParameters(req, "aroma", "palate", "taste", "appearance")) {
            throw new RuntimeException(
              "some parameters are missing. View the docs for a list of necessary parameters.");
        }

        try {
            int aroma = Integer.parseInt(req.getParameter("aroma"));
            int palate = Integer.parseInt(req.getParameter("palate"));
            int taste = Integer.parseInt(req.getParameter("taste"));
            int appearance = Integer.parseInt(req.getParameter("appearance"));

            top10Recs = beerReviewsDao.getPersonalizedBeerRecommendations(palate, taste, aroma, appearance);

        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

        String json = new Gson().toJson(top10Recs);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);

    }
}
