package org.saabo.android.support.async;

/**
 * Use this class in async tasks to handle response and exception handling.
 *  
 * @author guersel
 *
 * @param <T> The response type of the AsyncTask
 */
public class AsyncTaskResult<T> {
	
	/** Result type of the task. **/
	private T result;
	private Exception exception;
	
	public AsyncTaskResult(T result) {
		this.result = result;
	}
	
	public AsyncTaskResult(Exception exception) {
		this.exception = exception;
	}

	public T getResult() {
		return result;
	}

	public Exception getException() {
		return exception;
	}
}
