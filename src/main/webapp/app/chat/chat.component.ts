import {Component, OnInit} from '@angular/core';
import {ConversationService} from "../entities/conversation/service/conversation.service";
import {IConversation} from "../entities/conversation/conversation.model";
import {map} from "rxjs/operators";
import {Alert, AlertService} from "../core/util/alert.service";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'jhi-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {

  conversationsByMonths = new Map<string, IConversation[]>();

  conversationSelected: IConversation = {id: 0};

  isEditMode = false;
  private clonedName: string | null | undefined = '';

  constructor(private conversationService: ConversationService,
              private alertService: AlertService,
              private router: Router) {
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

  isConversationSelectedNotInEditMode(id: number): boolean {
    return this.conversationSelected.id === id && !this.isEditMode;
  }

  isConversationInEditMode(id: number): boolean {
    return this.conversationSelected.id === id && this.isEditMode;
  }

  trackId = (publicId: number, item: IConversation): string => this.conversationService.getConversationPublicIdentifier(item);

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
      size: 30,
      sort: ['createdDate,desc']
    };

    this.conversationService.findForConnectedUser(queryObject)
      .pipe(
        map(res => res.body)
      )
      .subscribe(conversations => {
        this.handleFetchConversationSuccess(conversations);
      });
  }

  private handleFetchConversationSuccess(conversations: IConversation[] | null): void {
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
}
