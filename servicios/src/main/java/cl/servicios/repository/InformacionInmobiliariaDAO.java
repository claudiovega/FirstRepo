package cl.servicios.repository;

import cl.servicios.model.InformacionInmobiliaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InformacionInmobiliariaDAO extends JpaRepository<InformacionInmobiliaria, Long>, JpaSpecificationExecutor<InformacionInmobiliaria> {
}
