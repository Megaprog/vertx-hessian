package org.jmmo.hessian;

import com.caucho.hessian.io.SerializerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import org.jmmo.vertx.InputStreamBuffer;
import org.jmmo.vertx.OutputStreamBuffer;

import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class AbstractHessianVerticleServer extends AbstractVerticle {
    protected Object service;
    protected HessianSkeletonVertx hessianSkeleton;
    protected SerializerFactory serializerFactory;

    @Override
    public void start() throws Exception {
        service = createService();
        hessianSkeleton = createSkeleton();
        serializerFactory = createSerializerFactory();

        final HttpServerOptions httpServerOptions = new HttpServerOptions();
        httpServerOptions.setTcpKeepAlive(false);

        final HttpServer httpServer = vertx.createHttpServer(httpServerOptions);

        httpServer.requestHandler(req -> {
            if (req.method() != HttpMethod.POST) {
                req.response().setStatusCode(500);
                req.response().end("Hessian Requires POST");
                return;
            }

            req.bodyHandler(buffer -> {
                final Buffer outputBuffer = Buffer.buffer();
                try {
                    hessianSkeleton.invokeVertx(service, new InputStreamBuffer(buffer), new OutputStreamBuffer(outputBuffer), serializerFactory).whenComplete((o, t) -> {
                        req.response().putHeader("content-type", "x-application/hessian").end(outputBuffer);
                    });
                } catch (Exception e) {
                    req.response().putHeader("content-type", "text/html").end(exceptionMessage(e));
                }
            });
        }).listen(getPort(), getHost());
    }

    protected HessianSkeletonVertx createSkeleton() {
        return new HessianSkeletonVertx(service, getApiClass());
    }

    protected SerializerFactory createSerializerFactory() {
        return new SerializerFactory();
    }

    protected String exceptionMessage(Exception e) {
        final StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter, true));
        return stringWriter.toString();
    }

    public String getHost() {
        return "0.0.0.0";
    }

    public int getPort() {
        return 8080;
    }

    abstract public Class<?> getApiClass();

    abstract protected Object createService();
}
