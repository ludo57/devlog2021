package com.springjava.javaspring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Sql({"/test_data.sql"})

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JavaspringApplicationTests {


	@Autowired
	private  WebApplicationContext context;

	private  MockMvc mvc;

	@BeforeEach
	public void initialisationMvc(){
		mvc = MockMvcBuilders.webAppContextSetup(context)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build();
	}

	@WithMockUser(username="Toto",roles={"USER"})
	@Test
	void accesALaMethodePourRecuppererUtilisateur_doitRetournerUnStatutOK()throws Exception {
		mvc.perform(get("/user/utilisateurs")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@WithMockUser(username="Toto",roles={"USER"})
	@Test
	void accesALaMethodePourSupprimerUnUtilisateur_doitRetournerunRetournerUnStatutForbiden() throws Exception {
		mvc.perform(get("/admin/utilisateurs/1"))
				.andExpect(status().isForbidden());
	}

	@WithMockUser(username="Toto",roles={"USER"})
	@Test
	void accesALaMethodePourRecupererUtilisateurAvecid1_doitRetournerUnUtilisateurEnJSON() throws Exception {
		mvc.perform(get("/user/utilisateurs/{%$1}",1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.id").value(1));
	}

	@WithMockUser(username="Ludo",roles={"USER"})
	@Test
	void accesALaMethodePourRecupererListeUtilisateur_doitRetournerUneListeUtilisateurAvecLogonLudo() throws Exception {
		mvc.perform(get("/user/utilisateurs"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].login").value(1))
				.andExpect(jsonPath("$.id").value(1));
	}

	@WithMockUser(username="Ludo",roles={"USER"})
	@Test
	void accesALaMethodePourRecupererListeUtilisateur_doitRetournerUtilisateurAvoir1CompetenceJava() throws Exception {
		mvc.perform(get("/user/utilisateurs"))
				.andExpect(status().isOk())
				//on test le nom de la premiere compétence du premier utilisateur de la liste
				.andExpect(jsonPath("$[0].listeCompetence[0].nom").value("spring"));

	}

}
