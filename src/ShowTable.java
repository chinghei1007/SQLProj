import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class ShowTable {
    public static void showtable(Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery("select * from products");
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int colCount=resultSetMetaData.getColumnCount();
        System.out.println(colCount);

        DefaultTableModel tableModel = new DefaultTableModel();
        String[] colNames = new String[colCount];
        for (int i = 1; i < colCount+1; i++){
            tableModel.addColumn(resultSetMetaData.getColumnName(i));
            colNames[i-1] = resultSetMetaData.getColumnName(i);
        }
        JTable table = new JTable(tableModel);
        JScrollPane jScrollPane = new JScrollPane(table);

        JFrame jFrame = new JFrame();
        jFrame.setTitle("Test");
        jFrame.setSize(500,600);
        jFrame.add(jScrollPane);
        jFrame.setVisible(true);

    }
}
