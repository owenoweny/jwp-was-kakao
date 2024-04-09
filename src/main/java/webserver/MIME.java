package webserver;

public enum MIME {
    HTML("text/html"),
    CSS("text/css"),
    JS("application/javascript"),
    ICO("image/x-icon"),
    PNG("image/png"),
    JPG("image/jpeg"),
    TXT("text/plain"),
    EOT("font/EOT"),
    SVG("font/SVG"),
    WOFF("font/WOFF"),
    WOFF2("font/WOFF2"),
    TTF("font/ttf");

    public String contentType;

    MIME(String contentType) {
        this.contentType = contentType;
    }

    public boolean isTemplate() {
        return this.equals(MIME.HTML) || this.equals(MIME.TXT);
    }
}