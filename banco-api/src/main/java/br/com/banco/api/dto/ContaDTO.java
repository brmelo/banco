package br.com.banco.api.dto;

import java.io.Serializable;

import br.com.banco.api.model.entity.Conta;

public class ContaDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nome;
	private String cpf;
	private Double saldo;
	private Integer conta;

	
	public ContaDTO() {
	}

	public ContaDTO(Conta entity) {
		id = entity.getId();
		nome = entity.getNome();
		cpf = entity.getCpf();
		saldo = entity.getSaldo();
		conta = entity.getConta();
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
	
	
}
