import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public abstract class JsonDBOpenHelper {

    private final String dbName;
    private final Context dbContext;
    private final int dbVersion;
    private final File file;
    private JSONObject jsonObject;

    protected JsonDBOpenHelper(String dbName, Context dbContext, int dbVersion) {
        this.dbName = dbName + ".json";
        this.dbContext = dbContext;
        this.dbVersion = dbVersion;
        this.file = new File(this.dbContext.getFilesDir(),this.dbName);

        try {
            buildJsonDataBase();
        } catch (IOException | JSONException | JsonDBException e) {
            e.printStackTrace();
        }
    }

    public void buildJsonDataBase() throws IOException, JsonDBException, JSONException {
        if (this.file.createNewFile()) {
            jsonObject = new JSONObject();
            jsonObject.put("_Version", this.dbVersion);
            onCreateDB(jsonObject);
        } else {
            jsonObject = readJson();
            CheckIfVersionSmaller(jsonObject);
            checkIfVersionExists(jsonObject);
            if (this.dbVersion > jsonObject.getInt("_Version")) {
                onUpdateDB(jsonObject);
                jsonObject = readJson();
                jsonObject.put("_Version", this.dbVersion);
                writeJson(jsonObject);
            }
        }
    }

    public JSONObject readJson() throws IOException, JSONException {
        FileReader fileReader = new FileReader(this.file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null){
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        String response = stringBuilder.toString();
        return new JSONObject(response);
    }

    public void writeJson(JSONObject jsonObject) throws IOException, JsonDBException, JSONException {
        CheckIfVersionSmaller(jsonObject);
        checkIfVersionExists(jsonObject);
        String userString = jsonObject.toString();
        FileWriter fileWriter = new FileWriter(this.file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(userString);
        bufferedWriter.close();
    }

    public void checkIfVersionExists(JSONObject jsonObject) throws JsonDBException {
        if (!jsonObject.has("_Version")) {
            throw new JsonDBException("you should have a \"_version\" key in the jsonObject " +
                    "when using the JsonDBOpenHelper class.");
        }
    }

    public void CheckIfVersionSmaller(JSONObject jsonObject) throws JsonDBException, JSONException {
        if (this.dbVersion < jsonObject.getInt("_Version")) {
            throw new JsonDBException("your version can not be older then " +
                    "the current version.");
        }
    }

    abstract void onCreateDB(JSONObject jsonObject) throws JSONException, IOException, JsonDBException;

    abstract void onUpdateDB(JSONObject jsonObject) throws JSONException, IOException, JsonDBException;

}
