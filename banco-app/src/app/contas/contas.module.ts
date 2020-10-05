import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ContasRoutingModule } from './contas-routing.module';
import { ContasFormComponent } from './contas-form/contas-form.component';
import { ContasListaComponent } from './contas-lista/contas-lista.component';


@NgModule({
  declarations: [ContasFormComponent, ContasListaComponent],
  imports: [
    CommonModule,
    ContasRoutingModule,
    FormsModule
    
  ], exports:[
    ContasFormComponent,
    ContasListaComponent
  ]
})
export class ContasModule { }
