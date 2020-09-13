import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import org.json.*;


public class Väderdata_XML {
    public static void main(String[] args) throws Exception {
        String fil = "YR_Väderprognos.csv";
        PrintWriter outputStream = new PrintWriter(fil);
        URL url = new URL("https://www.yr.no/place/Sweden/Uppsala/Uppsala/forecast_hour_by_hour.xml");
        InputStream datastream = url.openStream();
        JSONObject myJSON = XML.toJSONObject(new InputStreamReader(datastream));
        JSONObject weatherdata = (JSONObject) myJSON.get("weatherdata");
        String output = weatherdata.toString();
        String[] varjeTimme = output.split("from");

        char temp;
        for (int i = 1; i < 26; i++) {
            int antalkolon = 0;
            String text1 = varjeTimme[i];
            for (int j = 0; j < text1.length(); j++) {
                temp = text1.charAt(j);
                if (temp == ':') antalkolon++;
            }

            String[] delning = varjeTimme[i].split(":");
            int n = 23;
            int t = 26;
            String tecken = "}";
            if (antalkolon > 27) {
                n += 1;
                t += 2;
                tecken = ",";
            }

            String luftryck_String = delning[13].substring(0, delning[13].indexOf("}"));
            String vindhastighet_String = delning[15].substring(0, delning[15].indexOf(","));
            String nederbörd_String = delning[n].substring(0, delning[n].indexOf(tecken));
            String temperatur_String = delning[t].substring(0, delning[t].indexOf("}"));

            outputStream.print(varjeTimme[i].substring(14, 19) + "\t");
            outputStream.print(luftryck_String.replace(".", ",") + "\thPa\t");
            outputStream.print(vindhastighet_String.replace(".", ",") + "\tm/s\t");
            outputStream.print(nederbörd_String.replace(".", ",") + "\tmm/h\t");
            outputStream.print(temperatur_String + "\tC\t");
            outputStream.println();
            outputStream.flush();
        }
    }
}
