package ville.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import ville.bean.VilleBuilder;

/**
 * Servlet implementation class AjoutVille
 */
@WebServlet("/AjoutVille")
public class AjoutVille extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AjoutVille() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.setAttribute("messageErreur", "");

		this.getServletContext().getRequestDispatcher("/ajouterVille.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost("http://localhost:8181/villeFrancePut");

		VilleBuilder villeFrance = new VilleBuilder();
		villeFrance.setCodeCommuneInsee(request.getParameter("codeCommuneInsee"));
		villeFrance.setNomCommune(request.getParameter("nomCommune"));
		villeFrance.setCodePostal(request.getParameter("codePostal"));
		villeFrance.setLibelleAcheminement(request.getParameter("libelleAcheminement"));
		villeFrance.setLigne5(request.getParameter("ligne5"));
		villeFrance.setLattitude(request.getParameter("lattitude"));
		villeFrance.setLongitude(request.getParameter("longitude"));

		List<VilleBuilder> listeVille = this.getListVille(request, response, villeFrance.getCodeCommuneInsee());
		// Create some NameValuePair for HttpPost parameters

		if (listeVille.isEmpty()) {
			List<NameValuePair> arguments = new ArrayList<>();
			arguments.add(new BasicNameValuePair("value", villeFrance.toString()));

			try {
				post.setEntity(new UrlEncodedFormEntity(arguments));
				client.execute(post);


			} catch (IOException e) {
				throw e;
			}
			session.setAttribute("message", "succesAjout");
			this.getServletContext().getRequestDispatcher("/messageRequete.jsp").forward(request, response);
		} else {
			session.setAttribute("messageErreur", "Code INSEE déjà existant");

			this.getServletContext().getRequestDispatcher("/ajouterVille.jsp").forward(request, response);
		}

	}

	protected List<VilleBuilder> getListVille(HttpServletRequest request, HttpServletResponse response,
			String codeCommuneInsee) throws ServletException, IOException {


		URL url = new URL(
				"http://localhost:8181/villeFranceFind?value=" + URLEncoder.encode(codeCommuneInsee, "UTF-8"));
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response1 = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response1.append(inputLine);
		}
		in.close();

		List<VilleBuilder> villes = new ArrayList<VilleBuilder>();
		
		if (response1.length() != 2) {
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
		}
		return villes;
	}

}
