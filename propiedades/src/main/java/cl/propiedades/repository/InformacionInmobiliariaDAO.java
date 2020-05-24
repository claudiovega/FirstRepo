package cl.propiedades.repository;


import cl.propiedades.model.InformacionInmobiliaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InformacionInmobiliariaDAO extends JpaRepository<InformacionInmobiliaria, Long>, JpaSpecificationExecutor<InformacionInmobiliaria> {

    @Query("SELECT ii FROM InformacionInmobiliaria ii WHERE    ii.fechaPublicacion <= :fecha order by ii.fechaPublicacion DESC")
    List<InformacionInmobiliaria> deleteOldRecords(@Param("fecha")LocalDate fecha);

}
