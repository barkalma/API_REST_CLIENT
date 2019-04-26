package ville.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ville.bean.VilleMeteo;

/**
 * Servlet implementation class AfficheMeteo
 */
@WebServlet("/AfficheMeteo")
public class AfficheMeteo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AfficheMeteo() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String ville = request.getParameter("ville");


		URL url = new URL("http://localhost:8181/villeFranceFind?value=" + URLEncoder.encode(ville, "UTF-8"));

		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		
		StringBuffer response1 = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response1.append(inputLine);
		}
		in.close();
		
		ville = response1.toString();
		VilleMeteo villeMeteo = this.toVilleMeteo(ville);

		URL urlMeteo = new URL("http://api.openweathermap.org/data/2.5/weather?" + "lat=" + villeMeteo.getLattitude().split("\"")[1] + "&lon="
				+ villeMeteo.getLongitude().split("\"")[1] + "&APPID=06560519526bae616c17bb73cae22647");

		HttpURLConnection conMeteo = (HttpURLConnection) urlMeteo.openConnection();
		conMeteo.setRequestMethod("GET");

		BufferedReader inMeteo = new BufferedReader(new InputStreamReader(conMeteo.getInputStream()));
		String inputLineMeteo;
		StringBuffer responseMeteo = new StringBuffer();

		while ((inputLineMeteo = inMeteo.readLine()) != null) {
			responseMeteo.append(inputLineMeteo);
		}
		inMeteo.close();


		int debutTemps = responseMeteo.indexOf("weather");
		int finTemps = responseMeteo.indexOf(",\"description\"");
		int debutTemperature = responseMeteo.indexOf("temp\":");
		int finTemperature = responseMeteo.indexOf(",\"pressure");
		int debutIcone = responseMeteo.indexOf("\"weather\":[{");
		int finIcone = responseMeteo.indexOf("],\"base\"");

		String temps = responseMeteo.substring(debutTemps + 7, finTemps);
		temps = temps.substring(temps.indexOf("main\":") + 7, temps.length() - 1);

		String temperature = responseMeteo.substring(debutTemperature + 6, finTemperature);
		String temperatureCelcius = kelvinToCelcius(temperature);

		String icone = responseMeteo.substring(debutIcone + 12, finIcone);
		String cheminIcone = "http://openweathermap.org/img/w/"
				+ icone.substring(icone.indexOf("icon") + 7, icone.indexOf("}") - 1) + ".png";
		String population = "Inconnu";

		try {
			URL urlPop = new URL("https://geo.api.gouv.fr/communes?codePostal="+ villeMeteo.getCodePostal().split("\"")[1] +"&fields=population");
			HttpsURLConnection conPop = (HttpsURLConnection) urlPop.openConnection();
			conPop.setRequestMethod("GET");

			BufferedReader inPop = new BufferedReader(new InputStreamReader(conPop.getInputStream()));
			String inputLinePop;
			StringBuffer responsePop = new StringBuffer();

			while ((inputLinePop = inPop.readLine()) != null) {
				responsePop.append(inputLinePop);
			}
			inPop.close();

			if (!responsePop.equals("Not Found")) {
				population = responsePop.substring(responsePop.indexOf("population") + 12,
						responsePop.indexOf(",\"nom\""));
			} else {
				population = "Inconnu";
			}
		} catch (Exception e) {
			throw e;
		}

		HttpSession session = request.getSession();

		villeMeteo.setTemperature(temperatureCelcius);
		villeMeteo.setTemps(temps);
		villeMeteo.setIdTemps(cheminIcone);
		villeMeteo.setPop(population);

		session.setAttribute("villeMeteo", villeMeteo);

		this.getServletContext().getRequestDispatcher("/resultatMeteo.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private static String kelvinToCelcius(String temperature) {
		Double celcius = Double.parseDouble(temperature) - 273.15;
		DecimalFormat f = new DecimalFormat();
		f.setMaximumFractionDigits(2);
		return f.format(celcius);
	}
	
	public VilleMeteo toVilleMeteo(String villeString) {

		VilleMeteo ville = new VilleMeteo();
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

}
