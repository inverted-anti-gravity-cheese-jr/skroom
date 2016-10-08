package pl.pg.eti.kio.skroom.model.enumeration;

import pl.pg.eti.kio.skroom.exception.NoSuchTaskStatusException;

/**
 * Enumaration of all possible statuses for tasks in system.
 *
 * @author Wojciech Stanisławski
 * @since 17.08.16
 */
public enum TaskStatus {
	NOT_STARTED(0), IN_PROGRESS(1), AWAITING_REVIEW(2), ACCEPTED(3), BLOCKED(4), COMPLETED(5);

	private int code;

	TaskStatus(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static TaskStatus getByCode(int code) throws NoSuchTaskStatusException {
		for (TaskStatus taskStatus :
				TaskStatus.values()) {
			if(taskStatus.code == code) {
				return taskStatus;
			}
		}
		throw new NoSuchTaskStatusException();
	}
}
