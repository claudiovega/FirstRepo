package cl.propiedades.repository;


import cl.propiedades.model.LogProcesoInmobiliario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LogProcesoInmobiliarioDAO extends JpaRepository<LogProcesoInmobiliario, Long>, JpaSpecificationExecutor<LogProcesoInmobiliario> {


}
