import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IConversation, NewConversation } from '../conversation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConversation for edit and NewConversationFormGroupInput for create.
 */
type ConversationFormGroupInput = IConversation | PartialWithRequiredKeyOf<NewConversation>;

type ConversationFormDefaults = Pick<NewConversation, 'id'>;

type ConversationFormGroupContent = {
  id: FormControl<IConversation['id'] | NewConversation['id']>;
  name: FormControl<IConversation['name']>;
  publicId: FormControl<IConversation['publicId']>;
  profile: FormControl<IConversation['profile']>;
};

export type ConversationFormGroup = FormGroup<ConversationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConversationFormService {
  createConversationFormGroup(conversation: ConversationFormGroupInput = { id: null }): ConversationFormGroup {
    const conversationRawValue = {
      ...this.getFormDefaults(),
      ...conversation,
    };
    return new FormGroup<ConversationFormGroupContent>({
      id: new FormControl(
        { value: conversationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(conversationRawValue.name),
      publicId: new FormControl(conversationRawValue.publicId),
      profile: new FormControl(conversationRawValue.profile),
    });
  }

  getConversation(form: ConversationFormGroup): IConversation | NewConversation {
    return form.getRawValue() as IConversation | NewConversation;
  }

  resetForm(form: ConversationFormGroup, conversation: ConversationFormGroupInput): void {
    const conversationRawValue = { ...this.getFormDefaults(), ...conversation };
    form.reset(
      {
        ...conversationRawValue,
        id: { value: conversationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ConversationFormDefaults {
    return {
      id: null,
    };
  }
}
