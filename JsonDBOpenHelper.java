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
        // JsonDBOpenHelper constructor, in here we check if the json database exists
        // if it does not we create it using the onCreateDB method.
        // if it does exists we check the version if the version is newer then the old one
        // we use the onUpdateDB method.
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
        // used in the constructor.
        if (file.createNewFile()) {
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
        // call this method at the start of your methods to read the 
        // json file and create a jsonObject out of the database.
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
        // a method that takes in the jsonObject and writes it into the json file.
        CheckIfVersionSmaller(jsonObject);
        checkIfVersionExists(jsonObject);
        String userString = jsonObject.toString();
        FileWriter fileWriter = new FileWriter(this.file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(userString);
        bufferedWriter.close();
    }

    public void checkIfVersionExists(JSONObject jsonObject) throws JsonDBException {
        // checking that the _version exists in json database for version controll
        // is called every time we create the object of the database controller.
        if (!jsonObject.has("_Version")) {
            throw new JsonDBException("you should have a \"_version\" key in the jsonObject " +
                    "when using the JsonDBOpenHelper class.");
        }
    }

    public void CheckIfVersionSmaller(JSONObject jsonObject) throws JsonDBException, JSONException {
        // checking if the _version is smaller then the version of the database so there
        // wont be any problems with version control.
        if (this.dbVersion < jsonObject.getInt("_Version")) {
            throw new JsonDBException("your version can not be older then " +
                    "the current version.");
        }
    }
    
    // the two abstract methods are used for creating and updating the json database
    
    abstract void onCreateDB(JSONObject jsonObject) throws JSONException, IOException, JsonDBException;

    abstract void onUpdateDB(JSONObject jsonObject) throws JSONException, IOException, JsonDBException;

}
