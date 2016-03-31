package com.qmp.admin.utils.saves;

public enum SaveOperationTypes {
	DELETE("Delete"), GET("Get"), UPDATE("Update"), ADD("Add"), GET_LOCAL("Get_Local");
	private String name = "";

	private SaveOperationTypes(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}
