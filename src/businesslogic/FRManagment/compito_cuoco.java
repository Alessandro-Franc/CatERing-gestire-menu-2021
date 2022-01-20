package businesslogic.FRManagment;

import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class compito_cuoco {
    private int id_compito;
    private int id_cuoco;

    public compito_cuoco(int id_compito, int id_cuoco) {
        this.id_compito=id_compito;
        this.id_cuoco=id_cuoco;
    }

    public int getId_compito() {
        return id_compito;
    }

    public int getId_cuoco() {
        return id_cuoco;
    }

    //Static Method

    public static void saveNewCC(compito_cuoco cc){
        String prenInsert = "INSERT INTO catering.compito_cuoco (id_compito, id_cuoco) VALUES (?, ?);";
        int[] result = PersistenceManager.executeBatchUpdate(prenInsert, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, cc.getId_compito());
                ps.setInt(2, cc.getId_cuoco());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                if (count == 0) {
                    //p.setId(rs.getInt(1));
                }
            }
        });
    }
}