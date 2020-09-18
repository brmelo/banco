package br.com.banco.api.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.banco.api.model.entity.Conta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Integer> {
	List<Conta> findByConta(Integer conta);
}
