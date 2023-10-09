import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {IConversationWithMessages} from "../../entities/conversation/conversation.model";

@Component({
  selector: 'jhi-thread',
  templateUrl: './thread.component.html',
  styleUrls: ['./thread.component.scss']
})
export class ThreadComponent {

  @Input()
  public conversation: IConversationWithMessages | null | undefined;

  @Input()
  public loading = false;

  @Output()
  public sendMessage = new EventEmitter<string>();

  public message = '';

  onSendMessage(): void {
    if(!this.loading) {
      this.sendMessage.emit(this.message);
      this.message = '';
    }

  }
}
