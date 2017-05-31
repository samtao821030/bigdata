package com.ccclubs.bigdata.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.sql.*;


/**
 * Created by Administrator on 2017/5/31 0031.
 */
public class DBHelper {
    public static final String url="jdbc:mysql://121.199.49.206:3306/ccclubs_data_center";
    public static final String driver="com.mysql.jdbc.Driver";
    public static final String  user="tsm_user";
    public static final String password="fKWVfxCMEhGmRDyoozOw";

    public Connection conn = null;
    public PreparedStatement pst = null;

    public DBHelper(){
        try{
            Class.forName(driver);
            conn = DriverManager.getConnection(url,user,password);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public JSONArray queryRecords(String sql,long limit,long offset) throws SQLException {
        JSONArray jsonArray = new JSONArray();
        String pagination_sql = String.format(sql+" limit %d,%d",limit,offset);
        pst = conn.prepareStatement(pagination_sql);
        ResultSet rs = pst.executeQuery();
        ResultSetMetaData metaData = rs.getMetaData();
        JSONObject obj = null;
        while(rs.next()){
            obj = new JSONObject();
            for(int i=1;i<=metaData.getColumnCount();i++){
                String columnName = metaData.getColumnName(i);
                Object columnValue = rs.getObject(columnName);
                obj.put(columnName,columnValue);
            }
            jsonArray.add(obj);
        }

        return jsonArray;
    }
}
