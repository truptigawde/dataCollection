package org.cabi.pdc.common;

public class UrlFactory {
    private static UrlFactory mInstance;

    public static synchronized UrlFactory getInstance() {
        if (mInstance == null) {
            mInstance = new UrlFactory();
        }
        return mInstance;
    }

    private UrlFactory() {
    }

    private static final String baseUrl = "https://development-api.cabi.org/FormsAdmin/";
    public static final String verifyUserByEmail = baseUrl + "auth/VerifyUser/%s";//m.gupta%40cabi.org
    public static final String verifyUserWithToken = baseUrl + "auth/VerifyUserWithToken/{0}";
    public static final String regenerateToken = baseUrl + "auth/RegenerateToken";
    public static final String postSavePasscode = baseUrl + "auth/savepasscode/%s";

//    m.gupta@pwdoctor.org token (Dev)- eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySWQiOiJmMTU0NGJhYi1jMGIzLTRiYTQtOTE5Yi02NDM2Yzc4YjBmY2MiLCJQaW4iOjEyMzQsIkV4cGlyZXMiOiIyMDE4LTA1LTE1VDExOjQ0OjUyLjc5NTQ3MTRaIiwiUmVhdXRoZW50aWNhdGlvblRpbWUiOiIyMDE4LTA1LTE1VDE3OjQ0OjUyLjc5NTQ3MTRaIn0=.ojUFOb5/IpIyPkxGt46maFa94RqGfp+RSanob3Vynrk=


//    a.singhal@cabi.org token (Dev)- eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySWQiOiJjNjk1YmExNi0xNmU0LTQ2MjYtOTJjZC05YTFjNWZkYWEyN2MiLCJQaW4iOjEyMzQsIkV4cGlyZXMiOiIyMDE4LTA1LTEwVDEwOjQwOjIzLjc1MTk1MDZaIiwiUmVhdXRoZW50aWNhdGlvblRpbWUiOiIyMDE4LTA1LTEwVDE2OjQwOjIzLjc1MTk1MDZaIn0=.CeH459RVlX91Aztia2M4Ny9t+dbpvQZ7Lahe8pg/1F0=

//    p.kumar@cabi.org token (Dev)- eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySWQiOiJlZTIzNmU2MC04NmIzLTQyYjItYTYwYi1hYTQ4YzZhZTVjNmEiLCJQaW4iOjEyMzQsIkV4cGlyZXMiOiIyMDE4LTA1LTEwVDEwOjQxOjE5LjA1OTk3OTZaIiwiUmVhdXRoZW50aWNhdGlvblRpbWUiOiIyMDE4LTA1LTEwVDE2OjQxOjE5LjA1OTk3OTZaIn0=.d6TCDaHFH8nOFIUw1Bkp7Q46HPTSpec/+/hRJjEmt6Y=

//    s.gupta@pwdoctor.org token (Dev)-eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySWQiOiI5MDZkNjkxOS1hODUzLTQ5ZGMtOGJmZS03MGM2MDNkMjk4MzYiLCJQaW4iOjEyMzQsIkV4cGlyZXMiOiIyMDE4LTA4LTI0VDA5OjE5OjUyLjI0OTIzNDlaIiwiUmVhdXRoZW50aWNhdGlvblRpbWUiOiIyMDE4LTA4LTI0VDE1OjE5OjUyLjI0OTIzNDlaIn0=.1cD998tEhT6RWIVAxLKzHL6Nmyrq7fuFTbBpEfD1b0U=

    public static final String getFormdefs = baseUrl + "formdefs";
    public static final String getFormdefsUpdates = baseUrl + "formdefs/updates";
    public static final String getProject = baseUrl + "projects";
    public static final String getReport = baseUrl + "report";
    public static final String getPostSessions = baseUrl + "sessions";
    public static final String getMetadataTranslations = baseUrl + "metadata/Translation";
    public static final String getMetadataComplete = baseUrl + "metadata/complete";
    public static final String postForms=baseUrl+"forms";

}