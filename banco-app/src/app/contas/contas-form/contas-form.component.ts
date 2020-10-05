import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router'

import { Conta } from '../contas';
import { ContasService } from '../../contas.service';
import { Observable } from 'rxjs';


@Component({
  selector: 'app-contas-form',
  templateUrl: './contas-form.component.html',
  styleUrls: ['./contas-form.component.css']
})
export class ContasFormComponent implements OnInit {

  conta: Conta;
  success: boolean = false;
  errors: String[];
  numConta: number;

  constructor(
    private service: ContasService,
    private router: Router,
    private activatedRoute: ActivatedRoute
    ) { 
    this.conta = new Conta();
  }

  ngOnInit(): void {
    let params : Observable<Params> = this.activatedRoute.params
    params.subscribe(urlParams => {
      this.conta = urlParams['conta'];
      if(this.conta){
        this.service
        .depositar(this.conta)
        .subscribe(response => this.conta = response,
          errorResponse => this.conta = new Conta())
      }
      
    })
    
  }

  voltar(){
    this.router.navigate(['contas-lista'])
  }

  onSubmit(){
    this.service
      .salvar(this.conta)
      .subscribe(response => {
        this.success = true;
        this.conta = response;
        //console.log(response);
      }, errorResponse => {
        this.success = false;
        this.errors = errorResponse.error.errors;
        
      })
  }

}
