package util;

import model.PreparationStatus;
import java.util.Map;

public class BeveragePreparationStatusChecker {

    public PreparationStatus beveragePreparationStatusSetter(Map<String,Integer> totalQuantity,
                                                        Map<String, Integer> beverageQuantity,
                                                        StringBuilder unfulfillingIngredient)
    {
        for(String key : beverageQuantity.keySet())
        {
            if(!totalQuantity.containsKey(key))
            {
                unfulfillingIngredient.append(key);
                return PreparationStatus.Unavailable;
            }
            else if(totalQuantity.get(key) < beverageQuantity.get(key)) {
                unfulfillingIngredient.append(key);
                return PreparationStatus.Insufficient;
            }
        }
        return PreparationStatus.Prepared;
    }
}
