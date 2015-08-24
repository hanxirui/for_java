package com.hxr.javatone.rpc.distribution.client;

import java.lang.reflect.Method;
import java.util.UUID;

import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.Proxy;

import com.hxr.javatone.rpc.distribution.server.RpcRequest;
import com.hxr.javatone.rpc.distribution.server.RpcResponse;

//第九步：实现 RPC 代理
//
//这里使用 Java 提供的动态代理技术实现 RPC 代理（当然也可以使用 CGLib 来实现），具体代码如下：
public class RpcProxy {
    private final String serverAddress;

    public RpcProxy(final String serverAddress) {
        this.serverAddress = serverAddress;
    }


    @SuppressWarnings("unchecked")
    public <T> T create(final Class<?> interfaceClass) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] { interfaceClass },
                new InvocationHandler() {
                    @Override
                    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
                        RpcRequest request = new RpcRequest(); // 创建并初始化 RPC 请求
                        request.setRequestId(UUID.randomUUID().toString());
                        request.setClassName(method.getDeclaringClass().getName());
                        request.setMethodName(method.getName());
                        request.setParameterTypes(method.getParameterTypes());
                        request.setParameters(args);

                        String[] array = serverAddress.split(":");
                        String host = array[0];
                        int port = Integer.parseInt(array[1]);
                        RpcClient client = new RpcClient(host, port); // 初始化 RPC 客户端
                        RpcResponse response = client.send(request); // 通过 RPC 客户端发送 RPC 请求并获取 RPC 响应
                        if (response.getError()!=null) {
                            throw response.getError();
                        } else {
                            return response.getResult();
                        }
                    }
                });
    }
}