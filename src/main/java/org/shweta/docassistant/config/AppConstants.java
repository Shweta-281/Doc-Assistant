package org.shweta.docassistant.config;

public class AppConstants {

    public static final String[] PUBLIC_URLS = {
            "/api/auth/register",
            "/api/auth/login",
            "/error"
    };

    public static final String[] PRIVATE_URLS = {
            "/api/doc_assistant/upload",
            "/api/doc_assistant/download",
            "/api/doc_assistant/files",
            "/api/doc_assistant/prompt",
            "/api/doc_assistant/extract"

    };

    public static final long JWT_TOKEN_VALIDITY = 86400000;

    public static final String JWT_SECRET_DEFAULT =
            "rl$psc^nas%llss!iz@zclo#sa&dlsa;s18*392da%ksid#n3457@9375%";
}
