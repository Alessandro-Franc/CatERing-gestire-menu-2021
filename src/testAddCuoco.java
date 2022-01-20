import businesslogic.CatERing;
import businesslogic.FRManagment.FRManager;
import businesslogic.SIn;
import businesslogic.UseCaseLogicException;
import businesslogic.user.User;

public class testAddCuoco {
        public static void main(String[] args) {
            try {
                CatERing.getInstance().getUserManager().login(3);
                CatERing.getInstance().getFrManager().SelectCompito(CatERing.getInstance().getFrManager().selectCompito());
                System.out.println("Cuochi possibili:");
                User.printListaCuochi();
                System.out.println("Cuochi gia' assegnati al compito:");
                System.out.println(CatERing.getInstance().getFrManager().getCurrent_compito().ListaCuochiToString());
                System.out.println("Inserire id cuoco da aggiungere al compito");
                int idC = SIn.readInt();
                User u = User.loadUserById(idC);
                CatERing.getInstance().getFrManager().addCuoco(u);
                System.out.println("Cuochi assegnati al compito:");
                System.out.println(CatERing.getInstance().getFrManager().getCurrent_compito().ListaCuochiToString());
            } catch (UseCaseLogicException e) {
                System.out.println("errore nello use case");
            }
        }

}
