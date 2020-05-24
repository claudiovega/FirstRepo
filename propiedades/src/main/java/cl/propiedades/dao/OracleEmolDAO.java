package cl.propiedades.dao;

import cl.propiedades.vo.InformacionInmobiliariaTempVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class OracleEmolDAO extends  OracleBaseDAO {








	public  List<InformacionInmobiliariaTempVO> getDataToInsert(String portal, String tipoPropiedad, String tipoTransaccion) throws SQLException {

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		
		ResultSet rs = null;
		List<InformacionInmobiliariaTempVO> result = new ArrayList<InformacionInmobiliariaTempVO>();
		String strSQL = " SELECT distinct portal,"
				+"     codigo_publicacion,"
				+"     tipo_propiedad,"
				+"     tipo_transaccion,"
				 +"    fecha_publicacion,"
				 +"    titulo_publicacion,"
				 +"    comuna,"
				  +"   habitaciones,"
				  +"   \"BA�OS\","
				  +"   construido,"
				  +"   terreno,"
				  +"   precio_uf,"
				  +"   precio_pesos,"
				  +"   url"
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
			rs = preparedStatement.executeQuery();

	        while (rs.next()){
	        	InformacionInmobiliariaTempVO informacionInmobiliariaTemp = new InformacionInmobiliariaTempVO();

			if (rs.getString("PORTAL") != null){
	  			informacionInmobiliariaTemp.setPortal(rs.getString("PORTAL")); 
	       }
			if (rs.getString("CODIGO_PUBLICACION") != null){
	  			informacionInmobiliariaTemp.setCodigoPublicacion(rs.getString("CODIGO_PUBLICACION")); 
	       }
			if (rs.getString("TIPO_PROPIEDAD") != null){
	  			informacionInmobiliariaTemp.setTipoPropiedad(rs.getString("TIPO_PROPIEDAD")); 
	       }
			if (rs.getString("TIPO_TRANSACCION") != null){
	  			informacionInmobiliariaTemp.setTipoTransaccion(rs.getString("TIPO_TRANSACCION")); 
	       }
			if (rs.getString("FECHA_PUBLICACION") != null){
	  			informacionInmobiliariaTemp.setFechaPublicacion(rs.getTimestamp("FECHA_PUBLICACION")); 
	       }
			if (rs.getString("TITULO_PUBLICACION") != null){
	  			informacionInmobiliariaTemp.setTituloPublicacion(rs.getString("TITULO_PUBLICACION")); 
	       }
			if (rs.getString("COMUNA") != null){
	  			informacionInmobiliariaTemp.setComuna(rs.getString("COMUNA")); 
	       }
			if (rs.getString("HABITACIONES") != null){
	  			informacionInmobiliariaTemp.setHabitaciones(rs.getLong("HABITACIONES")); 
	       }
			if (rs.getString("BAÑOS") != null){
	  			informacionInmobiliariaTemp.setBaños(rs.getLong("BA�OS"));
	       }
			if (rs.getString("CONSTRUIDO") != null){
	  			informacionInmobiliariaTemp.setConstruido(rs.getLong("CONSTRUIDO")); 
	       }
			if (rs.getString("TERRENO") != null){
	  			informacionInmobiliariaTemp.setTerreno(rs.getLong("TERRENO")); 
	       }
			if (rs.getString("PRECIO_UF") != null){
	  			informacionInmobiliariaTemp.setPrecioUf(rs.getDouble("PRECIO_UF")); 
	       }
			if (rs.getString("PRECIO_PESOS") != null){
	  			informacionInmobiliariaTemp.setPrecioPesos(rs.getDouble("PRECIO_PESOS")); 
	       }
			if (rs.getString("URL") != null){
	  			informacionInmobiliariaTemp.setUrl(rs.getString("URL")); 
	       }
	                result.add(informacionInmobiliariaTemp);
	}

			
			
		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			closeAll(conn, preparedStatement, null);       

		}
		return result;

	}
	public boolean insertInformacionInmobiliaria(InformacionInmobiliariaTempVO p_informacionInmobiliaria) throws Exception {
	      boolean result = false;
	        Connection conn = null;
	        PreparedStatement preparedStatement = null;
	        ResultSet rs = null;
	        StringBuilder strSql = new StringBuilder();
	        if (p_informacionInmobiliaria == null){
	            throw new Exception("El parametro p_informacionInmobiliaria no debe ser nulo.");
	        }
	 		if (p_informacionInmobiliaria.getPortal() == null){
	            throw new Exception("El parametro Portal no debe ser nulo.");
			}
	 		if (p_informacionInmobiliaria.getCodigoPublicacion() == null){
	            throw new Exception("El parametro CodigoPublicacion no debe ser nulo.");
			}
	 		if (p_informacionInmobiliaria.getTipoPropiedad() == null){
	            throw new Exception("El parametro TipoPropiedad no debe ser nulo.");
			}
	 		if (p_informacionInmobiliaria.getTipoTransaccion() == null){
	            throw new Exception("El parametro TipoTransaccion no debe ser nulo.");
			}
	       strSql.append(" "); 
	       strSql.append(" INSERT INTO INFORMACION_INMOBILIARIA ( " );
	       strSql.append(" PORTAL, ");
	       strSql.append(" CODIGO_PUBLICACION, ");
	       strSql.append(" TIPO_PROPIEDAD, ");
	       strSql.append(" TIPO_TRANSACCION, ");
	       strSql.append(" FECHA_PUBLICACION, ");
	       strSql.append(" TITULO_PUBLICACION, ");
	       strSql.append(" COMUNA, ");
	       strSql.append(" HABITACIONES, ");
	       strSql.append(" BA�OS, ");
	       strSql.append(" CONSTRUIDO, ");
	       strSql.append(" TERRENO, ");
	       strSql.append(" PRECIO_UF, ");
	       strSql.append(" PRECIO_PESOS, ");
	       strSql.append(" URL ");
	       strSql.append(" )  VALUES (  ");
		   strSql.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
	         System.out.println(strSql.toString());
	         try {
	             conn = getDBConnection();
	             preparedStatement = conn.prepareStatement(strSql.toString());
	             int i = 0;
	  			i++;
	   			preparedStatement.setString(i, p_informacionInmobiliaria.getPortal());

	  			i++;
	   			preparedStatement.setString(i, p_informacionInmobiliaria.getCodigoPublicacion());

	  			i++;
	   			preparedStatement.setString(i, p_informacionInmobiliaria.getTipoPropiedad());

	  			i++;
	   			preparedStatement.setString(i, p_informacionInmobiliaria.getTipoTransaccion());

	  			i++;
	  			if (p_informacionInmobiliaria.getFechaPublicacion() != null){
	  				
	  				preparedStatement.setDate (i,  new java.sql.Date( p_informacionInmobiliaria.getFechaPublicacion().getTime()));
	  			}else{
	  				preparedStatement.setDate (i,  null);	
	  			}
	   			

	  			i++;
	   			preparedStatement.setString(i, p_informacionInmobiliaria.getTituloPublicacion());

	  			i++;
	   			preparedStatement.setString(i, p_informacionInmobiliaria.getComuna());

	  			i++;
	  			if (p_informacionInmobiliaria.getHabitaciones() != null){
	  				preparedStatement.setLong(i, p_informacionInmobiliaria.getHabitaciones());
	  			}else{
	  					
	  				preparedStatement.setLong(i, Types.BIGINT);
	  			}
	  			
	   			

	  			i++;
	  			if (p_informacionInmobiliaria.getBaños() != null){
	  				preparedStatement.setLong(i, p_informacionInmobiliaria.getBaños());
	  			}else{
	  				preparedStatement.setLong(i, Types.BIGINT);	
	  			}
	   			

	  			i++;
	  			if (p_informacionInmobiliaria.getConstruido() != null){
	  				preparedStatement.setLong(i, p_informacionInmobiliaria.getConstruido());	
	  			}else{
	  				preparedStatement.setLong(i, Types.BIGINT);
	  			}
	   			

	  			i++;
	  			if (p_informacionInmobiliaria.getTerreno()!=null){
	  				preparedStatement.setLong(i, p_informacionInmobiliaria.getTerreno());	
	  			}else{
	  				preparedStatement.setLong(i, Types.BIGINT);
	  			}
	   			

	  			i++;
	   			preparedStatement.setDouble(i, p_informacionInmobiliaria.getPrecioUf());

	  			i++;
	   			preparedStatement.setDouble(i, p_informacionInmobiliaria.getPrecioPesos());

	  			i++;
	   			preparedStatement.setString(i, p_informacionInmobiliaria.getUrl());
	             int x = 0;
	             x = preparedStatement.executeUpdate();
	             if (x > 0){
	                 result = true;
	             }
	         } catch (SQLException e){
	             result = false;
	             e.printStackTrace();
	             throw e;
	         } catch (Exception e) {
	             e.printStackTrace();
	             result = false;
	             throw new Exception("Ocurrio un error al ejecutar la consulta especificada.", e);
	         } finally {
	             closeAll(conn, preparedStatement, rs);                
	         }

	      return result;
	}
}
