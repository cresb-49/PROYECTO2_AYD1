import { RouterModule, Routes } from '@angular/router';
import { DefaultLayoutComponent } from './layout/default-layout/default-layout.component';
import { HomeComponent } from './components/home/home.component';
import { NgModule } from '@angular/core';
import { LoginComponent } from './components/login/login.component';

export const routes: Routes = [
  {
    path: '',
    component:DefaultLayoutComponent,
    children: [
      {
        path: '',component: HomeComponent
      },
      {
        path: 'home',component: HomeComponent
      },
      {
        path: 'login',component: LoginComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule {}
