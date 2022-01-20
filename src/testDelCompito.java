import businesslogic.CatERing;
import businesslogic.FRManagment.FRManager;
import businesslogic.UseCaseLogicException;

public class testDelCompito {

    public static void main(String[] args) {
        try {
            CatERing.getInstance().getUserManager().login(3);
            CatERing.getInstance().getFrManager().SelectCompito(CatERing.getInstance().getFrManager().selectCompito());
            CatERing.getInstance().getFrManager().delCompito();
        } catch (UseCaseLogicException e) {
            System.out.println("Errore nello use case");
        }
    }
}
