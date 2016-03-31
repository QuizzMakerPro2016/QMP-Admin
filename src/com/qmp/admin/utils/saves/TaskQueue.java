package com.qmp.admin.utils.saves;

import java.io.IOException;
import java.util.Observable;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

import com.qmp.admin.utils.WebGate;

public class TaskQueue extends Observable {
	private DelayQueue<DelayedTask> tasks;
	private DelayedService service;
	private String name;
	private WebGate webGate;
	private int rowGroupSize;

	public TaskQueue(String name, WebGate webGate) {
		this.name = name;
		tasks = new DelayQueue<DelayedTask>();
		this.webGate = webGate;
		this.rowGroupSize = 10;
		service = new DelayedService(this);
	}

	public void put(DelayedTask task) {
		tasks.put(task);
	}

	public void add(Object o) {
		SaveOperation updateOperation = new SaveOperation(SaveOperationTypes.ADD, o.getClass(), o) {
			@Override
			public Object call() throws Exception {
				return webGate.add(o);
			}
		};
		put(new DelayedTask(updateOperation, 5000));
	}

	public void update(Object o, Object id) {
		SaveOperation updateOperation = new SaveOperation(SaveOperationTypes.UPDATE, o.getClass(), o, id) {
			@Override
			public Object call() throws Exception {
				return webGate.update(o, id);
			}
		};
		put(new DelayedTask(updateOperation, 5000));
	}

	public void delete(Object o, Object id) {
		SaveOperation deleteOperation = new SaveOperation(SaveOperationTypes.DELETE, o.getClass(), o, id) {
			@Override
			public Object call() throws Exception {
				return webGate.delete(o, id);
			}
		};
		put(new DelayedTask(deleteOperation, 10000));
	}

	public void get(Class<? extends Object> clazz, int offset, int limit) {
		SaveOperation getOperation = new SaveOperation(SaveOperationTypes.GET, clazz) {

			@Override
			public Object call() throws Exception {
				return webGate.getAll(clazz, offset, limit);
			}
		};
		put(new DelayedTask(getOperation, 50));
	}
	
	public void getLocal(Class<? extends Object> clazz) {
		SaveOperation getOperation = new SaveOperation(SaveOperationTypes.GET_LOCAL, clazz) {
			
			@Override
			public Object call() throws Exception {
				return webGate.getAllLocal(clazz);
			}
		};
		put(new DelayedTask(getOperation, 0));
	}

	public void getAll(Class<? extends Object> clazz) {
		int size = 10;
		try {
			if(webGate.getModifs(clazz)){
				size = webGate.count(clazz);
				for (int i = 0; i < size / rowGroupSize + 1; i++) {
					get(clazz, i * rowGroupSize, rowGroupSize);
				}
			}else{
				getLocal(clazz);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public void start() {
		service.start();
	}

	public void stop() {
		try {
			while (tasks.poll(10, TimeUnit.SECONDS) != null) {
				// TODO Alert waiting message
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		service = null;
	}

	public DelayQueue<DelayedTask> getTasks() {
		return tasks;
	}

	public <T> void setChanged(SaveOperationTypes type, Class<T> clazz, Object resultCall) {
		this.setChanged();
		notifyObservers(new Object[] { type, clazz, resultCall });
	}

	public DelayedService getService() {
		return service;
	}
}
