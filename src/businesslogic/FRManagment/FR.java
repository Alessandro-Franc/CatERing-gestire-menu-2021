package businesslogic.FRManagment;

import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class FR {
    private int id;
    private String name;
    private int id_owner;
    private ArrayList<Compito> listaCompiti;

    public FR(String name, int id_owner) {
        this.name = name;
        this.id_owner = id_owner;
        this.listaCompiti = new ArrayList<>();
    }

    @Override
    public String toString() {
        return this.name + " " + this.id_owner;
    }

    public String getName() {
        return name;
    }

    public int getId_owner() {
        return id_owner;
    }

    public ArrayList<Compito> getListaCompiti() {
        return listaCompiti;
    }

    public void sortCompiti(){
        Collections.sort(listaCompiti, new Comparator<Compito>() {
            @Override
            public int compare(Compito o1, Compito o2) {
                Integer i1 = o1.getImportance();
                Integer i2 = o2.getImportance();
                return i2.compareTo(i1);
            }
        });
    }

    public static void fillListaCompiti(FR fr) {
        String workersQuery = "select compiti.* from compiti join FR on compiti.id_fr=fr.id where compiti.id_fr = "+fr.getId()+";";
        PersistenceManager.executeQuery(workersQuery, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                Compito c;
                int id = rs.getInt("id");
                int qty = rs.getInt("qty");
                int imp = rs.getInt("qty");
                int exp = rs.getInt("expectedTime");
                boolean compl = rs.getBoolean("complete");
                int qtyProd = rs.getInt("qty");
                String name = rs.getString("name");
                String turn = rs.getString("assignedTurn");
                c = new Compito(name, qty, imp, exp);
                c.setId_fr(fr.getId());
                c.setCompleted(compl);
                c.setQtyProduced(qtyProd);
                c.setId(id);
                c.setAssignedTurn(turn);
                fr.getListaCompiti().add(c);
            }
        });
    }

    public static void saveNewFoglio(FR fr){
        String prenInsert = "INSERT INTO catering.FR (name, id_owner) VALUES (?, ?);";
        int[] result = PersistenceManager.executeBatchUpdate(prenInsert, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setString(1, fr.getName());
                ps.setInt(2, fr.getId_owner());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                if (count == 0) {
                    fr.setId(rs.getInt(1));
                }
            }
        });
    }

    public static void saveDelFoglio(FR fr) {
        String updateDelete = "DELETE FROM catering.FR WHERE id = '"+fr.getId()+"';";
        PersistenceManager.executeUpdate(updateDelete);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setListaCompiti(ArrayList<Compito> listaCompiti) {
        this.listaCompiti = listaCompiti;
    }
}
