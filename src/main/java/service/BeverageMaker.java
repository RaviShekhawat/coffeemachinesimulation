package service;

import model.Beverage;
import model.Machine;
import model.PreparationStatus;
import org.json.JSONException;
import util.BeveragePreparationStatusChecker;
import util.JsonReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BeverageMaker {

    List<Beverage> beveragesToPrepare = new LinkedList<>();
    Map<String,Integer> availableItems = new HashMap<>();
    JsonReader jsonReader = new JsonReader();
    BeveragePreparationStatusChecker beveragePreparationStatusChecker = new BeveragePreparationStatusChecker();
    Map<String,PreparationStatus> preparationStatus = new HashMap<>();  //Will store preparation status for all beverages
    Map<String,String> preparationFailureCause = new HashMap<>();   //Will store preparation failure cause for all beverages if it fails
    int freeMachineSlots;
    String noOutletAvailableOutput = "As there are no outlets available no beverage can be prepared";

    public void initializeBeveragesAndTotalQuantity(String filePath) throws IOException, JSONException {
        beveragesToPrepare = jsonReader.beveragesToPrepare(filePath);
        availableItems = jsonReader.getAvailableIngrediantDetails();
    }

    public void prepareBeverages(String filePath) throws JSONException, IOException {

        initializeBeveragesAndTotalQuantity(filePath);
        freeMachineSlots = jsonReader.getMachine().getSlots();
        if(freeMachineSlots == 0)       //As only one beverage can be prepared on an outlet
        {
            return;
        }
        beveragesToPrepare.forEach(beverage -> {
            StringBuilder unfulfillingIngredient = new StringBuilder();
            PreparationStatus canPrepare = beveragePreparationStatusChecker.beveragePreparationStatusSetter(availableItems,
                    beverage.getIngredients(), unfulfillingIngredient);
            if(canPrepare == PreparationStatus.Insufficient || canPrepare == PreparationStatus.Unavailable)
            {
                preparationStatus.put(beverage.getName(), canPrepare);
                preparationFailureCause.put(beverage.getName(), unfulfillingIngredient.toString());
            }
            else
            {
                for(String ingredient : beverage.getIngredients().keySet())
                {
                    Integer currIngredientQuantity = beverage.getIngredients().get(ingredient);
                    Integer totalIngredientQuantity = availableItems.get(ingredient);
                    availableItems.put(ingredient, totalIngredientQuantity - currIngredientQuantity);
                }
                preparationStatus.put(beverage.getName(),PreparationStatus.Prepared);
            }
            freeMachineSlots -= 1;      //decrease the no. of available outlets as we prepare a beverage
            if(freeMachineSlots == 0)   //once all outlets are used once, refill the outlets with initial quantity and repeat the process for remaining beverages
            {
                freeMachineSlots = jsonReader.getMachine().getSlots();
                jsonReader.refillOutlets();
            }
        });
    }

    public String getPreparationStatus()
    {
        if(this.jsonReader.getMachine().getSlots() == 0)
            return noOutletAvailableOutput;
        StringBuilder overallPreparationStatus = new StringBuilder();
        for(String beverage : this.preparationStatus.keySet())
        {
            if(preparationStatus.get(beverage) == PreparationStatus.Unavailable) {
                String currentPreparationStatus = beverage + " cannot be prepared because " +
                        preparationFailureCause.get(beverage) + " is not available";
                System.out.println(currentPreparationStatus);
                overallPreparationStatus.append(currentPreparationStatus+"\n");
            }
            else if(preparationStatus.get(beverage) == PreparationStatus.Insufficient) {
                String currentPreparationStatus = beverage + " cannot be prepared because item " +
                        preparationFailureCause.get(beverage) + " is not sufficient";
                System.out.println(currentPreparationStatus);
                overallPreparationStatus.append(currentPreparationStatus+"\n");
            }
            else {
                String currentPreparationStatus = beverage + " is prepared";
                System.out.println(currentPreparationStatus);
                overallPreparationStatus.append(currentPreparationStatus+"\n");
            }
        }
        return overallPreparationStatus.toString();
    }
}
