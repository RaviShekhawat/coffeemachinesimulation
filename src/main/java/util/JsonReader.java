package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Beverage;
import model.Machine;
import model.PreparationStatus;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class JsonReader {

    JSONObject inputJSON;
    Machine machine = new Machine();

    public Map<String, Integer> getOriginalItemsQuantity() {
        return originalItemsQuantity;
    }

    public void setOriginalItemsQuantity(Map<String, Integer> originalItemsQuantity) {
        this.originalItemsQuantity = originalItemsQuantity;
    }

    Map<String,Integer> originalItemsQuantity = new HashMap<>();        //variable used to store initial available quantity

    public JSONObject getJsonObject() {
        return inputJSON;
    }

    public void setJsonObject(JSONObject inputJSON) {
        this.inputJSON = inputJSON;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public List<Beverage> beveragesToPrepare(String filePath) throws IOException, JSONException {

        List<Beverage> beveragesToPrepare = new LinkedList<>();
        String content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        inputJSON = new JSONObject(content);
        machine.setSlots(inputJSON.getJSONObject("machine").getJSONObject("outlets").optInt("count_n"));
        JSONObject inputBeverages = inputJSON.getJSONObject("machine").getJSONObject("beverages");

        for (Iterator it = inputBeverages.keys(); it.hasNext(); ) {
            String beverageName = it.next().toString();
            Beverage beverage = new Beverage();
            beverage.setName(beverageName);
            JSONObject currBeverageIngredientsJSON = inputBeverages.getJSONObject(beverageName);
            Map<String,Integer> currBeverageIngredients = new ObjectMapper().readValue(
                    currBeverageIngredientsJSON.toString(), HashMap.class);
            beverage.setIngredients(currBeverageIngredients);
            beveragesToPrepare.add(beverage);
        }
        return beveragesToPrepare;

    }

    public Map<String,Integer> getAvailableIngrediantDetails() throws JSONException, IOException {
        JSONObject availableItemsJSON = inputJSON.getJSONObject("machine").getJSONObject("total_items_quantity");   //compute total quantity available originally
        Map<String,Integer> availableItems = new ObjectMapper().readValue(availableItemsJSON.toString(), HashMap.class);
        originalItemsQuantity = availableItems;
        return availableItems;
    }

    public void refillOutlets()
    {
        this.setOriginalItemsQuantity(originalItemsQuantity);   //As soon as a round of preparation is over, this method would refill the outlets
    }

}
