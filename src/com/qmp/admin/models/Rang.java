package com.qmp.admin.models;

import java.util.ArrayList;
import java.util.List;


/**
* Classe Rang
*/
public class Rang {
	private int id;
	private String libelle;
	private List<Utilisateur> utilisateurs;

	public Rang() {
		super();
		utilisateurs=new ArrayList<>();
	}
	
	public Rang(int id, String libelle) {
		super();
		this.id = id;
		this.libelle = libelle;
		this.utilisateurs=new ArrayList<>();
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
	 * return the value of utilisateurs
	 * @return utilisateurs
	 */
	public List<Utilisateur> getUtilisateurs(){
		return this.utilisateurs;
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
	 * set the value of utilisateurs
	 * @param aUtilisateurs
	 */
	public void setUtilisateurs(List<Utilisateur> aUtilisateurs){
		this.utilisateurs=aUtilisateurs;
	}
	@Override
	public String toString() {
		return libelle;
	}
}