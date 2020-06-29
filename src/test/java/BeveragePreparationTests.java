import org.json.JSONException;
import org.junit.Test;
import service.BeverageMaker;
import java.io.IOException;

public class BeveragePreparationTests {

    BeverageMaker beverageMaker = new BeverageMaker();
    String noOutletAvailableExpectedOutputFilePath = "src/main/resources/coffeemachinewithnooutlet.json";
    String prepareBeverageWithSomeNotAvailableOrInsufficientFilePath = "src/main/resources/coffeemachine.json";
    String prepareBeverageWithSomeNotAvailableOrInsufficient = "hot_coffee is prepared\n" +
            "green_tea cannot be prepared because green_mixture is not available\n" +
            "black_tea is prepared\n" +
            "hot_tea cannot be prepared because item sugar_syrup is not sufficient\n";
    String noOutletAvailableExpectedOutput = "As there are no outlets available no beverage can be prepared";
    String prepareBeverageWithAllItemsSufficientFilePath = "src/main/resources/coffeemachinewithallitemssufficient.json";
    String prepareBeverageWithAllItemsSufficientExpectedOutput = "hot_coffee is prepared\n" +
            "green_tea is prepared\n" +
            "black_tea is prepared\n" +
            "hot_tea is prepared\n";

    @Test
    public void prepareBeverageWithSomeNotAvailableOrInsufficient() throws IOException, JSONException {
        beverageMaker.initializeBeveragesAndTotalQuantity(prepareBeverageWithSomeNotAvailableOrInsufficientFilePath);
        String preparationStatusOfBeverages = "";
        try {
            beverageMaker.prepareBeverages(prepareBeverageWithSomeNotAvailableOrInsufficientFilePath);
            preparationStatusOfBeverages = beverageMaker.getPreparationStatus();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        assert preparationStatusOfBeverages.equals(prepareBeverageWithSomeNotAvailableOrInsufficient);
    }

    @Test
    public void prepareBeverageWithNoOutletAvailable() throws IOException, JSONException {
        beverageMaker.initializeBeveragesAndTotalQuantity(noOutletAvailableExpectedOutputFilePath);
        String preparationStatusOfBeverages = "";
        try {
            beverageMaker.prepareBeverages(noOutletAvailableExpectedOutputFilePath);
            preparationStatusOfBeverages = beverageMaker.getPreparationStatus();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        assert preparationStatusOfBeverages.equals(noOutletAvailableExpectedOutput);
    }

    @Test
    public void prepareBeverageWithAllBeverageSufficient() throws IOException, JSONException {
        beverageMaker.initializeBeveragesAndTotalQuantity(prepareBeverageWithAllItemsSufficientFilePath);
        String preparationStatusOfBeverages = "";
        try {
            beverageMaker.prepareBeverages(prepareBeverageWithAllItemsSufficientFilePath);
            preparationStatusOfBeverages = beverageMaker.getPreparationStatus();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        assert preparationStatusOfBeverages.equals(prepareBeverageWithAllItemsSufficientExpectedOutput);
    }


}
