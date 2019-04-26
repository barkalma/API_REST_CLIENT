package ville.servlet;

import java.io.IOException;
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
 * Servlet implementation class SupprimerVille
 */
@WebServlet("/SupprimerVille")
public class SupprimerVille extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SupprimerVille() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		HttpSession session = request.getSession();
	
		HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://localhost:8181/villeFranceDelete");
        
		String ville = request.getParameter("supprimerVille");
		VilleBuilder villebuilder = this.ville(ville);
		
		 // Create some NameValuePair for HttpPost parameters
        List<NameValuePair> arguments = new ArrayList<>();
        arguments.add(new BasicNameValuePair("value", villebuilder.toString()));
        
        
        try {
            post.setEntity(new UrlEncodedFormEntity(arguments));
            HttpResponse reponse = client.execute(post);

            // Print out the response message
            System.out.println(EntityUtils.toString(reponse.getEntity()));
            
        } catch (IOException e) {
        	throw e;
        }		
		session.setAttribute("message", "SuccesSuppr");
		this.getServletContext().getRequestDispatcher("/messageRequete.jsp").forward(request, response);

 
		
		
      

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
