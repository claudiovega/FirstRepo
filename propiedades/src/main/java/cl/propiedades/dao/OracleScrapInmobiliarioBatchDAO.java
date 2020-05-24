package cl.propiedades.dao;

import cl.propiedades.model.InformacionInmobiliaria;
import cl.propiedades.model.InformacionInmobiliariaTemp;
import cl.propiedades.repository.InformacionInmobiliariaTempDAO;
import cl.propiedades.util.DateUtils;
import oracle.jdbc.OraclePreparedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class OracleScrapInmobiliarioBatchDAO extends  OracleBaseDAO{

	private static final Logger LOGGER = LoggerFactory.getLogger(OracleScrapInmobiliarioBatchDAO.class);

	@Autowired
	InformacionInmobiliariaTempDAO informacionInmobiliariaTempDAO;

	public Long batchInformacionInmobiliariaLoader(String pathFile,String dateFormatMask){
		BufferedReader br = null;
		FileReader fr = null;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		Long cont = 0L;
		String sCurrentLine = null;
		Integer total = 0;
		try {
			conn = getDBConnection();
			conn.setAutoCommit(false);
			StringBuilder strSql = new StringBuilder();
			 strSql.append(" INSERT INTO INFORMACION_INMOBILIARIA_TEMP ( " );
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
			 preparedStatement = conn.prepareStatement(strSql.toString());
			//Change batch size for this statement to 3 
			((OraclePreparedStatement)preparedStatement).setExecuteBatch (1000);
			
			fr = new FileReader(pathFile);
			br = new BufferedReader(fr);
			


			System.out.println("reading "+pathFile);
			Integer secondCont = 0;
			
			while ((sCurrentLine = br.readLine()) != null) {
				cont++;

				if (cont !=1){
					
					secondCont++;
					String[] arr = sCurrentLine.split("\\|");
					if (arr[1] != null && !arr[1].trim().isEmpty()){
						
						preparedStatement.setString(1, arr[0]);
						preparedStatement.setString(2, arr[1]);
						preparedStatement.setString(3, arr[2]);
						preparedStatement.setString(4, arr[3]);
						preparedStatement.setDate(5, DateUtils.stringToSqlDate(arr[4], dateFormatMask));
						preparedStatement.setString(6, arr[5]);
						preparedStatement.setString(7, arr[6]);

						if (stringToLong(arr[7]) != null){
							preparedStatement.setLong(8, stringToLong(arr[7]));
						}else{
							preparedStatement.setNull(8, Types.BIGINT);
						}

						if (stringToLong(arr[8]) != null){
							preparedStatement.setLong(9, stringToLong(arr[8]));
						}else{
							preparedStatement.setNull(9, Types.BIGINT);
						}

						if (stringToLong(arr[9]) != null){
							preparedStatement.setLong(10, stringToLong(arr[9]));
						}else{
							preparedStatement.setNull(10, Types.BIGINT);
						}

						if (stringToLong(arr[10]) != null){
							preparedStatement.setLong(11, stringToLong(arr[10]));
						}else{
							preparedStatement.setNull(11, Types.BIGINT);
						}



						if (stringToDouble(arr[11]) != null){
							preparedStatement.setDouble(12, stringToDouble(arr[11]));
						}else{
							preparedStatement.setNull(12, Types.BIGINT);
						}


						if (stringToLong(arr[12]) != null){
							preparedStatement.setLong(13, stringToLong(arr[12]));
						}else{
							preparedStatement.setNull(13, Types.BIGINT);
						}

						preparedStatement.setString(14, arr[13]);
						total = total+preparedStatement.executeUpdate();
						if (secondCont == 1000){
							System.out.println(pathFile +" Lineas cargadas: "+secondCont);
							secondCont= 0;

						}
					}

				}


			}
			total = total+preparedStatement.executeUpdate(); //JDBC queues this for later execution
			 
			try {
				((OraclePreparedStatement)preparedStatement).sendBatch(); // JDBC sends the queued request
			} catch (Exception e) {
				System.out.println(sCurrentLine);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			conn.commit();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}finally{
			try {
				if (br != null){
					br.close();	
				}
				if (fr != null) {
					fr.close();
				}
				closeAll(conn, preparedStatement, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		System.out.println("total+preparedStatement.executeUpdate() "+total);
		return cont;
	}
	public Long batchInformacionInmobiliariaLoader(List<String> listLines,String dateFormatMask, Integer pagina, Integer posicion){

		return this.batchInformacionInmobiliariaLoaderJpa(listLines,dateFormatMask,pagina,posicion);
//		Connection conn = null;
//		PreparedStatement preparedStatement = null;
//		Long cont = 0L;
//		String lineForError = "";
//		Integer total = 0;
//		try {
//			conn = getDBConnection();
//			conn.setAutoCommit(false);
//
//			//Change batch size for this statement to 3
//			//((OraclePreparedStatement)preparedStatement).setExecuteBatch (1000);
//
//			Integer secondCont = 0;
//
//			for(String sCurrentLine : listLines) {
//				lineForError = sCurrentLine;
//				StringBuilder strSql = new StringBuilder();
//				strSql.append(" INSERT INTO INFORMACION_INMOBILIARIA_TEMP ( " );
//				strSql.append(" PORTAL, ");
//				strSql.append(" CODIGO_PUBLICACION, ");
//				strSql.append(" TIPO_PROPIEDAD, ");
//				strSql.append(" TIPO_TRANSACCION, ");
//				strSql.append(" FECHA_PUBLICACION, ");
//				strSql.append(" TITULO_PUBLICACION, ");
//				strSql.append(" COMUNA, ");
//				strSql.append(" HABITACIONES, ");
//				strSql.append(" BAÑOS, ");
//				strSql.append(" CONSTRUIDO, ");
//				strSql.append(" TERRENO, ");
//				strSql.append(" PRECIO_UF, ");
//				strSql.append(" PRECIO_PESOS, ");
//				strSql.append(" URL,  ");
//				strSql.append(" REGION  ");
//				strSql.append(" )  VALUES (  ");
//				strSql.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
//				preparedStatement = conn.prepareStatement(strSql.toString());
//				cont++;
//
//
//
//					secondCont++;
//
//					String[] arr = sCurrentLine.split("\\|");
//					if (arr[1] != null && !arr[1].trim().isEmpty()){
//
//						preparedStatement.setString(1, arr[0]);
//						preparedStatement.setString(2, arr[1]);
//						preparedStatement.setString(3, arr[2]);
//						preparedStatement.setString(4, arr[3]);
//						preparedStatement.setDate(5, DateUtils.stringToSqlDate(arr[4], dateFormatMask));
//						preparedStatement.setString(6, arr[5]);
//						preparedStatement.setString(7, arr[6]);
//
//						try {
//							if (stringToLong(arr[7]) != null){
//								preparedStatement.setLong(8, stringToLong(arr[7]));
//							}else{
//								preparedStatement.setNull(8, Types.BIGINT);
//							}
//						} catch (Exception e) {
//							preparedStatement.setNull(8, Types.BIGINT);
//							this.logErroresProceso(arr[0]+" HABITACIONES batchInformacionInmobiliariaLoader, Pagina: "+pagina+" Posicion: "+posicion, sCurrentLine, arr[3], arr[2],  e);
//						}
//
//						try {
//							if (stringToLong(arr[8]) != null){
//								preparedStatement.setLong(9, stringToLong(arr[8]));
//							}else{
//								preparedStatement.setNull(9, Types.BIGINT);
//							}
//						} catch (Exception e) {
//							preparedStatement.setNull(9, Types.BIGINT);
//							this.logErroresProceso(arr[0]+" BAÑOS batchInformacionInmobiliariaLoader, Pagina: "+pagina+" Posicion: "+posicion, sCurrentLine,  arr[3], arr[2],  e);
//						}
//
//						try {
//							if (stringToLong(arr[9]) != null){
//								preparedStatement.setLong(10, stringToLong(arr[9]));
//							}else{
//								preparedStatement.setNull(10, Types.BIGINT);
//							}
//						} catch (Exception e) {
//							preparedStatement.setNull(10, Types.BIGINT);
//							this.logErroresProceso(arr[0]+" CONSTRUIDO batchInformacionInmobiliariaLoader, Pagina: "+pagina+" Posicion: "+posicion, sCurrentLine,  arr[3], arr[2],  e);
//						}
//
//						try {
//							if (stringToLong(arr[10]) != null){
//								preparedStatement.setLong(11, stringToLong(arr[10]));
//							}else{
//								preparedStatement.setNull(11, Types.BIGINT);
//							}
//						} catch (Exception e) {
//							preparedStatement.setNull(11, Types.BIGINT);
//							this.logErroresProceso(arr[0]+" TERRENO batchInformacionInmobiliariaLoader, Pagina: "+pagina+" Posicion: "+posicion, sCurrentLine,  arr[3], arr[2],  e);
//						}
//
//
//						try {
//							if (stringToDouble(arr[11]) != null){
//								preparedStatement.setDouble(12, stringToDouble(arr[11]));
//							}else{
//								preparedStatement.setNull(12, Types.BIGINT);
//							}
//						} catch (Exception e) {
//							preparedStatement.setNull(12, Types.BIGINT);
//							this.logErroresProceso(arr[0]+" PRECIO_UF batchInformacionInmobiliariaLoader, Pagina: "+pagina+" Posicion: "+posicion, sCurrentLine, arr[3], arr[2],  e);
//						}
//
//
//						try {
//							if (stringToLong(arr[12]) != null){
//								preparedStatement.setLong(13, stringToLong(arr[12]));
//							}else{
//								preparedStatement.setNull(13, Types.BIGINT);
//							}
//						} catch (Exception e) {
//							preparedStatement.setNull(13, Types.BIGINT);
//							this.logErroresProceso(arr[0]+" PRECIO_PESOS batchInformacionInmobiliariaLoader, Pagina: "+pagina+" Posicion: "+posicion, sCurrentLine,  arr[3], arr[2],  e);
//						}
//
//						preparedStatement.setString(14, arr[13]);
//						if (arr.length == 14){
//							preparedStatement.setString(15, "vacio");
//						}else{
//							preparedStatement.setString(15, arr[14]);
//						}
//
//
//						try {
//							preparedStatement.executeUpdate(); //JDBC queues this for later execution
//						} catch (Exception e) {
//							LOGGER.info("*************************************************************");
//							LOGGER.info(sCurrentLine);
//							e.printStackTrace();
//							LOGGER.info("*************************************************************");
//							this.logErroresProceso(arr[0]+" batchInformacionInmobiliariaLoader, Pagina: "+pagina+" Posicion: "+posicion, sCurrentLine,  arr[3], arr[2],  e);
//
//
//						}
//
//					}
//			}
//
//			conn.commit();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			if (StringUtils.isEmpty(lineForError)){
//				lineForError = "NA";
//			}
//
//			this.logErroresProceso(" batchInformacionInmobiliariaLoader, Pagina: "+pagina+" Posicion: "+posicion, lineForError, "NA", "NA",  e);
//
//
//		}finally{
//			try {
//
//				closeAll(conn, preparedStatement, null);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		return cont;
	}

	public void logErroresProceso(String portal, String url, String tipoTransaccion, String tipoPropiedad, Exception e) {
		ZoneId zoneId = ZoneId.of("Chile/Continental");


		StackTraceElement[] stackTraceElements = e.getStackTrace();
		StringBuilder errorCompleto = new StringBuilder("");
		errorCompleto.append(e.getMessage()+"\n");
		for (Integer i = 0; i < stackTraceElements.length; i++) {
			StackTraceElement stackTraceElement = stackTraceElements[i];
			errorCompleto.append(stackTraceElement.toString());
			errorCompleto.append("\n");

		}

		Connection conn = null;
		PreparedStatement preparedStatement = null;

		try {
			conn = getDBConnection();
			conn.setAutoCommit(true);
			StringBuilder strSql = new StringBuilder();
			strSql.append(" INSERT INTO errores ( ");
			strSql.append(" fecha, ");
			strSql.append(" portal, ");
			strSql.append(" url, ");
			strSql.append(" error, ");
			strSql.append(" tipo_transaccion, ");
			strSql.append(" tipo_inmueble ");

			strSql.append(" )  VALUES (  ");
			strSql.append("?, ?, ?, ?, ?, ?)");
			preparedStatement = conn.prepareStatement(strSql.toString());
			preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now(zoneId)));
			preparedStatement.setString(2, portal);
			preparedStatement.setString(3, url);
			preparedStatement.setString(4, errorCompleto.toString());
			preparedStatement.setString(5, tipoTransaccion);
			preparedStatement.setString(6, tipoPropiedad);

			preparedStatement.executeUpdate(); //JDBC queues this for later execution


			conn.commit();

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {
			try {

				closeAll(conn, preparedStatement, null);
			} catch (Exception eex) {
				// TODO Auto-generated catch block
				eex.printStackTrace();
			}
		}


	}



	public  void deleteRecordFromTable(String portal, String tipoPropiedad, String tipoTransaccion) throws SQLException {

		Connection conn = null;
		PreparedStatement preparedStatement = null;

		String deleteSQL = "DELETE from INFORMACION_INMOBILIARIA_TEMP WHERE PORTAL = ? and TIPO_PROPIEDAD = ? and  TIPO_TRANSACCION = ?";

		try {
			conn = getDBConnection();
			preparedStatement = conn.prepareStatement(deleteSQL);
			preparedStatement.setString(1, portal);
			preparedStatement.setString(2, tipoPropiedad);
			preparedStatement.setString(3, tipoTransaccion);
			// execute delete SQL stetement
			int a = preparedStatement.executeUpdate();

			//System.out.println(a +" Records  deleted!");
			LOGGER.info("deleteRecordFromTable: "+portal+"-"+tipoPropiedad+"-"+tipoTransaccion+" INFORMACION_INMOBILIARIA_TEMP: "+a +"  Records  deleted!");
		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			closeAll(conn, preparedStatement, null);       

		}

	}
	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public Long batchInformacionInmobiliariaLoaderJpa(List<String> listLines,String dateFormatMask, Integer pagina, Integer posicion){

		Long cont = 0L;
		String lineForError = "";
		try {

			for(String sCurrentLine : listLines) {
				InformacionInmobiliariaTemp informacionInmobiliariaTemp = new InformacionInmobiliariaTemp();
				lineForError = sCurrentLine;

				cont++;
				String[] arr = sCurrentLine.split("\\|");
				if (arr[1] != null && !arr[1].trim().isEmpty()){

					informacionInmobiliariaTemp.setPortal(arr[0]);

					informacionInmobiliariaTemp.setCodigoPublicacion(arr[1]);

					informacionInmobiliariaTemp.setTipoPropiedad(arr[2]);

					informacionInmobiliariaTemp.setTipoPransaccion(arr[3]);

					informacionInmobiliariaTemp.setFechaPublicacion(DateUtils.stringToLocalDate(arr[4], dateFormatMask));

					informacionInmobiliariaTemp.setTituloPublicacion(arr[5]);

					informacionInmobiliariaTemp.setComuna(arr[6]);

					try {
						if (stringToLong(arr[7]) != null){
							informacionInmobiliariaTemp.setHabitaciones(stringToLong(arr[7]));
						}
					} catch (Exception e) {
						this.logErroresProceso(arr[0]+" HABITACIONES batchInformacionInmobiliariaLoaderJpa, Pagina: "+pagina+" Posicion: "+posicion, sCurrentLine, arr[3], arr[2],  e);
					}

					try {
						if (stringToLong(arr[8]) != null){
							informacionInmobiliariaTemp.setBaños(stringToLong(arr[8]));

						}
					} catch (Exception e) {

						this.logErroresProceso(arr[0]+" BAÑOS batchInformacionInmobiliariaLoaderJpa, Pagina: "+pagina+" Posicion: "+posicion, sCurrentLine,  arr[3], arr[2],  e);
					}

					try {
						if (stringToLong(arr[9]) != null){

							informacionInmobiliariaTemp.setConstruido(stringToLong(arr[9]));

						}
					} catch (Exception e) {

						this.logErroresProceso(arr[0]+" CONSTRUIDO batchInformacionInmobiliariaLoaderJpa, Pagina: "+pagina+" Posicion: "+posicion, sCurrentLine,  arr[3], arr[2],  e);
					}

					try {
						if (stringToLong(arr[10]) != null){
							informacionInmobiliariaTemp.setTerreno(stringToLong(arr[10]));

						}
					} catch (Exception e) {

						this.logErroresProceso(arr[0]+" TERRENO batchInformacionInmobiliariaLoaderJpa, Pagina: "+pagina+" Posicion: "+posicion, sCurrentLine,  arr[3], arr[2],  e);
					}


					try {
						if (stringToDouble(arr[11]) != null){
							informacionInmobiliariaTemp.setPrecioUf(stringToDouble(arr[11]));

						}
					} catch (Exception e) {

						this.logErroresProceso(arr[0]+" PRECIO_UF batchInformacionInmobiliariaLoaderJpa, Pagina: "+pagina+" Posicion: "+posicion, sCurrentLine, arr[3], arr[2],  e);
					}


					try {
						if (stringToLong(arr[12]) != null){
							informacionInmobiliariaTemp.setPrecioPesos(stringToLong(arr[12]));

						}
					} catch (Exception e) {

						this.logErroresProceso(arr[0]+" PRECIO_PESOS batchInformacionInmobiliariaLoaderJpa, Pagina: "+pagina+" Posicion: "+posicion, sCurrentLine,  arr[3], arr[2],  e);
					}
					informacionInmobiliariaTemp.setUrl(arr[13]);

					if (arr.length == 14){
						informacionInmobiliariaTemp.setRegion("vacio");

					}else{
						informacionInmobiliariaTemp.setRegion(arr[14]);

					}


					try {

						this.informacionInmobiliariaTempDAO.save(informacionInmobiliariaTemp);
					} catch (Exception e) {
						LOGGER.info("*************************************************************");
						LOGGER.info(sCurrentLine);
						e.printStackTrace();
						LOGGER.info("*************************************************************");
						this.logErroresProceso(arr[0]+" batchInformacionInmobiliariaLoaderJpa, Pagina: "+pagina+" Posicion: "+posicion, sCurrentLine,  arr[3], arr[2],  e);


					}

				}
			}



		} catch (Exception e) {
			e.printStackTrace();
			if (StringUtils.isEmpty(lineForError)){
				lineForError = "NA";
			}

			this.logErroresProceso(" batchInformacionInmobiliariaLoaderJpa, Pagina: "+pagina+" Posicion: "+posicion, lineForError, "NA", "NA",  e);


		}

		return cont;
	}
}
