package com.klemstinegroup.wub3;

import com.echonest.api.v4.TrackAnalysis;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.TrackRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;
import com.wrapper.spotify.models.Track;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Paul on 2/23/2017.
 */
public class SpotifyUtils {

    private static Api api;
    private static String token;

    public static void main(String[] args) {
        new SpotifyUtils();
    }

    public static void setAccessToken() {
        System.out.println("Logging into spotify");
        api = Api.builder()
                .clientId(Credentials.clientId)
                .clientSecret(Credentials.clientSecret)
                .redirectURI("http://127.0.0.1:8002")
                .build();
        final ClientCredentialsGrantRequest request = api.clientCredentialsGrant().build();
        try {
            ClientCredentials clientCredentials = request.get();
            token=clientCredentials.getAccessToken();
            api.setAccessToken(token);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WebApiException e) {
            e.printStackTrace();
        }
    }
    protected static void downloadProcess(String s) {

        System.out.println(s);

    }



        public static HttpsURLConnection getConnection( URL url) throws KeyManagementException, NoSuchAlgorithmException, IOException{
            SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(null, new TrustManager[] { new InvalidCertificateTrustManager() }, null);
            SSLContext.setDefault(ctx);

//            String authEncoded = Base64.encodeBytes(authStr.getBytes());

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
//            connection.setRequestProperty("Authorization", "Basic " + authEncoded);

                connection.setHostnameVerifier(new InvalidCertificateHostVerifier());

            return connection;
    }

    public static JSONObject getDownloadList(String q) {
        String urlString = "https://datmusic.xyz/search?q=" + q.toString();
        System.out.println(urlString);
        URL url=null;
        try {
            url=new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            InputStream is =getConnection(url).getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            String s = "";
            while ((line = br.readLine()) != null) {
                s += line;
            }
            JSONParser parser = new JSONParser();
            return (JSONObject) parser.parse(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void downloadFile(String id) {
        Track track = getTrack(id);
        try {
            String artist = URLEncoder.encode(track.getArtists().get(0).getName(), "UTF-8");

            String title = URLEncoder.encode(track.getName(), "UTF-8");
            System.out.println(artist + "\t" + title);
            String s = "https://spoti.herokuapp.com/download?artist=" + artist + "&title=" + title;
            System.out.println(s);

            URL u = new URL(s);
            ReadableByteChannel rbc = Channels.newChannel(u.openStream());
            FileOutputStream fos = new FileOutputStream(id + ".mp3");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            System.out.println("downloaded!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Track getTrack(String id) {
        if (token == null) setAccessToken();
        final TrackRequest request = api.getTrack(id).build();

        try {
            final Track track = request.get();
            return track;
        } catch (WebApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static TrackAnalysis getAnalysis(String id) throws IOException, ParseException {
        if (token == null) setAccessToken();
        String stringUrl = "https://api.spotify.com/v1/audio-analysis/" + id;
        URL url = new URL(stringUrl);
        URLConnection uc = url.openConnection();
        uc.setRequestProperty("Authorization", "Bearer " + token);

        BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        String out = "";
        String line = "";
        while ((line = br.readLine()) != null) {
            out += line;
        }
        JSONParser parser = new JSONParser();
        JSONObject jso = (JSONObject) parser.parse(out);
        TrackAnalysis ta = new TrackAnalysis(jso);
        return ta;

    }

}

