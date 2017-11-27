package dataGen;

public class task {

	String subTask, task, taskType, role;
	
	public task(String subTask, String task, String taskType, String role) {
		super();
		this.subTask = subTask;
		this.task = task;
		this.taskType = taskType;
		this.role = role;
	}

	public String getSubTask() {
		return subTask;
	}

	public String getTask() {
		return task;
	}

	public String getTaskType() {
		return taskType;
	}

	public String getRole() {
		return role;
	}
	
}