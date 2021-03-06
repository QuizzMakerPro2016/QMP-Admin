package com.qmp.admin.models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
* Classe Questionnaire
*/
public class Questionnaire {
	private java.sql.Date date;
	private int id;
	private int idDomaine;
	private int idUtilisateur;
	private String libelle;
	private String description;
	private Domaine domaine;
	private List<Question> questions;
	private List<Groupe_questionnaire> groupe_questionnaires;
	private List<Question_questionnaire> question_questionnaires;
	private List<Realisation> realisations;
	private Utilisateur utilisateur;

	public Questionnaire() {
		super();
		utilisateur=new Utilisateur();
		realisations=new ArrayList<>();
		question_questionnaires=new ArrayList<>();
		groupe_questionnaires=new ArrayList<>();
		questions = new ArrayList<>();
		domaine=new Domaine();
	}
	
	public Questionnaire(Date date, int id, int idDomaine, int idUtilisateur, String libelle) {
		super();
		utilisateur=new Utilisateur();
		realisations=new ArrayList<>();
		question_questionnaires=new ArrayList<>();
		groupe_questionnaires=new ArrayList<>();
		questions = new ArrayList<>();
		domaine=new Domaine();
		
		this.date = date;
		this.id = id;
		this.idDomaine = idDomaine;
		this.idUtilisateur= idUtilisateur;
		this.libelle = libelle;

	}
	/**
	 * return the value of date
	 * @return date
	 */
	public java.sql.Date getDate(){
		return this.date;
	}
	/**
	 * return the value of id
	 * @return id
	 */
	public int getId(){
		return this.id;
	}
	/**
	 * return the value of idDomaine
	 * @return idDomaine
	 */
	public int getIdDomaine(){
		return this.idDomaine;
	}
	/**
	 * return the value of idUtilisateur
	 * @return idUtilisateur
	 */
	public int getIdUtilisateur(){
		return this.idUtilisateur;
	}
	/**
	 * return the value of libelle
	 * @return libelle
	 */
	public String getLibelle(){
		return this.libelle;
	}
	
	/**
	 * return the value of description
	 * @return description
	 */
	public String getDescription(){
		return this.description;
	}
	
	/**
	 * return the value of domaine
	 * @return domaine
	 */
	public Domaine getDomaine(){
		return this.domaine;
	}
	/**
	 * return the value of groupe_questionnaires
	 * @return groupe_questionnaires
	 */
	public List<Groupe_questionnaire> getGroupe_questionnaires(){
		return this.groupe_questionnaires;
	}
	/**
	 * return the value of question_questionnaires
	 * @return question_questionnaires
	 */
	public List<Question_questionnaire> getQuestion_questionnaires(){
		return this.question_questionnaires;
	}
	/**
	 * return the value of realisations
	 * @return realisations
	 */
	public List<Realisation> getRealisations(){
		return this.realisations;
	}
	/**
	 * return the value of utilisateur
	 * @return utilisateur
	 */
	public Utilisateur getUtilisateur(){
		return this.utilisateur;
	}
	
	/**
	 * return the value of groupe_questionnaires
	 * @return groupe_questionnaires
	 */
	public List<Question> getQuestions(){
		return this.questions;
	}

	/**
	 * set the value of date
	 * @param aDate
	 */
	public void setDate(java.sql.Date aDate){
		this.date=aDate;
	}
	/**
	 * set the value of id
	 * @param aId
	 */
	public void setId(int aId){
		this.id=aId;
	}
	/**
	 * set the value of idDomaine
	 * @param aIdDomaine
	 */
	public void setIdDomaine(int aIdDomaine){
		this.idDomaine=aIdDomaine;
	}
	/**
	 * set the value of idUtilisateur
	 * @param aIdUtilisateur
	 */
	public void setIdUtilisateur(int aIdUtilisateur){
		this.idUtilisateur=aIdUtilisateur;
	}
	/**
	 * set the value of libelle
	 * @param aLibelle
	 */
	public void setLibelle(String aLibelle){
		this.libelle=aLibelle;
	}
	
	/**
	 * set the value of description
	 * @return aDescription
	 */
	public void setDescription(String aDescription){
		this.description=aDescription;
	}
	
	/**
	 * set the value of domaine
	 * @param aDomaine
	 */
	public void setDomaine(Domaine aDomaine){
		this.domaine=aDomaine;
	}
	/**
	 * set the value of groupe_questionnaires
	 * @param aGroupe_questionnaires
	 */
	public void setGroupe_questionnaires(List<Groupe_questionnaire> aGroupe_questionnaires){
		this.groupe_questionnaires=aGroupe_questionnaires;
	}
	/**
	 * set the value of question_questionnaires
	 * @param aQuestion_questionnaires
	 */
	public void setQuestion_questionnaires(List<Question_questionnaire> aQuestion_questionnaires){
		this.question_questionnaires=aQuestion_questionnaires;
	}
	/**
	 * set the value of realisations
	 * @param aRealisations
	 */
	public void setRealisations(List<Realisation> aRealisations){
		this.realisations=aRealisations;
	}
	/**
	 * set the value of utilisateur
	 * @param aUtilisateur
	 */
	public void setUtilisateur(Utilisateur aUtilisateur){
		this.utilisateur=aUtilisateur;
	}
	@Override
	public String toString() {
		return libelle;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Questionnaire))
			return false;
		else {
			return this.id == ((Questionnaire) o).getId();
		}
	}

	@Override
	public int hashCode() {
		return Integer.valueOf(id).hashCode();
	}
}