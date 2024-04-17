package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.httpmessage.HttpRequest;
import webserver.httpmessage.HttpResponse;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    private APIHandler apiHandler;
    private StaticResourceHandler staticResourceHandler;

    public RequestHandler(Socket connection, APIHandler apiHandler, StaticResourceHandler staticResourceHandler) {
        this.connection = connection;
        this.apiHandler = apiHandler;
        this.staticResourceHandler = staticResourceHandler;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = HttpRequest.from(new BufferedReader(new InputStreamReader(in)));
            HttpResponse httpResponse;
            if (!httpRequest.getUri().hasExtension()) {
                httpResponse = apiHandler.handle(httpRequest);
            } else {
                httpResponse = staticResourceHandler.handle(httpRequest);
            }

            DataOutputStream dos = new DataOutputStream(out);
            httpResponse.send(dos);
            //TODO: 예외 처리
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
}
