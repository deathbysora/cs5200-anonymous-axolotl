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
import com.google.gson.Gson;

@WebServlet("/getTopBeersByWinterReviews")
public class GetTopBeersByWinterReviews extends HttpServlet {

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
            beerNames = beerReviewsDao.getTopBeersByWinterReviews();
            messages.put("success", "Displaying results for top winter beers.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e);
        }

        String json = new Gson().toJson(beerNames);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Origin");
        try {
            resp.getWriter().write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // req.setAttribute("beerNames", beerNames);
        // req.getRequestDispatcher("GetTopBeersByWinterReviews.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
