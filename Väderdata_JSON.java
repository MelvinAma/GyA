import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;

public class Väderdata_JSON {
    public static class Väderdata_JSON_public {
        public static void main(String[] args) throws Exception {
            String fil = "SMHI_väderprognos.csv";
            PrintWriter outputStream = new PrintWriter(fil);
            URL url = new URL("https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/16.158/lat/58.5812/data.json");
            InputStream datastream = url.openStream();
            JSONTokener reader = new JSONTokener(datastream);
            JSONObject myJSON = (JSONObject) reader.nextValue();
            JSONArray timeSeries = (JSONArray) myJSON.get("timeSeries");
            for (Object aJSONobject : timeSeries) {
                JSONObject weatherData = (JSONObject) aJSONobject;
                if (weatherData.get("validTime").toString().substring(0, 10).equalsIgnoreCase("2020-03-26")) {
                    outputStream.printf("%s: ", weatherData.get("validTime").toString().substring(0, 10));
                    outputStream.printf("%s\t", weatherData.get("validTime").toString().substring(11, 19));
                    JSONArray parameters = (JSONArray) weatherData.get("parameters");

                    // Grader i Celsius
                    for (Object anOtherJSONobject : parameters) {
                        JSONObject measure = (JSONObject) anOtherJSONobject;
                        if (measure.get("unit").toString().equalsIgnoreCase("Cel")) {
                            outputStream.print("Celsius: \t");
                            outputStream.printf("%s\t", measure.get("values").toString().substring(1,
                                    measure.get("values").toString().indexOf("]")).replace(".", ","));
                            outputStream.flush();
                        }
                    }

                    // Lufttryck i hPa
                    for (Object anOtherJSONobject : parameters) {
                        JSONObject measure = (JSONObject) anOtherJSONobject;
                        if (measure.get("unit").toString().equalsIgnoreCase("hPa")) {
                            outputStream.print("hPa: \t");
                            outputStream.printf("%s\t", measure.get("values").toString().substring(1, measure.get("values").toString().indexOf("]")).replace(".", ","));
                        }
                    }

                    // Nederbörd (medelvärde) i mm/h. (Viktigt: kg/m2/h = mm/h)
                    for (Object anOtherJSONobject : parameters) {
                        JSONObject measure = (JSONObject) anOtherJSONobject;
                        if (measure.get("name").toString().equalsIgnoreCase("pmean")) {
                            outputStream.printf("mm/h: \t");
                            outputStream.printf("%s\t", measure.get("values").toString().substring(1, measure.get("values").toString().indexOf("]")).replace(".", ","));
                            outputStream.flush();
                        }
                    }

                    // Vindhastighet i m/s.
                    for (Object anOtherJSONobject : parameters) {
                        JSONObject measure = (JSONObject) anOtherJSONobject;
                        if (measure.get("name").toString().equalsIgnoreCase("ws")) {
                            outputStream.printf("%s\t", measure.get("values").toString().substring(1, measure.get("values").toString().indexOf("]")).replace(".", ","));
                            outputStream.flush();
                        }
                    }
                }
                outputStream.println("");
            }

        }
    }
}
