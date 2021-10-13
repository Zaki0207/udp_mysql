import java.sql.*;

public class MySQLtest {
    public void addData(Recv_data r) throws SQLException {
        Connection conn = DbUtil.getConnection();
        String sql = "INSERT INTO databoard(feature_0, feature_1, feature_2, feature_3)"+
                "values("+"?,?,?,?)";
        PreparedStatement ptmt = conn.prepareStatement(sql); //预编译SQL，减少sql执行
        ptmt.setString(1, r.getMsg());
        ptmt.setInt(2, r.getData_1());
        ptmt.setInt(3, r.getData_2());
        ptmt.setDouble(4, r.getData_3());
        ptmt.execute();
    }

    public static void main(String[] args) throws SQLException {
        MySQLtest mt  = new MySQLtest();
        UDP_test ut = new UDP_test();
        Connection conn = DbUtil.getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("DROP TABLE databoard;");
        stmt.execute("CREATE TABLE IF NOT EXISTS `databoard`(" +
                "`feature_0` VARCHAR(8) NOT NULL," +
                "`feature_1` INT," +
                "`feature_2` INT," +
                "`feature_3` DOUBLE," +
                "   PRIMARY KEY ( `feature_1` )" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8;");
        while(true){
            mt.addData(ut.UDP_recv());
        }
    }
}
