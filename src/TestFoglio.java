import businesslogic.CatERing;
import businesslogic.FRManagment.Compito;
import businesslogic.FRManagment.FR;
import businesslogic.UseCaseLogicException;
import businesslogic.event.ServiceInfo;
import businesslogic.user.User;

public class TestFoglio {
    public static void main(String[] args) {
        try {
            CatERing.getInstance().getUserManager().login(3);
            User u = CatERing.getInstance().getUserManager().getCurrentUser();
            ServiceInfo s = new ServiceInfo("Coffee break mattino");
            FR fr = CatERing.getInstance().getFrManager().addFoglio(s);
            System.out.println("\nAggiorno compiti assegnando turno, cuochi e quantita' prodotte e ordino per importanza\n");
            CatERing.getInstance().getFrManager().sortCompiti();
            for(Compito c: fr.getListaCompiti()) {
                User u2 = User.loadUserById(6);
                User u3 = User.loadUserById(7);
                CatERing.getInstance().getFrManager().SelectCompito(c);
                CatERing.getInstance().getFrManager().setAssignedTurn("27-07-2022; 12:00/18:00");
                CatERing.getInstance().getFrManager().addCuoco(u2);
                CatERing.getInstance().getFrManager().addCuoco(u3);
                CatERing.getInstance().getFrManager().setQtyProd(30);
                CatERing.getInstance().getFrManager().setComplete(c.getQty()<=c.getQtyProduced());
                System.out.println(c);
            }
        } catch (UseCaseLogicException e) {
            System.out.println("Errore nello user case");
        }
    }
}
