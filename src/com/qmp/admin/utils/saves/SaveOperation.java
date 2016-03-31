package com.qmp.admin.utils.saves;

import java.util.Arrays;
import java.util.concurrent.Callable;

public abstract class SaveOperation implements Callable<Object> {
	private Object[] datas;
	private SaveOperationTypes type;
	private Class<? extends Object> clazz;


	public SaveOperation(SaveOperationTypes type, Class<? extends Object> clazz, Object... datas) {
		super();
		this.datas = datas;
		this.type = type;
		this.clazz = clazz;
	}

	public Object[] getDatas() {
		return datas;
	}

	public void setDatas(Object[] datas) {
		this.datas = datas;
	}

	@Override
	public String toString() {
		return type + " [datas=" + Arrays.toString(datas) + "]";
	}

	public SaveOperationTypes getType() {
		return type;
	}

	public void setType(SaveOperationTypes type) {
		this.type = type;
	}
	
	public Class<? extends Object> getClazz(){
		return clazz;
	}
	
	public void setClazz(Class<? extends Object> clazz){
		this.clazz = clazz;
	}
}
