package com.qmp.admin.models;

import java.util.ArrayList;
import java.util.List;


/**
* Classe Groupe
*/
public class Groupe {
	private String code;
	private int id;
	private String libelle;
	private List<Groupe_questionnaire> groupe_questionnaires;
	private List<Groupe_utilisateur> groupe_utilisateurs;
	private List<Utilisateur> utilisateurs;
	private List<Questionnaire> questionnaires;
	
	public Groupe() {
		super();
		groupe_utilisateurs=new ArrayList<>();groupe_questionnaires=new ArrayList<>();
		utilisateurs=new ArrayList<>();
		questionnaires=new ArrayList<>();
	}

	/**
	 * return the value of code
	 * @return code
	 */
	public String getCode(){
		return this.code;
	}
	/**
	 * return the value of id
	 * @return id
	 */
	public int getId(){
		return this.id;
	}
	/**
	 * return the value of libelle
	 * @return libelle
	 */
	public String getLibelle(){
		return this.libelle;
	}
	/**
	 * return the value of groupe_questionnaires
	 * @return groupe_questionnaires
	 */
	public List<Groupe_questionnaire> getGroupe_questionnaires(){
		return this.groupe_questionnaires;
	}
	/**
	 * return the value of groupe_utilisateurs
	 * @return groupe_utilisateurs
	 */
	public List<Groupe_utilisateur> getGroupe_utilisateurs(){
		return this.groupe_utilisateurs;
	}
	
	/**
	 * @return the utilisateurs
	 */
	public List<Utilisateur> getUtilisateurs() {
		return this.utilisateurs;
	}
	/**
	 * @param utilisateurs the utilisateurs to set
	 */
	public void setUtilisateurs(List<Utilisateur> aUtilisateurs) {
		this.utilisateurs = aUtilisateurs;
	}
	/**
	 * @return the questionnaires
	 */
	public List<Questionnaire> getQuestionnaires() {
		return this.questionnaires;
	}
	
	public void addQuestionnaire(Questionnaire q){
		this.questionnaires.add(q);
	}
	
	public void removeQuestionnaire(Questionnaire q){
		this.questionnaires.remove(q);
	}
	
	public void addUtilisateur(Utilisateur u){
		this.utilisateurs.add(u);
	}
	
	public void removeUtilisateur(Utilisateur u){
		this.utilisateurs.remove(u);
	}
	/**
	 * @param questionnaires the questionnaires to set
	 */
	public void setQuestionnaires(List<Questionnaire> aQuestionnaires) {
		this.questionnaires = aQuestionnaires;
	}
	/**
	 * set the value of code
	 * @param aCode
	 */
	public void setCode(String aCode){
		this.code=aCode;
	}
	/**
	 * set the value of id
	 * @param aId
	 */
	public void setId(int aId){
		this.id=aId;
	}
	/**
	 * set the value of libelle
	 * @param aLibelle
	 */
	public void setLibelle(String aLibelle){
		this.libelle=aLibelle;
	}
	/**
	 * set the value of groupe_questionnaires
	 * @param aGroupe_questionnaires
	 */
	public void setGroupe_questionnaires(List<Groupe_questionnaire> aGroupe_questionnaires){
		this.groupe_questionnaires=aGroupe_questionnaires;
	}
	/**
	 * set the value of groupe_utilisateurs
	 * @param aGroupe_utilisateurs
	 */
	public void setGroupe_utilisateurs(List<Groupe_utilisateur> aGroupe_utilisateurs){
		this.groupe_utilisateurs=aGroupe_utilisateurs;
	}
	

	
	@Override
	public String toString() {
		return " [code] = " + code+" [libelle] = " + libelle+" [id] = " + id;
	}
}