import { RouterModule, Routes } from '@angular/router';
import { DefaultLayoutComponent } from './layout/default-layout/default-layout.component';
import { CleanLayoutComponent } from './layout/clean-layout/clean-layout.component';
import { HomeComponent } from './components/home/home.component';
import { NgModule } from '@angular/core';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { ForgotPasswordComponent } from './components/forgot-password/forgot-password.component';
import { SeeEmployeesComponent } from './views/see-employees/see-employees.component';
import { EditEmployeeComponent } from './views/edit-employee/edit-employee.component';
import { AdminDashboardComponent } from './views/admin-dashboard/admin-dashboard.component';
import { EditCourtComponent } from './views/edit-court/edit-court.component';
import { SeeCourtsComponent } from './views/see-courts/see-courts.component';
import { ReservarCanchaComponent } from './views/reservar-cancha/reservar-cancha.component';
import { AgendarCitaComponent } from './views/agendar-cita/agendar-cita.component';

export const routes: Routes = [
  // Rutas que usan el DefaultLayoutComponent
  {
    path: '',
    component: DefaultLayoutComponent,
    children: [
      {
        path: '', component: HomeComponent
      },
      {
        path: 'home', component: HomeComponent
      },
      {
        path: 'empleados', component: SeeEmployeesComponent
      },
      {
        path: 'canchas', component: SeeCourtsComponent
      },
      {
        path: 'edit-cancha/:id', component: EditCourtComponent
      },
      {
        path: 'edit-empleado/:id', component: EditEmployeeComponent
      },
      {
        path: 'admin-dashboard', component: AdminDashboardComponent
      },
      {
        path: 'reservar-cancha/:id', component: ReservarCanchaComponent
      },
      {
        path: 'agendar-cita/:id', component: AgendarCitaComponent
      }
    ]
  },
  // Rutas que usan el CleanLayoutComponent
  {
    path: '',
    component: CleanLayoutComponent,
    children: [
      {
        path: 'login', component: LoginComponent
      },
      {
        path: 'signup', component: SignupComponent
      },
      {
        path: 'forgot-password', component: ForgotPasswordComponent
      }
    ]
  },
  // Redireccionar cualquier otra ruta no encontrada
  {
    path: '**', redirectTo: ''
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
