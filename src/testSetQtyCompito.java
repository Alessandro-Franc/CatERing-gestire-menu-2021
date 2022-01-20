import businesslogic.CatERing;
import businesslogic.SIn;
import businesslogic.UseCaseLogicException;

public class testSetQtyCompito {
    public static void main(String[] args) {
        try {
            CatERing.getInstance().getUserManager().login(3);
            CatERing.getInstance().getFrManager().SelectCompito(CatERing.getInstance().getFrManager().selectCompito());
            System.out.println("Quantità prodotta?");
            int qty = SIn.readInt();
            CatERing.getInstance().getFrManager().setQtyProd(qty);
            if(qty>=CatERing.getInstance().getFrManager().getCurrent_compito().getQty())
                CatERing.getInstance().getFrManager().setComplete(true);
            System.out.println(CatERing.getInstance().getFrManager().getCurrent_compito());
        } catch (UseCaseLogicException e) {
            System.out.println("Errore nello use case");
        }
    }
}
