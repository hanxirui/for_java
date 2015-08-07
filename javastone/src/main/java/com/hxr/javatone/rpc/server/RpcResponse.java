package com.hxr.javatone.rpc.server;

public class RpcResponse {
    private String requestId;
    private Throwable error;
    private Object result;
    /**
     * @return requestId - {return content description}
     */
    public String getRequestId() {
        return requestId;
    }
    /**
     * @param requestId - {parameter description}.
     */
    public void setRequestId(final String requestId) {
        this.requestId = requestId;
    }
    /**
     * @return error - {return content description}
     */
    public Throwable getError() {
        return error;
    }
    /**
     * @param error - {parameter description}.
     */
    public void setError(final Throwable error) {
        this.error = error;
    }
    /**
     * @return result - {return content description}
     */
    public Object getResult() {
        return result;
    }
    /**
     * @param result - {parameter description}.
     */
    public void setResult(final Object result) {
        this.result = result;
    }
   
}
