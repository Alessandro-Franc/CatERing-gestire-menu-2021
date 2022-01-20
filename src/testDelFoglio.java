import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;

public class testDelFoglio {

    public static void main(String[] args) {
        try {
            CatERing.getInstance().getUserManager().login(3);
            CatERing.getInstance().getFrManager().SelectFR(CatERing.getInstance().getFrManager().selectFR());
            CatERing.getInstance().getFrManager().delFoglio();
        } catch (UseCaseLogicException e) {
            System.out.println("Errore nello use case");
        }
    }
}
