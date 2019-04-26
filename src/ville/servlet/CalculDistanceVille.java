package ville.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ville.bean.VilleBuilder;

/**
 * Servlet implementation class CalculDistanceVille
 */
@WebServlet("/CalculDistanceVille")
public class CalculDistanceVille extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CalculDistanceVille() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		String ville1 = request.getParameter("ville1");
		String ville2 = request.getParameter("ville2");
		VilleBuilder villebuilder1 = new VilleBuilder();
		VilleBuilder villebuilder2 = new VilleBuilder();

		villebuilder1.setCodeCommuneInsee(ville1);
		villebuilder2.setCodeCommuneInsee(ville2);

		String responseVille1;
		String responseVille2;

		URL url1 = new URL("http://localhost:8181/villeFranceFind?value=" + URLEncoder.encode(ville1, "UTF-8"));
		URL url2 = new URL("http://localhost:8181/villeFranceFind?value=" + URLEncoder.encode(ville2, "UTF-8"));

		responseVille1 = this.connection(url1);

		villebuilder1 = this.toVille(responseVille1);

		responseVille2 = this.connection(url2);

		villebuilder2 = this.toVille(responseVille2);

		double distance = this.calculDistance(Double.parseDouble(villebuilder1.getLattitude().replace("\"", "")),
				Double.parseDouble(villebuilder1.getLongitude().replace("\"", "")),
				Double.parseDouble(villebuilder2.getLattitude().replace("\"", "")),
				Double.parseDouble(villebuilder2.getLongitude().replace("\"", "")));

		DecimalFormat df = new DecimalFormat("#.##");

		session.setAttribute("distance", df.format(distance));
		session.setAttribute("ville1", villebuilder1.getNomCommune());
		session.setAttribute("ville2", villebuilder2.getNomCommune());

		this.getServletContext().getRequestDispatcher("/distanceCalcule.jsp").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);

	}

	public VilleBuilder toVille(String villeString) {

		VilleBuilder ville = new VilleBuilder();
		villeString.replace(" ", "");
		villeString.replace("[", "");
		villeString.replace("]", "");
		villeString.replace("\"", "");

		String[] villepart = villeString.split(",");
		String codeCommune = villepart[0].split(":")[1];
		String nomCommune = villepart[1].split(":")[1];
		String codePostal = villepart[2].split(":")[1];
		String libelle = villepart[3].split(":")[1];
		String ligne = "";
		if (villepart[4].split(":").length == 2) {
			ligne = villepart[4].split(":")[1];
		}
		String lattitude = villepart[5].split(":")[1];
		String longitude = villepart[6].split(":")[1].split("}")[0];
		longitude = longitude.split("]")[0];
		ville.setCodeCommuneInsee(codeCommune);
		ville.setNomCommune(nomCommune);
		ville.setCodePostal(codePostal);
		ville.setLibelleAcheminement(libelle);
		ville.setLigne5(ligne);
		ville.setLattitude(lattitude);
		ville.setLongitude(longitude);
		return ville;
	}

	protected String connection(URL url) throws IOException {
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuilder response1 = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			response1.append(inputLine);
		}
		in.close();

		return response1.toString();
	}

	protected double calculDistance(double villeDepartLatitude, double villeDepartLongitude,
			double villeArriveeLatitude, double villeArriveeLongitude) {

		double latitudeDegre1 = Math.toDegrees(villeDepartLatitude);
		double longitudeDegre1 = Math.toDegrees(villeDepartLongitude);
		double latitudeDegre2 = Math.toDegrees(villeArriveeLatitude);
		double longitudeDegre2 = Math.toDegrees(villeArriveeLongitude);

		double distance = 6372 * (Math.acos(Math.sin(latitudeDegre1) * Math.sin(latitudeDegre2)
				+ Math.cos(latitudeDegre1) * Math.cos(latitudeDegre2) * Math.cos(longitudeDegre2 - longitudeDegre1)));

		return distance;
	}
}
