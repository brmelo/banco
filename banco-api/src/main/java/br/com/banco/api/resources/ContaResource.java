package br.com.banco.api.resources;


import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.banco.api.dto.ContaDTO;
import br.com.banco.api.model.repository.ContaRepository;
import br.com.banco.api.services.ContaService;

@RestController
@RequestMapping("/api/contas")
public class ContaResource {
	
	@Autowired
	private ContaService contaService;
	
	@Autowired
	private ContaRepository contaRepository;
	
	private Double limiteAbrirConta = 50.00;
	//private Double limiteMaximo = 500.00;

	@GetMapping
	public ResponseEntity<List<ContaDTO>> listarContas(){
		List<ContaDTO> lista = contaService.listarContas();
		return ResponseEntity.ok().body(lista);
	}
	
	@GetMapping("/{conta}")
    public ResponseEntity<List<ContaDTO>> listarPorNumeroConta(@PathVariable Integer conta){
		List<ContaDTO> numeroConta = contaService.listarPorNumeroConta(conta);
		
		if(numeroConta.size() > 0) {
			return ResponseEntity.ok().body(numeroConta);	
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada");
		}
    }
	
	@PostMapping
	public ResponseEntity<String> salvar(@RequestBody @Valid ContaDTO contaDTO){
    	ContaDTO newDTO = contaService.salvar(contaDTO);
    	return ResponseEntity.created(null).body("Conta cadastrada com sucesso!\n Conta Nº " + newDTO.getConta());
	}
	
    @PutMapping("/depositar/{conta}")
    public ResponseEntity<String> depositar(@Valid @RequestBody ContaDTO contaDTO, @PathVariable Integer conta) {

    	Optional<List<ContaDTO>> numeroConta = Optional.of(contaService.listarPorNumeroConta(conta));
        
			if(numeroConta.isPresent()){
				ContaDTO newDTO = contaService.depositar(contaDTO, conta);
			} else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi possível realizar o depósito");
        }
			return ResponseEntity.ok().body("Depósito realizado com sucesso!");
    }
    
    @PutMapping("/sacar/{conta}")
    public ResponseEntity<String> sacar(@Valid @RequestBody ContaDTO contaDTO, @PathVariable Integer conta){
    	Optional<List<ContaDTO>> numeroConta = Optional.of(contaService.listarPorNumeroConta(conta));
    	if(numeroConta.isPresent()) {
    		ContaDTO newDTO = contaService.sacar(contaDTO, conta);
    } else {
    	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi possível realizar o saque");
    }
    	return ResponseEntity.ok().body("Saque realizado com sucesso!");
    }
    
    @PutMapping("/transferir/{conta}&{conta2")
    public ResponseEntity<String> transferir(@Valid @RequestBody ContaDTO contaDTO, @PathVariable Integer conta, @PathVariable Integer conta2){
    	Optional<List<ContaDTO>> numeroConta = Optional.of(contaService.listarPorNumeroConta(conta));
    	Optional<List<ContaDTO>> numeroConta2 = Optional.of(contaService.listarPorNumeroConta(conta2));
    	ContaDTO newDTO = contaService.transferir(contaDTO, conta, conta2);
    	return ResponseEntity.ok().body("Transferência realizada com sucesso!");
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
