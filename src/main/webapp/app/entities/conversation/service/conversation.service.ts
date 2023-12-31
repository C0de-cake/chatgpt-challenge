import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConversation, NewConversation } from '../conversation.model';

export type PartialUpdateConversation = Partial<IConversation> & Pick<IConversation, 'id'>;

type RestOf<T extends IConversation | NewConversation> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestConversation = RestOf<IConversation>;

export type NewRestConversation = RestOf<NewConversation>;

export type PartialUpdateRestConversation = RestOf<PartialUpdateConversation>;

export type EntityResponseType = HttpResponse<IConversation>;
export type EntityArrayResponseType = HttpResponse<IConversation[]>;

@Injectable({ providedIn: 'root' })
export class ConversationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/conversations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(conversation: NewConversation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(conversation);
    return this.http
      .post<RestConversation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(conversation: IConversation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(conversation);
    return this.http
      .put<RestConversation>(`${this.resourceUrl}/${this.getConversationIdentifier(conversation)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(conversation: PartialUpdateConversation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(conversation);
    return this.http
      .patch<RestConversation>(`${this.resourceUrl}/${this.getConversationIdentifier(conversation)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestConversation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestConversation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getConversationIdentifier(conversation: Pick<IConversation, 'id'>): number {
    return conversation.id;
  }

  getConversationPublicIdentifier(conversation: Pick<IConversation, 'publicId'>): string {
    return conversation.publicId!;
  }

  compareConversation(o1: Pick<IConversation, 'id'> | null, o2: Pick<IConversation, 'id'> | null): boolean {
    return o1 && o2 ? this.getConversationIdentifier(o1) === this.getConversationIdentifier(o2) : o1 === o2;
  }

  addConversationToCollectionIfMissing<Type extends Pick<IConversation, 'id'>>(
    conversationCollection: Type[],
    ...conversationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const conversations: Type[] = conversationsToCheck.filter(isPresent);
    if (conversations.length > 0) {
      const conversationCollectionIdentifiers = conversationCollection.map(
        conversationItem => this.getConversationIdentifier(conversationItem)!
      );
      const conversationsToAdd = conversations.filter(conversationItem => {
        const conversationIdentifier = this.getConversationIdentifier(conversationItem);
        if (conversationCollectionIdentifiers.includes(conversationIdentifier)) {
          return false;
        }
        conversationCollectionIdentifiers.push(conversationIdentifier);
        return true;
      });
      return [...conversationsToAdd, ...conversationCollection];
    }
    return conversationCollection;
  }

  findForConnectedUser(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestConversation[]>(`${this.resourceUrl}/for-connected-user`,
        { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  partialUpdateForConnectedUser(conversation: PartialUpdateConversation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(conversation);
    if(conversation.publicId) {
      return this.http
        .patch<RestConversation>(`${this.resourceUrl}/for-connected-user/${conversation.publicId}`, copy, { observe: 'response' })
        .pipe(map(res => this.convertResponseFromServer(res)));
    } else {
      throw new Error();
    }
  }

  deleteForConnectedUser(publicId: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/for-connected-user/${publicId}`, { observe: 'response' });
  }

  protected convertDateFromClient<T extends IConversation | NewConversation | PartialUpdateConversation>(conversation: T): RestOf<T> {
    return {
      ...conversation,
      createdDate: conversation.createdDate?.toJSON() ?? null,
      lastModifiedDate: conversation.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restConversation: RestConversation): IConversation {
    return {
      ...restConversation,
      createdDate: restConversation.createdDate ? dayjs.unix(Number(restConversation.createdDate)) : undefined,
      lastModifiedDate: restConversation.lastModifiedDate ? dayjs.unix(Number(restConversation.lastModifiedDate)) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestConversation>): HttpResponse<IConversation> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestConversation[]>): HttpResponse<IConversation[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
