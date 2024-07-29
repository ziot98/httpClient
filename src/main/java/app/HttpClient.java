package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.bind.DatatypeConverter;

import org.json.JSONException;
import org.json.JSONObject;

import app.ssl.MyHandshakeCompletedListener;
import app.ssl.SecureSSLSocketFactory;

public class HttpClient {
	
	private static boolean useBase64 = false;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
        if (args.length < 1) {
        	toBase64SystemOut("Usage: java JSON Data, userBase64(option)");
            System.exit(1);
        }

        String jsonString = args[0];
        
        if (args.length == 2) {
        	useBase64 = true;
        }
        
        JSONObject jsonObject = new JSONObject(jsonString);
        
        //toBase64SystemOut("* Reqeust Data : ");
        //toBase64SystemOut(jsonObject.toString());

        String apiUrl = jsonObject.getString("URL");
        String body = jsonObject.getString("BODY");
        String method = jsonObject.getString("METHOD");
        JSONObject header = (JSONObject) jsonObject.get("HEADER");
        
        OutputStream os = null;
        BufferedReader br = null;
        InputStreamReader isr = null;

        try {
            // URL 설정
            URL url = new URL(apiUrl);

            // 연결 설정
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (apiUrl.startsWith("https")) {
            	HttpsURLConnection sslConnection = (HttpsURLConnection) connection;
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, null, null);
                
                sslConnection.setSSLSocketFactory(new SecureSSLSocketFactory(sslContext.getSocketFactory(), new MyHandshakeCompletedListener()));
            }
            
            connection.setRequestMethod(method);
            connection.setDoOutput(true);

            // Header 설정
            Iterator<String> keys = header.keys();
            while(keys.hasNext()) {
            	String key = keys.next();
            	String value = header.getString(key);
            	connection.setRequestProperty(key, value);
            }
            
        	os = connection.getOutputStream();
        	byte[] input = body.getBytes("utf-8");
            os.write(input, 0, input.length);

            // 응답 받기
            isr = new InputStreamReader(connection.getInputStream(), "utf-8");
            br = new BufferedReader(isr);
            
            String responseLine;
            StringBuilder response = new StringBuilder();
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            
            toBase64SystemOut("* Response Code : " + connection.getResponseCode());
            
            int status = connection.getResponseCode();

            if (status == HttpURLConnection.HTTP_OK) {
                toBase64SystemOut("* Response Header : ");
                
                for (Map.Entry<String, List<String>> entries : connection.getHeaderFields().entrySet()) {    
                    String values = "";
                    for (String value : entries.getValue()) {
                        values += value + ",";
                    }
                    if (entries.getKey() == null) {
                    	toBase64SystemOut(values);
                    } else {
                    	toBase64SystemOut(entries.getKey() + " : " + values);
                    }
                }
            }
            
            toBase64SystemOut("* Response Body :");
            toBase64SystemOut(response.toString());


            // 연결 종료
            connection.disconnect();
            
        } catch (Exception e) {
            toBase64SystemOut("* HTTPClient Error : ");
            printErrorStackTrace(e);
        } finally {
        	
        	if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					printErrorStackTrace(e);
				}
        	}
        	
        	if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					printErrorStackTrace(e);
				}
        	}
        	
        	if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					printErrorStackTrace(e);
				}
        	}

        }
    }
	
	public static void toBase64SystemOut(String s) {
		if (useBase64) {
			try {
				String encodedStr = DatatypeConverter.printBase64Binary(s.getBytes("UTF-8"));
				System.out.println(encodedStr);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println(s);
		}
	}
	
	public static void printErrorStackTrace(Exception e) {
    	StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		String errorString = errors.toString();
		toBase64SystemOut(errorString);
	}

}
