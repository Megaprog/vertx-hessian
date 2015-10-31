# Asynchronous Hessian

The approach to export [Hessian](http://hessian.caucho.com/) service through [Vert.x](http://vertx.io/) Verticle

## How to get it?

You can use it as a maven dependency:

```xml
<dependency>
    <groupId>org.jmmo</groupId>
    <artifactId>vertx-hessian</artifactId>
    <version>1.0</version>
</dependency>
```

Or download the latest build at:
    https://github.com/megaprog/vertx-hessian/releases

## How to use it?

Create client interface:

```java
public interface HessianServiceInterface {

    Integer synchronousCall();

    Integer asynchronousCall();
}
```

Create server interface with same method signatures as in client one but asynchronous methods must return CompletableFuture:

```java
public interface HessianServiceInterfaceAsync {

    Integer synchronousCall();

    CompletableFuture<Integer> asynchronousCall();
}
```

Implement server asynchronous interface:

```java
public class HessianServiceInterfaceAsyncImpl implements HessianServiceInterfaceAsync {
    private static final Logger log = Logger.getLogger(HessianServiceInterfaceAsyncImpl.class.getName());

    @Override
    public Integer synchronousCall() {
        log.info(() -> "Calling from http processing thread");
        return 1;
    }

    @Override
    public CompletableFuture<Integer> asynchronousCall() {
        return CompletableFuture.supplyAsync(() -> {
            log.info(() -> "Calling from ForkJoinPool thread");
            return 2;
        });
    }
}
```

Server:

```java
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
```

Client:

```java
public class Client {
    protected static final String SERVICE_URL = "http://localhost:8080";

    public static void main(String[] args) {
        final HessianServiceInterface hessianService = hessianService();

        System.out.println("synchronous method calling result is " + hessianService.synchronousCall());
        System.out.println("asynchronous method calling result is " + hessianService.asynchronousCall());
    }

    protected static HessianServiceInterface hessianService() {
        try {
            return (HessianServiceInterface) new HessianProxyFactory().create(HessianServiceInterface.class, SERVICE_URL);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
```

See more at client-server example:
    https://github.com/Megaprog/vertx-hessian/tree/master/example
    