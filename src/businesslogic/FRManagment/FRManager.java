package businesslogic.FRManagment;


import businesslogic.CatERing;
import businesslogic.SIn;
import businesslogic.UseCaseLogicException;
import businesslogic.event.ServiceInfo;
import businesslogic.menu.Menu;
import businesslogic.menu.MenuItem;
import businesslogic.user.User;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FRManager {

    private FR current_foglio;
    private Compito current_compito;
    private ArrayList<FRReceiver> eventReceivers;

    public FRManager(){
        eventReceivers = new ArrayList<>();
    }

    public FR getCurrent_foglio() {
        return current_foglio;
    }

    public Compito getCurrent_compito() {
        return current_compito;
    }

    public void setCurrent_foglio(FR f) {this.current_foglio=f;}

    public void setCurrent_compito(Compito c) {
        this.current_compito=c;
    }

    public FR addFoglio(ServiceInfo serviceInfo) throws UseCaseLogicException {
        if(CatERing.getInstance().getUserManager().getCurrentUser().isChef()) {
            String name = "Foglio Riepilogativo per il servizio "+serviceInfo.getName();
            FR f = new FR(name);
            setCurrent_foglio(f);
            ArrayList<String> items = Menu.loadMenuItemsById(serviceInfo.getApprovedMenu());
            notifyNewFoglio(f);
            System.out.println("Creato: "+f.getName()+"\n\nLista Compiti:");
            for(String item: items) {
                Compito c = new Compito(item, getRandomInt(100), getRandomInt(10), getRandomInt(200));
                addCompito(c);
                f.getListaCompiti().add(c);
            }
            return f;
        }
        else {
            System.out.println("Non sei uno chef, non puoi fare questa azione");
            throw new UseCaseLogicException();
        }
    }

    public void delFoglio() throws UseCaseLogicException {
        if(CatERing.getInstance().getUserManager().getCurrentUser().isChef() && CatERing.getInstance().getUserManager().getCurrentUser().getId()==current_foglio.getId_owner()) {
            notifyDelFoglio(current_foglio);
            System.out.println("Foglio eliminato.");
        }
        else {
            System.out.println("Non sei uno chef, il foglio non è tuo o non ci sono fogli da eliminare, non puoi fare questa azione");
            throw new UseCaseLogicException();
        }
    }

    public void sortCompiti() {
        if(CatERing.getInstance().getUserManager().getCurrentUser().isChef() && CatERing.getInstance().getUserManager().getCurrentUser().getId()==current_foglio.getId_owner()) {
            current_foglio.sortCompiti();
            for(Compito c: current_foglio.getListaCompiti()) {
                notifyDelCompito(c);
                notifyNewCompito(c);
            }
        }
    }

    public void addCompito(Compito c) throws UseCaseLogicException {
        if(CatERing.getInstance().getUserManager().getCurrentUser().isChef() && CatERing.getInstance().getUserManager().getCurrentUser().getId()==current_foglio.getId_owner()) {
            c.setId_fr(this.current_foglio.getId());
            notifyNewCompito(c);
            setCurrent_compito(c);
            System.out.println(c);
        }
        else {
            System.out.println("Non sei uno chef, non puoi fare questa azione");
            throw new UseCaseLogicException();
        }
    }

    public void delCompito() throws UseCaseLogicException {
        if(CatERing.getInstance().getUserManager().getCurrentUser().isChef() && CatERing.getInstance().getUserManager().getCurrentUser().getId()==current_foglio.getId_owner()) {
            notifyDelCompito(current_compito);
        }
        else {
            System.out.println("Non sei uno chef o non è un foglio di tua proprieta' o non sono presenti compiti nel foglio, non puoi fare questa azione");
            throw new UseCaseLogicException();
        }
    }

    public void setComplete(boolean b) {
        if (current_compito != null) {
            this.current_compito.setCompleted(b);
            notifyComplete(current_compito);
        }
    }

    public void setQtyProd(int qtyProd) {
        if (current_compito != null) {
            this.current_compito.setQtyProduced(qtyProd);
            notifyQtyProd(current_compito);
        }
    }

    public void setAssignedTurn(String turn) {
        if(current_compito != null) {
            this.current_compito.setAssignedTurn(turn);
            notifyTurn(current_compito);
        }
    }

    public void addEventReceiver(FRReceiver rec) {
        this.eventReceivers.add(rec);
    }

    private void notifyNewFoglio(FR f){
        for (FRReceiver er: this.eventReceivers){
            er.saveFR(f);
        }
    }

    private void notifyDelFoglio(FR f){
        for (FRReceiver er: this.eventReceivers){
            er.saveDelFR(f);
        }
    }

    private void notifyNewCompito(Compito c){
        for (FRReceiver er: this.eventReceivers){
            er.saveCompito(c);
        }
    }

    private void notifyDelCompito(Compito c){
        for (FRReceiver er: this.eventReceivers){
            er.saveDelCompito(c);
        }
    }

    private void notifyNewCook(Compito c, User cuoco) {
        for (FRReceiver er: this.eventReceivers){
            er.addCookToCompito(c, cuoco);
        }
    }

    private void notifyDelCook(Compito c, User u) {
        for (FRReceiver er: this.eventReceivers){
            er.delCookFromCompito(c, u);
        }
    }

    private void notifyComplete(Compito c){
        for (FRReceiver er: this.eventReceivers){
            er.setComplete(c);
        }
    }

    private void notifyQtyProd(Compito c){
        for (FRReceiver er: this.eventReceivers){
            er.setQtyProd(c);
        }
    }

    private void notifyTurn(Compito c) {
        for(FRReceiver er: this.eventReceivers){
            er.setTurn(c);
        }
    }

    public static ArrayList<FR> updateFR(){
        ArrayList<FR> cal = new ArrayList<>();
        String turnsQuery= "SELECT * FROM FR";
        PersistenceManager.executeQuery(turnsQuery, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                FR fr;
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int id_owner = rs.getInt("id_owner");
                fr = new FR(name, id_owner);
                fr.setId(id);
                cal.add(fr);
            }
        });
        return cal;
    }

    public FR selectFR() {
        ArrayList<FR> cal = updateFR();
        if(cal.size()==0) {
            System.out.println("Nessun foglio da visualizzare");
            return null;
        }
        int n = 0;
        for(FR t: cal) {
            System.out.println(n+" "+t.toString());
            n++;
        }
        System.out.println("Inserire id del foglio da selezionare");
        int y = SIn.readInt();
        while(y>n-1 || y<0) {
            System.out.println("Inserire id del foglio da selezionare");
            y = SIn.readInt();
        }
        System.out.println("hai selezionato "+cal.get(y));
        FR fr = cal.get(y);
        FR.fillListaCompiti(fr);
        return fr;
    }

    public void SelectFR(FR fr) throws UseCaseLogicException {
        if(fr!=null && CatERing.getInstance().getUserManager().getCurrentUser().isChef())
            setCurrent_foglio(fr);
        else throw new UseCaseLogicException();
    }

    public static ArrayList<Compito> updateCompiti(FR fr){
        fr.setListaCompiti(new ArrayList<>());
        String turnsQuery= "select compiti.* from compiti join fr on compiti.id_fr = fr.id where fr.id = "+fr.getId();
        PersistenceManager.executeQuery(turnsQuery, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                Compito c;
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int qty = rs.getInt("qty");
                int importance = rs.getInt("importance");
                int expTime = rs.getInt("expectedTime");
                String compl = rs.getString("complete");
                int qtyProd = rs.getInt("qtyProduced");
                int id_fr = rs.getInt("id_fr");
                c = new Compito(name, qty, importance, expTime);
                c.setId(id);
                c.setCompleted(compl.equals("true"));
                c.setQtyProduced(qtyProd);
                c.setId_fr(id_fr);
                fr.getListaCompiti().add(c);
            }
        });
        return fr.getListaCompiti();
    }

    public Compito selectCompito() throws UseCaseLogicException {
        SelectFR(selectFR());

        ArrayList<Compito> cal = updateCompiti(current_foglio);
        if(cal.size()==0) {
            System.out.println("Nessun compito da selezionare");
            return null;
        }
        int n = 0;
        for(Compito t: cal) {
            System.out.println(n+" "+t.toString());
            n++;
        }
        System.out.println("Inserire id del compito da selezionare");
        int y = SIn.readInt();
        while(y>n-1 || y<0) {
            System.out.println("Inserire id del compito da selezionare");
            y = SIn.readInt();
        }
        System.out.println("hai selezionato "+cal.get(y));
        Compito c = cal.get(y);
        Compito.fillListaCuochi(c);
        return c;
    }

    public void SelectCompito(Compito c) throws UseCaseLogicException {
        if(c!=null && CatERing.getInstance().getUserManager().getCurrentUser().isChef())
            setCurrent_compito(c);
        else throw new UseCaseLogicException();
    }

    public void addCuoco(User cuoco) throws UseCaseLogicException {
        ArrayList<User> lista = current_compito.getListaCuochi();
        if(cuoco.isCuoco() && !lista.contains(cuoco) && CatERing.getInstance().getUserManager().getCurrentUser().isChef()) {
            notifyNewCook(current_compito, cuoco);
            current_compito.getListaCuochi().add(cuoco);
            //System.out.println("Cuoco aggiunto al compito");
            return;
        }
        else if(lista.contains(cuoco)) System.out.println("Il cuoco e' gia' presente nella lista");
        else if(!cuoco.isCuoco()) System.out.println(cuoco.getUserName()+" non e' un cuoco");
        else System.out.println("Qualcosa e' andato storto");
        throw new UseCaseLogicException();
    }

    public void delCuoco(User cuoco) throws UseCaseLogicException {
        ArrayList<User> lista = current_compito.getListaCuochi();
        if(cuoco.isCuoco() && lista.contains(cuoco) && CatERing.getInstance().getUserManager().getCurrentUser().isChef()) {
            notifyDelCook(current_compito, cuoco);
            System.out.println("Cuoco rimosso dal compito");
            current_compito.getListaCuochi().remove(cuoco);
            return;
        }
        else if(!lista.contains(cuoco)) System.out.println("Il cuoco non e' presente nella lista");
        else if(!cuoco.isCuoco()) System.out.println(cuoco.getUserName()+" non e' un cuoco");
        else System.out.println("Qualcosa e' andato storto");
        throw new UseCaseLogicException();
    }

    public static int getRandomInt(int max) {
        return (int) Math.floor(Math.random() * max);
    }
}