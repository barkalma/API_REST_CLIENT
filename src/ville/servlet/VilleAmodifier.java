package ville.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ville.bean.VilleBuilder;

/**
 * Servlet implementation class VilleAmodifier
 */
@WebServlet("/VilleAmodifier")
public class VilleAmodifier extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public VilleAmodifier() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String ville = request.getParameter("modifierVille");
		VilleBuilder villebuilder = this.ville(ville);
		session.setAttribute("villeFrance", villebuilder);
		this.getServletContext().getRequestDispatcher("/modifierVille.jsp").forward(request, response);

	}

	protected VilleBuilder ville(String villeString) {

		VilleBuilder ville = new VilleBuilder();
		villeString.replace(" ", "");
		villeString.replace("[", "");
		villeString.replace("]", "");
		String[] villepart = villeString.split(",");
		String codeCommune = villepart[0].split("=")[1];
		String nomCommune = villepart[1].split("=")[1];
		String codePostal = villepart[2].split("=")[1];
		String libelle = villepart[3].split("=")[1];
		String ligne = "";
		if (villepart[4].split("=").length == 2) {
			ligne = villepart[4].split("=")[1];
		}
		String lattitude = villepart[5].split("=")[1];
		String longitude = villepart[6].split("=")[1];
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
