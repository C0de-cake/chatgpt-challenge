import {Component, OnInit} from '@angular/core';
import {ConversationService} from "../entities/conversation/service/conversation.service";
import {IConversation, IConversationWithMessages} from "../entities/conversation/conversation.model";
import {filter, map} from "rxjs/operators";
import {Alert, AlertService} from "../core/util/alert.service";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";
import {ChatService, EntityResponseType} from "./chat.service";
import {FlowMessageQueryDTO, FlowMessageResponseDTO} from "./flow-message.model";
import {IMessage} from "../entities/message/message.model";
import dayjs from "dayjs/esm";
import {cloneDeep} from 'lodash-es';
import {KeyValue} from "@angular/common";

@Component({
  selector: 'jhi-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {

  conversationsByMonths = new Map<string, IConversation[]>();

  conversationSelected: IConversationWithMessages = {id: 0};

  isEditMode = false;
  private clonedName: string | null | undefined = '';

  public loadingMessage = false;

  constructor(private conversationService: ConversationService,
              private alertService: AlertService,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private charService: ChatService) {
  }

  ngOnInit(): void {
    this.fetchConversations();

  }

  onSelect(conversation: IConversation): void {
    this.isEditMode = false;
    this.conversationSelected = conversation;
    this.clonedName = '';
    this.router.navigate(
      ['chat'],
      {queryParams: {'id': conversation.publicId}}
    );
  }

  onClickRename(): void {
    this.isEditMode = true;
    this.clonedName = this.conversationSelected.name;
  }

  onDelete(publicId: string | null | undefined): void {
    if (publicId) {
      this.isEditMode = false;
      this.conversationService.deleteForConnectedUser(publicId).pipe(
      ).subscribe({
        next: (res) => this.handleDeleteSuccess(res),
        error: (err: HttpErrorResponse) => this.handleDeleteError(err)
      });
    }
  }

  onCancelRename(): void {
    this.conversationSelected.name = this.clonedName;
    this.isEditMode = false;
  }

  onValidateRename(conversation: IConversation): void {
    this.conversationService.partialUpdateForConnectedUser(conversation)
      .pipe(map(res => res.body))
      .subscribe({
        next: () => this.handleSuccessRename(),
        error: (err: HttpErrorResponse) => this.handleErrorRename(err)
      });
  }

  isConversationSelectedNotInEditMode(publicId: string | null | undefined): boolean {
    return this.conversationSelected.publicId === publicId && !this.isEditMode;
  }

  isConversationInEditMode(publicId: string | null | undefined): boolean {
    return this.conversationSelected.publicId === publicId && this.isEditMode;
  }

  trackId = (publicId: number, item: IConversation): string => this.conversationService.getConversationPublicIdentifier(item);

  sortByMonth = (a: KeyValue<string, IConversation[]>, b: KeyValue<string, IConversation[]>): number =>
    dayjs(b.key, 'MMMM').toDate().getTime() - dayjs(a.key, 'MMMM').toDate().getTime();

  onNewConversation(): void {
    this.conversationSelected = {id: 0};
    this.router.navigate(['chat']);
  }

  onSendMessage(newMessage: string): void {
    this.loadingMessage = true;
    const newMessageQuery: FlowMessageQueryDTO = {
      content: newMessage,
      newConversation: this.conversationSelected.id === 0,
      conversationPublicId: this.conversationSelected.id !== 0 ? this.conversationSelected.publicId : null
    }
    this.charService.sendMessage(newMessageQuery)
      .subscribe({
          next: (res) => this.onMessageSuccess(res, newMessage),
          error: (err: HttpErrorResponse) => this.onErrorSendMessage(err)
        }
      )
  }

  private handleDeleteError(err: HttpErrorResponse): void {
    const alert: Alert = {
      translationKey: 'chatgptChallengeApp.chat.alert.delete.error',
      translationParams: {error: err.message},
      type: 'danger', toast: true
    };
    this.alertService.addAlert(alert);
  }

  private handleDeleteSuccess(res: HttpResponse<{}>): void {
    if (res.ok) {
      const alert: Alert = {
        translationKey: 'chatgptChallengeApp.chat.alert.delete.success',
        type: 'success', toast: true
      };
      this.alertService.addAlert(alert);
      this.fetchConversations();
    }
  }

  private handleErrorRename(err: HttpErrorResponse): void {
    const alert: Alert = {
      translationKey: 'chatgptChallengeApp.chat.alert.rename.error',
      translationParams: {error: err.message},
      type: 'danger', toast: true
    };
    this.alertService.addAlert(alert);
    this.isEditMode = false;
    this.clonedName = this.conversationSelected.name;
  }

  private handleSuccessRename(): void {
    const alert: Alert = {
      translationKey: 'chatgptChallengeApp.chat.alert.rename.success',
      type: 'success', toast: true
    };
    this.alertService.addAlert(alert);
    this.isEditMode = false;
    this.clonedName = this.conversationSelected.name;
  }

  private fetchConversations(): void {
    const queryObject: any = {
      page: 0,
      size: 100,
      sort: ['createdDate,desc']
    };

    this.conversationService.findForConnectedUser(queryObject)
      .pipe(
        map(res => res.body)
      )
      .subscribe(conversations => {
        this.handleFetchConversationSuccess(conversations);
        this.loadConversationInURL(conversations);
      });
  }

  private handleFetchConversationSuccess(conversations: IConversationWithMessages[] | null): void {
    if (conversations !== null) {
      this.conversationsByMonths.clear();
      for (const conversation of conversations) {
        if (conversation.createdDate) {
          const month = conversation.createdDate.format('MMMM');
          if (this.conversationsByMonths.has(month)) {
            this.conversationsByMonths.get(month)?.push(conversation);
          } else {
            const conversationsSpecificMonth = new Array<IConversation>();
            conversationsSpecificMonth.push(conversation);
            this.conversationsByMonths.set(month, conversationsSpecificMonth)
          }
        }
      }
    }
  }

  private loadConversationInURL(conversations: IConversationWithMessages[] | null): void {
    if (conversations !== null) {
      this.activatedRoute.queryParamMap.pipe(
        filter(params => params.has('id')),
        map(params => params.get('id'))
      ).subscribe(id => {
        const iConversationWithMessages =
          conversations.filter(conv => conv.publicId === id);
        if (iConversationWithMessages.length !== 0) {
          this.conversationSelected = iConversationWithMessages[0]
        }
      });
    }
  }

  private onErrorSendMessage(err: HttpErrorResponse): void {
    this.loadingMessage = false;
    const alert: Alert = {
      translationKey: 'chatgptChallengeApp.chat.alert.send.error',
      translationParams: {error: err.message},
      type: 'danger', toast: true
    };
    this.alertService.addAlert(alert);
  }

  private onMessageSuccess(res: EntityResponseType, messageContentFromUser: string): void {
    this.loadingMessage = false;
    const flowMessageResponse = res.body;
    console.dir(flowMessageResponse);
    if (flowMessageResponse?.conversation) {
      this.mapNewConversationAndMessagesFromAPI(flowMessageResponse, messageContentFromUser);
      this.putNewConversationInCurrentMonth();
      this.onSelect(flowMessageResponse.conversation as IConversationWithMessages)
    } else {
      this.putMessageInExistingConversation(flowMessageResponse, messageContentFromUser);
    }
  }

  private putMessageInExistingConversation(flowMessageResponse: FlowMessageResponseDTO | null, messageContentFromUser: string): void {
    for (const conversationsEntry of this.conversationsByMonths.entries()) {
      for (const conversation of conversationsEntry[1]) {
        console.dir(flowMessageResponse?.conversationPublicId);
        if (conversation.publicId === flowMessageResponse?.conversationPublicId) {
          if (flowMessageResponse && this.conversationSelected.messages) {
            const messageGPT: IMessage = {id: 1, content: flowMessageResponse.content, owner: "GPT"};
            const messageUser: IMessage = {id: 0, content: messageContentFromUser, owner: "USER"}
            this.conversationSelected.messages.push(messageUser);
            this.conversationSelected.messages.push(messageGPT);
          }
        }
      }
    }
  }

  private putNewConversationInCurrentMonth(): void {
    const currentMonth = dayjs().format('MMMM');
    if (this.conversationsByMonths.has(currentMonth)) {
      this.conversationsByMonths.get(currentMonth)?.unshift(cloneDeep(this.conversationSelected));
    } else {
      const conversationsSpecificMonth = new Array<IConversation>();
      conversationsSpecificMonth.push(cloneDeep(this.conversationSelected));
      this.conversationsByMonths.set(currentMonth, conversationsSpecificMonth);
    }
  }

  private mapNewConversationAndMessagesFromAPI(flowMessageResponse: FlowMessageResponseDTO,
                                               messageContentFromUser: string): void {
    this.conversationSelected = flowMessageResponse.conversation as IConversationWithMessages;
    this.conversationSelected.messages = new Array<IMessage>();
    const messageUser: IMessage = {id: 0, content: messageContentFromUser, owner: "USER"}
    const messageGPT: IMessage = {id: 1, content: flowMessageResponse.content, owner: "GPT"};
    this.conversationSelected.messages.push(messageUser);
    this.conversationSelected.messages.push(messageGPT);
  }
}
