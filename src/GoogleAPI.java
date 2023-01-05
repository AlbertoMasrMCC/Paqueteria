import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

//OBTENER LAS COORDENADAS DE UNA DIRECCION INTRODUCIDA POR EL USUARIO
public class GoogleAPI {

    public String[] obtenerCoordenadasReales(String direccion) {

        double latitud = 0;
        double longitud = 0;

        try {

            // Tu clave de API de Google Maps Geocoding
            String apiKey = "AIzaSyBOOexx1O9lqtZ4henM0k4sZ2D7Spl0HyE";

            // Se crea la solicitud a la API de geocodificación
            URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + URLEncoder.encode(direccion, "UTF-8") + "&key=" + apiKey);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Se obtiene la respuesta a la solicitud
            Scanner scanner = new Scanner(new InputStreamReader(connection.getInputStream()));
            String response = scanner.useDelimiter("\\A").next();

            // Se procesa la respuesta y se obtienen las coordenadas
            JSONObject json = new JSONObject(response);
            JSONArray results = json.getJSONArray("results");
            latitud = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            longitud = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");


        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (JSONException e) {

            e.printStackTrace();

        }

        //regresar las coordenadas lat y lng en un string
        return new String[]{latitud+"",longitud+""};

    }

}
