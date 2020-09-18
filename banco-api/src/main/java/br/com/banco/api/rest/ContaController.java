package br.com.banco.api.rest;


import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.banco.api.model.entity.Conta;
import br.com.banco.api.model.repository.ContaRepository;


import br.com.banco.api.validacoes.ValidaCPF;

@RestController
@RequestMapping("/api/contas")
@CrossOrigin("http://localhost:4200")
public class ContaController {
	
	private final ContaRepository contaRepository;
	
	private Double limiteAbrirConta = 50.00;
	private Double limiteMaximo = 500.00;

    @Autowired
    public ContaController(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

	@GetMapping
	public List<Conta> listarContas(){
	    return contaRepository.findAll();
	}

	@GetMapping("/{id}")
    public Conta pesquisarPorId(@PathVariable Integer id){
        return contaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente/conta não encontrada"));
    }
	
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String salvar(@RequestBody @Valid Conta conta){
    	
    	if(conta.getSaldo() < limiteAbrirConta) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente para abertura de nova conta.");
    	} else if ("".equals(conta.getCpf()) || conta.getCpf() == null){
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "É necessário informar um cpf para abertura de nova conta.");
    	} else if (ValidaCPF.isCPF(conta.getCpf()) == false) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF informado para criação de conta está inválido.");
    	} else {
    		conta.setConta(aleatorio(100000, 999999));
    		Conta c = contaRepository.save(conta);
        	return "Conta cadastrada com sucesso!\n Conta Nº " + c.getConta();
    	}
    	
    }
    
    @PutMapping("/depositar/{id}")
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

    public static int aleatorio(int minimo, int maximo) {
        Random random = new Random();
        return random.nextInt((maximo - minimo) + 1) + minimo;
    }
            
}
