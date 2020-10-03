package br.com.banco.api.resources;


import java.net.URI;
import java.util.List;



import java.util.Optional;
import java.util.Random;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.banco.api.dto.ContaDTO;
import br.com.banco.api.model.entity.Conta;
import br.com.banco.api.model.repository.ContaRepository;
import br.com.banco.api.services.ContaService;
import br.com.banco.api.validacoes.ValidaCPF;

@RestController
@RequestMapping("/api/contas")
public class ContaResource {
	
	@Autowired
	private ContaService contaService;
	
	private Double limiteAbrirConta = 50.00;
	private Double limiteMaximo = 500.00;

	@GetMapping
	public ResponseEntity<List<ContaDTO>> listarContas(){
		List<ContaDTO> lista = contaService.listarContas();
		return ResponseEntity.ok().body(lista);
	}
	
	@GetMapping("/{conta}")
    public ResponseEntity<List<ContaDTO>> numeroConta(@PathVariable Integer conta){
		List<ContaDTO> numeroConta = contaService.listarPorNumeroConta(conta);
		
		if(numeroConta.size() > 0) {
			return ResponseEntity.ok().body(numeroConta);	
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada");
		}
    }
	
	@PostMapping
	public ResponseEntity<String> salvar(@RequestBody @Valid ContaDTO contaDTO){
    	
		if(contaDTO.getSaldo() < limiteAbrirConta) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente para abertura de nova conta.");
    	} else if ("".equals(contaDTO.getCpf()) || contaDTO.getCpf() == null){
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "É necessário informar um cpf para abertura de nova conta.");
    	} else if (ValidaCPF.isCPF(contaDTO.getCpf()) == false) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF informado para criação de conta está inválido.");
    	} else {
    		ContaDTO newDTO = contaService.salvar(contaDTO);
			return ResponseEntity.created(null).body("Conta cadastrada com sucesso!\n Conta Nº " + newDTO.getConta());
    	}
	}
	
	/*   
    @PutMapping("/depositar/{conta}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public Conta depositar(@Valid @RequestBody Conta conta, @PathVariable Integer id) {

        Optional<Conta> contaOptional = contaRepository.findById(id);
        
        if(contaOptional.isPresent()){
        	Conta deposito = contaOptional.get();
        	deposito.setSaldo(conta.getSaldo() + deposito.getSaldo());
            System.out.println("Depósito realizado com sucesso!");
            contaRepository.save(deposito);
            return deposito;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi possível realizar o depósito");
        }
    }
    
    @PutMapping("/sacar/{id}")
    @Transactional
    public void sacar(@Valid @RequestBody Conta saque, @PathVariable Integer id) {

        Optional<Conta> contaOptional = contaRepository.findById(id);
        
        if(contaOptional.isPresent()){
        	Conta conta = contaOptional.get();
        	
        	if(saque.getSaldo() > limiteMaximo) {
        		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Operação de saque tem um limite máximo de 500.00 por operação.");
        	}else if (saque.getSaldo() > conta.getSaldo()) {
        		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente para a operação");
        	} else {
        		conta.setSaldo(conta.getSaldo() - saque.getSaldo());
                contaRepository.save(conta);
                throw new ResponseStatusException(HttpStatus.OK, "Saque realizado com sucesso!");
        	}
        }else {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi possível realizar o saque");
        }
    }
    
    /*@PutMapping("/tranferir/{id}")
    @Transactional
    public void transferir(@Valid @RequestBody Conta transf, @PathParam("id") Integer id , @PathParam("conta") Integer conta) {

        Optional<Conta> contaSolicitante = contaRepository.findById(id);
        List<Conta> contaBeneficiario = contaRepository.findByConta(conta);
        
        if(contaSolicitante.isPresent()){
        	Conta solicitante = contaSolicitante.get();
        	Conta benenificario = contaBeneficiario.get(conta);
        	
        	if(transf.getSaldo() > limiteMaximo) {
        		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Operação de saque tem um limite máximo de 500.00 por operação.");
        	}else if (transf.getSaldo() > solicitante.getSaldo()) {
        		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente para a operação.");
        	} else {
        		solicitante.setSaldo(solicitante.getSaldo() - transf.getSaldo());
                contaRepository.save(solicitante);
                
                benenificario.setSaldo(benenificario.getSaldo() + transf.getSaldo());
                contaRepository.save(benenificario);
                
                throw new ResponseStatusException(HttpStatus.OK, "Transferência realizada com sucesso!");
        	}
        }else {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi possível realizar a transferência");
        }
    }*/
            
}
