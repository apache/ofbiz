/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.ofbiz.passport.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.ofbiz.base.util.Debug;

public class PassportUtil {

    public static final String module = PassportUtil.class.getName();
    
    public static final String ClientIdLabel = "ClientId";
    
    public static final String SecretLabel = "Secret";

    public static final String ReturnUrlLabel = "ReturnUrl";

    public static final String TokenEndpointLabel = "TokenEndpoint";

    public static final String GrantTypeLabel = "grantType";

    public static final String ContentTypeLabel = "contentType";

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String UserProfileUrlLabel = "UserProfileUrl";

    public static final String GrantTypeParam = "grant_type";

    public static final String ContentTypeParam = "Content-Type";

    public static final String ACCEPT_HEADER = "Accept";

    public static final String APPLICATION_JSON = "application/json";

    public static final String RESTApiEndpointLabel = "RESTApiEndpoint";

    public static final String COMMON_CODE = "code";

    public static final String COMMON_SCOPE = "scope";

    public static final String AuthorizationCodeGrantType = "authorization_code";

    public static final String COMMON_STATE = "state";

    public static final String COMMON_ERROR = "error";

    public static final String COMMON_ERROR_DESCRIPTION = "error_description";

    public static final String ApiKeyLabel = "apiKey";

    public static final String SecretKeyLabel = "secretKey";

    public static final String COMMON_CLIENT_ID = "clientId";

    public static final String COMMON_RETURN_RUL = "returnUrl";

    public static final String COMMON_CLIENT_SECRET = "clientSecret";

    public static final String ApiIdLabel = "apiId";

    public static final String AppKeyLabel = "appKey";

    public static final String AppSecretLabel = "appSecret";

    public static final String AppIdLabel = "appId";

    public static final String COMMON_APP_KEY = "AppKey";

    public static final String COMMON_APP_SECRET = "AppSecret";
    
    protected PassportUtil() {
        // empty constructor
    }
    
    public static PassportUtil getInstance() {
        return new PassportUtil();
    }

    public static String getEnvPrefixByHost(HttpServletRequest request) {
        String prefix ="test";
        try {
            InetAddress[] addresses = InetAddress.getAllByName(request.getServerName());
            for (InetAddress address : addresses) {
                if (address.isAnyLocalAddress() || address.isLinkLocalAddress() || address.isLoopbackAddress()) {
                    return prefix;
                }
            }
            prefix = "live";
        } catch (UnknownHostException e) {
            Debug.logError(e.getMessage(), module);
        }
        return prefix;
    }

    private static String randomString(int lo, int hi) {
        int n = rand(lo, hi);
        byte b[] = new byte[n];
        for (int i = 0; i < n; i++) {
            b[i] = (byte)rand('a', 'z');
        }
        return new String(b);
    }

    private static int rand(int lo, int hi) {
        java.util.Random rn = new java.util.Random();
        int n = hi - lo + 1;
        int i = rn.nextInt() % n;
        if (i < 0)
                i = -i;
        return lo + i;
    }

    public static String randomString() {
        return randomString(8, 15);
    }

    @SuppressWarnings("deprecation")
    public DefaultHttpClient getAllowAllHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new AllowAllSSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    public class AllowAllSSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public AllowAllSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

}