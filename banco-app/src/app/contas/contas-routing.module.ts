import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ContasFormComponent } from './contas-form/contas-form.component';
import { ContasListaComponent } from './contas-lista/contas-lista.component';


const routes: Routes = [
  { path : 'contas-form', component: ContasFormComponent },
  { path : 'contas-form/:conta', component: ContasFormComponent },
  { path : 'contas-lista', component: ContasListaComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ContasRoutingModule { }
