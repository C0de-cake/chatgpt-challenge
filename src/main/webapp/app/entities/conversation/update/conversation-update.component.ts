import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ConversationFormService, ConversationFormGroup } from './conversation-form.service';
import { IConversation } from '../conversation.model';
import { ConversationService } from '../service/conversation.service';
import { IProfile } from 'app/entities/profile/profile.model';
import { ProfileService } from 'app/entities/profile/service/profile.service';

@Component({
  standalone: true,
  selector: 'jhi-conversation-update',
  templateUrl: './conversation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ConversationUpdateComponent implements OnInit {
  isSaving = false;
  conversation: IConversation | null = null;

  profilesSharedCollection: IProfile[] = [];

  editForm: ConversationFormGroup = this.conversationFormService.createConversationFormGroup();

  constructor(
    protected conversationService: ConversationService,
    protected conversationFormService: ConversationFormService,
    protected profileService: ProfileService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProfile = (o1: IProfile | null, o2: IProfile | null): boolean => this.profileService.compareProfile(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ conversation }) => {
      this.conversation = conversation;
      if (conversation) {
        this.updateForm(conversation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const conversation = this.conversationFormService.getConversation(this.editForm);
    if (conversation.id !== null) {
      this.subscribeToSaveResponse(this.conversationService.update(conversation));
    } else {
      this.subscribeToSaveResponse(this.conversationService.create(conversation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConversation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(conversation: IConversation): void {
    this.conversation = conversation;
    this.conversationFormService.resetForm(this.editForm, conversation);

    this.profilesSharedCollection = this.profileService.addProfileToCollectionIfMissing<IProfile>(
      this.profilesSharedCollection,
      conversation.profile
    );
  }

  protected loadRelationshipsOptions(): void {
    this.profileService
      .query()
      .pipe(map((res: HttpResponse<IProfile[]>) => res.body ?? []))
      .pipe(
        map((profiles: IProfile[]) => this.profileService.addProfileToCollectionIfMissing<IProfile>(profiles, this.conversation?.profile))
      )
      .subscribe((profiles: IProfile[]) => (this.profilesSharedCollection = profiles));
  }
}
