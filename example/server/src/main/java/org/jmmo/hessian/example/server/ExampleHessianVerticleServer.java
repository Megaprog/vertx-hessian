package org.jmmo.hessian.example.server;

import org.jmmo.hessian.AbstractHessianVerticleServer;

public class ExampleHessianVerticleServer extends AbstractHessianVerticleServer {

    @Override
    public Class<?> getApiClass() {
        return HessianServiceInterfaceAsync.class;
    }

    @Override
    protected Object createService() {
        return new HessianServiceInterfaceAsyncImpl();
    }
}
