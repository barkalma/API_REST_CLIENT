package ville.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ville.bean.VilleBuilder;

/**
 * Servlet implementation class ListeVilleMeteo
 */
@WebServlet("/ListeVilleMeteo")
public class ListeVilleMeteo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListeVilleMeteo() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		
		URL url = new URL("http://localhost:8181/villeFranceGet");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuilder response1 = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			response1.append(inputLine);
		}
		in.close();


		List<VilleBuilder> villes = new ArrayList<VilleBuilder>();
		String str = response1.toString().replace("}]", "");
		String[] respS = str.split("},");
		for (int i = 0; i < respS.length; i++) {
			String[] villepart = respS[i].split(",");
			VilleBuilder ville = new VilleBuilder();
			String codeCommune = villepart[0].split(":")[1];
			String nomCommune = villepart[1].split(":")[1];
			String codePostal = villepart[2].split(":")[1];
			String libelle = villepart[3].split(":")[1];
			String ligne = villepart[4].split(":")[1];
			String lattitude = villepart[5].split(":")[1];
			String longitude = villepart[6].split(":")[1];
			ville.setCodeCommuneInsee(codeCommune.replace("\"", ""));
			ville.setNomCommune(nomCommune.replace("\"", ""));
			ville.setCodePostal(codePostal.replace("\"", ""));
			ville.setLibelleAcheminement(libelle.replace("\"", ""));
			ville.setLigne5(ligne.replace("\"", ""));
			ville.setLattitude(lattitude.replace("\"", ""));
			ville.setLongitude(longitude.replace("\"", ""));
			villes.add(ville);
		}

		session.setAttribute("villeFrance", villes);
		this.getServletContext().getRequestDispatcher("/choixVilleMeteo.jsp").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}


}
