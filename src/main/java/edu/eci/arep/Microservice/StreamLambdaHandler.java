package edu.eci.arep.Microservice;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Application;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class StreamLambdaHandler implements RequestStreamHandler {
   private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
   static {
       try {
           handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(MicroserviceApplication.class);
       } catch (ContainerInitializationException e) {
           // if we fail here. We re-throw the exception to force another cold start
           e.printStackTrace();
           throw new RuntimeException("Could not initialize Spring Boot application", e);
       }
   }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AwsProxyRequest request = mapper.readValue(inputStream, AwsProxyRequest.class);

        String requestBody = request.getBody();
        System.out.println("Body recibido: " + requestBody);

        // Procesar la solicitud con el handler de Spring Boot
        handler.proxyStream(inputStream, outputStream, context);
    }

}