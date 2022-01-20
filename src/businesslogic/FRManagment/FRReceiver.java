package businesslogic.FRManagment;

import businesslogic.user.User;

public interface FRReceiver {
    public void saveFR(FR f);

    public void saveDelFR(FR f);

    public void updateFR(FR f);

    public void saveCompito(Compito c);

    void saveDelCompito(Compito c);

    void setQtyProd(Compito c);

    void setComplete(Compito c);

    void addCookToCompito(Compito c, User u);

    public void delCookFromCompito(Compito c, User u);

    void setTurn(Compito c);
}
