package cl.propiedades.repository;

import cl.propiedades.model.Proceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcesoDAO extends JpaRepository<Proceso, Long>, JpaSpecificationExecutor<Proceso> {
}
