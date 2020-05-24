package cl.propiedades.repository;


import cl.propiedades.model.Errores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ErroresDAO extends JpaRepository<Errores, Long>, JpaSpecificationExecutor<Errores> {


}
