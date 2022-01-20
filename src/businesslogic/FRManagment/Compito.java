package businesslogic.FRManagment;

import businesslogic.user.User;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Compito{
    private int id;
    private String name;
    private int qty;
    private int importance;
    private int expectedTime;
    private boolean completed;
    private int qtyProduced;
    private int id_fr;
    private ArrayList<User> listaCuochi;
    private String assignedTurn;

    public Compito(String name, int qty, int importance, int expectedTime) {
        this.name = name;
        this.qty = qty;
        this.importance = importance;
        this.expectedTime = expectedTime;
        this.completed = false;
        this.qtyProduced = 0;
        this.listaCuochi = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQtyProduced() {
        return qtyProduced;
    }

    public void setQtyProduced(int qtyProduced) {
        this.qtyProduced = qtyProduced;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String isCompletedtoString() {
        if(this.isCompleted()) return "true";
        else return "false";
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getId_fr() {
        return id_fr;
    }

    public void setId_fr(int id_fr) {
        this.id_fr = id_fr;
    }

    public static void saveNewCompito(Compito c){
        String prenInsert = "INSERT INTO catering.compiti (name, qty, importance, expectedTime, complete, id_fr, qtyProduced) VALUES (?, ?, ?, ?, ?, ?, ?);";
        int[] result = PersistenceManager.executeBatchUpdate(prenInsert, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setString(1, c.getName());
                ps.setInt(2, c.getQty());
                ps.setInt(3, c.getImportance());
                ps.setInt(4, c.getExpectedTime());
                ps.setString(5, c.isCompletedtoString());
                ps.setInt(6, c.getId_fr());
                ps.setInt(7, c.getQtyProduced());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                if (count == 0) {
                    c.setId(rs.getInt(1));
                }
            }
        });
    }

    public static void insertCook(Compito c, User u){
        String prenInsert = "INSERT INTO catering.compito_cuoco (id_compito, id_cuoco) VALUES (?, ?);";
        int[] result = PersistenceManager.executeBatchUpdate(prenInsert, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, c.getId());
                ps.setInt(2, u.getId());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                if (count == 0) {
                    //c.setId(rs.getInt(1));
                }
            }
        });
    }

    public static void delCook(Compito c, User u){
        String updateDelete = "delete from catering.compito_cuoco where id_compito="+c.getId()+" && id_cuoco="+u.getId()+";";
        PersistenceManager.executeUpdate(updateDelete);
    }

    public static void fillListaCuochi(Compito c) {
        String workersQuery = "select users.id from compiti join compito_cuoco on compiti.id=compito_cuoco.id_compito join users on compito_cuoco.id_cuoco=users.id where compiti.id="+c.getId()+";";
        PersistenceManager.executeQuery(workersQuery, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                User u;
                int id = rs.getInt("id");
                u = User.loadUserById(id);
                c.getListaCuochi().add(u);
            }
        });
    }

    public String ListaCuochiToString() {
        return getListaCuochi().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public int getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(int expectedTime) {
        this.expectedTime = expectedTime;
    }

    public ArrayList<User> getListaCuochi() {
        return listaCuochi;
    }

    public void setListaCuochi(ArrayList<User> listaCuochi) {
        this.listaCuochi = listaCuochi;
    }

    public static void saveTurn(Compito c){
        String updatePublish = "UPDATE catering.compiti SET assignedTurn = '"+c.getAssignedTurn()+"' WHERE id = '"+c.getId()+"';";
        PersistenceManager.executeUpdate(updatePublish);
    }

    public static void saveQtyProdTurn(Compito c){
        String updatePublish = "UPDATE catering.compiti SET qtyProduced = '"+c.getQtyProduced()+"' WHERE id = '"+c.getId()+"';";
        PersistenceManager.executeUpdate(updatePublish);
    }

    public static void saveCompleted(Compito c){
        String updatePublish = "UPDATE catering.compiti SET complete = '"+c.isCompletedtoString()+"' WHERE id = '"+c.getId()+"';";
        PersistenceManager.executeUpdate(updatePublish);
    }

    public static void saveDelCompito(Compito c) {
        String updateDelete = "DELETE FROM catering.compiti WHERE id = '"+c.getId()+"';";
        PersistenceManager.executeUpdate(updateDelete);
    }

    @Override
    public String toString() {
        String res = this.getName()+", quantita': "+this.getQty()+", tempo stimato: "+this.getExpectedTime()+", importanza: "+this.getImportance()+", completato: "+this.completed+", qty prodotta: "+this.qtyProduced;
        if(this.getAssignedTurn()!=null) res+= ", turno assegnato: "+this.getAssignedTurn();
        if(this.getListaCuochi().size()>0) {
            res+=", cuochi assegnati: ";
            for(User u: this.getListaCuochi()) {
                res+= u.getUserName()+" ";
            }
        }
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Compito) {
            return ((Compito) o).getId()==this.getId();
        }
        else return false;
    }

    public String getAssignedTurn() {
        return assignedTurn;
    }

    public void setAssignedTurn(String assignedTurn) {
        if(assignedTurn!=null) this.assignedTurn = assignedTurn;
    }

}
