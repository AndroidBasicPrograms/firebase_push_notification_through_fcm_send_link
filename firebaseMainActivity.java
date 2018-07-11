package com.example.appsb.firebase_demo.extra;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.appsb.firebase_demo.R;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

public class firebaseMainActivity extends AppCompatActivity {
    private static final String AUTH_KEY = "key=AIzaSyCS3pzVRa-Bx6Ygmm-mKnInY5yo-cpDbxQ";
    public static boolean isAppRunning;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_main);
        tv=findViewById(R.id.dispToken);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String tmp = "";
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                tmp += key + ": " + value + "\n\n";
            }
            tv.setText(tmp);
        }

    }

    public void showToken(View view) {
        tv.setText(FirebaseInstanceId.getInstance().getToken());
        Log.i("token", FirebaseInstanceId.getInstance().getToken());
    }

    public void sendToken(View view) {
        sendWithOtherThread("token");
    }

    private void sendWithOtherThread(final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //pushNotificationOld(type);
                pushNotification(type);
            }
        }).start();
    }

    private void pushNotification(String type) {

        JSONObject jPayload=new JSONObject();
        JSONObject jData=new JSONObject();


        try {
            jData.put("title", "FireBaseDemo");
            jData.put("body", "Welome");
            jData.put("picture", "http://nuxvomica.xyz/foodies/uploads/restaurant/resto_1529316485.jpg");

            jPayload.put("to", FirebaseInstanceId.getInstance().getToken().toString());
            jPayload.put("priority", "high");
            jPayload.put("data", jData);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", AUTH_KEY.toString());
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());


            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    tv.setText(resp);
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private String convertStreamToString(InputStream inputStream) {
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }

//    private void pushNotificationOld(String type) {
//        JSONObject jPayload = new JSONObject();
//        JSONObject jNotification = new JSONObject();
//        JSONObject jData = new JSONObject();
//        try
//        {
//            jNotification.put("title", "Fire Base Demo");
//            jNotification.put("body", "Firebase Cloud Messaging (App)");
//            jNotification.put("sound", "default");
//            jNotification.put("badge", "1");
//            jNotification.put("click_action", "OPEN_ACTIVITY_1");
//            jNotification.put("icon", "ic_launcher_background");
//
//            jData.put("picture", "http://nuxvomica.xyz/foodies/uploads/restaurant/resto_1529316485.jpg");
//
//            switch(type) {
//                case "token":
//                    JSONArray ja = new JSONArray();
//                    ja.put("fIXmrQW2QKc:APA91bEu54ej4ESa3U7QSfwoaKlbbIICYHeD6BD6Fbpy5qURc2cQNFk73O9KtaOoaNEoMFq6OkamXfYIZibGPsI-5nFtVqAPUuGta7lgaONqKmvO_d_xmzdalmmE_8cb8FAORBhMWpvX6yGu-uXJpdlKluE17QRzpQ");
//                    ja.put(FirebaseInstanceId.getInstance().getToken());
//                    ja.put("fcNsQD5AHFM:APA91bFOICWCjMA8ERUaQ2eoqdzdYZLtCN-Z-LH4YCPCRTcexmPmzBTc04mKnn8EU00-Lg8JPy_T4siYql_05O3TPxFgEh_5r5g23ZHuTDG0UGUW6oEcMLKWe5t-jfpFAtejhTZyIzwsETZBICUa9YTa388X9EDsFA");
//                    jPayload.put("registration_ids", ja);
//                    break;
//
//                default:
//                    jPayload.put("to", FirebaseInstanceId.getInstance().getToken().toString());
//            }
//
//            jPayload.put("priority", "high");
//            jPayload.put("notification", jNotification);
//            jPayload.put("data", jData);
//
//            URL url = new URL("https://fcm.googleapis.com/fcm/send");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Authorization", AUTH_KEY.toString());
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setDoOutput(true);
//
//
//            // Send FCM message content.
//            OutputStream outputStream = conn.getOutputStream();
//            outputStream.write(jPayload.toString().getBytes());
//
//            // Read FCM response.
//            InputStream inputStream = conn.getInputStream();
//            final String resp = convertStreamToString(inputStream);
//
//            Handler h = new Handler(Looper.getMainLooper());
//            h.post(new Runnable() {
//                @Override
//                public void run() {
//                    tv.setText(resp);
//                }
//            });
//
//
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
}
