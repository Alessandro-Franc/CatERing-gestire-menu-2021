import businesslogic.CatERing;
import businesslogic.FRManagment.FRManager;
import businesslogic.SIn;
import businesslogic.UseCaseLogicException;
import businesslogic.user.User;

public class testDelCuoco {

    public static void main(String[]args) {
        try {
            CatERing.getInstance().getUserManager().login(3);
            CatERing.getInstance().getFrManager().SelectCompito(CatERing.getInstance().getFrManager().selectCompito());
            System.out.println("Cuochi gia' assegnati al compito:");
            System.out.println(CatERing.getInstance().getFrManager().getCurrent_compito().ListaCuochiToString());
            System.out.println("Inserire id cuoco da rimuovere dal compito");
            int idC2 = SIn.readInt();
            User u2 = User.loadUserById(idC2);
            CatERing.getInstance().getFrManager().delCuoco(u2);
            System.out.println("Cuochi assegnati al compito:");
            System.out.println(CatERing.getInstance().getFrManager().getCurrent_compito().ListaCuochiToString());
        }catch (UseCaseLogicException e) {System.out.println("Errore nello use case");
        }
    }
}
