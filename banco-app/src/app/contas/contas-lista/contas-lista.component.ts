import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router'

import { ContasService } from '../../contas.service';
import { Conta } from '../contas';

@Component({
  selector: 'app-contas-lista',
  templateUrl: './contas-lista.component.html',
  styleUrls: ['./contas-lista.component.css']
})
export class ContasListaComponent implements OnInit {

  contas: Conta[] = [];
  
  constructor(
    private service: ContasService,
    private router: Router) { }

  ngOnInit(): void {
    this.service.listar().subscribe(resposta => this.contas = resposta);
  }

  cadastrar(){
    this.router.navigate(['/contas-form'])
  }

}
