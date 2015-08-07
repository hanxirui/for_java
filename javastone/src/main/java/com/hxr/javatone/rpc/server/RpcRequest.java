package com.hxr.javatone.rpc.server;

public class RpcRequest {
    private String requestId;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
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
     * @return className - {return content description}
     */
    public String getClassName() {
        return className;
    }
    /**
     * @param className - {parameter description}.
     */
    public void setClassName(final String className) {
        this.className = className;
    }
    /**
     * @return methodName - {return content description}
     */
    public String getMethodName() {
        return methodName;
    }
    /**
     * @param methodName - {parameter description}.
     */
    public void setMethodName(final String methodName) {
        this.methodName = methodName;
    }
    /**
     * @return parameterTypes - {return content description}
     */
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }
    /**
     * @param parameterTypes - {parameter description}.
     */
    public void setParameterTypes(final Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
    /**
     * @return parameters - {return content description}
     */
    public Object[] getParameters() {
        return parameters;
    }
    /**
     * @param parameters - {parameter description}.
     */
    public void setParameters(final Object[] parameters) {
        this.parameters = parameters;
    }
}
