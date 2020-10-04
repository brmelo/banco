package br.com.banco.api.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.banco.api.model.entity.Conta;

public class ContaDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nome;
	private String cpf;
	private Double saldo;
	private Integer conta;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dataCadastro;

	
	public ContaDTO() {
	}

	public ContaDTO(Conta entity) {
		id = entity.getId();
		nome = entity.getNome();
		cpf = entity.getCpf();
		saldo = entity.getSaldo();
		conta = entity.getConta();
		dataCadastro = entity.getDataCadastro();
	}
	
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getCpf() {
		return cpf;
	}


	public void setCpf(String cpf) {
		this.cpf = cpf;
	}


	public Double getSaldo() {
		return saldo;
	}


	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}


	public Integer getConta() {
		return conta;
	}


	public void setConta(Integer conta) {
		this.conta = conta;
	}

	public LocalDate getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public static List<ContaDTO> converter(List<Conta> contas) {
        return contas.stream().map(ContaDTO::new).collect(Collectors.toList());
    }

}
