package com.qmp.admin.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Utilisateur
 */
public class Utilisateur {
	private int id;
	private int idRang;
	private String mail;
	private String nom;
	private String password;
	private String prenom;
	private Rang rang;
	private List<Groupe_utilisateur> groupe_utilisateurs;
	private List<Question> questions;
	private List<Questionnaire> questionnaires;
	private List<Groupe> groupes;
	private List<Realisation> realisations;

	public Utilisateur() {
		super();
		realisations = new ArrayList<>();
		questionnaires = new ArrayList<>();
		questions = new ArrayList<>();
		groupe_utilisateurs = new ArrayList<>();
		rang = new Rang();
		groupes = new ArrayList<>();
	}

	/**
	 * return the value of id
	 * 
	 * @return id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * return the value of idRang
	 * 
	 * @return idRang
	 */
	public int getIdRang() {
		return this.idRang;
	}

	/**
	 * return the value of mail
	 * 
	 * @return mail
	 */
	public String getMail() {
		return this.mail;
	}

	/**
	 * return the value of nom
	 * 
	 * @return nom
	 */
	public String getNom() {
		return this.nom;
	}

	/**
	 * return the value of password
	 * 
	 * @return password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * return the value of prenom
	 * 
	 * @return prenom
	 */
	public String getPrenom() {
		return this.prenom;
	}

	/**
	 * return the value of rang
	 * 
	 * @return rang
	 */
	public Rang getRang() {
		return this.rang;
	}

	/**
	 * return the value of groupe_utilisateurs
	 * 
	 * @return groupe_utilisateurs
	 */
	public List<Groupe_utilisateur> getGroupe_utilisateurs() {
		return this.groupe_utilisateurs;
	}

	/**
	 * return the value of questions
	 * 
	 * @return questions
	 */
	public List<Question> getQuestions() {
		return this.questions;
	}

	/**
	 * return the value of questionnaires
	 * 
	 * @return questionnaires
	 */
	public List<Questionnaire> getQuestionnaires() {
		return this.questionnaires;
	}

	/**
	 * @return the groupes
	 */
	public List<Groupe> getGroupes() {
		return groupes;
	}

	/**
	 * @param groupes
	 *            the groupes to set
	 */
	public void setGroupes(List<Groupe> groupes) {
		this.groupes = groupes;
	}

	/**
	 * return the value of realisations
	 * 
	 * @return realisations
	 */
	public List<Realisation> getRealisations() {
		return this.realisations;
	}

	/**
	 * set the value of id
	 * 
	 * @param aId
	 */
	public void setId(int aId) {
		this.id = aId;
	}

	/**
	 * set the value of idRang
	 * 
	 * @param aIdRang
	 */
	public void setIdRang(int aIdRang) {
		this.idRang = aIdRang;
	}

	/**
	 * set the value of mail
	 * 
	 * @param aMail
	 */
	public void setMail(String aMail) {
		this.mail = aMail;
	}

	/**
	 * set the value of nom
	 * 
	 * @param aNom
	 */
	public void setNom(String aNom) {
		this.nom = aNom;
	}

	/**
	 * set the value of password
	 * 
	 * @param aPassword
	 */
	public void setPassword(String aPassword) {
		this.password = aPassword;
	}

	/**
	 * set the value of prenom
	 * 
	 * @param aPrenom
	 */
	public void setPrenom(String aPrenom) {
		this.prenom = aPrenom;
	}

	/**
	 * set the value of rang
	 * 
	 * @param aRang
	 */
	public void setRang(Rang aRang) {
		this.rang = aRang;
	}

	/**
	 * set the value of groupe_utilisateurs
	 * 
	 * @param aGroupe_utilisateurs
	 */
	public void setGroupe_utilisateurs(List<Groupe_utilisateur> aGroupe_utilisateurs) {
		this.groupe_utilisateurs = aGroupe_utilisateurs;
	}

	/**
	 * set the value of questions
	 * 
	 * @param aQuestions
	 */
	public void setQuestions(List<Question> aQuestions) {
		this.questions = aQuestions;
	}

	/**
	 * set the value of questionnaires
	 * 
	 * @param aQuestionnaires
	 */
	public void setQuestionnaires(List<Questionnaire> aQuestionnaires) {
		this.questionnaires = aQuestionnaires;
	}

	/**
	 * set the value of realisations
	 * 
	 * @param aRealisations
	 */
	public void setRealisations(List<Realisation> aRealisations) {
		this.realisations = aRealisations;
	}

	@Override
	public String toString() {
		return nom + " " + prenom;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Utilisateur))
			return false;
		else {
			return this.id == ((Utilisateur) o).getId();
		}
	}

	@Override
	public int hashCode() {
		return Integer.valueOf(id).hashCode();
	}

}