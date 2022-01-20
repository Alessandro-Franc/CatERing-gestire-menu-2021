package persistence;

import businesslogic.FRManagment.Compito;
import businesslogic.FRManagment.FR;
import businesslogic.FRManagment.FRReceiver;
import businesslogic.user.User;

public class FRPersistence implements FRReceiver {
    @Override
    public void saveFR(FR f) {
        FR.saveNewFoglio(f);
    }

    @Override
    public void saveDelFR(FR f) {
        FR.saveDelFoglio(f);
    }

    @Override
    public void updateFR(FR f) {FR.fillListaCompiti(f);}

    @Override
    public void saveCompito(Compito c) {
        Compito.saveNewCompito(c);
    }

    @Override
    public void saveDelCompito(Compito c) {
        Compito.saveDelCompito(c);
    }

    @Override
    public void setQtyProd(Compito c) {
        Compito.saveQtyProdTurn(c);
    }

    @Override
    public void setComplete(Compito c) {
        Compito.saveCompleted(c);
    }

    @Override
    public void addCookToCompito(Compito c, User u) {
        Compito.insertCook(c, u);
    }

    @Override
    public void delCookFromCompito(Compito c, User u) {
        Compito.delCook(c, u);
    }

    @Override
    public void setTurn(Compito c) {
        Compito.saveTurn(c);
    }
}
