package enums;

import exceptions.HttpRequestFormatException;

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

    public static MIME from(String mimeString) {
        MIME mime;
        try {
            mime = valueOf(mimeString);
        } catch (IllegalArgumentException e) {
            throw new HttpRequestFormatException("지원하지 않는 MIME입니다.");
        }
        return mime;
    }
}