import { NgModule } from '@angular/core';
import {CommonModule} from '@angular/common';
import {ChatComponent} from "./chat.component";
import SharedModule from "../shared/shared.module";
import {FormsModule} from "@angular/forms";
import { ThreadComponent } from './thread/thread.component';
import {InteractionComponent} from "./thread/interaction/interaction.component";
import {MarkdownModule} from "ngx-markdown";



@NgModule({
  declarations: [ChatComponent, ThreadComponent, InteractionComponent],
  imports: [
    CommonModule,
    SharedModule,
    FormsModule,
    MarkdownModule
  ]
})
export class ChatModule { }
