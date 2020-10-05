import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ContasListaComponent } from './contas/contas-lista/contas-lista.component';
import { HomeComponent } from './home/home.component';


const routes: Routes = [
  {path : 'lista', component: ContasListaComponent}
  //{ path : 'home', component: HomeComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
