import { Component, Input, OnInit, Output, EventEmitter, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

export interface OptionsModal {
  question: string;
  textYes: string;
  textNo: string;
  confirmAction: () => void;
}

@Component({
  standalone: true,
  imports: [CommonModule, RouterModule],
  selector: 'app-pop-up-modal',
  templateUrl: './pop-up-modal.component.html',
  styleUrls: ['./pop-up-modal.component.css']
})
export class PopUpModalComponent implements OnInit {
  @Input() hideModal = true;
  @Output() hideModalChange = new EventEmitter<boolean>(); // Added Output property
  @Input() options: OptionsModal = {
    question: 'Are you sure you want to delete this item?',
    textYes: "Yes, I'm sure",
    textNo: 'No, cancel',
    confirmAction: () => { }
  }

  constructor() { }

  ngOnInit() { }

  // Method to close the modal and emit the change
  closeModal() {
    this.hideModal = true;
    this.hideModalChange.emit(this.hideModal);
  }

  confirm() {
    this.options.confirmAction();
    this.closeModal();
  }
}
