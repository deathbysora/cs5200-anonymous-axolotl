package beerapp.servlet;

import beerapp.dal.BeersDao;
import beerapp.model.Beer;
import java.util.Arrays;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/findbeer")
public class FindBeer extends HttpServlet {

    protected final BeersDao beersDao;
    public FindBeer() {
        beersDao = BeersDao.getInstance();
    }

    /**
     * Simple {@code doGet()} method to build a GET routing for /findbeer.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("message", "Enter the name of the beer to search for.");
    }

    /**
     * Query {@link BeersDao#TABLE_NAME} for a list of beers whose name matches the given pattern.
     * <br>
     * Expected parameters are...
     * <ul>
     *     <li>pattern</li>
     *     <li>pagesize</li>
     *     <li>pagenumber</li>
     * </ul>
     * View {@link BeersDao#getBeersLikeName} for more information on the parameters.
     * <br><br>
     *
     * Modifies {@code request} with the following key values...
     * <ul>
     *     <li>message: the message to display to the user.</li>
     *     <li>beers: a list of {@link Beer}s to display to the user.</li>
     * </ul>
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        if (!requestContainsParameters(request, "pattern", "pagesize", "pagenumber")) {
            throw new RuntimeException("some parameters are missing. View the docs for a list of necessary parameters.");
        }

        String pattern = request.getParameter("pattern");
        if (isBadParameter(pattern)) {
            request.setAttribute("message", "Please enter a valid beer name.");
            return;
        }

        int pageSize = Integer.parseInt(request.getParameter("pagesize"));
        int pageNumber = Integer.parseInt(request.getParameter("pagenumber"));

        List<Beer> beers = beersDao.getBeersLikeName(pattern, pageSize, pageNumber);
        request.setAttribute("beers", beers);
        request.setAttribute("message", "Listing results for " + pattern + ".");
    }

    /**
     * @return true if all keys exist within {@link HttpServletRequest#getParameterMap()}.
     */
    private static boolean requestContainsParameters(HttpServletRequest request, String... keys) {
        return Arrays.stream(keys).allMatch(key -> request.getParameterMap().containsKey(key));
    }

    /**
     * @return {@code true} if {@code value} is {@code null} or an empty string.
     */
    private static boolean isBadParameter(String value) {
        return value == null || value.trim().isEmpty();
    }
}