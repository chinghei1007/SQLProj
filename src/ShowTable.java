import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class ShowTable {
    public static void showtable(Statement statement, String sql) throws SQLException {
        ResultSet resultSet = statement.executeQuery(sql);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int colCount=resultSetMetaData.getColumnCount();
        System.out.println(colCount);

        DefaultTableModel tableModel = new DefaultTableModel();
        String[] colNames = new String[colCount];
        for (int i = 1; i < colCount+1; i++){
            tableModel.addColumn(resultSetMetaData.getColumnName(i));
            colNames[i-1] = resultSetMetaData.getColumnName(i);
        }

        while (resultSet.next()) {
            Object[] row = new Object[colCount];
            for (int i = 1; i <= colCount; i++) {
                row[i - 1] = resultSet.getObject(i);
            }
            tableModel.addRow(row);
        }

        JTable table = new JTable(tableModel);
        JScrollPane jScrollPane = new JScrollPane(table);

        JFrame jFrame = new JFrame();
        jFrame.setTitle("Test");
        jFrame.setSize(900,600);
        jFrame.setLocationRelativeTo(null);
        jFrame.add(jScrollPane);
        //Column width auto
        int cellPadding = 10;
        int headerPadding = 10;

        // Resize columns based on maximum content length and column headers
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            TableCellRenderer headerRenderer = column.getHeaderRenderer();
            if (headerRenderer == null) {
                headerRenderer = table.getTableHeader().getDefaultRenderer();
            }
            Component headerComp = headerRenderer.getTableCellRendererComponent(table, column.getHeaderValue(), false, false, 0, 0);
            int headerWidth = headerComp.getPreferredSize().width + headerPadding;

            int preferredWidth = Math.max(headerWidth, column.getMinWidth());
            int maxWidth = column.getMaxWidth();

            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, i);
                Component cellComp = table.prepareRenderer(cellRenderer, row, i);
                int cellWidth = cellComp.getPreferredSize().width + cellPadding;
                preferredWidth = Math.max(preferredWidth, cellWidth);
                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }

            column.setPreferredWidth(preferredWidth);
        }

        jFrame.setVisible(true);

    }
}
