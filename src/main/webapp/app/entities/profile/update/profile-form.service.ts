import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProfile, NewProfile } from '../profile.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProfile for edit and NewProfileFormGroupInput for create.
 */
type ProfileFormGroupInput = IProfile | PartialWithRequiredKeyOf<NewProfile>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProfile | NewProfile> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ProfileFormRawValue = FormValueOf<IProfile>;

type NewProfileFormRawValue = FormValueOf<NewProfile>;

type ProfileFormDefaults = Pick<NewProfile, 'id' | 'createdDate' | 'lastModifiedDate'>;

type ProfileFormGroupContent = {
  id: FormControl<ProfileFormRawValue['id'] | NewProfile['id']>;
  subscription: FormControl<ProfileFormRawValue['subscription']>;
  createdBy: FormControl<ProfileFormRawValue['createdBy']>;
  createdDate: FormControl<ProfileFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<ProfileFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<ProfileFormRawValue['lastModifiedDate']>;
  user: FormControl<ProfileFormRawValue['user']>;
};

export type ProfileFormGroup = FormGroup<ProfileFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProfileFormService {
  createProfileFormGroup(profile: ProfileFormGroupInput = { id: null }): ProfileFormGroup {
    const profileRawValue = this.convertProfileToProfileRawValue({
      ...this.getFormDefaults(),
      ...profile,
    });
    return new FormGroup<ProfileFormGroupContent>({
      id: new FormControl(
        { value: profileRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      subscription: new FormControl(profileRawValue.subscription),
      createdBy: new FormControl(profileRawValue.createdBy),
      createdDate: new FormControl(profileRawValue.createdDate),
      lastModifiedBy: new FormControl(profileRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(profileRawValue.lastModifiedDate),
      user: new FormControl(profileRawValue.user),
    });
  }

  getProfile(form: ProfileFormGroup): IProfile | NewProfile {
    return this.convertProfileRawValueToProfile(form.getRawValue() as ProfileFormRawValue | NewProfileFormRawValue);
  }

  resetForm(form: ProfileFormGroup, profile: ProfileFormGroupInput): void {
    const profileRawValue = this.convertProfileToProfileRawValue({ ...this.getFormDefaults(), ...profile });
    form.reset(
      {
        ...profileRawValue,
        id: { value: profileRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProfileFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertProfileRawValueToProfile(rawProfile: ProfileFormRawValue | NewProfileFormRawValue): IProfile | NewProfile {
    return {
      ...rawProfile,
      createdDate: dayjs(rawProfile.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawProfile.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertProfileToProfileRawValue(
    profile: IProfile | (Partial<NewProfile> & ProfileFormDefaults)
  ): ProfileFormRawValue | PartialWithRequiredKeyOf<NewProfileFormRawValue> {
    return {
      ...profile,
      createdDate: profile.createdDate ? profile.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: profile.lastModifiedDate ? profile.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
