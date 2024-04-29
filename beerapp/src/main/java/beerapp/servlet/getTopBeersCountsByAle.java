package beerapp.servlet;

import beerapp.dal.BeerReviewsDao;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;

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
        List<String> beerNames = new ArrayList<>();
        try {
            beerNames = beerReviewsDao.getTopBeersCountsByAle();
            request.setAttribute("beerNames", beerNames);
            messages.put("success", "Top 10 Ale beers have been retrieved.");
            // request.getRequestDispatcher("/displayAle.jsp").forward(request, response);
        } catch (Exception e) {
            messages.put("success", "Error: " + e.getMessage());
            // request.getRequestDispatcher("/error.jsp").forward(request, response);
        }

        String json = new Gson().toJson(beerNames);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
