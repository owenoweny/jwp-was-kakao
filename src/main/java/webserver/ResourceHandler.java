package webserver;

import enums.MIME;
import utils.FileIoUtils;

import java.io.IOException;
import java.net.URISyntaxException;

public class ResourceHandler {
    public HttpResponse handle(HttpRequest httpRequest) throws IOException, URISyntaxException {
        String path = "./static";
        MIME mime = httpRequest.getUri().getExtension();
        if (mime.isTemplate()) {
            path = "./templates";
        }
        byte[] body = FileIoUtils.loadFileFromClasspath(path + httpRequest.getUri().getPath());

        return HttpResponse.staticResource(body, mime);
    }
}
