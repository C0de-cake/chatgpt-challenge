import { NgModule } from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {ChatComponent} from "./chat.component";
import SharedModule from "../shared/shared.module";
import {FormsModule} from "@angular/forms";
import { ThreadComponent } from './thread/thread.component';
import { InteractionComponent } from './thread/interaction/interaction.component';



@NgModule({
  declarations: [ChatComponent, ThreadComponent, InteractionComponent],
  imports: [
    CommonModule,
    SharedModule,
    FormsModule,
  ]
})
export class ChatModule { }
