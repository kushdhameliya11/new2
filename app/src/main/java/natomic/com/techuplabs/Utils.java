package natomic.com.techuplabs;



import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<model> readFromAsset(Context context) {
        List<model> modelList = new ArrayList<>();
        try {
            InputStream inputStream = context.getAssets().open("android_version.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json_string = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(json_string);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String version = jsonObject.getString("version");
                model model = new model(name, version);
                modelList.add(model);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return modelList;
    }
}


