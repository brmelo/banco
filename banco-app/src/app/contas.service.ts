import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Conta } from './contas/contas';

import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ContasService {

  constructor(private http: HttpClient) {

  }

  salvar(conta: Conta) : Observable<Conta> {
     return this.http.post<Conta>('http://localhost:8080/api/contas/cadastrar', conta);
  }

  listar() : Observable<Conta[]>{
    return this.http.get<Conta[]>('http://localhost:8080/api/contas');
  }

  depositar(conta: Conta) : Observable<Conta> {
    return this.http.put<Conta>(`http://localhost:8080/api/depositar/${conta.conta}`, conta);
 }

}
