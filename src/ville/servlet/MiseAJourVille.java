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

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import ville.bean.VilleBuilder;





/**
 * Servlet implementation class MiseAJourVille
 */
@WebServlet("/MiseAJourVille")
public class MiseAJourVille extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MiseAJourVille() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		HttpSession session = request.getSession();
	
		HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://localhost:8181/villeFrancePost");
        String codeCommuneInsee = request.getParameter("villeModifie");
        
        VilleBuilder villeFrance = new VilleBuilder();
		villeFrance.setCodeCommuneInsee(request.getParameter("codeCommuneInsee"));
		villeFrance.setNomCommune(request.getParameter("nomCommune"));
		villeFrance.setCodePostal(request.getParameter("codePostal"));
		villeFrance.setLibelleAcheminement(request.getParameter("libelleAcheminement"));
		villeFrance.setLigne5(request.getParameter("ligne5"));
		villeFrance.setLattitude(request.getParameter("lattitude"));
		villeFrance.setLongitude(request.getParameter("longitude"));
		
        // Create some NameValuePair for HttpPost parameters
        List<NameValuePair> arguments = new ArrayList<>();
        arguments.add(new BasicNameValuePair("ville", villeFrance.toString()));
        
        try {
            post.setEntity(new UrlEncodedFormEntity(arguments));
            HttpResponse reponse = client.execute(post);

            
        } catch (IOException e) {
            throw e;
        }		
		session.setAttribute("message", "succesModif");
		this.getServletContext().getRequestDispatcher("/messageRequete.jsp").forward(request, response);

	}

}
