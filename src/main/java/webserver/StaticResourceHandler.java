package webserver;

import enums.MIME;
import utils.FileIoUtils;
import webserver.httpmessage.HttpRequest;
import webserver.httpmessage.HttpResponse;

import java.io.IOException;
import java.net.URISyntaxException;

public class StaticResourceHandler {
    public static final String STATIC_RESOURCE_PATH = "./static";
    public static final String TEMPLATES_RESOURCE_PATH = "./templates";

    public HttpResponse handle(HttpRequest httpRequest) throws IOException, URISyntaxException {
        String path = STATIC_RESOURCE_PATH;
        MIME mime = httpRequest.getUri().getExtension();
        if (mime.isTemplate()) {
            path = TEMPLATES_RESOURCE_PATH;
        }
        byte[] body = FileIoUtils.loadFileFromClasspath(path + httpRequest.getUri().getPath());

        return HttpResponse.staticResource(body, mime);
    }
}
