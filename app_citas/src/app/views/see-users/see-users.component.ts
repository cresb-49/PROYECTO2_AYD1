import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../services/user/user.service';
import { TableAction, TableComponent, TableHeader } from '../../components/table/table.component';
import { OptionsModal, PopUpModalComponent } from '../../components/pop-up-modal/pop-up-modal.component';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule, TableComponent, PopUpModalComponent],
  selector: 'app-see-users',
  templateUrl: './see-users.component.html',
  styleUrls: ['./see-users.component.css']
})
export class SeeUsersComponent implements OnInit {
  hideModal = true;
  usuarios: any[] = [];
  tableHeaders: TableHeader[] = [
    { name: 'Nombre', key: 'name', main: true },
    { name: 'Apellido', key: 'lastname' },
    { name: 'Correo', key: 'email' }
  ]

  optionsModal:OptionsModal = {
    question: '¿Estás seguro de que deseas eliminar este usuario?',
    textYes: 'Sí, estoy seguro',
    textNo: 'No, cancelar',
    confirmAction: () => { }
  }

  data = [
    { id: 1, name: 'Juan', lastname: 'Perez', email: 'juan.perez@empresa.com', role: 'Admin' },
    { id: 2, name: 'Maria', lastname: 'Gonzalez', email: 'maria.gonzalez@empresa.com', role: 'User' },
    { id: 3, name: 'Pedro', lastname: 'Jimenez', email: 'pedro.jimenez@empresa.com', role: 'Admin' },
    { id: 4, name: 'Luis', lastname: 'Gutierrez', email: 'luis.gutierrez@empresa.com', role: 'User' },
    { id: 5, name: 'Ana', lastname: 'Martinez', email: 'ana.martinez@empresa.com', role: 'Admin' },
  ]

  actionsTable: TableAction[] = [
    { name: 'Editar', icon: 'edit', route: '/edit-user', key: 'name' },
    { name: 'Eliminar', icon: 'delete', action: (data: any) => this.openModal(data), return: true }
  ]
  constructor(private userService: UserService) { }

  ngOnInit() {
    this.obtenerUsuarios();
  }

  obtenerUsuarios() {
    // this.usuarios = this.userService.getUsers();
  }

  openModal(data: any) {
    this.hideModal = false;
    this.optionsModal.question = `¿Estás seguro de que deseas eliminar a ${data.name} ${data.lastname}?`;
    this.optionsModal.confirmAction = () => this.deteleUser(data);
  }

  deteleUser(user: any) {
    console.log('Delete user:', user);
  }

}
