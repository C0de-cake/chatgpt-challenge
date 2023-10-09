import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ChatComponent} from "./chat.component";
import SharedModule from "../shared/shared.module";
import {FormsModule} from "@angular/forms";



@NgModule({
  declarations: [ChatComponent],
  imports: [
    CommonModule,
    SharedModule,
    FormsModule
  ]
})
export class ChatModule { }
