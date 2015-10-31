package org.jmmo.hessian.example.client;

import com.caucho.hessian.client.HessianProxyFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Client {
    protected static final String SERVICE_URL = "http://localhost:8080";

    public static void main(String[] args) {
        final HessianServiceInterface hessianService = hessianService();

        System.out.println("synchronous method calling result is " + hessianService.synchronousCall());
        System.out.println("asynchronous method calling result is " + hessianService.asynchronousCall());

        try {
            URLConnection urlConnection = new URL(SERVICE_URL).openConnection();
            urlConnection.connect();

            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static HessianServiceInterface hessianService() {
        try {
            return (HessianServiceInterface) new HessianProxyFactory().create(HessianServiceInterface.class, SERVICE_URL);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
