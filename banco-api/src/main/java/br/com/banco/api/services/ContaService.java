package br.com.banco.api.services;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.banco.api.dto.ContaDTO;
import br.com.banco.api.model.entity.Conta;
import br.com.banco.api.model.repository.ContaRepository;

@Service
public class ContaService {
	
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
		Conta conta = new Conta();
		conta.setNome(contaDTO.getNome());
		conta.setCpf(contaDTO.getCpf());
		conta.setSaldo(contaDTO.getSaldo());
		conta.setConta(aleatorio(100000, 999999));
		conta = contaRepository.save(conta);
		return new ContaDTO(conta);
	}
	
    public static int aleatorio(int minimo, int maximo) {
        Random random = new Random();
        return random.nextInt((maximo - minimo) + 1) + minimo;
    }

}
