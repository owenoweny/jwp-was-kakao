package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    private APIHandler apiHandler;
    private ResourceHandler resourceHandler;

    public RequestHandler(Socket connection, APIHandler apiHandler, ResourceHandler resourceHandler) {
        this.connection = connection;
        this.apiHandler = apiHandler;
        this.resourceHandler = resourceHandler;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = HttpRequestParser.parse(new BufferedReader(new InputStreamReader(in)));

            HttpResponse httpResponse;
            if (httpRequest.getUri().getExtension().isEmpty()) {
                httpResponse = apiHandler.handle(httpRequest);
            } else {
                httpResponse = resourceHandler.handle(httpRequest);
            }

            DataOutputStream dos = new DataOutputStream(out);
            httpResponse.send(dos);
            //TODO: 예외 처리
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
