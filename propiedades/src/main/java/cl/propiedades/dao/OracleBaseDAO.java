package cl.propiedades.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.util.Date;

@Service("oracleBaseDAO")
public class OracleBaseDAO {
    @Value("oracle.jdbc.url")
    String databaseUrl;
    protected Connection getDBConnection(){
        Connection result = null;

        try{
            result = DriverManager.getConnection("jdbc:oracle:thin:propiedades/123456@192.168.1.81:1521/xe");
        }catch(Exception e){
            e.printStackTrace();
        }

        return result;

    }
    protected static void closeAll(Connection conn, PreparedStatement stmt, ResultSet rs) {
        // Cierra el ResultSet
        try {
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            ; // Ignorar
        }
        // Cierra el Statement
        try {
            if (stmt != null)
                stmt.close();
        } catch (SQLException e) {
            ; // Ignorar
        }
        // Cierra la Connection
        try {
            if (conn != null) {

                conn.close();
            }
        } catch (Exception e) {
            ; // Ignorar
        }
    }
    protected Long stringToLong(String number) throws Exception{
        Long result = null;
        if (number != null && !number.trim().isEmpty()){
            try{
                if (!number.toLowerCase().trim().equals("null")) {
                    if (number.contains(".")){
                        Double val = Double.parseDouble(number.trim());
                        result = val.longValue();
                    }else{
                        result = Long.parseLong(number.trim());
                    }
                }

            }catch(Exception e){
                throw e;
            }
        }


        return result;
    }
    protected Double stringToDouble(String number) throws Exception{
        Double result = null;

        if (number != null && !number.trim().isEmpty()){
            try{
                if (!number.toLowerCase().trim().equals("null")) {
                    if (number.contains(",")){
                        number = number.replace(".","");
                    }
                    number = number.replace(",",".");
                    result = Double.parseDouble(number.trim());
                }
            }catch(Exception e){
                throw e;
            }
        }


        return result;
    }
    public  void insertDataFromTmp(String portal, String tipoPropiedad, String tipoTransaccion) throws SQLException {

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        String strSQL = "INSERT INTO INFORMACION_INMOBILIARIA "
                +" ( portal,"
                +"  codigo_publicacion,"
                +"  tipo_propiedad,"
                +" tipo_transaccion,"
                +" fecha_publicacion,"
                +" titulo_publicacion,"
                +" comuna,"
                +" habitaciones,"
                +" \"BAÑOS\","
                +" construido,"
                +" terreno,"
                +" precio_uf,"
                +" precio_pesos,"
                +" url, "
                +" region )"
                +" SELECT distinct portal,"
                +"     codigo_publicacion,"
                +"     tipo_propiedad,"
                +"     tipo_transaccion,"
                +"    fecha_publicacion,"
                +"    titulo_publicacion,"
                +"    comuna,"
                +"   habitaciones,"
                +"   \"BAÑOS\","
                +"   construido,"
                +"   terreno,"
                +"   precio_uf,"
                +"   precio_pesos,"
                +"   url, region"
                +" FROM    INFORMACION_INMOBILIARIA_TEMP "
                +" where portal = ? "
                +" and tipo_propiedad = ? "
                +" and tipo_transaccion= ? "
                +" and  INFORMACION_INMOBILIARIA_TEMP.CODIGO_PUBLICACION not "
                +" in"
                +" ("
                +"     select codigo_publicacion from "
                +"     INFORMACION_INMOBILIARIA "
                +"     where portal = ? "
                +"     and tipo_propiedad = ? "
                +"     and tipo_transaccion= ?"
                +" )";

        try {
            conn = getDBConnection();
            preparedStatement = conn.prepareStatement(strSQL);
            preparedStatement.setString(1, portal);
            preparedStatement.setString(2, tipoPropiedad);
            preparedStatement.setString(3, tipoTransaccion);
            preparedStatement.setString(4, portal);
            preparedStatement.setString(5, tipoPropiedad);
            preparedStatement.setString(6, tipoTransaccion);
            // execute delete SQL stetement
            int a = preparedStatement.executeUpdate();



        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {

            closeAll(conn, preparedStatement, null);

        }

    }
    public LocalDate getMaxDate(String portal, String tipoPropiedad, String tipoTransaccion, Integer dormitorios){
        LocalDate result = null;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String strSQL = "";
        ResultSet rs = null;

        strSQL = "select max(FECHA_PUBLICACION) as FECHA_PUBLICACION  from  INFORMACION_INMOBILIARIA "
                +" where portal = ? "
                +" and tipo_propiedad = ? "
                +" and tipo_transaccion= ? ";
                //+" and habitaciones = ? ";

        try {
            conn = getDBConnection();
            preparedStatement = conn.prepareStatement(strSQL);
            preparedStatement.setString(1, portal);
            preparedStatement.setString(2, tipoPropiedad);
            preparedStatement.setString(3, tipoTransaccion);
//            if (dormitorios != null && dormitorios > 0){
//                preparedStatement.setInt(4, dormitorios);
//            }else{
//                preparedStatement.setInt(4, 1);
//            }
            // execute delete SQL stetement
            rs = preparedStatement.executeQuery();
            java.sql.Date date = null;
            while(rs.next()){
                date = rs.getDate("FECHA_PUBLICACION");
            }
            if (date != null){
                result = date.toLocalDate();
            }



        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {

            closeAll(conn, preparedStatement, null);

        }

        return result;
    }
    public java.util.Date getMaxDate(String portal, String tipoPropiedad, String tipoTransaccion, String region){
        Date result = null;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String strSQL = "";
        ResultSet rs = null;

        strSQL = "select max(FECHA_PUBLICACION) as FECHA_PUBLICACION  from  INFORMACION_INMOBILIARIA "
                +" where portal = ? "
                +" and tipo_propiedad = ? "
                +" and tipo_transaccion= ? "
                +" and region = ? ";

        try {
            conn = getDBConnection();
            preparedStatement = conn.prepareStatement(strSQL);
            preparedStatement.setString(1, portal);
            preparedStatement.setString(2, tipoPropiedad);
            preparedStatement.setString(3, tipoTransaccion);
            preparedStatement.setString(4, region);
//            if (dormitorios != null && dormitorios > 0){
//                preparedStatement.setInt(4, dormitorios);
//            }else{
//                preparedStatement.setInt(4, 1);
//            }
            // execute delete SQL stetement
            rs = preparedStatement.executeQuery();
            while(rs.next()){
                result = rs.getDate("FECHA_PUBLICACION");
            }



        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {

            closeAll(conn, preparedStatement, null);

        }

        return result;
    }
}
