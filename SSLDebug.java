import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SSLDebug {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java SSLDebug <URL>");
            return;
        }

        String httpsUrl = args[0];

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                    for (X509Certificate cert : certs) {
                        try {
                            cert.checkValidity();
                        } catch (CertificateException e) {
                            System.out.println("Certificate is not valid: " + e.getMessage());
                            System.out.println("Possible Resolution:");
                            System.out.println("1. Check the certificate's validity period.");
                            System.out.println("   - Open the certificate file and check the 'Valid from' and 'Valid to' dates.");
                            System.out.println("2. Ensure the system date and time are correct.");
                            System.out.println("   - On Windows, check the date and time settings in the Control Panel.");
                            System.out.println("   - On Linux, use the 'date' command to check and set the date and time.");
                            System.out.println("3. Obtain a new certificate if the current one is expired.");
                            System.out.println("   - Contact your Certificate Authority (CA) to renew the certificate.");
                        }
                        try {
                            cert.verify(cert.getPublicKey());
                        } catch (Exception e) {
                            System.out.println("Certificate verification failed: " + e.getMessage());
                            System.out.println("Possible Resolution:");
                            System.out.println("1. Ensure the certificate is signed by a trusted Certificate Authority (CA).");
                            System.out.println("   - Check the issuer of the certificate and verify it is a trusted CA.");
                            System.out.println("2. Import the CA certificate into the trust store.");
                            System.out.println("   - On Windows, use the 'certmgr.msc' tool to manage certificates.");
                            System.out.println("   - On Linux, add the CA certificate to the Java trust store using the 'keytool' command.");
                            System.out.println("     Example: keytool -import -alias ca -file ca-cert.crt -keystore $JAVA_HOME/jre/lib/security/cacerts");
                            System.out.println("3. Verify the certificate chain and ensure all intermediate certificates are present.");
                            System.out.println("   - Check the certificate chain in the certificate details and ensure all intermediate certificates are included.");
                        }
                    }
                }
            }}, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            HttpsURLConnection connection = (HttpsURLConnection) new java.net.URL(httpsUrl).openConnection();
            connection.connect();
            System.out.println("SSL connection established successfully.");

        } catch (Exception e) {
            System.out.println("SSL connection failed: " + e.getMessage());
            System.out.println("Possible Resolution:");
            System.out.println("1. Ensure the URL is correct.");
            System.out.println("   - Verify the URL is properly formatted and points to the correct server.");
            System.out.println("2. Verify that the server supports SSL/TLS.");
            System.out.println("   - Check the server configuration to ensure SSL/TLS is enabled.");
            System.out.println("3. Check network connectivity and firewall settings.");
            System.out.println("   - Ensure there are no network issues preventing the connection.");
            System.out.println("   - Check firewall settings to ensure the connection is not being blocked.");
            System.out.println("   - On Windows, use the 'Windows Firewall' settings in the Control Panel.");
            System.out.println("   - On Linux, use 'iptables' or 'firewalld' to manage firewall settings.");
        }
    }
}
