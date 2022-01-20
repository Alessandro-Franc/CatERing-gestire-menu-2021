import businesslogic.CatERing;
import businesslogic.FRManagment.Compito;
import businesslogic.FRManagment.FRManager;
import businesslogic.SIn;
import businesslogic.UseCaseLogicException;

public class testAddCompito {
    public static void main(String[] args) {
        try {
            CatERing.getInstance().getUserManager().login(3);
            Compito c;
            System.out.println("Nome compito?");
            String nameC = SIn.readLine();
            System.out.println("Creare con dati fittizzi? (y, n)");
            char dati2 = SIn.readChar();
            if (dati2 == 'y') {
                c = new Compito(nameC, 5, 2, 25);
            } else {
                System.out.println("Quantita' da produrre?");
                int qty = SIn.readInt();
                System.out.println("Importanza?");
                int imp = SIn.readInt();
                System.out.println("Tempo stimato?");
                int eTime = SIn.readInt();
                c = new Compito(nameC, qty, imp, eTime);
            }
            System.out.println("Selezionare foglio a cui aggiungere il compito appena creato");
            CatERing.getInstance().getFrManager().SelectFR(CatERing.getInstance().getFrManager().selectFR());
            CatERing.getInstance().getFrManager().addCompito(c);
        }catch (UseCaseLogicException e) {System.out.println("Errore nello use case");}
    }
}
