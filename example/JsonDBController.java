import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class JsonDBController extends JsonDBOpenHelper {

    public GameDB(Context dbContext) {
        super("gamedb", dbContext, 1);
    }

    @Override
    void onCreateDB(JSONObject jsonObject) throws JSONException, IOException, JsonDBException {
        JSONObject user = new JSONObject();
        JSONArray shop = new JSONArray();
        JSONObject item1 = new JSONObject();
        JSONObject item2 = new JSONObject();
        JSONObject item3 = new JSONObject();


        user.put("name", "noname");
        user.put("sound", 1);
        user.put("score", 0);
        user.put("userPower", 1);

        item1.put("power", 1);
        item1.put("price", 10);
        item1.put("pricePR", 5);
        item1.put("amount", 0);

        item2.put("power", 10);
        item2.put("price", 500);
        item2.put("pricePR", 5);
        item2.put("amount", 0);

        item3.put("power", 100);
        item3.put("price", 100000);
        item3.put("pricePR", 5);
        item3.put("amount", 0);

        shop.put(item1);
        shop.put(item2);
        shop.put(item3);

        jsonObject.put("user", user);
        jsonObject.put("shop", shop);

        writeJson(jsonObject);
    }

    @Override
    void onUpdateDB(JSONObject jsonObject) throws JSONException, IOException, JsonDBException {
    }

    public boolean checkSound() throws IOException, JSONException {
        JSONObject jsonObject = readJson();
        return jsonObject.getJSONObject("user").getInt("sound") == 1;
    }

    public void addOneToscore() {
        JSONObject jsonObject = readJson();
        int power = jsonObject.getJSONObject("user").getInt("score");
        power++;
        jsonObject.getJSONObject("user").put("score", power);
        writeJson(jsonObject);
    }
}
