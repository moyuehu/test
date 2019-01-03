package cn;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;

public class ReadExcelFromDB  {
    //字段名数组
    private static final String[] tableHeader={"id","username","age"};
//			"password","salt","email","mobile","valid","deptId",
//			"createdTime","modifiedTime","createdUser","modifiedUser"};
    //设置列宽
    private static final int[] colWidth={2000,2000,5000};
    //文件路径
    private static String outPutFile="."+ File.separator+"user.xls";
    //数据库连接参数
    private static String url="jdbc:mysql://localhost:3306/db6";
    private static String user="root";
    private static String password="158990279";

    public static void main(String[] args) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //连接数据库查询数据
            Class.forName("com.mysql.jdbc.Driver");
            conn= DriverManager.getConnection(url, user, password);
            String sql ="select * from user where id = id";
            ps=conn.prepareStatement(sql);
            //获取结果集
            rs = ps.executeQuery();
            //用于获取字段的描述信息,比如字段名
            ResultSetMetaData metaData = rs.getMetaData();
            //创建workBook对象
            HSSFWorkbook workBook=new HSSFWorkbook();
            //在workBook对象中创建一张表格
            HSSFSheet sheet= workBook.createSheet("sys_users");
            //设置每一列的宽度
            for(int i=0;i<colWidth.length;i++){
                sheet.setColumnWidth(i, colWidth[i]);
            }
            //单元格样式对象
            HSSFCellStyle cellStyle = workBook.createCellStyle();
            //设置文本居中
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            //创建第0行,作为表格的表头
            HSSFRow row=sheet.createRow(1);
            HSSFCell cell=null;
            for(int i=0;i<colWidth.length;i++){
                cell=row.createCell(i);
                //动态获取字段名
                cell.setCellValue(metaData.getColumnLabel(i+1));
                cell.setCellStyle(cellStyle);
            }
            int rowIndex=1;
            while(rs.next()){
                //循环将查询出来的数据封装到表格的一行中
                row=sheet.createRow(rowIndex);
                for(int i=0;i<colWidth.length;i++){
                    cell=row.createCell(i);
                    cell.setCellValue(rs.getString(i+1));
                    cell.setCellStyle(cellStyle);
                }
                rowIndex++;
            }
            FileOutputStream fos=new FileOutputStream(outPutFile);
            //输出流将文件写到硬盘
            workBook.write(fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rs.close();
            ps.close();
            conn.close();
        }
    }


}
