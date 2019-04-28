<!DOCTYPE html>
<%@page import="ville.bean.VilleBuilder"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<html lang="en">

<head>

<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">

<title>Villes de France</title>

<!-- Bootstrap core CSS -->
<link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom fonts for this template -->
<link
	href="https://fonts.googleapis.com/css?family=Raleway:100,100i,200,200i,300,300i,400,400i,500,500i,600,600i,700,700i,800,800i,900,900i"
	rel="stylesheet">
<link
	href="https://fonts.googleapis.com/css?family=Lora:400,400i,700,700i"
	rel="stylesheet">

<!-- Custom styles for this template -->
<link href="css/business-casual.min.css" rel="stylesheet">

</head>

<body>

	<h1 class="site-heading text-center text-white d-none d-lg-block">
		<!-- <span class="site-heading-upper text-primary mb-3">A Free Bootstrap 4 Business Theme</span> -->
		<span class="site-heading-lower">Les villes de France</span>
	</h1>

	<!-- Navigation -->
	<nav class="navbar navbar-expand-lg navbar-dark py-lg-4" id="mainNav">
		<div class="container">
			<a
				class="navbar-brand text-uppercase text-expanded font-weight-bold d-lg-none"
				href="#">Start Bootstrap</a>
			<button class="navbar-toggler" type="button" data-toggle="collapse"
				data-target="#navbarResponsive" aria-controls="navbarResponsive"
				aria-expanded="false" aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarResponsive">
				<ul class="navbar-nav mx-auto">
					<li class="nav-item active px-lg-4"><a
						class="nav-link text-uppercase text-expanded" href="index.html">Accueil
							<span class="sr-only">(current)</span>
					</a></li>
					<li class="nav-item px-lg-4"><a
						class="nav-link text-uppercase text-expanded"
						href="UtilisationVille">Gestion des villes</a></li>
					<li class="nav-item px-lg-4"><a
						class="nav-link text-uppercase text-expanded" href="ListeVille">Calculer
							une distance</a></li>
					<li class="nav-item px-lg-4"><a
						class="nav-link text-uppercase text-expanded"
						href="AjoutVille">Ajouter une ville</a></li>
					<li class="nav-item px-lg-4"><a
						class="nav-link text-uppercase text-expanded"
						href="ListeVilleMeteo">Meteo</a></li>

				</ul>
			</div>
		</div>
	</nav>

	<section class="page-section about-heading">
		<div class="container">
			<div class="about-heading-content">
				<div class="row">
					<div>
						<div class="bg-faded rounded p-5">
							<%
								if (session.getAttribute("villeFrance") != null) {
							%>

							<div class="">
								<table class="table table-striped table-bordered table-hover">
									<thead>
										<tr>
											<th scope="col" class="text-center">Code Commune Insee</th>
											<th scope="col" class="text-center">Nom Commune</th>
											<th scope="col" class="text-center">Code Postal</th>
											<th scope="col" class="text-center">Latitude</th>
											<th scope="col" class="text-center">Longitude</th>

										</tr>
									</thead>
									<tbody>
										<%!List<VilleBuilder> ville;%>
										<%
											ville = (ArrayList<VilleBuilder>) session.getAttribute("villeFrance");

												for (VilleBuilder v : ville) {
										%>
										<tr>
											<th class="text-center align-middle"><%=v.getCodeCommuneInsee()%></th>
											<td class="text-center align-middle"><%=v.getNomCommune()%></td>
											<td class="text-center align-middle"><%=v.getCodePostal()%></td>
											<td class="text-center align-middle"><%=v.getLattitude()%></td>
											<td class="text-center align-middle"><%=v.getLongitude()%></td>

											<td class="text-center align-middle">
												<form method="post" action="VilleAmodifier">
													<input type="hidden" name="modifierVille" value="<%=v%>" />
													<input type="submit" class="btn btn-primary btn-md"
														value="Modifier" />
												</form>
											</td>
											<td class="text-center align-middle">
												<form method="post" action="SupprimerVille">
													<input type="hidden" name="supprimerVille" value="<%=v%>" />
													<input type="submit" class="btn btn-primary btn-md"
														value="Supprimer" />
												</form>
											</td>
										</tr>
										<%
											}
										%>
									</tbody>
								</table>
							</div>

							<%
								}
							%>

							<div class="intro-button mx-auto">
								<form method="post" action="UtilisationVille">
									<input type="hidden" name="offset" value="50">
									<%
										if (!session.getAttribute("offset").equals(0)) {
									%>
									<input type="submit" name="precedent"
										class="btn btn-primary btn-md" value="Page précédente">

									<%
										}
									%>


									<input type="submit" name="suivant"
										class="btn btn-primary btn-md" value="Page suivante" />
								</form>
							</div>
							<div>

								<a class="btn btn-primary btn-xl" href="products.html">Retour</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>


	<footer class="footer text-faded text-center py-5">
		<div class="container">
			<p class="m-0 small">Copyright &copy; Villes de France 2019</p>
		</div>
	</footer>

	<!-- Bootstrap core JavaScript -->
	<script src="vendor/jquery/jquery.min.js"></script>
	<script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

</body>

</html>
