package com.jatora.tfg_the_climb_within;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Utils {
    // constants for game files
    private static final String SAVE_FILE = "save.json";

    // TODO: CHANGE THIS DATA TO "DEFAULT_SAVE_DATA" AND CHANGE CONTENT TO **REAL** DEFAULT SAVE DATA
    private static final String DEFAULT_TEST_SAVE_DATA = "{\n" +
            "    \"player\": {\n" +
            "        \"name\": \"\",\n" +
            "        \"maxhp\": 100,\n" +
            "        \"hp\": 100,\n" +
            "        \"unlocked_cards\": [1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009,  6003],\n" +
            "        \"tower_coins\": 0,\n" +
            "        \"unlocked_towers\": [0, 1],\n" +
            "        \"emotion_coins\": {\n" +
            "            \"anger\": 100,\n" +
            "            \"disgust\": 110,\n" +
            "            \"fear\": 120,\n" +
            "            \"happiness\": 130,\n" +
            "            \"sadness\": 140,\n" +
            "            \"surprise\": 150\n" +
            "        },\n" +
            "        \"deck\": [],\n" +
            "        \"stats\": {\n" +
            "            \"played_time\": 0,\n" +
            "            \"cards_played\": 0,\n" +
            "            \"damage_dealt\": 0,\n" +
            "            \"damage_received\": 0\n" +
            "        }\n" +
            "    }\n" +
            "}";


    private static final String PROPERTIES_EN_FILE = "game_data/properties_en.json";
    private static final String PROPERTIES_ES_FILE = "game_data/properties_es.json";
    private static final String STORY_FILE = "game_data/story.json";
    private static final String TOWERS_FILE = "game_data/towers.json";
    private static final String ENEMIES_FILE = "game_data/enemies.json";
    private static final String DECKS_FILE = "game_data/decks.json";
    private static final String CARDS_FILE = "game_data/cards.json";

    // GSON object for the whole class (shouldn't have any problems it doesn't store data)
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static void changeActivity(Activity from, Class to, int fromAnim, int toAnim) {
        Intent intent = new Intent(from, to);

        from.startActivity(intent);
        from.overridePendingTransition(toAnim, fromAnim);
    }


    /**
     * Reads the given file from the assets folder.
     * @param context
     * @param path
     * @return A String with the given file's content
     */
    public static String readFile(Context context, String path) {
        final String TAG = "Utils-readFile()";
        Log.d(TAG, "Retrieving file content: "+path);

        // fetch file
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(context.getAssets().open(path)));
        } catch (IOException e) {
            Log.e(TAG, "Error while opening file: " + e);
        }
        StringBuilder fileContent = new StringBuilder();
        String line;

        try {
            while ((line = br.readLine()) != null) {
                line.split(";");
                fileContent.append(line);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error while reading file: " + e);
        }

        try {
            br.close();
        } catch (IOException e) {
            Log.e(TAG, "Error while closing BufferedReader: " + e);
        }

        return fileContent.toString();
    }

    /**
     * Get the properties in the specified language:
     * <ul>
     *     <li>0 for English</li>
     *     <li>1 for Spanish</li>
     * </ul>
     * @param lang
     * @return A JsonObject containing the properties info in the specified language
     */
    public static JsonObject getProperties(Context context, int lang) {
        final String TAG = "Utils-getProperties()";

        String file = (lang == 1) ? PROPERTIES_ES_FILE : PROPERTIES_EN_FILE;

        String fileData = Utils.readFile(context, file);

        return gson.fromJson(fileData, JsonObject.class);
    }


    public static ArrayList<Tower> getTowersData(Context context) {
        final String TAG = "Utils-getTowersData()";

        // get file's content as String
        String towersData = Utils.readFile(context, TOWERS_FILE);

        // parse tower data to JSON
        JsonObject towerJSON = gson.fromJson(towersData, JsonObject.class);
        // get towers array located in object with key "towers"
        JsonArray towerArray = towerJSON.getAsJsonArray("towers");
        // ArrayList for storing towers
        ArrayList<Tower> towers = new ArrayList<>();

        // get properties data
        JsonObject propertiesENJSON = getProperties(context, 0);
        // get object with tower properties from inside properties (en) JSON object, located with key "towers"
        JsonObject towerProperties = propertiesENJSON.getAsJsonObject("towers");

        // loop through all elements in the JSON array with towers's info
        for (JsonElement je : towerArray) {
            Log.d("A", je.toString());
            // parse JSON object to Tower Java object
            Tower tower = gson.fromJson(je.toString(), Tower.class);
            // replace initial name (towerN.name) with property stored in the properties data
            tower.setName(String.valueOf(towerProperties.get(tower.getName())));
//            tower.setImg(String.valueOf(towerProperties.get(tower.getImg())));

            Log.e(TAG, "tower img: "+tower.getImg());

            towers.add(tower);
        }

        return towers;
    }


    public static ArrayList<Enemy> getEnemiesData(Context context) {
        final String TAG = "Utils-getEnemiesData()";

        // get file's content as String
        String enemiesData = Utils.readFile(context, ENEMIES_FILE);

        // parse enemies data to JSON
        JsonObject enemiesJSON = gson.fromJson(enemiesData, JsonObject.class);

        // ArrayList for storing towers
        ArrayList<Enemy> enemies = new ArrayList<>();

        // get properties data
        JsonObject propertiesENJSON = getProperties(context, 0);
        // get object with enemies properties from inside properties (en) JSON object, located with key "enemies"
        JsonObject enemiesProperties = propertiesENJSON.getAsJsonObject("enemies");

        for (String key : enemiesJSON.keySet()) {
            Log.d(TAG, "checking enemy: "+key);

            Enemy enemy = gson.fromJson(enemiesJSON.get(key).getAsString(), Enemy.class);
            enemy.setName(String.valueOf(enemiesProperties.get(enemy.getName())));
            enemy.setImg(String.valueOf(enemiesProperties.get(enemy.getImg())));

            enemies.add(enemy);
        }

        return enemies;
    }


    /**
     * Checks if the save.json file exists in the internal storage, if not, it creates it.
     * @param context
     */
    public static void checkSaveFileExistence(Context context) {
        final String TAG = "Utils-checkSaveFileExistence";

        File file = new File(context.getFilesDir(), "save.json");

        if (!file.exists()) {
            Log.d(TAG, "creating and inserting default data into save.json in internal storage");
            try (FileOutputStream fos = context.openFileOutput(SAVE_FILE, Context.MODE_PRIVATE)) {
                fos.write(DEFAULT_TEST_SAVE_DATA.getBytes());
            } catch (IOException e) {
                Log.e(TAG, "Failed to create save.json");
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * FOR TESTING PURPOSES, RESETS THE SAVE FILE CONTENT TO DEFAULT
     * @param context
     */
    public static void resetSaveFileContent(Context context) {
        final String TAG = "Utils-resetSaveFileContent";

        File file = new File(context.getFilesDir(), "save.json");

        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Log.d(TAG, "resetting data into save.json in internal storage");
        try (FileOutputStream fos = context.openFileOutput(SAVE_FILE, Context.MODE_PRIVATE)) {
            fos.write(DEFAULT_TEST_SAVE_DATA.getBytes());
        } catch (IOException e) {
            Log.e(TAG, "Failed to reset save.json");
            throw new RuntimeException(e);
        }
    }


//    public static Player getPlayerData(Context context) {
//        final String TAG = "Utils-getPlayerData()";
//
//        StringBuilder sb = new StringBuilder();
//
//        try (FileInputStream fis = context.openFileInput(SAVE_FILE)) {
//            int ch;
//            while ((ch = fis.read()) != -1) {
//                sb.append((char) ch);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//
//        // parse enemies data to JSON
//        JsonObject playerJSON = gson.fromJson(sb.toString(), JsonObject.class);
//
//        Log.d(TAG, "Getting player's saved data: "+playerJSON);
//
//        return gson.fromJson(playerJSON.get("player"), Player.class);
//    }


//    public static void savePlayerData(Context context, Player playerObject) throws IOException {
//        final String TAG = "Utils-savePlayerData()";
//
//        Log.d(TAG, "parsing player object into string and adding to jsonobject.");
//        JsonObject jo = new JsonObject();
//        jo.add("player", gson.toJsonTree(playerObject));
//
//        Log.d(TAG, "Writing into file: "+SAVE_FILE);
//
//        try (FileOutputStream fos = context.openFileOutput(SAVE_FILE, Context.MODE_PRIVATE)) {
//            fos.write(jo.toString().getBytes());
//            Log.d(TAG, "Successfully wrote into file.");
//        } catch (IOException e) {
//            Log.e(TAG, "Failed to write into the file.");
//            e.printStackTrace();
//        }
//    }


    public static ArrayList<Card> getCardsData(Context context) {
        final String TAG = "Utils-getCardsData()";

        // get file's content as String
        String cardsData = Utils.readFile(context, CARDS_FILE);

        // parse enemies data to JSON
        JsonObject cardsJSON = gson.fromJson(cardsData, JsonObject.class);

        // ArrayList for storing towers
        ArrayList<Card> cards = new ArrayList<>();

        // get properties data
        JsonObject propertiesENJSON = getProperties(context, 0);
        // get object with enemies properties from inside properties (en) JSON object, located with key "enemies"
        JsonObject cardsProperties = propertiesENJSON.getAsJsonObject("cards");

        for (String key : cardsJSON.keySet()) {
            Log.d(TAG, "checking card (ID): "+key);
            Log.d(TAG, "checking card (DATA): "+cardsJSON.get(key));


            Card card = gson.fromJson(cardsJSON.get(key), Card.class);
            card.setName(String.valueOf(cardsProperties.get(card.getName()).getAsString()));
            card.setDescription(String.valueOf(cardsProperties.get(card.getDescription()).getAsString()));
            card.setIcon(String.valueOf(cardsProperties.get(card.getIcon()).getAsString()));

            cards.add(card);
        }

        return cards;
    }


    public static ArrayList<Deck> getDecksData(Context context) {
        final String TAG = "Utils-getDecksData()";

        // get file's content as String
        String decksData = Utils.readFile(context, DECKS_FILE);

        // parse enemies data to JSON
        JsonObject decksJSON = gson.fromJson(decksData, JsonObject.class);

        // ArrayList for storing towers
        ArrayList<Deck> decks = new ArrayList<>();

        for (String key : decksJSON.keySet()) {
            Log.d(TAG, "checking deck: "+key);

            Deck deck = gson.fromJson(decksJSON.get(key), Deck.class);
            deck.setName(key);

            decks.add(deck);
        }

        return decks;
    }


    public static ArrayList<DialogueSet> getStoryData(Context context) {
        final String TAG = "Utils-getStoryData()";

        // get file's content as String
        String storyData = Utils.readFile(context, STORY_FILE);

        // parse enemies data to JSON
        JsonObject storyJSON = gson.fromJson(storyData, JsonObject.class);

        // get properties data
        JsonObject propertiesENJSON = getProperties(context, 0);
        // get object with enemies properties from inside properties (en) JSON object, located with key "enemies"
        JsonObject storyProperties = propertiesENJSON.getAsJsonObject("story");

        ArrayList<DialogueSet> story = new ArrayList<>();

        // loop through story.json keys
        for (String key : storyJSON.keySet()) {
            // set with all the dialogues of each part of the story (intro, towers...)
            DialogueSet dialogueSet = new DialogueSet();
            // stores only dialogues
            ArrayList<String> dialogues = new ArrayList<>();
            // gets all dialogues of that particular key (=part of the story)
            JsonObject dialoguesJSON = storyProperties.get(key).getAsJsonObject();

            // loops through each dialogue of it
            for (String dialogueID : dialoguesJSON.keySet()) {
                // adds each dialogue to the arraylist
                dialogues.add(dialoguesJSON.get(dialogueID).getAsString());
            }

            // sets the DialogueSet info with the retrieved data
            dialogueSet.setId(key);
            dialogueSet.setDialogues(dialogues);

            // adds set to story list
            story.add(dialogueSet);
        }

        return story;
    }
}
