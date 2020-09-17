package br.com.banco.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.banco.api.model.entity.Movimento;

@Repository
public interface MovimentoRepository extends JpaRepository<Movimento, Integer>{

}
