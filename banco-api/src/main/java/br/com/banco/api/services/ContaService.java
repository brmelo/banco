package br.com.banco.api.services;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.banco.api.dto.ContaDTO;
import br.com.banco.api.model.entity.Conta;
import br.com.banco.api.model.repository.ContaRepository;
import br.com.banco.api.validacoes.ValidaCPF;

@Service
public class ContaService {
	
	private Double limiteAbrirConta = 50.00;
	private Double limiteMaximo = 500.00;
	
	@Autowired
	private ContaRepository contaRepository;
	
	@Transactional(readOnly = true)
	public List<ContaDTO> listarContas(){
		List<Conta> lista = contaRepository.findAll();
		return lista.stream().map(O -> new ContaDTO(O)).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public List<ContaDTO> listarPorNumeroConta(Integer conta){
		List<Conta> numeroConta = contaRepository.findByConta(conta);
		return numeroConta.stream().map(O -> new ContaDTO(O)).collect(Collectors.toList());	 
	}
	
	@Transactional
	public ContaDTO salvar(ContaDTO contaDTO) {
		
		if(contaDTO.getSaldo() < limiteAbrirConta) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente para abertura de nova conta.");
    	} else if ("".equals(contaDTO.getCpf()) || contaDTO.getCpf() == null){
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "É necessário informar um cpf para abertura de nova conta.");
    	} else if (ValidaCPF.isCPF(contaDTO.getCpf()) == false) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF informado para criação de conta está inválido.");
    	} else {
    		Conta conta = new Conta();
    		conta.setNome(contaDTO.getNome());
    		conta.setCpf(contaDTO.getCpf());
    		conta.setSaldo(contaDTO.getSaldo());
    		conta.setConta(aleatorio(100000, 999999));
    		conta.setDataCadastro(contaDTO.getDataCadastro());
    		conta = contaRepository.save(conta);
    		return new ContaDTO(conta);
    	}
		
	}
	
	@Transactional
	public ContaDTO depositar(ContaDTO contaDTO, Integer conta) {
		List<Conta> numeroConta = contaRepository.findByConta(conta);
		Conta deposito = numeroConta.get(0);
		deposito.setSaldo(contaDTO.getSaldo() + deposito.getSaldo());
		contaRepository.save(deposito);
		return new ContaDTO(deposito);
	}
	
	@Transactional
	public ContaDTO sacar(ContaDTO saque, Integer conta) {
		List<Conta> numeroConta = contaRepository.findByConta(conta);
		Conta contadb = numeroConta.get(0);
		
		if(saque.getSaldo() > limiteMaximo) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Operação de saque tem um limite máximo de 500.00 por operação.");
    	}else if (saque.getSaldo() > contadb.getSaldo()) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente para a operação");
    	} else {
    		contadb.setSaldo(contadb.getSaldo() - saque.getSaldo());
    		contaRepository.save(contadb);
    		return new ContaDTO(contadb);
		}
	}
	
	@Transactional
	public ContaDTO transferir(ContaDTO transferir, Integer conta, Integer conta2) {
		
		List<Conta> numeroConta = contaRepository.findByConta(conta);
		Conta solicitante = numeroConta.get(0);
		
		List<Conta> numeroConta2 = contaRepository.findByConta(conta2);
		Conta beneficiario = numeroConta2.get(0);
		
		solicitante.setSaldo(solicitante.getSaldo() - transferir.getSaldo());
		beneficiario.setSaldo(beneficiario.getSaldo() + transferir.getSaldo());
		
		contaRepository.save(solicitante);
		contaRepository.save(beneficiario);
		
		return new ContaDTO(solicitante);
	}
	
    public static int aleatorio(int minimo, int maximo) {
        Random random = new Random();
        return random.nextInt((maximo - minimo) + 1) + minimo;
    }

}